package com.hletong.hyc.ui.fragment;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.AppContract;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.UnTodo;
import com.hletong.hyc.ui.activity.AppGuideHelper;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.activity.MessageActivity;
import com.hletong.hyc.util.ActionHelper;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.mob.widget.UndoTextView;
import com.xcheng.view.EasyView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页各模块入口
 * Created by cc on 2016/10/13.
 */
public class AppTruckShipFragment extends AppFragment implements AppContract.View {
    @BindView(R.id.gjzp)
    UndoTextView gjzp;
    @BindView(R.id.jjtb)
    UndoTextView jjtb;
    @BindView(R.id.wqcy)
    UndoTextView wqcy;
    @BindView(R.id.fhd)
    UndoTextView fhd;
    @BindView(R.id.shd)
    UndoTextView shd;
    @BindView(R.id.xx)
    UndoTextView xx;
    @BindView(R.id.anim_container)
    ViewGroup animContainer;
    ActionHelper mActionHelper;
    private int resumeCount;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_app;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (LoginInfo.hasLogin()) {
            //公司主账号不能参加
            if (LoginInfo.isCompany() && !LoginInfo.isChildAccount()) {
                animContainer.setVisibility(View.GONE);
            } else {
                resumeCount++;
                animContainer.setVisibility(View.VISIBLE);
                mActionHelper = new ActionHelper(animContainer);
                if (resumeCount == 2) {
                    mActionHelper.startAnim();
                }
            }
        }
        animContainer.setVisibility(View.GONE);
    }

    @Override
    protected void autoAdFirstOpen() {
        Bundle bundle = new Bundle();
        if (getActivity().getIntent().getStringExtra("share") != null) {
            bundle.putString("share", getActivity().getIntent().getStringExtra("share"));
        }
        toActivity(CommonWrapFragmentActivity.class, CommonWrapFragmentActivity.getBundle(EasyView.getString(R.string.cargo_block), SourceListFragmentPublic.class, bundle), null);
    }

    @Override
    protected void appGuideFirstOpen(AppGuideHelper guideHelper) {
        guideHelper.showTruckAndShipTipView();
    }

    @OnClick({R.id.ylyb, R.id.gjzp, R.id.jjtb, R.id.wqcy, R.id.hygg, R.id.fhd, R.id.shd, R.id.xx, R.id.zf, R.id.anim_container})
    @Override
    public void onClick(android.view.View v) {
        if (v.getId() == R.id.hygg) {
            toActivity(CommonWrapFragmentActivity.class, CommonWrapFragmentActivity.getBundle(getString(R.string.cargo_block), SourceListFragmentPublic.class), null);
        } else {
            if (willFilter(v)) {
                return;
            }

            switch (v.getId()) {
                case R.id.ylyb:
                    toActivity(CommonWrapFragmentActivity.class, CommonWrapFragmentActivity.getBundle(getString(R.string.transport_forecast), TransportHistoryListFragment.class), null);
                    //toActivity(TruckPersonalChildCertActivity.class,null,null);
                    break;
                case R.id.gjzp:
                    toActivity(CommonWrapFragmentActivity.class, CommonWrapFragmentActivity.getBundle(getString(R.string.gjzp), GuaJiaListFragment.class), null);
                    break;
                case R.id.jjtb:
                    toActivity(CommonWrapFragmentActivity.class, CommonWrapFragmentActivity.getBundle(getString(R.string.competitive_bidding), CompetitiveBiddingListFragment.class), null);
                    break;
                case R.id.wqcy:
                    toActivity(CommonWrapFragmentActivity.class, CommonWrapFragmentActivity.getBundle(getString(R.string.contract_block), ContractUnSignedFragment.class), null);
                    break;
                case R.id.fhd:
                    toActivity(CommonWrapFragmentActivity.class, CommonWrapFragmentActivity.getBundle(getString(R.string.invoice_block), InvoiceFragment.class), null);
                    break;
                case R.id.shd:
                    toActivity(CommonWrapFragmentActivity.class, CommonWrapFragmentActivity.getBundle(getString(R.string.receipt_block), ReceiptFragment.class), null);
                    break;
                case R.id.xx:
                    toActivity(MessageActivity.class, null, null);
                    break;
                case R.id.zf:
                    showMessage("敬请期待");
                    break;
                case R.id.anim_container:
                    getCurrentState();
                    break;
            }
        }
    }

    @Override
    public void setUnToDoInfo(UnTodo unTodo) {
        wqcy.setUndoMessage(unTodo.getWangqianchengyunhetong());
        fhd.setUndoMessage(unTodo.getFahuodanhuichuang());
        shd.setUndoMessage(unTodo.getShouhuodanhuichuan());
        xx.setUndoMessage(unTodo.getMessageCount());
        gjzp.setUndoMessage(unTodo.getGuajizhaipai());
        jjtb.setUndoMessage(unTodo.getJinjiazhaipai());
    }

    /**
     * 获取好司机当前的状态
     */
    private void getCurrentState() {
        DialogFactory.showAlert(getContext(), "通知", "投票已经结束，谢谢您的参与，我们正在对投票结果核查，稍后公布最终结果！");
//        final OkRequest request = EasyOkHttp.get("action/public/hletong/api/activity/action/list/get")
//                .extra(JpptParse.ENTRY, "data")
//                .build();
//        new ExecutorCall<Action>(request).enqueue(new AnimCallBack<Action>(this) {
//            @Override
//            public void onError(OkCall<Action> okCall, EasyError error) {
//                if (error instanceof BusiError) {
//                    BusiError busiError = (BusiError) error;
//                    switch (busiError.getBusiCode()) {
//                        case Action.CARE_DRIVER_SIGN:
//                        case Action.CARE_DRIVER_VOTE:
//                        case Action.CARE_DRIVER_SIGN_CARE_DRIVER_VOTE:
//                            try {
//                                JSONObject jsonObject = new JSONObject(busiError.getResponse());
//                                Action action = JSONUtils.fromJson(jsonObject.getString("data"), Action.class);
//                                DriverActionDialog.sNotice = action.getNotices().get(0);
//                                new DriverActionDialog(getContext(), busiError.getBusiCode()).show();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            return;
//                        case Action.BEFORE_CARE_DRIVER:
//                            showMessage("活动尚未开始");
//                            break;
//                        case Action.CARE_DRIVER_END:
//                            showMessage("活动已结束");
//                            break;
//                        default:
//                            showMessage(error.getMessage());
//                            break;
//                    }
//                } else {
//                    showMessage(error.getMessage());
//                }
//            }
//
//            @Override
//            public void onSuccess(OkCall<Action> okCall, Action response) {
//
//            }
//        });
    }
}
