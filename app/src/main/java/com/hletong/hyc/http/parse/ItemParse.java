package com.hletong.hyc.http.parse;

import com.google.gson.reflect.TypeToken;
import com.hletong.mob.HLApplication;
import com.hletong.mob.architect.error.DataError;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.error.ErrorState;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.net.JSONUtils;
import com.orhanobut.logger.Logger;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Response;

/**
 * Created by chengxin on 2017/7/1.
 */

public class ItemParse<T> extends JpptParse<T> {
    public static final ItemParse<?> INSTANCE=new ItemParse<>();
    @Override
    protected OkResponse<T> handleResponse(OkCall<T> okCall, Response response) throws IOException {
        final TypeToken<T> typeToken = getToken(okCall);
        final boolean isCommonType = CommonResult.class.isAssignableFrom(typeToken.getRawType());
        final String entry = okCall.request().extra(ENTRY);
        final String cache = response.body().string();
        Logger.d(okCall.request().url());
        if (HLApplication.JSON_LOG_ON) {
            Logger.json(cache);
        } else {
            Logger.d(cache);
        }
        if (!isCommonType && CommonResult.isNotSuccess(cache)) {
            /**如果token不是CommonResult，此时出错的话返回CommonResult
             * 如果此时状态码是200，返回{ errorNo:xxxx,errorMsg：xxxx},
             * 也能映射成T,只不过此时的T里面的数据都是默认值，无效结果
             */
            CommonResult cr = CommonResult.parseResult(cache);
            if (cr != null && cr.isSuccess()) {
                //用户请求的不是CommResult，但返回的数据用CommonResult包裹起来了
                return OkResponse.error(new DataError(ErrorState.NO_DATA, null, cache));
            } else {
                return OkResponse.error(ErrorFactory.getError(cache, response.code()));
            }
        }
        String data2Parse = getEntryJson(entry, cache);
        boolean isSuccess = false;
        T t = JSONUtils.fromJson(data2Parse, typeToken);
        if (t != null) {
            /**如果是CommonResult的情况通过判断errorNo==0决定是否是成功操作**/
            isSuccess = !isCommonType || CommonResult.class.cast(t).isSuccess();
        }
        if (!isSuccess) {
            return OkResponse.error(ErrorFactory.getError(cache, response.code()));
        } else {
            return OkResponse.success(t);
        }
    }

    private String getEntryJson(String entry, String jsonStr) {
        String realData = jsonStr;//真正的用于解析的数据
        try {
            if (entry != null) {//key非空的情况下jsonStr一定不是数组，如果真的是数组那也不解析
                JSONObject object = new JSONObject(jsonStr);
                realData = object.getString(entry);
            }
            //解析是数组的情况
            if (isJsonArray(realData)) {
                JSONArray array = new JSONArray(realData);
                if (array.length() > 0) {//内容非空
                    final String data = array.getString(0);
                    if (data == null || data.length() == 0 || "null".equals(data))
                        return null;
                    return data;
                } else {
                    return null;
                }
            }
            //非数组直接返回
            return realData;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isJsonArray(String value) {
        return value != null && value.startsWith("[") && value.endsWith("]");
    }
}
