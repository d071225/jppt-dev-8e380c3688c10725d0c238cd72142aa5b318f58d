package com.hletong.hyc.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.hletong.hyc.R;
import com.hletong.hyc.model.Action;
import com.hletong.hyc.model.Candidate;
import com.hletong.hyc.ui.dialog.DriverActionDialog;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.view.IProgress;
import com.hletong.mob.http.AnimCallBack;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;
import com.xcheng.view.util.ToastLess;

import java.util.List;

public class CandidateAdapter extends HFRecyclerAdapter<Candidate> {

    public CandidateAdapter(Context context, List<Candidate> data) {
        super(context, data);
    }

    @Override
    protected boolean canBindListener() {
        return true;
    }

    @Override
    protected BaseHolder<Candidate> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new BaseHolder<>(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_apply_candidate, parent, false));
    }

    @Override
    protected void onBindItemViewHolder(BaseHolder<Candidate> holder, final int position, final Candidate candidate) {
        super.onBindItemViewHolder(holder, position, candidate);
        holder.setText(R.id.tv_name, "姓名：" + candidate.getReg_name());
        holder.setText(R.id.tv_plate, "车牌号：" + candidate.getCarrier_no());
        holder.setText(R.id.tv_vote_num, "得票数：" + candidate.getVote_num());
        holder.getView(R.id.ev_id_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Action.canVote()) {
                    ToastLess.showToast("活动尚未开始");
                    return;
                }

                OkRequest request = EasyOkHttp.form(Constant.VOTER_FOR_CANDIDATE)
                        .tag(getContext().hashCode())
                        .param("voterUuid", DriverActionDialog.sVoter.getVoterUuid())
                        .param("candidateUuids", candidate.getReg_uuid())
                        .param("machineUuid", Constant.getDeviceId())
                        .param("voteResource", 0)
                        .param("voteType", DriverActionDialog.sIsGoodDriver ? "1" : "2")
                        .build();
                new ExecutorCall<CommonResult>(request).enqueue(new AnimCallBack<CommonResult>((IProgress) getContext()) {
                    @Override
                    public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                        ToastLess.showToast("投票成功");
                        if (mOnVoterListener != null) {
                            mOnVoterListener.onVoterSuccess(candidate, position);
                        }
                    }

                    @Override
                    public void onError(OkCall<CommonResult> okCall, EasyError error) {
//                        if (error instanceof BusiError) {
//                            int code = ((BusiError) error).getBusiCode();
//                            if (code == 222) {
//                                ToastLess.showToast("投票已达上限");
//                                return;
//                            } else if (code == 333) {
//                                ToastLess.showToast("请勿重复投票");
//                                return;
//                            }
                        ToastLess.showToast(error.getMessage());

//                        }
                    }
                });
            }
        });
        ImageView imageView = holder.getView(R.id.ev_id_headerPhoto);
        Glide.with(getContext()).load(Constant.formatActionImageUrl(candidate.getImg_group_id())).into(imageView);
    }

    private OnVoterListener mOnVoterListener;

    public void setOnVoterListener(OnVoterListener onVoterListener) {
        this.mOnVoterListener = onVoterListener;
    }

    public interface OnVoterListener {

        void onVoterSuccess(Candidate candidate, int index);
    }
}
