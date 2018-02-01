package com.hletong.hyc.ui.activity.cargo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.CertInfoContract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.CargoAgreement;
import com.hletong.hyc.model.CertInfo;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.PaperPhoto;
import com.hletong.hyc.model.RegisterPhoto;
import com.hletong.hyc.presenter.CertInfoPresenter;
import com.hletong.hyc.ui.activity.AddressSelectorActivity;
import com.hletong.hyc.ui.activity.UploadPhotoActivity;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.ui.widget.SmoothClickCheckBox;
import com.hletong.hyc.util.CertParamHelper;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.ContractPack;
import com.hletong.hyc.util.DatePickerUtil;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.util.AppManager;
import com.hletong.mob.util.SimpleDate;
import com.xcheng.view.EasyView;
import com.xcheng.view.util.JumpUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 货主版个人信息补全
 * Created by chengxin on 2017/6/12.
 */
public class CargoPersonalCertActivity extends BaseActivity implements CertInfoContract.View, DatePickerUtil.OnDateSetListener {
    //会员名
    @BindView(R.id.cv_memberName)
    CommonInputView cv_memberName;

    @BindView(R.id.cv_chinaName)
    CommonInputView cv_chinaName;
    //地址
    @BindView(R.id.cv_address)
    CommonInputView cvAddress;


    @BindView(R.id.cv_identifyNumber)
    CommonInputView cv_identifyNumber;

    @BindView(R.id.iv_photo)
    ImageView iv_photo;

    @BindView(R.id.tv_endDate_select)
    TextView tv_endDate_select;
    DatePickerUtil datePickerUtil;
    private CertInfoPresenter presenter;

    @BindView(R.id.btn_commit)
    Button btnCommit;
    //车牌号
    @BindView(R.id.agreement)
    SmoothClickCheckBox agreement;

    private CargoAgreement mAgreement;

    private Address mAddress;
    private PaperPhoto mIDPaperPhoto = new PaperPhoto(new RegisterPhoto(new String[]{EasyView.getString(R.string.upload_front_photo), EasyView.getString(R.string.upload_contrary_photo)}, new int[]{R.drawable.src_identify_front, R.drawable.src_identify_back}, 0.62f), "身份证", 8);

    private Map<String, Object> getParams() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("member_code", LoginInfo.getLoginInfo().getUser_code());
        params.put("initiation_agree", 1);
        params.put("update_from", mIsOwnCert ? "2" : "1");

        Map<String, Object> member_data = new LinkedHashMap<>();
        member_data.put("member_name", cv_memberName.getInputValue());
        member_data.put("personal_sname", cv_chinaName.getInputValue());
        member_data.put("member_address_province", mAddress != null ? mAddress.getRealProvince() : "");
        member_data.put("member_address_city", mAddress != null ? mAddress.getRealCity() : "");
        member_data.put("member_address_area", mAddress != null ? mAddress.getRealCounty() : "");
        member_data.put("member_address", memberAddress != null ? memberAddress : "");
        params.put("member_data", CertParamHelper.getMapJson(member_data));
        if (mIDPaperPhoto.isDataComplete()) {
            List<PaperPhoto> paperPhotos = new ArrayList<>();
            paperPhotos.add(mIDPaperPhoto);
            params.put("member_paper", CertParamHelper.getPaperjsonArray(paperPhotos));
        }
        return params;
    }

    private boolean validate() {
        if (mIsOwnCert) {
            return true;
        }
        if (!Validator.isNotNull(cv_memberName.getInputValue(), this, "会员名不能为空")) {
            return false;
        } else if (!Validator.isNotNull(cv_chinaName.getInputValue(), this, "姓名不能为空")) {
            return false;
        } else if (!Validator.isNotNull(mAddress, this, "地址不能为空")) {
            return false;
        } else if (!Validator.isNotNull(cv_identifyNumber.getInputValue(), this, "身份证不能为空")) {
            return false;
        }
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_cargo_personal_certificate;
    }

    private boolean mIsOwnCert;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        mIsOwnCert = AppTypeConfig.removeMustInput(this);

        btnCommit.setEnabled(false);
        mIDPaperPhoto = new PaperPhoto(new RegisterPhoto(new String[]{getString(R.string.upload_front_photo), getString(R.string.upload_contrary_photo)}, new int[]{R.drawable.src_identify_front, R.drawable.src_identify_back}, 0.62f), "身份证", 8);
        //初始化协议以及点击事件
        mAgreement = new CargoAgreement();
        new ContractPack(this, false, agreement, btnCommit, mAgreement).setOnSpanClick(new SmoothClickCheckBox.OnSpanClickListener() {
            @Override
            public void onSpanClick(int index) {
                mAgreement.fillData(getWindow().getDecorView());
            }
        });
        presenter = new CertInfoPresenter(this);
        presenter.getCertInfo();
    }

    @OnClick({R.id.cv_address, R.id.iv_photo, R.id.tv_endDate_select, R.id.btn_commit})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.cv_address) {
            toActivity(AddressSelectorActivity.class, JumpUtil.getBundle("title", "选择地址"), Constant.SELECT_ADDRESS_REQUEST, null);
        } else if (v.getId() == R.id.iv_photo) {
            RegisterPhoto photo = mIDPaperPhoto.getRegisterPhoto();
            JumpUtil.toActivityForResult((Activity) getContext(), UploadPhotoActivity.class, Constant.PHOTO_ADD_REQUEST,
                    JumpUtil.getBundle(RegisterPhoto.class.getSimpleName(), photo));

        } else if (v.getId() == R.id.tv_endDate_select) {
            if (datePickerUtil == null) {
                datePickerUtil = new DatePickerUtil(this, this, Calendar.getInstance());
            }
            datePickerUtil.showDatePicker(((Activity) getContext()).getFragmentManager(), 0);

        } else if (v.getId() == R.id.btn_commit) {
            if (validate()) {
                presenter.summitTruckPerson(getParams());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.phone_selector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.call) {
            CallPhoneDialog.getInstance().show(getSupportFragmentManager());
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        if (requestCode == Constant.SELECT_ADDRESS_REQUEST) {
            mAddress = Address.getAddress(data);
            cvAddress.setText(mAddress.buildAddress());
        } else if (requestCode == Constant.PHOTO_ADD_REQUEST) {
            RegisterPhoto registerPhoto = (RegisterPhoto) data.getSerializableExtra(RegisterPhoto.class.getSimpleName());
            mIDPaperPhoto.setRegisterPhoto(registerPhoto);
            Glide.with(this).load(registerPhoto.getPhotos()[0]).fitCenter().into(iv_photo);
        }
    }

    @Override
    public void onDateSet(int year, int monthOfYear, int dayOfMonth, int tag) {
        SimpleDate simpleDate = new SimpleDate();
        simpleDate.setDate(year, monthOfYear, dayOfMonth);
        mIDPaperPhoto.setDate(simpleDate.dateString(true, ""));
        tv_endDate_select.setText(SimpleDate.format(mIDPaperPhoto.getDate(), new SimpleDateFormat("yyyyMMdd", Locale.getDefault())));
    }

    private String memberAddress;

    @Override
    public void initCertInfo(CertInfo info) {
        CertInfo.Paper_ idPaper_ = CertInfo.getPaper(info, 8);
        if (idPaper_ != null) {
            mIDPaperPhoto.setDate(idPaper_.endDt);
            mIDPaperPhoto.setPaperUuid(idPaper_.paperUuid);
            RegisterPhoto registerPhoto = mIDPaperPhoto.getRegisterPhoto();
            registerPhoto.setHttp(true);
            registerPhoto.setFileGroupId(idPaper_.paperFile);
            tv_endDate_select.setText(SimpleDate.format(mIDPaperPhoto.getDate(), new SimpleDateFormat("yyyyMMdd", Locale.getDefault())));
            TextView tvPhotoTip = (TextView) findViewById(R.id.tv_tip);
            tvPhotoTip.setText("已上传");
        }
        if (info.hasProtocol()) {
            agreement.setVisibility(View.GONE);
            btnCommit.setEnabled(true);
        }
        CertInfo.MemberDtoBean dto = info.getNMemberDto();
        if (dto == null) {
            return;
        }

        cv_memberName.setText(dto.getMemberName());
        cv_memberName.getInput().setEnabled(false);
        cv_chinaName.setText(dto.getPersonalName());
        cv_identifyNumber.setText(dto.getPersonalIdentity());
        mAddress = new Address(dto.getMemberAddressProvince(), dto.getMemberAddressCity(), dto.getMemberAddressArea(), "");
        cvAddress.setText(mAddress.buildAddress());
        memberAddress = dto.getMemberAddress();
    }

    @Override
    public void certSuccess(String message) {
        showMessage(message);
        AppManager.removeCookie();
        finish();
    }
}
