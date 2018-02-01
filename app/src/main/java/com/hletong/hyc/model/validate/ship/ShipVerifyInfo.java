package com.hletong.hyc.model.validate.ship;

import android.text.TextUtils;

import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.validate.Paperwork;
import com.hletong.hyc.model.validate.Validate;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.util.ParamsHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongdaqing on 2017/6/17.
 * 船舶信息补全
 */

public class ShipVerifyInfo implements Validate<CommonResult> {
    private String ship;// 船编号(船名字)
    private DictionaryItem shipType;// 船舶类型
    private String shipLength;// 船长
    private String fullDraft;// 满载吃水
    private String netTon;// 净吨位
    private String loadTon;// 载重吨位
    private List<Paperwork> paperStr;//证件资料
    private RegisterPersonalShipInfo.ShipInfo mShipInfo;

    public ShipVerifyInfo(String ship, RegisterPersonalShipInfo.ShipInfo shipInfo, List<Paperwork> paperStr) {
        this.ship = ship;
        this.mShipInfo = shipInfo;
        this.paperStr = paperStr;
        if (mShipInfo != null) {
            this.shipType = mShipInfo.getShip_type();
            this.shipLength = mShipInfo.getShip_length();
            this.fullDraft = mShipInfo.getFull_draft();
            this.netTon = mShipInfo.getNt_ton();
            this.loadTon = mShipInfo.getLoad_ton();
        }
    }

    @Override
    public ItemRequestValue<CommonResult> validate(IBaseView baseView) {
        boolean valid = false;
        for (Paperwork p : paperStr) {
            valid = valid || p.paramsValid();
        }

        if (!valid) {
            baseView.handleError(ErrorFactory.getParamError("请选择一种证件上传"));
            return null;
        }

        if (mShipInfo == null){
            baseView.handleError(ErrorFactory.getParamError("请填写船舶信息"));
            return null;
        }

        if (shipType == null) {
            baseView.handleError(ErrorFactory.getParamError("请选择船舶类型"));
            return null;
        } else if (TextUtils.isEmpty(shipLength)) {
            baseView.handleError(ErrorFactory.getParamError("请输入船长"));
            return null;
        } else if (TextUtils.isEmpty(fullDraft)) {
            baseView.handleError(ErrorFactory.getParamError("请输入满载吃水"));
            return null;
        } else if (TextUtils.isEmpty(netTon)) {
            baseView.handleError(ErrorFactory.getParamError("请输入净吨位"));
            return null;
        } else if (TextUtils.isEmpty(loadTon)) {
            baseView.handleError(ErrorFactory.getParamError("请输入载重吨位"));
            return null;
        }

        return new ItemRequestValue<CommonResult>(
                baseView.hashCode(),
                Constant.getUrl(Constant.COMPLETE_SHIP_INFO),
                new ParamsHelper()
                        .put("ship", ship)
                        .put("shipType", shipType.getId())
                        .put("shipLength", shipLength)
                        .put("fullDraft", fullDraft)
                        .put("netTon", netTon)
                        .put("loadTon", loadTon)
                        .put("paperStr", formatAsString(paperStr))) {
        };
    }

    public static String formatAsString(List<Paperwork> data) {
        List<Paperwork> list = new ArrayList<>(data);
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).paramsValid()) {
                list.remove(i);
                i--;
            }
        }
        String kk = "[";
        for (int i = 0; i < list.size(); i++) {
            kk += list.get(i).getAsString();
            kk += ",";
        }
        return kk.substring(0, kk.length() - 1) + "]";
    }
}
