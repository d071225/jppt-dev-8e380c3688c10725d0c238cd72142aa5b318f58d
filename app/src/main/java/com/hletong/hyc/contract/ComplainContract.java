package com.hletong.hyc.contract;

import com.hletong.mob.architect.view.ICommitSuccessView;

import java.util.ArrayList;

/**
 * Created by ddq on 2017/3/30.
 */

public interface ComplainContract {
    interface View extends ICommitSuccessView,FileContract.View{

    }

    interface Presenter extends FileContract.Presenter{
        void submit(String message, ArrayList<String> images);
    }
}