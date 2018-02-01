package com.hletong.hyc.http.parse;

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
import java.util.List;

import okhttp3.Response;

/**
 * Created by chengxin on 2017/7/1.
 */

public class ListParse<T> extends JpptParse<List<T>> {
    public static final ListParse<?> INSTANCE = new ListParse<>();

    @Override
    protected OkResponse<List<T>> handleResponse(OkCall<List<T>> okCall, Response response) throws IOException {
        final String entry = okCall.request().extra(ENTRY);
        final String cache = response.body().string();
        if (HLApplication.JSON_LOG_ON) {
            Logger.json(cache);
        } else {
            Logger.d(cache);
        }
        JSONArray list2Parse = getJsonArray(entry, cache);
        //获取到了对应Key的Value
        if (list2Parse != null) {
            List<T> t = JSONUtils.fromJson(list2Parse.toString(), getToken(okCall));
            if (t == null) {
                return OkResponse.error(new DataError(ErrorState.UNKNOWN_DATA, null, cache));
            } else if (t.size() == 0) {
                return OkResponse.error(new DataError(ErrorState.NO_DATA, null, cache));
            } else {
                return OkResponse.success(t);
            }
        }
        //如果是从CommonResult里面取列表，未获取到了对应Key的Value,
        //或者可能httpCode为200的时候返回CommonResult业务错误信息等
        CommonResult cr = CommonResult.parseResult(cache);
        if (cr != null && cr.isSuccess()) {
            return OkResponse.error(new DataError(ErrorState.NO_DATA, null, cache));
        } else {
            return OkResponse.error(ErrorFactory.getError(cache, response.code()));
        }
    }


    /**
     * 获取json列表对象
     *
     * @param jsonStr
     * @return
     */
    private JSONArray getJsonArray(String entry, String jsonStr) {
        try {
            /**处理挂价摘牌，竞价投标等没有数据的时候返回空json兼容***/
            if (JSONUtils.EMPTY_JSON.equals(jsonStr) || JSONUtils.EMPTY_JSON_ARRAY.equals(jsonStr)) {
                return new JSONArray();
            }
            //返回的JSON就是JsonArray没有任何嵌套
            if (entry != null) {
                return new JSONObject(jsonStr).getJSONArray(entry);
            }
            return new JSONArray(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
