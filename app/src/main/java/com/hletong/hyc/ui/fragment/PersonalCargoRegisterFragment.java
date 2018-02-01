package com.hletong.hyc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.RegisterContract;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.CargoAgreement;
import com.hletong.hyc.model.PaperTable;
import com.hletong.hyc.model.RegisterPhoto;
import com.hletong.hyc.model.validate.RegisterInfo;
import com.hletong.hyc.model.validate.cargo.RegisterPersonalCargoInfo;
import com.hletong.hyc.presenter.RegisterTabPresenter;
import com.hletong.hyc.ui.activity.AddressSelectorActivity;
import com.hletong.hyc.ui.activity.SetPasswordActivity;
import com.hletong.hyc.ui.activity.UploadPhotoActivity;
import com.hletong.hyc.ui.widget.SmoothClickCheckBox;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.ContractPack;
import com.hletong.hyc.util.GetMemberUnit;
import com.hletong.mob.base.BaseFragment;
import com.hletong.mob.util.StringUtil;
import com.xcheng.okhttp.util.ParamUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 个人注册
 * Created by cc on 2016/10/13.
 */
public class PersonalCargoRegisterFragment extends BaseFragment implements RegisterContract.IBaseInfoView {
    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.et_xinming)
    EditText etXinming;
    @BindView(R.id.et_loginname)
    EditText etLoginName;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.identify_photo)
    ImageView identifyPhoto;
    @BindView(R.id.identify_num)
    TextView tvIdentifyNum;

    @BindView(R.id.unitCode)
    EditText unitCode;
    @BindView(R.id.unitName)
    TextView unitName;

    @BindView(R.id.agreement)
    SmoothClickCheckBox agreement;

    private RegisterTabPresenter baseInfoPresenter;
    private RegisterPersonalCargoInfo personalRegisterInfo;

    private RegisterPhoto registerIdentifyPhoto;
    private CargoAgreement mAgreement;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_personal_cargo_register;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this, getView());
        btnNext.setEnabled(false);
        registerIdentifyPhoto = new RegisterPhoto(new String[]{getString(R.string.upload_front_photo), getString(R.string.upload_contrary_photo)}, new int[]{R.drawable.src_identify_front, R.drawable.src_identify_back}, 0.62f);
        personalRegisterInfo = new RegisterPersonalCargoInfo();

        mAgreement = new CargoAgreement();
        //初始化协议以及点击事件
        new ContractPack(this, false, agreement, btnNext, mAgreement).setOnSpanClick(new SmoothClickCheckBox.OnSpanClickListener() {
            @Override
            public void onSpanClick(int index) {
                mAgreement.fillData(getView());

            }
        });
        new GetMemberUnit(unitCode, unitName, hashCode()).startListener();
    }

    @NonNull
    @Override
    public RegisterInfo getRegisterInfo() {
        String xinming = etXinming.getText().toString();
        personalRegisterInfo.setPersonal_name(xinming);
        personalRegisterInfo.setUnitCode(unitCode.getText().toString());

        String loginName = etLoginName.getText().toString();
        String identifyNum = tvIdentifyNum.getText().toString();
        personalRegisterInfo.setMember_name(loginName);
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
        if (!ParamUtil.isEmpty(paperTables)) {
            personalRegisterInfo.setPaper_table(paperTables);
        }
        return personalRegisterInfo;
    }

    @OnClick({R.id.btn_next, R.id.tv_address, R.id.identify_photo})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (baseInfoPresenter == null) {
                    baseInfoPresenter = new RegisterTabPresenter(this);
                }
                baseInfoPresenter.commit(getRegisterInfo());
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
        }
    }

    @Override
    public void onSuccess(RegisterInfo registerInfo, String message) {
        showMessage(message);
        Bundle bundle = new Bundle();
        bundle.putSerializable(RegisterInfo.class.getSimpleName(), registerInfo);
        toActivity(SetPasswordActivity.class, bundle, null);
    }

    private static final int TYPE_IDENTIFY = 8;

    @Override
    public void onActivityResultOk(int requestCode, Intent data) {
        super.onActivityResultOk(requestCode, data);
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
        }
    }
}
