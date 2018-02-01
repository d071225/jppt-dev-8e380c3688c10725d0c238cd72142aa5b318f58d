package com.hletong.hyc.ui.dialog;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;

import com.hletong.hyc.R;
import com.hletong.hyc.http.parse.JpptParse;
import com.hletong.hyc.model.Action;
import com.hletong.hyc.model.Candidate;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.Voter;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.activity.GoodDriverActivity;
import com.hletong.hyc.ui.fragment.CandidateFragment;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.error.BusiError;
import com.hletong.mob.architect.view.IProgress;
import com.hletong.mob.http.AnimCallBack;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;
import com.xcheng.view.controller.EasyDialog;
import com.xcheng.view.controller.dialog.BottomOptionDialog;
import com.xcheng.view.controller.dialog.LoadingDialog;
import com.xcheng.view.controller.dialog.SimpleSelectListener;
import com.xcheng.view.util.JumpUtil;
import com.xcheng.view.util.ShapeBinder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by chengxin on 2017/10/18.
 */
public class DriverActionDialog extends EasyDialog implements IProgress {
    private LoadingDialog mLoadingDialog;

    @BindView(R.id.layout_title)
    View layoutTitle;
    @BindView(R.id.iv_cancel)
    ImageView ivCancel;
    @BindView(R.id.iv_difficultDriver)
    ImageView ivDifficultDriver;
    @BindView(R.id.iv_goodDriver)
    ImageView ivGoodDriver;
    //报名的状态
    public static int sSignCode;
    //活动的状态
    public static int sActionCode;
    //好司机还是特困司机
    public static boolean sIsGoodDriver;

    //是否为候选人注册，否则为投票报名
    public static boolean sIsCandidateSign;

    public static Voter sVoter;
    public static Action.Notice sNotice;


    public DriverActionDialog(@NonNull Context context, int code) {
        super(context, R.style.dialog_CommonStyle);
        sActionCode = code;
        //默认为true
        sIsCandidateSign = true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.dialog_action_good_driver_select;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        ShapeBinder.with(Color.WHITE).radii(new float[]{10, 10, 0, 0}).drawableTo(layoutTitle);
        ShapeBinder.with(getContext(), R.color.bgColor).radii(new float[]{10, 10, 10, 10}).drawableStateTo(getWindow().getDecorView());
    }

    @OnClick({R.id.iv_cancel, R.id.iv_difficultDriver, R.id.iv_goodDriver})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        dismiss();
        switch (v.getId()) {
            case R.id.iv_cancel:
                break;
            case R.id.iv_goodDriver:
                hasSign(true);
//                 JumpUtil.toActivity(getActivity(), GoodDriverActivity.class, null);
//                JumpUtil.toActivity(getActivity(), CommonWrapFragmentActivity.class
//                        , CommonWrapFragmentActivity.getBundle("列表", CandidateFragment.class));
                break;
            case R.id.iv_difficultDriver:
                hasSign(false);
                break;
        }
    }

    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(getContext());
        }
        mLoadingDialog.show();
    }

    public void hideLoading() {
        if (mLoadingDialog != null) {
            mLoadingDialog.dismiss();
        }
    }

    //查看是否已经报名
    private void hasSign(boolean isGoodDriver) {
        sIsGoodDriver = isGoodDriver;
        final OkRequest request = EasyOkHttp.get(Constant.CHECK_VOTER)
                .tag(hashCode())
                .extra(JpptParse.ENTRY, "data")
                .param("voteType", isGoodDriver ? "1" : "2")
                .param("carrierNo", LoginInfo.getLoginInfo().getLogin_name()).build();
        new ExecutorCall<Voter>(request).enqueue(new AnimCallBack<Voter>(this) {
            @Override
            public void onError(OkCall<Voter> okCall, EasyError error) {
                if (error instanceof BusiError) {
                    BusiError busiError = (BusiError) error;
                    if (busiError.getBusiCode() == 0) {
                        //data为空的情况，表示什么都没有报名
                        sSignCode = Candidate.ACT_ALL_UNREGISTER;
                        target();
                        return;
                    }
                }
                showMessage(error.getMessage());
            }

            @Override
            public void onSuccess(OkCall<Voter> okCall, Voter response) {
                sVoter = response;
                boolean isVoter = response.isVoter();
                boolean iCandidate = response.isCandidate();
                if (iCandidate) {
                    sSignCode = Candidate.ACT_CANDIDATE_REGISTERED;
                } else if (isVoter) {
                    sSignCode = Candidate.ACT_VOTE_REGISTERED;
                } else {
                    sSignCode = Candidate.ACT_ALL_UNREGISTER;
                }
                target();
            }
        });
    }

    private void target() {
        final String signText = sIsGoodDriver ? "好司机候选人" : "特困司机候选人";
        if (sActionCode == Action.CARE_DRIVER_SIGN) {
            //报名中
            if (sSignCode == Candidate.ACT_ALL_UNREGISTER) {
                JumpUtil.toActivity(getActivity(), GoodDriverActivity.class, null);
            } else {
                JumpUtil.toActivity(getActivity(), CommonWrapFragmentActivity.class
                        , CommonWrapFragmentActivity.getBundle(signText, CandidateFragment.class));
            }
        } else if (sActionCode == Action.CARE_DRIVER_VOTE) {
            //投票中
            if (sSignCode == Candidate.ACT_ALL_UNREGISTER) {
                sIsCandidateSign = false;
                //投票报名页面，然后跳转到列表
                JumpUtil.toActivity(getActivity(), GoodDriverActivity.class, null);
            } else {
                JumpUtil.toActivity(getActivity(), CommonWrapFragmentActivity.class
                        , CommonWrapFragmentActivity.getBundle(signText, CandidateFragment.class));
            }
        } else if (sActionCode == Action.CARE_DRIVER_SIGN_CARE_DRIVER_VOTE) {
            //报名投票状态
            if (sSignCode == Candidate.ACT_CANDIDATE_REGISTERED) {
                //好司机已报名
                JumpUtil.toActivity(getActivity(), CommonWrapFragmentActivity.class
                        , CommonWrapFragmentActivity.getBundle(signText, CandidateFragment.class));
            } else {
                String textOptions[] = sIsGoodDriver ? new String[]{"好司机参选", "好司机投票"} : new String[]{"特困司机参选", "特困司机投票"};
                new BottomOptionDialog.Builder(getContext()).optionTexts(textOptions).onSelectListener(new SimpleSelectListener() {
                    @Override
                    public void onOptionSelect(View view, int position) {
                        super.onOptionSelect(view, position);
                        if (position == 0) {
                            //好司机报名
                            JumpUtil.toActivity(getActivity(), GoodDriverActivity.class, null);
                        } else {
                            //好司机投票
                            if (sSignCode == Candidate.ACT_VOTE_REGISTERED) {
                                JumpUtil.toActivity(getActivity(), CommonWrapFragmentActivity.class
                                        , CommonWrapFragmentActivity.getBundle(signText, CandidateFragment.class));
                            } else if (sSignCode == Candidate.ACT_ALL_UNREGISTER) {
                                sIsCandidateSign = false;
                                JumpUtil.toActivity(getActivity(), GoodDriverActivity.class, null);
                            }
                        }
                    }
                }).show();
            }
        }
    }
}
