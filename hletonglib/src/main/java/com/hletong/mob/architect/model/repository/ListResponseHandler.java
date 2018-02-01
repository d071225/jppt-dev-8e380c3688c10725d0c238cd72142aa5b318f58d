package com.hletong.mob.architect.model.repository;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.hletong.mob.HLApplication;
import com.hletong.mob.architect.error.DataError;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.error.ErrorState;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.net.JSONUtils;
import com.orhanobut.logger.Logger;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Response;

/**
 * Created by ddq on 2016/12/30.
 * 将服务端返回的结果解析成为List<?>格式
 */

class ListResponseHandler<T> extends ResponseHandler<List<T>> {
    private final TypeToken<List<T>> mTypeToken;
    private final String mEntry;

    public ListResponseHandler(int tag, DataCallback<List<T>> callback, TypeToken<List<T>> typeToken, String entry) {
        super(tag, callback);
        this.mTypeToken = typeToken;
        this.mEntry = entry;
    }

    @Override
    protected final List<T> handleResponse(Response response) throws IOException {
        final String cache = response.body().string();
        if (HLApplication.JSON_LOG_ON) {
            Logger.json(cache);
        } else {
            Logger.d(cache);
        }
        JSONArray list2Parse = getJsonArray(cache);
        //获取到了对应Key的Value
        if (list2Parse != null) {
            List<T> t = parseList(list2Parse);
            if (t == null) {
                setError(new DataError(ErrorState.UNKNOWN_DATA, null, cache));
                return null;
            } else if (t.size() == 0) {
                setError(new DataError(ErrorState.NO_DATA, null, cache));
                return null;
            } else {
                return t;
            }
        }
        //如果是从CommonResult里面取列表，未获取到了对应Key的Value,
        //或者可能httpCode为200的时候返回CommonResult业务错误信息等
        CommonResult cr = CommonResult.parseResult(cache);
        if (cr != null && cr.isSuccess())
            setError(new DataError(ErrorState.NO_DATA, null, cache));
        else
            setError(ErrorFactory.getError(cache, response.code()));
        return null;
    }

    /**
     * json数组转换成列表对象
     *
     * @param list2Parse JSONArray对象
     * @return 返回列表
     */
    protected List<T> parseList(@NonNull JSONArray list2Parse) {
        return JSONUtils.fromJson(list2Parse.toString(), mTypeToken);
    }

    /**
     * 获取json列表对象
     *
     * @param jsonStr
     * @return
     */
    private JSONArray getJsonArray(String jsonStr) {
        try {
            /**处理挂价摘牌，竞价投标等没有数据的时候返回空json兼容***/
            if (JSONUtils.EMPTY_JSON.equals(jsonStr) || JSONUtils.EMPTY_JSON_ARRAY.equals(jsonStr)) {
                return new JSONArray();
            }
            //返回的JSON就是JsonArray没有任何嵌套
            if (mEntry != null) {
                return new JSONObject(jsonStr).getJSONArray(mEntry);
            }
            return new JSONArray(jsonStr);
        } catch (JSONException e) {
            handleException(e);
            return null;
        }
    }
}
