package com.hletong.hyc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.InfoCompleteContract;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.InvoiceCert;
import com.hletong.hyc.model.RegisterPhoto;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.util.StringUtil;
import com.xcheng.view.util.JumpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 开票信息输入
 * Created by chengxin on 2017/6/12.
 */
public class InvoiceCertInputActivity extends BaseActivity implements InfoCompleteContract.View {
    @BindView(R.id.cv_invoicePersonName)
    CommonInputView cv_invoicePersonName;
    //纳税人识别号
    @BindView(R.id.cv_nsrsbh)
    CommonInputView cv_nsrsbh;
    //证件资料
    @BindView(R.id.cv_openingbank)
    CommonInputView cv_openingBank;

    @BindView(R.id.cv_cardNumber)
    CommonInputView cv_cardNumber;

    @BindView(R.id.cv_tel)
    CommonInputView cv_tel;

    @BindView(R.id.cv_address)
    CommonInputView cv_address;

    @BindView(R.id.iv_invoiceCert)
    ImageView iv_invoiceCert;
    private InvoiceCert mInvoiceCert;
    private Address mAddress;

    @Override
    public int getLayoutId() {
        return R.layout.activity_invoice_input;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        mInvoiceCert = getSerializable(InvoiceCert.class.getSimpleName());
        if (mInvoiceCert != null) {
            cv_cardNumber.setText(mInvoiceCert.getCardNumber());
            cv_openingBank.setText(mInvoiceCert.getOpenBankName());
            mAddress = new Address(mInvoiceCert.getProvince(), mInvoiceCert.getCity(), mInvoiceCert.getCounty(), "");
            cv_address.setText(mAddress.buildAddress());
            Glide.with(this).load(mInvoiceCert.getInvoicePhoto().getPhotos()[0]).fitCenter().into(iv_invoiceCert);
            cv_tel.setText(mInvoiceCert.getTel());
            cv_nsrsbh.setText(mInvoiceCert.getNsrsbh());
            cv_invoicePersonName.setText(mInvoiceCert.getSprName());
        } else {
            mInvoiceCert = new InvoiceCert();
        }
    }


    @OnClick({R.id.iv_invoiceCert, R.id.cv_address, R.id.btn_commit})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.iv_invoiceCert) {
            toActivity(UploadPhotoActivity.class,
                    JumpUtil.getBundle(RegisterPhoto.class.getSimpleName(), mInvoiceCert.getInvoicePhoto()),
                    Constant.PHOTO_ADD_REQUEST,
                    null);
        } else if (v.getId() == R.id.btn_commit) {
            if (bindCard()) {
                Intent intent = new Intent();
                intent.putExtra(InvoiceCert.class.getSimpleName(), mInvoiceCert);
                setResult(RESULT_OK, intent);
                finish();
            }
        } else if (v.getId() == R.id.cv_address) {
            toActivity(AddressSelectorActivity.class, JumpUtil.getBundle("title", "选择地址"), Constant.SELECT_ADDRESS_REQUEST, null);
        }
    }

    private boolean bindCard() {
        if (StringUtil.isTrimBlank(cv_invoicePersonName.getInputValue())) {
            showMessage("请输入受票人姓名");
            return false;
        } else if (!StringUtil.isChinese(cv_invoicePersonName.getInputValue())) {
            showMessage("受票人姓名必须为中文");
            return false;
        } else if (StringUtil.isTrimBlank(cv_nsrsbh.getInputValue())) {
            showMessage("请输入纳税人识别号");
            return false;
        } else if (StringUtil.isTrimBlank(cv_openingBank.getInputValue())) {
            showMessage("请输入开户银行");
            return false;
        } else if (StringUtil.isTrimBlank(cv_cardNumber.getInputValue())) {
            showMessage("请输入银行账号");
            return false;
        } else if (StringUtil.isTrimBlank(cv_tel.getInputValue())) {
            showMessage("请输入联系电话");
            return false;
        } else if (mAddress == null) {
            showMessage("请选择地址");
            return false;
        } else if (!mInvoiceCert.getInvoicePhoto().canUpload()) {
            showMessage("请上传开票图片");
            return false;
        }
        mInvoiceCert.setProvince(mAddress.getRealProvince());
        mInvoiceCert.setCity(mAddress.getRealCity());
        mInvoiceCert.setCounty(mAddress.getRealCounty());
        mInvoiceCert.setSprName(cv_invoicePersonName.getInputValue());
        mInvoiceCert.setNsrsbh(cv_nsrsbh.getInputValue());
        mInvoiceCert.setCardNumber(cv_cardNumber.getInputValue());
        mInvoiceCert.setOpenBankName(cv_openingBank.getInputValue());
        mInvoiceCert.setTel(cv_tel.getInputValue());
        return true;

    }

    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        if (requestCode == Constant.PHOTO_ADD_REQUEST) {
            RegisterPhoto invoicePhoto = (RegisterPhoto) data.getSerializableExtra(RegisterPhoto.class.getSimpleName());
            mInvoiceCert.setInvoicePhoto(invoicePhoto);
            Glide.with(this).load(invoicePhoto.getPhotos()[0]).fitCenter().into(iv_invoiceCert);
        } else if (requestCode == Constant.SELECT_ADDRESS_REQUEST) {
            mAddress = Address.getAddress(data);
            cv_address.setText(mAddress.buildAddress());
            mInvoiceCert.setProvince(mAddress.getRealProvince());
            mInvoiceCert.setCity(mAddress.getRealCity());
            mInvoiceCert.setCounty(mAddress.getRealCounty());
        }
    }

    @Override
    public void success(String message) {
        showMessage(message);
    }
}
