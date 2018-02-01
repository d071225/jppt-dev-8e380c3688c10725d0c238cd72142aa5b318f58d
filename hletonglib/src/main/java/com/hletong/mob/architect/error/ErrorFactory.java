package com.hletong.mob.architect.error;

import com.hletong.mob.architect.model.CommonResult;
import com.orhanobut.logger.Logger;
import com.xcheng.view.enums.RequestState;

import java.io.IOException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import static com.xcheng.view.enums.RequestState.*;


/**
 * Created by ddq on 2016/12/7.
 */
public class ErrorFactory {

    public static BaseError getError(Exception e) {
        if (e instanceof IOException) {
            return getNetworkError((IOException) e);
        }
        return new DataError(ErrorState.UNKNOWN_DATA);
    }

    public static NetworkError getNetworkError(IOException e) {
        if (e instanceof UnknownHostException) {
            return new NetworkError(ErrorState.UNKNOWN_HOST);
        } else if (e instanceof ConnectException) {
            return new NetworkError(ErrorState.NETWORK_ERROR);
        } else if (e instanceof NoRouteToHostException) {
            return new NetworkError(ErrorState.NO_ROUTE_TO_HOST_ERROR);
        } else if (e instanceof SocketException) {
            return new NetworkError(ErrorState.SERVER_ERROR);
        } else if (e instanceof SocketTimeoutException) {
            return new NetworkError(ErrorState.TIME_OUT);
        } else {
            return new NetworkError(ErrorState.SERVER_ERROR);
        }
    }

    public static RequestState getErrorState(BaseError error) {
        int errorCode = error.getCode();
        if (errorCode == ErrorState.UNKNOWN_HOST || errorCode == ErrorState.NETWORK_ERROR) {
            return NETWORK_ERROR;
        } else if (errorCode == ErrorState.SERVER_ERROR) {
            return SERVER_ERROR;
        } else if (errorCode == ErrorState.TIME_OUT) {
            return TIME_OUT;
        } else if (errorCode == ErrorState.SESSION_TIME_OUT) {
            return SESSION_TIME_OUT;
        } else if (errorCode == ErrorState.NO_DATA) {
            return NO_DATA;
        } else {
            return FAILED;
        }
    }
    /**
     * 有响应时
     *
     * @param responseStr
     * @param httpCode
     * @return
     */
    public static DataError getError(String responseStr, int httpCode) {
        Logger.e(responseStr);
        //追加所需错误
        if (httpCode == 404) {
            return new DataError(ErrorState.NOT_FOUND);
        }
        CommonResult commonResult = CommonResult.parseResult(responseStr);
        if (commonResult != null) {
            if (commonResult.isSessionTimeOut()) {
                //session 过期特殊处理
                return new DataError(ErrorState.SESSION_TIME_OUT);
            }
            return new BusiError(commonResult, responseStr);
        } else {
            return new DataError(ErrorState.UNKNOWN_DATA, null, responseStr);
        }
    }

    public static DataError getParamError(String message) {
        return new DataError(ErrorState.QUERY_PARAMS_ERROR, message);
    }
}
