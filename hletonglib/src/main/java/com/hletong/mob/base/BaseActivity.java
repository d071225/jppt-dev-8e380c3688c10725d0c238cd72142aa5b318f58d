package com.hletong.mob.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.support.v4.content.LocalBroadcastManager;
import android.widget.EditText;
import android.widget.TextView;

import com.hletong.mob.annotation.LoginAction;
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
import com.hletong.mob.util.SPUtils;
import com.hletong.mob.util.StringUtil;
import com.xcheng.view.controller.EasyActivity;
import com.xcheng.view.util.JumpUtil;

import java.math.BigDecimal;

/**
 * 所有Activity的基类
 *
 * @author xincheng
 */
public abstract class BaseActivity extends EasyActivity implements IBaseView, ITransactionView, IBroadcastDataView, IDialogView, IReceiverView, ICommitSuccessView {
    private BroadcastReceiver mReceiver;

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            if (ThemeManager.isThemeChanged(mCurrentThemeId)) {
                recreate();
            }
        }
    }
    @Override
    protected void onDestroy() {
        if (mReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onDestroy();
    }

    /**
     * 会有很多页面跳转到登录页面，故在此BaseActivity中定义
     * 获取传递到登录页面的行为Bundle
     */
    public Bundle createLoginBundle(@LoginAction int loginAction) {
        Bundle bundle = new Bundle();
        bundle.putInt(LoginAction.KEY_LOGIN_ACTION, loginAction);
        return bundle;
    }

    /**
     * ================================View相关操作==============================================
     */
    public String getTextValue(TextView textView) {
        return textView.getText().toString();
    }

    public String getTextValue(@IdRes int textViewId) {
        TextView et = (TextView) findViewById(textViewId);
        return getTextValue(et);
    }

    /**
     * 设置TextView的数据
     *
     * @param textViewId
     * @param text
     */
    public void setText(@IdRes int textViewId, CharSequence text) {
        TextView textView = (TextView) findViewById(textViewId);
        textView.setText(text);
    }

    public BigDecimal getEditTextDecimal(EditText editText) {
        return new BigDecimal(editText.getText().toString());
    }

    /**
     * 判断是不是空白数据
     *
     * @param editText
     * @return
     */
    public boolean isTrimBlank(EditText editText) {
        return StringUtil.isTrimBlank(getTextValue(editText));
    }

    @Override
    public void handleError(@NonNull BaseError mError) {
        showMessage(mError.getMessage());
    }

    @StyleRes
    private int mCurrentThemeId = 0;

    @Override
    public void setTheme(@StyleRes int resid) {
        if (ThemeManager.isThemeChanged(resid)) {
            int savedThemeIndex = ThemeManager.getThemeIndex();
            resid = ThemeManager.THEMES[savedThemeIndex];
        }
        mCurrentThemeId = resid;
        super.setTheme(resid);
    }

    @Override
    public final void finishWithOptions(FinishOptions options) {
        if (options != null && options.isForwardResult()) {
            setResult(options.getResultCode(), options.getData());
        }
        finish();
    }

    @Override
    public final void toActivity(Class cls, Bundle bundle, FinishOptions options) {
        JumpUtil.toActivity(this, cls, bundle);
        if (options != null) {
            if (options.isForwardResult()) {
                setResult(options.getResultCode(), options.getData());
            }
            finish();
        }
    }

    @Override
    public final void toActivity(Intent intent, FinishOptions options) {
        startActivity(intent);
        if (options != null) {
            if (options.isForwardResult()) {
                setResult(options.getResultCode(), options.getData());
            }
            finish();
        }
    }

    @Override
    public final void toActivity(Class cls, Bundle bundle, int requestCode, FinishOptions options) {
        JumpUtil.toActivityForResult(this, cls, requestCode, bundle);
        if (options != null) {
            if (options.isForwardResult()) {
                setResult(options.getResultCode(), options.getData());
            }
            finish();
        }
    }

    @Override
    public final void toActivity(Intent intent, int requestCode, FinishOptions options) {
        startActivityForResult(intent, requestCode);
        if (options != null) {
            if (options.isForwardResult()) {
                setResult(options.getResultCode(), options.getData());
            }
            finish();
        }
    }

    @Override
    public final void broadcast(Intent intent) {
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }

    @Override
    public final void showDialog(boolean cancelable, String title, String content, String p, String n, DialogInterface.OnClickListener pl, DialogInterface.OnClickListener nl) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        if (params == null) return;
        IntentFilter filter = new IntentFilter();
        for (int i = 0; i < params.length; i++) {
            filter.addAction(params[i]);
        }
        if (mReceiver != null)
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        else
            mReceiver = new DelegateBroadcastReceiver(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);
    }

    @Override
    public void onReceive(Context context, String action, Intent intent) {

    }

    //should be final
    @Override
    public void success(String message) {
        showMessage(message);
    }

    public static class ThemeManager {
        /*当前app所有的主题**/
        public static int[] THEMES;

        @StyleRes
        public static int getThemeIndex() {
            return SPUtils.getInt("savedThemeIndex", -1);
        }

        /**
         * 判断字体设置改变后返回到对应页面判断是否需要重启改变字体大小
         **/
        public static boolean isThemeChanged(int currentThemeId) {
            int savedThemeIndex = ThemeManager.getThemeIndex();
            if (savedThemeIndex >= 0) {
                if (THEMES != null && savedThemeIndex < THEMES.length) {
                    return THEMES[savedThemeIndex] != currentThemeId;
                }
            }
            return false;
        }

        public static void setThemeIndex(Activity context, int themeIndex) {
            setThemeIndex(context, themeIndex, false);
        }

        @SuppressLint("CommitPrefEdits")
        public static void setThemeIndex(Activity context, int themeIndex, boolean isRecreate) {
            if (themeIndex >= 0) {
                SPUtils.putInt("savedThemeIndex", themeIndex);
                if (isRecreate) {
                    context.recreate();
                }
            }
        }
    }
}
