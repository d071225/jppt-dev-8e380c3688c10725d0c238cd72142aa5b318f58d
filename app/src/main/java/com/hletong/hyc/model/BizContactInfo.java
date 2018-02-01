package com.hletong.hyc.model;

import java.util.Locale;

/**
 * Created by ddq on 2017/1/10.
 */

public class BizContactInfo {
    private String unitUuid;// 会员管理单位流水号
    private String unitCode;// 会员管理单位编号
    private String unitName;// 会员管理单位名称
    private String bizContact;// 业务联系人
    private String bizContactTel;// 业务联系电话

    @Override
    public String toString() {
        return String.format(Locale.CHINESE,
                "尊敬的会员，您尚未开通E商贸通，如需参与自主交易业务，请联系会员管理单位：%s，联系人：%s(%s)，办理惠龙卡并开通E商贸通。", unitName,bizContact,bizContactTel);
    }
}
