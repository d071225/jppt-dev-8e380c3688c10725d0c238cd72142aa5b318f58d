package com.hletong.hyc.contract;

import com.hletong.mob.architect.view.ITransactionView;

/**
 * Created by dongdaqing on 2017/8/7.
 */

public interface UploadPhotoContract {
    interface View extends FileContract.View, ITransactionView {
        void inflateImageView(int index, float ratio, String des);

        void showImage(int index, String url);

        void showImage(int index, int src, boolean center);

        void changeButtonState(boolean enable);
    }

    interface Presenter extends FileContract.Presenter {
        void photoSelected(int index, String photo);

        void upload();
    }
}
