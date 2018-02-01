package com.hletong.hyc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.InfoCompleteContract;
import com.hletong.hyc.model.BankCard;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.RegisterPhoto;
import com.hletong.hyc.ui.dialog.DictItemDialog;
import com.hletong.hyc.ui.fragment.BankListFragment;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.dialog.selector.BottomSelectorDialog;
import com.hletong.mob.util.StringUtil;
import com.xcheng.view.util.JumpUtil;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 银行卡信息输入
 * Created by chengxin on 2017/6/12.
 */
public class BankCardInputActivity extends BaseActivity implements InfoCompleteContract.View {
    @BindView(R.id.cv_cardNumber)
    CommonInputView cv_cardNumber;

    @BindView(R.id.cv_bank)
    CommonInputView cv_bank;

    @BindView(R.id.cv_userName)
    CommonInputView cv_userName;

    @BindView(R.id.cv_bankBranch)
    CommonInputView cv_bankBranch;

    @BindView(R.id.cv_paperType)
    CommonInputView cv_paperType;


    @BindView(R.id.cv_paperNumber)
    CommonInputView cv_paperNumber;

    @BindView(R.id.cv_zdzhqyxwh)
    CommonInputView cv_zdzhqyxwh;

    @BindView(R.id.iv_bankCard)
    ImageView iv_bankCard;
    private BankCard mBankCard;
    private DictItemDialog mDictItemFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bankcard_input;
    }

    public void bindNonNull(CommonInputView inputView, String text) {
        if (inputView != null && text != null) {
            inputView.setText(text);
        }
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        mDictItemFragment = new DictItemDialog(this);
        mDictItemFragment.setOnItemSelected(new BottomSelectorDialog.OnItemSelectedListener<DictionaryItem>() {
            @Override
            public void onItemSelected(DictionaryItem item, int extra) {
                cv_paperType.setText(item.getText());
                mBankCard.setPaper(item);
            }
        });
        mBankCard = getSerializable(BankCard.class.getSimpleName());
        if (mBankCard != null) {
            bindNonNull(cv_cardNumber, mBankCard.getBankCode());
            bindNonNull(cv_bank, mBankCard.getBankEnum().getText());
            bindNonNull(cv_userName, mBankCard.getName());
            bindNonNull(cv_bankBranch, mBankCard.getBankAddress());
            bindNonNull(cv_paperNumber, mBankCard.getPaperNumber());
            bindNonNull(cv_zdzhqyxwh, mBankCard.getAssignCode());
            if (mBankCard.getPaper() != null) {
                cv_paperType.setText("已选择");
            }
            if (mBankCard.getCardPhoto() != null) {
                Glide.with(this).load(mBankCard.getCardPhoto().getPhotos()[0]).fitCenter().into(iv_bankCard);
            }
        } else {
            mBankCard = new BankCard();
        }

    }

    private ArrayList<DictionaryItem> items;

    @OnClick({R.id.iv_bankCard, R.id.cv_bank, R.id.cv_paperType, R.id.btn_commit})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.iv_bankCard) {
            toActivity(
                    UploadPhotoActivity.class,
                    JumpUtil.getBundle(RegisterPhoto.class.getSimpleName(), mBankCard.getCardPhoto()),
                    Constant.PHOTO_ADD_REQUEST,
                    null);
        } else if (v.getId() == R.id.btn_commit) {
            if (bindCard()) {
                Intent intent = new Intent();
                intent.putExtra(BankCard.class.getSimpleName(), mBankCard);
                setResult(RESULT_OK, intent);
                finish();
            }
        } else if (v.getId() == R.id.cv_bank) {
            toActivity(
                    CommonWrapFragmentActivity.class,
                    CommonWrapFragmentActivity.getBundle("开户银行", BankListFragment.class),
                    Constant.BANKCARD_REQUEST,
                    null);
        } else if (v.getId() == R.id.cv_paperType) {
            if (items == null) {
                items = new ArrayList<>();
                if (LoginInfo.isCompany() && !LoginInfo.isChildAccount()) {
                    items.add(new DictionaryItem("2", "组织机构代码"));
                    items.add(new DictionaryItem("3", "营业执照(三证合一)"));
                } else {
                    items.add(new DictionaryItem("1", "身份证"));
                }
            }

            mDictItemFragment.showDict("证件资料", "请选择证件类型", items, 1);
//            if (mPaperPhotos == null) {
//                mPaperPhotos = new ArrayList<>();
//                mPaperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{getString(R.string.upload_front_photo), getString(R.string.upload_contrary_photo)}, new int[]{R.drawable.src_identify_front, R.drawable.src_identify_back}, 0.62f), "身份证", 1));
//                PaperPhoto sel = mBankCard.getPaper();
//                if (sel != null) {
//                    for (int index = 0; index < mPaperPhotos.size(); index++) {
//                        if (mPaperPhotos.get(index).getPaperType() == sel.getPaperType()) {
//                            mPaperPhotos.set(index, sel);
//                        }
//                    }
//                }
//            }
            // toActivityForResult(PapersAddActivity.class, Constant.PAPER_REQUEST, JumpUtil.getBundle(PaperPhoto.class.getSimpleName(), mPaperPhotos));
        }
    }

    private boolean bindCard() {
        if (StringUtil.isTrimBlank(cv_cardNumber.getInputValue())) {
            showMessage("请输入银行账号");
            return false;
        } else if (mBankCard.getBankEnum() == null) {
            showMessage("请输入开户银行");
            return false;
        } else if (StringUtil.isTrimBlank(cv_userName.getInputValue())) {
            showMessage("请输入户名");
            return false;
        } else if (StringUtil.isTrimBlank(cv_bankBranch.getInputValue())) {
            showMessage("请输入开户支行");
            return false;
        } else if (mBankCard.getPaper() == null) {
            showMessage("请选择证件类型");
            return false;
        } else if (StringUtil.isTrimBlank(cv_paperNumber.getInputValue())) {
            showMessage("请输入证件号码");
            return false;
        }
//        else if (StringUtil.isTrimBlank(cv_zdzhqyxwh.getInputValue())) {
//            showMessage("请输入席位号");
//            return false;
//        }
        else if (!mBankCard.getCardPhoto().canUpload()) {
            showMessage("请上传银行卡图片");
            return false;
        }
        mBankCard.setName(cv_userName.getInputValue());
        mBankCard.setPaperNumber(cv_paperNumber.getInputValue());
        mBankCard.setAssignCode(cv_zdzhqyxwh.getInputValue());
        mBankCard.setBankCode(cv_cardNumber.getInputValue());
        mBankCard.setBankAddress(cv_bankBranch.getInputValue());
        return true;

    }

    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        if (requestCode == Constant.PHOTO_ADD_REQUEST) {
            RegisterPhoto cardPhoto = (RegisterPhoto) data.getSerializableExtra(RegisterPhoto.class.getSimpleName());
            mBankCard.setCardPhoto(cardPhoto);
            Glide.with(this).load(cardPhoto.getPhotos()[0]).fitCenter().into(iv_bankCard);
        }
//            else if (requestCode == Constant.PAPER_REQUEST) {
//                mPaperPhotos = (ArrayList<PaperPhoto>) data.getSerializableExtra(PaperPhoto.class.getSimpleName());
//                for (PaperPhoto paper : mPaperPhotos) {
//                    if (paper.isDataComplete()) {
//                        mBankCard.setPaper(paper);
//                        break;
//                    }
//                }
//                cv_paperType.setText("已填写");
//            }
        else if (requestCode == Constant.BANKCARD_REQUEST) {
            DictionaryItem bank = (DictionaryItem) data.getSerializableExtra(DictionaryItem.class.getSimpleName());
            cv_bank.setText(bank.getText());
            mBankCard.setBankEnum(bank);
        }
    }

    @Override
    public void success(String message) {
        showMessage(message);
    }
}
