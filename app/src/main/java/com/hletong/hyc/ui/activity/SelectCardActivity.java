package com.hletong.hyc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.SelectCardAdapter;
import com.hletong.hyc.model.ETCCard;
import com.hletong.hyc.ui.fragment.SelectPlateFragment;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.pullrefresh.SpaceItemDecoration;
import com.xcheng.view.util.LocalDisplay;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SelectCardActivity extends BaseActivity implements SelectCardAdapter.OnCardSelected {
    @BindView(R.id.id_recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.apply_now)
    Button apply_now;

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_card;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setCustomTitle(R.string.choose_card);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpaceItemDecoration(LocalDisplay.dp2px(8)));

        List<ETCCard> list = new ArrayList<>();
        list.add(new ETCCard(true, getString(R.string.hl_etc_card), getString(R.string.card_detail), R.drawable.hl_etc_credit_card_small));
        list.add(new ETCCard(false, getString(R.string.hl_credit_card), getString(R.string.apply_not_online), R.drawable.hl_credit_card_s));
        list.add(new ETCCard(false, getString(R.string.hl_etc_deposit_card), getString(R.string.apply_not_online), R.drawable.hl_etc_debit_card_small));
        recyclerView.setAdapter(new SelectCardAdapter(this, list, this));
    }
//关闭申请记录接口
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.apply, menu);
//        MenuItem mi = menu.findItem(R.id.apply);
//        mi.setTitle(R.string.apply_record);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        toActivity(CommonWrapFragmentActivity.class
//                , CommonWrapFragmentActivity.getBundle(getString(R.string.apply_record), ApplyRecordFragment.class),
//                null);
//        return true;
//    }

    @OnClick({R.id.apply_now})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.apply_now:
                //     showMessage("敬请期待");
                ETCCard etcCard = (ETCCard) apply_now.getTag();
                Bundle bundle = new Bundle();
                bundle.putString("CardType", etcCard.getCardId());
                toActivity(
                        CommonWrapFragmentActivity.class,
                        CommonWrapFragmentActivity.getBundle("选择车牌号", SelectPlateFragment.class, bundle),
                        null);
                break;
        }
    }

    @Override
    public void cardSelected(ETCCard etcCard, boolean isChecked) {
        apply_now.setEnabled(isChecked);
        apply_now.setTag(etcCard);
    }
}
