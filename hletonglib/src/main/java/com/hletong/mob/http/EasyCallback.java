package com.hletong.mob.http;

import android.support.annotation.NonNull;

import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.view.IBaseView;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.OkCall;

/**
 * Created by chengxin on 2017/4/25.
 */
public abstract class EasyCallback<T> extends AnimCallBack<T> {
    private IBaseView mBaseView;

    public EasyCallback(@NonNull IBaseView baseView) {
        this(baseView, false);
    }

    public EasyCallback(@NonNull IBaseView baseView, boolean withoutProgress) {
        super(withoutProgress ? null : baseView);
        mBaseView = baseView;
    }

    @Override
    public void onError(OkCall<T> okCall, EasyError error) {
        mBaseView.handleError((BaseError) error);
    }
}
