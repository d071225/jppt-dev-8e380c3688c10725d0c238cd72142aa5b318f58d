package com.hletong.hyc.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.hletong.mob.util.AppManager;

/**
 * Created by ddq on 2016/12/27.
 * 版本信息
 */

public class VersionInfo implements Parcelable {
    private String versionUuid;//新版本唯一编号
    private String url;//下载地址
    private int appVersion;//新版本号
    private String appDesc;//版本描述，本版本的更新功能点
    private String updateType;//版本描述，本版本的更新功能点
    private String appSize;//文件大小
    private String releaseDate;//发布日期 例：20170831123456　

    public String getFileAddress() {
        return url;
    }

    public int getCurrentVersion() {
        return appVersion;
    }

    public boolean isForced() {
        return "1".equals(updateType);
    }

    public String getAppDesc() {
        return appDesc;
    }

    public String getAppSize() {
        return "安装包大小：" + appSize + "M";
    }

    public String getAppVersion() {
        return "欢迎升级" + AppManager.appName + " V" + appVersion;
    }

    public void writeToPreference(Context mContext) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences("version", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        mEditor.putBoolean("version_info_set", true);
        mEditor.putString("version_info", new Gson().toJson(this));
        mEditor.apply();
    }

    public static VersionInfo getVersionInfo(Context mContext) {
        SharedPreferences mSharedPreferences = mContext.getSharedPreferences("version", Context.MODE_PRIVATE);
        if (!mSharedPreferences.getBoolean("version_info_set", false)) {
            return null;
        }
        return new Gson().fromJson(mSharedPreferences.getString("version_info", null), VersionInfo.class);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.versionUuid);
        dest.writeString(this.url);
        dest.writeInt(this.appVersion);
        dest.writeString(this.appDesc);
        dest.writeString(this.updateType);
        dest.writeString(this.appSize);
        dest.writeString(this.releaseDate);
    }

    public VersionInfo() {
    }

    protected VersionInfo(Parcel in) {
        this.versionUuid = in.readString();
        this.url = in.readString();
        this.appVersion = in.readInt();
        this.appDesc = in.readString();
        this.updateType = in.readString();
        this.appSize = in.readString();
        this.releaseDate = in.readString();
    }

    public static final Creator<VersionInfo> CREATOR = new Creator<VersionInfo>() {
        @Override
        public VersionInfo createFromParcel(Parcel source) {
            return new VersionInfo(source);
        }

        @Override
        public VersionInfo[] newArray(int size) {
            return new VersionInfo[size];
        }
    };
}
