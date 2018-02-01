package com.hletong.hyc.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.internal.LinkedTreeMap;
import com.xcheng.okhttp.util.ParamUtil;

import java.util.List;

/**
 * Created by cc on 2016/11/3.
 * 承运合同详情
 */
public class TransportContractDetails implements Parcelable {
    private ContractBasic contractMemInfo;
    private String cargoOwnerName;//
    private UnSignedContract bookOrder;
    private List<Order> orderDtlList;//
    private LinkedTreeMap<String, List<ContractCarrierInfo>> carryList;//历史承运信息

    public LinkedTreeMap<String, List<ContractCarrierInfo>> getCarryList() {
        return carryList;
    }

    public String getTransportType() {
        return bookOrder.getTransporterRequests();
    }

    public ContractBasic getBasic() {
        return contractMemInfo;
    }

    public UnSignedContract getBookOrder() {
        return bookOrder;
    }

    public List<Order> getOrderDtlList() {
        return orderDtlList;
    }

    public String getCarrierName() {
        if (contractMemInfo == null)
            return "";
        return contractMemInfo.getMemberName();
    }

    public String getTipMessage() {
        if (ParamUtil.isEmpty(orderDtlList))
            return null;
        Order order = orderDtlList.get(0);
        return order.showWrtrTips() ? order.getWrtrTipsMsg() : null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.contractMemInfo, flags);
        dest.writeString(this.cargoOwnerName);
        dest.writeParcelable(this.bookOrder, flags);
        dest.writeTypedList(this.orderDtlList);
    }

    public TransportContractDetails() {
    }

    protected TransportContractDetails(Parcel in) {
        this.contractMemInfo = in.readParcelable(ContractBasic.class.getClassLoader());
        this.cargoOwnerName = in.readString();
        this.bookOrder = in.readParcelable(UnSignedContract.class.getClassLoader());
        this.orderDtlList = in.createTypedArrayList(Order.CREATOR);
    }

    public static final Creator<TransportContractDetails> CREATOR = new Creator<TransportContractDetails>() {
        @Override
        public TransportContractDetails createFromParcel(Parcel source) {
            return new TransportContractDetails(source);
        }

        @Override
        public TransportContractDetails[] newArray(int size) {
            return new TransportContractDetails[size];
        }
    };
}
