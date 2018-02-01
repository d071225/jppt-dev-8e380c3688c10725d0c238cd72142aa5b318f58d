package com.hletong.mob.architect.model;

import android.support.annotation.Nullable;

import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.hletong.mob.net.JSONUtils;

/**
 * 通用的结果处理
 * Created by cc on 2016/10/21.
 */
public class CommonResult {
    public static final int SUCCESS = 0;
    public static final int SESSIONTIMEOUT = 550;
    private int errorNo = -1;
    @SerializedName(value = "errorInfo", alternate = "errorMsg"/*备选解析字段*/)
    private String errorInfo;

    public CommonResult(int errorNo, String errorInfo) {
        this.errorNo = errorNo;
        this.errorInfo = errorInfo;
    }

    public static CommonResult RESULT(boolean success) {
        if (success)
            return new CommonResult(0, "操作成功");
        return new CommonResult(1, "操作失败");
    }

    /**
     * 处理内容包含错误日志的信息
     **/
    private String parseMsg() {
        if (errorInfo != null && errorInfo.contains(":")) {
            boolean isException = errorInfo.contains("exception") || errorInfo.contains("Exception");
            if (isException) {
                String split[] = errorInfo.split(":");
                if (split.length > 1) {
                    return split[1];
                }
            }

        }
        return errorInfo;
    }

    public String getErrorInfo() {
        return parseMsg();
    }

    public int getErrorNo() {
        return errorNo;
    }

    /**
     * 适用于反序列化结果中不需要data的json
     **/
    @Nullable
    public static CommonResult parseResult(String jsonStr) {
        if (CommonResult.isCommonResult(jsonStr)) {
            return JSONUtils.fromJson(jsonStr, getTypeToken());
        }
        return null;
    }

    /**
     * 如果data中有你需要的数据，你不要使用此方法
     **/
    public static TypeToken<CommonResult> getTypeToken() {
        return new TypeToken<CommonResult>() {
        };
    }

    public boolean isSessionTimeOut() {
        return errorNo == SESSIONTIMEOUT;

    }

    public boolean isSuccess() {
        return errorNo == SUCCESS;
    }

    /**
     * 判断返回的结果是否是CommonResult
     **/
    public static boolean isCommonResult(String jsonStr) {
        return jsonStr != null && jsonStr.contains("\"errorNo\":");
    }

    public static boolean isNotSuccess(String jsonStr) {
        CommonResult commonResult = parseResult(jsonStr);
        return commonResult != null && !commonResult.isSuccess();
    }
}
