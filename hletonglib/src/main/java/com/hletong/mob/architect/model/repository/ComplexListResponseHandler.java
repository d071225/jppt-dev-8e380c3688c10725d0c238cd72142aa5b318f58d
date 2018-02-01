package com.hletong.mob.architect.model.repository;

import android.support.annotation.NonNull;

import com.google.gson.reflect.TypeToken;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.model.MixItemSpec;
import com.hletong.mob.net.JSONUtils;
import com.xcheng.okhttp.util.ParamUtil;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ddq on 2016/12/27.
 * 混合列表解析
 */

class ComplexListResponseHandler extends ListResponseHandler<Object> {
    private ArrayList<MixItemSpec> mItemSpecs;

    public ComplexListResponseHandler(int tag, DataCallback<List<Object>> callback, TypeToken<List<Object>> typeToken, String entry, ArrayList<MixItemSpec> itemSpecs) {
        super(tag, callback, typeToken, entry);
        this.mItemSpecs = itemSpecs;
    }

    @Override
    protected List<Object> parseList(@NonNull JSONArray list2Parse) {
        if (ParamUtil.isEmpty(mItemSpecs)) {
            setError(ErrorFactory.getParamError("用于解析数据的参数有误"));
            return null;
        }
        List<Object> mixObjects = new ArrayList<>();
        for (int i = 0; i < list2Parse.length(); i++) {
            try {
                String jsonObject = list2Parse.getString(i);
                for (MixItemSpec mixItem : mItemSpecs) {
                    String spec = mixItem.getSpec();
                    if (jsonObject.contains(spec)) {
                        Object result = JSONUtils.fromJson(jsonObject, mixItem.getTypeToken());
                        if (result != null) {
                            mixObjects.add(result);
                        }
                        break;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return mixObjects;
//
//        List<Object> mResult;
//        ArrayMap<Type, ArrayList<String>> set = new ArrayMap<>();
//
//        for (int i = 0; i < list2Parse.length(); i++) {
//            try {
//                String object = list2Parse.getString(i);
//                //将列表根据数据特征值进行归类
//                for (int j = 0; j < mItemSpecs.size(); j++) {
//                    MixItemSpec mSpec = mItemSpecs.get(j);
//                    String spec = mSpec.getSpec();
//                    if (object.contains(spec)) {
//                        ArrayList<String> mList = set.get(mSpec.getType());
//                        if (mList == null) {
//                            mList = new ArrayList<>();
//                            set.put(mSpec.getType(), mList);
//                        }
//                        mList.add(object);
//                        break;
//                    }
//                }
//            } catch (JSONException e) {
//                handleException(e);
//            }
//        }
//
//        Gson mGson = new Gson();
//        mResult = new ArrayList<>();
//        ArrayList<List> tmp = new ArrayList<>();
//        for (Map.Entry<Type, ArrayList<String>> mEntry : set.entrySet()) {
//            JSONArray mJSONArray = new JSONArray();
//            ArrayList<String> mValue = mEntry.getValue();
//            for (String s : mValue) {
//                mJSONArray.put(s);
//            }
//            StringBuilder mBuilder = new StringBuilder();
//            mBuilder.append("[");
//            for (int i = 0; i < mJSONArray.length(); i++) {
//                try {
//                    mBuilder.append(mJSONArray.get(i));
//                } catch (JSONException mE) {
//                    handleException(mE);
//                }
//                if (i == mJSONArray.length() - 1) {
//                    mBuilder.append("]");
//                } else {
//                    mBuilder.append(",");
//                }
//            }
//
//            try {
//                List mList = mGson.fromJson(mBuilder.toString(), mEntry.getKey());
//                tmp.add(mList);
//            } catch (Exception e) {
//                handleException(e);
//            }
//        }
//
//        for (List aTmp : tmp) {
//            mResult.addAll(aTmp);
//        }
//
//        return mResult;
    }
}
