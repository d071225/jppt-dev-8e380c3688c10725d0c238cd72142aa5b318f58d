package com.hletong.hyc.util;

import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.hletong.hyc.http.parse.DataStrParse;
import com.hletong.mob.architect.error.BusiError;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.callback.HttpParser;
import com.xcheng.okhttp.callback.UICallback;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by chengxin on 2017/7/29.
 */
//获取会员管理单位
public class GetMemberUnit {
    private Object tag;
    private EditText unitCode;
    private TextView unitName;

    public GetMemberUnit(EditText unitCode, TextView unitName, Object tag) {
        this.tag = tag;
        this.unitCode = unitCode;
        this.unitName = unitName;
    }

    public void startListener() {
        unitCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                EasyOkHttp.cancel(tag);
                unitName.setText("");
                request(s.toString());
            }
        });
    }

    private void request(String unitCode) {
        OkRequest okRequest = EasyOkHttp.get(Constant.MEMBER_UNIT)
                .tag(tag)
                .parserFactory(new HttpParser.Factory() {
                    @NonNull
                    @Override
                    public HttpParser<?> parser(OkRequest request) {
                        return DataStrParse.INSTANCE;
                    }
                })
                .param("unitCode", unitCode)
                .build();
        new ExecutorCall<String>(okRequest).enqueue(new UICallback<String>() {
            @Override
            public void onError(OkCall<String> okCall, EasyError error) {
                if (error instanceof BusiError) {
                    unitName.setText("无此会员管理单位，请核实重新输入");
                } else {
                    unitName.setText("网络异常");
                }
            }

            @Override
            public void onSuccess(OkCall<String> okCall, String response) {
                unitName.setText(response);
            }
        });
    }
}
