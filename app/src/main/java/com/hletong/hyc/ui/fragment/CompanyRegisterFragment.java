package com.hletong.hyc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.RegisterContract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.Agreement;
import com.hletong.hyc.model.CargoAgreement;
import com.hletong.hyc.model.CompanyShipAgreement;
import com.hletong.hyc.model.CompanyTruckAgreement;
import com.hletong.hyc.model.PaperTable;
import com.hletong.hyc.model.RegisterPhoto;
import com.hletong.hyc.model.validate.RegisterCompanyInfo;
import com.hletong.hyc.model.validate.RegisterInfo;
import com.hletong.hyc.presenter.RegisterTabPresenter;
import com.hletong.hyc.ui.activity.AddressSelectorActivity;
import com.hletong.hyc.ui.activity.SetPasswordActivity;
import com.hletong.hyc.ui.activity.UploadPhotoActivity;
import com.hletong.hyc.ui.widget.SmoothClickCheckBox;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.ContractPack;
import com.hletong.hyc.util.GetMemberUnit;
import com.hletong.mob.base.BaseFragment;
import com.hletong.mob.widget.BorderLinearLayout;
import com.xcheng.okhttp.util.ParamUtil;
import com.xcheng.view.controller.dialog.BottomOptionDialog;
import com.xcheng.view.controller.dialog.SimpleSelectListener;
import com.xcheng.view.util.LocalDisplay;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 公司注册
 * Created by cx on 2016/10/13.
 */
public class CompanyRegisterFragment extends BaseFragment implements RegisterContract.IBaseInfoView {
    @BindView(R.id.tv_topTip)
    TextView tvTopTip;
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.et_companyname)
    EditText etCompanyName;
    @BindView(R.id.et_loginname)
    EditText etLoginName;
    @BindView(R.id.et_companyContract)
    EditText etCompanyContract;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    private RegisterTabPresenter baseInfoPresenter;

    @BindView(R.id.unitCode)
    EditText unitCode;
    @BindView(R.id.unitName)
    TextView unitName;

    @BindView(R.id.agreement)
    SmoothClickCheckBox agreement;

    //货主板所有
    @BindView(R.id.rlyt_cargo_busi_license)
    ViewGroup layoutCargoBusiLicense;
    @BindView(R.id.identify_num)
    EditText identifyNum;
    @BindView(R.id.identify_photo)
    ImageView identifyPhoto;
    private RegisterPhoto registerBusiLicensePhoto;
    private Agreement mAgreement;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_company_register;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this, getView());
        btnNext.setEnabled(false);
        if (AppTypeConfig.isCargo()) {
            BorderLinearLayout borderLayoutAddress = findViewById(R.id.llyt_address);
            borderLayoutAddress.getBorderHelper().setLineBottomLeft(LocalDisplay.dp2px(15));
            TextView tvMemberTip = findViewById(R.id.tv_memberTip);
            tvMemberTip.setVisibility(View.VISIBLE);
            layoutCargoBusiLicense.setVisibility(View.VISIBLE);
            registerBusiLicensePhoto = new RegisterPhoto(new String[]{""}, new int[]{R.drawable.src_ship_license_first}, 1.33f);
            tvTopTip.setText(R.string.registerAd_cargo);
        }
        if (AppTypeConfig.isTruck()) {
            findViewById(R.id.tv_truckWarm).setVisibility(View.VISIBLE);
        }

        //初始化协议以及点击事件
        //针对车船货做不同初始化
        if (AppTypeConfig.isCargo()) {
            mAgreement = new CargoAgreement();
        } else if (AppTypeConfig.isShip()) {
            mAgreement = new CompanyShipAgreement();
        } else if (AppTypeConfig.isTruck()) {
            mAgreement = new CompanyTruckAgreement();
        }
        new ContractPack(this, true, agreement, btnNext, mAgreement).setOnSpanClick(new SmoothClickCheckBox.OnSpanClickListener() {
            @Override
            public void onSpanClick(int index) {
                mAgreement.fillData(getView());
            }
        });

        new GetMemberUnit(unitCode, unitName, hashCode()).startListener();

    }

    @OnClick({R.id.btn_next, R.id.tv_address, R.id.identify_photo})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v == btnNext) {
            if (baseInfoPresenter == null) {
                baseInfoPresenter = new RegisterTabPresenter(this);
            }
            baseInfoPresenter.submitCompanyInfo();
        } else if (v == tvAddress) {
            Intent intent = new Intent(getContext(), AddressSelectorActivity.class);
            intent.putExtra("title", "选择公司地址");
            startActivityForResult(intent, Constant.SELECT_ADDRESS_REQUEST);
        } else if (v == identifyPhoto) {
            if (registerBusiLicensePhoto != null && registerBusiLicensePhoto.getPhotos()[0] != null) {
                toUpLoadPhotoController();
                return;
            }
            new BottomOptionDialog.Builder(getContext())
                    .optionTexts(getString(R.string.busi_license_new), getString(R.string.busi_license_old))
                    .tipText(getString(R.string.busi_license_tip))
                    .onSelectListener(new SimpleSelectListener() {
                        @Override
                        public void onOptionSelect(View view, int position) {
                            if (position == 0) {
                                mLicenseType = NEW_LICENSE;
                                registerBusiLicensePhoto = new RegisterPhoto(new String[]{"请上传三证合一营业执照照片"}, new int[]{R.drawable.src_busi_license_new}, 1.33f);
                            } else if (position == 1) {
                                mLicenseType = OLD_LICENSE;
                                registerBusiLicensePhoto = new RegisterPhoto(new String[]{"请上传旧版营业执照照片"}, new int[]{R.drawable.src_busi_license_old}, 0.87f);
                            }
                            toUpLoadPhotoController();
                        }
                    }).show();
        }
    }

    private void toUpLoadPhotoController() {
        Intent intent = new Intent(getContext(), UploadPhotoActivity.class);
        intent.putExtra(RegisterPhoto.class.getSimpleName(), registerBusiLicensePhoto);
        startActivityForResult(intent, 20);
    }

    @NonNull
    @Override
    public RegisterInfo getRegisterInfo() {
        String companyName = etCompanyName.getText().toString();
        String loginName = etLoginName.getText().toString();
        String companyContract = etCompanyContract.getText().toString();
        RegisterCompanyInfo registerInfo = new RegisterCompanyInfo();

        registerInfo.setUnitCode(unitCode.getText().toString());

        registerInfo.setMember_name(loginName);
        registerInfo.setBiz_contact(companyContract);
        registerInfo.setCompany_name(companyName);
        registerInfo.setRegisterUrl(Constant.getUrl(Constant.COMPANY_REGISTER));
        if (AppTypeConfig.isCargo()) {
            registerInfo.setOrganiz_code(identifyNum.getText().toString());
            List<PaperTable> paperTables = new ArrayList<>();
            if (registerBusiLicensePhoto.getFileGroupId() != null) {
                PaperTable table = new PaperTable(String.valueOf(mLicenseType), registerBusiLicensePhoto.getFileGroupId());
                paperTables.add(table);
            }
            if (!ParamUtil.isEmpty(paperTables)) {
                registerInfo.setId_type(String.valueOf(mLicenseType));
                registerInfo.setPaper_table(paperTables);
            }
        }
        if (mAddress != null) {
            registerInfo.setProvince(mAddress.getProvinceForQuery());
            registerInfo.setCity(mAddress.getCityForQuery());
            registerInfo.setCountry(mAddress.getCountyForQuery());
        }
        return registerInfo;
    }

    private Address mAddress;
    private static final int NEW_LICENSE = 6;
    private static final int OLD_LICENSE = 2;
    private int mLicenseType;

    @Override
    public void onSuccess(RegisterInfo registerInfo, String message) {
        showMessage(message);
        Bundle bundle = new Bundle();
        bundle.putSerializable(RegisterInfo.class.getSimpleName(), registerInfo);
        toActivity(SetPasswordActivity.class, bundle, null);
    }

    @Override
    public void onActivityResultOk(int requestCode, Intent data) {
        super.onActivityResultOk(requestCode, data);
        if (requestCode == Constant.SELECT_ADDRESS_REQUEST) {
            mAddress = Address.getAddress(data);
            tvAddress.setText(mAddress.buildAddress());
        } else if (requestCode == 20) {
            registerBusiLicensePhoto = (RegisterPhoto) data.getSerializableExtra(RegisterPhoto.class.getSimpleName());
            Glide.with(this).load(registerBusiLicensePhoto.getPhotos()[0]).fitCenter().into(identifyPhoto);
        }
    }
}
