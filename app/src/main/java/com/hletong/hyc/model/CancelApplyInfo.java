package com.hletong.hyc.model;

/**
 * Created by 阳乾春 on 2017/1/17.
 */
public class CancelApplyInfo {
    /**
     * returnCode : E0000
     * returnMesg : null
     */
    private String returnCode;
    private String returnMesg;

    public String getReturnCode() {
        return returnCode;
    }
    public String getReturnMesg() {
        return returnMesg;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public void setReturnMesg(String returnMesg) {
        this.returnMesg = returnMesg;
    }

    @Override
    public String toString() {
        return "CancelApplyInfo{" +
                "returnCode='" + returnCode + '\'' +
                ", returnMesg='" + returnMesg + '\'' +
                '}';
    }
}
