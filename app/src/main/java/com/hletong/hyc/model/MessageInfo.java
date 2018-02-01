package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hletong.mob.util.SimpleDate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by cc on 2016/11/23.
 */
public class MessageInfo implements Parcelable {
    private String summary;
    private String transaction_code;
    private String call_peer_tel;
    private String create_time;
    private String call_peer;
    private String inform_call_type;
    private String transaction_uuid;
    private String inform_call_uuid;
    private String version;
    private String content;
    private String channel_type;
    private String inform_call_code;
    private String call_start_time;
    private String status;
    private String formatContent;
    private String formatTitle;

    public boolean isShowTip() {
        if (summary != null) {
            try {
                JSONObject jsonObject = new JSONObject(summary);
                String warnFlag = jsonObject.getString("warnFlag");
                if ("1".equals(warnFlag)) {
                    return true;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public String getInform_call_uuid() {
        return inform_call_uuid;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreate_time() {
        return create_time;
    }

    public String getVersion() {
        return version;
    }

    public String getStatus() {
        return status;
    }

    public String getFormatDate() {
        Date date = SimpleDate.parseStrDate(create_time, SimpleDate.formatterHlet);
        return SimpleDate.formatDate(date, SimpleDate.formatterToMin);
    }

    public String getFormatContent() {
        if (formatContent != null)
            return formatContent;

        if (content != null) {
            String result = content;
            int index = result.indexOf("<div class=\"msg-detail-content\">");
            if (index != -1) {
                result = result.substring(index);
                result = result.replace("<div class=\"msg-detail-content\">", "");
                result = result.replace("</div>", "").trim();
                formatContent = result.replaceAll("\\\\n", "<br />");
            } else {
                formatContent = content.trim().replaceAll("\\\\n", "<br />");
            }
        }
        return formatContent;
    }

    public String getFormatTitle() {
        if (formatTitle != null)
            return formatTitle;

        if (content != null) {
            String result = content;
            int index = result.indexOf("</div>");
            if (index != -1) {
                result = result.substring(0, index);
                result = result.replace("<div class=\"msg-summary\">", "");
                result = result.replace("<div>", "");
                result = result.replace("</div>", "").trim();
                formatTitle = result.replaceAll("\\\\n", "");
            } else {
                formatTitle = content.trim().replaceAll("\\\\n", "");
            }
        }
        return formatTitle;
    }

    public boolean isType(String type) {
        return type.equals(inform_call_type);
    }

    private String flag;
    private String flagText;

    public void initFlag() {
        switch (inform_call_type) {
            case "3":
                flag = "装";
                flagText = "装货通知";
                break;
            case "4":
                flag = "卸";
                flagText = "卸货通知";
                break;
            case "5":
                flag = "启";
                flagText = "启程通知";
                break;
            case "6":
                flag = "离";
                flagText = "驶离通知";
                break;
            case "7":
                flag = "缴";
                flagText = "驶费通知";
                break;
            case "8":
                flag = "损";
                flagText = "损溢通知";
                break;
            case "9":
                flag = "守";
                flagText = "守约通知";
                break;
            case "10":
                flag = "违";
                flagText = "违约通知";
                break;
            case "11":
                flag = "变";
                flagText = "卸货地变更通知";
                break;
            case "12":
                flag = "配";
                flagText = "配送通知";
                break;
            case "13":
                flag = "货";
                flagText = "收发货通知单";
                break;
            case "14":
            case "15":
                flag = "竟";
                flagText = "竞价通知";
                break;
            case "16":
            case "17":
                flag = "挂";
                flagText = "挂价通知";
                break;
            case "18":
                flag = "中";
                flagText = "中标通知";
                break;
            case "19":
                flag = "摘";
                flagText = "摘牌通知";
                break;
            case "20":
                flag = "质";
                flagText = "质检通知";
                break;
            case "21":
                flag = "承";
                flagText = "承运通知";
                break;
            case "40":
                flag = "收";
                flagText = "收款通知";
                break;
            case "41":
                flag = "付";
                flagText = "付款通知";
                break;
            case "42":
                flag = "冻";
                flagText = "冻结通知";
                break;
            case "50":
                flag = "收";
                flagText = "收款通知";
                break;
            case "51":
                flag = "付";
                flagText = "付款通知";
                break;
            case "60":
            case "61":
            case "62":
            case "63":
            case "64":
            case "65":
                flag = "自";
                flagText = "自主交易";
                break;
            case "71":
                flag = "告";
                flagText = "平台公告";
                break;
        }
    }

    public String getFlag() {
        if (flag == null) {
            initFlag();
        }
        return flag;
    }

    public String getFlagText() {
        if (flagText == null) {
            initFlag();
        }
        return flagText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.summary);
        dest.writeString(this.transaction_code);
        dest.writeString(this.call_peer_tel);
        dest.writeString(this.create_time);
        dest.writeString(this.call_peer);
        dest.writeString(this.inform_call_type);
        dest.writeString(this.transaction_uuid);
        dest.writeString(this.inform_call_uuid);
        dest.writeString(this.version);
        dest.writeString(this.content);
        dest.writeString(this.channel_type);
        dest.writeString(this.inform_call_code);
        dest.writeString(this.call_start_time);
        dest.writeString(this.status);
        dest.writeString(this.flag);
        dest.writeString(this.flagText);
    }

    public MessageInfo() {
    }

    protected MessageInfo(Parcel in) {
        this.summary = in.readString();
        this.transaction_code = in.readString();
        this.call_peer_tel = in.readString();
        this.create_time = in.readString();
        this.call_peer = in.readString();
        this.inform_call_type = in.readString();
        this.transaction_uuid = in.readString();
        this.inform_call_uuid = in.readString();
        this.version = in.readString();
        this.content = in.readString();
        this.channel_type = in.readString();
        this.inform_call_code = in.readString();
        this.call_start_time = in.readString();
        this.status = in.readString();
        this.flag = in.readString();
        this.flagText = in.readString();
    }

    public static final Creator<MessageInfo> CREATOR = new Creator<MessageInfo>() {
        @Override
        public MessageInfo createFromParcel(Parcel source) {
            return new MessageInfo(source);
        }

        @Override
        public MessageInfo[] newArray(int size) {
            return new MessageInfo[size];
        }
    };
}
