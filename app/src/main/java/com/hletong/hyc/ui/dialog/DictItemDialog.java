package com.hletong.hyc.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.hletong.hyc.contract.DictItemContract;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.datasource.DictItemSource;
import com.hletong.hyc.presenter.DictItemPresenter;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.dialog.selector.BottomSelectorDialog;
import com.xcheng.okhttp.util.ParamUtil;

import java.util.ArrayList;
import java.util.List;

public class DictItemDialog extends BottomSelectorDialog<DictionaryItem> implements DictItemContract.View {

    private DictItemContract.Presenter mPresenter;
    private String mDictType;
    private String mTitle;
    private boolean canUseCache;//这个Fragment可能会在同一个界面里面被共享，这时候就要控制是否使用缓存数据
    private final List<DictionaryItem> items2Fetch;
    private PrefetchListener mPrefetchListener;

    public DictItemDialog(Context context) {
        this(context, Constant.getUrl(Constant.TRUCK_OPTIONAL_ITEM));
    }

    //调用这个方法，上面的方法不适应新情况
    public DictItemDialog(Context context, String url) {
        super(context);
        mPresenter = new DictItemPresenter(new DictItemSource(context), this);
        Bundle bundle = new Bundle();
        bundle.putString("url", url);
        setArguments(bundle);
        items2Fetch = new ArrayList<>();
    }

    public void showDict(String dictType, String title, int extra) {
        showDict(dictType, title, null, extra);
    }

    /**
     * @param dictType Dictionary 某个字典项的属性
     * @param title    标题
     * @param items
     * @param extra
     */
    public void showDict(String dictType, String title, ArrayList<DictionaryItem> items, int extra) {
        canUseCache = mDictType != null && mDictType.equals(dictType);
        mDictType = dictType;
        mTitle = title;

        Bundle bundle = getArguments();
        bundle.putParcelableArrayList(mDictType, items);
        show(extra);
    }

    @Override
    protected String getTitle() {
        return mTitle != null ? mTitle : "请选择";
    }

    //预加载多个栏目
    public void prefetch(List<DictionaryItem> items, PrefetchListener prefetchListener) {
        if (ParamUtil.isEmpty(items))
            throw new IllegalStateException("prefetch list can not be empty");

        this.mPrefetchListener = prefetchListener;
        int oldSize = items2Fetch.size();
        this.items2Fetch.addAll(items);
        if (oldSize == 0) {
            DictionaryItem di = items2Fetch.get(0);
            prefetch(di.getText(), di.getIdAsInt());
        }
    }

    public void prefetch(String dictType, int extra) {
        this.mDictType = dictType;
        super.prefetch(extra);
    }

    private String findId;

    public void getDictItemById(@Nullable final String id, String dictType, final OnGetItemByIdListener onGetItemByIdListener) {
        if (TextUtils.isEmpty(id)) {
            return;
        }
        List<DictionaryItem> items = getArguments().getParcelableArrayList(dictType);
        DictItemContract.View callbackView = new DictItemContract.View() {

            @Override
            public void showDicts(List<DictionaryItem> list) {
                for (DictionaryItem temp : list) {
                    if (temp.getId().equals(id)) {
                        onGetItemByIdListener.onFindItem(temp);
                        return;
                    }
                }
            }

            @Override
            public void handleError(@NonNull BaseError error) {

            }

            @Override
            public void showLoading() {

            }

            @Override
            public void hideLoading() {

            }
        };

        if (ParamUtil.isEmpty(items)) {
            DictItemContract.Presenter mPresenter = new DictItemPresenter(new DictItemSource(getContext()), callbackView);
            mPresenter.loadDictItems(dictType, getArguments().getString("url"));
        } else {
            callbackView.showDicts(items);
        }
    }

    @Override
    protected boolean useCache() {
        return canUseCache;
    }

    @Override
    protected void onLoad() {
        List<DictionaryItem> items = getArguments().getParcelableArrayList(mDictType);
        if (ParamUtil.isEmpty(items)) {
            mPresenter.loadDictItems(mDictType, getArguments().getString("url"));
        } else {
            showList(items, true);
        }
    }

    @Override
    public void showDicts(List<DictionaryItem> list) {
        showList(list, true);
    }

    @Override
    public void showList(List<DictionaryItem> list, boolean refresh) {
        super.showList(list, refresh);
        for (DictionaryItem item : list) {
            if (item.getId().equals(findId) && onGetItemByIdListener != null) {
                onGetItemByIdListener.onFindItem(item);
            }
        }
    }

    @Override
    protected void prefetchFinished() {
        if (items2Fetch.size() == 0)
            return;
        items2Fetch.remove(0);
        if (items2Fetch.size() > 0) {
            DictionaryItem di = items2Fetch.get(0);
            this.mDictType = null;
            prefetch(di.getText(), di.getIdAsInt());
        } else {
            this.items2Fetch.clear();
            this.mDictType = null;
            if (mPrefetchListener != null)
                mPrefetchListener.prefetchFinished();
        }
    }

    public interface PrefetchListener {
        void prefetchFinished();
    }

    private OnGetItemByIdListener onGetItemByIdListener;

    public interface OnGetItemByIdListener {
        void onFindItem(DictionaryItem item);
    }
}
