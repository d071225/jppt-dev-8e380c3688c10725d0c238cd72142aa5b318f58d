package com.hletong.hyc.model;

import com.hletong.hyc.util.Pages;

/**
 * Created by dongdaqing on 2017/7/13.
 */

public class CompanyTruckAgreement extends Agreement {
    public String mContactPhone;//业务联系人手机
    public String contactYear;//合同签订年
    public String contactMonth;//合同签订月
    public String contactDate;//合同签订日
    public String mChargeZipCode;//公司负责人邮政编码
    public String transContractYear;//运输合同签订年
    public String transContractMonth;//运输合同签订月
    public String transContractDate;//运输合同签订日
    public String vehicleNo;//车牌号（支持多辆），如：vehicleNo=1&vehicleNo=2
    public String vehicleCharge;//车辆负责人（支持多辆）
    public String vehicleChargeIdCode;//负责人身份证（支持多辆）
    public String vehicleCode;//车辆识别代号(支持多辆车)
    public String vehicleType;//车辆类型(支持多辆车)
    public String vehicleMaxLoad;//最大配载量（吨）(支持多辆车)
    public String vehicleLength;//车厢长（米）(支持多辆车)
    public String vehicleWidth;//车厢宽（米）(支持多辆车)
    public String vehicleHeight;//车厢高（米）(支持多辆车)
    public String vehicleRegDate;//车辆首次登记日期(支持多辆车)
    public String vehicleMobile;//车辆对应手机号码(支持多辆车)
    public String vehicleBankName;//车辆会员对应的银行卡用户姓名(支持多辆车)
    public String vehicleBankType;//车辆会员对应的银行类型(支持多辆车)
    public String vehicleBankNo;//车辆会员对应的银行卡号(支持多辆车)
    public String vehicleScBankYear;//每个车账户结算证明签订年(支持多辆车)
    public String vehicleScBankMonth;//每个车账户结算证明签订月(支持多辆车)
    public String vehicleScBankDate;//每个车账户结算证明签订日(支持多辆车)

    @Override
    protected String getUrl() {
        return Pages.COMPANY_TRUCK_REGISTER_CONTRACT;
    }
}
