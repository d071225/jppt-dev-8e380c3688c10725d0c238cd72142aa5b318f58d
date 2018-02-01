package com.hletong.mob.architect.error;

import static com.hletong.mob.architect.error.ErrorState.DEFAULT_ERROR;
import static com.hletong.mob.architect.error.ErrorState.DataErrorDef;


/**
 * 通用错误处理
 * Created by cx on 2017/2/21.
 */
public class DataError extends BaseError {
    private String response;

    public DataError(@DataErrorDef int errorCode, String message, String response) {
        super(errorCode, message);
        this.response = response;
    }

    public DataError(@DataErrorDef int errorCode, String message) {
        this(errorCode, message, null);
    }

    public DataError(@DataErrorDef int errorCode) {
        this(errorCode, null);
    }

    /**
     * 简单的提示信息,没有错误码
     **/
    public DataError(String message) {
        super(DEFAULT_ERROR, message);
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
}
