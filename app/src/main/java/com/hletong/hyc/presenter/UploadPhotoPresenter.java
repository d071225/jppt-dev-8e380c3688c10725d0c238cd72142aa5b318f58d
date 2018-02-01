package com.hletong.hyc.presenter;

import android.content.Intent;

import com.hletong.hyc.contract.UploadPhotoContract;
import com.hletong.hyc.model.FileInfo;
import com.hletong.hyc.model.RegisterPhoto;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.util.FinishOptions;

/**
 * Created by dongdaqing on 2017/8/7.
 */
public class UploadPhotoPresenter extends FilePresenter<UploadPhotoContract.View> implements UploadPhotoContract.Presenter {
    private RegisterPhoto mPhoto;

    public UploadPhotoPresenter(UploadPhotoContract.View view, RegisterPhoto photo) {
        super(view);
        mPhoto = photo;
    }

    @Override
    public void start() {
        for (int i = 0; i < mPhoto.getPhotos().length; i++) {
            getView().inflateImageView(i, mPhoto.getRatio(), mPhoto.getPhotosDescriptions()[i]);
            updateView(i, mPhoto.getPhotos()[i]);
        }
    }

    @Override
    public void photoSelected(int index, String photo) {
        if (mPhoto.setPhotoIndex(photo, index)) {
            updateView(index, photo);
        } else {
            getView().handleError(ErrorFactory.getParamError("请不要重复添加相同的照片"));
        }
    }

    @Override
    public void upload() {
        upload(mPhoto.getUpLoadPhotos(), mPhoto.getUpLoadUrl(), mPhoto.isPublic());
    }

    @Override
    protected void uploadFinished(int remain, boolean error, FileInfo fileInfo) {
        if (remain == 0 && !error) {
            mPhoto.setFileGroupId(getGroupId());
            Intent intent = new Intent();
            intent.putExtra(RegisterPhoto.class.getSimpleName(), mPhoto);
            getView().finishWithOptions(FinishOptions.FORWARD_RESULT(intent));
        }
    }

    private void updateView(int index, String url) {
        if (url == null) {
            getView().showImage(index, mPhoto.getDefaultSrc(index), mPhoto.isFitCenter(index));
        } else {
            getView().showImage(index, url);
        }
        getView().changeButtonState(mPhoto.canUpload());
    }
}
