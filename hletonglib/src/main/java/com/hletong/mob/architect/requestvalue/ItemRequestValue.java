package com.hletong.mob.architect.requestvalue;

import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.reflect.TypeToken;
import com.hletong.mob.net.HttpMethod;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.okhttp.util.ParamUtil;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

/**
 * Created by ddq on 2016/12/30.
 * 基本请求参数
 */

public class ItemRequestValue<T> {

    private List<String> headers;
    private ParamsHelper params;
    private String url;
    /**
     * 返回的json中要解析的列表对应的key
     */
    private String entry;
    private TypeToken<T> typeToken;

    @IntDef({HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Method {
    }

    private int httpMethod = HttpMethod.GET;//请求方式，默认GET请求
    private int httpFlag;//用于取消当前请求
    private int requestFlag;//用于区分不同请求
    private Object typeAdapter;//自定义json解析器

    protected ItemRequestValue(int httpFlag, String url, ParamsHelper params, String entry) {
        this(httpFlag, url, params);
        this.entry = entry;
    }

    protected ItemRequestValue(int httpFlag, String url, ParamsHelper params) {
        this.httpFlag = httpFlag;
        this.url = url;
        this.params = params;
    }

    /**
     * 如果没有设置typeToken ，则返回默认的typeToken
     *
     * @return json反序列化所需typToken
     */
    public TypeToken<T> getTypeToken() {
        if (typeToken == null) {
            //setDefault,不在构造函数里面设置避免重复操作
            typeToken = createDefaultToken();
        }
        return typeToken;
    }

    /**
     * 如果子类有对泛型处理，请重写此方法
     *
     * @return 默认的typeToken
     */
    protected TypeToken<T> createDefaultToken() {
        return ParamUtil.createTypeToken(getClass());
    }

    public void setTypeToken(TypeToken<T> typeToken) {
        this.typeToken = typeToken;
    }

    @Nullable
    public String getEntry() {
        return entry;
    }

    public void setRequestFlag(int requestFlag) {
        this.requestFlag = requestFlag;
    }

    public void setHttpMethod(@Method int httpMethod) {
        this.httpMethod = httpMethod;
    }

    @Method
    public int getHttpMethod() {
        return httpMethod;
    }

    public void setTypeAdapter(Object typeAdapter) {
        this.typeAdapter = typeAdapter;
    }

    public Object getTypeAdapter() {
        return typeAdapter;
    }

    public void addParam(String key, Object o) {
        if (params == null)
            params = new ParamsHelper();

        params.put(key, o);
    }

    /***
     * 添加请求头信息
     *
     * @param name  名字
     * @param value 值
     */
    public void addHeader(String name, String value) {
        if (headers == null) {
            headers = new ArrayList<>();
        }
        headers.add(name);
        headers.add(value);
    }

    /***
     * 添加不检查session过期标记
     **/
    public void addNotCheckSessionHeader() {
        addHeader("isCheck", "false");
    }

    public static boolean isCheckHeader(@NonNull Headers headers) {
        return "false".equals(headers.get("isCheck"));
    }

    public int getRequestFlag() {
        return requestFlag;
    }

    public int getHttpFlag() {
        return httpFlag;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public ParamsHelper getParams() {
        if (params == null)
            return new ParamsHelper();
        return params;
    }

    public String getUrl() {
        return url;
    }

    public void setParams(ParamsHelper params) {
        this.params = params;
    }
}
