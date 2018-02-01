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
import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.RegisterContract;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.PaperTable;
import com.hletong.hyc.model.PersonalTruckAgreement;
import com.hletong.hyc.model.RegisterPhoto;
import com.hletong.hyc.model.validate.RegisterInfo;
import com.hletong.hyc.model.validate.truck.RegisterPersonalTruckInfo;
import com.hletong.hyc.presenter.RegisterTabPresenter;
import com.hletong.hyc.ui.activity.AddressSelectorActivity;
import com.hletong.hyc.ui.activity.SetPasswordActivity;
import com.hletong.hyc.ui.activity.UploadPhotoActivity;
import com.hletong.hyc.ui.activity.truck.RegisterTruckInfoActivity;
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

import static com.hletong.hyc.model.validate.truck.RegisterPersonalTruckInfo.TruckInfo;

/**
 * 个人注册
 * Created by cc on 2016/10/13.
 */
public class PersonalTruckRegisterFragment extends BaseFragment implements RegisterContract.IBaseInfoView {
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.et_carNumber)
    EditText etCarNumber;
    @BindView(R.id.et_loginname)
    EditText etLoginName;
    @BindView(R.id.et_carContact)
    EditText etCarContact;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.identify_photo)
    ImageView identifyPhoto;
    @BindView(R.id.driving_license_photo)
    ImageView licensePhoto;
    @BindView(R.id.jumpTruckInfo)
    TextView tvVehicleInfo;
    @BindView(R.id.identify_num)
    TextView tvIdentifyNum;

    @BindView(R.id.unitCode)
    EditText unitCode;
    @BindView(R.id.unitName)
    TextView unitName;

    private OtherInfoHelper mOtherInfoHelper;


    //入会协议
//    @BindView(R.id.checkboxAgree)
//    CheckBox checkboxAgree;
    @BindView(R.id.agreement)
    SmoothClickCheckBox agreement;

    //公司车辆
    @BindView(R.id.cb_company_truck)
    SmoothClickCheckBox cbCompanyTruck;

    private RegisterTabPresenter baseInfoPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_personal_truck_register;
    }

    private RegisterPersonalTruckInfo personalRegisterInfo;

    private RegisterPhoto registerIdentifyPhoto;
    private RegisterPhoto registerLicensePhoto;
    private TruckInfo mTruckInfo;
    private PersonalTruckAgreement mAgreement;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this, getView());
        btnNext.setEnabled(false);
        mOtherInfoHelper = new OtherInfoHelper(this, (CommonInputView) findViewById(R.id.cv_otherInfos));

        registerIdentifyPhoto = new RegisterPhoto(new String[]{getString(R.string.upload_front_photo), getString(R.string.upload_contrary_photo)}, new int[]{R.drawable.src_identify_front, R.drawable.src_identify_back}, 0.62f);
        registerLicensePhoto = new RegisterPhoto(new String[]{getString(R.string.driving_license_info)}, new int[]{R.drawable.src_driver_license}, 0.75f);
        personalRegisterInfo = new RegisterPersonalTruckInfo();

        //初始化协议以及点击事件
        mAgreement = new PersonalTruckAgreement();
        new ContractPack(this, false, agreement, btnNext, mAgreement).setOnSpanClick(new SmoothClickCheckBox.OnSpanClickListener() {
            @Override
            public void onSpanClick(int index) {
                if (mTruckInfo != null) {
                    mAgreement.vehicleType = mTruckInfo.getTruck_type().getText();
                    mAgreement.vehicleHeight = mTruckInfo.getMax_height();
                    mAgreement.vehicleLength = mTruckInfo.getMax_length();
                    mAgreement.vehicleWidth = mTruckInfo.getMax_width();
                    mAgreement.vehicleMaxLoad = mTruckInfo.getMax_heavy();
                }
                mAgreement.fillData(getView());
            }
        });

        cbCompanyTruck.setCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
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
        String carNumber = etCarNumber.getText().toString();
        if (BuildConfig.DEBUG && StringUtil.isTrimBlank(carNumber)) {
            etCarNumber.setText("浙A1231D");
        }
        String loginName = etLoginName.getText().toString();
        String carContact = etCarContact.getText().toString();
        String identifyNum = tvIdentifyNum.getText().toString();
        personalRegisterInfo.setMember_name(loginName);
        personalRegisterInfo.setUnitCode(unitCode.getText().toString());
        personalRegisterInfo.setPlate(carNumber);
        personalRegisterInfo.setPersonal_name(carContact);
        personalRegisterInfo.setRegisterUrl(Constant.getUrl(Constant.PERSONAL_REGISTER));
        if (!StringUtil.isTrimBlank(identifyNum)) {
            personalRegisterInfo.setPersonal_identity(identifyNum);
        } else {
            personalRegisterInfo.setPersonal_identity(null);
        }

        List<PaperTable> paperTables = new ArrayList<>();
        if (registerIdentifyPhoto.getFileGroupId() != null) {
            PaperTable table = new PaperTable(String.valueOf(TYPE_IDENTIFY), registerIdentifyPhoto.getFileGroupId());
            paperTables.add(table);
        }
        if (registerLicensePhoto.getFileGroupId() != null) {
            PaperTable table = new PaperTable(String.valueOf(TYPE_LICENSE), registerLicensePhoto.getFileGroupId());
            paperTables.add(table);
        }
        if (!ParamUtil.isEmpty(paperTables)) {
            personalRegisterInfo.setPaper_table(paperTables);
        }
        if (mTruckInfo != null) {
            personalRegisterInfo.setMax_heavy(mTruckInfo.getMax_heavy());
            personalRegisterInfo.setMax_length(mTruckInfo.getMax_length());
            personalRegisterInfo.setMax_width(mTruckInfo.getMax_width());
            personalRegisterInfo.setMax_height(mTruckInfo.getMax_height());
            personalRegisterInfo.setTruck_type(mTruckInfo.getTruck_type().getId());
            personalRegisterInfo.setWheel(mTruckInfo.getTrailerAxle().getId() + "-" + mTruckInfo.getAxleCount().getId());
        }
        personalRegisterInfo.initOtherInfo(mOtherInfoHelper.getOtherInfo());

        return personalRegisterInfo;
    }

    @OnClick({R.id.btn_next, R.id.tv_address, R.id.identify_photo, R.id.driving_license_photo, R.id.jumpTruckInfo, R.id.cv_otherInfos})
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
            case R.id.driving_license_photo: {
                Intent intent = new Intent(getContext(), UploadPhotoActivity.class);
                intent.putExtra(RegisterPhoto.class.getSimpleName(), registerLicensePhoto);
                startActivityForResult(intent, 20);
            }
            break;
            //车辆信息
            case R.id.jumpTruckInfo: {
                Intent intent = new Intent(getContext(), RegisterTruckInfoActivity.class);
                intent.putExtra(TruckInfo.class.getSimpleName(), mTruckInfo);
                startActivityForResult(intent, 30);
            }
            break;
            //其他信息
            case R.id.cv_otherInfos: {
                mOtherInfoHelper.toOtherInfoView();
            }
            break;

        }
    }

    @Override
    public void onSuccess(RegisterInfo registerInfo, String message) {
        showMessage(message);
        Bundle bundle = new Bundle();
        bundle.putSerializable(RegisterInfo.class.getSimpleName(), registerInfo);
        toActivity(SetPasswordActivity.class, bundle, null);
    }

    private static final int TYPE_IDENTIFY = 13;
    private static final int TYPE_LICENSE = 14;

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
                mTruckInfo = (TruckInfo) data.getSerializableExtra(TruckInfo.class.getSimpleName());
                tvVehicleInfo.setText("已选择");
                break;
        }
    }
}
