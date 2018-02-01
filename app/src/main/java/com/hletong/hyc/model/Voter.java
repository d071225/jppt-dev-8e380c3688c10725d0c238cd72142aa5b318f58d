package com.hletong.hyc.model;

import java.io.Serializable;

/**
 * Created by chengxin on 2017/10/23.
 */

public class Voter implements Serializable {
    private static final long serialVersionUID = -1085132413327387836L;
    private String voterName;
    private String carrierNo;
    private String voterUuid;
    private int remainVotes;
    private String votes;
    private int totalVotes;
    private boolean isCandidate;
    private boolean isVoter;

    public String getVoterName() {
        return voterName;
    }

    public String getCarrierNo() {
        return carrierNo;
    }

    public String getVoterUuid() {
        return voterUuid;
    }

    public int getRemainVotes() {
        return remainVotes;
    }

    public String getVotes() {
        return votes;
    }

    public int getTotalVotes() {
        return totalVotes;
    }

    public void upTotalVotes() {
        totalVotes++;
    }

    public boolean isCandidate() {
        return isCandidate;
    }

    public boolean isVoter() {
        return isVoter;
    }

    /**
     * 剩余票数减一
     */
    public void downRemainVotes() {
        remainVotes--;
    }
}
