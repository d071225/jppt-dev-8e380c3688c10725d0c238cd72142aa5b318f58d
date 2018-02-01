package com.hletong.hyc.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.BookContract;
import com.hletong.hyc.contract.SourceContract;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.TransporterBase;
import com.hletong.hyc.model.validate.Validate2;
import com.hletong.hyc.presenter.BiddingBookPresenter;

/**
 * Created by ddq on 2017/1/5.
 * 竞价基类
 */

public abstract class BiddingBookActivity<T extends TransporterBase> extends BookActivityBase<T, BookContract.BiddingPresenter> implements BookContract.BiddingView {

    @Override
    public void onClick(View v) {
        //提交竞价信息
        getPresenter().submit(getSubmitParams(input.getInputValue(), price.getInputValue(), getDeductTaxRt()));
    }

    protected abstract Validate2 getSubmitParams(String cargo, String price, Source.DeductRt deductTaxRt);

    @Override
    protected String getCommitTitle() {
        return getString(R.string.commit_title_cb);
    }

    @Override
    public void showBiddingHallView() {
        //竞价大厅按钮
        bidButton.setVisibility(View.VISIBLE);
        bidButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().toBiddingHall();
            }
        });
    }

    @Override
    protected BookContract.BiddingPresenter createPresenter() {
        return new BiddingBookPresenter(this);
    }

    @Override
    public void onReceive(Context context, String action, Intent intent) {
        if (SourceContract.PLATFORM_BIDDING_PRICE_CHANGED.equals(action)){
            //平台开票货源竞价价格发生了变化
            getPresenter().priceChanged(intent.getStringExtra("price"));
        }else {
            super.onReceive(context, action, intent);
        }
    }
}
