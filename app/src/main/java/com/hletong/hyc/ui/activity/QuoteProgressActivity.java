package com.hletong.hyc.ui.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.QuoteContract;
import com.hletong.hyc.model.QuoteInfos;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.presenter.QuoteBasicPresenter;
import com.hletong.hyc.ui.fragment.DirectQuoteFragment;
import com.hletong.hyc.ui.fragment.QuoteProgressFragment;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.mob.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dongdaqing on 2017/5/31.
 * 待办消息-货方查看议价进度
 */

public class QuoteProgressActivity extends BaseActivity implements QuoteContract.BasicView {
    @BindView(R.id.special_req)
    TextView specialReq;
    @BindView(R.id.cargoName)
    TextView cargoName;
    @BindView(R.id.cargoDesc)
    TextView cargoDesc;
    @BindView(R.id.startAddress)
    CommonInputView startAddress;
    @BindView(R.id.endAddress)
    CommonInputView endAddress;
    @BindView(R.id.model_req)
    CommonInputView modelReq;
    @BindView(R.id.loading_date)
    CommonInputView loadingDate;
    //    @BindView(R.id.bus_doc_no)
//    CommonInputView busDocNo;
//    @BindView(R.id.volume)
//    CommonInputView volume;
    @BindView(R.id.p1)
    View p1;
//    @BindView(R.id.p2)
//    View p2;

    private QuoteContract.BasicPresenter mPresenter;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPresenter.loadDetails();
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_quote_progress;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        mPresenter = new QuoteBasicPresenter(getIntent().getStringExtra("cargoUuid"), this);
        mPresenter.loadDetails();
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction("quote_state_changed");
        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onStop() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiver);
        super.onStop();
    }

    @Override
    public void initWithData(Source source) {
        p1.setVisibility(View.VISIBLE);
//        p2.setVisibility(View.VISIBLE);

        cargoName.setText(source.getOrgin_cargon_kind_name());//名称
        cargoDesc.setText(source.getCargoDescWithUnit());//数量
        startAddress.setText(source.getLoadingAddress());//出发地
        endAddress.setText(source.getUnLoadingAddress());//目的地
        if (source.getTransport_type() == 1) {
            modelReq.setText("车辆类型：" + source.getTransporterRequests());//车辆(船舶)需求
            modelReq.setLabelDrawable(R.drawable.truck_model);
        } else {
            modelReq.setText("船舶类型：" + source.getTransporterRequests());//车辆(船舶)需求
            modelReq.setLabelDrawable(R.drawable.ship_model);
        }
        loadingDate.setText("装货日期：" + source.getLoadingPeriod());//装货日期
//        volume.setText(source.getVolume("/"));//
//        busDocNo.setText(source.getCargo_code());//业务单据号
    }

    @Override
    public void initList(QuoteInfos quoteInfos, boolean guolian) {
        String FRAGMENT_TAG = "quote_fragment";
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(FRAGMENT_TAG);
        if (fragment != null) {
            if (fragment instanceof QuoteProgressFragment) {
                QuoteProgressFragment fr = (QuoteProgressFragment) fragment;
                fr.update(quoteInfos.getQuoteList());
            }
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString("volume", quoteInfos.getCargoMap().getVolume("/"));
        bundle.putString("doc", quoteInfos.getCargoMap().getCargo_code());
        bundle.putString("file", quoteInfos.getCargoMap().getCargoFileId());
        String name;
        if (guolian) {
            //国联议价
            name = DirectQuoteFragment.class.getName();
            bundle.putString("cargoUuid", quoteInfos.getCargoMap().getCargo_uuid());
        } else {
            //正常议价
            name = QuoteProgressFragment.class.getName();
            bundle.putInt("bookRefType", quoteInfos.getCargoMap().getBook_ref_type());
            bundle.putString("unit", quoteInfos.getCargoMap().getCargoUnit());
            bundle.putParcelableArrayList("list", new ArrayList<>(quoteInfos.getQuoteList()));
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, Fragment.instantiate(this, name, bundle), FRAGMENT_TAG);
        transaction.commit();
    }
}
