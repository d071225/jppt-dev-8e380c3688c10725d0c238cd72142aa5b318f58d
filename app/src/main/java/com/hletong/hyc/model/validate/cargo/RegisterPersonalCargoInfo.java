package com.hletong.hyc.model.validate.cargo;

import com.google.gson.reflect.TypeToken;
import com.hletong.hyc.model.PaperTable;
import com.hletong.hyc.model.validate.RegisterInfo;
import com.hletong.hyc.util.RegexUtil;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.util.StringUtil;

import java.util.List;

/**
 * Created by cx on 2016/10/26.
 */
public class RegisterPersonalCargoInfo extends RegisterInfo {


    private static final long serialVersionUID = -1078851854747698803L;
    /**
     * 姓名
     */
    private String personal_name;
    /**
     * 身份证
     */
    private String personal_identity;

    private List<PaperTable> paper_table;

    public List<PaperTable> getPaper_table() {
        return paper_table;
    }

    public void setPaper_table(List<PaperTable> paper_table) {
        this.paper_table = paper_table;
    }

    public String getPersonal_identity() {
        return personal_identity;
    }

    public void setPersonal_identity(String personal_identity) {
        this.personal_identity = personal_identity;
    }

    public String getPersonal_name() {
        return personal_name;
    }

    public void setPersonal_name(String personal_name) {
        this.personal_name = personal_name;
    }

    @Override
    public ItemRequestValue<CommonResult> validateBaseInfo(IBaseView baseView, String url) {
         if (StringUtil.isTrimBlank(personal_name)) {
            baseView.handleError(ErrorFactory.getParamError("姓名不能为空"));
        } else if (!StringUtil.isChinese(personal_name)) {
            baseView.handleError(ErrorFactory.getParamError("姓名必须为中文"));
        } else if (StringUtil.isTrimBlank(getProvince())) {
            baseView.handleError(ErrorFactory.getParamError("地址不能为空"));
        } else if (StringUtil.isTrimBlank(getMember_name())) {
            baseView.handleError(ErrorFactory.getParamError("会员名不能为空"));
        } else if (RegexUtil.containEmpty(getMember_name())) {
            baseView.handleError(ErrorFactory.getParamError("会员名不能包含空格"));
        } else if (!StringUtil.isTrimBlank(personal_identity) && !StringUtil.isIdCardNumber(personal_identity)) {
            baseView.handleError(ErrorFactory.getParamError("身份证格式错误"));
        } else {
            return getRequestValue(baseView.hashCode(), url);
        }
        return null;
    }

    @Override
    protected TypeToken<? extends RegisterInfo> getTypeToken() {
        return new TypeToken<RegisterPersonalCargoInfo>() {
        };
    }
}
