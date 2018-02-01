package com.hletong.hyc.ui.activity;

import android.content.DialogInterface;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.BookContract;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.TransporterBase;
import com.hletong.hyc.model.validate.PayMode;
import com.hletong.hyc.presenter.QuoteBookPresenter;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.mob.dialog.AlertDialog;
import com.xcheng.view.util.LocalDisplay;

/**
 * Created by ddq on 2017/3/28.
 * 议价基础类（国联议价，普通议价）
 */

public abstract class QuoteBookActivity<T extends TransporterBase> extends NormalBookActivity<T, BookContract.QuotePresenter> implements BookContract.QuoteView {

    private AlertDialog mDialog;

    @Override
    protected BookContract.QuotePresenter createPresenter() {
        return new QuoteBookPresenter(this, getIntent().getStringExtra("quoteUuid"));
    }

    @Override
    protected String getCommitTitle() {
        return "报价确认";
    }

    @Override
    protected CharSequence getCommitMessage() {
        return "确定要提交报价内容吗？";
    }

    @Override
    public void onClick(View v) {
        getPresenter().action();
    }

    @Override
    protected PayMode getSubmitParam(String cargo, Source.DeductRt deductTaxRt) {
        //do nothing
        return null;
    }

    @Override
    public void showMenu() {
        getToolbar().inflateMenu(R.menu.phone_selector);
        MenuItem mi = getToolbar().getMenu().findItem(R.id.call);
        mi.setTitle("取消报价");
        getToolbar().setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                DialogFactory.showAlertWithNegativeButton(getContext(), "提示", "确定取消该货源报价？",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getPresenter().revoke();
                            }
                        });
                return true;
            }
        });
    }

    @Override
    public void showViewsAtLeft() {
        final int space = LocalDisplay.dp2px(100);

        plate.forceAlignViewToLeft(space);
        plate.getSuffix().setVisibility(View.GONE);
        plate.setOnClickListener(null);

        input.forceAlignViewToLeft(space);
        input.getSuffix().setVisibility(View.GONE);

        price.forceAlignViewToLeft(space);
        price.getSuffix().setVisibility(View.GONE);

        value.forceAlignViewToLeft(space);
        value.getSuffix().setVisibility(View.GONE);
    }

    @Override
    public void prefetchTransporter() {
        prefetch();
    }

    @Override
    public void updateTransporter(String transporter) {
        plate.setText(transporter);
    }

    @Override
    public void updatePrice(String data) {
        price.setText(data);
    }

    @Override
    public void updateCargo(String cargo) {
        input.setText(cargo);
    }

    @Override
    public void showTransporter(String label) {
        plate.setVisibility(View.VISIBLE);
        plate.setLabel(label);
    }

    @Override
    public void makeCall(String tel) {
        CallPhoneDialog dialog = CallPhoneDialog.getInstance();
        dialog.setOnCalledListener(new CallPhoneDialog.OnCalledListener() {
            @Override
            public void onCalled() {
                getPresenter().record();
            }
        });
        dialog.show(getSupportFragmentManager(), "电话报价", "是否确定拨打该电话议价？", tel);
    }

    @Override
    public void priceModified(String message) {
        showMessage(message);
    }

    @Override
    public void showModifyView(String unit, int bookType, String book, String price) {
        if (mDialog == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("修改报价")
                    .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            CommonInputView i1 = (CommonInputView) mDialog.getWindow().getDecorView().findViewById(R.id.ct);
                            CommonInputView i2 = (CommonInputView) mDialog.getWindow().getDecorView().findViewById(R.id.price);
                            //提交新报价信息
                            getPresenter().modifyOffer(i2.getInputValue(), i1.getInputValue());
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .setContentView(R.layout.dialog_mofidy_offer);
            mDialog = builder.build();

            CommonInputView i1 = (CommonInputView) mDialog.getWindow().getDecorView().findViewById(R.id.ct);
            if (bookType == 0)
                i1.setLabel("重量");
            else
                i1.setLabel("数量");
            i1.getSuffix().setText(unit);
            if ("吨".equals(unit) || "立方".equals(unit)) {
                i1.setInputType(CommonInputView.NUMBER_DECIMAL);//只能输入整数
            } else {
                i1.setInputType(CommonInputView.NUMBER);
            }

            CommonInputView i2 = (CommonInputView) mDialog.getWindow().getDecorView().findViewById(R.id.price);
            i2.getSuffix().setText("元/" + unit);
        }
        CommonInputView i1 = (CommonInputView) mDialog.getWindow().getDecorView().findViewById(R.id.ct);
        i1.setText(book);

        CommonInputView i2 = (CommonInputView) mDialog.getWindow().getDecorView().findViewById(R.id.price);
        i2.setText(price);

        mDialog.show();
    }
}
