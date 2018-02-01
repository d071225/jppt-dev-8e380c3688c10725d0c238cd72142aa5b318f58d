package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hletong.hyc.contract.DictItemContract;
import com.hletong.hyc.http.parse.JpptParse;
import com.hletong.hyc.model.Dictionary;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.error.DataError;
import com.hletong.mob.architect.error.ErrorState;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.http.EasyCallback;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.callback.HttpParser;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;
import com.xcheng.okhttp.request.OkResponse;
import com.xcheng.okhttp.util.ParamUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * 负责加载字典数据
 */
public class DictItemPresenter extends BasePresenter<DictItemContract.View> implements DictItemContract.Presenter {
    private DictItemContract.DictLocalSource mLocalSource;

    public DictItemPresenter(DictItemContract.DictLocalSource localSource, DictItemContract.View view) {
        super(view);
        mLocalSource = localSource;
    }

    @Override
    public void loadDictItems(final String fieldName, final String url) {
        mLocalSource.loadDict(fieldName, new EasyCallback<List<DictionaryItem>>(getView()) {
            @Override
            public void onSuccess(OkCall<List<DictionaryItem>> okCall, List<DictionaryItem> response) {
                if (ParamUtil.isEmpty(response)) {
                    if (url.endsWith(Constant.CARGO_FORECAST_DICTIONARY)) {
                        loadDictionary(fieldName);
                    } else {
                        loadMap(fieldName);
                    }
                } else {
                    getView().showDicts(response);
                }
            }
        });
    }

    private void loadDictionary(final String key) {
        OkRequest request = EasyOkHttp.get(Constant.CARGO_FORECAST_DICTIONARY).build();
        new ExecutorCall<Dictionary>(request).enqueue(new EasyCallback<Dictionary>(getView()) {
            @Override
            public void onSuccess(OkCall<Dictionary> okCall, Dictionary response) {
                final List<DictionaryItem> list = response.getDictionary(key);
                try {
                    mLocalSource.saveDict(response.getDictionaries(), new EasyCallback<CommonResult>(getView()) {
                        @Override
                        public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                            getView().showDicts(list);
                        }
                    });
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void loadMap(final String key) {
        OkRequest request = EasyOkHttp.get(Constant.TRUCK_OPTIONAL_ITEM)
                .parserFactory(new HttpParser.Factory() {
                    @NonNull
                    @Override
                    public HttpParser<?> parser(OkRequest request) {
                        return Parser.INSTANCE;
                    }
                })
                .build();
        new ExecutorCall<Map<String, List<DictionaryItem>>>(request).enqueue(new EasyCallback<Map<String, List<DictionaryItem>>>(getView()) {
            @Override
            public void onSuccess(OkCall<Map<String, List<DictionaryItem>>> okCall, Map<String, List<DictionaryItem>> response) {
                final List<DictionaryItem> list = response.get(key);
                if (ParamUtil.isEmpty(list)) {
                    getView().handleError(new DataError(ErrorState.NO_DATA));
                } else {
                    mLocalSource.saveDict(response, new EasyCallback<CommonResult>(getView()) {
                        @Override
                        public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                            getView().showDicts(list);
                        }
                    });
                }
            }
        });
    }

    //必须为public 否则release版本反射不了
    public static class Parser extends JpptParse<Map<String, List<DictionaryItem>>> {
        public static final Parser INSTANCE = new Parser();

        @Override
        protected OkResponse<Map<String, List<DictionaryItem>>> handleResponse(OkCall<Map<String, List<DictionaryItem>>> okCall, Response response) throws IOException {
            String[] ss = {"truck_type", "plate_color", "bank_type", "etc_vehicle_type", "ship_type", "trailer_axle", "axle_count", "sex"};
            String s = response.body().string();
            Gson gson = new Gson();
            Type type = new TypeToken<List<DictionaryItem>>() {
            }.getType();
            try {
                Map<String, List<DictionaryItem>> map = new HashMap<>();
                JSONArray array = new JSONArray(s);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = (JSONObject) array.get(i);
                    for (int j = 0; j < ss.length; j++) {
                        String key = ss[j];
                        String data = getValue(object, key);
                        if (data != null) {
                            List<DictionaryItem> items = gson.fromJson(data, type);
                            map.put(key, items);
                        }
                    }
                }
                return OkResponse.success(map);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return OkResponse.error(new DataError(ErrorState.NO_DATA, "暂无数据"));
        }

        private String getValue(JSONObject object, String key) {
            try {
                return object.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}
