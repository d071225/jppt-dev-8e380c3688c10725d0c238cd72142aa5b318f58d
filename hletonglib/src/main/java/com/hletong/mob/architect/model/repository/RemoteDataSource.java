package com.hletong.mob.architect.model.repository;

import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.model.DataSource;
import com.hletong.mob.architect.requestvalue.FileRequestValue;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.requestvalue.ListRequestValue;
import com.hletong.mob.architect.requestvalue.MixListRequestValue;
import com.hletong.mob.net.HttpMethod;
import com.hletong.mob.net.OkHttpUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

/**
 * Created by ddq on 2016/12/27.
 * 向服务端发起请求
 */

public class RemoteDataSource implements DataSource {
    @Override
    public <R> void loadItem(ItemRequestValue<R> requestValue, DataCallback<R> callback) {
        ItemResponseHandler<R> itemResponseHandler = new ItemResponseHandler<>(requestValue.getRequestFlag(),
                requestValue.getEntry(),
                requestValue.getTypeToken(),
                requestValue.getTypeAdapter(),
                callback);
        switch (requestValue.getHttpMethod()) {
            case HttpMethod.GET:
                OkHttpUtil.getRequest(
                        requestValue.getUrl(),
                        requestValue.getParams().getObject(),
                        requestValue.getHeaders(),
                        requestValue.getHttpFlag(),
                        itemResponseHandler);
                break;
            case HttpMethod.POST:
                OkHttpUtil.postFormRequest(
                        requestValue.getUrl(),
                        requestValue.getParams().getObject(),
                        requestValue.getHeaders(),
                        requestValue.getHttpFlag(),
                        itemResponseHandler);
                break;
        }
    }

    @Override
    public <R> void loadList(ListRequestValue<R> requestValue, DataCallback<List<R>> callback) {
        ListResponseHandler<R> listResponseHandler = new ListResponseHandler<>(
                requestValue.getRequestFlag(),
                callback,
                requestValue.getTypeToken(),
                requestValue.getEntry());
        switch (requestValue.getHttpMethod()) {
            case HttpMethod.GET:
                OkHttpUtil.getRequest(
                        requestValue.getUrl(),
                        requestValue.getParams().getObject(),
                        requestValue.getHeaders(),
                        requestValue.getHttpFlag(),
                        listResponseHandler);
                break;
            case HttpMethod.POST:
                OkHttpUtil.postFormRequest(
                        requestValue.getUrl(),
                        requestValue.getParams().getObject(),
                        requestValue.getHeaders(),
                        requestValue.getHttpFlag(),
                        listResponseHandler);
                break;
        }
    }

    @Override
    public <R> void uploadFile(ItemRequestValue<R> requestValue, DataCallback<R> callback) {
        JSONObject mObject = requestValue.getParams().getObject();
        if (mObject == null) {
            callback.onError(ErrorFactory.getParamError("上传文件参数出错"));
            return;
        }
        try {
            boolean haveGroupId = mObject.getBoolean("haveGroupID");
            OkHttpUtil.uploadImage(requestValue.getUrl(), mObject.getString("file"), mObject.getString("mode"), haveGroupId ? mObject.getString("groupID") : null, requestValue.getHttpFlag(), new ItemResponseHandler<>(requestValue.getRequestFlag(), requestValue.getEntry(), requestValue.getTypeToken(), requestValue.getTypeAdapter(), callback));
        } catch (JSONException mE) {
            mE.printStackTrace();
            callback.onError(ErrorFactory.getParamError("上传文件参数出错"));
        }
    }

    @Override
    public void loadFile(FileRequestValue requestValue, DataCallback<File> callback) {
        OkHttpUtil.getRequest(
                requestValue.getUrl(),
                requestValue.getParams().getObject(),
                requestValue.getHeaders(),
                requestValue.getHttpFlag(),
                new FileResponseHandler(
                        requestValue.getRequestFlag(),
                        callback,
                        requestValue.getFilePath()));
    }

    @Override
    public void loadMixList(MixListRequestValue requestValue, DataCallback<List<Object>> callback) {
        OkHttpUtil.getRequest(
                requestValue.getUrl(),
                requestValue.getParams().getObject(),
                requestValue.getHeaders(),
                requestValue.getHttpFlag(),
                new ComplexListResponseHandler(
                        requestValue.getRequestFlag(),
                        callback,
                        requestValue.getTypeToken(),
                        requestValue.getEntry(),
                        requestValue.getItemSpecs()));
    }
}
