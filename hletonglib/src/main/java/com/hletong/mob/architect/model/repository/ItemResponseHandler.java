package com.hletong.mob.architect.model.repository;

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

import okhttp3.Response;

/**
 * Created by ddq on 2016/12/27.
 * 处理服务端的响应
 */

class ItemResponseHandler<T> extends ResponseHandler<T> {
    private final TypeToken<T> mTypeToken;
    private final String mEntry;
    private final Object mTypeAdapter;//自定义json解析器
    /***
     * 是否是CommonResult类型的数据
     **/
    private final boolean isCommonType;

    public ItemResponseHandler(int tag, String entry, TypeToken<T> typeToken, Object typeAdapter, DataCallback<T> callback) {
        super(tag, callback);
        this.mTypeToken = typeToken;
        this.mTypeAdapter = typeAdapter;
        this.mEntry = entry;
        isCommonType = CommonResult.class.isAssignableFrom(mTypeToken.getRawType());
    }

    @SuppressWarnings("unchecked")
    @Override
    protected T handleResponse(Response response) throws IOException {
        final String cache = response.body().string();
        if (HLApplication.JSON_LOG_ON) {
            Logger.json(cache);
        } else {
            Logger.d(cache);
        }
        //如果返回的时String类型的数据，直接判断做如下处理
        if (String.class.isAssignableFrom(mTypeToken.getRawType())) {
            CommonResult commonResult = CommonResult.parseResult(cache);
            if (commonResult != null) {
                //如果不成功往正常流程走，不能和上面if合并
                // 跳到String.class.isAssignableFrom(mType.getRawType())外面,
                if (commonResult.isSuccess()) {
                    try {
                        JSONObject jsonObject = new JSONObject(cache);
                        Object object = jsonObject.get("data");
                        if (object instanceof JSONArray) {
                            JSONArray jsonArray = (JSONArray) object;
                            if (jsonArray.length() > 0) {
                                return (T) jsonArray.getString(0);
                            }
                        } else {
                            return (T) object.toString();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            } else {
                return (T) cache;
            }
        }
        if (!isCommonType && CommonResult.isNotSuccess(cache)) {
            /**如果token不是CommonResult，此时出错的话返回CommonResult
             * 如果此时状态码是200，返回{ errorNo:xxxx,errorMsg：xxxx},
             * 也能映射成T,只不过此时的T里面的数据都是默认值，无效结果
             */
            CommonResult cr = CommonResult.parseResult(cache);
            if (cr != null && cr.isSuccess()) {
                //用户请求的不是CommResult，但返回的数据用CommonResult包裹起来了
                setError(new DataError(ErrorState.NO_DATA, null, cache));
            } else {
                setError(ErrorFactory.getError(cache, response.code()));
            }
            return null;
        }
        String data2Parse = getEntryJson(cache);
        boolean isSuccess = false;
        T t = JSONUtils.fromJson(data2Parse, mTypeToken, mTypeAdapter);
        if (t != null) {
            /**如果是CommonResult的情况通过判断errorNo==0决定是否是成功操作**/
            isSuccess = !isCommonType || CommonResult.class.cast(t).isSuccess();
        }
        if (!isSuccess) {
            setError(ErrorFactory.getError(cache, response.code()));
            return null;
        } else {
            return t;
        }
    }

    public String getEntryJson(String jsonStr) {
        String realData = jsonStr;//真正的用于解析的数据
        try {
            if (mEntry != null) {//key非空的情况下jsonStr一定不是数组，如果真的是数组那也不解析
                JSONObject object = new JSONObject(jsonStr);
                realData = object.getString(mEntry);
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
            handleException(e);
        }
        return null;
    }

    private boolean isJsonArray(String value) {
        return value != null && value.startsWith("[") && value.endsWith("]");
    }
}
