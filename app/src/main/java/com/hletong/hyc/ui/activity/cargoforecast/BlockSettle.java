package com.hletong.hyc.ui.activity.cargoforecast;

import android.content.Intent;
import android.view.View;
import android.view.ViewStub;

import com.hletong.hyc.R;
import com.hletong.hyc.model.BusinessContact;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.Drawee;
import com.hletong.hyc.model.Payer;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.ui.activity.CargoForecastActivity;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.fragment.CargoForecastBaseSelectFragment;
import com.hletong.hyc.ui.fragment.CargoForecastBillReceiverFragment;
import com.hletong.hyc.ui.fragment.CargoForecastContactFragment;
import com.hletong.hyc.ui.fragment.CargoForecastPayerFragment;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.mob.architect.view.ITransactionView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/3/22.
 * 付款及开票信息
 */

public class BlockSettle extends BlockNoneSelfTrade {
    @BindView(R.id.payer)
    CommonInputView payer;
    @BindView(R.id.billReceiver)
    CommonInputView billReceiver;
    @BindViews({R.id.settleAuth, R.id.chargeType})
    CommonInputView[] mViews;
    @BindViews({R.id.shipper, R.id.receiver})
    CommonInputView[] mContacts;

    public BlockSettle(ViewStub VSSettle, CargoForecastActivity.BlockAction dictItemDialog, ITransactionView switchDelegate) {
        super(VSSettle, "付款及开票信息", dictItemDialog, switchDelegate);
    }

    @Override
    void initWithSource(Source source, boolean fullCopy) {
        {
            Payer payer = source.getPayer();
            if (payer != null) {
                Intent intent = new Intent();
                intent.putExtra("data", payer);
                onActivityResult(104, intent);
            }
        }

        {
            Drawee drawee = source.getDrawee();
            if (drawee != null) {
                Intent intent = new Intent();
                intent.putExtra("data", drawee);
                onActivityResult(105, intent);
            }
        }

        {
            DictionaryItem di = source.getSettleAuthAsDI();
            if (di != null) {
                onItemSelected(di, 0);
            }
        }
        {
            DictionaryItem di = source.getChargeRefTypeAsDI();
            if (di != null) {
                onItemSelected(di, 1);
            }
        }
    }

    @OnClick({R.id.payer, R.id.billReceiver, R.id.settleAuth, R.id.chargeType, R.id.shipper, R.id.receiver})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.payer:
                startActivityForResult(CommonWrapFragmentActivity.class, 104, CommonWrapFragmentActivity.getBundle(
                        "付款方", CargoForecastPayerFragment.class, CargoForecastBaseSelectFragment.getDefaultBundle(false, -1, null, null)));
                break;
            case R.id.billReceiver:
                startActivityForResult(CommonWrapFragmentActivity.class, 105, CommonWrapFragmentActivity.getBundle(
                        "受票方", CargoForecastBillReceiverFragment.class, CargoForecastBaseSelectFragment.getDefaultBundle(false, -1, null, null)));
                break;
            case R.id.settleAuth:
                showSelector("settleAuthEnum", "结算权限", 0);
                break;
            case R.id.chargeType:
                showSelector("chargeRefTypeEnum", "收费依据", 1);
                break;
            case R.id.shipper:
                startActivityForResult(CommonWrapFragmentActivity.class, 106, CommonWrapFragmentActivity.getBundle(
                        "发货人", CargoForecastContactFragment.class, CargoForecastBaseSelectFragment.getDefaultBundle(false, -1, "新增联系人", "管理联系人")));
                break;
            case R.id.receiver:
                startActivityForResult(CommonWrapFragmentActivity.class, 107, CommonWrapFragmentActivity.getBundle(
                        "收货人", CargoForecastContactFragment.class, CargoForecastBaseSelectFragment.getDefaultBundle(false, -1, "新增联系人", "管理联系人")));
                break;
        }
    }

    @Override
    public List<DictionaryItem> prefetchItems() {
        List<DictionaryItem> list = new ArrayList<>();
        if (mViews[0].getTag() == null)
            list.add(new DictionaryItem("0", "settleAuthEnum"));
        if (mViews[1].getTag() == null)
            list.add(new DictionaryItem("1", "chargeRefTypeEnum"));
        return list;
    }

    @Override
    public void onItemSelected(DictionaryItem item, int extra) {
        mViews[extra].setText(item.getText());
        mViews[extra].setTag(item);
    }

    @Override
    boolean isBlockSatisfied() {
        boolean r = payer.getTag() != null &&
                mViews[0].getTag() != null &&
                mViews[1].getTag() != null;
        if (getBillingType() == 1) {//平台开票要选择受票方
            r &= billReceiver.getTag() != null;
        }
        return r;
    }

    @Override
    public String getErrorMessage() {
        if (payer.getTag() == null)
            return "请选择付款方";
        if (mViews[0].getTag() == null)
            return "请选择结算权限";
        if (mViews[1].getTag() == null)
            return "请选择收费依据";
        if (getBillingType() == 1 && billReceiver.getTag() == null)
            return "请选择受票方";
        return null;
    }

    @Override
    public void getBlockParams(HashMap<String, String> params) {
        Payer cp = (Payer) payer.getTag();
        params.put("payer_member_code", cp.getDelegate_uuid());
        Drawee draweeInfo = (Drawee) billReceiver.getTag();
        if (draweeInfo != null) {
            params.put("drawee_name", draweeInfo.getInvoice_name());
            params.put("drawee_tax_code", draweeInfo.getInvoice_taxpayer());
            params.put("drawee_tel", draweeInfo.getInvoice_tel());
            params.put("drawee_addr", draweeInfo.getInvoice_address());
            params.put("drawee_bank_acct_no", draweeInfo.getInvoice_bank_code());
            params.put("drawee_open_bank_name", draweeInfo.getInvoice_bank());
        }
        params.put("settle_auth", getTag(mViews[0]).getId());
        params.put("charge_refer_type", getTag(mViews[1]).getId());
        BusinessContact de = (BusinessContact) mContacts[0].getTag();
        if (de != null) {
            params.put("loading_contacts_name", de.getContact_name());
            params.put("loading_contacts_tax_code", de.getTaxpayer_code());
        }
        BusinessContact re = (BusinessContact) mContacts[1].getTag();
        if (re != null) {
            params.put("unload_contacts_name", re.getContact_name());
            params.put("unload_contacts_tax_code", re.getTaxpayer_code());
        }
    }

    @Override
    public void fillSource(Source source) {
        source.setDraweeInfo((Drawee) billReceiver.getTag());
        source.setPayerMemberCode((Payer) payer.getTag());
        source.setSettleAuth((DictionaryItem) mViews[0].getTag());
        source.setChargeReferType((DictionaryItem) mViews[1].getTag());
        source.setShipContact((BusinessContact) mContacts[0].getTag());
        source.setReceiveContact((BusinessContact) mContacts[1].getTag());
    }

    @Override
    void billingTypeChangedInternal(int billingType) {
        super.billingTypeChangedInternal(billingType);
        if (!isVisible())
            return;
        switch (billingType) {
            case 1://平台开票
                billReceiver.setVisibility(View.VISIBLE);
                mContacts[0].setVisibility(View.VISIBLE);
                mContacts[1].setVisibility(View.VISIBLE);
                childViewVisibilityChanged();
                break;
            case 2://自主开票，没有受票方，收发货人
                clear(billReceiver);
                clear(mContacts[0]);
                clear(mContacts[1]);
                billReceiver.setVisibility(View.GONE);
                mContacts[0].setVisibility(View.GONE);
                mContacts[1].setVisibility(View.GONE);
                childViewVisibilityChanged();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, Intent data) {
        if (requestCode == 104) {
            Payer da = data.getParcelableExtra("data");
            payer.setText(da.getAccountName());
            payer.setTag(da);
        } else if (requestCode == 105) {
            Drawee da = data.getParcelableExtra("data");
            billReceiver.setText(da.getInvoice_name());
            billReceiver.setTag(da);
        } else if (requestCode == 106 || requestCode == 107) {
            BusinessContact contact = data.getParcelableExtra("data");
            mContacts[requestCode - 106].setText(contact.getContact_name());
            mContacts[requestCode - 106].setTag(contact);
        }
    }
}
