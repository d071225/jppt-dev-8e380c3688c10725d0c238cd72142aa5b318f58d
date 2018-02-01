package com.hletong.mob.architect.requestvalue;

import com.google.gson.reflect.TypeToken;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.okhttp.util.ParamUtil;

import java.util.List;

/**
 * 列表请求入参
 * Created by ddq on 2016/12/29.
 */

public class ListRequestValue<T> extends ItemRequestValue<List<T>> {
    public static final int REFRESH = -1;
    public static final int LOAD_MORE = -2;

    public void setRefresh(boolean isRefresh) {
        setRequestFlag(isRefresh ? REFRESH : LOAD_MORE);
    }

    /***
     * entry默认值为"list"
     **/
    protected ListRequestValue(int httpFlag, String url, ParamsHelper params) {
        this(httpFlag, url, params, "list");
    }

    /**
     * 如果需要指定mEntry ，调用此构造函数
     **/
    protected ListRequestValue(int httpFlag, String url, ParamsHelper params, String entry) {
        super(httpFlag, url, params, entry);
    }

    @Override
    protected TypeToken<List<T>> createDefaultToken() {
        return ParamUtil.createListTypeToken(getClass());
    }
}
