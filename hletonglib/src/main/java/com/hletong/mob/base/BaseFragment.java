package com.hletong.mob.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;

import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.architect.view.IBroadcastDataView;
import com.hletong.mob.architect.view.ICommitSuccessView;
import com.hletong.mob.architect.view.IDialogView;
import com.hletong.mob.architect.view.IReceiverView;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.dialog.AlertDialog;
import com.hletong.mob.util.DelegateBroadcastReceiver;
import com.hletong.mob.util.FinishOptions;
import com.xcheng.view.controller.EasyFragment;
import com.xcheng.view.util.JumpUtil;

/**
 * 基础Fragment，提供公有方法
 * 缓存RootView
 *
 * @author xincheng @date:2014-8-4
 */
public abstract class BaseFragment extends EasyFragment implements IBaseView, ITransactionView, IBroadcastDataView, IDialogView, IReceiverView, ICommitSuccessView {

    private BroadcastReceiver mReceiver;

    @Override
    public void onDestroyView() {
        if (mReceiver != null)
            LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
        super.onDestroyView();
    }

    public BaseActivity getBaseActivity() {
        return (BaseActivity) getActivity();
    }

    @Override
    public void handleError(@NonNull BaseError mError) {
        showMessage(mError.getMessage());
    }

    public void setTitle(String title) {
        getBaseActivity().setCustomTitle(title);
    }

    @Override
    public final void finishWithOptions(FinishOptions options) {
        ITransactionView view = (ITransactionView) getActivity();
        view.finishWithOptions(options);
    }

    @Override
    public final void toActivity(Class cls, Bundle bundle, FinishOptions finishOptions) {
        ITransactionView view = (ITransactionView) getActivity();
        view.toActivity(cls, bundle, finishOptions);
    }

    @Override
    public final void toActivity(Intent intent, FinishOptions options) {
        ITransactionView view = (ITransactionView) getActivity();
        view.toActivity(intent, options);
    }

    @Override
    public final void toActivity(Class cls, Bundle bundle, int requestCode, FinishOptions options) {
        JumpUtil.toActivityForResult(this, cls, requestCode, bundle);
        if (options != null) {
            if (options.isForwardResult()) {
                getActivity().setResult(options.getResultCode(), options.getData());
            }
            getActivity().finish();
        }
    }

    @Override
    public final void toActivity(Intent intent, int requestCode, FinishOptions options) {
        startActivityForResult(intent, requestCode);
        if (options != null) {
            if (options.isForwardResult()) {
                getActivity().setResult(options.getResultCode(), options.getData());
            }
            getActivity().finish();
        }
    }

    @Override
    public final void broadcast(Intent intent) {
        getBaseActivity().broadcast(intent);
    }

    @Override
    public final void showDialog(boolean cancelable, String title, String content, String p, String n, DialogInterface.OnClickListener pl, DialogInterface.OnClickListener nl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(cancelable)
                .setTitle(title)
                .setContent(content)
                .setPositiveButton(p, pl)
                .setNegativeButton(n, nl)
                .show();
    }

    @Override
    public final void showDialog(String title, String content, String p, DialogInterface.OnClickListener pl) {
        showDialog(true, title, content, p, "取消", pl, null);
    }

    @Override
    public final void showDialog(String title, String content, DialogInterface.OnClickListener pl) {
        showDialog(true, title, content, "确定", "取消", pl, null);
    }

    @Override
    public final void register(String... params) {
        if (getActivity() == null)
            throw new IllegalStateException("not attached to activity");

        if (params == null) return;
        IntentFilter filter = new IntentFilter();
        for (String param : params) {
            filter.addAction(param);
        }
        if (mReceiver != null)
            LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiver);
        else
            mReceiver = new DelegateBroadcastReceiver(this);
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiver, filter);
    }

    @Override
    public void onReceive(Context context, String action, Intent intent) {

    }

    //should be final
    @Override
    public void success(String message) {
        showMessage(message);
    }
}
