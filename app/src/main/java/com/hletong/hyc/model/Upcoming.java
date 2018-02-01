package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.hletong.mob.util.SimpleDate;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

/**
 * Created by ddq on 2016/12/14.
 * 待办任务
 */
public class Upcoming implements Parcelable {
    private String status_;
    private String moduleType;
    private String userCode_;
    private String description;
    private String endDt;
    private String remark;
    private String refUuid;
    private int type;
    private String params;
    private String uuid;
    private String createDt;
    private int version;
    private String url;
    private String userCode;
    private String openType;
    private String executor;
    private String userIds;
    private String name;
    private String create_date;
    private String attachId;
    private String type_;
    private String status;

    private ZhiYaDan mZhiYaDan;
    private QuoteUpcoming quoteUpcoming;
    private PersonalCollection mCollection;

    public String getShortType() {
        switch (type) {
            case 11:
                return "违约";
            case 12:
                return "守约";
            case 14:
                return "滞压";
            case 50:
                return "运单";
            case 61:
            case 62:
                return "合同";
            case 200://货方-自主交易结束确认
            case 201://车船-自主交易结束确认
            case 203://预摘牌
                return "自主";
            case 204://货方议价
                return "议价";
            case 205://车船报价
                return "报价";
            case 300://会员评价
                return "评价";
        }
        return "";
    }

    public int getType() {
        return type;
    }

    public String getType_() {
        if (type == 200 || type == 201)
            return "交易确认单";
        if (type == 203)
            return "申请垫付保证金";
        if (type == 205)
            return "议价进度";
        if (type == 300)
            return "会员评价";
        return type_;
    }

    public String getFormatDate() {
        Date date = SimpleDate.parseStrDate(createDt, SimpleDate.formatterHlet);
        return SimpleDate.formatDate(date, SimpleDate.formatterToMin);
    }

    public String getDescription() {
        if (type == 14) {
            return getZhiYaDan().getContentDescription();
        } else if (type == 200 || type == 201 || type == 203)
            return name;
        else if (type == 204 || type == 205)//报价
            return getQuoteEntity().toString();
        return description;
    }

    public PersonalCollection getCollection() {
        if (type == 200 && mCollection == null)
            mCollection = new Gson().fromJson(params, PersonalCollection.class);
        return mCollection;
    }

    public String getRefUuid() {
        return refUuid;
    }

    public ZhiYaDan getZhiYaDan() {
        if (type == 14 && mZhiYaDan == null) {
            Gson gson = new Gson();
            mZhiYaDan = gson.fromJson(description, ZhiYaDan.class);
        }
        return mZhiYaDan;
    }

    public QuoteUpcoming getQuoteEntity() {
        if ((type == 204 || type == 205) && quoteUpcoming == null) {
            Gson gson = new Gson();
            quoteUpcoming = gson.fromJson(params, QuoteUpcoming.class);
        }
        return quoteUpcoming;
    }

    public String getStringFromParams(String key) {
        if (params == null || key == null)
            return null;

        try {
            JSONObject mObject = new JSONObject(params);
            return mObject.getString(key);
        } catch (JSONException mE) {
            mE.printStackTrace();
        }
        return null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status_);
        dest.writeString(this.moduleType);
        dest.writeString(this.userCode_);
        dest.writeString(this.description);
        dest.writeString(this.endDt);
        dest.writeString(this.remark);
        dest.writeString(this.refUuid);
        dest.writeInt(this.type);
        dest.writeString(this.params);
        dest.writeString(this.uuid);
        dest.writeString(this.createDt);
        dest.writeInt(this.version);
        dest.writeString(this.url);
        dest.writeString(this.userCode);
        dest.writeString(this.openType);
        dest.writeString(this.executor);
        dest.writeString(this.userIds);
        dest.writeString(this.name);
        dest.writeString(this.create_date);
        dest.writeString(this.attachId);
        dest.writeString(this.type_);
        dest.writeString(this.status);
        dest.writeParcelable(this.mZhiYaDan, flags);
    }

    public Upcoming() {
    }

    protected Upcoming(Parcel in) {
        this.status_ = in.readString();
        this.moduleType = in.readString();
        this.userCode_ = in.readString();
        this.description = in.readString();
        this.endDt = in.readString();
        this.remark = in.readString();
        this.refUuid = in.readString();
        this.type = in.readInt();
        this.params = in.readString();
        this.uuid = in.readString();
        this.createDt = in.readString();
        this.version = in.readInt();
        this.url = in.readString();
        this.userCode = in.readString();
        this.openType = in.readString();
        this.executor = in.readString();
        this.userIds = in.readString();
        this.name = in.readString();
        this.create_date = in.readString();
        this.attachId = in.readString();
        this.type_ = in.readString();
        this.status = in.readString();
        this.mZhiYaDan = in.readParcelable(ZhiYaDan.class.getClassLoader());
    }

    public static final Creator<Upcoming> CREATOR = new Creator<Upcoming>() {
        @Override
        public Upcoming createFromParcel(Parcel source) {
            return new Upcoming(source);
        }

        @Override
        public Upcoming[] newArray(int size) {
            return new Upcoming[size];
        }
    };
}
