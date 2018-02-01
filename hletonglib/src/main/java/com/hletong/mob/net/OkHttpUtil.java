package com.hletong.mob.net;

import android.support.annotation.NonNull;

import com.hletong.mob.security.EncryptUtils;
import com.hletong.mob.util.AppManager;
import com.hletong.mob.util.HttpUtil;
import com.orhanobut.logger.Logger;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.util.Https;
import com.xcheng.okhttp.util.ParamUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * OKHttp 帮助类
 * Created by xincheng on 2016/7/21.
 */
public class OkHttpUtil {
    private volatile static OkHttpClient sOkHttpClient;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /***
     * init in application
     **/
    public static void initHttp(long connectTimeout, long readTimeout, long writeTimeout) {
        //创建OkHttpClient对象，用于稍后发起请求
        sOkHttpClient = new OkHttpClient.Builder().cookieJar(
                /*统一Cookie入口**/
                getCookieJar())
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .build();
    }


    /**
     * 初始化有https请求的OkHttpClient
     *
     * @param connectTimeout
     * @param readTimeout
     * @param writeTimeout
     * @param CER            证书字符串
     */
    public static void initHttps(long connectTimeout, long readTimeout, long writeTimeout, @NonNull String CER) {
        //创建OkHttpClient对象，用于稍后发起请求
        Https.SSLParams sslParams = Https.getSslSocketFactory(new Buffer()
                .writeUtf8(CER)
                .inputStream());
        //创建OkHttpClient对象，用于稍后发起请求
        sOkHttpClient = new OkHttpClient.Builder().cookieJar(
                /*统一Cookie入口**/
                getCookieJar()).sslSocketFactory(sslParams.sSLSocketFactory, sslParams.trustManager)
                  /*忽略hostname验证**/
                .hostnameVerifier(Https.UNSAFE_HOSTNAME_VERIFIER)
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .build();
    }


    private static CookieJar getCookieJar() {
        return new CookieJar() {
            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                for (Cookie cookie : cookies) {
                    if (cookie.name().equals("__sid")) {
                        AppManager.setCookie(cookie.name() + "=" + cookie.value());
                        break;
                    }
                }
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = new ArrayList<>(0);
                String setCookie = AppManager.getCookie();
                if (setCookie != null) {
                    Cookie cookie = Cookie.parse(url, setCookie);
                    cookies.add(cookie);
                }
                return cookies;
            }
        };
    }

    public static OkHttpClient getOkHttpClient() {
        if (sOkHttpClient == null) {
            throw new IllegalStateException("please call OkHttpUtil.initHttp() before getOkHttpClient");
        }
        return sOkHttpClient;
    }

    public static void getRequest(String url, int hashCodeTag, Callback callback) {
        getRequest(url, null, hashCodeTag, callback);
    }

    public static void uploadImage(String url, String image, String mode, String groupId, int hashCode, Callback callback) {
        try {
            Logger.d(String.format(Locale.ENGLISH, "upload image => %s, mode => %s, Group => %s", EncryptUtils.md5Encrypt(image), mode, groupId));
            Request.Builder builder = new Request.Builder();
            //根据Request对象发起Get异步Http请求，并添加请求回调
            Call call = sOkHttpClient.newCall(
                    builder.tag(hashCode)
                            .url(url)
                            .post(getRequestBodyForImage(image, mode, groupId))
                            .build()
            );
            call.enqueue(callback);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static RequestBody getRequestBodyForImage(String image, String mode, String groupId) {
        File file = new File(image);
        MultipartBody.Builder builder = new MultipartBody.Builder();
        builder.setType(MultipartBody.FORM)
                .addFormDataPart("file_upload_mode", mode)
                .addFormDataPart("file_pre", "android/")
                .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file));

        if (groupId != null) {
            builder.addFormDataPart("file_group_id", groupId);
        }
        return builder.build();
    }

    public static void getRequest(String url, JSONObject requestJson, int hashCodeTag, Callback callback) {
        getRequest(url, requestJson, null, hashCodeTag, callback);
    }

    public static void getRequest(String url, JSONObject requestJson, List<String> headers, int hashCodeTag, Callback callback) {
        Request.Builder builder = new Request.Builder().tag(hashCodeTag).url(HttpUtil.appendParams(url, requestJson));
        //根据请求URL创建一个Request对象
        if (!ParamUtil.isEmpty(headers)) {
            Headers.Builder hb = new Headers.Builder();
            for (int index = 0; index < headers.size(); index += 2) {
                hb.add(headers.get(index), headers.get(index + 1));
            }
            builder.headers(hb.build());
        }

        //根据Request对象发起Get异步Http请求，并添加请求回调
        Call call = sOkHttpClient.newCall(builder.build());
        call.enqueue(callback);
    }
    /**
     * json提交
     */
    public static void postJsonRequest(String url, JSONObject requestJson, List<String> headers, int hashCodeTag, Callback callback) {
        //通过FormBody对象添加多个请求参数键值对
        RequestBody jsonBody = RequestBody.create(JSON, requestJson.toString());
        //通过请求地址和请求体构造Post请求对象Request
        Request request;
        if (ParamUtil.isEmpty(headers)) {
            request = new Request.Builder().tag(hashCodeTag).url(url).post(jsonBody).build();
        } else {
            Headers.Builder headerBuilder = new Headers.Builder();
            for (int index = 0; index < headers.size(); index += 2) {
                headerBuilder.add(headers.get(index), headers.get(index + 1));
            }
            request = new Request.Builder().tag(hashCodeTag).url(url).headers(headerBuilder.build()).post(jsonBody).build();
        }
        Call call = sOkHttpClient.newCall(request);
        call.enqueue(callback);
    }

    /**
     * form表单提交
     */
    public static void postFormRequest(String url, JSONObject requestJson, List<String> headers, int hashCodeTag, Callback callback) {
        //通过FormBody对象添加多个请求参数键值对
        RequestBody formBody = getRequestBody(requestJson);
        //通过请求地址和请求体构造Post请求对象Request
        Request request = new Request.Builder().tag(hashCodeTag).url(url).post(formBody).build();
        Call call = sOkHttpClient.newCall(request);
        call.enqueue(callback);
    }

    /**
     * 生成Post请求的body
     *
     * @param requestJson 请求的json数据
     * @return 返回RequestBody
     */
    public static RequestBody getRequestBody(JSONObject requestJson) {
        FormBody.Builder builder = new FormBody.Builder();
        try {
            //先遍历整个 requestJson 对象
            for (Iterator<String> iter = requestJson.keys(); iter.hasNext(); ) {
                String key = iter.next();
                builder.add(key, requestJson.getString(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return builder.build();
    }

    //取消请求
    public static void cancel(Object tag) {
        for (Call call : sOkHttpClient.dispatcher().queuedCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        for (Call call : sOkHttpClient.dispatcher().runningCalls()) {
            if (tag.equals(call.request().tag())) {
                call.cancel();
            }
        }
        EasyOkHttp.cancel(tag);
        EventSessionTimeout.cancel(tag);
    }

    /**
     * 取消所有请求
     */
    public static void cancelAll() {
        sOkHttpClient.dispatcher().cancelAll();
        EasyOkHttp.cancelAll();
        EventSessionTimeout.cancelAll();
    }
}
