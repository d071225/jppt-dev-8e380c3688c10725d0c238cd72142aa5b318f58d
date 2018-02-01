package com.hletong.mob.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by dongdaqing on 2017/6/28.
 */

public class HttpUtil {
    private static final String UTF_8 = "utf-8";

    public static String appendParams(String base, Map<String, String> params) {
        if (params == null || params.size() == 0)
            return base;

        StringBuilder builder = getBuilder(base);

        for (Map.Entry<String, String> entry : params.entrySet()) {
            try {
                builder
                        .append(entry.getKey())
                        .append("=")
                        .append(entry.getValue() == null ? "" : URLEncoder.encode(entry.getValue(), UTF_8))
                        .append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        return builder.substring(0, builder.length() - 1);
    }

    public static String appendParams(String base, JSONObject object) {
        if (object == null || object.length() == 0)
            return base;

        StringBuilder builder = getBuilder(base);
        Iterator<String> iterator = object.keys();
        while (iterator.hasNext()) {
            final String key = iterator.next();
            try {
                final String value = object.getString(key);
                if (value != null) {
                    builder
                            .append(key)
                            .append("=")
                            .append(URLEncoder.encode(value, UTF_8))
                            .append("&");
                }
            } catch (JSONException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return builder.substring(0, builder.length() - 1);
    }

    private static StringBuilder getBuilder(String base) {
        StringBuilder builder;
        if (base.endsWith("?"))
            builder = new StringBuilder(base);
        else if (base.endsWith("&"))
            builder = new StringBuilder(base.substring(0, base.length() - 1));
        else
            builder = new StringBuilder(base + "?");
        return builder;
    }
}
