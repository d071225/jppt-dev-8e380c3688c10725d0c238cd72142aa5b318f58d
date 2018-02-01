package com.hletong.hyc.ui.activity.cargoforecast;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.CargoContact;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.ui.activity.CargoForecastActivity;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.activity.PathPlanActivity;
import com.hletong.hyc.ui.fragment.CargoAddressListFragment;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.mob.architect.view.ITransactionView;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/3/22.
 * 卸货信息
 */

public class BlockUnLoad extends BaseBlock {

    @BindView(R.id.unloadAddress)
    CommonInputView unloadAddress;
    @BindView(R.id.unloadContact)
    CommonInputView unloadContact;
    @BindView(R.id.unloadContactTel)
    CommonInputView unloadContactTel;
    @BindView(R.id.routePlan)
    TextView routePlan;

    private Address loadAddress;
    private Address uAddress;

    public BlockUnLoad(ViewStub viewStub, CargoForecastActivity.BlockAction dictItemDialog, ITransactionView switchDelegate) {
        super(viewStub, "卸货信息", dictItemDialog, switchDelegate);
    }

    @Override
    void initWithSource(Source source, boolean fullCopy) {
        Intent intent = new Intent();
        intent.putExtra("data", source.getUnLoadInfo());
        onActivityResult(103, intent);

        loadAddress = source.getStartAddress();
    }

    @Override
    public void onActivityResult(int requestCode, Intent data) {
        if (requestCode == 103) {
            CargoContact info = data.getParcelableExtra("data");
            if (info.isAddressValid()) {
                uAddress = info.getCustomAddress();
                unloadAddress.setTag(info);
                unloadAddress.setText(info.getCustomAddress().buildAddress(' ', true));
                if (getBillingType() != 3) {
                    unloadContactTel.setText(info.getContactTel());
                    unloadContact.setText(info.getContactName());
                }
            }
        }
    }

    @Override
    boolean isBlockSatisfied() {
        return unloadAddress.getTag() != null;
    }

    @Override
    public String getErrorMessage() {
        if (unloadAddress == null)
            return "请选择卸货地信息";
        return null;
    }

    @Override
    public void getBlockParams(HashMap<String, String> params) {
        CargoContact info = (CargoContact) unloadAddress.getTag();
        params.put("unload_province", info.getProvince());
        params.put("unload_city", info.getCity());
        params.put("unload_country", info.getCountry());
        params.put("unload_addr", info.getAddress());
        params.put("unload_harbor_depth", info.getWaterDepth());
        params.put("unload_contacts", info.getContactName());
        params.put("unload_contacts_tel", info.getContactTel());
    }

    @Override
    public void getSelfTradeBlockParams(HashMap<String, String> params) {
        CargoContact info = (CargoContact) unloadAddress.getTag();
        params.put("unloadProvince", info.getProvince());
        params.put("unloadCity", info.getCity());
        params.put("unloadCountry", info.getCountry());
        params.put("unloadAddr", info.getAddress());
    }

    @Override
    public void fillSource(Source source) {
        source.setUnLoadInfo((CargoContact) unloadAddress.getTag());
    }

    @OnClick({R.id.unloadAddress, R.id.unloadContact, R.id.unloadContactTel, R.id.routePlan})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.unloadAddress:
            case R.id.unloadContact:
            case R.id.unloadContactTel:
                startActivityForResult(CommonWrapFragmentActivity.class, 103,
                        CommonWrapFragmentActivity.getBundle("卸货地信息", CargoAddressListFragment.class,
                                CargoAddressListFragment.getDefaultBundle(
                                        false, -1, "新增常用地址", "管理装卸货信息", getBillingType() == 3)));
                break;
            case R.id.routePlan:
                if (loadAddress == null)
                    showMessage("请选择装货地");
                else if (uAddress == null)
                    showMessage("请选择卸货地");
                else {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable(PathPlanActivity.START_ADDRESS, loadAddress);
                    bundle.putParcelable(PathPlanActivity.END_ADDRESS, uAddress);
                    startActivity(PathPlanActivity.class, bundle);
                }
                break;
        }
    }

    @Override
    public void onBlockFieldChanged(BaseBlock block, String dictType, Object object) {
        if ("transportTypeEnum".equals(dictType)) {
            DictionaryItem di = (DictionaryItem) object;
            if ("1".equals(di.getId())) {//车辆有路径规划
                setViewVisibility(routePlan, View.VISIBLE);
            } else {//船舶没得路径规划
                setViewVisibility(routePlan, View.GONE);
            }
        } else if ("loadAddress".equals(dictType)) {
            this.loadAddress = (Address) object;
        }
    }

    @Override
    void billingTypeChangedInternal(int billingType) {
        super.billingTypeChangedInternal(billingType);
        if (billingType == 3) {
            //自主交易时，地址是从地址选择器选择,详细地址要手动填写，没有卸货联系人和电话
            unloadContact.setVisibility(View.GONE);
            unloadContactTel.setVisibility(View.GONE);
        } else {
            unloadContact.setVisibility(View.VISIBLE);
            unloadContactTel.setVisibility(View.VISIBLE);
        }
        childViewVisibilityChanged();
    }
}
