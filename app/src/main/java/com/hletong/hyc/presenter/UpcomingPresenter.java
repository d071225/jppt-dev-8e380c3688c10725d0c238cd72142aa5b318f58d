package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.hletong.hyc.contract.UpcomingContract;
import com.hletong.hyc.model.Receipt;
import com.hletong.hyc.model.Upcoming;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.error.BusiError;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.model.MixItemSpec;
import com.hletong.mob.architect.model.repository.SimpleCallback;
import com.hletong.mob.architect.presenter.ListPresenter;
import com.hletong.mob.architect.requestvalue.ListRequestValue;
import com.hletong.mob.architect.requestvalue.MixListRequestValue;
import com.hletong.mob.util.ParamsHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ddq on 2016/12/23.
 * 待办任务
 */
public class UpcomingPresenter extends ListPresenter<Object> implements UpcomingContract.Presenter {
    private ArrayList<MixItemSpec> mMixItemSpecs;
    private UpcomingContract.View mView;
    private UpcomingContract.Action tmp;

    public UpcomingPresenter(UpcomingContract.View view, UpcomingContract.LocalDataSource dataSource) {
        super(dataSource, view);
        this.mView = view;
        mMixItemSpecs = new ArrayList<>();
        mMixItemSpecs.add(new MixItemSpec("\"type\":", new TypeToken<Upcoming>() {
        }));
        mMixItemSpecs.add(new MixItemSpec("\"todoType\":", new TypeToken<Receipt>() {
        }));
    }

    @Override
    public void loadList(ListRequestValue<Object> mRequestValue, boolean refresh) {
        setRefresh(refresh);
        MixListRequestValue mMixListRequestValue;
        if (mRequestValue instanceof MixListRequestValue) {
            mMixListRequestValue = (MixListRequestValue) mRequestValue;
        } else {
            mMixListRequestValue = new MixListRequestValue(mRequestValue);
        }

        mMixListRequestValue.setItemSpecs(mMixItemSpecs);
        mMixListRequestValue.setRefresh(refresh);
        getDataRepository().loadMixList(mMixListRequestValue, new SimpleCallback<List<Object>>(getView(), true) {
            @Override
            public void onSuccess(@NonNull List<Object> response) {
                getView().showList(response, isRefresh());
            }
        });
    }

    @Override
    public void collect(String tradeUuid, boolean collected) {
        if (tradeUuid == null) {
            handleMessage("参数出错");
            return;
        }

        tmp = new UpcomingContract.Action(tradeUuid, !collected);
        simpleSubmit(
                collected ? Constant.getUrl(Constant.DEL_FROM_COLLECTION) : Constant.getUrl(Constant.ADD_TO_COLLECTION),
                new ParamsHelper().put("tradeUuid", tradeUuid)
        );
    }

    @Override
    protected void simpleSubmitSucceed(CommonResult cr) {
//        UpcomingContract.LocalDataSource dataSource = (UpcomingContract.LocalDataSource) getDataRepository();
//        dataSource.addToCollection(tmp);
        mView.success(tmp);
    }

    @Override
    protected void simpleSubmitFailed(BaseError error) {
        if (error instanceof BusiError) {
            BusiError busiError = (BusiError) error;
            //重复收藏，这种情况也正常返回
            if (busiError.getBusiCode() == 10) {
                mView.success(tmp);
                return;
            }
        }
        super.simpleSubmitFailed(error);
    }

    @Override
    public boolean isCollected(String tradeUuid) {
        UpcomingContract.LocalDataSource dataSource = (UpcomingContract.LocalDataSource) getDataRepository();
        return dataSource.isCollected(tradeUuid);
    }
}
