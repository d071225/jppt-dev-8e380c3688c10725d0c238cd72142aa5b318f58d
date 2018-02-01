package com.hletong.mob.architect.error;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.net.JSONUtils;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 通用错误处理
 * Created by cx on 2017/2/21.
 */
public final class BusiError extends DataError {
    private CommonResult commonResult;

    public BusiError(@NonNull CommonResult commonResult, String cache) {
        super(ErrorState.BUSINESS_ERROR, commonResult.getErrorInfo(), cache);
        this.commonResult = commonResult;
    }

    /**
     * 获取cache里面的某个字段值
     ***/
    public <T> T getData(String key, TypeToken<T> token) {
        try {
            String response = getResponse();
            if (response != null) {
                JSONObject jsonObject = new JSONObject(response);
                return JSONUtils.fromJson(jsonObject.getString(key), token);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public CommonResult getCommonResult() {
        return commonResult;
    }

    public int getBusiCode() {
        return commonResult.getErrorNo();
    }
}
