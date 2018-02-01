package com.hletong.hyc.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.ComplainContract;
import com.hletong.hyc.presenter.ComplainPresenter;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.mob.gallery.builder.PreViewBuilder;
import com.hletong.mob.gallery.widget.PickerRecyclerView;
import com.xcheng.view.processbtn.ProcessButton;

/**
 * Created by ddq on 2017/3/30.
 * 自主交易投诉
 */

public class SelfTradeComplainActivity extends ImageSelectorActivityNew<ComplainContract.Presenter> implements ComplainContract.View {
    private PickerRecyclerView mRecyclerView;
    private EditText input;
    private ProcessButton processButton;

    @Override
    public int getLayoutId() {
        return R.layout.activity_self_trade_complain;
    }

    @Override
    protected ProcessButton getProcessBtn() {
        return processButton;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setCustomTitle("违约投诉");
        input = (EditText) findViewById(R.id.input);
        processButton = (ProcessButton) findViewById(R.id.submit);
        processButton.setOnClickListener(this);
        mRecyclerView = (PickerRecyclerView) findViewById(R.id.picker);
        mRecyclerView.setOnTakeClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelector(mRecyclerView);
            }
        });
        new PickerRecyclerView.Builder(this).maxCount(3).action(PreViewBuilder.SELECT).build(mRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.phone_selector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CallPhoneDialog.getInstance().show(getSupportFragmentManager(), "违约投诉", null, null);
        return true;
    }

    @Override
    public void onClick(View v) {
        getPresenter().submit(input.getText().toString().trim(), mRecyclerView.getPhotos());
    }

    @Override
    public void success(String message) {
        DialogFactory.showAlertWithNegativeButton(this, false, "违约投诉", "投诉内容已提交成功，你还可以联系客服",
                "确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        CallPhoneDialog.getInstance().show(getSupportFragmentManager());
                    }
                },
                "取消",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
    }

    @Override
    protected ComplainContract.Presenter createPresenter() {
        return new ComplainPresenter(this, getIntent().getStringExtra("tradeUuid"), getIntent().getStringExtra("confirmType"));
    }
}
