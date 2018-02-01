package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.hletong.mob.util.NumberUtil;
import com.hletong.mob.util.SimpleDate;

/**
 * Created by ddq on 2016/10/28.
 * 运力预报
 */

public class ForecastTransporterInfo implements Parcelable {

    private String origin_province;
    private String origin_country;
    private String origin_city;

    private String destination_province;
    private String destination_city;
    private String destination_country;

    private String create_ts;
    private String contact_tel;
    private String contact;

    private String owner_member_mgt_unit_code;
    private String owner_member_code;
    private String owner_member_sub_code;

    private String create_opid;
    private String carrier_no;
    private String vehicle_name;

    private String top_width;
    private String bottom_width;
    private String draught_depth;
    private String authz_carrying_capacity;
    private double avl_carrying_capacity;

    private String carrier_type;
    private String carrier_sub_type;

    private String forecast_uuid;
    private String forecast_code;
    private String avl_carrier_dt;
    private String leave_dt;

    private String longitude;
    private String latitude;

    private String width;
    private String height;
    private String length;

    private String status;
    private String version;

    public String getCargoWeight() {
        return NumberUtil.format3f(avl_carrying_capacity) + "吨";
    }

    public String getTimeDes(String separator) {
        StringBuilder builder = new StringBuilder();
        SimpleDate start = SimpleDate.parse(avl_carrier_dt);
        builder.append(start.getMonthOfYear());
        builder.append("月");
        builder.append(start.getDayOfMonth());
        builder.append("日");
        builder.append(separator);

        SimpleDate end = SimpleDate.parse(leave_dt);
        builder.append(end.getMonthOfYear());
        builder.append("月");
        builder.append(end.getDayOfMonth());
        builder.append("日");
        return builder.toString();
    }

    public String getAvl_carrier_dt() {
        return avl_carrier_dt;
    }

    public String getLeave_dt() {
        return leave_dt;
    }

    public String getCarrier_no() {
        return carrier_no;
    }

    public String getOrigin_province() {
        return origin_province;
    }

    public String getOrigin_city() {
        return origin_city;
    }

    public String getDestination_province() {
        return destination_province;
    }

    public String getDestination_city() {
        return destination_city;
    }

    public double getAvl_carrying_capacity() {
        return avl_carrying_capacity;
    }

    public String getCapacityWithUnit() {
        return NumberUtil.format3f(avl_carrying_capacity) + " 吨";
    }

    public String getOrigin_country() {
        return origin_country;
    }

    public String getDestination_country() {
        return destination_country;
    }

    public String getContact_tel() {
        return contact_tel;
    }

    public String getContact() {
        return contact;
    }

    public String getForecast_uuid() {
        return forecast_uuid;
    }

    public Address getStartAddress() {
        return new Address(origin_province, origin_city, origin_country);
    }

    public Address getStopAddress() {
        return new Address(destination_province, destination_city, destination_country);
    }

    public Address getOriginAddressForDisplay() {
        return new Address(getOriginProvinceForDisplay(), getOriginCityForDisplay(), getOriginCountyForDisplay());
    }

    public Address getDestAddressForDisplay() {
        return new Address(getDestinationProvinceForDisplay(), getDestinationCityForDisplay(), getDestinationCountyForDisplay());
    }

    public String getOriginProvinceForDisplay() {
        if (origin_province == null)
            return "全国";
        return origin_province;
    }

    public String getOriginCityForDisplay() {
        if (origin_city == null) {
            return getOriginProvinceForDisplay();
        }
        return origin_city;
    }

    public String getOriginCountyForDisplay() {
        if (origin_country == null) {
            if (origin_city != null)
                return "全市";
            else if (origin_province != null)
                return "全省";
            return "全部";
        }
        return origin_country;
    }

    public String getDestinationProvinceForDisplay() {
        if (destination_province == null)
            return "全国";
        return destination_province;
    }

    public String getDestinationCityForDisplay() {
        if (destination_city == null) {
            return getDestinationProvinceForDisplay();
        }
        return destination_city;
    }

    public String getDestinationCountyForDisplay() {
        if (destination_country == null) {
            if (destination_city != null)
                return "全市";
            else if (destination_province != null)
                return "全省";
            return "全部";
        }
        return destination_country;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.origin_province);
        dest.writeString(this.origin_country);
        dest.writeString(this.origin_city);
        dest.writeString(this.destination_province);
        dest.writeString(this.destination_city);
        dest.writeString(this.destination_country);
        dest.writeString(this.create_ts);
        dest.writeString(this.contact_tel);
        dest.writeString(this.contact);
        dest.writeString(this.owner_member_mgt_unit_code);
        dest.writeString(this.owner_member_code);
        dest.writeString(this.owner_member_sub_code);
        dest.writeString(this.create_opid);
        dest.writeString(this.carrier_no);
        dest.writeString(this.vehicle_name);
        dest.writeString(this.top_width);
        dest.writeString(this.bottom_width);
        dest.writeString(this.draught_depth);
        dest.writeString(this.authz_carrying_capacity);
        dest.writeDouble(this.avl_carrying_capacity);
        dest.writeString(this.carrier_type);
        dest.writeString(this.carrier_sub_type);
        dest.writeString(this.forecast_uuid);
        dest.writeString(this.forecast_code);
        dest.writeString(this.avl_carrier_dt);
        dest.writeString(this.leave_dt);
        dest.writeString(this.longitude);
        dest.writeString(this.latitude);
        dest.writeString(this.width);
        dest.writeString(this.height);
        dest.writeString(this.length);
        dest.writeString(this.status);
        dest.writeString(this.version);
    }

    public ForecastTransporterInfo() {
    }

    protected ForecastTransporterInfo(Parcel in) {
        this.origin_province = in.readString();
        this.origin_country = in.readString();
        this.origin_city = in.readString();
        this.destination_province = in.readString();
        this.destination_city = in.readString();
        this.destination_country = in.readString();
        this.create_ts = in.readString();
        this.contact_tel = in.readString();
        this.contact = in.readString();
        this.owner_member_mgt_unit_code = in.readString();
        this.owner_member_code = in.readString();
        this.owner_member_sub_code = in.readString();
        this.create_opid = in.readString();
        this.carrier_no = in.readString();
        this.vehicle_name = in.readString();
        this.top_width = in.readString();
        this.bottom_width = in.readString();
        this.draught_depth = in.readString();
        this.authz_carrying_capacity = in.readString();
        this.avl_carrying_capacity = in.readDouble();
        this.carrier_type = in.readString();
        this.carrier_sub_type = in.readString();
        this.forecast_uuid = in.readString();
        this.forecast_code = in.readString();
        this.avl_carrier_dt = in.readString();
        this.leave_dt = in.readString();
        this.longitude = in.readString();
        this.latitude = in.readString();
        this.width = in.readString();
        this.height = in.readString();
        this.length = in.readString();
        this.status = in.readString();
        this.version = in.readString();
    }

    public static final Creator<ForecastTransporterInfo> CREATOR = new Creator<ForecastTransporterInfo>() {
        @Override
        public ForecastTransporterInfo createFromParcel(Parcel source) {
            return new ForecastTransporterInfo(source);
        }

        @Override
        public ForecastTransporterInfo[] newArray(int size) {
            return new ForecastTransporterInfo[size];
        }
    };
}
