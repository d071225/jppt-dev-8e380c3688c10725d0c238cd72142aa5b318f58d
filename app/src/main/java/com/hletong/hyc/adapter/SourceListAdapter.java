package com.hletong.hyc.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.presenter.SourcePresenter;
import com.hletong.hyc.ui.activity.BookActivityBase;
import com.hletong.hyc.ui.fragment.SourceListFragmentPublic;
import com.hletong.hyc.util.LoginFilterDelegate;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ViewUtil;
import com.xcheng.view.util.ToastLess;

import java.util.List;

/**
 * Created by chengxin on 16/9/27.
 * 货源相关的列表
 */
public class SourceListAdapter extends HFRecyclerAdapter<Source> {

    private ITransactionView delegate;

    public SourceListAdapter(Context context, List<Source> items, ITransactionView delegate) {
        super(context, items);
        this.delegate = delegate;
    }

    protected View getView(ViewGroup parent) {
        return getInflater().inflate(R.layout.recycler_item_source, parent, false);
    }

    @Override
    protected BaseHolder<Source> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == Integer.MIN_VALUE) {
            //无状态
            return new DisabledViewHolder(getInflater().inflate(R.layout.recycler_item_source_disabled, parent, false), delegate);
        } else {
            switch (viewType) {
                case 1://竞价
                    return new BiddingViewHolder<>(getView(parent), delegate);
                case 2://挂价
                case 3://人工匹配
                case 4://兜底运输
                    return new BookViewHolder<>(getView(parent), delegate);
                case 300://自主交易-议价
                    return new QuoteViewHolder<>(getView(parent), delegate);
            }
            //无状态
            return new DisabledViewHolder(getInflater().inflate(R.layout.recycler_item_source_disabled, parent, false), delegate);
        }
    }

    @Override
    public int getViewType(int position) {
        Source source = getItem(position);
        if (source.get_disableRow_()) {
            return Integer.MIN_VALUE;
        } else {
            return source.getRound_trans_type();
        }
    }

    public ITransactionView getDelegate() {
        return delegate;
    }

    public static boolean willFilter(View v) {
        LoginFilterDelegate loginFilterDelegate = SourceListFragmentPublic.getLoginFilterDelegate();
        return loginFilterDelegate != null && loginFilterDelegate.willFilter(v);
    }

    public static class BaseViewHolder<T extends Source> extends BaseHolder<T> implements View.OnClickListener {
        public static final int REQUEST_CODE_FOR_LIST_ITEM = 800;
        private Source source;
        private ITransactionView delegate;

        public BaseViewHolder(View itemView, ITransactionView delegate) {
            super(itemView);
            this.delegate = delegate;
            getView(R.id.action).setOnClickListener(this);
            changeStyle((TextView) getView(R.id.flag), (TextView) getView(R.id.action));
        }

        public void setData(Source source) {
            this.source = source;
            setText(R.id.title, source.getOrgin_cargon_kind_name());
            setText(R.id.CargoWeight, getCargoDesc(source));
            setText(R.id.startProvince, Address.chooseCity(source.getLoading_province(), source.getLoading_city()));//不是显示省，而是市
            setText(R.id.startCity, Address.chooseCountry(source.getLoading_province(), source.getLoading_city(), source.getLoading_country()));//不是显示市，而是区
            setText(R.id.endProvince, Address.chooseCity(source.getUnload_province(), source.getUnload_city()));//不是显示省，而是市
            setText(R.id.endCity, Address.chooseCountry(source.getUnload_province(), source.getUnload_city(), source.getUnload_country()));//不是显示市，而是区
            setText(R.id.time, getTimeLabelDesc(source));
        }

        protected int getSourceType() {
            return SourcePresenter.DEFAULT;
        }

        protected String getCargoDesc(Source source) {
            return source.getCargoDescWithUnit();
        }

        protected String getTimeLabelDesc(Source source) {
            return "装货日期：" + source.getLoadingPeriod();
        }

        @Override
        public void onClick(View v) {
            if (willFilter(v)) {
                return;
            }
            if (isSourceAvailable()) {
                Intent intent = BookActivityBase.getBookActivity(itemView.getContext(), getSourceType());
                if (intent == null) return;
                intent.putExtra("type", getSourceType());
                intent.putExtra("cargoUuid", getSource().getCargo_uuid());
                intent.putExtra("cargoSrcType", getSource().getCargo_src_type());
                startActivityForResult(intent);
            } else {
                //防止一直弹出Toast
                ToastLess.showToast(R.string.source_not_available);
            }
        }

        private boolean isSourceAvailable() {
            return getSourceType() == SourcePresenter.CB
                    || getSourceType() == SourcePresenter.GUAJIA
                    || getSourceType() == SourcePresenter.CB_HISTORY
                    || getSourceType() == SourcePresenter.QUOTE;
        }

        public Source getSource() {
            return source;
        }

        protected void startActivityForResult(Intent intent) {
            delegate.toActivity(intent, REQUEST_CODE_FOR_LIST_ITEM, null);
        }

        protected void startActivityForResult(Class cls, Bundle bundle) {
            Intent intent = new Intent(itemView.getContext(), cls);
            if (bundle != null)
                intent.putExtras(bundle);
            startActivityForResult(intent);
        }

        public final String getTime() {
            return source.getAvailableTime();
        }

        //根据不同的item类型，改变样式，子类自己实现
        protected void changeStyle(TextView flag, TextView action) {

        }
    }

    static class BiddingViewHolder<T extends Source> extends BaseViewHolder<T> {

        BiddingViewHolder(View itemView, ITransactionView delegate) {
            super(itemView, delegate);
            itemView.setOnClickListener(this);
        }

        @Override
        protected String getCargoDesc(Source source) {
            return source.getRemainCargoDescWithUnit();
        }

        @Override
        protected int getSourceType() {
            return SourcePresenter.CB;
        }

        @Override
        protected void changeStyle(TextView flag, TextView action) {
            flag.setText("竞");
            flag.setTextColor(getColor(R.color.yellow));
            ViewUtil.setBackground(flag, ContextCompat.getDrawable(itemView.getContext(), R.drawable.recycler_item_jingjia_text_bg));

            action.setText("竞价");
            action.setTextColor(getColor(R.color.yellow));
            ViewUtil.setBackground(action, ContextCompat.getDrawable(itemView.getContext(), R.drawable.recycler_item_jingjia_border));
        }
    }

    static class BookViewHolder<T extends Source> extends BaseViewHolder<T> {

        BookViewHolder(View itemView, ITransactionView delegate) {
            super(itemView, delegate);
            itemView.setOnClickListener(this);
        }

        @Override
        protected String getCargoDesc(Source source) {
            return source.getRemainCargoDescWithUnit();
        }

        @Override
        protected int getSourceType() {
            return SourcePresenter.GUAJIA;
        }
    }

    static class QuoteViewHolder<T extends Source> extends BaseViewHolder<T> {

        QuoteViewHolder(View itemView, ITransactionView delegate) {
            super(itemView, delegate);
            itemView.setOnClickListener(this);
        }

        @Override
        protected String getCargoDesc(Source source) {
            return source.getRemainCargoDescWithUnit();
        }

        @Override
        protected int getSourceType() {
            return SourcePresenter.QUOTE;
        }

        @Override
        protected void changeStyle(TextView flag, TextView action) {
            flag.setText("议");
            flag.setTextColor(getColor(R.color.colorAccent));
            ViewUtil.setBackground(flag, ContextCompat.getDrawable(itemView.getContext(), R.drawable.recycler_item_bg_quote));

            action.setText("议价");
            action.setTextColor(getColor(R.color.colorAccent));
            ViewUtil.setBackground(action, ContextCompat.getDrawable(itemView.getContext(), R.drawable.recycler_item_quote_border));
        }
    }

    private static class DisabledViewHolder extends BookViewHolder<Source> {

        DisabledViewHolder(View itemView, ITransactionView delegate) {
            super(itemView, delegate);
        }

        @Override
        public void setData(Source source) {
            super.setData(source);
        }

        @Override
        protected int getSourceType() {
            return SourcePresenter.DEFAULT;
        }
    }
}
