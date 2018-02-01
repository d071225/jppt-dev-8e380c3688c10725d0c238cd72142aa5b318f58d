package com.hletong.hyc.ui.activity.cargo;

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
import com.hletong.hyc.model.CargoAgreement;
import com.hletong.hyc.model.CertInfo;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.InvoiceCert;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.PaperPhoto;
import com.hletong.hyc.presenter.CertInfoPresenter;
import com.hletong.hyc.ui.activity.AddressSelectorActivity;
import com.hletong.hyc.ui.activity.InvoiceCertAddActivity;
import com.hletong.hyc.ui.activity.PapersAddActivity;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.ui.dialog.DictItemDialog;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.ui.widget.SmoothClickCheckBox;
import com.hletong.hyc.util.CertParamHelper;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.ContractPack;
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
 * 车主版个人信息补全
 * Created by chengxin on 2017/6/12.
 */
public class CargoCompanyCertActivity extends BaseActivity implements CertInfoContract.View {
    //会员名
    @BindView(R.id.cv_memberName)
    CommonInputView cv_memberName;
    //公司名
    @BindView(R.id.cv_companyName)
    CommonInputView cv_companyName;
    //地址
    @BindView(R.id.cv_address)
    CommonInputView cvAddress;
    //证件资料类型
    @BindView(R.id.cv_papers)
    CommonInputView cvPapers;
    //业务联系人手机号
    @BindView(R.id.cv_busiContactPhone)
    CommonInputView cv_busiContactPhone;
    //业务联系人
    @BindView(R.id.cv_busiContact)
    CommonInputView cv_busiContact;


    @BindView(R.id.cv_paperSingleType)
    CommonInputView cv_paperSingleType;

    @BindView(R.id.cv_paperNumber)
    CommonInputView cv_paperNumber;
    private DictItemDialog mDictItemFragment;
    private ArrayList<DictionaryItem> items = new ArrayList<>();
    //证件类型
    private DictionaryItem selectItem;

    //业务联系人
    @BindView(R.id.cv_invoiceInfo)
    CommonInputView cv_invoiceInfo;
    private CertInfoPresenter presenter;

    @BindView(R.id.btn_commit)
    Button btnCommit;
    @BindView(R.id.agreement)
    SmoothClickCheckBox agreement;

    private CargoAgreement mAgreement;
    private Address mAddress;
    private ArrayList<PaperPhoto> mPaperPhotos = PapersHelper.getCompanyCargo();
    private ArrayList<InvoiceCert> mInvoiceCert = new ArrayList<>();

    private Map<String, Object> getParams() {

        Map<String, Object> params = new LinkedHashMap<>();
        params.put("member_code", LoginInfo.getLoginInfo().getUser_code());
        params.put("initiation_agree", 1);
        params.put("update_from", mIsOwnCert ? "2" : "1");

        Map<String, Object> member_data = new LinkedHashMap<>();
        member_data.put("member_name", cv_memberName.getInputValue());
        member_data.put("company_name", cv_companyName.getInputValue());
        member_data.put("member_address_province", mAddress != null ? mAddress.getRealProvince() : "");
        member_data.put("member_address_city", mAddress != null ? mAddress.getRealCity() : "");
        member_data.put("member_address_area", mAddress != null ? mAddress.getRealCounty() : "");
        member_data.put("member_address", memberAddress != null ? memberAddress : "");
        member_data.put("biz_contact", cv_busiContact.getInputValue());
        member_data.put("biz_contact_tel", cv_busiContactPhone.getInputValue());
        if (selectItem != null) {
            member_data.put("id_type", selectItem.getId());
            member_data.put("organiz_code", cv_paperNumber.getInputValue());
        }
        params.put("member_data", CertParamHelper.getMapJson(member_data));

        if (!ParamUtil.isEmpty(mPaperPhotos)) {
            params.put("member_paper", CertParamHelper.getPaperjsonArray(mPaperPhotos));

        }
        if (!ParamUtil.isEmpty(mInvoiceCert)) {
            params.put("invoice_list", CertParamHelper.getInvoicejsonArray(mInvoiceCert));
        }
        return params;
    }

    private boolean validate() {
        if (mIsOwnCert) {
            return true;
        }
        if (!Validator.isNotNull(cv_memberName.getInputValue(), this, "会员名不能为空")) {
            return false;
        } else if (!Validator.isNotNull(cv_companyName.getInputValue(), this, "公司名称为空")) {
            return false;
        } else if (!Validator.isNotNull(mAddress, this, "请选择地址")) {
            return false;
        } else if (!Validator.isNotNull(cv_busiContact.getInputValue(), this, "请输入业务联系人")) {
            return false;
        } else if (!Validator.isNotNull(cv_busiContactPhone.getInputValue(), this, "请输入业务联系人手机")) {
            return false;
        }
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_cargo_company_certificate;
    }
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

    @OnClick({R.id.cv_address, R.id.cv_papers, R.id.cv_invoiceInfo, R.id.cv_paperSingleType, R.id.btn_commit})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.cv_address) {
            toActivity(AddressSelectorActivity.class, JumpUtil.getBundle("title", "选择地址"), Constant.SELECT_ADDRESS_REQUEST, null);
        } else if (v.getId() == R.id.cv_paperSingleType) {
            mDictItemFragment.showDict("证件资料", "请选择证件类型", items, 1);
        } else if (v.getId() == R.id.cv_papers) {
            toActivity(PapersAddActivity.class, JumpUtil.getBundle(PaperPhoto.class.getSimpleName(), mPaperPhotos), Constant.PAPER_REQUEST, null);
        } else if (v.getId() == R.id.cv_invoiceInfo) {
            toActivity(InvoiceCertAddActivity.class, JumpUtil.getBundle(InvoiceCert.class.getSimpleName(), mInvoiceCert), Constant.INVOICE_REQUEST, null);
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
            mPaperPhotos = (ArrayList<PaperPhoto>) data.getSerializableExtra(PaperPhoto.class.getSimpleName());
            cvPapers.setText("已填写");
        } else if (requestCode == Constant.INVOICE_REQUEST) {
            mInvoiceCert = (ArrayList<InvoiceCert>) data.getSerializableExtra(InvoiceCert.class.getSimpleName());
            cv_invoiceInfo.setText("已填写");
        }
    }

    private String memberAddress;

    @Override
    public void initCertInfo(CertInfo info) {
        if (info.hasProtocol()) {
            agreement.setVisibility(View.GONE);
            btnCommit.setEnabled(true);
        }
        CertInfo.MemberDtoBean dto = info.getNMemberDto();
        if (dto == null) {
            return;
        }

        mInvoiceCert = CertInfo.getInvoiceCert(info.getInvoiceList());
        if (mInvoiceCert != null) {
            cv_invoiceInfo.setText("已填写");
        }


        setPaperType(dto.getPaperByType(items));
        cv_paperNumber.setText(dto.getOrganizCode());

        if (!ParamUtil.isEmpty(info.getPaperList())) {
            CertInfo.filterPaperPhoto(info.getPaperList(), mPaperPhotos);
            cvPapers.setText("已填写");
        }

        cv_memberName.setText(dto.getMemberName());
        cv_memberName.getInput().setEnabled(false);
        cv_companyName.setText(dto.getCompanyName());
        cv_busiContactPhone.setText(dto.getBizContactTel());
        cv_busiContact.setText(dto.getBizContact());
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
