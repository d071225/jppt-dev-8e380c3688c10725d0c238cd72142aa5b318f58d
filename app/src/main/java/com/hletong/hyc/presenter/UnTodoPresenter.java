package com.hletong.hyc.presenter;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.contract.AppContract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.http.parse.JpptParse;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.UnTodo;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.callback.UICallback;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

import org.greenrobot.eventbus.EventBus;

/**
 * 未读消息请求
 * Created by cc on 2017/1/4.
 */
public class UnTodoPresenter extends BasePresenter<AppContract.View> implements AppContract.Presenter {

    public UnTodoPresenter(AppContract.View view) {
        super(view);
    }


    private OkRequest request() {
        return EasyOkHttp.get(Constant.UNDO_INFO)
                .tag(tag())
                .extra(JpptParse.SESSION_NOT_CHECKED, true)
                .param("type", AppTypeConfig.getUndoTypes())
                .param("userType", BuildConfig.app_type)
                .build();
    }

    @Override
    public void getUnTodoInfo() {
        if (LoginInfo.hasLogin()) {
            new ExecutorCall<UnTodo>(request()).enqueue(new UICallback<UnTodo>() {
                @Override
                public void onError(OkCall<UnTodo> okCall, EasyError error) {
                }

                @Override
                public void onSuccess(OkCall<UnTodo> okCall, UnTodo response) {
                    EventBus.getDefault().post(response.getUndoEvent());
                    getView().setUnToDoInfo(response);
                }
            });
        }
    }
}
