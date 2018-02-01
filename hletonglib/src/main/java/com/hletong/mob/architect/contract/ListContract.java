package com.hletong.mob.architect.contract;

import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.requestvalue.ListRequestValue;
import com.hletong.mob.architect.view.IBaseView;

import java.util.List;

/**
 * Created by ddq on 2016/12/6.
 * 列表基础
 */
public interface ListContract {
    interface View<T> extends IBaseView {
        void showList(List<T> list, boolean refresh);
    }

    interface Presenter<T> extends IBasePresenter{
        void loadList(ListRequestValue<T> requestValue, boolean refresh);
    }
}
