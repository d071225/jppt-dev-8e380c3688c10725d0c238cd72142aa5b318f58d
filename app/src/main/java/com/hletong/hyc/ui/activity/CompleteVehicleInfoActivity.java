package com.hletong.hyc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.CompleteVehicleContract;
import com.hletong.hyc.presenter.CompleteVehicleInfoPresenter;
import com.hletong.hyc.util.Validator;
import com.xcheng.view.processbtn.ProcessButton;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CompleteVehicleInfoActivity extends ImageSelectorActivityNew<CompleteVehicleContract.Presenter> implements CompleteVehicleContract.View {

    @BindView(R.id.iv_xsz)
    ImageView iv_xsz;
    @BindView(R.id.iv_jsz)
    ImageView iv_jsz;
    @BindView(R.id.iv_sfz)
    ImageView iv_sfz;
    @BindView(R.id.et_sfzId)
    EditText et_sfzid;//身份证
    @BindView(R.id.et_add_phonenumber)
    EditText et_phoneNum;//联系人电话
    @BindView(R.id.et_maxweight)
    EditText et_maxweight;//车最大重量
    @BindView(R.id.et_carlength)
    EditText et_carlength;//车长
    @BindView(R.id.et_carwidth)
    EditText et_carwidth;//车宽
    @BindView(R.id.et_carheight)
    EditText et_carheight;//车高
    @BindView(R.id.submit)
    ProcessButton processSubmit;
    @Override
    protected ProcessButton getProcessBtn() {
        return processSubmit;
    }
    @Override
    public int getLayoutId() {
        return R.layout.activity_complete_vehicle_info;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        setCustomTitle(R.string.fill_vehicle_information);
    }

    @OnClick({R.id.bl_jsz, R.id.bl_xsz, R.id.bl_sfz, R.id.submit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bl_xsz:
                showSelector(iv_xsz);
                break;
            case R.id.bl_jsz:
                showSelector(iv_jsz);
                break;
            case R.id.bl_sfz:
                showSelector(iv_sfz);
                break;
            case R.id.submit:
                if (invalidate()) {

                }
                break;
        }
    }

    public boolean invalidate() {
        String sfzid = et_sfzid.getText().toString();
        String num = et_phoneNum.getText().toString();
        String weight = et_maxweight.getText().toString().trim();
        String length = et_carlength.getText().toString().trim();
        String width = et_carwidth.getText().toString().trim();
        String height = et_carheight.getText().toString().trim();

        return Validator.isIdCard(sfzid, this)
                && Validator.isPhone(num, this)
                && Validator.isNotNull(weight, this, "车辆最大装载重量不能为空")
                && Validator.isNotNull(length, this, "车辆长度不能为空")
                && Validator.isNotNull(width, this, "车厢宽度不能为空")
                && Validator.isNotNull(height, this, "车厢栏板高度不能为空");
    }

    @Override
    public void showFiles(List<String> mFiles) {

    }

    @Override
    protected CompleteVehicleContract.Presenter createPresenter() {
        return new CompleteVehicleInfoPresenter(this);
    }
}
