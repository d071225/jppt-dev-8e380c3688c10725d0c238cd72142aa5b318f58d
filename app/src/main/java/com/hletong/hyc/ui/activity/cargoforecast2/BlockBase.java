package com.hletong.hyc.ui.activity.cargoforecast2;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.CargoForecastContract;
import com.hletong.hyc.presenter.cargo.ForecastPresenterManager;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.util.FinishOptions;
import com.orhanobut.logger.Logger;
import com.xcheng.view.util.LocalDisplay;
import com.xcheng.view.util.ToastLess;

import java.security.InvalidParameterException;

import butterknife.ButterKnife;

/**
 * Created by dongdaqing on 2017/8/25.
 * 货源预报，所有Block的基类
 */

public abstract class BlockBase<P extends CargoForecastContract.BlockPresenter> implements CargoForecastContract.BlockView, ValueAnimator.AnimatorUpdateListener {
    private ViewStub mViewStub;
    private LinearLayout mRootView;
    private TextView mStatusView;
    private ImageView mArrowView;
    private SparseArray<View> mCache;

    private P mPresenter;

    //最小高度和当前高度
    private final int minHeight;
    private int height;

    //Block展开收起的动画
    private ValueAnimator mOpenAnimator;
    private ValueAnimator mCloseAnimator;
    private ObjectAnimator mRotateOpenAnimator;
    private ObjectAnimator mRotateCloseAnimator;
    private AnimatorSet mAnimatorSet;

    public BlockBase(ViewStub viewStub) {
        mViewStub = viewStub;
        minHeight = LocalDisplay.dp2px(48);
        mCache = new SparseArray<>();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void handleError(@NonNull BaseError error) {
        showMessage(error.getMessage());
    }

    //显示
    @Override
    public void showBlock() {
        if (mPresenter == null) {
            mPresenter = createPresenter();
        }
        ForecastPresenterManager.attach(mPresenter);

        if (mViewStub != null) {
            mRootView = (LinearLayout) mViewStub.inflate();
            View view = LayoutInflater.from(mRootView.getContext()).inflate(R.layout.activity_cargo_forecast_block_title, mRootView, false);
            mRootView.addView(view, 0, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, minHeight));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //用户点击的时候可能正在执行展开或者收起动画，这种情况就要反向执行动画
                    if (mOpenAnimator.isRunning()) {
                        close();
                    } else if (mCloseAnimator.isRunning())
                        open();
                    else {
                        if (mRootView.getHeight() == minHeight) {
                            open();
                        } else {
                            close();
                        }
                    }
                }
            });

            //block 标题
            TextView titleView = (TextView) view.findViewById(R.id.title);
            titleView.setText(getTitle());

            mArrowView = (ImageView) view.findViewById(R.id.indicator);
            mStatusView = (TextView) view.findViewById(R.id.status);

            ButterKnife.bind(this, mRootView);
            viewInflated(mRootView);
            //计算高度
            calculateBlockHeight();

            //view 默认为收起状态
            setRootViewHeight(minHeight);

            //创建动画相关Animator
            mOpenAnimator = ValueAnimator.ofInt(minHeight, height);
            mCloseAnimator = ValueAnimator.ofInt(height, minHeight);
            mRotateOpenAnimator = ObjectAnimator.ofFloat(mStatusView, "rotation", mArrowView.getRotation(), 90);
            mRotateCloseAnimator = ObjectAnimator.ofFloat(mStatusView, "rotation", mArrowView.getRotation(), 0);

            mAnimatorSet = new AnimatorSet();
        }

        mRootView.setVisibility(View.VISIBLE);
    }

    //隐藏
    @Override
    public void hideBlock() {
        if (mRootView != null)
            mRootView.setVisibility(View.GONE);
        ForecastPresenterManager.detach(mPresenter);//不再接受任何事件
    }

    //展开
    @Override
    public void open() {
        mAnimatorSet.end();

        mOpenAnimator.setIntValues(mRootView.getHeight(), height);
        mRotateOpenAnimator.setFloatValues(mArrowView.getRotation(), 9);

        mAnimatorSet.playTogether(mOpenAnimator, mRotateOpenAnimator);
        mAnimatorSet.start();
    }

    //收起
    @Override
    public void close() {
        if (mRootView != null) {
            mAnimatorSet.end();

            mCloseAnimator.setIntValues(mRootView.getHeight(), minHeight);
            mRotateCloseAnimator.setFloatValues(mArrowView.getRotation(), 0);

            mAnimatorSet.playTogether(mCloseAnimator, mRotateCloseAnimator);
            mAnimatorSet.start();
        }
    }

    @Override
    public final void showView(int... id) {
        //将所有View全部变为显示状态，然后再计算高度，免得做重复操作
        boolean blockChanged = false;
        for (int i = 0; i < id.length; i++) {
            blockChanged |= setViewVisibility(getView(id[i]), true);
        }

        if (blockChanged)
            animateToNewHeight();
    }

    @Override
    public final void hideView(int... id) {
        //将所有View变为不可见状态，然后再计算高度，免得做重复操作
        boolean blockChanged = false;
        for (int i = 0; i < id.length; i++) {
            View view = getView(id[i]);
            if (view instanceof CommonInputView) {
                CommonInputView civ = (CommonInputView) view;
                civ.setText(null);
            }
            blockChanged |= setViewVisibility(view, true);
        }

        if (blockChanged)
            animateToNewHeight();
    }

    @Override
    public void showMessage(String message) {
        ToastLess.showToast(message);
    }

    abstract P createPresenter();

    protected String getTitle() {
        return null;
    }

    protected void viewInflated(View rootView) {
    }

    public P getPresenter() {
        return mPresenter;
    }

    //改变View的显示状态
    private boolean setViewVisibility(View view, boolean visible) {
        if (view == null)
            throw new InvalidParameterException("view must not be null");

        boolean isVisible = view.getVisibility() == View.VISIBLE;//当前是否可见
        boolean isViewStateOk = isVisible ? view.getHeight() > 0 : view.getHeight() == 0;//当前状态是否正常
        boolean isOpen = mRootView.getHeight() == minHeight;//block是否展开
        if (isVisible != visible || !isViewStateOk) {
            view.setVisibility(visible ? View.VISIBLE : View.GONE);
            return true;
        }
        return false;
    }

    private void animateToNewHeight() {
        if (mAnimatorSet.isRunning())
            mAnimatorSet.end();

        int old = mRootView.getHeight();
        calculateBlockHeight();
        ValueAnimator animator = ValueAnimator.ofInt(old, height);
        animator.addUpdateListener(this);
        animator.start();
    }

    //计算Block的高度
    private void calculateBlockHeight() {
        mRootView.measure(
                View.MeasureSpec.makeMeasureSpec(LocalDisplay.widthPixel(), View.MeasureSpec.AT_MOST),
                //有可能会有超大型block(虽然目前没有),这里就用一个大一点的值来算
                View.MeasureSpec.makeMeasureSpec(9999, View.MeasureSpec.AT_MOST)
        );
        height = mRootView.getMeasuredHeight();
        Logger.d("block " + getClass().getSimpleName() + " height:" + height);
    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {
        setRootViewHeight((Integer) animation.getAnimatedValue());
    }

    private void setRootViewHeight(int height) {
        ViewGroup.LayoutParams layoutParams = mRootView.getLayoutParams();
        layoutParams.height = height;
        mRootView.setLayoutParams(layoutParams);
    }

    public final <T extends View> T getView(int id) {
        View view = mCache.get(id);
        if (view == null) {
            view = mRootView.findViewById(id);
            mCache.put(id, view);
        }
        //noinspection unchecked
        return (T) view;
    }

    @Override
    public void finishWithOptions(FinishOptions options) {
        getTransactionView().finishWithOptions(options);
    }

    @Override
    public void toActivity(Class<? extends Activity> cls, Bundle bundle, FinishOptions options) {
        getTransactionView().toActivity(cls, bundle, options);
    }

    @Override
    public void toActivity(Intent intent, FinishOptions options) {
        getTransactionView().toActivity(intent, options);
    }

    @Override
    public void toActivity(Class<? extends Activity> cls, Bundle bundle, int requestCode, FinishOptions options) {
        getTransactionView().toActivity(cls, bundle, requestCode, options);
    }

    @Override
    public void toActivity(Intent intent, int requestCode, FinishOptions options) {
        getTransactionView().toActivity(intent, requestCode, options);
    }

    private ITransactionView getTransactionView() {
        if (mViewStub != null)
            return (ITransactionView) mViewStub.getContext();
        else
            return (ITransactionView) mRootView.getContext();
    }
}
