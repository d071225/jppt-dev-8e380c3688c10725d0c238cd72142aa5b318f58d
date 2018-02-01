package com.hletong.hyc.ui.fragment;

import android.animation.Animator;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.AppointCarrierAdapter;
import com.hletong.hyc.adapter.DraftAdapter;
import com.hletong.hyc.adapter.SourceListAdapter;
import com.hletong.hyc.contract.DraftContract;
import com.hletong.hyc.model.Draft;
import com.hletong.hyc.model.datasource.DraftDataSource;
import com.hletong.hyc.presenter.DraftPresenter;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.requestvalue.ListRequestValue;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.SimpleAnimatorListener;
import com.hletong.mob.widget.BorderLinearLayout;
import com.xcheng.view.util.LocalDisplay;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/4/6.
 * 草稿列表
 */

public class DraftFragment extends BaseRefreshFragment<Draft> implements DraftContract.View, AppointCarrierAdapter.ItemChangedListener<Draft> {
    @BindView(R.id.toggle)
    TextView toggle;
    @BindView(R.id.delete)
    TextView delete;
    @BindView(R.id.buttons)
    BorderLinearLayout buttons;

    private MenuItem mMenuItem;

    private DraftContract.Presenter mPresenter;
    private int translationY;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_draft;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        translationY = LocalDisplay.dp2px(48);
        mPresenter = new DraftPresenter(this, new DraftDataSource(getContext()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.revoke, menu);
        mMenuItem = menu.findItem(R.id.revoke);
        mMenuItem.setTitle("编辑");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        DraftAdapter adapter = (DraftAdapter) mAdapter;
        if (adapter.getDataCount() > 0) {
            if (adapter.toggle()) {
                editState();
            } else {
                selectState();
            }
        } else {
            showMessage("没有可编辑的数据");
        }
        return true;
    }

    @Override
    public void success(String message) {
        showMessage(message);
        mPtrFrameLayout.autoRefresh();
    }

    @Override
    public void requestData(boolean refresh) {
        mPresenter.loadList(new ListRequestValue<Draft>(hashCode(), null, getRequestJson(refresh)) {
        }, refresh);
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<Draft> createAdapter() {
        return new DraftAdapter(getContext(), mPresenter, this, this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @OnClick({R.id.toggle, R.id.delete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toggle:
                DraftAdapter adapter = (DraftAdapter) mAdapter;
                adapter.changeState(adapter.haveUnSelected());
                break;
            case R.id.delete:
                DialogFactory.showAlertWithNegativeButton(getContext(), "删除草稿", "确定要删除选中的草稿吗？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        DraftAdapter adapter = (DraftAdapter) mAdapter;
                        mPresenter.delete(adapter.getItem2Delete());
                    }
                });
                break;
        }
    }

    @Override
    public void onActivityResultOk(int requestCode, Intent data) {
        super.onActivityResultOk(requestCode, data);
        if (requestCode == SourceListAdapter.BaseViewHolder.REQUEST_CODE_FOR_LIST_ITEM)
            mPtrFrameLayout.autoRefresh(true);
    }

    @Override
    public void handleError(@NonNull BaseError error) {
        if (error.isListRequest() && error.isEmptyError()) {
            selectState();
        }
        super.handleError(error);
    }

    private void editState() {
        mMenuItem.setTitle("取消");
        buttons.animate().translationY(0).setListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                buttons.setTranslationY(translationY);
                buttons.setVisibility(View.VISIBLE);
            }
        }).start();
    }

    private void selectState() {
        mMenuItem.setTitle("编辑");
        buttons.animate().translationY(buttons.getHeight()).setListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                buttons.setVisibility(View.GONE);
            }
        }).start();
    }

    @Override
    public void itemSelected(Draft item) {
        delete.setEnabled(true);
        DraftAdapter adapter = (DraftAdapter) mAdapter;
        if (adapter.haveUnSelected()) {
            toggle.setText("全选");
            toggle.setSelected(false);
        } else {
            toggle.setText("反选");
            toggle.setSelected(true);
        }
    }

    @Override
    public void itemUnSelected(Draft item) {
        toggle.setText("全选");
        toggle.setSelected(false);

        DraftAdapter adapter = (DraftAdapter) mAdapter;
        delete.setEnabled(adapter.haveSelected());
    }
}
