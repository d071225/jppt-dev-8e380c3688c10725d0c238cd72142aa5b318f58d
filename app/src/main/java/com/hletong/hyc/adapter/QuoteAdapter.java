package com.hletong.hyc.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.QuoteContract;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.Quote;
import com.hletong.hyc.ui.widget.RatingBar;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

/**
 * Created by ddq on 2017/3/27.
 * 报价进度
 */

public class QuoteAdapter extends HFRecyclerAdapter<Quote> {
    private MakePhoneCall callTransporter;//联系承运方议价
    private MakePhoneCall callUnit;//联系会员管理单位
    private QuoteContract.Presenter mPresenter;
    private String unit;//单位
    private int bookRefType;//摘牌依据
    private String member_tel;//会员管理单位联系电话

    public QuoteAdapter(Context context, MakePhoneCall callTransporter, MakePhoneCall callUnit, QuoteContract.Presenter presenter) {
        super(context, null);
        this.callTransporter = callTransporter;
        this.callUnit = callUnit;
        this.mPresenter = presenter;
        LoginInfo loginInfo = LoginInfo.getLoginInfo();
        member_tel = loginInfo.getMm_biz_contact_tel();
    }

    public void setExtra(String unit, int bookRefType) {
        this.unit = unit;
        this.bookRefType = bookRefType;
    }

    @Override
    protected BaseHolder<Quote> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        LinearLayout layout = (LinearLayout) getInflater().inflate(R.layout.recycler_item_quote_progress, parent, false);
        layout.setBackgroundColor(Color.WHITE);
        switch (viewType) {
            case 0://带按钮的Item
                return new Status0(getInflater().inflate(R.layout.recycler_item_quote_progress_actions, layout));
            case 100://标题
                return new TitleViewHolder(getInflater().inflate(R.layout.recycler_item_cargo_track_title, parent, false));
            default://不带按钮的Item
                return new ViewHolder(layout);
        }
    }

    @Override
    public int getViewType(int position) {
        if (position == 0)
            return 100;//标题
        return getItem(position).getQuoteType();
    }

    private class TitleViewHolder extends BaseHolder<Quote> {

        public TitleViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(Quote data) {
            TextView textView = (TextView) itemView;
            textView.setText("货源状态");
        }
    }

    private class ViewHolder extends BaseHolder<Quote> {
        private RatingBar mRatingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            mRatingBar = (RatingBar) itemView.findViewById(R.id.rate);
            mRatingBar.setVisibility(View.VISIBLE);
        }

        public void setData(Quote quote) {
            setText(R.id.carrier, quote.getCarrierNo());
            if (bookRefType == 0) {
                setText(R.id.book, "报价重量：" + quote.getCargoByBookRefType(bookRefType, unit));
            } else {
                setText(R.id.book, "报价数量：" + quote.getCargoByBookRefType(bookRefType, unit));
            }
            setText(R.id.price, "报价：" + quote.getUnitAmt(unit));
            setText(R.id.time, quote.getCreateTs());
            setText(R.id.status, quote.getStatus());
            setText(R.id.favor_rate, "好评率：" + quote.getMemberGrade() + "%");
            setText(R.id.trade_ct, "交易笔数：" + quote.getTradeAccount());
            mRatingBar.setProgress(quote.getMemberGrade() / 100);
        }
    }

    private class Status0 extends ViewHolder implements View.OnClickListener {

        public Status0(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.accept).setOnClickListener(this);
            itemView.findViewById(R.id.refuse).setOnClickListener(this);
            itemView.findViewById(R.id.call).setOnClickListener(this);
        }

        @Override
        public void setData(Quote quote) {
            super.setData(quote);
            if ("0".equals(quote.getWarrantStatus())) {
                setText(R.id.accept, "联系会员管理单位");
                setVisible(R.id.accept, View.VISIBLE);
            } else {
                setText(R.id.accept, "接受报价");
                //货方确认报价之后，保留取消报价和电话议价按钮，接受报价按钮删除
                setVisible(R.id.accept, "00".equals(quote.getRealStatus()) ? View.VISIBLE : View.GONE);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.accept:
                    if ("0".equals(getItem(getRealItemPosition(getAdapterPosition())).getWarrantStatus())) {
                        //联系会员管理单位
                        callUnit.call("会员管理单位", member_tel, null);
                    } else {
                        mPresenter.accept(getItem(getRealItemPosition(getAdapterPosition())).getQuoteUuid());
//                        mPresenter.checkUserInfo(getItem(getAdapterPosition()).getQuoteUuid());//接受报价之前，检查用户信息是否完整
                    }
                    break;
                case R.id.refuse:
                    DialogFactory.showAlertWithNegativeButton(v.getContext(), "取消报价", "确定要取消报价吗？取消报价后该报价失效", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            mPresenter.refuse(getItem(getRealItemPosition(getAdapterPosition())).getQuoteUuid());
                        }
                    });
                    break;
                case R.id.call:
                    Quote quote = getItem(getRealItemPosition(getAdapterPosition()));
                    callTransporter.call(quote.getMemberName(), quote.getMemberTel(), quote.getMemberCode() + "," + quote.getQuoteUuid());
                    break;
            }
        }
    }
}
