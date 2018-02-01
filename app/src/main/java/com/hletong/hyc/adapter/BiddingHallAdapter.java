package com.hletong.hyc.adapter;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.CBRoundItem;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

import java.util.List;

/**
 * Created by ddq on 2016/11/4.
 * 竞价大厅
 */

public class BiddingHallAdapter extends HFRecyclerAdapter<CBRoundItem> {
    private String unit;
    private int bookRefType;

    public BiddingHallAdapter(Context context, List<CBRoundItem> data, int length, String unit, int bookRefType) {
        super(context, data, length);
        this.unit = unit;
        this.bookRefType = bookRefType;
    }

    @Override
    protected BaseHolder<CBRoundItem> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getInflater().inflate(R.layout.recycler_item_bidding_hall, parent, false);
        if (viewType == 1)
            return new HeadViewHolder(view);
        else
            return new ViewHolder(view);
    }

    @Override
    public int getViewType(int position) {
        if (position == 0)
            return 1;
        else
            return 2;
    }

    @Override
    public void refresh(List<CBRoundItem> newData) {
        newData.add(0, new CBRoundItem());
        super.refresh(newData);
    }

    private class ViewHolder extends BaseHolder<CBRoundItem> {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void setData(CBRoundItem cbh) {
            setText(R.id.rank, String.valueOf(getAdapterPosition()));
            setText(R.id.price, cbh.getBidUnitAmt());
            setText(R.id.weight, cbh.getCargoDesc(bookRefType));
        }
    }

    private class HeadViewHolder extends BaseHolder<CBRoundItem> {

        public HeadViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(CBRoundItem data) {
            if (unit == null)
                return;

            TextView rank = getView(R.id.rank);
            setText(rank, "排名", "价格由低到高");

            TextView price = getView(R.id.price);
            setText(price, "竞价价格", "元/" + unit);

            TextView weight = getView(R.id.weight);
            setText(weight, getBookTypeDesc(), unit);
        }

        private void setText(TextView tv, String main, String sub) {
            SpannableStringBuilder rs = new SpannableStringBuilder(main);
            rs.append("\n");
            int start = rs.length();
            rs.append("(");
            rs.append(sub);
            rs.append(")");
            rs.setSpan(new AbsoluteSizeSpan(getDimen(R.dimen.small_textsize), false), start, rs.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            tv.setText(rs);
        }

        private String getBookTypeDesc() {
            if (bookRefType == 0) {
                return "承运重量\n";
            } else if (bookRefType == 1) {
                return "承运数量\n";
            } else
                return "\n";
        }
    }
}
