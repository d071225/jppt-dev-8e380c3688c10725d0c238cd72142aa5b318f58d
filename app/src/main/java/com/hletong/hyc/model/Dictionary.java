package com.hletong.hyc.model;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 所有字典
 **/
public class Dictionary {
    /**
     * plate_color : [{"id":"0","text":"蓝色"},{"id":"1","text":"黄色"},{"id":"2","text":"黑色"},{"id":"3","text":"白色"}]
     */
    @SerializedName("plate_color")
    private List<DictionaryItem> plateColor;
    @SerializedName("truck_type")
    private List<DictionaryItem> truckType;
    //拖挂轮轴
    @SerializedName("trailer_axle")
    private List<DictionaryItem> trailerAxle;
    //车辆轴数
    @SerializedName("axle_count")
    private List<DictionaryItem> axleCount;
    //船舶类型
    @SerializedName("ship_type")
    private List<DictionaryItem> shipType;

    private List<DictionaryItem> billingTypeEnum;
    private List<DictionaryItem> multiTransportFlag;
    private List<DictionaryItem> measureTypeEnum;
    private List<DictionaryItem> truckCarrierLengthTypeEnum;
    private List<DictionaryItem> shipCarrierModelTypeEnum;
    private List<DictionaryItem> unitsEnum;
    private List<DictionaryItem> chargeRefTypeEnum;
    private List<DictionaryItem> transportTypeEnum;
    private List<DictionaryItem> transTypeEnum;
    private List<DictionaryItem> settleAuthEnum;
    private List<DictionaryItem> settleTypeEnum;
    private List<DictionaryItem> insureTypeEnum;
    private List<DictionaryItem> appointTransportFlagEnum;
    private List<DictionaryItem> truckCarrierModelTypeEnum;
    private List<DictionaryItem> bookRefTypeEnum;

    private CargoOwnerInfo memberDto;
    private int isCatchAllFlag;//是否兜底单位，0是，1不是
    private String invoiceType;//会员类型，1=>平台开票会员，2=>自主开票会员，3=>普通会员
    private double taxRate;//

    private Map<String, List<DictionaryItem>> dicts;

    public Map<String, List<DictionaryItem>> getDictionaries() throws IllegalAccessException {
        if (dicts != null)
            return dicts;
        dicts = new HashMap<>();
        /**
         * 这里说一下，由于整个类的数据都是通过{@link com.hletong.hyc.presenter.DictItemPresenter}
         * 来取的，而这个Presenter又是只用于取{@link DictionaryItem}这种类型的数据的
         * 这里多出的几个数据{@value #memberDto},{@value #isCatchAllFlag},{@value #invoiceType},
         * 要取用的话也就必须转成DictionaryItem的形式了，这里把这几项包裹成DictionaryItem
         */
        {
            ArrayList<DictionaryItem> arrayList = new ArrayList<>(1);
            arrayList.add(new DictionaryItem(String.valueOf(isCatchAllFlag), String.valueOf(isCatchAllFlag)));
            dicts.put("isCatchAllFlag", arrayList);
        }
        if (!TextUtils.isEmpty(invoiceType)) {
            ArrayList<DictionaryItem> arrayList = new ArrayList<>(1);
            arrayList.add(new DictionaryItem(invoiceType, invoiceType));
            dicts.put("invoiceType", arrayList);
        }
        {
            ArrayList<DictionaryItem> arrayList = new ArrayList<>(1);
            arrayList.add(new DictionaryItem("taxRate", String.valueOf(taxRate)));
            dicts.put("taxRate", arrayList);
        }
        {//议价字典项，服务端没有给，自己造一个
            ArrayList<DictionaryItem> arrayList = new ArrayList<>(2);
            arrayList.add(new DictionaryItem("0", "否"));
            arrayList.add(new DictionaryItem("1", "是"));
            dicts.put("appCustomQuoteFlagEnum", arrayList);
        }

        Field[] fields = getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object o = field.get(this);
            if (o instanceof List) {
                dicts.put(field.getName(), (List<DictionaryItem>) o);
            }
        }
        return dicts;
    }

    public List<DictionaryItem> getDictionary(String fieldName) {
        try {
            return getDictionaries().get(fieldName);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getMemberDto() {
        return new Gson().toJson(memberDto);
    }
}
