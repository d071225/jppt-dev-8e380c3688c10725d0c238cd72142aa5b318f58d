package com.hletong.hyc.ui.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.FileContract;
import com.hletong.mob.base.MvpActivity;
import com.hletong.mob.gallery.utils.TakePhotoHelper;
import com.hletong.mob.gallery.widget.PickerRecyclerView;
import com.orhanobut.logger.Logger;
import com.xcheng.view.processbtn.ProcessButton;

import java.util.List;
import java.util.Locale;

/**
 * Created by ddq on 2017/1/4.
 * 提供选择图片的功能
 */

public abstract class ImageSelectorActivityNew<P extends FileContract.Presenter> extends MvpActivity<P> implements TakePhotoHelper.PhotoCaptured, FileContract.View {
    private TakePhotoHelper takePhotoHelper;

    protected void showSelector(View view) {
        if (takePhotoHelper == null) {
            takePhotoHelper = TakePhotoHelper.with(this);
        }
        takePhotoHelper.setPicView(view, this);
        takePhotoHelper.showTakeDialog();
    }

    protected void showSelector(PickerRecyclerView view) {
        if (takePhotoHelper == null) {
            takePhotoHelper = TakePhotoHelper.with(this);
        }
        takePhotoHelper.setMultiView(view);
        takePhotoHelper.showTakeDialog();
    }

    protected void setMultiView(PickerRecyclerView multiPickResultView) {
        if (takePhotoHelper == null) {
            takePhotoHelper = TakePhotoHelper.with(this);
        }
        takePhotoHelper.setMultiView(multiPickResultView);
    }

    protected void setPicView(View picView) {
        if (takePhotoHelper == null) {
            takePhotoHelper = TakePhotoHelper.with(this);
        }
        takePhotoHelper.setPicView(picView, this);
    }

    @Override
    public void photoCaptured(String path, View picView) {
        if (picView instanceof ImageView) {
            picView.setTag(R.id.image_path, path);
            Glide.with(this).load(path).fitCenter().thumbnail(0.2f).into((ImageView) picView);
        }
    }

    @Override
    public void photoDelete(View picView) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (takePhotoHelper != null) {
            takePhotoHelper.onActivityResultFreshView(requestCode, resultCode, data);
        }
    }

    @Override
    public void progress(float progress, long totalByte, boolean done, String tag, int imageIndex, int imageCount) {
        Logger.e("progress:%f ,totalByte:%d, imageIndex:%d,imageCount:%d", progress, totalByte, imageIndex, imageCount);
        if (getProcessBtn() != null) {
            int percent = (int) (progress * 100);
            getProcessBtn().setProgressText(String.format(Locale.CHINA, "上传图片 %d/%d(%d%%)", imageIndex, imageCount, percent));
            getProcessBtn().setProgress(percent);
        }
    }

    @Override
    public void uploadStart(int imageCount) {
        if (getProcessBtn() != null) {
            getProcessBtn().setProgressText(String.format(Locale.CHINA, "上传图片 %d/%d(%d%%)", 0, imageCount, 0));
            getProcessBtn().setProgress(0);
        }
    }

    @Override
    public void uploadFinished(boolean isSuccess) {
        if (getProcessBtn() != null) {
            getProcessBtn().normal();
        }
    }

    protected ProcessButton getProcessBtn() {
        return null;
    }

    @Override
    public void showFiles(List<String> mFiles) {

    }
}
