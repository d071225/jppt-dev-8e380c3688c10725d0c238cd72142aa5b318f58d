package com.hletong.hyc.ui.activity.ship;

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
import com.hletong.hyc.model.PersonalShipAgreement;
import com.hletong.hyc.model.ShipInfo;
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
import com.hletong.hyc.util.Validator;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.util.AppManager;
import com.xcheng.okhttp.util.ParamUtil;
import com.xcheng.view.util.JumpUtil;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 船舶个人信息补全
 * Created by chengxin on 2017/6/12.
 */
public class ShipPersonalChildCertActivity extends BaseActivity implements CertInfoContract.View {
    @BindView(R.id.cv_ship)
    CommonInputView cv_ship;

    @BindView(R.id.cv_memberName)
    CommonInputView cv_memberName;

    @BindView(R.id.cv_shipOwner)
    CommonInputView cv_shipOwner;

    @BindView(R.id.cv_memberPhoneNumber)
    CommonInputView cv_memberPhoneNumber;

    @BindView(R.id.cv_identifyNumber)
    CommonInputView cv_identifyNumber;

    @BindView(R.id.cv_address)
    CommonInputView cv_address;

    @BindView(R.id.cv_shipInfos)
    CommonInputView cv_shipInfos;


    @BindView(R.id.cv_papersInfo)
    CommonInputView cv_papersInfo;

    //证件资料
    @BindView(R.id.cv_bankcardInfo)
    CommonInputView cv_bankcardInfo;
    static final int SHIPINFO = 10;


    @BindView(R.id.btn_commit)
    Button btnCommit;

    @BindView(R.id.agreement)
    SmoothClickCheckBox agreement;

    //公司车辆
    @BindView(R.id.cb_company_ship)
    SmoothClickCheckBox cbCompanyShip;

    private PersonalShipAgreement mAgreement;
    private Address mAddress;

    private ShipInfo mShipInfo;

    private ArrayList<PaperPhoto> mPaperPhotos = PapersHelper.getShipManager();
    private OtherInfoHelper mOtherInfoHelper;


    private ArrayList<BankCard> mBankCards;
    private CertInfoPresenter presenter;

    private Map<String, Object> getParams() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("member_code", LoginInfo.getLoginInfo().getUser_code());
        //==
        params.put("settled_status", cbCompanyShip.getTag());
        params.put("initiation_agree", 1);
        params.put("update_from", mIsOwnCert ? "2" : "1");


        Map<String, Object> member_data = new LinkedHashMap<>();
        member_data.put("member_name", cv_memberName.getInputValue());
        member_data.put("member_address_province", mAddress != null ? mAddress.getRealProvince() : "");
        member_data.put("member_address_city", mAddress != null ? mAddress.getRealCity() : "");
        member_data.put("member_address_area", mAddress != null ? mAddress.getRealCounty() : "");
        member_data.put("member_address", memberAddress != null ? memberAddress : "");
        member_data.putAll(mOtherInfoHelper.getParams());

        params.put("member_data", CertParamHelper.getMapJson(member_data));


        if (mShipInfo != null) {
            Map<String, Object> ship_data = new LinkedHashMap<>();
            ship_data.put("ship", cv_ship.getInputValue());
            ship_data.put("ship_type", DictionaryItem.getDictId(mShipInfo.getShip_type()));
            ship_data.put("load_ton", mShipInfo.getLoadTon());
            ship_data.put("ship_length", mShipInfo.getShipLength());
            ship_data.put("net_ton", mShipInfo.getNewTonnage());
            ship_data.put("full_draft", mShipInfo.getLoadedWater());
            ship_data.put("nationality_cert", mShipInfo.getGjzsNumber());

            ship_data.put("ship_name", cv_shipOwner.getInputValue());
            ship_data.put("dentity_no", cv_identifyNumber.getInputValue());
            ship_data.put("member_tel", cv_memberPhoneNumber.getInputValue());

            params.put("ship_data", CertParamHelper.getMapJson(ship_data));
        }
        if (!ParamUtil.isEmpty(mPaperPhotos)) {
            params.put("ship_paper", CertParamHelper.getPaperjsonArray(mPaperPhotos));
        }
        if (!ParamUtil.isEmpty(mBankCards)) {
            params.put("ship_bank", CertParamHelper.getBankListjsonArray(mBankCards));

        }
        return params;
    }


    private boolean validate() {
        if (mIsOwnCert) {
            return true;
        }
        if (!Validator.isNotNull(cv_ship.getInputValue(), this, "船号不能为空")) {
            return false;
        } else if (!Validator.isNotNull(cv_memberName.getInputValue(), this, "会员名不能为空")) {
            return false;
        } else if (!Validator.isNotNull(cv_shipOwner.getInputValue(), this, "船舶所有人不能为空")) {
            return false;
        } else if (!Validator.isNotNull(cv_memberPhoneNumber.getInputValue(), this, "会员手机号不能为空")) {
            return false;
        } else if (!Validator.isNotNull(cv_identifyNumber.getInputValue(), this, "身份证号不能为空")) {
            return false;
        } else if (!Validator.isNotNull(mAddress, this, "住址不能为空")) {
            return false;
        } else if (mShipInfo == null) {
            showMessage("请选择船舶信息");
            return false;
        } else if (mPaperPhotos == null) {
            showMessage("请选择证件信息");
            return false;
        } else if (!Validator.isNotNull(mOtherInfoHelper.getOtherInfo(), this, "请填写其他信息")) {
            return false;
        }
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_ship_personal_child_cert;
    }

    private boolean mIsOwnCert;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        mIsOwnCert = AppTypeConfig.removeMustInput(this);
        if (mIsOwnCert) {
            cbCompanyShip.setVisibility(View.GONE);
        }
        mOtherInfoHelper = new OtherInfoHelper(this, (CommonInputView) findViewById(R.id.cv_otherInfos));

        btnCommit.setEnabled(false);
        cbCompanyShip.setTag(0);
        cbCompanyShip.setCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                cbCompanyShip.setTag(isChecked ? "1" : "0");
            }
        });
        //初始化协议以及点击事件
        mAgreement = new PersonalShipAgreement();
        new ContractPack(this, false, agreement, btnCommit, mAgreement).setOnSpanClick(new SmoothClickCheckBox.OnSpanClickListener() {
            @Override
            public void onSpanClick(int index) {
                if (mShipInfo != null) {
                    mAgreement.shipType = mShipInfo.getShip_type().getText();
                    mAgreement.shipWeight = mShipInfo.getNewTonnage();
                }
                mAgreement.fillData(getWindow().getDecorView());

            }
        });
        presenter = new CertInfoPresenter(this);
        presenter.getCertInfo();
//        mShipManager = getSerializable("shipManager");
//        if (mShipManager != null) {
//            cv_ship.setText((String) mShipManager.get("ship"));
//            mShipInfo = (ShipInfo) mShipManager.get("shipInfo");
//            if (mShipInfo != null) {
//                cv_ship.setText("已填写");
//            }
//            mPaperPhotos = (ArrayList<PaperPhoto>) mShipManager.get("papersInfo");
//            if (mPaperPhotos != null) {
//                cv_papersInfo.setText("已填写");
//            }
//            mBankCards = (ArrayList<BankCard>) mShipManager.get("bankcards");
//            if (mBankCards != null) {
//                cv_bankcardInfo.setText("已填写");
//            }
//        } else {
//            mShipManager = new HashMap<>();
//        }
    }

    @OnClick({R.id.cv_bankcardInfo, R.id.cv_address, R.id.cv_shipInfos, R.id.cv_papersInfo, R.id.btn_commit, R.id.cv_otherInfos})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.cv_bankcardInfo) {
            toActivity(BankCardAddActivity.class, JumpUtil.getBundle(BankCard.class.getSimpleName(), mBankCards), Constant.BANKCARD_REQUEST, null);
        } else if (v.getId() == R.id.cv_address) {
            toActivity(AddressSelectorActivity.class, JumpUtil.getBundle("title", "选择地址"), Constant.SELECT_ADDRESS_REQUEST, null);
        } else if (v.getId() == R.id.cv_shipInfos) {
            Bundle bundle = JumpUtil.getBundle(ShipInfo.class.getSimpleName(), mShipInfo);
            toActivity(ShipInfoAddActivity.class, bundle, SHIPINFO, null);
        } else if (v.getId() == R.id.cv_papersInfo) {
            toActivity(PapersAddActivity.class, JumpUtil.getBundle(PaperPhoto.class.getSimpleName(), mPaperPhotos), Constant.PAPER_REQUEST, null);
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

        if (requestCode == SHIPINFO) {
            mShipInfo = (ShipInfo) data.getSerializableExtra(ShipInfo.class.getSimpleName());
            cv_shipInfos.setText("已填写");
        } else if (requestCode == Constant.PAPER_REQUEST) {
            mPaperPhotos = (ArrayList<PaperPhoto>) data.getSerializableExtra(PaperPhoto.class.getSimpleName());
            cv_papersInfo.setText("已填写");
        } else if (requestCode == Constant.BANKCARD_REQUEST) {
            mBankCards = (ArrayList<BankCard>) data.getSerializableExtra(BankCard.class.getSimpleName());
            cv_bankcardInfo.setText("已填写");
        } else if (requestCode == Constant.SELECT_ADDRESS_REQUEST) {
            mAddress = Address.getAddress(data);
            cv_address.setText(mAddress.buildAddress());
        }
    }

    private String memberAddress;

    @Override
    public void initCertInfo(CertInfo info) {
        mOtherInfoHelper.setOtherInfo(info.getOtherInfo(false));

        if (info.hasProtocol()) {
            agreement.setVisibility(View.GONE);
            btnCommit.setEnabled(true);
        }

        CertInfo.MmShipResponseDtoBean shipDto = info.getMmShipResponseDto();
        if (shipDto == null) {
            return;
        }

        mBankCards = CertInfo.getBankList(shipDto.getBankList());
        if (mBankCards != null) {
            cv_bankcardInfo.setText("已填写");
        }

        if (!ParamUtil.isEmpty(shipDto.getPaperList())) {
            CertInfo.filterPaperPhoto(shipDto.getPaperList(), mPaperPhotos);
            cv_papersInfo.setText("已填写");
        }
        mShipInfo = shipDto.getShipInfo();
        if (mShipInfo != null) {
            cv_shipInfos.setText("已填写");
        }
        CertInfo.MmShipResponseDtoBean.MmShipDtoBean dto = shipDto.getMmShipDto();
        if (dto == null) {
            return;
        }
        cv_identifyNumber.setText(dto.getDentityNo());
        cv_shipOwner.setText(dto.getShipName());

        cv_ship.setText(dto.getShip());
        cv_ship.getInput().setEnabled(false);
        cv_memberName.setText(dto.getMemberName());
        cv_memberName.getInput().setEnabled(false);
        cv_memberPhoneNumber.setText(dto.getMemberTel());
        memberAddress = dto.getMemberAddress();
        mAddress = new Address(dto.getMemberAddressProvince(), dto.getMemberAddressCity(), dto.getMemberAddressArea(), "");
        cv_address.setText(mAddress.buildAddress());
    }

    @Override
    public void certSuccess(String message) {
        showMessage(message);
        AppManager.removeCookie();
        finish();
    }
}
