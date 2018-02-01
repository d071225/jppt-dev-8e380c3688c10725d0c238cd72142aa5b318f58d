package com.hletong.hyc.ui.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.UpgradeContract;
import com.hletong.hyc.model.VersionInfo;
import com.hletong.hyc.presenter.UpgradePresenter;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.ui.dialog.UpgradeFragment;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.mob.base.MvpActivity;
import com.hletong.mob.util.SPUtils;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutUsActivity extends MvpActivity<UpgradeContract.Presenter> implements UpgradeContract.View {
    @BindView(R.id.version)
    TextView version;
    @BindView(R.id.tv_call)
    TextView call;
    @BindView(R.id.update)
    CommonInputView update;

//    private Intent intentForUpgrade;

    @Override
    public int getLayoutId() {
        return R.layout.activity_about_hlet;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_CONFIGURATIONS);
            version.setText(String.format(Locale.ENGLISH, "v%s", packageInfo.versionName));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        SpannableString sb = new SpannableString(getString(R.string.phone_num));
        sb.setSpan(new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorAccent)), 3, sb.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        call.setText(sb);

        if (SPUtils.getBoolean("show_c_button", true)) {
            update.setVisibility(View.VISIBLE);
            update.getInput_().setTextColor(Color.RED);
            update.getInput_().setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
            if (SPUtils.getBoolean("found_n_v", false)) {
                update.setText("•");
            }
        } else {
            update.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.tv_hlyt, R.id.tv_hzdw, R.id.tv_call, R.id.update})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_hlyt:
                toActivity(CompanyHistoryActivity.class, null, null);
                break;
            case R.id.tv_hzdw:
                toActivity(CooperationActivity.class, null, null);
                break;
            case R.id.tv_call:
                CallPhoneDialog.getInstance().show(getSupportFragmentManager());
                break;
            case R.id.update:
                getPresenter().checkVersionInfo();
                break;
        }
    }

    @Override
    protected UpgradeContract.Presenter createPresenter() {
        return new UpgradePresenter<>(this, true);
    }

    @Override
    public void showUpgradeDialog(VersionInfo versionInfo) {
        UpgradeFragment.getInstance(versionInfo).show(getSupportFragmentManager(), UpgradeFragment.class.getName());
    }
}
