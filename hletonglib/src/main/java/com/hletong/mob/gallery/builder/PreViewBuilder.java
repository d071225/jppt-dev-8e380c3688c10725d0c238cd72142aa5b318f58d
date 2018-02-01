package com.hletong.mob.gallery.builder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.hletong.mob.gallery.PhotoPagerActivity;
import com.hletong.mob.gallery.PhotoPickerActivity;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

/**
 * Created by chengxin on 17/02/27.
 * Builder class to ease Intent setup.
 */
public class PreViewBuilder {
    //编辑 -选择/不选择
    public static final int SELECT = -1;
    //删除
    public static final int DELETE = -2;
    //预览
    public static final int PREVIEW = -3;

    @IntDef({SELECT, DELETE, PREVIEW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PreViewAction {
    }

    //预览
    public final static int REQUEST_CODE_PREVIEW = 234;

    public final static String EXTRA_CURRENT_ITEM = "current_item";
    public final static String EXTRA_ACTION = "action";
    public final static String KEY_PREVIEW_PHOTOS = "preview_photos";
    public static PreViewBuilder builder() {
        return new PreViewBuilder();
    }

    public static PreViewBuilder builder(Bundle dataBundle) {
        return new PreViewBuilder(dataBundle);
    }

    private Bundle mPickerOptionsBundle;
    private Intent mPickerIntent;

    public PreViewBuilder() {
        this(new Bundle());
    }

    public PreViewBuilder(Bundle dataBundle) {
        mPickerOptionsBundle = dataBundle;
        mPickerIntent = new Intent();
    }

    /**
     * Send the Intent from an Activity with a custom request code
     *
     * @param activity    Activity to receive result
     * @param requestCode requestCode for result
     */
    public void start(@NonNull Activity activity, int requestCode) {
        activity.startActivityForResult(getIntent(activity), requestCode);
    }

    /**
     * Send the crop Intent from an Activity
     *
     * @param activity Activity to receive result
     */
    public void start(@NonNull Activity activity) {
        start(activity, REQUEST_CODE_PREVIEW);
    }

    /**
     * Send the Intent with a custom request code
     *
     * @param fragment    Fragment to receive result
     * @param requestCode requestCode for result
     */
    public void start(@NonNull android.support.v4.app.Fragment fragment, int requestCode) {
        fragment.startActivityForResult(getIntent(fragment.getContext()), requestCode);
    }

    /**
     * Send the Intent with a custom request code
     *
     * @param fragment Fragment to receive result
     */
    public void start(@NonNull android.support.v4.app.Fragment fragment) {
        start(fragment, REQUEST_CODE_PREVIEW);
    }

    /**
     * Get Intent to start {@link PhotoPickerActivity}
     *
     * @return Intent for {@link PhotoPickerActivity}
     */
    public Intent getIntent(@NonNull Context context) {
        mPickerIntent.setClass(context, PhotoPagerActivity.class);
        mPickerIntent.putExtras(mPickerOptionsBundle);
        return mPickerIntent;
    }

    //设置展现方式
    public PreViewBuilder setAction(@PreViewAction int action) {
        mPickerOptionsBundle.putInt(EXTRA_ACTION, action);
        return this;
    }

    //获取展现方式
    public int getAction() {
        return mPickerOptionsBundle.getInt(EXTRA_ACTION, PREVIEW);
    }

    @NonNull
    public ArrayList<String> getSelectPhotos() {
        ArrayList<String> selectPhotos = mPickerOptionsBundle.getStringArrayList(PickerBuilder.KEY_SELECTED_PHOTOS);
        return selectPhotos != null ? selectPhotos : new ArrayList<String>();
    }

    public PreViewBuilder setSelectPhotos(ArrayList<String> selectPhotos) {
        if (selectPhotos == null) {
            selectPhotos = new ArrayList<>(0);
        }
        mPickerOptionsBundle.putStringArrayList(PickerBuilder.KEY_SELECTED_PHOTOS, selectPhotos);
        return this;
    }

    @NonNull
    public ArrayList<String> getPreViewPhotos() {
        ArrayList<String> preViewPhotos = mPickerOptionsBundle.getStringArrayList(KEY_PREVIEW_PHOTOS);
        if (preViewPhotos == null || preViewPhotos.size() == 0) {
            preViewPhotos = new ArrayList<>(getSelectPhotos());
        }
        return preViewPhotos;
    }

    public PreViewBuilder setPreViewPhotos(ArrayList<String> preViewPhotos) {
        if (preViewPhotos == null) {
            preViewPhotos = new ArrayList<>(0);
        }
        mPickerOptionsBundle.putStringArrayList(KEY_PREVIEW_PHOTOS, preViewPhotos);
        return this;
    }


    //预览的时候选择
    public PreViewBuilder setCurrentItem(int currentItem) {
        mPickerOptionsBundle.putInt(EXTRA_CURRENT_ITEM, currentItem);
        return this;
    }

    public int getCurrentItem() {
        return mPickerOptionsBundle.getInt(EXTRA_CURRENT_ITEM, 0);
    }

    //最多选择多少张
    public PreViewBuilder setMaxPhotoCount(int photoCount) {
        mPickerOptionsBundle.putInt(PickerBuilder.EXTRA_MAX_COUNT, photoCount);
        return this;
    }

    public int getMaxPhotoCount() {
        return mPickerOptionsBundle.getInt(PickerBuilder.EXTRA_MAX_COUNT, PickerBuilder.DEFAULT_MAX_COUNT);
    }

}
