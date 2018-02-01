package com.hletong.mob.gallery.event;

import android.view.View;

import java.util.ArrayList;

/**
 * 传递到
 * Created by cx on 15/6/20.
 */
public interface OnPhotoPickerListener {


    void onItemCheck(int position, String path, boolean isCheck, int selectCount);

    void onCameraClick(View v);

    void onPhotoClick(View v, int position, ArrayList<String> selectPhotos, ArrayList<String> allPhotos);
}
