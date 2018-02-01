package com.hletong.hyc.model.validate.truck;

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
public class RegisterPersonalTruckInfo extends RegisterInfo {
    public static class TruckInfo implements Serializable {
        private static final long serialVersionUID = 2757247067783524991L;
        /**
         * 车辆类型
         */
        private DictionaryItem truck_type;
        /**
         * 拖挂轮轴
         */
        private DictionaryItem trailerAxle;
        /**
         * 车辆轴数
         */
        private DictionaryItem axleCount;
        /**
         * 最大载重
         */
        private String max_heavy;
        /**
         * 长度
         */
        private String max_length;
        /**
         * 宽度
         */
        private String max_width;
        /**
         * 高度
         */
        private String max_height;

        public String getMax_height() {
            return max_height;
        }

        public void setMax_height(String max_height) {
            this.max_height = max_height;
        }

        public String getMax_width() {
            return max_width;
        }

        public void setMax_width(String max_width) {
            this.max_width = max_width;
        }

        public String getMax_length() {
            return max_length;
        }

        public void setMax_length(String max_length) {
            this.max_length = max_length;
        }

        public String getMax_heavy() {
            return max_heavy;
        }

        public void setMax_heavy(String max_heavy) {
            this.max_heavy = max_heavy;
        }

        public DictionaryItem getAxleCount() {
            return axleCount;
        }

        public void setAxleCount(DictionaryItem axleCount) {
            this.axleCount = axleCount;
        }

        public DictionaryItem getTrailerAxle() {
            return trailerAxle;
        }

        public void setTrailerAxle(DictionaryItem trailerAxle) {
            this.trailerAxle = trailerAxle;
        }

        public DictionaryItem getTruck_type() {
            return truck_type;
        }

        public void setTruck_type(DictionaryItem truck_type) {
            this.truck_type = truck_type;
        }


    }

    private static final long serialVersionUID = -3001754426925190469L;

    private String plate;
    /**
     * 车辆类型
     */
    private String truck_type;

    /**
     * 轮轴信息=拖挂轮轴-车辆轴数
     */
    private String wheel;
    /**
     * 车辆所有人
     */
    private String personal_name;
    /**
     * 身份证
     */
    private String personal_identity;
    /**
     * 最大载重
     */
    private String max_heavy;
    /**
     * 长度
     */
    private String max_length;
    /**
     * 宽度
     */
    private String max_width;
    /**
     * 高度
     */
    private String max_height;
    private List<PaperTable> paper_table;
    /**
     * 车辆的UUID
     **/
    private String truck_uuid = getUUID();

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getTruck_type() {
        return truck_type;
    }

    public void setTruck_type(String truck_type) {
        this.truck_type = truck_type;
    }

    public String getWheel() {
        return wheel;
    }

    public void setWheel(String wheel) {
        this.wheel = wheel;
    }

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

    public String getMax_heavy() {
        return max_heavy;
    }

    public void setMax_heavy(String max_heavy) {
        this.max_heavy = max_heavy;
    }

    public String getMax_length() {
        return max_length;
    }

    public void setMax_length(String max_length) {
        this.max_length = max_length;
    }

    public String getMax_width() {
        return max_width;
    }

    public void setMax_width(String max_width) {
        this.max_width = max_width;
    }

    public String getMax_height() {
        return max_height;
    }

    public void setMax_height(String max_height) {
        this.max_height = max_height;
    }

    public List<PaperTable> getPaper_table() {
        return paper_table;
    }

    public void setPaper_table(List<PaperTable> paper_table) {
        this.paper_table = paper_table;
    }

    @Override
    public ItemRequestValue<CommonResult> validateBaseInfo(IBaseView baseView, String url) {
        if (StringUtil.isTrimBlank(plate)) {
            baseView.handleError(ErrorFactory.getParamError("车牌号不能为空"));
        } else if (RegexUtil.containEmpty(plate)) {
            baseView.handleError(ErrorFactory.getParamError("车牌号不能包含空格"));
        } else if (!RegexUtil.isCarNumber(plate)) {
            baseView.handleError(ErrorFactory.getParamError("车牌号格式不正确"));
        } else if (StringUtil.isTrimBlank(personal_name)) {
            baseView.handleError(ErrorFactory.getParamError("车辆所有人不能为空"));
        } else if (!StringUtil.isChinese(personal_name)) {
            baseView.handleError(ErrorFactory.getParamError("车辆所有人必须为中文"));
        } else if (StringUtil.isTrimBlank(getProvince())) {
            baseView.handleError(ErrorFactory.getParamError("地址不能为空"));
        } else if (StringUtil.isTrimBlank(getMember_name())) {
            baseView.handleError(ErrorFactory.getParamError("会员名不能为空"));
        } else if (RegexUtil.containEmpty(getMember_name())) {
            baseView.handleError(ErrorFactory.getParamError("会员名不能包含空格"));
        } else if (plate != null && plate.equalsIgnoreCase(getMember_name())) {
            baseView.handleError(ErrorFactory.getParamError("会员名不能与车号一致"));
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
        return new TypeToken<RegisterPersonalTruckInfo>() {
        };
    }

    @Override
    public String getLoginName() {
        return plate;
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
