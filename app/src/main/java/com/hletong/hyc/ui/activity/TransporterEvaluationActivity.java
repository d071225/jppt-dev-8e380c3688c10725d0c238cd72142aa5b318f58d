package com.hletong.hyc.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.TransporterEvaluationContract;
import com.hletong.hyc.model.TransporterEvaluation;
import com.hletong.hyc.presenter.TransporterEvaluationPresenter;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.ui.widget.RatingBar;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.mob.base.BaseActivity;
import com.orhanobut.logger.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dongdaqing on 2017/5/11.
 * 货方评价车船
 */

public class TransporterEvaluationActivity extends BaseActivity implements TransporterEvaluationContract.View {
    @BindView(R.id.avatar)
    ImageView mAvatar;
    @BindView(R.id.name)
    TextView mName;
    @BindView(R.id.rate)
    RatingBar mRate;
    @BindView(R.id.favor_rate)
    TextView mFavorRate;
    @BindView(R.id.trade_count)
    TextView mTradeCount;
    @BindView(R.id.bus_doc_no)
    CommonInputView mBusDocNo;
    @BindView(R.id.carrier)
    CommonInputView mCarrier;
    @BindView(R.id.cargo_name)
    CommonInputView mCargoName;
    @BindView(R.id.load)
    CommonInputView mLoad;
    @BindView(R.id.unload)
    CommonInputView mUnload;
    @BindView(R.id.load_time)
    CommonInputView mLoadTime;
    @BindView(R.id.unload_time)
    CommonInputView mUnloadTime;
    @BindView(R.id.loss)
    CommonInputView mLoss;
    @BindView(R.id.count)
    CommonInputView mCount;
    @BindView(R.id.immediate)
    TextView mImmediate;
    @BindView(R.id.immediate_rate)
    RatingBar mImmediateRate;
    @BindView(R.id.cargo)
    TextView mCargo;
    @BindView(R.id.cargo_rate)
    RatingBar mCargoRate;
    @BindView(R.id.service)
    TextView mService;
    @BindView(R.id.service_rate)
    RatingBar mServiceRate;
    @BindView(R.id.advice)
    EditText mAdvice;
    @BindView(R.id.layout_root)
    View root;
    @BindView(R.id.scrollView)
    View scrollView;

    private final String[] PROGRESS_MESSAGE = {"极差", "较差", "一般", "较好", "极好"};

    private TransporterEvaluationContract.Presenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_transporter_evaluation;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);

        mImmediateRate.addOnLayoutChangeListener(new AdjustViewPosition(mImmediate));
        mImmediateRate.setOnRatingBarChangeListener(new AdjustStatus(mImmediate, "及时性："));
        mCargoRate.addOnLayoutChangeListener(new AdjustViewPosition(mCargo));
        mCargoRate.setOnRatingBarChangeListener(new AdjustStatus(mCargo, "货损货差："));
        mServiceRate.addOnLayoutChangeListener(new AdjustViewPosition(mService));
        mServiceRate.setOnRatingBarChangeListener(new AdjustStatus(mService, "服务态度："));
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d("onClick => " + scrollView.getScrollY());
                DialogFactory.showAlertWithNegativeButton(TransporterEvaluationActivity.this, "提示", "评价为完成确定要退出吗？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
            }
        });

        mPresenter = new TransporterEvaluationPresenter(
                this,
                getIntent().getStringExtra("tradeUuid"),
                getIntent().getStringExtra("billingType")
        );
        mPresenter.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.d("onResume => " + scrollView.getScrollY());
    }

    @OnClick(R.id.submit)
    public void onViewClicked() {
        mPresenter.submit(
                mImmediateRate.getRating(),
                mCargoRate.getRating(),
                mServiceRate.getRating(),
                mAdvice.getText().toString().trim());
    }

    @Override
    public void success(String message) {
        showMessage(message);
    }

    @Override
    public void showSubmitAlert(DialogInterface.OnClickListener listener) {
        DialogFactory.showAlertWithNegativeButton(getContext(), "评价确认", "确认提交评价？", listener);
    }

    @Override
    public void initSelfTradeView() {
        mCount.setVisibility(View.VISIBLE);
    }

    @Override
    public void initNormalView() {
        mLoadTime.setVisibility(View.VISIBLE);
        mUnloadTime.setVisibility(View.VISIBLE);
        mLoss.setVisibility(View.VISIBLE);
    }

    @Override
    public void initViewWithData(TransporterEvaluation te, boolean selfTrade) {
        root.setVisibility(View.VISIBLE);
        mBusDocNo.setText(te.getTradeCode());
        mCarrier.setText(te.getCarrierNo());
        mCargoName.setText(te.getOrgin_cargon_kind_name());
        mLoad.setText(te.getLoadingAddress());
        mUnload.setText(te.getUnLoadingAddress());
        if (selfTrade) {
            mCount.setLabel(te.getCargoLabel());
            mCount.setText(te.getDeliverCargoWithUnit());
        } else {
            mLoadTime.setText(te.getLoadingDttm());
            mUnloadTime.setText(te.getUnloadDttm());
            mLoss.setText(te.getLoss());
        }

        mName.setText(te.getCarrierMemberName());
        mTradeCount.setText("交易笔数：" + te.getTradeAccount());
        mRate.setProgress((float) te.getCarrierMemberGrade());
        mFavorRate.setText("好评率：" + te.getCarrierMemberGradeWithUnit());
    }

    @Override
    public void onBackPressed() {
        DialogFactory.showAlertWithNegativeButton(this, "提示", "评价为完成确定要退出吗？", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
    }

    private class AdjustViewPosition implements View.OnLayoutChangeListener {
        private View mView;

        public AdjustViewPosition(View view) {
            mView = view;
        }

        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            if (right - left > 0) {
                v.removeOnLayoutChangeListener(this);
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) mView.getLayoutParams();
                params.leftMargin = left;
                mView.setLayoutParams(params);
            }
        }
    }

    private class AdjustStatus implements RatingBar.OnRatingBarChangeListener {
        private TextView tv;
        private String prefix;

        public AdjustStatus(TextView tv, String prefix) {
            this.tv = tv;
            this.prefix = prefix;
        }

        @Override
        public void onRatingChanged(float progress, int stars) {
            tv.setText(ss((int) Math.ceil(progress * stars) - 1));
        }

        private SpannableString ss(int index) {
            SpannableString ss = new SpannableString(prefix + PROGRESS_MESSAGE[index]);
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(), R.color.yellow)), prefix.length(), ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            return ss;
        }
    }
}
