package com.hletong.hyc.model;

/**
 * Created by ddq on 2017/4/5.
 */

public class PersonalCollection {
    private String confirmType;
    private int storedStatus;
    private String uniqueId;
    private String tradeUuid;

    public String getTradeUuid() {
        return tradeUuid;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public String getConfirmType() {
        return confirmType;
    }

    public boolean isCollected(){
        return 1 == storedStatus;
    }

    public void setStoredStatus(int storedStatus) {
        this.storedStatus = storedStatus;
    }
}
