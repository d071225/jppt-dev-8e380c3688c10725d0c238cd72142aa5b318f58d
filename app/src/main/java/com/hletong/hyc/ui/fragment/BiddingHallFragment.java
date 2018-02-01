package com.hletong.hyc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.BiddingHallAdapter;
import com.hletong.hyc.contract.CBRoundContract;
import com.hletong.hyc.model.CBHistoryItem;
import com.hletong.hyc.model.CBRoundItem;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.presenter.BiddingPresenter;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.DividerItemDecoration;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

/**
 * Created by ddq on 2017/1/4.
 * 竞价大厅(非平开)
 */

public class BiddingHallFragment extends BaseRefreshFragment<CBRoundItem> implements CBRoundContract.View<CBRoundItem> {
//    @BindView(R.id.des)
//    TextView textView;
//    @BindView(R.id.modify_price)
//    View mp;
//    @BindView(R.id.bt)
//    TextView bt;
//
//    private CompetitiveBiddingDialogFragment cb_dialog;
//    private CountDownHelper helper;
//    private CBRoundContract.Presenter mPresenter;

    //    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mPresenter = new BiddingPresenter(this);
//    }
    private BiddingHallCommon mCommon;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mCommon = new BiddingHallCommon(new BiddingPresenter(this), this, view);
        mCommon.onViewCreated();
//        ButterKnife.bind(this, view);
//
//        //下面的getBundleParams()有参数的说明
//        boolean needSourceDetail = getArguments().getBoolean("needSourceDetail", false);
//        if (needSourceDetail) {
//            //根据竞价ID获取货源详情
//            mPresenter.loadCargoDetails(getArguments().getString("bidUuid"));
//        } else {
//            //加载当前场次剩余时间和参与竞价的信息
//            mPresenter.loadTime(getArguments().getString("roundUUID"));
//        }
    }

    @Override
    protected boolean isAutoLoad() {
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_bidding_hall;
    }

    @Override
    protected String getRequestUrl() {
        return null;//不用返回，这里继承BaseRefreshFragmentNew，是为了使用里面的初始化方法，列表不是通过这个加载的
    }


//    @Override
//    public void onMoneyChanged(String money) {
//        mPresenter.modifyBidPrice(money, getArguments().getString("bidUuid"));
//    }

//    //倒计时中
//    @Override
//    public void onTicking(long hour, long minute, long second) {
//        final String s = textView.getTag() + "，" + String.format(Locale.CHINESE, getString(R.string.count_down), CountDownHelper.build(hour, minute, second));
//        textView.setText(s);
//    }
//
//    //倒计时结束
//    @Override
//    public void countFinished() {
//        textView.setText(R.string.count_down_finished);
//    }

    @NonNull
    @Override
    public HFRecyclerAdapter<CBRoundItem> createAdapter() {
        BiddingHallAdapter adapter = new BiddingHallAdapter(getContext(), null, Integer.MAX_VALUE, getArguments().getString("unit"), getArguments().getInt("bookRefType", -1));
        adapter.hideFooter();
        return adapter;
    }

//    @OnClick({R.id.modify_price})
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.modify_price: {
//                if (cb_dialog == null) {
//                    cb_dialog = new CompetitiveBiddingDialogFragment();
//                    cb_dialog.setMc(this);
//                }
//                cb_dialog.setBidPrice(getBidPriceWithUnit());
//                cb_dialog.showBlock(getActivity().getFragmentManager(), "cb_dialog");
//                break;
//            }
//        }
//    }

    @Override
    public void onPause() {
        super.onPause();
        mCommon.onPause();
//        if (helper != null)
//            helper.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mCommon.onResume();
//        if (helper != null)
//            helper.onResume();
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    protected boolean onlyView() {
        return true;
    }

    @Override
    public void timeLoaded(long end, String remain) {
        mCommon.timeLoaded(end, remain);
//        //只有竞价中的货源才能修改竞价价格
//        if (getArguments().getBoolean("canModifyBidPrice", false)) {
//            mp.setVisibility(View.VISIBLE);
//        } else {
//            bt.setVisibility(View.VISIBLE);
//            final String bidUuid = getArguments().getString("bidUuid");
//            if (bidUuid != null) {//参与过竞价
//                bt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
//                bt.setText(String.format(Locale.CHINESE, "您的竞价价格为：%s", getBidPriceWithUnit()));
//            } else {//未参与过竞价
//                bt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
//                bt.setText(R.string.hint_bid_list);
//            }
//        }
//
//        textView.setVisibility(View.VISIBLE);//默认不可见
//        textView.setTag(String.format(Locale.CHINESE, getString(R.string.available_cargo), (remain + getArguments().getString("unit"))));
//        //开始倒计时
//        if (helper == null) {
//            helper = new CountDownHelper(this, HLApplication.getDelta());
//        }
//        helper.startCounting(end);
    }

    @Override
    public void cargoLoaded(String roundUUID, String bidPrice, boolean canModifyBidPrice) {
        mCommon.cargoLoaded(roundUUID, bidPrice, canModifyBidPrice);
//        //设置能否修改竞价价格(货源状态是竞价中才能修改竞价价格，其它状态是不能修改的)，后面会用到这个参数
//        getArguments().putBoolean("canModifyBidPrice", canModifyBidPrice);
//        getArguments().putString("bidPrice", bidPrice);
//        getArguments().putString("roundUUID", roundUUID);//修改竞价价格之后需要重新获取竞价信息，通过这个字段
//        //加载当前场次剩余时间和参与竞价的信息
//        mPresenter.loadTime(roundUUID);
    }

    /**
     * 进入竞价大厅的入口有很多：货源详情，竞价摘牌界面，竞价中界面，竞价历史界面
     * <p>
     * 1.货源详情界面调用这个函数上面的Source具体类型是CBHistoryItem.class，不需要重新请求货源详情（因为已经请求过了）
     * 2.竞价摘牌界面调用这个函数上面的Source具体类型可能是Source.class（普通的进入摘牌界面）也可能是CBHistoryItem.class（通过货源详情界面再次竞价按钮进入摘牌界面）：
     * 1.如果是点击menu进入这个界面只能看竞价场次信息，不能做其他任何操作
     * 2.如果是摘牌成功之后进入这个界面，由于这里没有bidUuid，不能获取货源详情，进来之后只能看竞价信息，不能修改竞价价格，要修改竞价价格只能通过竞价中进来
     * 3.竞价中，竞价历史界面调用这个函数上面的Source具体类型是CBHistoryItem.class，但是关键的round_uuid缺失，要重新请求货源详情
     */
    public static Bundle getBundleParams(Source source, boolean needSourceDetail) {
        if (source == null)
            return new Bundle();

        Bundle bundle = new Bundle();
        bundle.putString("roundUUID", source.getRound_uuid());//竞价场次ID
        bundle.putString("unit", source.getCargoUnit());//货源的单位
        bundle.putInt("bookRefType", source.getBook_ref_type());//计费依据
        if (source instanceof CBHistoryItem) {
            CBHistoryItem item = (CBHistoryItem) source;
            bundle.putString("bidUuid", item.getBidUuid());//投标ID
            bundle.putBoolean("needSourceDetail", needSourceDetail);//是否需要重新请求货源详情，请求货源详情需要bidUuid，所以这句代码写在if外面是没有意义的
            if (!needSourceDetail) {//需要请求货源详情时，下面这些信息等货源详情获取之后再取
                bundle.putString("bidPrice", item.getBidPrice());//竞价价格，在修改竞价价格之后这个字段要对应着更新
                bundle.putBoolean("canModifyBidPrice", item.canModifyBidPrice());
            }
        }
        return bundle;
    }

//    private String getBidPriceWithUnit() {
//        final String price = getArguments().getString("bidPrice");
//        final String unit = getArguments().getString("unit");
//        return price + "元/" + unit;
//    }

    //竞价价格修改成功
    @Override
    public void bidPriceChanged(String bidPrice) {
//        getArguments().putString("bidPrice", bidPrice);//更新存储的竞价价格
//        showMessage("修改竞价价格成功！");
//        //加载当前场次剩余时间和参与竞价的信息
//        mPresenter.loadTime(getArguments().getString("roundUUID"));
        mCommon.bidPriceChanged(bidPrice);
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration();
    }
}
