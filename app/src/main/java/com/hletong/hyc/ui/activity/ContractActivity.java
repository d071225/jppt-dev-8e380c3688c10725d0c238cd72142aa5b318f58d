package com.hletong.hyc.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.Contract;
import com.hletong.mob.HLApplication;
import com.hletong.mob.base.CountDownActivity;
import com.hletong.mob.base.CountDownHelper;

import java.util.Locale;

import butterknife.BindView;

/**
 * Created by ddq on 2017/1/4.
 * 合同详情
 */

public abstract class ContractActivity<P extends Contract.Presenter> extends CountDownActivity<P> implements Contract.View {
    @BindView(R.id.tv_limitTime)
    TextView timeView;

    Button submitView;
    ViewGroup rootView;
    TextView contractView;//合同展示

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setCustomTitle(getIntent().getStringExtra("title"));

        rootView = (ViewGroup) findViewById(R.id.root);
        contractView = (TextView) findViewById(R.id.tv_contract);
        submitView = (Button) findViewById(R.id.btn_sign);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPresenter().start();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_sign_contract;
    }

    @Override
    public void showHtmlContract(Spanned contract) {
        contractView.setText(contract);//显示合同
    }

    @Override
    public void startCountingInMillis(long milliseconds) {
        timeView.setVisibility(View.VISIBLE);
        super.startCountingInMillis(milliseconds);
    }

    @Override
    protected long getTimeShift() {
        //倒计时的时间偏移
        return HLApplication.getDelta();
    }

    @Override
    public void showPasswordArea() {
        inflateInputArea();//载入合同展示和内容输入区域
        toggleInputArea(true);
        submitView.setVisibility(View.VISIBLE);//默认是隐藏的
        submitView.setOnClickListener(this);
    }

    @Override
    public void hideTimeView() {
        //在签约模式下，有些合同的可能没有时间显示，比如兜底合同，指定运输的合同，这时候就不要显示了，但还是可以输入合同信息
        timeView.animate().translationY(timeView.getHeight()).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                timeView.setVisibility(View.GONE);
            }
        }).start();
    }

    @Override
    public void showTimeOutView() {
        //非签约模式下，显示已结束
        setResult(RESULT_OK);
        timeView.setText("已结束");
        toggleInputArea(false);//隐藏密码输入界面
    }

    //倒计时中
    @Override
    public void onTicking(long hour, long minute, long second) {
        timeView.setText(String.format(Locale.CHINESE, "请在%s内签订合同，否则摘牌自动失效", CountDownHelper.build(hour, minute, second)));
    }

    //倒计时结束
    @Override
    public void countFinished() {
        getPresenter().signTimeOut();
    }

    /**
     * 合同不可签约时，哪些区域需要隐藏
     * 合同可输入时，哪些区域需要展示
     *
     * @param visible
     */
    abstract void toggleInputArea(boolean visible);

    /**
     * 初始化合同展示和输入区域，
     * 货方合同和车船合同使用的是不同的布局，通过这个函数加载布局文件
     */
    abstract void inflateInputArea();

    public ViewGroup getRootLayout() {
        return rootView;
    }

    public TextView getContractView() {
        return contractView;
    }

    protected View.OnClickListener getListener() {
        return this;
    }
}
