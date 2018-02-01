package com.hletong.mob.architect.model.repository;

import android.support.annotation.NonNull;

import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.view.IBaseView;

/**
 * Created by dongdaqing on 2017/4/25.
 */

public abstract class SimpleCallback<R> extends DataCallback<R> {
    private IBaseView mBaseView;

    public SimpleCallback(@NonNull IBaseView baseView) {
        this(baseView, false);
    }

    public SimpleCallback(@NonNull IBaseView baseView, boolean withoutProgress) {
        super(withoutProgress ? null : baseView);
        mBaseView = baseView;
    }

    @Override
    public final void onError(@NonNull BaseError error) {
        mBaseView.handleError(error);
    }
}
