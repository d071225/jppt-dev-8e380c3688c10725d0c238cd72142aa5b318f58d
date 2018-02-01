package com.component.simple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.validator.SimpleValidatorListener;
import com.hletong.mob.validator.Validator;
import com.hletong.mob.widget.ShapeBinder;
import com.orhanobut.logger.Logger;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FormValidatorActivity extends BaseActivity {
    @BindView(R.id.cv_truckCode)
    CommonInputView cv_truckCode;

    @BindView(R.id.cv_truckLength)
    CommonInputView cv_truckLength;

    @BindView(R.id.cv_vehicleMass)
    CommonInputView cv_vehicleMass;

    @BindView(R.id.cv_loadedVehicleQuality)
    CommonInputView cv_et_loadedVehicleQuality;

    @BindView(R.id.cv_load_ton)
    CommonInputView cv_et_load_ton;

    @BindView(R.id.cv_qualification_certificate)
    CommonInputView cv_qualification_certificate;

    @BindView(R.id.cv_road_transport_certificate)
    CommonInputView cv_road_transport_certificate;

    @BindView(R.id.tv_formMap)
    TextView tvFormMap;


    @BindView(R.id.btn_validator)
    Button btnValidator;
    Validator validator;

    @Override
    public int getLayoutId() {
        return R.layout.activity_formvalidator2;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        ShapeBinder.with(this, R.color.blue).drawableStateTo(btnValidator);
        validator = Validator.create(this);
        validator.setValidatorAll(false);
        validator.setOnValidatorListener(new SimpleValidatorListener() {

            @Override
            public void onFormSuccess(Map<String, String> formMap) {
                Logger.d(formMap);
            }
        });
    }

    @OnClick(R.id.btn_validator)
    @Override
    public void onClick(View v) {
        super.onClick(v);
        validator.validate();

    }

}
