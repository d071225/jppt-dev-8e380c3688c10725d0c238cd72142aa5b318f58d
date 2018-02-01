package com.hletong.mob.architect.presenter;

import android.support.annotation.NonNull;

import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.model.DataSource;
import com.hletong.mob.architect.model.repository.DataCallback;
import com.hletong.mob.architect.requestvalue.ListRequestValue;
import com.hletong.mob.architect.contract.ListContract;

import java.util.List;

/**
 * Created by ddq on 2016/12/29.
 */

public class ListPresenter<T> extends BasePresenter<ListContract.View<T>> implements ListContract.Presenter<T> {
    private boolean isRefresh;

    public ListPresenter(ListContract.View<T> view) {
        super(view);
    }

    public ListPresenter(DataSource dataRepository, ListContract.View<T> view) {
        super(dataRepository, view);
    }

    public boolean isRefresh() {
        return isRefresh;
    }

    public void setRefresh(boolean refresh) {
        this.isRefresh = refresh;
    }

    @Override
    public void loadList(final ListRequestValue<T> requestValue, boolean refresh) {
        setRefresh(refresh);
        requestValue.setRefresh(refresh);
        getDataRepository().loadList(requestValue, new DataCallback<List<T>>() {
            @Override
            public void onSuccess(@NonNull List<T> response) {
                getView().showList(response, isRefresh());
            }

            @Override
            public void onError(@NonNull BaseError error) {
                if (error.isEmptyError() && getEmptyDataDesc() != null) {
                    error.setMessage(getEmptyDataDesc());
                }
                getView().handleError(error);
            }
        });
    }

    protected String getEmptyDataDesc() {
        return null;
    }
}
