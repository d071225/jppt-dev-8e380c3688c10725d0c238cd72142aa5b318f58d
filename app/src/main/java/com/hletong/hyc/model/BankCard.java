package com.hletong.hyc.model;

import java.io.Serializable;

/**
 * Created by chengxin on 2017/6/29.
 */

public class BankCard implements Serializable {
    private static final long serialVersionUID = -7294367880414883734L;
    private DictionaryItem bankEnum;
    private RegisterPhoto cardPhoto = new RegisterPhoto(new String[]{"请上传银行卡正面照片"}, 0.62f);
    private DictionaryItem paper;

    private String paperNumber;

    private String bankUuid;

    private int index;

    //银行卡号
    private String bankCode;
    //户名
    private String name;
    //是否默认
    private boolean isDefault;
    //开户支行/分理处
    private String bankAddress;
    //所属标识
    private String refCode;
    //指定账号签约席位号
    private String assignCode;
    //fileGroupId
    private String bank_file;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getBankAddress() {
        return bankAddress;
    }

    public void setBankAddress(String bankAddress) {
        this.bankAddress = bankAddress;
    }

    public String getRefCode() {
        return refCode;
    }

    public void setRefCode(String refCode) {
        this.refCode = refCode;
    }

    public String getAssignCode() {
        return assignCode;
    }

    public void setAssignCode(String assignCode) {
        this.assignCode = assignCode;
    }

    public String getBank_file() {
        return bank_file;
    }

    public void setBank_file(String bank_file) {
        this.bank_file = bank_file;
    }

    public DictionaryItem getBankEnum() {
        return bankEnum;
    }

    public void setBankEnum(DictionaryItem bankEnum) {
        this.bankEnum = bankEnum;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public void setCardPhoto(RegisterPhoto cardPhoto) {
        this.cardPhoto = cardPhoto;
    }

    public RegisterPhoto getCardPhoto() {
        return cardPhoto;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public DictionaryItem getPaper() {
        return paper;
    }

    public void setPaper(DictionaryItem paper) {
        this.paper = paper;
    }

    public String getPaperNumber() {
        return paperNumber;
    }

    public void setPaperNumber(String paperNumber) {
        this.paperNumber = paperNumber;
    }

    public String getBankUuid() {
        return bankUuid;
    }

    public void setBankUuid(String bankUuid) {
        this.bankUuid = bankUuid;
    }
}
