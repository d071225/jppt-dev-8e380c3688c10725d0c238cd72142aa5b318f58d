package com.hletong.hyc.model;

import com.hletong.hyc.R;
import com.hletong.hyc.util.LinkView;
import com.hletong.hyc.util.Pages;

/**
 * Created by dongdaqing on 2017/7/13.
 */

public class PersonalTruckAgreement extends Agreement {
    @LinkView(R.id.et_carContact)
    public String mName;//车辆所有人姓名
    @LinkView({R.id.cv_carNumber, R.id.et_carNumber})
    public String vehicleNo;//车牌号(支持多辆车)，如两辆，则vehicleNo=1&vehicleNo=2,顺序要与如下车辆一致
    public String vehicleCode;//车辆识别代号(支持多辆车)
    public String vehicleType;//车辆类型(支持多辆车)
    public String vehicleMaxLoad;//最大配载量（吨）(支持多辆车)
    public String vehicleLength;//车厢长（米）(支持多辆车)
    public String vehicleWidth;//车厢宽（米）(支持多辆车)
    public String vehicleHeight;//车厢高（米）(支持多辆车)
    public String vehicleRegDate;//车辆首次登记日期(支持多辆车)
    public String vehicleMobile;//车辆对应手机号码(支持多辆车)
    public String vehicleBankName;//结算对应银行卡用户姓名(支持多辆车)
    public String vehicleBankType;//结算对应银行类型(支持多辆车)
    public String vehicleBankNo;//结算对应银行卡号(支持多辆车)
    public String vehicleScYear;//结算证明签订年(支持多辆车)
    public String vehicleScMonth;//结算证明签订月(支持多辆车)
    public String vehicleScDate;//结算证明签订日(支持多辆车)

    @Override
    protected String getUrl() {
        return Pages.PERSONAL_TRUCK_REGISTER_CONTRACT;
    }
}
