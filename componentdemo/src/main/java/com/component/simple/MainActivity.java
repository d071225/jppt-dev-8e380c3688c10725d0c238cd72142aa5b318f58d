package com.component.simple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.widget.ShapeBinder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    @BindView(R.id.btn_shape)
    Button btnCircle;
    @BindView(R.id.btn_defineView)
    Button btnDefineView;
    @BindView(R.id.btn_bottom_Dialog)
    Button btnBottomDialog;
    @BindView(R.id.btn_pickView)
    Button btnPickerView;
    @BindView(R.id.btn_listView)
    Button btnListView;
    @BindView(R.id.btn_http)
    Button btnHttp;
    @BindView(R.id.btn_formSubmit)
    Button btnFormSubmit;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        ShapeBinder.with(this, R.color.colorAccent).drawableStateTo(btnCircle);
        ShapeBinder.with(this, R.color.blue).drawableStateTo(btnDefineView);
        ShapeBinder.with(this, R.color.red_dark).drawableStateTo(btnBottomDialog);
        ShapeBinder.with(this, R.color.yellow).drawableStateTo(btnPickerView);
        ShapeBinder.with(this, R.color.green).drawableStateTo(btnListView);
        ShapeBinder.with(this, R.color.text_blue).drawableStateTo(btnHttp);
        ShapeBinder.with(this, R.color.text_grey).drawableStateTo(btnFormSubmit);

    }

    @OnClick({R.id.btn_shape, R.id.btn_defineView, R.id.btn_bottom_Dialog, R.id.btn_pickView, R.id.btn_listView, R.id.btn_http, R.id.btn_formSubmit})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.btn_shape) {
            toActivity(ShapeActivity.class, null);
        } else if (v.getId() == R.id.btn_defineView) {
            toActivity(CustomViewActivity.class, null);
        } else if (v.getId() == R.id.btn_bottom_Dialog) {
            toActivity(BottomDialogActivity.class, null);
        } else if (v.getId() == R.id.btn_pickView) {
            toActivity(GalleryActivity.class, null);
        } else if (v.getId() == R.id.btn_listView) {
            toActivity(RefreshActivity.class, null);
        } else if (v.getId() == R.id.btn_http) {
            toActivity(HttpActivity.class, null);
        } else if (v.getId() == R.id.btn_formSubmit) {
            toActivity(FormValidatorActivity.class, null);
        }
    }
}
