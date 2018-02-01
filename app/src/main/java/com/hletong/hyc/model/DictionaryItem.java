package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.hletong.mob.dialog.selector.IItemShow;

import java.io.Serializable;

/**
 * Created by ddq on 2017/3/10.
 */
public class DictionaryItem implements IItemShow, Serializable, Parcelable {
    private static final long serialVersionUID = -2779789605891411368L;

    private String id;
    private String text;

    public DictionaryItem(String id, String text) {
        this.id = id;
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public int getIdAsInt() {
        try {
            return Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return Integer.MIN_VALUE;
    }

    public String getText() {
        return text;
    }

    @Override
    public String getValue() {
        return text;
    }


    @Override
    public boolean equals(Object obj) {
        String s = null;
        if (obj instanceof String)
            s = (String) obj;
        else if (obj instanceof DictionaryItem) {
            DictionaryItem type = (DictionaryItem) obj;
            s = type.getId();
        }
        return getId().equals(s);
    }

    public static String getDictId(@Nullable DictionaryItem item) {
        if (item != null) {
            return item.getId();
        }
        return "";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.text);
    }

    protected DictionaryItem(Parcel in) {
        this.id = in.readString();
        this.text = in.readString();
    }

    public static final Creator<DictionaryItem> CREATOR = new Creator<DictionaryItem>() {
        @Override
        public DictionaryItem createFromParcel(Parcel source) {
            return new DictionaryItem(source);
        }

        @Override
        public DictionaryItem[] newArray(int size) {
            return new DictionaryItem[size];
        }
    };
}
