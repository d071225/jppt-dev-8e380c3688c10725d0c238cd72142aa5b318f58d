package com.hletong.hyc.ui.activity.cargoforecast;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewStub;

import com.hletong.hyc.R;
import com.hletong.hyc.model.CargoContact;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.ui.activity.CargoForecastActivity;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.fragment.CargoAddressListFragment;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.ui.widget.DatePickerView;
import com.hletong.mob.architect.view.ITransactionView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/3/22.
 * 装货信息
 */

public class BlockLoad extends BaseBlock {
    @BindView(R.id.loadDT)
    DatePickerView loadDT;
    @BindView(R.id.loadTM)
    DatePickerView loadTM;
    @BindView(R.id.loadTM_)
    CommonInputView loadTM_;
    @BindView(R.id.loadAddress)
    CommonInputView loadAddress;
    @BindView(R.id.loadContact)
    CommonInputView loadContact;
    @BindView(R.id.loadContactTel)
    CommonInputView loadContactTel;

    public BlockLoad(ViewStub viewStub, CargoForecastActivity.BlockAction dictItemDialog, ITransactionView switchDelegate) {
        super(viewStub, "装货信息", dictItemDialog, switchDelegate);
    }

    @OnClick({R.id.loadAddress})
    public void onClick() {
        startActivityForResult(CommonWrapFragmentActivity.class, 102,
                CommonWrapFragmentActivity.getBundle("装货地信息", CargoAddressListFragment.class,
                        CargoAddressListFragment.getDefaultBundle(
                                false, -1, "新增常用地址", "管理装卸货信息", getBillingType() == 3)));
    }

    @Override
    void viewInflated(View view) {
        if (getBillingType() != 3) {
            loadContact.setOnClickListener(this);
            loadContactTel.setOnClickListener(this);
        }
    }

    @Override
    void initWithSource(Source source, boolean fullCopy) {
        if (fullCopy) {
            if (!TextUtils.isEmpty(source.getLoading_start_dt()))
                loadDT.setStart(source.getLoading_start_dt());
            if (!TextUtils.isEmpty(source.getLoading_end_dt()))
                loadDT.setEnd(source.getLoading_end_dt());
            if (!TextUtils.isEmpty(source.getLoading_start_tm()))
                loadTM.setStart(source.getLoading_start_tm());
            if (!TextUtils.isEmpty(source.getLoading_end_tm()))
                loadTM.setEnd(source.getLoading_end_tm());
        }
        CargoContact contact = source.getLoadingInfo();
        Intent intent = new Intent();
        intent.putExtra("data", contact);
        onActivityResult(102, intent);
    }

    @Override
    public void onActivityResult(int requestCode, Intent data) {
        if (requestCode == 102) {
            CargoContact info = data.getParcelableExtra("data");
            if (info.isAddressValid()) {
                loadAddress.setTag(info);
                loadAddress.setText(info.getCustomAddress().buildAddress(' ', true));
                if (getBillingType() != 3) {
                    loadContactTel.setText(info.getContactTel());
                    loadContact.setText(info.getContactName());
                }
                notifyItemChanged("loadAddress", info.getCustomAddress());
            }
        }
    }

    @Override
    boolean isBlockSatisfied() {
        if (getBillingType() == 3) {//自主交易
            return loadDT.getStart() != null
                    && loadDT.getEnd() != null
                    && loadAddress.getTag() != null
                    && loadContact.inputHaveValue()
                    && loadContactTel.inputHaveValue();
        } else {
            return loadDT.getStart() != null
                    && loadDT.getEnd() != null
                    && loadTM.getStart() != null
                    && loadTM.getEnd() != null
                    && loadAddress.getTag() != null;
        }
    }

    @Override
    public String getErrorMessage() {
        if (loadDT.getStart() == null)
            return "请选择开始装货时间";
        if (loadDT.getEnd() == null)
            return "请选择结束装货时间";
        if (!loadDT.isStartEarlierThanEnd())
            return "装货结束日期不能早于开始日期";
        if (loadAddress.getTag() == null)
            return "请选择装货地址";
        if (getBillingType() != 3) {
            if (loadTM.getStart() == null)
                return "请选择装货开始时间";
            if (loadTM.getEnd() == null)
                return "请选择装货结束时间";
            if (!loadTM.isStartEarlierThanEnd())
                return "装货结束时间不能早于开始时间";
        }
        return null;
    }

    @Override
    public void getBlockParams(HashMap<String, String> params) {
        if (loadDT.getStart() != null)
            params.put("loading_start_dt", loadDT.getStart().dateString(true, ""));
        if (loadDT.getEnd() != null)
            params.put("loading_end_dt", loadDT.getEnd().dateString(true, ""));
        CargoContact info = (CargoContact) loadAddress.getTag();
        params.put("loading_province", info.getProvince());
        params.put("loading_city", info.getCity());
        params.put("loading_country", info.getCountry());
        params.put("loading_addr", info.getAddress());
        params.put("loading_harbor_depth", info.getWaterDepth());
        if (loadTM.getStart() != null)
            params.put("loading_start_tm", loadTM.getStart().timeString(true, ""));
        if (loadTM.getEnd() != null)
            params.put("loading_end_tm", loadTM.getEnd().timeString(true, ""));
        params.put("loading_contacts", info.getContactName());
        params.put("loading_contacts_tel", info.getContactTel());
    }

    @Override
    public void getSelfTradeBlockParams(HashMap<String, String> params) {
        if (loadDT.getStart() != null)
            params.put("loadingStartDt", loadDT.getStart().dateString(true, ""));
        if (loadDT.getEnd() != null)
            params.put("loadingEndDt", loadDT.getEnd().dateString(true, ""));
        CargoContact info = (CargoContact) loadAddress.getTag();
        params.put("loadingProvince", info.getProvince());
        params.put("loadingCity", info.getCity());
        params.put("loadingCountry", info.getCountry());
        params.put("loadingAddr", info.getAddress());
    }

//    @Override
//    public void getBlockParams(HashMap<String, String> params) {
//        if (loadDT.getStart() != null)
//            params.put("loading_start_dt", loadDT.getStart().dateString(true, ""));
//        if (loadDT.getEnd() != null)
//            params.put("loading_end_dt", loadDT.getEnd().dateString(true, ""));
//        CargoContact info = (CargoContact) loadAddress.getTag();
//        params.put("loading_province", info.getGoods_address_province());
//        params.put("loading_city", info.getGoods_address_city());
//        params.put("loading_country", info.getGoods_address_area());
//        params.put("loading_addr", info.getAddress());
//        params.put("loading_harbor_depth", "");
//        if (loadTM.getStart() != null)
//            params.put("loading_start_tm", loadTM.getStart().timeString(true, ""));
//        if (loadTM.getEnd() != null)
//            params.put("loading_end_tm", loadTM.getEnd().timeString(true, ""));
//        params.put("loading_contacts", info.getContact_name());
//        params.put("loading_contacts_tel", info.getContact_tel());
//    }
//
//    @Override
//    public void getSelfTradeBlockParams(HashMap<String, String> params) {
//        if (loadDT.getStart() != null)
//            params.put("loadingStartDt", loadDT.getStart().dateString(true, ""));
//        if (loadDT.getEnd() != null)
//            params.put("loadingEndDt", loadDT.getEnd().dateString(true, ""));
//        CargoContact info = (CargoContact) loadAddress.getTag();
//        params.put("loadingProvince", info.getGoods_address_province());
//        params.put("loadingCity", info.getGoods_address_city());
//        params.put("loadingCountry", info.getGoods_address_area());
//        params.put("loadingAddr", info.getAddress());
////        params.put("contactName", loadContact.getInputValue());
////        params.put("contactTel", loadContactTel.getInputValue());
//    }


    @Override
    public void fillSource(Source source) {
        if (loadDT.getStart() != null)
            source.setLoading_start_dt(loadDT.getStart().dateString(true, ""));
        if (loadDT.getEnd() != null)
            source.setLoading_end_dt(loadDT.getEnd().dateString(true, ""));
        if (loadTM.getStart() != null)
            source.setLoading_start_tm(loadTM.getStart().timeString(true, ""));
        if (loadTM.getEnd() != null)
            source.setLoading_end_tm(loadTM.getEnd().timeString(true, ""));
        source.setLoadingInfo((CargoContact) loadAddress.getTag());
    }

    @Override
    void billingTypeChangedInternal(int billingType) {
        super.billingTypeChangedInternal(billingType);
        if (billingType == 3) {//自主交易
            loadContact.setVisibility(View.GONE);
            loadContactTel.setVisibility(View.GONE);
            loadTM_.setVisibility(View.GONE);
        } else {
            loadContact.setVisibility(View.VISIBLE);
            loadContactTel.setVisibility(View.VISIBLE);
            loadTM_.setVisibility(View.VISIBLE);
        }
        childViewVisibilityChanged();
    }
}
