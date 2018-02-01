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
import com.hletong.hyc.adapter.InvoiceCertAdapter;
import com.hletong.hyc.contract.InfoCompleteContract;
import com.hletong.hyc.model.InvoiceCert;
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
 * 通用的开票信息补全页面
 * Created by chengxin on 2017/6/12.
 */
public class InvoiceCertAddActivity extends BaseActivity implements InfoCompleteContract.View {
    @BindView(R.id.id_recyclerView)
    RecyclerView mCardRecyclerView;
    private InvoiceCertAdapter mInvoiceAdapter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_invoice_add;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        ArrayList<InvoiceCert> invoiceCerts = getSerializable(InvoiceCert.class.getSimpleName());
        mCardRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mCardRecyclerView.addItemDecoration(new SpaceItemDecoration(LocalDisplay.dp2px(8), true));
        mInvoiceAdapter = new InvoiceCertAdapter(this, invoiceCerts);
        mCardRecyclerView.setAdapter(mInvoiceAdapter);
    }

    @OnClick({R.id.cv_bankcard_add, R.id.btn_commit})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (R.id.cv_bankcard_add == v.getId()) {
            toActivity(InvoiceCertInputActivity.class, null, Constant.ADD_REQUEST, null);
        } else if (R.id.btn_commit == v.getId()) {
            if (mInvoiceAdapter.getDataCount() == 0) {
                showMessage("请添加开票信息");
            }
            Intent intent = new Intent();
            intent.putExtra(InvoiceCert.class.getSimpleName(), (Serializable) mInvoiceAdapter.getData());
            setResult(RESULT_OK, intent);
            finish();
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
            mInvoiceAdapter.add((InvoiceCert) data.getSerializableExtra(InvoiceCert.class.getSimpleName()));
        } else if (requestCode == Constant.EDIT_REQUEST) {
            InvoiceCert invoiceCert = (InvoiceCert) data.getSerializableExtra(InvoiceCert.class.getSimpleName());
            mInvoiceAdapter.getData().set(invoiceCert.getIndex(), invoiceCert);
            mInvoiceAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void success(String message) {
        showMessage(message);
    }
}
