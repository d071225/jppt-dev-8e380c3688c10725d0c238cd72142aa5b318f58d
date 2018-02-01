package com.hletong.mob.base;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.hletong.mob.R;
import com.hletong.mob.architect.contract.ListContract;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.presenter.ListPresenter;
import com.hletong.mob.architect.requestvalue.ListRequestValue;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.EmptyView;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.pullrefresh.IPullRefreshViewNew;
import com.hletong.mob.pullrefresh.PtrDefaultHandlerWithLoadMore;
import com.hletong.mob.pullrefresh.PtrRVFrameLayout;
import com.hletong.mob.pullrefresh.SpaceItemDecoration;
import com.hletong.mob.pullrefresh.UIState;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.okhttp.util.ParamUtil;
import com.xcheng.view.enums.RequestState;
import com.xcheng.view.util.LocalDisplay;

import java.util.List;

import in.srain.cube.views.ptr.PtrFrameLayout;


/**
 * 刷新基类组件
 * Created by cx on 2016/10/13.
 */
public abstract class BaseRefreshFragment<T> extends BaseFragment implements ListContract.View<T>, IPullRefreshViewNew<T> {
    protected RecyclerView mRecyclerView;
    protected HFRecyclerAdapter<T> mAdapter;
    private TypeToken<List<T>> mListToken;
    protected PtrRVFrameLayout mPtrFrameLayout;
    private boolean hasInitView;
    private ListContract.Presenter<T> mPresenter;

    /**
     * 请在{@link #complete(boolean, UIState)}方法前赋值
     */
    private RequestState mRequestState = RequestState.LOADING;

    @CallSuper
    @SuppressWarnings("unchecked")
    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        //子类如果要自己实现Presenter,可以在onCreate方法里面调用setPresenter方法
        if (!onlyView() && mPresenter == null)
            mPresenter = new ListPresenter<>(this);
        mPtrFrameLayout = findViewById(R.id.id_ptrHLFrameLayout);
        mRecyclerView = findViewById(R.id.id_recyclerView);
        mRecyclerView.setLayoutManager(getLayoutManager());
        mRecyclerView.setItemAnimator(getItemAnimator());
        mRecyclerView.addItemDecoration(getItemDecoration());
        mAdapter = createAdapter();
        if (mAdapter == null) {
            throw new NullPointerException("mBaseTAdapter 未初始化，请先初始化");
        }
        mListToken = ParamUtil.createListTypeToken(getTokenClazz());
        mAdapter.setEmpty(getEmptyView());
        mAdapter.setFooter(LayoutInflater.from(mRecyclerView.getContext()).inflate(R.layout.footer_load_more, mRecyclerView, false));
        mRecyclerView.setAdapter(mAdapter);
        hasInitView = true;
    }

    @CallSuper
    @Override
    public void setListener() {
        super.setListener();
        mAdapter.setOnHolderBindListener(new HFRecyclerAdapter.OnHolderBindListener<T>() {
            @Override
            public void onHeaderBind(BaseHolder<T> holder) {
            }

            @Override
            public void onEmptyBind(BaseHolder<T> holder) {
                EmptyView emptyView = holder.getView(R.id.id_emptyView);
                emptyView.setRequestState(mRequestState);
            }

            @Override
            public void onFooterBind(BaseHolder<T> holder) {
                UIState loadingState = mPtrFrameLayout.getState();
                if (loadingState == UIState.LOADING_MORE || loadingState == UIState.REFRESHING) {
                    holder.setVisible(R.id.id_progressBarLoadMore, View.VISIBLE);
                } else {
                    holder.setVisible(R.id.id_progressBarLoadMore, View.INVISIBLE);
                }
                holder.setText(R.id.id_textLoadMore, loadingState.getText());
            }
        });

        mPtrFrameLayout.setPtrHandler(new PtrDefaultHandlerWithLoadMore() {
            @Override
            public void onLoadMore() {
                requestData(false);
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                if (mPtrFrameLayout.canRefresh()) {
                    mRequestState = RequestState.LOADING;
                    requestData(true);
                }
            }
        });

        if (!onlyView() && isAutoLoad()) {
            lazyLoad();
        } else {
            complete(true, UIState.INIT);
        }
    }

    public RecyclerView.LayoutManager getLayoutManager() {
        return new LinearLayoutManager(getContext());
    }

    public RecyclerView.ItemDecoration getItemDecoration() {
        return new SpaceItemDecoration(LocalDisplay.dp2px(8));
    }

    public RecyclerView.ItemAnimator getItemAnimator() {
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        // 取消notifyItemChanged动画
        defaultItemAnimator.setSupportsChangeAnimations(false);
        return defaultItemAnimator;
    }

    @Override
    public void refreshView(boolean isRefresh, List<T> data) {
        if (isRefresh) {
            mAdapter.refresh(data);
        } else {
            mAdapter.addData(data);
        }
        boolean allLoaded = data == null || data.size() < mAdapter.getLength();
        complete(isRefresh, allLoaded ? UIState.NO_MORE : UIState.INIT);
    }

    public void complete(boolean isRefresh, UIState state) {
        mPtrFrameLayout.complete(isRefresh, state);
        //手动complete的情况，防止emptyView页面仍然处于加载状态
        if (mRequestState == RequestState.LOADING && mAdapter.isEmpty()) {
            mRequestState = RequestState.NO_DATA;
        }
    }


    @CallSuper
    public EmptyView getEmptyView() {
        return EmptyView.with(getActivity()).setOnStateChangeListener(new EmptyView.OnStateChangeListener() {
            @Override
            public boolean onStateChanged(RequestState state, TextView handleView) {
                return state == RequestState.NETWORK_ERROR || state == RequestState.TIME_OUT;
            }

            @Override
            public void onClick(RequestState state, TextView handleView) {
                mPtrFrameLayout.autoRefresh(true);
            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (!onlyView() && isAutoLoad()) {
            lazyLoad();
        } else {
            complete(true, UIState.INIT);
        }
    }

    /**
     * 是否进行自动更新
     *
     * @return
     */
    protected boolean isAutoLoad() {
        return true;
    }

    private void lazyLoad() {
        if (getUserVisibleHint() && hasInitView) {
            if (mAdapter != null && mAdapter.getDataCount() == 0) {
                mPtrFrameLayout.autoRefresh(true, 1000);
            }
        }
    }

    @Override
    public void requestData(boolean refresh) {
        if (onlyView()) {
            complete(refresh, UIState.INIT);
            return;
        }

        if (mPresenter != null) {
            ListRequestValue<T> listRequestValue = new ListRequestValue<T>(hashCode(), getRequestUrl(), getRequestJson(refresh), getEntry()) {
            };
            listRequestValue.setTypeToken(mListToken);
            mPresenter.loadList(listRequestValue, refresh);

        }
    }

    /**
     * 返回的json中对应的数据列表的key
     *
     * @return
     */
    protected String getEntry() {
        return "list";
    }

    public ParamsHelper getCommonRequestValue(boolean refresh) {
        return new ParamsHelper()
                .put("limit", mAdapter.getLength())
                .put("start", refresh ? 1 : mAdapter.getDataCount() + 1);
    }

    @Override
    public void handleError(@NonNull BaseError error) {
        if (!onRefreshError(error)) {
            /**普通错误处理***/
            super.handleError(error);
        }
    }

    /**
     * 处理刷新错误代码
     *
     * @param error
     * @return true 代表处理成功，false代表处理失败
     */
    public final boolean onRefreshError(BaseError error) {
        if (error.isListRequest()) {
            if (error.isEmptyError()) {
                //如果是刷新，清除原有数据,如果是加载更多转换为加载完毕状态
                refreshView(error.isRefresh(), null);
            } else {
                mRequestState = ErrorFactory.getErrorState(error);
                complete(error.isRefresh(), UIState.INIT);//还原为初始状态，保持原有数据不变
                //在列表有数据的情况下，弹出错误内容
                if (!mAdapter.isEmpty())
                    showMessage(error.getMessage());
            }
            return true;
        }
        return false;
    }

    protected ParamsHelper getRequestJson(boolean refresh) {
        return getCommonRequestValue(refresh);
    }

    protected String getRequestUrl() {
        return null;
    }

    /**
     * 防止子类有添加了自定义的泛型的情况，
     * 父类生成TypeToken的时候会取错泛型类型
     *
     * @return Item class
     */
    protected Class<?> getTokenClazz() {
        return getClass();
    }

    @Override
    public int getLayoutId() {
        return R.layout.common_recycler_list;
    }

    /**
     * 若有自定义的Presenter，通过这个函数进行设置
     *
     * @param presenter 刷新请求的业务presenter
     */
    public void setPresenter(ListContract.Presenter<T> presenter) {
        this.mPresenter = presenter;
    }

    @Override
    public void showList(List<T> mList, boolean refresh) {
        refreshView(refresh, mList);
    }

    /**
     * 这个函数是为那些只是想使用这里面的初始化过程和回调函数的子类所提供的
     * eg：竞价大厅里面有列表，但是我们加载的列表包裹在一个item里面，这个item里面所有的数据都是需要的
     * 故而，我们把所有数据都取回来，然后取出列表进行显示，这里只用到了回调方法，没用到Presenter
     *
     * @return 返回true表示不需要用到Presenter
     */
    protected boolean onlyView() {
        return false;
    }
}

