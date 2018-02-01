package com.hletong.hyc.enums;

import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;

import com.hletong.hyc.R;
import com.hletong.hyc.ui.fragment.AppFragment;
import com.hletong.hyc.ui.fragment.SettingFragment;

public enum MainTab {

    APP("home", "应用", R.drawable.ic_launcher, AppFragment.class),
    SETTING("setting_main", "设置", R.drawable.ic_launcher, SettingFragment.class);

    private String tag;
    private String name;
    private int resIcon;
    private Class<? extends Fragment> fragmentClazz;

    MainTab(String tag, String name, int resIcon, Class<? extends Fragment> fragmentClazz) {
        this.tag = tag;
        this.name = name;
        this.resIcon = resIcon;
        this.fragmentClazz = fragmentClazz;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    @DrawableRes
    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

    public Class<? extends Fragment> getFragmentClazz() {
        return fragmentClazz;
    }

    public void setFragmentClazz(Class<? extends Fragment> fragmentClazz) {
        this.fragmentClazz = fragmentClazz;
    }

    public static int getCurrentTagIndex(String tag) {
        MainTab mainTabs[] = MainTab.values();
        for (int index = 0; index < mainTabs.length; index++) {
            if (mainTabs[index].getTag().equals(tag)) {
                return index;
            }
        }
        return 0;
    }
}
