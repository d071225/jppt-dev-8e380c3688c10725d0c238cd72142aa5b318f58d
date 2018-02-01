package com.hletong.hyc.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatCheckBox;
import android.text.Editable;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.VehicleInfoContract;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.EtcApplyInfo;
import com.hletong.hyc.model.RegisterPhoto;
import com.hletong.hyc.model.validate.EtcTransporterInfoPart2;
import com.hletong.hyc.presenter.VehiclePresenter;
import com.hletong.hyc.ui.dialog.DictItemDialog;
import com.hletong.hyc.util.DatePickerUtil;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.hyc.util.SimpleTextWatcher;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.dialog.selector.BottomSelectorDialog;
import com.hletong.mob.util.SimpleDate;
import com.hletong.mob.widget.BorderLinearLayout;
import com.xcheng.view.util.JumpUtil;
import com.xcheng.view.widget.CommonView;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BasicInfoActivity extends BaseActivity implements DatePickerUtil.OnDateSetListener, BottomSelectorDialog.OnItemSelectedListener<DictionaryItem>, VehicleInfoContract.View {

    private DatePickerUtil dataPicker;
    @BindView(R.id.selected_card)
    TextView selected;
    @BindView(R.id.chinese_name)
    EditText name;
    @BindView(R.id.pinying_name)
    EditText spell;
    @BindView(R.id.identify_card)
    TextView identify_card_type;
    @BindView(R.id.certificate_num)
    EditText id_no;
    @BindView(R.id.validity)
    TextView validity;
    @BindView(R.id.validDate)
    TextView validDate;
    @BindView(R.id.sex)
    TextView sex;
    @BindView(R.id.tv_birth)
    TextView tv_birth;
    @BindView(R.id.tv_PhoneNumber)
    EditText tv_PhoneNumber;
    @BindView(R.id.cb)
    AppCompatCheckBox cb;
    @BindView(R.id.expire_date)
    BorderLinearLayout expire_date;
    @BindView(R.id.cv_introduce)
    CommonView cvIntroduce;
    @BindView(R.id.submit)
    View submit;

    private VehicleInfoContract.Presenter mPresenter;
    private DictItemDialog mDictItemFragment;

    @Override
    public int getLayoutId() {
        return R.layout.activity_basic_info;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setCustomTitle(R.string.baseInfo);
        ButterKnife.bind(this);

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                submit.setEnabled(isChecked);
            }
        });

        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();
            }
        });

        id_no.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mPresenter.identityChanged(s.toString().trim());//更新个人信息
            }
        });

        name.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                mPresenter.nameChanged(s.toString().trim());
            }
        });

        mPresenter = new VehiclePresenter(this, (EtcApplyInfo) getParcelable("vehicleInfo"));
        mPresenter.start();
    }

    @OnClick({R.id.validDate, R.id.submit, R.id.validity, R.id.sex, R.id.tv_birth, R.id.identify_card, R.id.cv_introduce})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit: {
                RegisterPhoto registerPhoto = cvIntroduce.getResult();
                String recommendation = registerPhoto != null ? registerPhoto.getFileGroupId() : null;
                mPresenter.submit(new EtcTransporterInfoPart2(
                        name.getText().toString().trim(),
                        spell.getText().toString(),
                        (DictionaryItem) identify_card_type.getTag(),
                        id_no.getText().toString().trim(),
                        (DictionaryItem) validity.getTag(),
                        (SimpleDate) validDate.getTag(),
                        (DictionaryItem) sex.getTag(),
                        (SimpleDate) tv_birth.getTag(),
                        tv_PhoneNumber.getText().toString().trim(),
                        recommendation
                ));
            }

            break;
            case R.id.validity:
                mPresenter.prepareDataForSelector(VehicleInfoContract.TYPE_VALIDITY, v.getId());
                break;
            case R.id.sex:
                mPresenter.prepareDataForSelector(VehicleInfoContract.TYPE_SEX, v.getId());
                break;
            case R.id.identify_card:
                mPresenter.prepareDataForSelector(VehicleInfoContract.TYPE_IDENTITY, v.getId());
                break;
            case R.id.tv_birth:
                showDatePicker(v.getId());
                break;
            case R.id.validDate:
                showDatePicker(v.getId());
                break;
            case R.id.cv_introduce:
                RegisterPhoto registerPhoto = cvIntroduce.getResult();
                if (registerPhoto == null) {
                    registerPhoto = new RegisterPhoto(new String[]{"请上传介绍信照片"}, 0.67f);
                    cvIntroduce.setResult(registerPhoto);
                }
                toActivity(UploadPhotoActivity.class, JumpUtil.getBundle(RegisterPhoto.class.getSimpleName(), registerPhoto), REQUEST_CODE_INTRODUCE, null);
                break;
        }
    }

    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        super.onActivityResultOk(requestCode, data);
        if (requestCode == REQUEST_CODE_INTRODUCE) {
            cvIntroduce.setResult(JumpUtil.getSerializable(data, RegisterPhoto.class.getSimpleName()));
            cvIntroduce.setText("已上传");
        }
    }

    public static final int REQUEST_CODE_INTRODUCE = 1;

    private void showDatePicker(int tag) {
        if (dataPicker == null) {
            Calendar calendar = Calendar.getInstance();
            dataPicker = new DatePickerUtil(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        }
        dataPicker.showDatePicker(getFragmentManager(), tag);
    }

    @Override
    public void onDateSet(int year, int monthOfYear, int dayOfMonth, int tag) {
        TextView textView = (TextView) findViewById(tag);
        SimpleDate simpleDate = (SimpleDate) textView.getTag();
        if (simpleDate == null) {
            simpleDate = new SimpleDate();
            textView.setTag(simpleDate);
        }
        simpleDate.setDate(year, monthOfYear, dayOfMonth);
        textView.setText(simpleDate.dateString());
    }

    @Override
    public void initBasic(String name, String phone, String cardName, String plate) {
        //  this.name.setText(name);
        this.tv_PhoneNumber.setText(phone);
        this.selected.setText(String.format(getString(R.string.selected_card), cardName, plate));
    }

    @Override
    public void initValidity(DictionaryItem validity, SimpleDate expireDate) {
        this.validity.setText(validity.getText());
        this.validity.setTag(validity);

        if ("0".equals(validity.getId()))
            expire_date.setVisibility(View.VISIBLE);

        if (expireDate != null) {
//            validDate.setText(expireDate.dateString());
//            validDate.setTag(expireDate);
        }
    }

    @Override
    public void initIdentityNo(DictionaryItem type, String id_no) {
        this.identify_card_type.setText(type.getText());
        this.identify_card_type.setTag(type);

        this.id_no.setText(id_no);
    }

    @Override
    public void initPersonalInfo(DictionaryItem sex, SimpleDate birthday) {
        this.sex.setText(sex.getText());
        this.sex.setTag(sex);

        if (birthday != null) {
            this.tv_birth.setText(birthday.dateString());
            this.tv_birth.setTag(birthday);
        }
    }

    @Override
    public void initNameSpell(String spell) {
        this.spell.setText(spell);
    }

    @Override
    public void onSuccess(String url) {
        setResult(RESULT_OK);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url.trim()));
        startActivity(intent);
        finish();

    }

    @Override
    public void showSelector(ArrayList<DictionaryItem> items, String type, String title, int extra) {
        if (mDictItemFragment == null) {
            mDictItemFragment = new DictItemDialog(this);
            mDictItemFragment.setOnItemSelected(this);
        }
        mDictItemFragment.showDict(type, title, items, extra);
    }

    @Override
    public void onItemSelected(DictionaryItem item, int extra) {
        TextView view = findViewById(extra);
        view.setTag(item);
        view.setText(item.getText());

        if (extra == R.id.validity) {//证件非长期有效 -> 要填写证件到期日期
            expire_date.setVisibility("0".equals(item.getId()) ? View.VISIBLE : View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        DialogFactory.showAlertWithNegativeButton(this, R.string.exit, R.string.etc_hint_exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
    }
}
