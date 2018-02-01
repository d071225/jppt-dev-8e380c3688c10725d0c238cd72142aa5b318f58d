package com.hletong.mob.gallery;

import android.app.Activity;
import android.content.Intent;

import com.hletong.mob.gallery.builder.PickerBuilder;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/8/5 0005.
 */
public class PhotoPickUtils {

    public static void onActivityResult(int requestCode, int resultCode, Intent data,PickHandler pickHandler ) {

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PickerBuilder.REQUEST_CODE_PICKER) {//第一次，选择图片后返回
                if (data != null) {
                    ArrayList<String> photos = data.getStringArrayListExtra(PickerBuilder.KEY_SELECTED_PHOTOS);
                    pickHandler.onPickSuccess(photos);
                } else {
                    pickHandler.onPickFail("选择图片失败");
                }
            }else if (requestCode == PickerBuilder.REQUEST_CODE_PREVIEW){//如果是预览与删除后返回
                if (data != null) {
                    ArrayList<String> photos = data.getStringArrayListExtra(PickerBuilder.KEY_SELECTED_PHOTOS);
                    pickHandler.onPreviewBack(photos);
                } else {
                   // pickHandler.onPickFail("选择图片失败");
                }

            }
        }else {

            if (requestCode == PickerBuilder.REQUEST_CODE_PICKER){
                pickHandler.onPickCancel();
            }
        }

    }

    public static void startPick(Activity context,ArrayList<String> photos){
        PickerBuilder.builder()
                .setMaxPhotoCount(9)
                .setShowCamera(true)
                .setShowGif(true)
                .setSelectPhotos(photos)
                .setPreviewEnabled(true)
                .start(context, PickerBuilder.REQUEST_CODE_PICKER);
    }

    public interface  PickHandler{
        void onPickSuccess(ArrayList<String> photos);
        void onPreviewBack(ArrayList<String> photos);
        void onPickFail(String error);
        void onPickCancel();
    }
}
