package com.hletong.hyc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.TransportContract;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.ForecastTransporterInfo;
import com.hletong.hyc.model.TransporterBase;
import com.hletong.hyc.model.validate.Transport;
import com.hletong.hyc.presenter.YunLiPresenter;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.ui.widget.DatePickerView;
import com.hletong.hyc.ui.widget.RatingBar;
import com.hletong.mob.util.SimpleDate;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/1/4.
 * 运力预报基类
 */

public abstract class BaseForecastActivity<T extends TransporterBase> extends TransporterSelectorActivity<T, TransportContract.Presenter> implements TransportContract.View {

    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.transporter)
    CommonInputView mTransporter;
    @BindView(R.id.load)
    CommonInputView mLoad;
    @BindView(R.id.source)
    CommonInputView mSource;
    @BindView(R.id.destination)
    CommonInputView mDestination;
    @BindView(R.id.loadTM)
    DatePickerView mLoadTM;
    @BindView(R.id.phone)
    CommonInputView mPhone;
    @BindView(R.id.spare)
    CommonInputView mSpare;
    @BindView(R.id.carrier)
    TextView carrier;
    @BindView(R.id.favor_rate)
    TextView favor_rate;
    @BindView(R.id.trade_ct)
    TextView trade_ct;
    @BindView(R.id.rate)
    RatingBar rate;

    @Override
    public boolean isSupportActionBar() {
        return false;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        mTransporter.setLabel(BuildConfig.transporter_label);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPresenter().start();
    }

    @Override
    public int getLayoutId() {
        if (getParcelable("data") == null)
            return R.layout.activity_yunlifabu;
        return R.layout.activity_yunlifabu_view;
    }

    @Override
    protected CommonInputView getTransporterLabel() {
        return mTransporter;
    }

    @Override
    public void itemSelected(T item, int extra) {
        //用户选择了车辆(船舶)
        rate.setVisibility(View.VISIBLE);
        mPhone.setText(item.getMember_tel());
        carrier.setText(item.getValue());
        rate.setProgress(item.getMember_grade());
        favor_rate.setText(item.getMemberGrade());
        trade_ct.setText(item.getTradeCount());
    }

    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        switch (requestCode) {
            case 99:
            case 100: {
                Address address = Address.getAddress(data);
                final CommonInputView[] views = {mSource, mDestination};
                views[requestCode - 99].setTag(address);
                views[requestCode - 99].setText(address.buildAddress());
                break;
            }
        }
    }

    @OnClick(R.id.submit)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit: {//提交信息
                getPresenter().submit(getTransportInfo(
                        (Address) mSource.getTag(),
                        (Address) mDestination.getTag(),
                        mLoadTM.getStart(),
                        mLoadTM.getEnd(),
                        mLoad.getInputValue(),
                        mSpare.getInputValue()));
                break;
            }
            case R.id.source: {
                Bundle bundle = new Bundle();
                bundle.putString("title", getString(R.string.select_start_address));
                bundle.putInt("address_extra", 4);//能选择全市，不能选全国全省
                toActivity(AddressSelectorActivity.class, bundle, 99, null);
                break;
            }
            case R.id.destination: {
                Bundle bundle = new Bundle();
                bundle.putString("title", getString(R.string.select_destination_address));
                bundle.putInt("address_extra", 4);//能选择全市，不能选全国全省
                toActivity(AddressSelectorActivity.class, bundle, 100, null);
                break;
            }
        }
    }

    protected abstract Transport getTransportInfo(Address startLoc, Address endLoc, SimpleDate startDT, SimpleDate endDT, String capacity, String backup);

    @Override
    protected TransportContract.Presenter createPresenter() {
        return new YunLiPresenter(this, (ForecastTransporterInfo) getParcelable("data"));
    }

    @Override
    public void showTitle(String title) {
        setCustomTitle(title);
    }

    @Override
    public void inflateMenu(int menu, Toolbar.OnMenuItemClickListener listener) {
        getToolbar().inflateMenu(menu);
        getToolbar().setOnMenuItemClickListener(listener);
    }

    @Override
    public void setAddressClickListener() {
        mSource.setOnClickListener(this);
        mDestination.setOnClickListener(this);
    }

    @Override
    public void showAddress(Address startAddress, Address endAddress) {
        mSource.setText(startAddress.buildAddress());
        mDestination.setText(endAddress.buildAddress());
    }

    @Override
    public void showAvailableTime(String from, String to) {
        mLoadTM.setView(true);//设置为只看模式
        mLoadTM.setStart(from);
        mLoadTM.setEnd(to);
    }

    @Override
    public void showPlate(String plate) {
        mTransporter.setText(plate);
    }

    @Override
    public void showLoad(String load) {
        mLoad.setText(load);
    }

    @Override
    public void showSpare(String spare) {
        mSpare.setText(spare);
    }

    @Override
    public void hideSpare() {
        mSpare.setVisibility(View.GONE);
    }

    @Override
    public void prefetchTransporter() {
        prefetch();//预加载车辆（船舶）列表
    }
}
