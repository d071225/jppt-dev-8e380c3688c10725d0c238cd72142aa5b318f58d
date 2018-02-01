package com.hletong.mob.architect.error;

import com.hletong.mob.architect.requestvalue.ListRequestValue;
import com.hletong.mob.util.StringUtil;
import com.xcheng.okhttp.error.EasyError;

import static com.hletong.mob.architect.error.ErrorState.BUSINESS_ERROR;
import static com.hletong.mob.architect.error.ErrorState.DEFAULT_ERROR;
import static com.hletong.mob.architect.error.ErrorState.ErrorDef;
import static com.hletong.mob.architect.error.ErrorState.NO_DATA;
import static com.hletong.mob.architect.error.ErrorState.SESSION_TIME_OUT;
import static com.hletong.mob.architect.error.ErrorState.getMsg;

/**
 * Created by ddq on 2016/12/22.
 */
public class BaseError extends EasyError {
    //标记当前错误状态
    private int id;
    private int code;

    public BaseError(@ErrorDef int errorCode, String msg) {
        super(checkMsgNonNull(errorCode, msg));
        code = errorCode;
    }

    private static String checkMsgNonNull(@ErrorDef int errorCode, String msg) {
        if (StringUtil.isTrimBlank(msg)) {
            msg = getMsg(errorCode);
            if (msg == null) {
                msg = getMsg(DEFAULT_ERROR);
            }
        }
        return msg;
    }

    public int getCode() {
        return code;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     * 返回false不代表是加载更多
     *
     * @return
     */
    public boolean isRefresh() {
        return id == ListRequestValue.REFRESH;
    }

    public boolean isLoadMore() {
        return id == ListRequestValue.LOAD_MORE;
    }

    public boolean isListRequest() {
        return isRefresh() || isLoadMore();
    }

    public boolean isEmptyError() {
        return getCode() == NO_DATA;
    }

    public boolean isBusiError() {
        return getCode() == BUSINESS_ERROR;
    }

    public boolean isSessionTimeOut() {
        return getCode() == SESSION_TIME_OUT;
    }
}
