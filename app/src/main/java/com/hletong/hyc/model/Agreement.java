package com.hletong.hyc.model;

import android.view.View;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.LinkView;
import com.hletong.mob.util.SimpleDate;
import com.hletong.mob.util.StringUtil;

import java.lang.reflect.Field;
import java.util.Calendar;

/**
 * Created by chengxin on 2017/7/13.
 */

public abstract class Agreement {
    //会员名
    @LinkView({R.id.et_carContact, R.id.et_shipOwner, R.id.cv_shipOwner, R.id.et_companyname, R.id.et_loginname, R.id.cv_memberName})
    public String mUserName;
    //地址
    @LinkView({R.id.tv_address, R.id.cv_address})
    public String mAddress;
    //业务联系人
    @LinkView({R.id.et_companyContract, R.id.cv_busiContact})
    public String mContact;
    //业务联系人手机
    public String mContactMobile;
    @LinkView({R.id.et_companyname, R.id.et_xinming, R.id.cv_companyName})
    public String mCompName;
    //公司传真
    public String mFax;
    //邮箱
    public String mMail;
    //邮政编码
    public String mZipCode;

    //身份证号码
    @LinkView({R.id.identify_num, R.id.cv_identifyNumber})
    public String mIdCode;
    @LinkView({R.id.cv_memberPhoneNumber})
    public String mMobile;
    //电话号码
    public String mPhone;
    //车船负责人
    public String mCompCharge;
    //负责人身份证号
    public String mChargeIdCode;
    //负责人手机号
    public String mChargeMobile;
    //负责人电话
    public String mChargePhone;
    //负责人邮箱
    public String mChargeMail;
    //负责人传真
    public String mChargeFax;

    protected abstract String getUrl();

    public String getUrlWithParams() {
        return getUrl() + "?" + appendParams();
    }

    public void fillData(View root) {
        Field[] fields = getClass().getFields();
        for (Field field : fields) {
            LinkView linkView = field.getAnnotation(LinkView.class);
            if (linkView != null) {
                int[] views = linkView.value();
                for (int i = 0; i < views.length; i++) {
                    View view = root.findViewById(views[i]);
                    if (view != null) {
                        try {
                            if (view instanceof TextView) {
                                TextView tv = (TextView) view;
                                if (!StringUtil.isTrimBlank(tv.getText().toString())) {
                                    field.set(this, tv.getText().toString());
                                }
                                break;
                            } else if (view instanceof CommonInputView) {
                                CommonInputView civ = (CommonInputView) view;
                                if (!StringUtil.isTrimBlank(civ.getInputValue())) {
                                    field.set(this, civ.getInputValue());
                                }
                                break;
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
    }

    private String appendParams() {
        StringBuilder params = new StringBuilder();
        Field[] field = getClass().getFields();//获取实体类的所有属性，返回Field数组
        for (int j = 0; j < field.length; j++) {
            String name = field[j].getName();
            try {
                Object value = field[j].get(this);
                if (value != null) {
                    params.append(name).append("=").append(value.toString());
                    if (j != field.length - 1) {
                        params.append("&");
                    }
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        appendDate(params);
        return params.toString();
    }

    private void appendDate(StringBuilder params) {
        SimpleDate simpleDate = new SimpleDate(Calendar.getInstance());
        int year = simpleDate.getYear();
        int month = simpleDate.getMonthOfYear();
        int day = simpleDate.getDayOfMonth();
        params.append("&contactYear=").append(year).append("&contactMonth=").append(month).append("&contactDate=").append(day)
                .append("&contractYear=").append(year).append("&contractMonth=").append(month).append("&contractDate=").append(day)
                .append("&regContractYear=").append(year).append("&regContractMonth=").append(month).append("&regContractDate=").append(day)
                .append("&vehicleScYear=").append(year).append("&vehicleScMonth=").append(month).append("&vehicleScDate=").append(day)
                .append("&vehicleScBankYear=").append(year).append("&vehicleScBankMonth=").append(month).append("&vehicleScBankDate=").append(day)
                .append("&transContractYear=").append(year).append("&transContractMonth=").append(month).append("&transContractDate=").append(day)
                .append("&scContractYear=").append(year).append("&scContractMonth=").append(month).append("&scContractDate=").append(day);

    }
}
