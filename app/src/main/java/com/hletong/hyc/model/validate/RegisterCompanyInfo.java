package com.hletong.hyc.model.validate;

import com.google.gson.reflect.TypeToken;
import com.hletong.hyc.model.PaperTable;
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
public class RegisterCompanyInfo extends RegisterInfo {
    private static final long serialVersionUID = -7690459223539194106L;
    private String company_name;
    private String biz_contact;
    //货主版公司注册需要：证件号码
    private String organiz_code;
    private String id_type;

    private List<PaperTable> paper_table;


    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getBiz_contact() {
        return biz_contact;
    }

    public void setBiz_contact(String biz_contact) {
        this.biz_contact = biz_contact;
    }

    public ItemRequestValue<CommonResult> validateBaseInfo(IBaseView baseView, String url) {
        if (StringUtil.isTrimBlank(company_name)) {
            baseView.handleError(ErrorFactory.getParamError("公司名不能为空"));
        } else if (RegexUtil.containEmpty(company_name)) {
            baseView.handleError(ErrorFactory.getParamError("公司名不能包含空格"));
        } else if (StringUtil.isTrimBlank(getMember_name())) {
            baseView.handleError(ErrorFactory.getParamError("用户名不能为空"));
        } else if (RegexUtil.containEmpty(getMember_name())) {
            baseView.handleError(ErrorFactory.getParamError("用户名不能包含空格"));
        } else if (StringUtil.isTrimBlank(biz_contact)) {
            baseView.handleError(ErrorFactory.getParamError("业务联系人不能为空"));
        }  else if (!StringUtil.isChinese(biz_contact)) {
            baseView.handleError(ErrorFactory.getParamError("业务联系人必须为中文"));
        } else if (StringUtil.isTrimBlank(getProvince())) {
            baseView.handleError(ErrorFactory.getParamError("地址不能为空"));
        } else {
            return getRequestValue(baseView.hashCode(), url/*Constant.getUrl(Constant.CHECK_MEMBERNAME)*/);
        }
        return null;
    }

    @Override
    protected TypeToken<? extends RegisterInfo> getTypeToken() {
        return new TypeToken<RegisterCompanyInfo>(){};
    }

    public List<PaperTable> getPaper_table() {
        return paper_table;
    }

    public void setPaper_table(List<PaperTable> paper_table) {
        this.paper_table = paper_table;
    }

    public String getOrganiz_code() {
        return organiz_code;
    }

    public void setOrganiz_code(String organiz_code) {
        this.organiz_code = organiz_code;
    }

    public String getId_type() {
        return id_type;
    }

    public void setId_type(String id_type) {
        this.id_type = id_type;
    }
}
