package com.hletong.hyc.ui.activity.truck;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.CertInfoContract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.BankCard;
import com.hletong.hyc.model.CertInfo;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.PaperPhoto;
import com.hletong.hyc.model.PersonalTruckAgreement;
import com.hletong.hyc.model.TruckCompleteInfo;
import com.hletong.hyc.presenter.CertInfoPresenter;
import com.hletong.hyc.ui.activity.AddressSelectorActivity;
import com.hletong.hyc.ui.activity.BankCardAddActivity;
import com.hletong.hyc.ui.activity.PapersAddActivity;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.ui.widget.SmoothClickCheckBox;
import com.hletong.hyc.util.CertParamHelper;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.ContractPack;
import com.hletong.hyc.util.OtherInfoHelper;
import com.hletong.hyc.util.PapersHelper;
import com.hletong.hyc.util.RegexUtil;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.util.AppManager;
import com.hletong.mob.util.StringUtil;
import com.xcheng.okhttp.util.ParamUtil;
import com.xcheng.view.util.JumpUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 车主版个人信息补全
 * Created by chengxin on 2017/6/12.
 */
public class TruckPersonalChildCertActivity extends BaseActivity implements CertInfoContract.View {
    @BindView(R.id.cv_carNumber)
    CommonInputView cv_carNumber;

    @BindView(R.id.cv_carOwner)
    CommonInputView cv_carOwner;

    @BindView(R.id.cv_memberPhoneNumber)
    CommonInputView cv_memberPhoneNumber;

    @BindView(R.id.cv_identifyNumber)
    CommonInputView cv_identifyNumber;

    @BindView(R.id.cv_dlysxkz)
    CommonInputView cv_dlysxkz;

    @BindView(R.id.cv_address)
    CommonInputView cvAddress;
    //证件资料
    @BindView(R.id.cv_papers)
    CommonInputView cvPapers;

    @BindView(R.id.cv_bankInfo)
    CommonInputView cvBankInfo;
    private OtherInfoHelper mOtherInfoHelper;

    @BindView(R.id.cv_truckInfo)
    CommonInputView cvTruckInfo;
    static final int TRUCKINFO = 11;
    private TruckCompleteInfo mTruckInfo;

    @BindView(R.id.btn_commit)
    Button btnCommit;

    @BindView(R.id.agreement)
    SmoothClickCheckBox agreement;

    //公司车辆
    @BindView(R.id.cb_company_truck)
    SmoothClickCheckBox cbCompanyTruck;

    private PersonalTruckAgreement mAgreement;

    //    @BindView(R.id.cb_company_truck)
//    CheckBox cbCompanyTruck;
//    //车牌号
//    @BindView(R.id.checkboxAgree)
//    CheckBox checkboxAgree;
    private Address mAddress;
    private ArrayList<PaperPhoto> mPaperPhotos = PapersHelper.getTruckPersonalChild();
    private ArrayList<BankCard> mBankCards;
    private CertInfoPresenter presenter;

    private Map<String, Object> getParams() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("member_code", LoginInfo.getLoginInfo().getUser_code());
        params.put("settled_status", cbCompanyTruck.getTag());
        params.put("initiation_agree", 1);
        params.put("update_from", mIsOwnCert ? "2" : "1");

        Map<String, Object> member_data = new LinkedHashMap<>();
        member_data.put("member_name", LoginInfo.getLoginInfo().getUser_name());
        member_data.put("member_address_province", mAddress != null ? mAddress.getRealProvince() : "");
        member_data.put("member_address_city", mAddress != null ? mAddress.getRealCity() : "");
        member_data.put("member_address_area", mAddress != null ? mAddress.getRealCounty() : "");
        member_data.put("member_address", memberAddress != null ? memberAddress : "");
        member_data.put("road_transport_permit_no", cv_dlysxkz.getInputValue());
        member_data.putAll(mOtherInfoHelper.getParams());

        params.put("member_data", CertParamHelper.getMapJson(member_data));

        Map<String, Object> truck_data = new LinkedHashMap<>();
        truck_data.put("plate", cv_carNumber.getInputValue());
        truck_data.put("truck_name", cv_carOwner.getInputValue());
        truck_data.put("dentity_no", cv_identifyNumber.getInputValue());
        truck_data.put("member_tel", cv_memberPhoneNumber.getInputValue());
        if (mTruckInfo != null) {
            truck_data.put("truck_type", DictionaryItem.getDictId(mTruckInfo.getTruck_type()));
            truck_data.put("license_plate_color", DictionaryItem.getDictId(mTruckInfo.getTruck_color()));
            truck_data.put("gender", DictionaryItem.getDictId(mTruckInfo.getSexType()));
            truck_data.put("road_transport_certificate", mTruckInfo.getDlysNumber());
            truck_data.put("qualification_certificate", mTruckInfo.getCyzgzNumber());
            truck_data.put("max_length", mTruckInfo.getTruckLength());
            truck_data.put("max_heavy", mTruckInfo.getLoadTon());
            truck_data.put("vehicle_mass", mTruckInfo.getVehicleMass());
            truck_data.put("loaded_vehicle_quality", mTruckInfo.getLoadedVehicleQuality());
            truck_data.put("identify", mTruckInfo.getSbdhNumber());
        }
        params.put("truck_data", CertParamHelper.getMapJson(truck_data));

        if (mPaperPhotos != null) {
            params.put("truck_paper", CertParamHelper.getPaperjsonArray(mPaperPhotos));

        }
        if (mBankCards != null) {
            params.put("truck_bank", CertParamHelper.getBankListjsonArray(mBankCards));
        }
        return params;
    }

    private boolean validate() {
        if (mIsOwnCert) {
            return true;
        }
        String plate = cv_carNumber.getInputValue();
        if (StringUtil.isTrimBlank(plate)) {
            showMessage("车牌号不能为空");
            return false;
        } else if (RegexUtil.containEmpty(plate)) {
            showMessage("车牌号不能包含空格");
            return false;
        } else if (!RegexUtil.isCarNumber(plate)) {
            showMessage("车牌号格式不正确");
            return false;
        } else if (StringUtil.isTrimBlank(cv_carOwner.getInputValue())) {
            showMessage("请输入车辆所有人");
            return false;
        } else if (StringUtil.isTrimBlank(cv_memberPhoneNumber.getInputValue())) {
            showMessage("请输入会员手机号");
            return false;
        } else if (StringUtil.isTrimBlank(cv_identifyNumber.getInputValue())) {
            showMessage("请输入身份证号码");
            return false;
        } else if (StringUtil.isTrimBlank(cv_dlysxkz.getInputValue())) {
            showMessage("请输入道路运输许可证");
            return false;
        } else if (mAddress == null) {
            showMessage("请选择公司住址");
            return false;
        } else if (!Validator.isNotNull(mOtherInfoHelper.getOtherInfo(), this, "请填写其他信息")) {
            return false;
        }
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_truck_personal_child_certificate;
    }

    //是否主动补充
    private boolean mIsOwnCert;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        mIsOwnCert = AppTypeConfig.removeMustInput(this);
        //自主认证直接不显示
        if (mIsOwnCert){
            cbCompanyTruck.setVisibility(View.GONE);
        }
        btnCommit.setEnabled(false);
        mOtherInfoHelper = new OtherInfoHelper(this, (CommonInputView) findViewById(R.id.cv_otherInfos));

        presenter = new CertInfoPresenter(this);
        presenter.getCertInfo();
        cbCompanyTruck.setTag(0);
        cbCompanyTruck.setCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cbCompanyTruck.setTag(isChecked ? "1" : "0");
            }
        });
        //初始化协议以及点击事件
        mAgreement = new PersonalTruckAgreement();
        new ContractPack(this, false, agreement, btnCommit, mAgreement).setOnSpanClick(new SmoothClickCheckBox.OnSpanClickListener() {
            @Override
            public void onSpanClick(int index) {
                if (mTruckInfo != null) {
                    mAgreement.vehicleType = mTruckInfo.getTruck_type().getText();
                    mAgreement.vehicleLength = mTruckInfo.getTruckLength();
                    mAgreement.vehicleMaxLoad = mTruckInfo.getLoadTon();
                }
                mAgreement.fillData(getWindow().getDecorView());
                mAgreement.mPhone =
                        mAgreement.mUserName = LoginInfo.getLoginInfo().getUser_name();
            }
        });
    }

    private String memberAddress;

    @Override
    public void initCertInfo(CertInfo info) {
        mOtherInfoHelper.setOtherInfo(info.getOtherInfo(true));

        if (info.hasProtocol()) {
            agreement.setVisibility(View.GONE);
            btnCommit.setEnabled(true);
        }

        CertInfo.MemberDtoBean memberDto = info.getNMemberDto();
        if (memberDto != null) {
            cv_carOwner.setText(memberDto.getPersonalName());
            cv_identifyNumber.setText(memberDto.getPersonalIdentity());
            cv_dlysxkz.setText(memberDto.getRoadTransportPermitNo());
        }

        CertInfo.MmTruckResponseDtoBean truckDto = info.getMmTruckResponseDto();
        if (truckDto == null) {
            return;
        }

        mBankCards = CertInfo.getBankList(truckDto.getBankList());
        if (mBankCards != null) {
            cvBankInfo.setText("已填写");
        }
        if (!ParamUtil.isEmpty(truckDto.getPaperList())) {
            CertInfo.filterPaperPhoto(truckDto.getPaperList(), mPaperPhotos);
            cvPapers.setText("已填写");
        }
        mTruckInfo = truckDto.getTruckInfo();
        if (mTruckInfo != null) {
            cvTruckInfo.setText("已填写");
        }
        CertInfo.MmTruckResponseDtoBean.MmTruckDtoBean truckDtoInfo = truckDto.getMmTruckDto();
        if (truckDtoInfo == null) {
            return;
        }
        cv_identifyNumber.setText(truckDtoInfo.getDentityNo());
        cv_carOwner.setText(truckDtoInfo.getTruckName());
        cv_carNumber.setText(truckDtoInfo.getPlate());
        cv_carNumber.getInput().setEnabled(false);
        cv_memberPhoneNumber.setText(truckDtoInfo.getMemberTel());
        memberAddress = truckDtoInfo.getMemberAddress();
        mAddress = new Address(truckDtoInfo.getMemberAddressProvince(), truckDtoInfo.getMemberAddressCity(), truckDtoInfo.getMemberAddressArea(), "");
        cvAddress.setText(mAddress.buildAddress());
    }

    @Override
    public void certSuccess(String message) {
        showMessage(message);
        AppManager.removeCookie();
        finish();
    }

    @OnClick({R.id.cv_address, R.id.cv_truckInfo, R.id.cv_papers, R.id.cv_bankInfo, R.id.btn_commit, R.id.cv_otherInfos})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.cv_address) {
            toActivity(AddressSelectorActivity.class, JumpUtil.getBundle("title", "选择地址"), Constant.SELECT_ADDRESS_REQUEST, null);
        } else if (v.getId() == R.id.cv_truckInfo) {
            Bundle bundle = JumpUtil.getBundle(TruckCompleteInfo.class.getSimpleName(), mTruckInfo);
            toActivity(TruckInfoAddActivity.class, bundle, TRUCKINFO, null);
        } else if (v.getId() == R.id.cv_papers) {
            toActivity(PapersAddActivity.class, JumpUtil.getBundle(PaperPhoto.class.getSimpleName(), mPaperPhotos), Constant.PAPER_REQUEST, null);
        } else if (v.getId() == R.id.cv_bankInfo) {
            toActivity(BankCardAddActivity.class, JumpUtil.getBundle(BankCard.class.getSimpleName(), mBankCards), Constant.BANKCARD_REQUEST, null);
        } else if (v.getId() == R.id.btn_commit) {
            if (validate()) {
                presenter.summitTruckPerson(getParams());
            }
        } else if (v.getId() == R.id.cv_otherInfos) {
            mOtherInfoHelper.toOtherInfoView();
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

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        mOtherInfoHelper.receiveData(requestCode, data);
        if (requestCode == Constant.SELECT_ADDRESS_REQUEST) {
            mAddress = Address.getAddress(data);
            cvAddress.setText(mAddress.buildAddress());
        } else if (requestCode == TRUCKINFO) {
            mTruckInfo = (TruckCompleteInfo) data.getSerializableExtra(TruckCompleteInfo.class.getSimpleName());
            cvTruckInfo.setText("已填写");
        } else if (requestCode == Constant.PAPER_REQUEST) {
            mPaperPhotos = (ArrayList<PaperPhoto>) data.getSerializableExtra(PaperPhoto.class.getSimpleName());
            cvPapers.setText("已填写");
        } else if (requestCode == Constant.BANKCARD_REQUEST) {
            mBankCards = (ArrayList<BankCard>) data.getSerializableExtra(BankCard.class.getSimpleName());
            cvBankInfo.setText("已填写");
        }
    }
}
