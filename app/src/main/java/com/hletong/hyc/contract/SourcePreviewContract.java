package com.hletong.hyc.contract;

import com.hletong.hyc.model.Source;
import com.hletong.mob.architect.view.ICommitSuccessView;

/**
 * Created by ddq on 2017/3/24.
 */

public interface SourcePreviewContract {
    interface View extends FileContract.View,ICommitSuccessView {
        void initWithSource(Source source);
    }

    interface Presenter extends FileContract.Presenter{
        void revoke();
    }
}
