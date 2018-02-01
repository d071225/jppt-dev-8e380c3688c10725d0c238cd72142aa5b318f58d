package com.hletong.hyc.ui.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.CandidateAdapter;
import com.hletong.hyc.model.Candidate;
import com.hletong.hyc.model.Voter;
import com.hletong.hyc.ui.activity.CandidateInfoActivity;
import com.hletong.hyc.ui.dialog.DriverActionDialog;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.SimpleTextWatcher;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.EmptyView;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.view.adapter.SpaceDecoration;
import com.xcheng.view.util.JumpUtil;
import com.xcheng.view.util.LocalDisplay;
import com.xcheng.view.util.ShapeBinder;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 候选人列表
 */
public class CandidateFragment extends BaseRefreshFragment<Candidate> {
    @BindView(R.id.et_condition)
    EditText etCondition;
    @BindView(R.id.tv_totalVotes)
    TextView tvTotalVotes;
    @BindView(R.id.tv_hasvotes)
    TextView tvHasvotes;

    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.CANDIDATE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_candidates;
    }

    Voter mVoter = DriverActionDialog.sVoter;

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this, getView());
        EventBus.getDefault().register(this);
        if (mVoter != null) {
            tvTotalVotes.setText("今日可投：" + mVoter.getRemainVotes());
            tvHasvotes.setText("历史已投：" + mVoter.getTotalVotes());
        }

        ShapeBinder.with(Color.TRANSPARENT).stroke(ContextCompat.getColor(getContext(), R.color.divider)).radius(LocalDisplay.dp2px(5)).drawableTo(etCondition);
        etCondition.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                super.afterTextChanged(s);
                EasyOkHttp.cancel(CandidateFragment.this.hashCode());
                requestData(true);
            }
        });
        mAdapter.setOnItemClickListener(new HFRecyclerAdapter.OnItemClickListener<Candidate>() {
            @Override
            public void onItemClick(View view, BaseHolder<Candidate> holder, int position) {
                JumpUtil.toActivityForResult(CandidateFragment.this, CandidateInfoActivity.class, 1, JumpUtil.getBundle(Candidate.class.getName(), mAdapter.getItem(position)));
            }
        });
        CandidateAdapter adapter = (CandidateAdapter) mAdapter;
        adapter.setOnVoterListener(new CandidateAdapter.OnVoterListener() {

            @Override
            public void onVoterSuccess(Candidate candidate, int index) {
                refreshVoter(candidate);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onVoterSuccess(EventVoter eventVoter) {
        refreshVoter(eventVoter.candidate);
    }

    private void refreshVoter(Candidate candidate) {
        if (mVoter != null) {
            mVoter.downRemainVotes();
            mVoter.upTotalVotes();
            tvTotalVotes.setText("今日可投：" + mVoter.getRemainVotes());
            tvHasvotes.setText("历史已投：" + mVoter.getTotalVotes());
        }
        String uuid = candidate.getReg_uuid();
        int position = -1;
        for (int index = 0; index < mAdapter.getDataCount(); index++) {
            Candidate temp = mAdapter.getItem(index);
            if (temp.getReg_uuid().equals(uuid)) {
                temp.upVote_num();
                position = index;
                break;
            }
        }
        if (position != -1) {
            mAdapter.notifyItemChanged(position);
        } else {
            //强制刷新
            mPtrFrameLayout.autoRefresh();
        }
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<Candidate> createAdapter() {
        return new CandidateAdapter(getActivity(), null);
    }

    @Override
    public ParamsHelper getRequestJson(boolean isRefresh) {
        return getCommonRequestValue(isRefresh).put("condition", etCondition.getText().toString()).put("driverType", DriverActionDialog.sIsGoodDriver ? 1 : 2);
    }

    @Override
    public EmptyView getEmptyView() {
        return super.getEmptyView().setEmptyLogo(R.drawable.apply_record);
    }

    @Override
    protected String getEntry() {
        return "data";
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new SpaceDecoration(2);
    }

    public static class EventVoter {
        private Candidate candidate;

        public EventVoter(Candidate candidate) {
            this.candidate = candidate;
        }
    }
}
