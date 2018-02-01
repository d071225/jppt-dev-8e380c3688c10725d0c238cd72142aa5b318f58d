package com.hletong.hyc.model;

import java.io.Serializable;

/**
 * Created by chengxin on 2017/6/29.
 */

public class InvoiceCert implements Serializable {
    private static final long serialVersionUID = -7294367880414883734L;
    private String sprName;
    private String nsrsbh;
    //开户行
    private String openBankName;
    private String cardNumber;
    private String tel;
    private String province;
    private String city;
    private String county;
    private String address;
    private RegisterPhoto invoicePhoto = new RegisterPhoto(new String[]{"请上传开票照片"}, 0.62f);
    private boolean isDefault;
    private int index;
    private String invoiceUuid;

    public InvoiceCert() {
    }

    public String getOpenBankName() {
        return openBankName;
    }

    public void setOpenBankName(String openBankName) {
        this.openBankName = openBankName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setInvoicePhoto(RegisterPhoto invoicePhoto) {
        this.invoicePhoto = invoicePhoto;
    }

    public RegisterPhoto getInvoicePhoto() {
        return invoicePhoto;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getSprName() {
        return sprName;
    }

    public void setSprName(String sprName) {
        this.sprName = sprName;
    }

    public String getNsrsbh() {
        return nsrsbh;
    }

    public void setNsrsbh(String nsrsbh) {
        this.nsrsbh = nsrsbh;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getInvoiceUuid() {
        return invoiceUuid;
    }

    public void setInvoiceUuid(String invoiceUuid) {
        this.invoiceUuid = invoiceUuid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
