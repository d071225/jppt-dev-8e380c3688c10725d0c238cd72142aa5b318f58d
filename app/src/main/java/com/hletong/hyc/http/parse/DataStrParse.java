package com.hletong.hyc.http.parse;

import com.hletong.mob.HLApplication;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.model.CommonResult;
import com.orhanobut.logger.Logger;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

/**
 * 解析CommonResult
 * Created by chengxin on 2017/7/1.
 */

public class DataStrParse extends JpptParse<String> {

    public static final DataStrParse INSTANCE = new DataStrParse();

    @Override
    protected OkResponse<String> handleResponse(OkCall<String> okCall, Response response) throws IOException {
        final String cache = response.body().string();
        if (HLApplication.JSON_LOG_ON) {
            Logger.json(cache);
        } else {
            Logger.d(cache);
        }
        CommonResult cr = CommonResult.parseResult(cache);
        if (cr != null) {
            if (cr.isSuccess()) {
                try {
                    JSONObject jsonObject = new JSONObject(cache);
                    Object object = jsonObject.get("data");
                    if (object instanceof JSONArray) {
                        JSONArray jsonArray = (JSONArray) object;
                        if (jsonArray.length() > 0) {
                            return OkResponse.success(jsonArray.getString(0));
                        }
                    } else if (object instanceof String) {
                        return OkResponse.success(object.toString());
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return OkResponse.error(ErrorFactory.getError(cache, response.code()));
    }
}
