package com.hletong.hyc.model;

import android.os.Parcel;

import com.hletong.mob.util.SimpleDate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by ddq on 2016/11/9.
 * 合同列表项
 */
public class ContractItemForHistoryList extends Source{

    private String contract_uuid ;// 合同流水号
    private String contract_code ;// 合同编号
    private String carrier_no;//车牌号
    private double contract_qt ;// 合同/协议货源量(计费依据为0-重量，则为重量的值，为1-数量，则为数量的值)

    public String getContract_uuid() {
        return contract_uuid;
    }

    public String getContract_code() {
        return contract_code;
    }

    public String getCreateTimeInChinese(){
        /////////////////////////////////////////////2016-11-15 11:44:10.0
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.PRC);
        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(getCreate_ts()));
            SimpleDate sd = new SimpleDate(calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH) + 1,calendar.get(Calendar.DAY_OF_MONTH));
            return sd.chineseFormat();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public String getPlate() {
        return carrier_no;
    }

    @Override
    public double getWeight() {
        if (getBook_ref_type() == 0)
            return contract_qt;
        return 0;
    }

    @Override
    public double getUnit_ct() {
        if (getBook_ref_type() == 1)
            return contract_qt;
        return 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.contract_uuid);
        dest.writeString(this.contract_code);
        dest.writeDouble(this.contract_qt);
    }

    public ContractItemForHistoryList() {
    }

    protected ContractItemForHistoryList(Parcel in) {
        super(in);
        this.contract_uuid = in.readString();
        this.contract_code = in.readString();
        this.contract_qt = in.readDouble();
    }

    public static final Creator<ContractItemForHistoryList> CREATOR = new Creator<ContractItemForHistoryList>() {
        @Override
        public ContractItemForHistoryList createFromParcel(Parcel source) {
            return new ContractItemForHistoryList(source);
        }

        @Override
        public ContractItemForHistoryList[] newArray(int size) {
            return new ContractItemForHistoryList[size];
        }
    };
}
