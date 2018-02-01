package com.hletong.hyc.http.parse;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.mob.HLApplication;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.error.ErrorState;
import com.hletong.mob.util.AppManager;
import com.xcheng.okhttp.callback.HttpParser;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;
import com.xcheng.okhttp.request.OkResponse;

import java.io.IOException;

import okhttp3.Response;

/**
 * 顶层解析类
 * Created by chengxin on 2017/7/1.
 */
public abstract class JpptParse<T> implements HttpParser<T> {
    public static final String ENTRY = "entry";
    public static final String TYPETOKEN = TypeToken.class.getSimpleName();
    public static final String SESSION_NOT_CHECKED = "session_not_checked";

    @SuppressWarnings("unchecked")
    protected TypeToken<T> getToken(OkCall<T> okCall) {
        TypeToken<T> token = okCall.request().extra(TYPETOKEN);
        if (token == null) {
            token = (TypeToken<T>) TypeToken.get(okCall.getType());
        }
        return token;
    }

    @NonNull
    @Override
    public OkResponse<T> parseNetworkResponse(OkCall<T> okCall, Response response) throws IOException {
        //计算本地与服务端的时间差
        HLApplication.setNewDate(System.currentTimeMillis(), response.header("Date"));
        OkResponse<T> okResponse;
        if (!response.isSuccessful()) {
            //非成功响应，按照通用错误处理
            okResponse = OkResponse.error(ErrorFactory.getError(response.body().string(), response.code()));
        } else {//服务端成功响应，解析返回数据
            okResponse = handleResponse(okCall, response);
        }
        //检查是否是Session过期。在货源公告中清空session然后上拉此时code=200，也报出SessionTimeOut，在此统一检测
        if (willReLogin((BaseError) okResponse.getError(), okCall)) {
            if (login(okCall.request().tag())) {
                return okCall.clone().execute();
            } else {
                LoginInfo.loginDialog();
            }
        }
        return okResponse;
    }

    private boolean willReLogin(BaseError jppError, OkCall<T> okCall) {
        return jppError != null && jppError.getCode() == ErrorState.SESSION_TIME_OUT
                && LoginInfo.hasLogin() && okCall.request().extra(SESSION_NOT_CHECKED) == null;
    }

    @NonNull
    @Override
    public EasyError parseException(OkCall<T> okCall, IOException e) {
        return ErrorFactory.getNetworkError(e);
    }

    protected abstract OkResponse<T> handleResponse(OkCall<T> okCall, Response response) throws IOException;

    private boolean login(Object tag) {
        try {
            OkRequest request = LoginInfo.request(AppManager.getLoginName(), AppManager.getPassword(), tag);
            OkResponse<LoginInfo> response = new ExecutorCall<LoginInfo>(request).execute();
            if (response.isSuccess()) {
                LoginInfo.setLoginInfo(response.getBody(), AppManager.getLoginName(), AppManager.getPassword());
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
