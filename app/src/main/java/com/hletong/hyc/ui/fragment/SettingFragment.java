package com.hletong.hyc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.SettingContract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.enums.Config;
import com.hletong.hyc.model.ETCCard;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.ui.activity.AboutUsActivity;
import com.hletong.hyc.ui.activity.CardDetailActivity;
import com.hletong.hyc.ui.activity.DevSettingActivity;
import com.hletong.hyc.ui.activity.ToolboxActivity;
import com.hletong.hyc.ui.activity.WebActivity;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.ui.widget.RatingBar;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.LoginFilterDelegate;
import com.hletong.hyc.util.UnicornHelper;
import com.hletong.mob.architect.error.BusiError;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.base.BaseFragment;
import com.hletong.mob.http.AnimCallBack;
import com.hletong.mob.widget.BorderTextView;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;
import com.xcheng.view.util.ToastLess;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * 设置页面
 * Created by cc on 2016/10/13.
 */
public class SettingFragment extends BaseFragment implements SettingContract.IExitView {

    @BindView(R.id.tv_userSet)
    TextView tvUserSet;
    @BindView(R.id.tv_applyHlCard)
    View applyEtc;
    @BindView(R.id.toolbox)
    BorderTextView toolbox;

    @BindView(R.id.include_evaluate)
    View includeEvaluate;
    @BindView(R.id.tv_username)
    TextView tvUserName;
    @BindView(R.id.rate)
    RatingBar ratingBar;
    @BindView(R.id.tv_percent_favourable)
    TextView tvPercentFav;
    @BindView(R.id.tv_trade_account)
    TextView tvTradeCount;

    Unbinder unbinder;
    //如果是子账号，显示
    @BindView(R.id.tv_memberCert)
    TextView tvMemberCert;

    private LoginFilterDelegate mLoginFilterDelegate;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_setting;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.phone_selector, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.call) {
            CallPhoneDialog.getInstance().show(getFragmentManager());
            return true;
        }
        return false;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        unbinder = ButterKnife.bind(this, getView());
        mLoginFilterDelegate = new LoginFilterDelegate(getActivity());
        devTool();
    }

    private void devTool() {
        //非生产环境显示的内容
        if (Config.getConfig(BuildConfig.config) != Config.PRODUCT) {
            TextView tvDev = findViewById(R.id.tv_dev_set);
            tvDev.setVisibility(View.VISIBLE);
            tvDev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toActivity(DevSettingActivity.class, null, null);
                }
            });
        }

    }

    private void filterResume() {
        if (LoginInfo.getLoginInfo() == null && mLoginFilterDelegate == null) {
            mLoginFilterDelegate = new LoginFilterDelegate(getActivity());
        }
        if (mLoginFilterDelegate != null) {
            mLoginFilterDelegate.handleResume();
        }
    }

    private boolean willFilter(View view) {
        if (getString(R.string.hl_tag_login).equals(view.getTag())) {
            return mLoginFilterDelegate != null && mLoginFilterDelegate.willFilter(view);
        }
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        LoginInfo loginInfo = LoginInfo.getLoginInfo();
        if (loginInfo != null) {
            tvUserSet.setText(String.format("会员编号: %s", loginInfo.getMember_code()));
        } else {
            tvUserSet.setText("未登录");
        }
        if (LoginInfo.isChildAccount()) {
            includeEvaluate.setVisibility(View.VISIBLE);
            tvUserName.setText(loginInfo.getUser_name());
            tvTradeCount.setText(String.format(Locale.CHINA, "交易笔数: %d", loginInfo.getTrade_account()));
            tvPercentFav.setText(String.format(Locale.CHINA, "好评率: %d%%", loginInfo.getMember_grade()));
            ratingBar.setProgress(((float) loginInfo.getMember_grade()) / 100);
        } else {
            includeEvaluate.setVisibility(View.GONE);
        }
        //车船子账号和货方账号可以显示认证入口
        if ((AppTypeConfig.isTransporter() && LoginInfo.isChildAccount()) || AppTypeConfig.isCargo()) {
            tvMemberCert.setVisibility(View.VISIBLE);
        } else {
            tvMemberCert.setVisibility(View.GONE);
        }
        //只有车主才能申请ETC
        applyEtc.setVisibility(AppTypeConfig.isTruck() ? View.VISIBLE : View.GONE);

        filterResume();
    }

    @OnClick({
            R.id.tv_userSet, R.id.tv_memberCert, R.id.tv_historyOrder, R.id.tv_kehufuwu, R.id.tv_aboutHlet,
            R.id.tv_applyHlCard, R.id.toolbox, R.id.tv_user_guide,
            R.id.userInfo})
    @Override
    public void onClick(View v) {
        if (willFilter(v)) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_memberCert:
                checkAuditing();
                break;
            case R.id.tv_historyOrder:
                AppTypeConfig.toHistoryOrder(this);
                break;
            case R.id.tv_kehufuwu:
                UnicornHelper.enter(getContext());
                break;
            case R.id.tv_applyHlCard:
                Bundle bundle = new Bundle();
                bundle.putParcelable("etc", new ETCCard(true, getString(R.string.hl_etc_card), getString(R.string.card_detail), R.drawable.hl_etc_credit_card_small));
                bundle.putInt("position", 0);
                toActivity(CardDetailActivity.class, bundle, null);
                break;
            case R.id.tv_aboutHlet:
                toActivity(AboutUsActivity.class, null, null);
                break;
            case R.id.toolbox:
                toActivity(ToolboxActivity.class, null, null);
                break;
            case R.id.tv_user_guide:
                WebActivity.openActivity(getActivity(), getString(R.string.userGuide), AppTypeConfig.getUserGuideUrl());
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    //判断证件资料是否正在审核
    private void checkAuditing() {
        OkRequest request = EasyOkHttp.get(Constant.CHECK_AUDITING)
                .param("memberCode", LoginInfo.getLoginInfo().getMember_code())
                .build();
        new ExecutorCall<CommonResult>(request).enqueue(new AnimCallBack<CommonResult>(this) {
            @Override
            public void onError(OkCall<CommonResult> okCall, EasyError error) {
                // AppTypeConfig.toMemberCert(getActivity());

                if (error instanceof BusiError) {
                    if (((BusiError) error).getBusiCode() == 600) {
                        showMessage("会员正在审核中");
                        return;
                    }
                }
                ToastLess.showToast(error.getMessage());
            }

            @Override
            public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                AppTypeConfig.toMemberCert(getActivity());
            }
        });
    }
}
