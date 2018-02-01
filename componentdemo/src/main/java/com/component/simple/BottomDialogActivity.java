package com.component.simple;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;

import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.dialog.CommonBottomDialog;
import com.hletong.mob.util.ToastLess;
import com.hletong.mob.widget.ShapeBinder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BottomDialogActivity extends BaseActivity {
    @BindView(R.id.btn_dialog_list)
    Button btn_list;
    @BindView(R.id.btn_dialog_grid)
    Button btn_grid;
    @BindView(R.id.btn_dialog_network)
    Button btn_list_network;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bottom_dialog;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        ShapeBinder.with(ContextCompat.getColor(this, R.color.themeColor)).drawableTo(btn_list);
        ShapeBinder.with(ContextCompat.getColor(this, R.color.green)).drawableTo(btn_grid);
        ShapeBinder.with(ContextCompat.getColor(this, R.color.yellow)).drawableTo(btn_list_network);

    }

    @OnClick({R.id.btn_dialog_grid, R.id.btn_dialog_list, R.id.btn_dialog_network})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (R.id.btn_dialog_grid == v.getId()) {
            new CommonBottomDialog(this).setOnAccessDialogListener(new CommonBottomDialog.OnAccessDialogListener() {

                @Override
                public void initView(View rootView, Bundle savedInstanceState) {

                }

                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.tv_etc) {
                        ToastLess.showToast("申请ETC");
                    } else if (v.getId() == R.id.tv_login) {
                        ToastLess.showToast("登录");
                    } else if (v.getId() == R.id.tv_register) {
                        ToastLess.showToast("注册");
                    }
                }

                @NonNull
                @Override
                public int[] getLayoutAndViewIds() {
                    return new int[]{R.layout.dialog_grid};
                }
            }).show();
        } else if (R.id.btn_dialog_list == v.getId()) {
            new CommonBottomDialog(this).setOnAccessDialogListener(new CommonBottomDialog.OnAccessDialogListener() {

                @Override
                public void initView(View rootView, Bundle savedInstanceState) {

                }

                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.tv_etc) {
                        ToastLess.showToast("申请ETC");
                    } else if (v.getId() == R.id.tv_login) {
                        ToastLess.showToast("登录");
                    } else if (v.getId() == R.id.tv_register) {
                        ToastLess.showToast("注册");
                    }
                }

                @NonNull
                @Override
                public int[] getLayoutAndViewIds() {
                    return new int[]{R.layout.dialog_list, R.id.tv_etc, R.id.tv_login, R.id.tv_register};
                }
            }).show();
        } else if (R.id.btn_dialog_network == v.getId()) {
                new NewWorkDialog(this).show();
        }
    }
}
