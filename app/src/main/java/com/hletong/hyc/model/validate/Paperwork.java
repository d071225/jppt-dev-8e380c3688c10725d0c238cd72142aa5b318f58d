package com.hletong.hyc.model.validate;

import android.text.TextUtils;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.model.RegisterPhoto;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.util.ParamsHelper;

/**
 * Created by dongdaqing on 2017/6/17.
 */

public class Paperwork implements Validate<CommonResult> {
    private String attachType;//所属对象类型(会员类型)
    private String attachUuid;//所属对象编号(会员编号)
    private String paperType;//证件类型
    private RegisterPhoto paperPhoto;//证件图片
    private String beginDt;//生效日期
    private String endDt;//到期日期

    public Paperwork(String paperType, RegisterPhoto paperPhoto, String endDt) {
        this.paperType = paperType;
        this.paperPhoto = paperPhoto;
        this.endDt = endDt;
        this.attachType = String.valueOf(BuildConfig.app_type);
    }

    public ItemRequestValue<CommonResult> validate(IBaseView baseView) {
        if (!paramsValid()) {
            baseView.handleError(ErrorFactory.getParamError("请上传" + getNameByType()));
            return null;
        }

        return new ItemRequestValue<CommonResult>(
                baseView.hashCode(),
                Constant.getUrl(Constant.COMPLETE_PERSONAL_INFO),
                getAsParams()) {
        };
    }

    private ParamsHelper getAsParams() {
        return new ParamsHelper()
                .put("attachType", attachType)
                .put("attachUuid", attachUuid)
                .put("paperType", paperType)
                .put("paperFile", paperPhoto.getFileGroupId())
                .put("beginDt", beginDt)
                .put("endDt", endDt);
    }

    public String getAsString() {
        return getAsParams().getAsString();
    }

    public boolean paramsValid() {
        return paperPhoto != null && !TextUtils.isEmpty(paperPhoto.getFileGroupId());
    }

    private String getNameByType() {
        switch (paperType) {
            case "1":
                return "入会协议";
            case "2":
                return "营业执照";
            case "3":
                return "组织机构代码证";
            case "4":
                return "税务登记证";
            case "6":
                return "营业执照（三证合一）";
            case "8":
                return "身份证";
            case "9":
                return "驾驶证";
            case "10":
                return "船员证";
            case "11":
                return "费用结算指定银行账户证明";
            case "13":
                return "车辆所有人身份证";
            case "16":
                return "车辆负责人身份证";
            case "14":
                return "车辆行驶证";
            case "5":
                return "道路运输经营许可证";
            case "15":
                return "车辆营运证";
            case "17":
                return "车辆登记证";
            case "18":
                return "车辆保险单证";
            case "19":
                return "车辆会员入会协议书";
            case "12":
                return "车辆运输合同";
            case "21":
                return "船舶照片";
            case "23":
                return "船舶所有人身份证";
            case "31":
                return "船舶负责人身份证";
            case "7":
                return "水路运输经营许可证";
            case "29":
                return "船舶所有权证";
            case "25":
                return "船舶国籍证书";
            case "28":
                return "船舶检验证书";
            case "26":
                return "船舶营运证";
            case "27":
                return "船舶适航证书";
            case "30":
                return "船舶保险单证";
            case "22":
                return "船舶会员入会协议书";
            case "24":
                return "船舶运输合同";
            case "20":
                return "挂靠协议";
            case "32":
                return "垫付保证金协议";
            case "33":
                return "道路运输从业资格证";
        }
        return "";
    }
}
