package com.hletong.hyc.model;

import java.util.ArrayList;

/**
 * Created by ddq on 2017/3/8.
 * 货源的交易信息，包含承运信息和开票信息
 */

public class SourceTrade {
    private ArrayList<SourceCarryInfo> carryList;
    private SourceInvoice invoiceInfo;

    public ArrayList<SourceCarryInfo> getCarryList() {
        return carryList;
    }

    public SourceInvoice getInvoiceInfo() {
        return invoiceInfo;
    }
}
