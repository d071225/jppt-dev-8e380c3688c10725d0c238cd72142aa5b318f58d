package com.hletong.hyc.ui.fragment;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.CBRoundContract;
import com.hletong.mob.base.CountDownHelper;
import com.hletong.mob.HLApplication;
import com.hletong.mob.base.BaseRefreshFragment;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dongdaqing on 2017/7/19.
 */

public class BiddingHallCommon implements CountDownHelper.CountDownListener, CompetitiveBiddingDialogFragment.MoneyChanged {
    @BindView(R.id.des)
    TextView textView;
    @BindView(R.id.modify_price)
    View mp;
    @BindView(R.id.bt)
    TextView bt;

    private CompetitiveBiddingDialogFragment cb_dialog;
    private CountDownHelper helper;
    private CBRoundContract.Presenter mPresenter;
    private BaseRefreshFragment mFragment;

    public BiddingHallCommon(CBRoundContract.Presenter presenter, BaseRefreshFragment fragment, View view) {
        mPresenter = presenter;
        mFragment = fragment;
        ButterKnife.bind(this, view);
    }

    public void onViewCreated() {
        //下面的getBundleParams()有参数的说明
        boolean needSourceDetail = mFragment.getArguments().getBoolean("needSourceDetail", false);
        if (needSourceDetail) {
            //根据竞价ID获取货源详情
            mPresenter.loadCargoDetails(mFragment.getArguments().getString("bidUuid"));
        } else {
            //加载当前场次剩余时间和参与竞价的信息
            mPresenter.loadTime(mFragment.getArguments().getString("roundUUID"));
        }
    }

    @Override
    public void onMoneyChanged(String money) {
        mPresenter.modifyBidPrice(money, mFragment.getArguments().getString("bidUuid"));
    }

    //倒计时中
    @Override
    public void onTicking(long hour, long minute, long second) {
        final String s = textView.getTag() + "，" + String.format(Locale.CHINESE, mFragment.getString(R.string.count_down), CountDownHelper.build(hour, minute, second));
        textView.setText(s);
    }

    //倒计时结束
    @Override
    public void countFinished() {
        textView.setText(R.string.count_down_finished);
    }

    public void timeLoaded(long end, String remain) {
        //只有竞价中的货源才能修改竞价价格
        if (getArguments().getBoolean("canModifyBidPrice", false)) {
            mp.setVisibility(View.VISIBLE);
        } else {
            bt.setVisibility(View.VISIBLE);
            final String bidUuid = getArguments().getString("bidUuid");
            if (bidUuid != null) {//参与过竞价
                bt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                bt.setText(String.format(Locale.CHINESE, "您的竞价价格为：%s", getBidPriceWithUnit()));
            } else {//未参与过竞价
                bt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
                bt.setText(R.string.hint_bid_list);
            }
        }

        textView.setVisibility(View.VISIBLE);//默认不可见
        textView.setTag(String.format(Locale.CHINESE, mFragment.getString(R.string.available_cargo), (remain + getArguments().getString("unit"))));
        //开始倒计时
        if (helper == null) {
            helper = new CountDownHelper(this, HLApplication.getDelta());
        }
        helper.startCounting(end);
    }

    public void cargoLoaded(String roundUUID, String bidPrice, boolean canModifyBidPrice) {
        //设置能否修改竞价价格(货源状态是竞价中才能修改竞价价格，其它状态是不能修改的)，后面会用到这个参数
        getArguments().putBoolean("canModifyBidPrice", canModifyBidPrice);
        getArguments().putString("bidPrice", bidPrice);
        getArguments().putString("roundUUID", roundUUID);//修改竞价价格之后需要重新获取竞价信息，通过这个字段
        //加载当前场次剩余时间和参与竞价的信息
        mPresenter.loadTime(roundUUID);
    }

    private String getBidPriceWithUnit() {
        final String price = getArguments().getString("bidPrice");
        final String unit = getArguments().getString("unit");
        return price + "元/" + unit;
    }

    //竞价价格修改成功
    public void bidPriceChanged(String bidPrice) {
        getArguments().putString("bidPrice", bidPrice);//更新存储的竞价价格
        mFragment.showMessage("修改竞价价格成功！");
        //加载当前场次剩余时间和参与竞价的信息
        mPresenter.loadTime(getArguments().getString("roundUUID"));
    }

    private Bundle getArguments() {
        return mFragment.getArguments();
    }

    public void onPause() {
        if (helper != null)
            helper.onPause();
    }

    public void onResume() {
        if (helper != null)
            helper.onResume();
    }

    @OnClick({R.id.modify_price})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.modify_price: {
                if (cb_dialog == null) {
                    cb_dialog = new CompetitiveBiddingDialogFragment();
                    cb_dialog.setMc(this);
                }
                cb_dialog.setBidPrice(getBidPriceWithUnit());
                cb_dialog.show(mFragment.getActivity().getFragmentManager(), "cb_dialog");
                break;
            }
        }
    }
}
