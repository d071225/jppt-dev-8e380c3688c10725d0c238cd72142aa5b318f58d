package com.hletong.mob.dialog.selector;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hletong.mob.R;
import com.hletong.mob.architect.contract.ListContract;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.dialog.BottomDialog;
import com.hletong.mob.pullrefresh.DividerItemDecoration;
import com.xcheng.okhttp.util.ParamUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 底部选择器基类
 * Created by ddq on 2016/11/4.
 */

public abstract class BottomSelectorDialog<T extends IItemShow> extends BottomDialog implements ListContract.View<T> {
    private ProgressBar mProgressBar;
    private TextView errorView;
    private RecyclerView mRecyclerView;
    private TextView titleView;
    private TextView tvCancel;

    private ItemSelectorAdapter<T> mAdapter;
    private List<T> mPrefetchData;//预加载得到的数据
    /**
     * 区分此选择器多用的情况下对应需要处理的View
     * id 、数组下标index、某个flag
     */
    private int mExtra;
    private OnItemSelectedListener<T> onItemSelectedListener;
    private boolean prefetch = false;//数据预加载
    private Bundle mArguments;

    public BottomSelectorDialog(Context context) {
        super(context);
    }

    public void setArguments(Bundle args) {
        mArguments = args;
    }

    /**
     * Return the arguments supplied when the fragment was instantiated,
     * if any.
     */
    public Bundle getArguments() {
        return mArguments;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_bottom_selector;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        errorView = (TextView) findViewById(R.id.errorView);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerView);
        titleView = (TextView) findViewById(R.id.ev_id_titleView);
        tvCancel = (TextView) findViewById(R.id.id_cancel);
        tvCancel.setOnClickListener(this);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration());
        titleView.setText(getTitle());

        hideLoading();
    }

    protected abstract String getTitle();

    /***
     * 开始加载数据
     ***/
    protected abstract void onLoad();

    //是否使用缓存数据
    protected boolean useCache() {
        return true;
    }


    //数据预加载
    public void prefetch(int extra) {
        setExtra(extra);
        prefetch = true;
        onLoad();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    @Override
    public void handleError(@NonNull BaseError error) {
        if (prefetch) {
            prefetchFinished();
        }

        if (!isShowing())
            return;

        clearData();

        if (errorView != null) {
            errorView.setVisibility(View.VISIBLE);
            errorView.setText(error.getMessage());
        }
    }

    public void setOnItemSelected(OnItemSelectedListener<T> ts) {
        this.onItemSelectedListener = ts;
    }

    public void show(int extra) {
        this.mExtra = extra;
        show();
    }

    @Override
    public void show() {
        super.show();

        if (titleView.getText() != null && !titleView.getText().equals(getTitle())) {
            titleView.setText(getTitle());
        }

        /**触发加载请求***/
        if (useCache()) {
            if (mAdapter != null && !mAdapter.isEmpty()) {
                showList(new ArrayList<>(mAdapter.getData()), true);
                return;
            }
            if (!ParamUtil.isEmpty(mPrefetchData)) {
                showList(mPrefetchData, true);
                return;
            }
        }
        //清除现有数据
        clearData();
        //加载新数据
        onLoad();
    }

    @Override
    public void showLoading() {
        if (mProgressBar != null)
            mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        if (mProgressBar != null)
            mProgressBar.setVisibility(View.GONE);
    }

    public void setExtra(int extra) {
        if (mExtra != extra) {
            mExtra = extra;
            mPrefetchData = null;
        }
    }

    /**
     * called when success;
     *
     * @param list 返回列表
     */
    @Override
    public void showList(List<T> list, boolean refresh) {
        if (prefetch) {//预加载的数据
            mPrefetchData = list;
            prefetch = false;
            boolean callFinish = true;
            if (onItemSelectedListener instanceof SelectorPrefetchListener && !ParamUtil.isEmpty(list)) {
                SelectorPrefetchListener<T> prefetchListener = (SelectorPrefetchListener<T>) onItemSelectedListener;
                callFinish = prefetchListener.dataRetrieved(list, mExtra);
            }
            if (callFinish) {
                prefetchFinished();//预加载结束
            }
            return;
        }
        if (isShowing()) {
            errorView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            resizeWindow(Math.min(list.size(), 5));

            if (mAdapter == null) {
                mAdapter = new ItemSelectorAdapter<>(getContext(), null, new OnItemSelectedListener<T>() {
                    @Override
                    public void onItemSelected(T item, int extra) {
                        if (onItemSelectedListener != null) {
                            onItemSelectedListener.onItemSelected(item, mExtra);
                        }
                        dismiss();//dismiss函数一定不要放在onItemSelected函数的前面
                    }
                });
                mRecyclerView.setAdapter(mAdapter);
            }
            if (!useCache())
                mAdapter.setSelected(-1);

            mAdapter.refresh(list);
        }
    }

    public void clearData() {
        if (mAdapter != null) {
            mAdapter.refresh(null);
        }
        resizeWindow(1);
    }

    private void resizeWindow(int itemCount) {
        ViewGroup.LayoutParams layoutParams = mRecyclerView.getLayoutParams();
        final int sh = getResources().getDimensionPixelSize(R.dimen.bottom_selector_item_height);
        final int height = itemCount * sh;
        if (layoutParams.height != height) {
            layoutParams.height = height;
            mRecyclerView.setLayoutParams(layoutParams);
        }
    }

    protected void prefetchFinished() {

    }

    public interface OnItemSelectedListener<T> {
        /**
         * @param item  返回的选中的item，
         * @param extra 标记View的id或者数组的下标，页面有多个picker选择，可以共享此监听
         */
        void onItemSelected(T item, int extra);
    }
}
