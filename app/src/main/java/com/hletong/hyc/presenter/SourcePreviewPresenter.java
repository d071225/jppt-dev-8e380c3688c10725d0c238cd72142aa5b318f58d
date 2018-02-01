package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;

import com.hletong.hyc.contract.SourcePreviewContract;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.model.repository.SimpleCallback;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.util.ParamsHelper;

import java.util.HashMap;

/**
 * Created by ddq on 2017/3/24.
 */

public class SourcePreviewPresenter extends FilePresenter<SourcePreviewContract.View> implements SourcePreviewContract.Presenter {
    private Source mSource;
    private String cargoUuid;

    public SourcePreviewPresenter(SourcePreviewContract.View view, Source source, String cargoUuid) {
        super(view);
        mSource = source;
        this.cargoUuid = cargoUuid;
    }

    @Override
    public void start() {
        if (mSource != null){
            getView().initWithSource(mSource);
        }else {
            HashMap<String, String> map = new HashMap<>(1);
            map.put("cargoUuid", cargoUuid);
            //加载货源详情
            ItemRequestValue<Source> requestValue = new ItemRequestValue<Source>(
                    getView().hashCode(), Constant.getUrl(Constant.COPY_CARGO_INFO),
                    new ParamsHelper(map)){};
            getDataRepository().loadItem(requestValue, new SimpleCallback<Source>(getView()) {
                @Override
                public void onSuccess(@NonNull Source response) {
                    mSource = response;
                    getView().initWithSource(response);
                }
            });
        }
    }

    @Override
    public void revoke() {
        simpleSubmit(
                Constant.getUrl(Constant.CARGO_REVOKE),
                new ParamsHelper().put("cargoUuid",cargoUuid)
        );
    }
}
