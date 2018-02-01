package com.hletong.hyc.util;

import com.hletong.hyc.model.BankCard;
import com.hletong.hyc.model.InvoiceCert;
import com.hletong.hyc.model.PaperPhoto;
import com.xcheng.okhttp.util.EasyPreconditions;
import com.xcheng.okhttp.util.ParamUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chengxin on 2017/7/4.
 */

public class CertParamHelper {

    /**
     * 将 map转化成json
     *
     * @param map
     * @return
     */
    public static JSONObject getMapJson(Map<String, Object> map) {
        EasyPreconditions.checkArgument(!ParamUtil.isEmpty(map), "params can not be null or empty");
        return new JSONObject(map);
    }

    public static List<JSONObject> getBankListjsonArray(List<BankCard> bankCards) {
        List<JSONObject> bankParams = new ArrayList<>();

        for (BankCard card : bankCards) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("bank_type", card.getBankEnum().getId());
            temp.put("bank_address", card.getBankAddress());
            temp.put("name", card.getName());
            temp.put("bank_code", card.getBankCode());
            temp.put("cert_type", card.getPaper().getId());
            temp.put("cert_code", card.getPaperNumber());
            temp.put("is_default", card.isDefault() ? 1 : 2);
            temp.put("assign_code", card.getAssignCode());
            temp.put("bank_file", card.getCardPhoto().getFileGroupId());
            if (card.getBankUuid() != null) {
                temp.put("bank_uuid", card.getBankUuid());
            }
            bankParams.add(getMapJson(temp));
        }
        return bankParams;
    }

    public static List<JSONObject> getPaperjsonArray(List<PaperPhoto> paperPhotos) {
        List<JSONObject> paperParams = new ArrayList<>();

        for (PaperPhoto paper : paperPhotos) {
            if (paper.isDataComplete()) {
                Map<String, Object> temp = new LinkedHashMap<>();
                temp.put("paper_type", paper.getPaperType());
                temp.put("paper_file", paper.getRegisterPhoto().getFileGroupId());
                temp.put("end_dt", paper.getDate());
                if (paper.getPaperUuid() != null) {
                    temp.put("paper_uuid", paper.getPaperUuid());
                }
                paperParams.add(getMapJson(temp));
            }
        }
        return paperParams;
    }

    public static List<JSONObject> getInvoicejsonArray(List<InvoiceCert> paperPhotos) {
        List<JSONObject> paperParams = new ArrayList<>();
        for (InvoiceCert invoce : paperPhotos) {
            Map<String, Object> temp = new LinkedHashMap<>();
            temp.put("invoice_name", invoce.getSprName());
            temp.put("invoice_taxpayer", invoce.getNsrsbh());
            temp.put("invoice_tel", invoce.getTel());
            temp.put("invoice_province", invoce.getProvince());
            temp.put("invoice_city", invoce.getCity());
            temp.put("invoice_county", invoce.getCounty());
            if (invoce.getProvince() != null) {
                temp.put("invoice_address", invoce.getProvince() + " " + invoce.getCity() + " " + invoce.getCounty());
            } else {
                temp.put("invoice_address", invoce.getAddress());
            }
            temp.put("invoice_file", invoce.getInvoicePhoto().getFileGroupId());
            temp.put("is_default", invoce.isDefault() ? 1 : 2);
            temp.put("invoice_bank_code", invoce.getCardNumber());
            temp.put("invoice_bank", invoce.getOpenBankName());
            if (invoce.getInvoiceUuid() != null) {
                temp.put("invoice_uuid", invoce.getInvoiceUuid());
            }
            paperParams.add(getMapJson(temp));
        }
        return paperParams;
    }
}
