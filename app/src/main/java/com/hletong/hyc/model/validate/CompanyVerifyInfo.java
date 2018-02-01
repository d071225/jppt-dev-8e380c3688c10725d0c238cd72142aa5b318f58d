package com.hletong.hyc.model.validate;

import com.hletong.hyc.model.validate.ship.ShipVerifyInfo;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.util.ParamsHelper;

import java.util.List;

/**
 * Created by dongdaqing on 2017/6/17.
 */

public class CompanyVerifyInfo implements Validate<CommonResult>{
    private List<Paperwork> paperStr;

    public CompanyVerifyInfo(List<Paperwork> paperStr) {
        this.paperStr = paperStr;
    }

    @Override
    public ItemRequestValue<CommonResult> validate(IBaseView baseView) {
        boolean valid = false;
        for (Paperwork p : paperStr) {
            valid = valid || p.paramsValid();
        }

        if (!valid){
            baseView.handleError(ErrorFactory.getParamError("请选择一种证件上传"));
            return null;
        }

        return new ItemRequestValue<CommonResult>(
                baseView.hashCode(),
                Constant.getUrl(Constant.COMPLETE_COMPANY_INFO),
                new ParamsHelper().put("paperStr", ShipVerifyInfo.formatAsString(paperStr))
        ){};
    }
}
