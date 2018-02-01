package com.hletong.hyc.model.validate;

import android.text.TextUtils;

import com.hletong.hyc.model.DictionaryItem;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.util.SimpleDate;
import com.hletong.mob.util.StringUtil;

/**
 * Created by ddq on 2017/2/3.
 */

public class EtcTransporterInfoPart2 {
    private String name;
    private String spell;//拼音
    private DictionaryItem id_type;//证件类型 1=>身份证
    private String id_no;//证件号码
    private DictionaryItem id_validity;//有效期 0=>非长期有效，1=>长期有效
    private SimpleDate date;//到期时间
    private DictionaryItem sex;//0-男，1-女
    private SimpleDate birthday;
    private String phone;
    //推荐信
    private String recommendation;

    public EtcTransporterInfoPart2(String name, String spell, DictionaryItem id_type, String id_no, DictionaryItem id_validity, SimpleDate date, DictionaryItem sex, SimpleDate birthday, String phone, String recommendation) {
        this.name = name;
        this.spell = spell;
        this.id_type = id_type;
        this.id_no = id_no;
        this.id_validity = id_validity;
        this.date = date;
        this.sex = sex;
        this.birthday = birthday;
        this.phone = phone;
        this.recommendation = recommendation;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public String getName() {
        return name;
    }

    public String getSpell() {
        return spell;
    }

    public DictionaryItem getId_type() {
        return id_type;
    }

    public String getId_no() {
        return id_no;
    }

    public DictionaryItem getId_validity() {
        return id_validity;
    }

    public SimpleDate getDate() {
        return date;
    }

    public DictionaryItem getSex() {
        return sex;
    }

    public SimpleDate getBirthday() {
        return birthday;
    }

    public String getPhone() {
        return phone;
    }

    public boolean validate(IBaseView baseView) {
        if (TextUtils.isEmpty(name)) {
            baseView.handleError(ErrorFactory.getParamError("请填写姓名"));
            return false;
        }

        if (TextUtils.isEmpty(spell)) {
            baseView.handleError(ErrorFactory.getParamError("请填写姓名拼音"));
            return false;
        }

        if (id_type == null) {
            baseView.handleError(ErrorFactory.getParamError("请填选择证件类型"));
            return false;
        }

        if (TextUtils.isEmpty(id_no)) {
            baseView.handleError(ErrorFactory.getParamError("请填写证件号码"));
            return false;
        }

        if ("1".equals(id_type.getId()) && !StringUtil.isIdCardNumber(id_no)) {
            baseView.handleError(ErrorFactory.getParamError("请填写正确的身份证号码"));
            return false;
        }

        if (id_validity == null) {
            baseView.handleError(ErrorFactory.getParamError("请填写证件有效期"));
            return false;
        }

        if ("0".equals(id_validity.getId()) && date == null) {
            baseView.handleError(ErrorFactory.getParamError("请选择证件到期时间"));
            return false;
        }

        if (sex == null) {
            baseView.handleError(ErrorFactory.getParamError("请选择性别"));
            return false;
        }

        if (birthday == null) {
            baseView.handleError(ErrorFactory.getParamError("请选择出生日期"));
            return false;
        }

        if (TextUtils.isEmpty(phone)) {
            baseView.handleError(ErrorFactory.getParamError("请填写手机号码"));
            return false;
        }

        return true;
    }
}
