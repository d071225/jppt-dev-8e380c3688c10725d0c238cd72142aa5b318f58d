package com.hletong.hyc.model;

import com.hletong.mob.util.SimpleDate;

/**
 * Created by ddq on 2016/11/24.
 */
public class HistoryOrderDetail {
    private String create_ts;//创建时间
    private String content;//说明

    public void setCreate_ts(String create_ts) {
        this.create_ts = create_ts;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreate_ts() {
        return create_ts;
    }

    public String getContent() {
        return content;
    }

    public String getCreateTime(){
        SimpleDate sd = SimpleDate.parseMilliSecondsFormat(create_ts);
        if (sd != null)
            return sd.chineseFormat() + " " + sd.timeString(true,":");
        return null;
    }
}
