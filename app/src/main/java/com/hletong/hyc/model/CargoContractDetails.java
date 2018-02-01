package com.hletong.hyc.model;

/**
 * Created by ddq on 2017/2/24.
 * 货方合同详情
 */

public class CargoContractDetails{
    private ContractBasic contractMemInfo;
    private Source cargoInfo;
    private boolean canLoans;

    public ContractBasic getContractMemInfo() {
        return contractMemInfo;
    }

    public Source getCargoInfo() {
        return cargoInfo;
    }
}
