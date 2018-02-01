package com.hletong.mob.dialog.selector;

import java.util.List;

/**
 * Created by ddq on 2017/2/6.
 * 预加载监听
 */
public abstract class SelectorPrefetchListener<T> implements BottomSelectorDialog.OnItemSelectedListener<T> {
    /**
     * 是否回调prefetchFinished()函数
     * @param data
     * @param extra
     * @return true 回调prefetchFinished()，false otherwise
     */
    public boolean dataRetrieved(List<T> data, int extra) {
        onItemSelected(data.get(0), extra);
        return true;
    }
}
