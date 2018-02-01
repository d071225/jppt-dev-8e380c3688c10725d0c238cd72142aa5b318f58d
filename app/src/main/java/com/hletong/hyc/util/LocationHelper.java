package com.hletong.hyc.util;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;

import com.hletong.hyc.HyApplication;
import com.hletong.hyc.contract.LocationView;
import com.hletong.mob.dialog.AlertDialog;
import com.hletong.mob.util.AMapLocationManager;
import com.xcheng.permission.DeniedResult;
import com.xcheng.permission.EasyPermission;
import com.xcheng.permission.OnRequestCallback;

import java.util.List;

/**
 * Created by chengxin on 2017/5/25.
 */
public class LocationHelper implements LocationView {
    private Activity mActivity;
    private static final String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

    public LocationHelper(Activity activity) {
        mActivity = activity;
    }

    @Override
    public void startLocating(boolean isForceStart) {
        boolean isGpsOpen = AMapLocationManager.isGpsProvider() || AMapLocationManager.isNetWorkProvider();
        if (isForceStart) {
            if (!isGpsOpen) {
                DialogFactory.showAlertWithNegativeButton(mActivity, false, "当前操作需要位置信息,请开启", null, "开启", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AMapLocationManager.toLocationSetting(mActivity);
                        dialog.dismiss();
                    }
                }, "取消", null);
            }
            requestPermissions();
        } else {
            if (isGpsOpen) {
                if (!EasyPermission.isMarshmallow()) {
                    HyApplication.startLocation();
                    return;
                }
                List<String> deniedPermissions = EasyPermission.findDeniedPermissions(mActivity, permissions);
                if (deniedPermissions.size() == 0) {
                    HyApplication.startLocation();
                }
            }
        }
    }

    private void requestPermissions() {
        EasyPermission.with(mActivity).permissions(permissions)
                .request(1228, new OnRequestCallback() {
                    @Override
                    public void onAllowed() {
                        HyApplication.startLocation();
                    }

                    @Override
                    public void onRefused(DeniedResult deniedResult) {
                        if (deniedResult.allNeverAsked) {
                            DialogFactory.permissionSetting(mActivity, deniedResult.deniedPerms);
                        }
                    }
                });
    }

    @Override
    public void onLocationError(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("位置信息不匹配")
                .setContent(message)
                .setCancelable(false)
                .setPositiveButton("知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        startLocating(true);
                    }
                })
                .show();

    }

}
