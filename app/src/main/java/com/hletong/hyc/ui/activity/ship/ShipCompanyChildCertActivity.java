package com.hletong.hyc.ui.activity.ship;

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
import com.hletong.hyc.model.CompanyShipAgreement;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.PaperPhoto;
import com.hletong.hyc.model.ShipInfo;
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
public class ShipCompanyChildCertActivity extends BaseActivity implements CertInfoContract.View {
    @BindView(R.id.cv_memberName)
    CommonInputView cv_memberName;

    @BindView(R.id.cv_memberPhoneNumber)
    CommonInputView cv_memberPhoneNumber;

    @BindView(R.id.cv_companyName)
    CommonInputView cv_companyName;

    @BindView(R.id.cv_address)
    CommonInputView cvAddress;

    @BindView(R.id.cv_shipManager)
    CommonInputView cv_shipManager;
    //船舶负责人
    @BindView(R.id.cv_shipChargePerson)
    CommonInputView cv_shipChargePerson;
    //身份证号码
    @BindView(R.id.cv_identifyNumber)
    CommonInputView cv_identifyNumber;

    @BindView(R.id.btn_commit)
    Button btnCommit;

    private OtherInfoHelper mOtherInfoHelper;

    static final int SHIPMANAGER = 11;
    private HashMap<String, Object> shipManager = new HashMap<>();

    @BindView(R.id.agreement)
    SmoothClickCheckBox agreement;
    private CompanyShipAgreement mAgreement;

    private Address mAddress;

    private CertInfoPresenter presenter;

    private Map<String, Object> getParams() {

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("member_code", LoginInfo.getLoginInfo().getUser_code());
        params.put("initiation_agree", 1);
        params.put("update_from", mIsOwnCert ? "2" : "1");

        Map<String, Object> member_data = new LinkedHashMap<>();
        member_data.put("member_name", cv_memberName.getInputValue());
        //member_data.put("member_tel", cv_memberPhoneNumber.getInputValue());
        member_data.put("member_address_province", mAddress != null ? mAddress.getRealProvince() : "");
        member_data.put("member_address_city", mAddress != null ? mAddress.getRealCity() : "");
        member_data.put("member_address_area", mAddress != null ? mAddress.getRealCounty() : "");
        member_data.put("member_address", memberAddress != null ? memberAddress : "");
        member_data.put("company_name", cv_companyName.getInputValue());
        member_data.putAll(mOtherInfoHelper.getParams());

        params.put("member_data", CertParamHelper.getMapJson(member_data));

        if (shipManager != null) {
            Map<String, Object> ship_data = new LinkedHashMap<>();
            ship_data.put("ship", shipManager.get("ship"));
            ShipInfo info = (ShipInfo) shipManager.get("shipInfo");
            if (info != null) {
                ship_data.put("ship_type", DictionaryItem.getDictId(info.getShip_type()));
                ship_data.put("load_ton", info.getLoadTon());
                ship_data.put("ship_length", info.getShipLength());
                ship_data.put("net_ton", info.getNewTonnage());
                ship_data.put("full_draft", info.getLoadedWater());
                ship_data.put("nationality_cert", info.getGjzsNumber());
            }
            //个人账号信息填写到船舶或者车辆信息里
            ship_data.put("ship_name", cv_shipChargePerson.getInputValue());
            ship_data.put("dentity_no", cv_identifyNumber.getInputValue());
            ship_data.put("member_tel", cv_memberPhoneNumber.getInputValue());

            params.put("ship_data", CertParamHelper.getMapJson(ship_data));

            List<PaperPhoto> paperPhotos = (List<PaperPhoto>) shipManager.get("papersInfo");
            if (paperPhotos != null) {
                params.put("ship_paper", CertParamHelper.getPaperjsonArray(paperPhotos));
            }
            List<BankCard> bankCards = (List<BankCard>) shipManager.get("bankcards");
            if (bankCards != null) {
                params.put("ship_bank", CertParamHelper.getBankListjsonArray(bankCards));
            }
        }
        return params;
    }

    private boolean validate() {
        if (mIsOwnCert) {
            return true;
        }
        if (!Validator.isNotNull(cv_memberName.getInputValue(), this, "会员名不能为空")) {
            return false;
        } else if (!Validator.isNotNull(cv_memberPhoneNumber.getInputValue(), this, "会员手机号不能为空")) {
            return false;
        } else if (!Validator.isNotNull(cv_companyName.getInputValue(), this, "公司名称为空")) {
            return false;
        } else if (!Validator.isNotNull(mAddress, this, "请选择地址")) {
            return false;
        } else if (!Validator.isNotNull(shipManager, this, "请填写船舶管理")) {
            return false;
        } else if (!Validator.isNotNull(cv_shipChargePerson.getInputValue(), this, "请填写船舶负责人")) {
            return false;
        } else if (!Validator.isNotNull(cv_identifyNumber, this, "请填写身份证号")) {
            return false;
        } else if (!Validator.isNotNull(mOtherInfoHelper.getOtherInfo(), this, "请填写其他信息")) {
            return false;
        }
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_ship_company_child_certificate;
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
        mAgreement = new CompanyShipAgreement();
        new ContractPack(this, false, agreement, btnCommit, mAgreement).setOnSpanClick(new SmoothClickCheckBox.OnSpanClickListener() {
            @Override
            public void onSpanClick(int index) {
                if (shipManager != null) {
                    ShipInfo info = (ShipInfo) shipManager.get("shipInfo");
                    if (info != null) {
                        DictionaryItem shipType = info.getShip_type();
                        if (shipType != null) {
                            mAgreement.shipType = shipType.getText();
                        }
                        mAgreement.shipWeight = info.getNewTonnage();
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
        mOtherInfoHelper.setOtherInfo(info.getOtherInfo(false));

        if (info.hasProtocol()) {
            agreement.setVisibility(View.GONE);
            btnCommit.setEnabled(true);
        }

        CertInfo.MemberDtoBean memberDtoBean = info.getNMemberDto();
        if (memberDtoBean != null) {
            cv_companyName.setText(memberDtoBean.getCompanyName());
        }

        CertInfo.MmShipResponseDtoBean shipDto = info.getMmShipResponseDto();
        if (shipDto == null) {
            return;
        }
        CertInfo.MmShipResponseDtoBean.MmShipDtoBean dto = shipDto.getMmShipDto();
        if (dto == null) {
            return;
        }
        cv_identifyNumber.setText(dto.getDentityNo());
        cv_shipChargePerson.setText(dto.getShipName());
        ShipInfo shipInfo = shipDto.getShipInfo();
        if (shipInfo != null) {
            shipManager.put("shipInfo", shipInfo);
        }
        if (dto.getShip() != null) {
            shipManager.put("ship", dto.getShip());
            cv_shipManager.setText(dto.getShip());
        }
        shipManager.put("bankcards", CertInfo.getBankList(shipDto.getBankList()));

        List<PaperPhoto> localPapers = PapersHelper.getShipManager();
        CertInfo.filterPaperPhoto(shipDto.getPaperList(), localPapers);
        shipManager.put("papersInfo", localPapers);

        //cv_companyName.setText(dto.getCompanyName());
        cv_memberName.setText(dto.getMemberName());
        cv_memberName.getInput().setEnabled(false);
        cv_memberPhoneNumber.setText(dto.getMemberTel());
        memberAddress = dto.getMemberAddress();
        mAddress = new Address(dto.getMemberAddressProvince(), dto.getMemberAddressCity(), dto.getMemberAddressArea(), "");
        cvAddress.setText(mAddress.buildAddress());
    }

    @Override
    public void certSuccess(String message) {
        showMessage(message);
        AppManager.removeCookie();
        finish();
    }

    @OnClick({R.id.cv_address, R.id.cv_shipManager, R.id.btn_commit,R.id.cv_otherInfos})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.cv_address) {
            toActivity(AddressSelectorActivity.class, JumpUtil.getBundle("title", "选择地址"), Constant.SELECT_ADDRESS_REQUEST, null);
        } else if (v.getId() == R.id.cv_shipManager) {
            Bundle bundle = JumpUtil.getBundle("shipManager", shipManager);
            bundle.putBoolean("isOwnCert",mIsOwnCert);
            toActivity(ShipCompanyChildCertShipManagerActivity.class, bundle, SHIPMANAGER, null);
        } else if (v.getId() == R.id.btn_commit) {
            if (validate()) {
                presenter.summitTruckPerson(getParams());
            }
        }else if (v.getId() == R.id.cv_otherInfos) {
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
        } else if (requestCode == SHIPMANAGER) {
            shipManager = (HashMap<String, Object>) data.getSerializableExtra("shipManager");
            cv_shipManager.setText((String) shipManager.get("ship"));
        }
    }
}
