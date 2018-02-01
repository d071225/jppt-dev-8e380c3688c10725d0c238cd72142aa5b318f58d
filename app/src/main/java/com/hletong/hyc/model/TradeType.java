package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ddq on 2017/3/13.
 */

public class TradeType implements Parcelable {
    private int type;
    private String title;
    private String description;
    private boolean available;
    private String disableMessage;
    private boolean reviewing;//是否正在审核，只有平台开票才有
    private boolean needUploadPaper;//是否需要上传资料，平开和自开都有
    private boolean notAuthorized;//是否是未授权，只有自开有

    public TradeType(int type, String title, String description, boolean available, String disableMessage, boolean reviewing, boolean needUploadPaper, boolean notAuthorized) {
        this.type = type;
        this.title = title;
        this.description = description;
        this.available = available;
        this.disableMessage = disableMessage;
        this.reviewing = reviewing;
        this.needUploadPaper = needUploadPaper;
        this.notAuthorized = notAuthorized;
    }

    public DictionaryItem getAsDictionaryItem() {
        return new DictionaryItem(String.valueOf(type), title);
    }

    public static TradeType getPlatform(AuthInfo authInfo, boolean reviewing, String reviewMessage) {
        return new TradeType(1, "平台开票", "通过平台结算运费并开具发票的场内交易模式。", authInfo.isAuthorized(), reviewing ? reviewMessage : authInfo.getAuth_msg(), reviewing, !authInfo.isAuthorized() && !reviewing, false);
    }

    public static TradeType getSelfTrade(AuthInfo authInfo) {
        return new TradeType(3, "自主交易", "货方会员自主定价或货运双方自主议价的场外交易模式", true, authInfo.getAuth_msg(), false, false, false);
    }

    public static TradeType getOneSelf(AuthInfo authInfo) {
        return new TradeType(2, "自主开票", "通过平台/自主结算运费并自主开票的场内交易模式。", authInfo.isAuthorized(), authInfo.getAuth_msg(), false, "0".equals(authInfo.getAuth_flag()), "2".equals(authInfo.getAuth_flag()));
    }

    public static DictionaryItem getDictionaryById(int id) {
        switch (id) {
            case 1:
                return new DictionaryItem("1", "平台开票");
            case 2:
                return new DictionaryItem("2", "自主开票");
            case 3:
                return new DictionaryItem("3", "自主交易");
        }
        return null;
    }

    public boolean isAvailable() {
        return available;
    }

    public int getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDisableMessage() {
        return disableMessage;
    }

    public boolean isReviewing() {
        return reviewing;
    }

    public boolean isNeedUploadPaper() {
        return needUploadPaper;
    }

    public boolean isNotAuthorized() {
        return notAuthorized;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.type);
        dest.writeString(this.title);
        dest.writeString(this.description);
        dest.writeByte(this.available ? (byte) 1 : (byte) 0);
        dest.writeString(this.disableMessage);
    }

    protected TradeType(Parcel in) {
        this.type = in.readInt();
        this.title = in.readString();
        this.description = in.readString();
        this.available = in.readByte() != 0;
        this.disableMessage = in.readString();
    }

    public static final Creator<TradeType> CREATOR = new Creator<TradeType>() {
        @Override
        public TradeType createFromParcel(Parcel source) {
            return new TradeType(source);
        }

        @Override
        public TradeType[] newArray(int size) {
            return new TradeType[size];
        }
    };
}
