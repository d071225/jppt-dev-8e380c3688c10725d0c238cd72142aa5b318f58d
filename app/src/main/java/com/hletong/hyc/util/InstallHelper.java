package com.hletong.hyc.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.hletong.mob.util.AppManager;

import java.io.File;

/**
 * Created by dongdaqing on 2017/9/14.
 */

public class InstallHelper {
    private Context mContext;

    private InstallHelper(Context context) {
        mContext = context;
    }

    public static InstallHelper with(Context context) {
        return new InstallHelper(context);
    }

    public void install(File file) {
        if (!file.exists())
            return;

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = getUriForFile(file);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(file);
        }

        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    public void install(String name){
        install(new File(name));
    }

    public Uri getUriForFile(File file) {
        return FileProvider.getUriForFile(mContext, AppManager.appId + ".FileProvider", file);
    }
}
