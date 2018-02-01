package com.hletong.mob.gallery.builder;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hletong.mob.gallery.PhotoPickerActivity;

import java.util.ArrayList;

/**
 * Created by chengxin on 17/02/27.
 * Builder class to ease Intent setup.
 */
public class PickerBuilder {
    public final static int INDEX_ALL_PHOTOS = 0;

    //选择
    public static final int REQUEST_CODE_PICKER = 233;
    //预览
    public final static int REQUEST_CODE_PREVIEW = 234;
    //相机
    public static final int REQUEST_CODE_TAKE_PHOTO = 235;

    public final static int DEFAULT_MAX_COUNT = 9;
    public final static int DEFAULT_COLUMN_NUMBER = 3;
    public final static String EXTRA_PHOTO_SIZE = "photo_size";

    public final static String KEY_SELECTED_PHOTOS = "select_photos";
    public final static String EXTRA_MAX_COUNT = "max_count";
    public final static String EXTRA_SHOW_CAMERA = "show_camera";
    public final static String EXTRA_SHOW_GIF = "show_gif";
    public final static String EXTRA_COLUMN_COUNT = "column_count";
    //是否可以点击
    public final static String EXTRA_PREVIEW_ENABLED = "preview_enable";

    public static PickerBuilder builder() {
        return new PickerBuilder();
    }

    public static PickerBuilder builder(Bundle dataBundle) {
        return new PickerBuilder(dataBundle);
    }

    private Bundle mPickerOptionsBundle;
    private Intent mPickerIntent;

    public PickerBuilder() {
        this(new Bundle());
    }

    public PickerBuilder(Bundle dataBundle) {
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
        start(activity, REQUEST_CODE_PICKER);
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
        start(fragment,  REQUEST_CODE_PICKER);
    }

    /**
     * Get Intent to start {@link PhotoPickerActivity}
     *
     * @return Intent for {@link PhotoPickerActivity}
     */
    public Intent getIntent(@NonNull Context context) {
        mPickerIntent.setClass(context,  PhotoPickerActivity.class);
        mPickerIntent.putExtras(mPickerOptionsBundle);
        return mPickerIntent;
    }

    //最多选择多少张
    public PickerBuilder setMaxPhotoCount(int photoCount) {
        mPickerOptionsBundle.putInt(EXTRA_MAX_COUNT, photoCount);
        return this;
    }

    public int getMaxPhotoCount() {
        return mPickerOptionsBundle.getInt(EXTRA_MAX_COUNT, DEFAULT_MAX_COUNT);
    }

    //设置每行多少条
    public PickerBuilder setColumnCount(int columnCount) {
        mPickerOptionsBundle.putInt(EXTRA_COLUMN_COUNT, columnCount);
        return this;
    }

    //设置每行多少条
    public int getColumnCount() {
        return mPickerOptionsBundle.getInt(EXTRA_COLUMN_COUNT, DEFAULT_COLUMN_NUMBER);
    }

    public PickerBuilder setShowGif(boolean showGif) {
        mPickerOptionsBundle.putBoolean(EXTRA_SHOW_GIF, showGif);
        return this;
    }

    public boolean isShowGif() {
        return mPickerOptionsBundle.getBoolean(EXTRA_SHOW_GIF, false);
    }

    public PickerBuilder setShowCamera(boolean showCamera) {
        mPickerOptionsBundle.putBoolean(EXTRA_SHOW_CAMERA, showCamera);
        return this;
    }

    public boolean isShowCamera() {
        return mPickerOptionsBundle.getBoolean(EXTRA_SHOW_CAMERA, true);
    }

    public PickerBuilder setSelectPhotos(ArrayList<String> selectPhotos) {
        if (selectPhotos == null) {
            selectPhotos = new ArrayList<>(0);
        }
        mPickerOptionsBundle.putStringArrayList(KEY_SELECTED_PHOTOS, selectPhotos);
        return this;
    }

    @NonNull
    public ArrayList<String> getSelectPhotos() {
        ArrayList<String> selectPhotos = mPickerOptionsBundle.getStringArrayList(KEY_SELECTED_PHOTOS);
        return selectPhotos != null ? selectPhotos : new ArrayList<String>();
    }

    public PickerBuilder setPreviewEnabled(boolean previewEnabled) {
        mPickerOptionsBundle.putBoolean(EXTRA_PREVIEW_ENABLED, previewEnabled);
        return this;
    }

    public boolean isPreviewEnabled() {
        return mPickerOptionsBundle.getBoolean(EXTRA_PREVIEW_ENABLED, true);
    }
}
