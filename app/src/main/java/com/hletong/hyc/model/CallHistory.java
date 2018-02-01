package com.hletong.hyc.model;

/**
 * Created by dongdaqing on 2017/5/31.
 */

public class CallHistory {
    private String callMemerbCode; //呼叫方编号
    private String callMemberSname; //呼叫方名称
    private String callTel; //呼叫方电话
    private String calledMemberCode; //被叫方编号
    private String calledMemberSname; //被叫方名称
    private String calledTel; //被叫方电话
    private String callDate; //呼叫时间
    private String bizUuid; //业务流水号

    public String getText(){
        return callMemberSname + " 进行了电话议价";
    }

    public String getCallTel() {
        return callTel;
    }

    public String getCallMemberSname() {
        return callMemberSname;
    }

    public String getCallMemerbCode() {
        return callMemerbCode;
    }
}
