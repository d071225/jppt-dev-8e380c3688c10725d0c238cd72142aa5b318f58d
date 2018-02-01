package com.hletong.mob.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.hletong.mob.architect.view.IReceiverView;

/**
 * Created by dongdaqing on 2017/7/31.
 */

public final class DelegateBroadcastReceiver extends BroadcastReceiver {
    private IReceiverView mReceiveView;

    public DelegateBroadcastReceiver(IReceiverView receiveView) {
        mReceiveView = receiveView;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mReceiveView.onReceive(context, intent.getAction(), intent);
    }
}
