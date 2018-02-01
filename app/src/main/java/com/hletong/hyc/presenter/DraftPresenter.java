package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;

import com.hletong.hyc.contract.DraftContract;
import com.hletong.hyc.model.Draft;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.model.repository.DataCallback;
import com.hletong.mob.architect.presenter.ListPresenter;
import com.hletong.mob.architect.requestvalue.ListRequestValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ddq on 2017/4/6.
 * 草稿
 */

public class DraftPresenter extends ListPresenter<Draft> implements DraftContract.Presenter {
    private DraftContract.View mView;
    private DataCallback<CommonResult> mCallback = new DataCallback<CommonResult>() {
        @Override
        public void onSuccess(@NonNull CommonResult response) {
            mView.success(response.getErrorInfo());
        }

        @Override
        public void onError(@NonNull BaseError error) {
            mView.handleError(error);
        }
    };

    public DraftPresenter(DraftContract.View baseView, DraftContract.LocalDataSource dataSource) {
        super(dataSource, baseView);
        this.mView = baseView;
    }

    @Override
    public void loadList(ListRequestValue<Draft> requestValue, boolean refresh) {
        setRefresh(refresh);
        DraftContract.LocalDataSource dataSource = (DraftContract.LocalDataSource) getDataRepository();
        dataSource.loadList(requestValue.getParams().getInt("start") - 1, requestValue.getParams().getInt("limit"), new DataCallback<List<Draft>>() {
            @Override
            public void onSuccess(@NonNull List<Draft> response) {
                mView.showList(response, isRefresh());
            }

            @Override
            public void onError(@NonNull BaseError error) {
                error.setId(isRefresh() ? ListRequestValue.REFRESH : ListRequestValue.LOAD_MORE);
                mView.handleError(error);
            }
        });
    }

    @Override
    public void modify(Draft draft) {
        DraftContract.LocalDataSource dataSource = (DraftContract.LocalDataSource) getDataRepository();
        dataSource.modify(draft, mCallback);
    }

    @Override
    public void delete(ArrayList<Draft> sources) {
        DraftContract.LocalDataSource dataSource = (DraftContract.LocalDataSource) getDataRepository();
        dataSource.delete(sources, mCallback);
    }
}
