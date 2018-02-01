package com.hletong.hyc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.SplashContract;
import com.hletong.hyc.presenter.SplashPresenter;
import com.hletong.hyc.ui.fragment.GuideFragment;
import com.hletong.mob.base.CountDownActivity;
import com.hletong.mob.util.FinishOptions;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ddq on 2016/12/12.
 */
public class SplashActivity extends CountDownActivity<SplashContract.Presenter> implements SplashContract.View {
    @BindView(R.id.count_down)
    TextView cd;
    @BindView(R.id.image)
    ImageView image;

    @Override
    public int getLayoutId() {
        return R.layout.activity_splash;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        getPresenter().start();
    }

    @Override
    public void startCounting(int seconds) {
        image.setImageResource(BuildConfig.splash);
        super.startCounting(seconds);
    }

    @Override
    public void onTicking(long hour, long minute, long second) {
        cd.bringToFront();
        cd.setText(getString(R.string.skip) + "0" + second);
    }

    @Override
    public void countFinished() {
        Bundle bundle = new Bundle();
        if (getIntent().getData() != null) {
            bundle.putBoolean("actionShare", true);
            bundle.putString("share", getIntent().getDataString());
        }
        toActivity(MainActivity.class, bundle, FinishOptions.FINISH_ONLY());
    }

    @OnClick({R.id.count_down})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.count_down:
                //停止倒计时
                stopCounting();
                break;
        }
    }

    @Override
    protected SplashContract.Presenter createPresenter() {
        return new SplashPresenter(this);
    }

    @Override
    public void showGuideView() {
        final GuideFragment guideFragment = (GuideFragment) Fragment.instantiate(this, GuideFragment.class.getName());

        getSupportFragmentManager()
                .beginTransaction()
                .add(Window.ID_ANDROID_CONTENT, guideFragment, "guideFragment")
                .commit();

        guideFragment.setOnRemoveGuideListener(new GuideFragment.OnRemoveGuideListener() {
            @Override
            public void onRemoved() {
                getSupportFragmentManager().beginTransaction().remove(guideFragment).commit();
                //引导界面展示完毕
                getPresenter().guideFinished();
            }
        });
    }
}
