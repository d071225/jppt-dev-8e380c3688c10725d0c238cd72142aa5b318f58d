package com.hletong.hyc.ui.dialog;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;

import com.hletong.hyc.R;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.mob.dialog.AlertDialog;
import com.xcheng.permission.DeniedResult;
import com.xcheng.permission.EasyPermission;
import com.xcheng.permission.OnRequestCallback;

/**
 * 拨打电话
 * Created by ddq on 2016/11/10.
 */
public class CallPhoneDialog extends DialogFragment {
    private final int CALL = 799;
    private Activity mActivity;
    private OnCalledListener mOnCalledListener;

    public static CallPhoneDialog getInstance() {
        return new CallPhoneDialog();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = getActivity();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        if (mTitle == null) {
            mTitle = getString(R.string.call_customer_service);
        }
        if (mContent == null) {
            mContent = getString(R.string.phone_no);
        }
        if (mNumber == null) {
            mNumber = "051185188888";
        }
        return builder.setTitle(mTitle)
                .setContent(mContent)
                .setPositiveButton(getString(R.string.call), getListener())
                .setNegativeButton(getString(R.string.cancel), null).build();
    }

    private void onCallResult() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mNumber.trim()));
        if (intent.resolveActivity(mActivity.getPackageManager()) != null) {
            if (ActivityCompat.checkSelfPermission(mActivity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                mActivity.startActivity(intent);
                if (mOnCalledListener != null) {
                    mOnCalledListener.onCalled();
                }
            }
        }
    }

    private String mTitle;
    private String mContent;
    private String mNumber;

    public void show(FragmentManager manager) {
        show(manager, CallPhoneDialog.class.getSimpleName());
    }

    public void show(FragmentManager manager, String title, String content, String number) {
        mTitle = title;
        mContent = content;
        mNumber = number;
        show(manager);
    }

    private DialogInterface.OnClickListener getListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EasyPermission.with(mActivity).permissions(Manifest.permission.CALL_PHONE)
                        .request(1228, new OnRequestCallback() {
                            @Override
                            public void onAllowed() {
                                onCallResult();
                            }

                            @Override
                            public void onRefused(DeniedResult deniedResult) {
                                if (deniedResult.allNeverAsked) {
                                    DialogFactory.permissionSetting(mActivity, deniedResult.deniedPerms);
                                }
                            }
                        });
                dialog.dismiss();
            }
        };
    }

    public void setOnCalledListener(OnCalledListener onCalledListener) {
        mOnCalledListener = onCalledListener;
    }

    public interface OnCalledListener {
        void onCalled();
    }
}
