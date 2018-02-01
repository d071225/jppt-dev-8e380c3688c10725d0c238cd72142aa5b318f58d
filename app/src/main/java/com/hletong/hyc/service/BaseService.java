package com.hletong.hyc.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.Toast;

import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.architect.view.IBroadcastDataView;
import com.hletong.mob.architect.view.IReceiverView;
import com.hletong.mob.util.DelegateBroadcastReceiver;

/**
 * Created by dongdaqing on 2017/7/31.
 */

public abstract class BaseService extends Service implements IBaseView, IReceiverView, IBroadcastDataView {
    private BroadcastReceiver mReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void showLoading() {
        //do nothing
    }

    @Override
    public void hideLoading() {
        //do nothing
    }

    public void showMessage(String message) {
        if (message != null)
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void handleError(@NonNull BaseError error) {
        showMessage(error.getMessage());
    }

    @Override
    public void register(String... params) {
        if (params == null)
            return;
        IntentFilter filter = new IntentFilter();
        for (int i = 0; i < params.length; i++) {
            filter.addAction(params[i]);
        }

        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
            unregisterReceiver(mReceiver);
        } else {
            mReceiver = new DelegateBroadcastReceiver(this);
        }

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onReceive(Context context, String action, Intent intent) {

    }

    @Override
    public void onDestroy() {
        if (mReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
            unregisterReceiver(mReceiver);
        }
        super.onDestroy();
    }

    @Override
    public final void broadcast(Intent intent) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
