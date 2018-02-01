package com.hletong.hyc.ui.activity.truck;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.CertInfoContract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.BankCard;
import com.hletong.hyc.model.CertInfo;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.PaperPhoto;
import com.hletong.hyc.presenter.CertInfoPresenter;
import com.hletong.hyc.ui.activity.AddressSelectorActivity;
import com.hletong.hyc.ui.activity.BankCardAddActivity;
import com.hletong.hyc.ui.activity.PapersAddActivity;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.ui.dialog.DictItemDialog;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.CertParamHelper;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.PapersHelper;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.dialog.selector.BottomSelectorDialog;
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
 * 船舶公司主账号认证
 * Created by chengxin on 2017/6/12.
 */
public class TruckCompanyMainCertActivity extends BaseActivity implements CertInfoContract.View {
    @BindView(R.id.cv_memberName)
    CommonInputView cv_memberName;

    @BindView(R.id.cv_memberPhoneNumber)
    CommonInputView cv_memberPhoneNumber;

    @BindView(R.id.cv_companyName)
    CommonInputView cv_companyName;

    @BindView(R.id.cv_dlysxkz)
    CommonInputView cv_dlysxkz;

    @BindView(R.id.cv_address)
    CommonInputView cvAddress;

    @BindView(R.id.cv_paperSingleType)
    CommonInputView cv_paperSingleType;

    @BindView(R.id.cv_paperNumber)
    CommonInputView cv_paperNumber;

    @BindView(R.id.cv_companyPaperType)
    CommonInputView cv_companyPaperType;

    @BindView(R.id.cv_companyBankCount)
    CommonInputView cv_companyBankCount;

    private DictItemDialog mDictItemFragment;
    private ArrayList<DictionaryItem> items = new ArrayList<>();
    //证件类型
    private DictionaryItem selectItem;

    private Address mAddress;

    private ArrayList<PaperPhoto> mCompanyPaperPhotos = PapersHelper.getTruckCompanyMain();

    private ArrayList<BankCard> mBankCards;

    private CertInfoPresenter presenter;

    private Map<String, Object> getParams() {
        Map<String, Object> params = new LinkedHashMap<>();
        params.put("member_code", LoginInfo.getLoginInfo().getUser_code());
        params.put("update_from", mIsOwnCert ? "2" : "1");
        Map<String, Object> member_data = new LinkedHashMap<>();
        member_data.put("member_name", LoginInfo.getLoginInfo().getUser_name());
        member_data.put("member_tel", cv_memberPhoneNumber.getInputValue());
        member_data.put("company_name", cv_companyName.getInputValue());
        member_data.put("member_address_province", mAddress != null ? mAddress.getRealProvince() : "");
        member_data.put("member_address_city", mAddress != null ? mAddress.getRealCity() : "");
        member_data.put("member_address_area", mAddress != null ? mAddress.getRealCounty() : "");
        member_data.put("member_address", memberAddress != null ? memberAddress : "");
        member_data.put("road_transport_permit_no", cv_dlysxkz.getInputValue());
        if (selectItem != null) {
            member_data.put("id_type", selectItem.getId());
            member_data.put("organiz_code", cv_paperNumber.getInputValue());
        }
        params.put("member_data", CertParamHelper.getMapJson(member_data));

        if (!ParamUtil.isEmpty(mCompanyPaperPhotos)) {
            params.put("member_paper", CertParamHelper.getPaperjsonArray(mCompanyPaperPhotos));
        }
        if (!ParamUtil.isEmpty(mBankCards)) {
            params.put("member_bank", CertParamHelper.getBankListjsonArray(mBankCards));
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
        } else if (!Validator.isNotNull(cv_dlysxkz.getInputValue(), this, "道路运输许可证号为空")) {
            return false;
        } else if (!Validator.isNotNull(mAddress, this, "请选择地址")) {
            return false;
        } else if (!Validator.isNotNull(selectItem, this, "请选择证件类型")) {
            return false;
        } else if (!Validator.isNotNull(cv_paperNumber, this, "请输入证件号码")) {
            return false;
        }
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_truck_company_main_certificate;
    }

    //是否主动补充
    private boolean mIsOwnCert;

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        mIsOwnCert = AppTypeConfig.removeMustInput(this);
        presenter = new CertInfoPresenter(this);
        presenter.getCertInfo();

        items.add(new DictionaryItem("1", "营业执照(三证合一)"));
        items.add(new DictionaryItem("2", "组织机构代码"));
        mDictItemFragment = new DictItemDialog(this);
        mDictItemFragment.setOnItemSelected(new BottomSelectorDialog.OnItemSelectedListener<DictionaryItem>() {
            @Override
            public void onItemSelected(DictionaryItem item, int extra) {
                setPaperType(item);
            }
        });
    }

    private void setPaperType(DictionaryItem paperType) {
        if (paperType != null) {
            selectItem = paperType;
            cv_paperSingleType.setText(paperType.getText());
        }
    }

    private String memberAddress;

    @Override
    public void initCertInfo(CertInfo info) {
        CertInfo.MemberDtoBean memberDto = info.getNMemberDto();
        if (memberDto == null) {
            return;
        }
        cv_dlysxkz.setText(memberDto.getRoadTransportPermitNo());

        setPaperType(memberDto.getPaperByType(items));
        cv_paperNumber.setText(memberDto.getOrganizCode());

        mBankCards = CertInfo.getBankList(info.getBankList());
        if (mBankCards != null) {
            cv_companyBankCount.setText("已填写");
        }

        if (!ParamUtil.isEmpty(info.getPaperList())) {
            CertInfo.filterPaperPhoto(info.getPaperList(), mCompanyPaperPhotos);
            cv_companyPaperType.setText("已填写");
        }

        cv_companyName.setText(memberDto.getCompanyName());
        cv_memberName.setText(memberDto.getMemberName());
        cv_memberName.getInput().setEnabled(false);
        cv_memberPhoneNumber.setText(memberDto.getMemberTel());
        cv_dlysxkz.setText(memberDto.getRoadTransportPermitNo());
        mAddress = new Address(memberDto.getMemberAddressProvince(), memberDto.getMemberAddressCity(), memberDto.getMemberAddressArea(), "");
        cvAddress.setText(mAddress.buildAddress());
        memberAddress = memberDto.getMemberAddress();
    }

    @Override
    public void certSuccess(String message) {
        showMessage(message);
        AppManager.removeCookie();
        finish();
    }

    @OnClick({R.id.cv_address, R.id.cv_paperSingleType, R.id.cv_companyPaperType, R.id.cv_companyBankCount, R.id.btn_commit})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.cv_address) {
            toActivity(AddressSelectorActivity.class, JumpUtil.getBundle("title", "选择地址"), Constant.SELECT_ADDRESS_REQUEST, null);
        } else if (v.getId() == R.id.cv_paperSingleType) {
            mDictItemFragment.showDict("证件资料", "请选择证件类型", items, 1);
        } else if (v.getId() == R.id.cv_companyPaperType) {
            toActivity(PapersAddActivity.class, JumpUtil.getBundle(PaperPhoto.class.getSimpleName(), mCompanyPaperPhotos), Constant.PAPER_REQUEST, null);
        } else if (v.getId() == R.id.cv_companyBankCount) {
            toActivity(BankCardAddActivity.class, JumpUtil.getBundle(BankCard.class.getSimpleName(), mBankCards), Constant.BANKCARD_REQUEST, null);
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

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        if (requestCode == Constant.SELECT_ADDRESS_REQUEST) {
            mAddress = Address.getAddress(data);
            cvAddress.setText(mAddress.buildAddress());
        } else if (requestCode == Constant.PAPER_REQUEST) {
            mCompanyPaperPhotos = (ArrayList<PaperPhoto>) data.getSerializableExtra(PaperPhoto.class.getSimpleName());
            cv_companyPaperType.setText("已填写");
        } else if (requestCode == Constant.BANKCARD_REQUEST) {
            mBankCards = (ArrayList<BankCard>) data.getSerializableExtra(BankCard.class.getSimpleName());
            cv_companyBankCount.setText("已填写");
        }
    }
}
