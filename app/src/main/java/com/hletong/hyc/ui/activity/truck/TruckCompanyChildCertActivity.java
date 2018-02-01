package com.hletong.hyc.ui.activity.truck;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.CertInfoContract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.BankCard;
import com.hletong.hyc.model.CertInfo;
import com.hletong.hyc.model.CompanyTruckAgreement;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.PaperPhoto;
import com.hletong.hyc.model.TruckCompleteInfo;
import com.hletong.hyc.presenter.CertInfoPresenter;
import com.hletong.hyc.ui.activity.AddressSelectorActivity;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.ui.widget.SmoothClickCheckBox;
import com.hletong.hyc.util.CertParamHelper;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.ContractPack;
import com.hletong.hyc.util.OtherInfoHelper;
import com.hletong.hyc.util.PapersHelper;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.util.AppManager;
import com.hletong.mob.util.StringUtil;
import com.xcheng.view.util.JumpUtil;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 车主版个人信息补全
 * Created by chengxin on 2017/6/12.
 */
public class TruckCompanyChildCertActivity extends BaseActivity implements CertInfoContract.View {
    @BindView(R.id.cv_memberName)
    CommonInputView cv_memberName;

    @BindView(R.id.cv_memberPhoneNumber)
    CommonInputView cv_memberPhoneNumber;


    @BindView(R.id.cv_dlysxkz)
    CommonInputView cv_dlysxkz;

    @BindView(R.id.cv_address)
    CommonInputView cvAddress;
    //证件资料
    @BindView(R.id.cv_companyName)
    CommonInputView cv_companyName;
    //车辆负责人
    @BindView(R.id.cv_truckChargePerson)
    CommonInputView cv_truckChargePerson;
    //身份证号码
    @BindView(R.id.cv_identifyNumber)
    CommonInputView cv_identifyNumber;

    @BindView(R.id.cv_truckManager)
    CommonInputView cv_truckManager;
    static final int TRUCKMANAGER = 11;
    private OtherInfoHelper mOtherInfoHelper;
    @BindView(R.id.btn_commit)
    Button btnCommit;
    //车牌号
    @BindView(R.id.agreement)
    SmoothClickCheckBox agreement;
    CompanyTruckAgreement mAgreement;
    private Address mAddress;
    private CertInfoPresenter presenter;
    private HashMap<String, Object> truckMgData = new HashMap<>();
    public static final String KEY_TRUCK_MANAGER = "key_truckManager";

    private boolean validate() {
        if (mIsOwnCert) {
            return true;
        }
        if (StringUtil.isTrimBlank(cv_memberName.getInputValue())) {
            showMessage("会员名不能为空");
            return false;
        } else if (StringUtil.isTrimBlank(cv_memberPhoneNumber.getInputValue())) {
            showMessage("请输入会员手机号");
            return false;
        } else if (StringUtil.isTrimBlank(cv_companyName.getInputValue())) {
            showMessage("请输入公司名称");
            return false;
        } else if (StringUtil.isTrimBlank(cv_dlysxkz.getInputValue())) {
            showMessage("请输入道路运输许可证");
            return false;
        } else if (mAddress == null) {
            showMessage("请选择住址");
            return false;
        } else if (!Validator.isNotNull(cv_truckChargePerson.getInputValue(), this, "请填写车辆负责人")) {
            return false;
        } else if (!Validator.isNotNull(cv_identifyNumber, this, "请填写身份证号")) {
            return false;
        } else if (!Validator.isNotNull(mOtherInfoHelper.getOtherInfo(), this, "请填写其他信息")) {
            return false;
        }
        return true;
    }

    private Map<String, Object> getParams() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("member_code", LoginInfo.getLoginInfo().getUser_code());
        params.put("initiation_agree", 1);
        params.put("update_from", mIsOwnCert ? "2" : "1");


        Map<String, Object> member_data = new LinkedHashMap<>();
        member_data.put("member_name", cv_memberName.getInputValue());
        // member_data.put("member_tel", cv_memberPhoneNumber.getInputValue());

        member_data.put("member_address_province", mAddress != null ? mAddress.getRealProvince() : "");
        member_data.put("member_address_city", mAddress != null ? mAddress.getRealCity() : "");
        member_data.put("member_address_area", mAddress != null ? mAddress.getRealCounty() : "");
        member_data.put("member_address", memberAddress != null ? memberAddress : "");

        member_data.put("company_name", cv_companyName.getInputValue());
        member_data.put("road_transport_permit_no", cv_dlysxkz.getInputValue());
        member_data.putAll(mOtherInfoHelper.getParams());
        params.put("member_data", CertParamHelper.getMapJson(member_data));
        if (truckMgData != null) {
            Map<String, Object> truck_data = new LinkedHashMap<>();
            truck_data.put("plate", truckMgData.get("plate"));
            TruckCompleteInfo info = (TruckCompleteInfo) truckMgData.get("truckInfo");
            if (info != null) {
                truck_data.put("truck_type", DictionaryItem.getDictId(info.getTruck_type()));
                truck_data.put("license_plate_color", DictionaryItem.getDictId(info.getTruck_color()));
                truck_data.put("gender", DictionaryItem.getDictId(info.getSexType()));
                truck_data.put("road_transport_certificate", info.getDlysNumber());
                truck_data.put("qualification_certificate", info.getCyzgzNumber());
                truck_data.put("max_length", info.getTruckLength());
                truck_data.put("max_heavy", info.getLoadTon());
                truck_data.put("vehicle_mass", info.getVehicleMass());
                truck_data.put("loaded_vehicle_quality", info.getLoadedVehicleQuality());
                truck_data.put("identify", info.getSbdhNumber());
            }
            truck_data.put("truck_name", cv_truckChargePerson.getInputValue());
            truck_data.put("dentity_no", cv_identifyNumber.getInputValue());
            truck_data.put("member_tel", cv_memberPhoneNumber.getInputValue());
            params.put("truck_data", CertParamHelper.getMapJson(truck_data));

            List<PaperPhoto> paperPhotos = (List<PaperPhoto>) truckMgData.get("papersInfo");
            if (paperPhotos != null) {
                params.put("truck_paper", CertParamHelper.getPaperjsonArray(paperPhotos));
            }
            List<BankCard> bankCards = (List<BankCard>) truckMgData.get("bankcards");
            if (bankCards != null) {
                params.put("truck_bank", CertParamHelper.getBankListjsonArray(bankCards));
            }
        }
        return params;
    }


    @Override
    public int getLayoutId() {
        return R.layout.activity_truck_company_child_certificate;
    }

    //是否主动补充
    private boolean mIsOwnCert;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        mIsOwnCert = AppTypeConfig.removeMustInput(this);
        if (mIsOwnCert) {
            cv_companyName.getInput().setEnabled(false);
        }
        btnCommit.setEnabled(false);
        mOtherInfoHelper = new OtherInfoHelper(this, (CommonInputView) findViewById(R.id.cv_otherInfos));
        //初始化协议以及点击事件
        mAgreement = new CompanyTruckAgreement();
        new ContractPack(this, false, agreement, btnCommit, mAgreement).setOnSpanClick(new SmoothClickCheckBox.OnSpanClickListener() {
            @Override
            public void onSpanClick(int index) {
                if (truckMgData != null) {
                    TruckCompleteInfo info = (TruckCompleteInfo) truckMgData.get("truckInfo");
                    if (info != null) {
                        DictionaryItem truckType = info.getTruck_type();
                        if (truckType != null) {
                            mAgreement.vehicleType = truckType.getText();
                        }
                        mAgreement.vehicleLength = info.getTruckLength();
                        mAgreement.vehicleMaxLoad = info.getLoadTon();
                    }
                }
                mAgreement.fillData(getWindow().getDecorView());
            }
        });
        presenter = new CertInfoPresenter(this);
        presenter.getCertInfo();
    }

    private String memberAddress;

    @Override
    public void initCertInfo(CertInfo info) {
        mOtherInfoHelper.setOtherInfo(info.getOtherInfo(true));
        if (info.hasProtocol()) {
            agreement.setVisibility(View.GONE);
            btnCommit.setEnabled(true);
        }
        CertInfo.MemberDtoBean memberDtoBean = info.getNMemberDto();
        if (memberDtoBean != null) {
            cv_companyName.setText(memberDtoBean.getCompanyName());
            cv_dlysxkz.setText(memberDtoBean.getRoadTransportPermitNo());
        }

        CertInfo.MmTruckResponseDtoBean truckDto = info.getMmTruckResponseDto();
        if (truckDto == null) {
            return;
        }
        CertInfo.MmTruckResponseDtoBean.MmTruckDtoBean dto = truckDto.getMmTruckDto();
        if (dto == null) {
            return;
        }
        cv_truckChargePerson.setText(dto.getTruckName());

        cv_identifyNumber.setText(dto.getDentityNo());

        TruckCompleteInfo truckInfo = truckDto.getTruckInfo();

        if (truckInfo != null) {
            truckMgData.put("truckInfo", truckInfo);
        }
        if (dto.getPlate() != null) {
            truckMgData.put("plate", dto.getPlate());
            cv_truckManager.setText(dto.getPlate());
        }

        truckMgData.put("bankcards", CertInfo.getBankList(truckDto.getBankList()));
        List<PaperPhoto> localPapers = PapersHelper.getTruckManager();
        CertInfo.filterPaperPhoto(truckDto.getPaperList(), localPapers);
        truckMgData.put("papersInfo", localPapers);

        cv_memberPhoneNumber.setText(dto.getMemberTel());
        cv_memberName.setText(dto.getMemberName());
        cv_memberName.getInput().setEnabled(false);
        // cv_companyName.setText(dto.getCompanyName());
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

    @OnClick({R.id.cv_address, R.id.cv_truckManager, R.id.btn_commit, R.id.cv_otherInfos})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.cv_address) {
            toActivity(AddressSelectorActivity.class, JumpUtil.getBundle("title", "选择地址"), Constant.SELECT_ADDRESS_REQUEST, null);
        } else if (v.getId() == R.id.cv_truckManager) {
            Bundle bundle = JumpUtil.getBundle(KEY_TRUCK_MANAGER, truckMgData);
            bundle.putBoolean("isOwnCert",mIsOwnCert);
            toActivity(TruckCompanyChildCertTruckManagerActivity.class, bundle, TRUCKMANAGER, null);
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
        } else if (requestCode == TRUCKMANAGER) {
            truckMgData = (HashMap<String, Object>) data.getSerializableExtra(KEY_TRUCK_MANAGER);
            cv_truckManager.setText((String) truckMgData.get("plate"));
        }
    }
}
