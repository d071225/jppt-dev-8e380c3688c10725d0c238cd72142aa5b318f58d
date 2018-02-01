package com.hletong.hyc.ui.activity;

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
import com.hletong.hyc.contract.InfoCompleteContract;
import com.hletong.hyc.model.RegisterPhoto;
import com.hletong.hyc.model.validate.Paperwork;
import com.hletong.hyc.presenter.InfoCompletePresenter;
import com.hletong.hyc.ui.activity.UploadPhotoActivity;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.util.DatePickerUtil;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.util.SimpleDate;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 货主版个人信息补全
 * Created by chengxin on 2017/6/12.
 */
public class CargoPersonCompleteInfoActivity extends BaseActivity implements InfoCompleteContract.View {
    @BindView(R.id.identify_photo)
    ImageView identifyPhoto;

    @BindView(R.id.btn_commit)
    Button btnCommit;

    @BindView(R.id.tv_endDate_select)
    TextView endDate;

    private RegisterPhoto completeIdentifyPhoto;

    private InfoCompleteContract.Presenter mPresenter;
    private DatePickerUtil mDatePickerUtil;
    private SimpleDate mSimpleDate;

    @Override
    public int getLayoutId() {
        return R.layout.activity_cargo_personal_complete_info;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        completeIdentifyPhoto = new RegisterPhoto(new String[]{getString(R.string.upload_front_photo), getString(R.string.upload_contrary_photo)}, new int[]{R.drawable.src_identify_front, R.drawable.src_identify_back}, 0.62f);
        mPresenter = new InfoCompletePresenter(this);
    }

    @OnClick({R.id.identify_photo, R.id.btn_commit, R.id.tv_endDate_select})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (R.id.identify_photo == v.getId()) {
            Intent intent = new Intent(getContext(), UploadPhotoActivity.class);
            intent.putExtra(RegisterPhoto.class.getSimpleName(), completeIdentifyPhoto);
            startActivityForResult(intent, 10);
        } else if (R.id.btn_commit == v.getId()) {
            mPresenter.submit(new Paperwork("8", completeIdentifyPhoto, mSimpleDate == null ? null : mSimpleDate.dateString(true, "")));
        } else if (R.id.tv_endDate_select == v.getId()) {
            if (mDatePickerUtil == null) {
                mDatePickerUtil = new DatePickerUtil(this, new DatePickerUtil.OnDateSetListener() {
                    @Override
                    public void onDateSet(int year, int monthOfYear, int dayOfMonth, int tag) {
                        if (mSimpleDate == null)
                            mSimpleDate = new SimpleDate();
                        mSimpleDate.setDate(year, monthOfYear, dayOfMonth);
                        endDate.setText(mSimpleDate.dateString(true, "-"));
                    }
                }, Calendar.getInstance());
            }
            mDatePickerUtil.showDatePicker(getFragmentManager(), -1);
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
        switch (requestCode) {
            case 10:
                completeIdentifyPhoto = (RegisterPhoto) data.getSerializableExtra(RegisterPhoto.class.getSimpleName());
                Glide.with(this).load(completeIdentifyPhoto.getPhotos()[0]).fitCenter().into(identifyPhoto);
                break;
        }
    }
}
