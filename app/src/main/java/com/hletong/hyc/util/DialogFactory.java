package com.hletong.hyc.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;

import com.hletong.hyc.R;
import com.hletong.mob.dialog.AlertDialog;

import java.util.List;

/**
 * Created by ddq on 2016/11/2.
 */

public class DialogFactory {

    public static void showAlert(Context context, int title, int message) {
        showAlert(context, context.getString(title), context.getString(message), context.getString(R.string.ok));
    }

    public static void showAlert(Context context, CharSequence title, CharSequence message) {
        showAlert(context, title, message, context.getString(R.string.ok));
    }

    public static void showAlert(Context context, int title, int message, int positiveText) {
        showAlert(context, context.getString(title), context.getString(message), context.getString(positiveText));
    }

    public static void showAlert(Context context, CharSequence title, CharSequence message, CharSequence positiveText) {
        showAlert(context, title, message, positiveText, true, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }

    public static void showAlert(Context context, int title, int message, DialogInterface.OnClickListener listener) {
        showAlert(context, context.getString(title), context.getString(message), context.getString(R.string.ok), true, listener);
    }

    /**
     * 是否介意cancel
     **/
    public static void showAlert(Context context, int title, int message, boolean cancelable, DialogInterface.OnClickListener listener) {
        showAlert(context, context.getString(title), context.getString(message), context.getString(R.string.ok), cancelable, listener);
    }

    public static void showAlert(Context context, CharSequence title, CharSequence message, boolean cancelable, DialogInterface.OnClickListener listener) {
        showAlert(context, title, message, context.getString(R.string.ok), cancelable, listener);
    }

    public static void showAlert(Context context, CharSequence title, CharSequence message, DialogInterface.OnClickListener listener) {
        showAlert(context, title, message, context.getString(R.string.ok), true, listener);
    }

    public static void showAlert(Context context, int title, int message, int positiveText, DialogInterface.OnClickListener listener) {
        showAlert(context, context.getString(title), context.getString(message), context.getString(positiveText), true, listener);
    }

    public static void showAlert(Context context, CharSequence title, CharSequence message, CharSequence positiveText, boolean cancelable, DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setContent(message)
                .setPositiveButton(positiveText, listener)
                .setCancelable(cancelable)
                .show();
    }

    public static void showAlertWithNegativeButton(Context context, int title, int message, DialogInterface.OnClickListener listener) {
        showAlertWithNegativeButton(context, false, context.getString(title), context.getString(message), context.getString(R.string.ok), listener, context.getString(R.string.cancel), null);
    }

    public static void showAlertWithNegativeButton(Context context, CharSequence title, CharSequence message, DialogInterface.OnClickListener listener) {
        showAlertWithNegativeButton(context, false, title, message, context.getString(R.string.ok), listener, context.getString(R.string.cancel), null);
    }

    public static void showAlertWithNegativeButton(Context context, CharSequence title, CharSequence message, DialogInterface.OnClickListener pl, DialogInterface.OnClickListener nl) {
        showAlertWithNegativeButton(context, false, title, message, pl, nl);
    }

    public static void showAlertWithNegativeButton(Context context, boolean cancelable, CharSequence title, CharSequence message, DialogInterface.OnClickListener pl, DialogInterface.OnClickListener nl) {
        showAlertWithNegativeButton(context, cancelable, title, message, context.getString(R.string.ok), pl, context.getString(R.string.cancel), nl);
    }

    public static void showAlertWithNegativeButton(Context context, boolean cancelable, CharSequence title, CharSequence message,
                                                   CharSequence positiveString, DialogInterface.OnClickListener positiveListener,
                                                   CharSequence negativeString, DialogInterface.OnClickListener negativeListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setContent(message)
                .setCancelable(cancelable)
                .setPositiveButton(positiveString, positiveListener)
                .setNegativeButton(negativeString, negativeListener)
                .show();
    }

    public static void permissionSetting(final Activity activity, @NonNull List<String> deniedPerms) {
        //找到被拒绝的
        String message = "";
        for (String temp : deniedPerms) {
            switch (temp) {
                case Manifest.permission.READ_EXTERNAL_STORAGE:
                case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                    message += "存储、";
                    break;
                case Manifest.permission.CALL_PHONE:
                case Manifest.permission.READ_PHONE_STATE:
                    message += "电话、";
                    break;
                case Manifest.permission.CAMERA:
                    message += "相机、";
                    break;
                case Manifest.permission.ACCESS_FINE_LOCATION:
                case Manifest.permission.ACCESS_COARSE_LOCATION:
                    message += "位置、";
                    break;
            }
        }

        if (message.endsWith("、")) {
            message = message.substring(0, message.length() - 1);
        }
        showAlertWithNegativeButton(activity, false, "权限不足", message + "等相关权限被您拒绝了，请到设置页面打开相关权限", "去设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                goToSetting(activity);
            }
        }, "取消", null);
    }

    //跳转到设置界面，引导用户打开权限
    public static void goToSetting(Activity activity) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
        activity.startActivity(intent);
    }
}
