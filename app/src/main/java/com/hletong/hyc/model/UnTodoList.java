package com.hletong.hyc.model;

import com.google.gson.annotations.SerializedName;
import com.xcheng.okhttp.util.ParamUtil;

/**
 * 待办事项通知接口
 * Created by cc on 2016/12/27.
 */
public class UnTodoList {
    /**
     * 挂价摘牌
     **/
    @SerializedName("01")
    private BaseListResult<?> guajizhaipai;
    /**
     * 网签承运合同
     **/
    @SerializedName("02")
    private BaseListResult<?> wangqianchengyunhetong;
    /**
     * 发货单回传
     **/
    @SerializedName("03")
    private BaseListResult<?> fahuodanhuichuang;
    /**
     * 收货单回传
     **/
    @SerializedName("04")
    private BaseListResult<?> shouhuodanhuichuan;
    /**
     * 竞价摘牌
     **/
    @SerializedName("06")
    private BaseListResult<?> jinjiazhaipai;
    /**
     * 事故确认单
     **/
    @SerializedName("10")
    private BaseListResult<?> shigudanqueren;
    /**
     * 违约确认
     **/
    @SerializedName("11")
    private BaseListResult<?> weiyuequeren;
    /**
     * 守约确认
     **/
    @SerializedName("12")
    private BaseListResult<?> shouyuequeren;
    /**
     * 滞压费用单证上传
     **/
    @SerializedName("14")
    private BaseListResult<?> zhiyafeiyongdanshangchuan;
    /**
     * 收货单运单回传
     **/
    @SerializedName("50")
    private BaseListResult<?> shouhuodanyundanhuichuan;
    /**
     * 卸货地变更补签合同
     **/
    @SerializedName("61")
    private BaseListResult<?> xiehuodibiangengbuqianhetong;
    /**
     * 卸货地变更通知
     **/
    @SerializedName("62")
    private BaseListResult<?> xiehuodibiangengtongzhi;

    public BaseListResult<?> getGuajizhaipai() {
        return guajizhaipai;
    }

    public BaseListResult<?> getWangqianchengyunhetong() {
        return wangqianchengyunhetong;
    }

    public BaseListResult<?> getFahuodanhuichuang() {
        return fahuodanhuichuang;
    }

    public BaseListResult<?> getShouhuodanhuichuan() {
        return shouhuodanhuichuan;
    }

    public BaseListResult<?> getJinjiazhaipai() {
        return jinjiazhaipai;
    }

    public BaseListResult<?> getShigudanqueren() {
        return shigudanqueren;
    }

    public BaseListResult<?> getWeiyuequeren() {
        return weiyuequeren;
    }

    public BaseListResult<?> getShouyuequeren() {
        return shouyuequeren;
    }

    public BaseListResult<?> getZhiyafeiyongdanshangchuan() {
        return zhiyafeiyongdanshangchuan;
    }

    public BaseListResult<?> getShouhuodanyundanhuichuan() {
        return shouhuodanyundanhuichuan;
    }

    public BaseListResult<?> getXiehuodibiangengbuqianhetong() {
        return xiehuodibiangengbuqianhetong;
    }

    public BaseListResult<?> getXiehuodibiangengtongzhi() {
        return xiehuodibiangengtongzhi;
    }

    public int getCount(BaseListResult<?> baseListResult) {
        if (baseListResult != null && !ParamUtil.isEmpty(baseListResult.getList())) {
            return baseListResult.getList().size();
        }
        return 0;
    }
}
