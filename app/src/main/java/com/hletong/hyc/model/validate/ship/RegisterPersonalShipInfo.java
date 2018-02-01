package com.hletong.hyc.model.validate.ship;

import com.google.gson.reflect.TypeToken;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.PaperTable;
import com.hletong.hyc.model.validate.RegisterInfo;
import com.hletong.hyc.util.OtherInfoHelper;
import com.hletong.hyc.util.RegexUtil;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.util.StringUtil;
import com.hletong.mob.validator.Validator;
import com.hletong.mob.validator.result.ResultBasic;

import java.io.Serializable;
import java.util.List;

/**
 * Created by cx on 2016/10/26.
 */
public class RegisterPersonalShipInfo extends RegisterInfo {
    public static class ShipInfo implements Serializable {
        private static final long serialVersionUID = 2757247067783524991L;

        /**
         * 船舶类型
         */
        private DictionaryItem ship_type;
        /**
         * 长度
         */
        private String ship_length;
        /**
         * 型宽
         */
        private String ship_width;
        /**
         * 型深
         */
        private String deep;
        /**
         * 满载吃水
         **/
        private String full_draft;

        /**
         * 净吨位  和完善
         **/
        private String nt_ton;

        /**
         * 载重吨位
         */
        private String load_ton;

        public String getLoad_ton() {
            return load_ton;
        }

        public void setLoad_ton(String load_ton) {
            this.load_ton = load_ton;
        }

        public String getFull_draft() {
            return full_draft;
        }

        public void setFull_draft(String full_draft) {
            this.full_draft = full_draft;
        }

        public String getDeep() {
            return deep;
        }

        public void setDeep(String deep) {
            this.deep = deep;
        }

        public String getShip_width() {
            return ship_width;
        }

        public void setShip_width(String ship_width) {
            this.ship_width = ship_width;
        }

        public String getShip_length() {
            return ship_length;
        }

        public void setShip_length(String ship_length) {
            this.ship_length = ship_length;
        }

        public DictionaryItem getShip_type() {
            return ship_type;
        }

        public void setShip_type(DictionaryItem ship_type) {
            this.ship_type = ship_type;
        }

        public String getNt_ton() {
            return nt_ton;
        }

        public void setNt_ton(String nt_ton) {
            this.nt_ton = nt_ton;
        }
    }

    private static final long serialVersionUID = -3001754426925190469L;

    private String ship;

    public String getShip_type() {
        return ship_type;
    }

    public void setShip_type(String ship_type) {
        this.ship_type = ship_type;
    }

    public String getFull_draft() {
        return full_draft;
    }

    public void setFull_draft(String full_draft) {
        this.full_draft = full_draft;
    }

    public String getLoad_ton() {
        return load_ton;
    }

    public void setLoad_ton(String load_ton) {
        this.load_ton = load_ton;
    }

    public String getDeep() {
        return deep;
    }

    public void setDeep(String deep) {
        this.deep = deep;
    }

    public String getShip_width() {
        return ship_width;
    }

    public void setShip_width(String ship_width) {
        this.ship_width = ship_width;
    }

    public String getShip_length() {
        return ship_length;
    }

    public void setShip_length(String ship_length) {
        this.ship_length = ship_length;
    }

    public String getNationality_cert() {
        return nationality_cert;
    }

    public void setNationality_cert(String nationality_cert) {
        this.nationality_cert = nationality_cert;
    }

    public String getShip() {
        return ship;
    }

    public void setShip(String ship) {
        this.ship = ship;
    }

    /**
     * 车辆类型
     */
    private String ship_type;
    /**
     * 船舶所有人
     */
    private String personal_name;
    /**
     * 身份证
     */
    private String personal_identity;
    /**
     * 国籍证书号
     **/
    private String nationality_cert;

    /**
     * 长度
     */
    private String ship_length;
    /**
     * 型宽
     */
    private String ship_width;
    /**
     * 型深
     */
    private String deep;
    /**
     * 满载吃水
     **/
    private String full_draft;
    /**
     * 载重吨位
     */
    private String load_ton;
    private List<PaperTable> paper_table;
    /**
     * 船舶的UUID
     **/
    private String ship_uuid = getUUID();


    public String getPersonal_name() {
        return personal_name;
    }

    public void setPersonal_name(String personal_name) {
        this.personal_name = personal_name;
    }

    public String getPersonal_identity() {
        return personal_identity;
    }

    public void setPersonal_identity(String personal_identity) {
        this.personal_identity = personal_identity;
    }


    public List<PaperTable> getPaper_table() {
        return paper_table;
    }

    public void setPaper_table(List<PaperTable> paper_table) {
        this.paper_table = paper_table;
    }

    @Override
    public ItemRequestValue<CommonResult> validateBaseInfo(IBaseView baseView, String url) {
        if (StringUtil.isTrimBlank(ship)) {
            baseView.handleError(ErrorFactory.getParamError("船舶号不能为空"));
        } else if (RegexUtil.containEmpty(ship)) {
            baseView.handleError(ErrorFactory.getParamError("船舶号不能包含空格"));
        } else if (StringUtil.isTrimBlank(personal_name)) {
            baseView.handleError(ErrorFactory.getParamError("船舶所有人不能为空"));
        } else if (!StringUtil.isChinese(personal_name)) {
            baseView.handleError(ErrorFactory.getParamError("船舶有人必须为中文"));
        } else if (StringUtil.isTrimBlank(getProvince())) {
            baseView.handleError(ErrorFactory.getParamError("地址不能为空"));
        } else if (StringUtil.isTrimBlank(getMember_name())) {
            baseView.handleError(ErrorFactory.getParamError("会员名不能为空"));
        } else if (RegexUtil.containEmpty(getMember_name())) {
            baseView.handleError(ErrorFactory.getParamError("会员名不能包含空格"));
        } else if (ship != null && ship.equalsIgnoreCase(getMember_name())) {
            baseView.handleError(ErrorFactory.getParamError("会员名不能与船号一致"));
        } else if (!StringUtil.isTrimBlank(personal_identity) && !StringUtil.isIdCardNumber(personal_identity)) {
            baseView.handleError(ErrorFactory.getParamError("身份证格式错误"));
        } else if (resultBasics == null) {
            baseView.handleError(ErrorFactory.getParamError("请选择其他信息"));
        } else {
            return getRequestValue(baseView.hashCode(), url);
        }
        return null;
    }

    @Override
    protected TypeToken<? extends RegisterInfo> getTypeToken() {
        return new TypeToken<RegisterPersonalShipInfo>() {
        };
    }

    @Override
    public String getLoginName() {
        return ship;
    }

    private String is_soldier;
    private String is_organization;
    //默认为空
    private String soldier_kind = Validator.EMPTY;
    private String soldier_army = Validator.EMPTY;
    private String soldier_level = Validator.EMPTY;
    private transient ResultBasic resultBasics;

    public void initOtherInfo(ResultBasic resultBasics) {
        this.resultBasics = resultBasics;
        if (resultBasics != null) {
            is_organization = resultBasics.get(OtherInfoHelper.key_isOrganization).resultText();
            ResultBasic solderInfo = resultBasics.get(OtherInfoHelper.key_soldierInfo);
            is_soldier = solderInfo.get(OtherInfoHelper.key_isSoldier).resultText();
            if (is_soldier.equals("1")) {
                soldier_army = solderInfo.get(OtherInfoHelper.key_soldierArmy).resultText();
                soldier_kind = solderInfo.get(OtherInfoHelper.key_soldierKind).resultText();
                soldier_level = solderInfo.get(OtherInfoHelper.key_soldierLevel).resultText();
            }
        }
    }
}
