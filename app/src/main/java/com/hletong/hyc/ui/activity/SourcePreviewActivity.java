package com.hletong.hyc.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.SourcePreviewContract;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.presenter.SourcePreviewPresenter;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.mob.gallery.builder.PreViewBuilder;
import com.hletong.mob.gallery.widget.PickerRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ddq on 2017/3/21.
 * 货源预报-货源预览
 */

public class SourcePreviewActivity extends ImageSelectorActivityNew<SourcePreviewContract.Presenter> implements SourcePreviewContract.View {
    @BindView(R.id.special_req)
    TextView specialReq;
    @BindView(R.id.cargoName)
    TextView cargoName;
    @BindView(R.id.cargoDesc)
    TextView cargoDesc;
    @BindView(R.id.from)
    CommonInputView from;
    @BindView(R.id.to)
    CommonInputView to;
    @BindView(R.id.shipper)
    CommonInputView shipper;
    @BindView(R.id.receiver)
    CommonInputView receiver;
    @BindView(R.id.loading_dt)
    CommonInputView loadingDt;
    @BindView(R.id.cargo_value)
    CommonInputView cargoValue;
    @BindView(R.id.volume)
    CommonInputView volume;
    @BindView(R.id.measure_type)
    CommonInputView measureType;
    @BindView(R.id.ct)
    CommonInputView ct;
    @BindView(R.id.weight)
    CommonInputView weight;
    @BindView(R.id.charge_type)
    CommonInputView chargeType;
    @BindView(R.id.freight_price)
    CommonInputView freightPrice;
    @BindView(R.id.loading_tm)
    CommonInputView loadingTm;
    @BindView(R.id.transportType)
    CommonInputView transportType;
    @BindView(R.id.carrier_model)
    CommonInputView carrierModel;
    @BindView(R.id.carrierLength)
    CommonInputView carrierLength;
    @BindView(R.id.multi_transport)
    CommonInputView multiTransport;
    @BindView(R.id.settleType)
    CommonInputView settleType;
    @BindView(R.id.settleAuth)
    CommonInputView settleAuth;
    @BindView(R.id.billingType)
    CommonInputView billingType;
    @BindView(R.id.appoint)
    CommonInputView appoint;
    @BindView(R.id.gallery)
    PickerRecyclerView gallery;
    @BindView(R.id.scrollView)
    View scrollView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_source_preview;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        setCustomTitle(getIntent().getStringExtra("title"));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPresenter().start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.revoke, menu);
        return getIntent().getBooleanExtra("revoke", false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DialogFactory.showAlertWithNegativeButton(this, "撤销货源", "确认撤销该货源预报吗？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                getPresenter().revoke();
            }
        });
        return true;
    }

    @Override
    public void showFiles(List<String> mFiles) {
        gallery.setVisibility(View.VISIBLE);
        new PickerRecyclerView.Builder(this).selectedPhotos(new ArrayList<>(mFiles)).action(PreViewBuilder.PREVIEW).build(gallery);
    }

    @Override
    public void success(String message) {
        showMessage(message);
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void initWithSource(Source source) {
        scrollView.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(source.getSpecialRequest())) {
            specialReq.setVisibility(View.VISIBLE);
            specialReq.setText(source.getSpecialRequest());
        }

        cargoName.setText(source.getOrgin_cargon_kind_name());//货物名称
        cargoDesc.setText(source.getCargoDescWithUnit());//货物描述
        from.setText(source.getLoadingAddress());//装货地
        to.setText(source.getUnLoadingAddress());//卸货地
//        shipper.setText();
//        shipper.setText();
        final String s = source.getLoadingPeriod();//
        loadingDt.setText("装货日期：" + (s == null ? "" : s));//装货日期
        cargoValue.setText(source.getCargoPriceInThousands());//货物价值
        volume.setText(source.getVolume("，"));//长宽高
        measureType.setText(source.getMeasure_type());//计量方式
        ct.setText(source.getCargoNumbersWithUnit());//数量
        weight.setText(source.getCargoWeightWithUnit());//重量
        chargeType.setText(source.getBook_ref_type_());//计费依据
        freightPrice.setText(source.getOrignUnitAmtWithUnit());//单价
        loadingTm.setText(source.getTimePeriod("-"));//装货时间
        transportType.setText(source.getTransportType());//运输方式
        if (source.getTransport_type() == 2) {
            carrierModel.setLabel("船型要求");
            carrierLength.setVisibility(View.GONE);
            multiTransport.setLabel("整船运输");
        }
        carrierModel.setText(source.getCarrier_model_type_());//车型要求
        carrierLength.setText(source.getCarrier_length_type_());//车长要求
        multiTransport.setText(source.getMultiTransportFlag());//整车运输
        settleType.setText(source.getSettle_type());//结算方式
        settleAuth.setText(source.getSettle_auth_());//结算权限
        billingType.setText(source.getChargeReferType());//开票方式
        appoint.setText(source.getAppoint_transport_flag_());//指定运输

        getPresenter().download(source.getCargoFileId());
//        downloadImage(source.getCargoFileId());
    }

    @Override
    protected SourcePreviewContract.Presenter createPresenter() {
        return new SourcePreviewPresenter(this, (Source) getParcelable("source"), getIntent().getStringExtra("cargoUuid"));
    }
}
