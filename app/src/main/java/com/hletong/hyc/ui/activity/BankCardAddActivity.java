package com.hletong.hyc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.BankCardAdapter;
import com.hletong.hyc.contract.InfoCompleteContract;
import com.hletong.hyc.model.BankCard;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.pullrefresh.SpaceItemDecoration;
import com.xcheng.view.util.LocalDisplay;

import java.io.Serializable;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 通用的证件信息补全页面
 * 车主版个人信息补全
 * Created by chengxin on 2017/6/12.
 */
public class BankCardAddActivity extends BaseActivity implements InfoCompleteContract.View {
    @BindView(R.id.id_recyclerView)
    RecyclerView mCardRecyclerView;
    private BankCardAdapter mBankCardAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_bank_card_add;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        ArrayList<BankCard> bankCards = getSerializable(BankCard.class.getSimpleName());
        mCardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCardRecyclerView.addItemDecoration(new SpaceItemDecoration(LocalDisplay.dp2px(8), true));
        mBankCardAdapter = new BankCardAdapter(this, bankCards);
        mCardRecyclerView.setAdapter(mBankCardAdapter);
    }

    @OnClick({R.id.cv_bankcard_add, R.id.btn_commit})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (R.id.cv_bankcard_add == v.getId()) {
            toActivity(BankCardInputActivity.class, null, Constant.ADD_REQUEST, null);
        } else if (R.id.btn_commit == v.getId()) {
            if (mBankCardAdapter.getDataCount() == 0) {
                showMessage("请添加银行卡");
            } else {
                Intent i = new Intent();
                i.putExtra(BankCard.class.getSimpleName(), (Serializable) mBankCardAdapter.getData());
                setResult(RESULT_OK, i);
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.phone_selector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.call) {
            CallPhoneDialog.getInstance().show(getSupportFragmentManager());
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        if (requestCode == Constant.ADD_REQUEST) {
            mBankCardAdapter.add((BankCard) data.getSerializableExtra(BankCard.class.getSimpleName()));
        } else if (requestCode == Constant.EDIT_REQUEST) {
            BankCard card = (BankCard) data.getSerializableExtra(BankCard.class.getSimpleName());
            mBankCardAdapter.getData().set(card.getIndex(), card);
            mBankCardAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void success(String message) {
        showMessage(message);
    }
}
