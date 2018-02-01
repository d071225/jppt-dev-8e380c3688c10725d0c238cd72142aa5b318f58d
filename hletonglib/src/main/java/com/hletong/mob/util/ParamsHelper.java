package com.hletong.mob.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by ddq on 2017/3/24.
 */

public class ParamsHelper {
    private JSONObject mObject;

    public ParamsHelper() {
        mObject = new JSONObject();
    }

    public ParamsHelper(JSONObject object) {
        this.mObject = object;
    }

    public ParamsHelper(HashMap<String, String> map) {
        this.mObject = new JSONObject(map);
    }

    public Map<String, String> getMaps() {
        Map<String, String> params = new HashMap<>();
        Iterator<String> iterator = mObject.keys();
        while (iterator.hasNext()) {
            final String key = iterator.next();
            try {
                params.put(key, mObject.getString(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return params;
    }

    public ParamsHelper put(ParamsHelper helper) {
        Iterator<String> iterator = helper.getObject().keys();
        while (iterator.hasNext()) {
            final String key = iterator.next();
            try {
                mObject.put(key, helper.get(key));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public ParamsHelper put(String key, Object value) {
        //过滤空数据
        if (value == null)
            return this;
        try {
            mObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public ParamsHelper putNull(String key) {
        try {
            mObject.put(key, null);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }

    public JSONObject getObject() {
        return mObject;
    }

    public String get(String key) {
        if (mObject != null) {
            try {
                return mObject.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public int getInt(String key) {
        if (mObject != null) {
            try {
                return mObject.getInt(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    public String getAsString() {
        return mObject.toString();
    }
}
