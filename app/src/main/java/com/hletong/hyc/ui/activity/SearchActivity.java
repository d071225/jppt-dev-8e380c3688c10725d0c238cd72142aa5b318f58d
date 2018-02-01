package com.hletong.hyc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.mob.base.BaseActivity;

/**
 * Created by ddq on 2017/3/7.
 */

public class SearchActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private EditText input;
    private TextView cancel;
    private TextView clear;

    @Override
    public int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mRecyclerView = (RecyclerView) findViewById(R.id.id_recyclerView);
        input = (EditText) findViewById(R.id.search);
        cancel = (TextView) findViewById(R.id.cancel);
        cancel.setOnClickListener(this);
        clear = (TextView) findViewById(R.id.clear);
        clear.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.cancel:
                finish();
                break;
            case R.id.clear:
                break;
        }
    }
}
