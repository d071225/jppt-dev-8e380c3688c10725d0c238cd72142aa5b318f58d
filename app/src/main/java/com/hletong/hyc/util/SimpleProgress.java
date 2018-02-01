package com.hletong.hyc.util;

import android.content.Context;

import com.hletong.mob.architect.view.IProgress;
import com.xcheng.view.controller.dialog.LoadingDialog;

/**
 * Created by ddq on 2017/2/16.
 */

public class SimpleProgress implements IProgress {
    private LoadingDialog mLoadingDialog;

    public SimpleProgress(Context context) {
        mLoadingDialog = new LoadingDialog(context);
    }

    @Override
    public void showLoading() {
        mLoadingDialog.show();
    }

    @Override
    public void hideLoading() {
        mLoadingDialog.dismiss();
    }
}
