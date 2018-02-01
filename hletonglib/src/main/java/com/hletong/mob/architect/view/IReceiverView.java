package com.hletong.mob.architect.view;

import android.content.Context;
import android.content.Intent;

/**
 * Created by dongdaqing on 2017/7/31.
 * 广播基础方法，Activity和Fragment中使用{@link android.support.v4.content.LocalBroadcastManager}
 * 实现，Service中使用全局广播实现
 */

public interface IReceiverView {
    /**
     * 注册广播
     * @param params 要注册的actions
     */
    void register(String... params);

    /**
     * 注册的广播的回调函数，同BroadcastReceiver中的onReceive()函数
     * @param context
     * @param action 通过intent.getAction()获得
     * @param intent
     */
    void onReceive(Context context, String action, Intent intent);
}
