package com.hletong.hyc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hletong.hyc.HyApplication;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.InvoiceContract;
import com.hletong.hyc.model.Invoice;
import com.hletong.hyc.model.validate.InvoiceInfo;
import com.hletong.hyc.presenter.InvoicePresenter;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.LocationHelper;
import com.hletong.mob.base.MvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/1/5.
 * 上传发货单
 */

public class InvoiceUploadActivityNew extends MvpActivity<InvoiceContract.Presenter> implements InvoiceContract.View {

    @BindView(R.id.contact)
    TextView relative;
    @BindView(R.id.contract)
    TextView contract;
    @BindView(R.id.contract_block)
    View contractBlock;
    @BindView(R.id.submit)
    View submit;
    @BindView(R.id.contact_info)
    View contact_info;
    @BindView(R.id.contact_desc)
    TextView contact_desc;
    @BindView(R.id.cargo_code)
    CommonInputView mCargoCode;
    @BindView(R.id.cargo_name)
    CommonInputView mCargoName;
    @BindView(R.id.plate)
    CommonInputView mPlate;
    @BindView(R.id.des)
    CommonInputView mDes;
    @BindView(R.id.address)
    CommonInputView mAddress;
    @BindView(R.id.date)
    CommonInputView mDate;
    @BindView(R.id.unit_ct)
    CommonInputView mUnitCt;
    @BindView(R.id.weight)
    CommonInputView mWeight;
    @BindView(R.id.deliver_psw)
    CommonInputView mDeliverPsw;
    @BindView(R.id.loading_psw)
    CommonInputView mLoadingPsw;

    private LocationHelper mLocationHelper;

    @Override
    public int getLayoutId() {
        return R.layout.activity_upload_invoice;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mLocationHelper = new LocationHelper(this);
        getPresenter().start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.phone_selector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CallPhoneDialog.getInstance().show(getSupportFragmentManager());
        return true;
    }

    @OnClick({R.id.contract, R.id.contact_info, R.id.submit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.contact_info:
                getPresenter().call();
                break;
            case R.id.contract: {
                getPresenter().viewContract();
                break;
            }
            case R.id.submit: {
                getPresenter().submit(
                        new InvoiceInfo(
                                HyApplication.getAMapLocation(),
                                getWeight(),
                                getCount(),
                                mDeliverPsw.getInputValue(),
                                mLoadingPsw.getInputValue()
                        )
                );
                break;
            }
        }
    }

    private String getWeight() {
        final String s = mWeight.getInputValue();
        if (TextUtils.isEmpty(s) && mWeight.getInput().getHint() != null)
            return mWeight.getInput().getHint().toString();
        return s;
    }

    private String getCount() {
        final String s = mUnitCt.getInputValue();
        if (TextUtils.isEmpty(s) && mUnitCt.getInput().getHint() != null) {
            return mUnitCt.getInput().getHint().toString();
        }
        return s;
    }

    @Override
    public void startLocating(boolean isForceStart) {
        mLocationHelper.startLocating(isForceStart);
    }

    @Override
    public void onLocationError(String message) {
        mLocationHelper.onLocationError(message);
    }

    @Override
    protected InvoiceContract.Presenter createPresenter() {
        return new InvoicePresenter(this, (Invoice) getParcelable("invoice"));
    }

    @Override
    public void showTitle(String title) {
        setCustomTitle(title);
    }

    @Override
    public void showContact(String label, String contact) {
        contact_desc.setText(label);
        relative.setText(contact);//装货联系人
    }

    @Override
    public void showCargo(String doc, String cargoName, String carrierLabel, String carrier, String cargo, String address, String date) {
        mCargoCode.setText(doc);
        mCargoName.setText(cargoName);
        mPlate.setLabel(carrierLabel);
        mPlate.setText(carrier);
        mDes.setText(cargo);
        mDate.setText(date);
        mAddress.setText(address);
    }

    @Override
    public void showContract() {
        contractBlock.setVisibility(View.VISIBLE);
    }

    @Override
    public void showWeight(String cargo, boolean input) {
        if (input) {
            mWeight.setEditTextHint(cargo);
        } else {
            mWeight.setText(cargo);
            mWeight.setMode(CommonInputView.VIEW);
        }
    }

    @Override
    public void showNumber(String cargo, String unit, boolean fraction, boolean input) {
        mUnitCt.setSuffixText(unit);
        if (input) {
            mUnitCt.setEditTextHint(cargo);
            if (fraction) {
                mUnitCt.setInputType(CommonInputView.NUMBER_DECIMAL);
            } else {
                mUnitCt.setInputType(CommonInputView.NUMBER);
            }
        } else {
            mUnitCt.setText(cargo);
            mUnitCt.setMode(CommonInputView.VIEW);
        }
    }

    @Override
    public void showPasswordArea() {
        mDeliverPsw.setVisibility(View.VISIBLE);
        mLoadingPsw.setVisibility(View.VISIBLE);
    }

    @Override
    public void showSubmitButton() {
        submit.setVisibility(View.VISIBLE);
    }

    @Override
    public void makeCall(String contact, String tel) {
        CallPhoneDialog.getInstance().show(getSupportFragmentManager(), "拨打电话", "是否要联系装货地联系人" + contact, tel);
    }
}
