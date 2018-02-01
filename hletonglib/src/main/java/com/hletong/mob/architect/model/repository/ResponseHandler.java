package com.hletong.mob.architect.model.repository;

import com.hletong.mob.HLApplication;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.error.DataError;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.net.EventSessionTimeout;
import com.hletong.mob.net.ISessionTimeoutError;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Response;

/**
 * response响应基类,用于解析服务端返回的数据
 * Created by ddq on 2016/12/6.
 */
abstract class ResponseHandler<T> implements okhttp3.Callback, ISessionTimeoutError {
    private DataCallback<T> mCallback;
    private BaseError mError;
    private int mRequestFlag;//放到Error里面，用于区分不同请求

    ResponseHandler(int requestFlag, DataCallback<T> mCallback) {
        this.mCallback = mCallback;
        this.mRequestFlag = requestFlag;
    }

    @Override
    public void onFailure(Call call, IOException e) {
        /**如果请求被取消了，不做Presenter 和 UI层回调**/
        mCallback.setCall(call);
        setError(ErrorFactory.getNetworkError(e));//处理网络出错
        dispatchError();
    }

    @Override
    public final void onResponse(Call call, Response response) {
        mCallback.setCall(call);
        //计算本地与服务端的时间差
        HLApplication.setNewDate(System.currentTimeMillis(), response.header("Date"));
        setError(null);//清空目前已有的error
        try {
            if (!response.isSuccessful()) {
                //非成功响应，按照通用错误处理
                setError(ErrorFactory.getError(response.body().string(), response.code()));
            } else {//服务端成功响应，解析返回数据
                T data = handleResponse(response);
                if (data != null) {
                    mCallback.onSuccess(data);//向下传递数据
                    return;
                }
            }
            if (checkErrorNull()) {
                /**in case设置缺省值*/
                setError(new DataError("数据异常"));
            }
        } catch (IOException e) {
            setError(ErrorFactory.getNetworkError(e));//数据读写过程中发生了错误
        }
        //检查是否是Session过期。在货源公告中清空session然后上拉此时code=200，也报出SessionTimeOut，在此统一检测
        if (!ItemRequestValue.isCheckHeader(call.request().headers()) && mError.isSessionTimeOut()) {
            EventBus.getDefault().post(EventSessionTimeout.getInstance(mError.getMessage(), call, this));
            //拦截回调，静默登录失败后继续执行session过期回调
            return;
        }
        //向下传递错误 保证Error不为null
        dispatchError();
    }

    public void setError(BaseError error) {
        this.mError = error;
        if (mError != null) {
            this.mError.setId(mRequestFlag);
        }
    }

    /**
     * called setError before this method
     * 这个函数应该作为结束函数调用，这个函数之后这个类里面不应该有其他的处理逻辑
     */
    private void dispatchError() {
        mCallback.onError(mError);
    }

    /**
     * session过期错误
     */
    @Override
    public void onSessionError() {
        if (mError.isSessionTimeOut()) {
            dispatchError();
        }
    }

    boolean checkErrorNull() {
        return mError == null;
    }

    /**
     * 子类处理结果,返回为null代表失败，否则视为成功
     *
     * @param response 返回的结果
     */
    protected abstract T handleResponse(Response response) throws IOException;

    void handleException(Exception e) {
        e.printStackTrace();
        setError(ErrorFactory.getError(e));
    }
}
