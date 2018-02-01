package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ddq on 2017/2/3.
 */

public class ETCCard implements Parcelable {
    private boolean available;
    private String title;
    private String content;
    private int avatar;
    private String cardId;

    public ETCCard(boolean available, String title, String content, int avatar) {
        this(available, title, content, avatar, title);//用title当做卡片ID
    }

    public ETCCard(boolean available, String title, String content, int avatar, String cardId) {
        this.available = available;
        this.title = title;
        this.content = content;
        this.avatar = avatar;
        this.cardId = cardId;
    }

    public String getCardId() {
        return cardId;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public int getAvatar() {
        return avatar;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(this.available ? (byte) 1 : (byte) 0);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeInt(this.avatar);
        dest.writeString(this.cardId);
    }

    protected ETCCard(Parcel in) {
        this.available = in.readByte() != 0;
        this.title = in.readString();
        this.content = in.readString();
        this.avatar = in.readInt();
        this.cardId = in.readString();
    }

    public static final Creator<ETCCard> CREATOR = new Creator<ETCCard>() {
        @Override
        public ETCCard createFromParcel(Parcel source) {
            return new ETCCard(source);
        }

        @Override
        public ETCCard[] newArray(int size) {
            return new ETCCard[size];
        }
    };
}
