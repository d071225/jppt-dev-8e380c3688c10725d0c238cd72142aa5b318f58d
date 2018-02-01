package com.hletong.hyc.model;

/**
 * Created by ddq on 2017/2/27.
 */

public class CargoContractHistoryDetails extends ContractItemForDetails{
    private String c_weight;
    private String unit_amt_sw;
    private String create_opid;
    private String print_status;
    private String unit_ct_sw;
    private String unit_amt;
    private String margin_amt;
    private String c_unit_ct;
    private String cntrct_create_dt;
    private String margin_rt;
    private String weight_sw;
    private double total_amt;
    private String cntrct_start_dt;

    @Override
    public String getTransportTotalFee() {
        return getTransportFeeForCargoOwner();
    }
}
