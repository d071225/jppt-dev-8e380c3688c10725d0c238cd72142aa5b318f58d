package com.hletong.mob.gallery.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Toast;

import com.hletong.mob.R;
import com.hletong.mob.gallery.builder.PickerBuilder;
import com.hletong.mob.gallery.widget.PickerRecyclerView;
import com.xcheng.okhttp.util.ParamUtil;
import com.xcheng.permission.DeniedResult;
import com.xcheng.permission.EasyPermission;
import com.xcheng.permission.OnRequestCallback;
import com.xcheng.view.controller.dialog.BottomOptionDialog;
import com.xcheng.view.controller.dialog.SimpleSelectListener;
import com.xcheng.view.util.ToastLess;

import java.io.IOException;
import java.util.ArrayList;

public class TakePhotoHelper {
    private final Activity mActivity;
    private final Fragment mFragment;

    private final boolean isStartFromFragment;
    private PickerRecyclerView mPickerRecyclerView;
    private View mPicView;
    private PhotoCaptured mPhotoCaptured;
    private ImageCaptureManager captureManager;
    private BottomOptionDialog dialog;

    //获取当前操作的View
    @Nullable
    public View getCurrentPicView() {
        return mPickerRecyclerView != null ? mPickerRecyclerView : mPicView;
    }

    public static TakePhotoHelper with(Activity startFrom) {
        return new TakePhotoHelper(startFrom);
    }

    public static TakePhotoHelper with(Fragment startFrom) {
        return new TakePhotoHelper(startFrom);
    }

    public void showTakeDialog() {
        if (dialog == null) {
            dialog = new BottomOptionDialog.Builder(mActivity)
                    .optionTexts("拍照", "从相册中选择")
                    .onSelectListener(new SimpleSelectListener() {
                        @Override
                        public void onOptionSelect(View view, int position) {
                            if (position == 0) {
                                requestPermission(PickerBuilder.REQUEST_CODE_TAKE_PHOTO);
                            } else if (position == 1) {
                                requestPermission(PickerBuilder.REQUEST_CODE_PICKER);
                            }
                        }
                    }).create();
        }
        dialog.show();
    }

    private TakePhotoHelper(Fragment startFrom) {
        mActivity = startFrom.getActivity();
        mFragment = startFrom;
        isStartFromFragment = true;
        captureManager = new ImageCaptureManager();
    }

    private TakePhotoHelper(Activity startFrom) {
        mActivity = startFrom;
        mFragment = null;
        isStartFromFragment = false;
        captureManager = new ImageCaptureManager();
    }

    public TakePhotoHelper setPicView(View picView, @NonNull PhotoCaptured photoCaptured) {
        mPickerRecyclerView = null;
        mPhotoCaptured = photoCaptured;
        mPicView = picView;
        return this;
    }

    public TakePhotoHelper setMultiView(PickerRecyclerView multiPickResultView) {
        mPickerRecyclerView = multiPickResultView;
        mPicView = null;
        mPhotoCaptured = null;
        return this;
    }

    private void requestPermission(final int requestCode) {
        EasyPermission.with(mActivity).permissions(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .request(requestCode, new OnRequestCallback() {
                    @Override
                    public void onAllowed() {
                        if (requestCode == PickerBuilder.REQUEST_CODE_PICKER) {
                            onPickerPhotoResult();
                        } else if (requestCode == PickerBuilder.REQUEST_CODE_TAKE_PHOTO) {
                            onCameraResult();
                        }
                    }

                    @Override
                    public void onRefused(DeniedResult deniedResult) {
                        if (deniedResult.allNeverAsked) {
                            ToastLess.showToast("相机和存储相关权限被拒绝，请在设置中打开");
                        }
                    }
                });
    }

    private void onCameraResult() {
        try {
            Intent intent = captureManager.dispatchTakePictureIntent();
            if (intent != null) {
                if (isStartFromFragment) {
                    mFragment.startActivityForResult(intent, PickerBuilder.REQUEST_CODE_TAKE_PHOTO);
                } else {
                    mActivity.startActivityForResult(intent, PickerBuilder.REQUEST_CODE_TAKE_PHOTO);
                }
            } else {
                Toast.makeText(mActivity, R.string.unknown_error, Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
            Toast.makeText(mActivity, "请检查SD卡是否已挂载！", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void onPickerPhotoResult() {
        if (mPickerRecyclerView != null) {
            mPickerRecyclerView.toPicker();
        } else {
            if (isStartFromFragment) {
                PickerBuilder.builder().setMaxPhotoCount(1).start(mFragment);
            } else {
                PickerBuilder.builder().setMaxPhotoCount(1).start(mActivity);
            }
        }
    }

    /**
     * 页面放回后刷新View
     **/
    public void onActivityResultFreshView(int requestCode, int resultCode, Intent data) {
        /**此方法必须在onActivityResult 中之后执行 否则在Activity分发事件的时候找不到对应的Fragment**/
        if (resultCode == Activity.RESULT_OK) {
            if (mPickerRecyclerView != null) {
                if (requestCode == PickerBuilder.REQUEST_CODE_TAKE_PHOTO) {
                    captureManager.galleryAddPic();
                    mPickerRecyclerView.addTakePics(captureManager.getCurrentPhotoPath());
                } else {
                    //内部自己做了对RequestCode的判断 预览和从图库选择
                    mPickerRecyclerView.onActivityResult(requestCode, resultCode, data);
                }
            } else if (mPicView != null) {
                if (requestCode == PickerBuilder.REQUEST_CODE_PICKER) {
                    if (mPhotoCaptured != null) {
                        ArrayList<String> array = data.getStringArrayListExtra(PickerBuilder.KEY_SELECTED_PHOTOS);
                        mPhotoCaptured.photoCaptured(array.get(0), mPicView);
                    }
                } else if (requestCode == PickerBuilder.REQUEST_CODE_TAKE_PHOTO) {
                    if (mPhotoCaptured != null) {
                        captureManager.galleryAddPic();
                        mPhotoCaptured.photoCaptured(captureManager.getCurrentPhotoPath(), mPicView);
                    }
                } else if (requestCode == PickerBuilder.REQUEST_CODE_PREVIEW) {
                    ArrayList<String> array = data.getStringArrayListExtra(PickerBuilder.KEY_SELECTED_PHOTOS);
                    /**单张图片被删除**/
                    if (mPhotoCaptured != null && ParamUtil.isEmpty(array)) {
                        mPhotoCaptured.photoDelete(mPicView);
                    }
                }
            }
        }
    }


    public interface PhotoCaptured {
        /**
         * imageView单选
         **/
        void photoCaptured(String path, View picView);

        /**
         * imageView删除src中图片
         **/
        void photoDelete(View picView);
    }
}