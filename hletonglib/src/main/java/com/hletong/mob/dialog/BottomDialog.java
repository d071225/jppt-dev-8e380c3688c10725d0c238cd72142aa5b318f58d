package com.hletong.mob.dialog;


import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;

import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.net.OkHttpUtil;

import org.greenrobot.eventbus.EventBus;

public abstract class BottomDialog extends com.xcheng.view.controller.dialog.BottomDialog implements IBaseView {

    public BottomDialog(Context context) {
        super(context);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        OkHttpUtil.cancel(hashCode());
    }

    public void toActivity(Class<?> cls, Bundle bundle) {
        Intent intent = new Intent(getActivity(), cls);
        if (bundle != null) {
            intent.putExtras(bundle);
        }
        getActivity().startActivity(intent);
    }

    public void handleError(@NonNull BaseError error) {
        showMessage(error.getMessage());
    }


    public Resources getResources() {
        return getContext().getResources();
    }

    @NonNull
    public final String getString(@StringRes int resId) {
        return getResources().getString(resId);
    }
}
