package com.hletong.hyc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.RegisterContract;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.PaperTable;
import com.hletong.hyc.model.PersonalShipAgreement;
import com.hletong.hyc.model.RegisterPhoto;
import com.hletong.hyc.model.validate.RegisterInfo;
import com.hletong.hyc.model.validate.ship.RegisterPersonalShipInfo;
import com.hletong.hyc.presenter.RegisterTabPresenter;
import com.hletong.hyc.ui.activity.AddressSelectorActivity;
import com.hletong.hyc.ui.activity.SetPasswordActivity;
import com.hletong.hyc.ui.activity.UploadPhotoActivity;
import com.hletong.hyc.ui.activity.ship.RegisterShipInfoActivity;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.ui.widget.SmoothClickCheckBox;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.ContractPack;
import com.hletong.hyc.util.GetMemberUnit;
import com.hletong.hyc.util.OtherInfoHelper;
import com.hletong.mob.base.BaseFragment;
import com.hletong.mob.util.StringUtil;
import com.xcheng.okhttp.util.ParamUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hletong.hyc.model.validate.ship.RegisterPersonalShipInfo.ShipInfo;

/**
 * 个人注册
 * Created by cc on 2016/10/13.
 */
public class PersonalShipRegisterFragment extends BaseFragment implements RegisterContract.IBaseInfoView {
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.et_shipNumber)
    EditText etShipNumber;
    @BindView(R.id.et_loginname)
    EditText etLoginName;
    @BindView(R.id.et_shipOwner)
    EditText etShipOwner;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.identify_photo)
    ImageView identifyPhoto;
    @BindView(R.id.ship_license_photo)
    ImageView licensePhoto;
    @BindView(R.id.jumpShipInfo)
    TextView tvJumpShipInfo;
    @BindView(R.id.identify_num)
    TextView tvIdentifyNum;
    @BindView(R.id.ship_license_num)
    TextView tvLicenseNum;

    @BindView(R.id.unitCode)
    EditText unitCode;
    @BindView(R.id.unitName)
    TextView unitName;

    private OtherInfoHelper mOtherInfoHelper;

    @BindView(R.id.agreement)
    SmoothClickCheckBox agreement;

    //公司船舶
    @BindView(R.id.cb_company_ship)
    SmoothClickCheckBox cbCompanyShip;

    private RegisterTabPresenter baseInfoPresenter;
    private PersonalShipAgreement mAgreement;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_personal_ship_register;
    }

    private RegisterPersonalShipInfo personalRegisterInfo;

    private RegisterPhoto registerIdentifyPhoto;
    private RegisterPhoto registerLicensePhoto;
    private ShipInfo mShipInfo;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this, getView());
        btnNext.setEnabled(false);
        mOtherInfoHelper = new OtherInfoHelper(this, (CommonInputView) findViewById(R.id.cv_otherInfos));

        registerIdentifyPhoto = new RegisterPhoto(new String[]{getString(R.string.upload_front_photo), getString(R.string.upload_contrary_photo)}, new int[]{R.drawable.src_identify_front, R.drawable.src_identify_back}, 0.62f);
        registerLicensePhoto = new RegisterPhoto(new String[]{"请上传船舶国籍证书封面照片", "请上传船舶国籍证书第一页照片"}, new int[]{R.drawable.src_ship_license_cover, R.drawable.src_ship_license_first}, 1.33f);
        personalRegisterInfo = new RegisterPersonalShipInfo();

        //初始化协议以及点击事件
        mAgreement = new PersonalShipAgreement();
        new ContractPack(this, false, agreement, btnNext, mAgreement).setOnSpanClick(new SmoothClickCheckBox.OnSpanClickListener() {
            @Override
            public void onSpanClick(int index) {
                mAgreement.fillData(getView());
                if (mShipInfo != null) {
                    mAgreement.shipType = mShipInfo.getShip_type().getText();
                    mAgreement.shipWeight = mShipInfo.getNt_ton();
                    mAgreement.shipWidth = mShipInfo.getShip_width();
                    mAgreement.shipDepth = mShipInfo.getDeep();
                    mAgreement.shipCarry = mShipInfo.getLoad_ton();
                    mAgreement.shipMaxDepth = mShipInfo.getFull_draft();
                }
            }
        });

        cbCompanyShip.setCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                personalRegisterInfo.setSettled_status(isChecked ? "1" : "0");
            }
        });
        new GetMemberUnit(unitCode, unitName, hashCode()).startListener();

    }

    @NonNull
    @Override
    public RegisterInfo getRegisterInfo() {
        String shipNumber = etShipNumber.getText().toString();

        String loginName = etLoginName.getText().toString();
        String shipOwner = etShipOwner.getText().toString();
        String identifyNum = tvIdentifyNum.getText().toString();
        String licenseNum = tvLicenseNum.getText().toString();

        personalRegisterInfo.setUnitCode(unitCode.getText().toString());

        personalRegisterInfo.setMember_name(loginName);
        personalRegisterInfo.setShip(shipNumber);
        personalRegisterInfo.setPersonal_name(shipOwner);
        personalRegisterInfo.setRegisterUrl(Constant.getUrl(Constant.PERSONAL_REGISTER));
        if (!StringUtil.isTrimBlank(identifyNum)) {
            personalRegisterInfo.setPersonal_identity(identifyNum);
        } else {
            personalRegisterInfo.setPersonal_identity(null);
        }
        if (!StringUtil.isTrimBlank(licenseNum)) {
            personalRegisterInfo.setNationality_cert(licenseNum);
        } else {
            personalRegisterInfo.setNationality_cert(null);
        }
        List<PaperTable> paperTables = new ArrayList<>();
        if (registerIdentifyPhoto.getFileGroupId() != null) {
            PaperTable table = new PaperTable(String.valueOf(TYPE_IDENTIFY), registerIdentifyPhoto.getFileGroupId());
            paperTables.add(table);
        }
        if (registerIdentifyPhoto.getFileGroupId() != null) {
            PaperTable table = new PaperTable(String.valueOf(TYPE_LICENSE), registerLicensePhoto.getFileGroupId());
            paperTables.add(table);
        }
        if (!ParamUtil.isEmpty(paperTables)) {
            personalRegisterInfo.setPaper_table(paperTables);
        }
        if (mShipInfo != null) {
            personalRegisterInfo.setShip_type(mShipInfo.getShip_type().getId());
            personalRegisterInfo.setShip_width(mShipInfo.getShip_width());
            personalRegisterInfo.setShip_length(mShipInfo.getShip_width());
            personalRegisterInfo.setDeep(mShipInfo.getDeep());
            personalRegisterInfo.setFull_draft(mShipInfo.getFull_draft());
            personalRegisterInfo.setLoad_ton(mShipInfo.getLoad_ton());
        }
        personalRegisterInfo.initOtherInfo(mOtherInfoHelper.getOtherInfo());
        return personalRegisterInfo;
    }

    @OnClick({R.id.btn_next, R.id.tv_address, R.id.identify_photo, R.id.ship_license_photo, R.id.jumpShipInfo, R.id.cv_otherInfos})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (baseInfoPresenter == null) {
                    baseInfoPresenter = new RegisterTabPresenter(this);
                }
                baseInfoPresenter.submitPersonInfo();
                break;
            case R.id.tv_address: {
                Intent intent = new Intent(getContext(), AddressSelectorActivity.class);
                intent.putExtra("title", "选择住址");
                startActivityForResult(intent, Constant.SELECT_ADDRESS_REQUEST);
            }
            break;
            //身份证拍照
            case R.id.identify_photo: {
                Intent intent = new Intent(getContext(), UploadPhotoActivity.class);
                intent.putExtra(RegisterPhoto.class.getSimpleName(), registerIdentifyPhoto);
                startActivityForResult(intent, 10);
            }
            break;
            //拍照行驶证
            case R.id.ship_license_photo: {
                Intent intent = new Intent(getContext(), UploadPhotoActivity.class);
                intent.putExtra(RegisterPhoto.class.getSimpleName(), registerLicensePhoto);
                startActivityForResult(intent, 20);
            }
            break;
            //船舶信息
            case R.id.jumpShipInfo: {
                Intent intent = new Intent(getContext(), RegisterShipInfoActivity.class);
                intent.putExtra(ShipInfo.class.getSimpleName(), mShipInfo);
                startActivityForResult(intent, 30);
            }
            break;
            //其他信息
            case R.id.cv_otherInfos: {
                mOtherInfoHelper.toOtherInfoView();
            }
        }
    }

    @Override
    public void onSuccess(RegisterInfo registerInfo, String message) {
        showMessage(message);
        Bundle bundle = new Bundle();
        bundle.putSerializable(RegisterInfo.class.getSimpleName(), registerInfo);
        toActivity(SetPasswordActivity.class, bundle, null);
    }

    private static final int TYPE_IDENTIFY = 23;
    private static final int TYPE_LICENSE = 25;

    @Override
    public void onActivityResultOk(int requestCode, Intent data) {
        mOtherInfoHelper.receiveData(requestCode, data);
        switch (requestCode) {
            case Constant.SELECT_ADDRESS_REQUEST:
                Address address = Address.getAddress(data);
                personalRegisterInfo.setProvince(address.getProvinceForQuery());
                personalRegisterInfo.setCity(address.getCityForQuery());
                personalRegisterInfo.setCountry(address.getCountyForQuery());
                tvAddress.setText(address.buildAddress());
                break;
            case 10:
                registerIdentifyPhoto = (RegisterPhoto) data.getSerializableExtra(RegisterPhoto.class.getSimpleName());
                Glide.with(this).load(registerIdentifyPhoto.getPhotos()[0]).fitCenter().into(identifyPhoto);
                break;
            case 20:
                registerLicensePhoto = (RegisterPhoto) data.getSerializableExtra(RegisterPhoto.class.getSimpleName());
                Glide.with(this).load(registerLicensePhoto.getPhotos()[0]).fitCenter().into(licensePhoto);
                break;
            case 30:
                mShipInfo = (ShipInfo) data.getSerializableExtra(ShipInfo.class.getSimpleName());
                tvJumpShipInfo.setText("已选择");
                break;
        }
    }
}
