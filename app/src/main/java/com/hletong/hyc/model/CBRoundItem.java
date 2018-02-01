package com.hletong.hyc.model;

import com.hletong.mob.util.NumberUtil;

/**
 * Created by ddq on 2016/11/3.
 * 竞价投标大厅里面的数据项
 */

public class CBRoundItem {
    private double bidWeight;
    private double bidUnitCt;
    private String status;
    private double bidUnitAmt;
    private boolean empty;

    public CBRoundItem() {

    }

    public CBRoundItem(boolean mEmpty) {
        empty = mEmpty;
    }

    public String getBidWeight() {
        if (empty)
            return "-";
        return NumberUtil.format3f(bidWeight);
    }

    public String getBidUnitCt() {
        if (empty)
            return "-";
        return NumberUtil.format3f(bidUnitCt);
    }

    public String getStatus() {
        return status;
    }

    public String getBidUnitAmt() {
        if (empty)
            return "-";
        return NumberUtil.format2f(bidUnitAmt);
    }

    public String getCargoDesc(int book_ref_type){
        if (book_ref_type == 0){
            return getBidWeight();
        }else if (book_ref_type == 1)
            return getBidUnitCt();
        else
            return "";
    }

    public boolean isEmpty() {
        return empty;
    }
}
