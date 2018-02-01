package com.hletong.hyc.model;

/**
 * Created by dongdaqing on 2017/8/11.
 * bizType目前已有类型如下
 * 100：消息待办任务
 */

public class PushData {
    private String bizType;//主类型
    private String subBizType;//子类型
    private String jsonText;//

    public String getBizType() {
        return bizType;
    }

    public String getSubBizType() {
        return subBizType;
    }

    public String getJsonText() {
        return jsonText;
    }
}
