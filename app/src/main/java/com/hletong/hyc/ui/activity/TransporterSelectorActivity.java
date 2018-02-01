package com.hletong.hyc.ui.activity;

import android.view.View;

import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.TransporterBase;
import com.hletong.hyc.ui.dialog.TransporterSelectorDialog;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.base.MvpActivity;
import com.hletong.mob.dialog.selector.SelectorPrefetchListener;

/**
 * Created by ddq on 2016/11/4.
 * 选择车船的基类
 */

public abstract class TransporterSelectorActivity<T extends TransporterBase, P extends IBasePresenter> extends MvpActivity<P> {
    private TransporterSelectorDialog<T> transporter;
    private T mItem;

    protected T getSelectedItem() {
        return mItem;
    }

    protected void itemSelected(T item, int extra) {

    }

    @Override
    public void setListener() {
        super.setListener();
        CommonInputView view = getTransporterLabel();
        if (view != null) {
            //如果是子账号就没有必要选择了
            if (LoginInfo.isChildAccount()) {
                view.setMode(CommonInputView.VIEW);
            } else {
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (transporter != null)
                            transporter.show();
                    }
                });
            }
        }
    }

    //子类需在恰当的时候调用这个函数
    protected final void prefetch() {
        if (transporter == null) {
            transporter = getTransporter();
            transporter.setOnItemSelected(new SelectorPrefetchListener<T>() {
                @Override
                public void onItemSelected(T item, int extra) {
                    mItem = item;
                    getTransporterLabel().setText(item.getValue());
                    itemSelected(item, extra);
                }
            });
        }
        transporter.prefetch(0);
    }

    protected CommonInputView getTransporterLabel() {
        return null;
    }

    protected abstract TransporterSelectorDialog<T> getTransporter();
}
