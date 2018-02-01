package com.hletong.hyc.model;

import com.google.gson.annotations.SerializedName;
import com.hletong.hyc.enums.AppTypeConfig;

/**
 * 待办事项通知接口
 * Created by cc on 2016/12/27.
 */
public class UnTodo {
    /**
     * 挂价摘牌
     **/
    @SerializedName("01")
    private int guajizhaipai;
    /**
     * 网签承运合同
     **/
    @SerializedName("02")
    private int wangqianchengyunhetong;
    /**
     * 发货单回传
     **/
    @SerializedName("03")
    private int fahuodanhuichuang;
    /**
     * 收货单回传
     **/
    @SerializedName("04")
    private int shouhuodanhuichuan;
    /**
     * 竞价摘牌
     **/
    @SerializedName("06")
    private int jinjiazhaipai;
    /**
     * 事故确认单
     **/
    @SerializedName("10")
    private int shigudanqueren;
    /**
     * 违约确认
     **/
    @SerializedName("11")
    private int weiyuequeren;
    /**
     * 守约确认
     **/
    @SerializedName("12")
    private int shouyuequeren;
    /**
     * 滞压费用单证上传
     **/
    @SerializedName("14")
    private int zhiyafeiyongdanshangchuan;
    /**
     * 收货单运单回传
     **/
    @SerializedName("50")
    private int shouhuodanyundanhuichuan;
    /**
     * 卸货地变更补签合同
     **/
    @SerializedName("61")
    private int xiehuodibiangengbuqianhetong;
    /**
     * 卸货地变更通知
     **/
    @SerializedName("62")
    private int xiehuodibiangengtongzhi;

    @SerializedName("05")
    private int huofanghetong;
    @SerializedName("200")//货方自主交易货方确认代办
    private int zizhujiaoyihuofangquerendaiban;
    @SerializedName("201")//车船自主交易货方确认代办
    private int zizhujiaoyichechuangquerendaiban;
    @SerializedName("202")//会员管理单位垫付审核
    private int huiyuanguanlidangweidianfubaozhengjin;
    @SerializedName("203")//申请垫付保证金
    private int shenqingdianfubaozhengjin;
    @SerializedName("204")//议价进度
    private int yijiajingdu;
    @SerializedName("205")//报价进度
    private int baojiajindu;
    @SerializedName("300")//会员评价
    private int huiyuanpingjia;

    public int getGuajizhaipai() {
        return guajizhaipai;
    }

    public int getWangqianchengyunhetong() {
        return wangqianchengyunhetong;
    }

    public int getHuofanghetong() {
        return huofanghetong;
    }

    public int getFahuodanhuichuang() {
        return fahuodanhuichuang;
    }

    public int getShouhuodanhuichuan() {
        return shouhuodanhuichuan;
    }

    public int getJinjiazhaipai() {
        return jinjiazhaipai;
    }

    public int getMessageCount() {
        //车船
        if (AppTypeConfig.isTransporter()) {
            return shigudanqueren + weiyuequeren + shouyuequeren + zhiyafeiyongdanshangchuan + shouhuodanyundanhuichuan + xiehuodibiangengbuqianhetong + xiehuodibiangengtongzhi
                    + zizhujiaoyichechuangquerendaiban + huiyuanguanlidangweidianfubaozhengjin + shenqingdianfubaozhengjin + baojiajindu;
        } else {
            //货方
            return shouhuodanyundanhuichuan + huofanghetong + shenqingdianfubaozhengjin + xiehuodibiangengbuqianhetong + xiehuodibiangengtongzhi
                    + yijiajingdu + huiyuanpingjia;
        }
    }

    public UndoEvent getUndoEvent() {
        return new UndoEvent(getMessageCount()
                + getGuajizhaipai()
                + getWangqianchengyunhetong()
                + getHuofanghetong()
                + getFahuodanhuichuang()
                + getShouhuodanhuichuan()
                + getJinjiazhaipai());
    }

    public static class UndoEvent {
        private int undoCount;

        private UndoEvent(int undoCount) {
            this.undoCount = undoCount;
        }

        public int getUndoCount() {
            return undoCount;
        }
    }
}
