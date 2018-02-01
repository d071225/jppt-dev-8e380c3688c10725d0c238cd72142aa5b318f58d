package com.hletong.hyc.ui.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hletong.hyc.HyApplication;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.ReceiptContract;
import com.hletong.hyc.model.Receipt;
import com.hletong.hyc.model.validate.ReceiptInfo;
import com.hletong.hyc.presenter.ReceiptPresenter;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.LocationHelper;
import com.hletong.hyc.util.RecyclerAdapterObserver;
import com.hletong.mob.base.BaseRecyclerAdapter;
import com.hletong.mob.gallery.builder.PreViewBuilder;
import com.hletong.mob.gallery.widget.PickerRecyclerView;
import com.xcheng.view.processbtn.ProcessButton;
import com.xcheng.view.util.JumpUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/1/4.
 * 上传收货单
 */

public class ReceiptUploadActivityNew extends ImageSelectorActivityNew<ReceiptContract.Presenter> implements ReceiptContract.View {

    @BindView(R.id.submit)
    ProcessButton mSubmitView;
    @BindView(R.id.checkbox)
    CheckBox mCheckBox;
    @BindView(R.id.confirm_block)
    View mConfirmBlock;
    @BindView(R.id.confirm_title)
    TextView mConfirmTitle;

    @BindView(R.id.contract)
    View mContractView;
    @BindView(R.id.contract_block)
    View mContractBlock;

    @BindView(R.id.contact_desc)
    TextView mContactLabel;
    @BindView(R.id.contact)
    TextView mContact;

    //这个View在收货单未上传之前或是运单回传（待办消息）的时候，用来上传运单图片，
    //若收货单已上传，那么这个view用来展示已上传的图片（如果没有上传过图片就把这个View隐藏起来）
    @BindView(R.id.primary)
    View mPrimaryGallery;
    //这个是用收货单已经上传了，但是运单未审核或未审核通过的时候来做运单重传（非运单回传）的
    @BindView(R.id.secondary)
    ViewStub mSecondaryGallery;

    @BindView(R.id.cargo_code)
    CommonInputView mCargoCode;
    @BindView(R.id.cargo_name)
    CommonInputView mCargoName;
    @BindView(R.id.plate)
    CommonInputView mPlate;
    @BindView(R.id.des)
    CommonInputView mDes;
    @BindView(R.id.address)
    CommonInputView mAddress;
    @BindView(R.id.date)
    CommonInputView mDate;
    @BindView(R.id.unit_ct)
    CommonInputView mUnitCt;
    @BindView(R.id.weight)
    CommonInputView mWeight;
    @BindView(R.id.receive_psw)
    CommonInputView mReceivePsw;
    @BindView(R.id.loading_psw)
    CommonInputView mLoadingPsw;

    private LocationHelper mLocationHelper;

    @Override
    protected ProcessButton getProcessBtn() {
        return mSubmitView;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_upload_receipt;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mLocationHelper = new LocationHelper(this);
        getPresenter().start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.phone_selector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CallPhoneDialog.getInstance().show(getSupportFragmentManager());
        return true;
    }

    @OnClick({R.id.submit, R.id.contact_info, R.id.contract})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit: {
                switch (getIntent().getIntExtra("type", -1)) {
                    case ReceiptContract.FROM_HISTORY:
                        getPresenter().submit(getPhotos(), null);
                        break;
                    case ReceiptContract.FROM_MESSAGE:
                        getPresenter().submit(getPhotos(), null);
                        break;
                    case ReceiptContract.FROM_UPCOMING:
                        getPresenter().submit(
                                getPhotos(),
                                new ReceiptInfo(
                                        HyApplication.getAMapLocation(),
                                        getWeight(),
                                        getCount(),
                                        mReceivePsw.getInputValue(),
                                        mLoadingPsw.getInputValue(),
                                        mCheckBox.isChecked()));
                        break;
                }
                break;
            }
            case R.id.contact_info:
                getPresenter().call();
                break;
            case R.id.contract: {
                getPresenter().viewContract();
                break;
            }
        }
    }

    private String getWeight() {
        final String s = mWeight.getInputValue();
        if (TextUtils.isEmpty(s) && mWeight.getInput().getHint() != null)
            return mWeight.getInput().getHint().toString();
        return s;
    }

    private String getCount() {
        final String s = mUnitCt.getInputValue();
        if (TextUtils.isEmpty(s) && mUnitCt.getInput().getHint() != null) {
            return mUnitCt.getInput().getHint().toString();
        }
        return s;
    }

    @Override
    public void startLocating(boolean isForceStart) {
        mLocationHelper.startLocating(isForceStart);
    }

    @Override
    public void onLocationError(String message) {
        mLocationHelper.onLocationError(message);
    }

    Receipt mReceipt;

    @Override
    protected ReceiptContract.Presenter createPresenter() {
        mReceipt = getParcelable("receipt");
        return new ReceiptPresenter(this,
                getIntent().getIntExtra("type", -1),
                mReceipt);
    }

    @Override
    public void showTitle(String title) {
        setCustomTitle(title);
    }

    @Override
    public void showContact(String label, String contact) {
        mContactLabel.setText(label);
        mContact.setText(contact);
    }

    @Override
    public void showReceipt(String doc, String cargoName, String carrierLabel, String carrier, String cargoDesc, String address, String date) {
        mCargoCode.setText(doc);//业务单据号
        mPlate.setLabel(carrierLabel);
        mPlate.setText(carrier);//车牌号
        mCargoName.setText(cargoName);//货物名称
        mDes.setText(cargoDesc);//货物描述
        mAddress.setText(address);//装货地
        mDate.setText(date);//装货时间
    }

    @Override
    public void showContract() {
        mContractBlock.setVisibility(View.VISIBLE);
    }

    @Override
    public void showWeight(String cargo, boolean input) {
        if (input) {
            mWeight.setEditTextHint(cargo);
        } else {
            mWeight.setText(cargo);
            mWeight.setMode(CommonInputView.VIEW);
        }
    }

    @Override
    public void showNumber(String cargo, String unit, boolean fraction, boolean input) {
        mUnitCt.setSuffixText(unit);
        if (input) {
            mUnitCt.setEditTextHint(cargo);
            mUnitCt.setInputType(fraction ? CommonInputView.NUMBER_DECIMAL : CommonInputView.NUMBER);
        } else {
            mUnitCt.setText(cargo);
            mUnitCt.setMode(CommonInputView.VIEW);
        }
    }

    @Override
    public void showPrimaryGallery(String label, String hint, int billingType) {
        final PickerRecyclerView pickerRecyclerView = initialImageBlock(mPrimaryGallery, label, hint);
        new PickerRecyclerView.Builder(this).maxCount(3).action(PreViewBuilder.SELECT).observe(new PhotoObserver(pickerRecyclerView, billingType)).build(pickerRecyclerView);
        pickerRecyclerView.setOnTakeClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelector(pickerRecyclerView);
            }
        });
    }

    @Override
    public void showPrimaryGallery(String label, String hint, List<String> images) {
        final PickerRecyclerView pickerRecyclerView = initialImageBlock(mPrimaryGallery, label, hint);
        new PickerRecyclerView.Builder(this).selectedPhotos(new ArrayList<>(images)).action(PreViewBuilder.PREVIEW).build(pickerRecyclerView);
    }

    @Override
    public void showSecondaryGallery(String hint, int billingType) {
        View gallery = mSecondaryGallery.inflate();
        gallery.setId(R.id.secondary);
        final PickerRecyclerView pickerRecyclerView = initialImageBlock(gallery, "运单重传", hint);
        new PickerRecyclerView.Builder(this).maxCount(3).action(PreViewBuilder.SELECT).observe(new PhotoObserver(pickerRecyclerView, billingType)).build(pickerRecyclerView);
        pickerRecyclerView.setOnTakeClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelector(pickerRecyclerView);
            }
        });
    }

    @Override
    public void showDocumentConfirm(String title) {
        mConfirmBlock.setVisibility(View.VISIBLE);
        mConfirmTitle.setText(title);
    }

    @Override
    public void showSubmitButton() {
        mSubmitView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showPasswordArea() {
        mReceivePsw.setVisibility(View.VISIBLE);
        mLoadingPsw.setVisibility(View.VISIBLE);
    }

    @Override
    public void makeCall(String contact, String tel) {
        CallPhoneDialog.getInstance().show(getSupportFragmentManager(), "拨打电话", "是否要联系卸货地联系人" + contact + "?", tel);
    }

    private PickerRecyclerView initialImageBlock(View parent, String label, String hint) {

        parent.setVisibility(View.VISIBLE);
        TextView labelView = parent.findViewById(R.id.upload_title);
        labelView.setText(label);
        TextView hintView = parent.findViewById(R.id.upload_hint);
        hintView.setText(hint);

        if (mPrimaryGallery == parent && mReceipt != null && mReceipt.getWaybill_status() == 3) {
            hintView.setText("（点击查看未通过原因）");
            hintView.setTextColor(Color.RED);
            hintView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JumpUtil.toActivity(ReceiptUploadActivityNew.this, ReceiptRefuseActivity.class, JumpUtil.getBundle("receipt", mReceipt));
                }
            });
        }
        hintView.setVisibility(View.VISIBLE);
        return (PickerRecyclerView) parent.findViewById(R.id.recycler_GridView);
    }

    private List<String> getPhotos() {
        View view = findViewById(R.id.secondary);
        if (view instanceof ViewStub) {
            PickerRecyclerView recyclerView = mPrimaryGallery.findViewById(R.id.recycler_GridView);
            return recyclerView.getPhotos();
        } else {
            PickerRecyclerView recyclerView = view.findViewById(R.id.recycler_GridView);
            return recyclerView.getPhotos();
        }
    }

    private class PhotoObserver extends RecyclerAdapterObserver {
        private RecyclerView rv;
        private int billingType;

        public PhotoObserver(RecyclerView rv, int billingType) {
            this.rv = rv;
            this.billingType = billingType;
            mSubmitView.setEnabled(billingType != 1);
            ViewGroup vp = (ViewGroup) rv.getParent();
            TextView uh = vp.findViewById(R.id.upload_hint);
            uh.setVisibility(billingType == 1 ? View.VISIBLE : View.GONE);
        }

        @Override
        public void onChanged() {
            if (billingType == 1) {
                BaseRecyclerAdapter adapter = (BaseRecyclerAdapter) rv.getAdapter();
                mSubmitView.setEnabled(adapter.getDataCount() > 0);
            }
        }
    }
}
