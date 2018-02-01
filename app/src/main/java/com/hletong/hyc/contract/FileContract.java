package com.hletong.hyc.contract;

import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;

import java.util.List;

/**
 * Created by ddq on 2017/1/4.
 */

public interface FileContract {
    interface View extends IBaseView {

        /**
         * @param progress   (0,1]
         * @param totalByte  当前图片字节数
         * @param done       当前图片是否上传完成
         * @param tag        标记
         * @param imageIndex 当前第几张图片
         * @param imageCount 总共多少图片
         */
        void progress(float progress, long totalByte, boolean done, String tag, int imageIndex, int imageCount);

        /**
         * 开始上传图片
         */
        void uploadStart(int imageCount);

        /**
         * 图片上传成功
         * 做一些UI层的必要处理，如 ProcessBtn的 Mask的隐藏
         */
        void uploadFinished(boolean isSuccess);

        void showFiles(List<String> mFiles);
    }

    interface Presenter extends IBasePresenter {
        void upload(List<String> files, String url, boolean isPublic);

        void download(String id);
    }
}
