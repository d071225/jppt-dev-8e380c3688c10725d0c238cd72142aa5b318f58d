package com.hletong.hyc.contract;

import com.hletong.hyc.model.UnTodo;
import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.IBaseView;

/**
 * 设置的协议模块，包含所有设置相关的接口
 * Created by cc on 2017/1/5.
 */
public interface AppContract {
    interface View extends IBaseView {
        void setUnToDoInfo(UnTodo unTodo);
    }

    /***
     * 获取待办事项
     ***/
    interface Presenter extends IBasePresenter{
        void getUnTodoInfo();
    }
}
