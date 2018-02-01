package com.hletong.hyc.ui.activity.cargoforecast;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.ui.activity.CargoForecastActivity;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.dialog.selector.SelectorPrefetchListener;
import com.orhanobut.logger.Logger;
import com.xcheng.view.util.LocalDisplay;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by ddq on 2017/3/22.
 * 货源发布，所有模块的基础
 */

public abstract class BaseBlock extends SelectorPrefetchListener<DictionaryItem> implements View.OnClickListener {
    private ViewStub mViewStub;
    private View mView;

    private View indicator;
    private TextView statusView;
    private String titleString;

    private int originalHeight;
    private final int titleHeight;
    private AnimatorSet open;
    private AnimatorSet close;
    private CargoForecastActivity.BlockAction mBlockAction;
    private ITransactionView mTransactionView;
    private int billingType;//开票方式
    //动画更新
    private ValueAnimator.AnimatorUpdateListener aul = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            ViewGroup.LayoutParams layoutParams = mView.getLayoutParams();
            layoutParams.height = (int) animation.getAnimatedValue();
            mView.setLayoutParams(layoutParams);
        }
    };

    BaseBlock(ViewStub viewStub, String titleString, CargoForecastActivity.BlockAction blockAction, ITransactionView switchDelegate) {
        mViewStub = viewStub;
        this.titleString = titleString;
        this.mTransactionView = switchDelegate;
        this.mBlockAction = blockAction;
        this.titleHeight = LocalDisplay.dp2px(48);
    }

    private void inflate() {
        if (mView != null) {
            return;
        }

        mView = mViewStub.inflate();
        mView.setId(mViewStub.getId());
        mViewStub = null;

        ButterKnife.bind(this, mView);
        viewInflated(mView);

        //添加block的Title View，并设置点击事件
        ViewGroup viewGroup = (ViewGroup) mView;
        ViewGroup statusBar = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.activity_cargo_forecast_block_title, viewGroup, false);
        viewGroup.addView(statusBar, 0, new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, titleHeight));

        statusView = (TextView) statusBar.findViewById(R.id.status);
        indicator = statusBar.findViewById(R.id.indicator);

        TextView textView = (TextView) statusBar.findViewById(R.id.title);
        textView.setText(titleString);

        statusBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isOpen()) {
                    animateClose();
                    if (!isBlockSatisfied()) {
                        statusView.setText("待填写");
                    } else {
                        statusView.setText(null);
                    }
                } else {
                    animateOpen();
                    statusView.setText(null);
                }
            }
        });

        //计算真实高度
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        mView.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.AT_MOST));
        originalHeight = mView.getMeasuredHeight();

        ViewGroup.LayoutParams layoutParams = mView.getLayoutParams();
        if (layoutParams.height != titleHeight) {
            //设置mView为收起状态
            layoutParams.height = titleHeight;
            mView.setLayoutParams(layoutParams);
        }
    }

    void hideBlock() {
        setViewVisibility(mView, View.GONE);
    }

    /**
     * 显示block
     *
     * @param open open表示展开全部内容，false表示仅显示title
     */
    void showBlock(boolean open) {
        inflate();
        mView.setVisibility(View.VISIBLE);
        if (open)
            animateOpen();
        else
            animateClose();
    }

    //能否保存到草稿
    public boolean canSaveToDraft() {
        return true;
    }

    //子View的内容发生了变化，整个控件的高度可能发生了变化,如果发生了变化，重新计算高度
    final void childViewVisibilityChanged() {
        boolean isOpen = isOpen();//记录当前的状态
        DisplayMetrics displayMetrics = getContext().getResources().getDisplayMetrics();
        mView.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.AT_MOST));

        originalHeight = mView.getMeasuredHeight();
        ViewGroup.LayoutParams layoutParams = mView.getLayoutParams();
        if (layoutParams.height != mView.getMeasuredHeight()) {
            layoutParams.height = mView.getMeasuredHeight();
            mView.setLayoutParams(layoutParams);
        }

        if (isOpen ^ isOpen()) {
            if (isOpen)
                animateOpen();
            else
                animateClose();
        }
    }

    void setViewVisibility(View view, int visibility) {
        if (view == null)
            return;

        if (view instanceof CommonInputView && visibility == View.GONE) {
            //设置View消失的时候，清空数据。如果是手动调用setVisibility函数记得调用clear(CommonInputView)函数
            clear((CommonInputView) view);
        }

        if (view.getVisibility() != visibility) {
            view.setVisibility(visibility);
            childViewVisibilityChanged();
        } else if (view.getVisibility() == View.VISIBLE) {
            //检查View是否正常显示
            ViewGroup vg = (ViewGroup) view.getParent();
            Logger.d("view bottom => " + view.getBottom() + " ,parent height => " + vg.getHeight());
        }
    }

    public final void billingTypeChanged(int billingType, Source source, boolean fullCopy) {
        this.billingType = billingType;
        billingTypeChangedInternal(billingType);
        //不可见就不用加载字典项了
        if (isVisible()) {
            //界面初始化
            if (source != null) {
                initWithSource(source, fullCopy);
            }
            //预加载字典项
            mBlockAction.prefetch(this);
        }
    }

    void initWithSource(Source source, boolean fullCopy) {

    }

    void billingTypeChangedInternal(int billingType) {
        showBlock(false);
    }

    //停止一切动画
    public void stop() {
        if (open != null && open.isRunning())
            open.end();
        if (close != null && close.isRunning())
            close.end();
    }

    //当前block是否展开
    private boolean isOpen() {
//        Logger.d("height => " + titleHeight + " ,measuredHeight => " + mView.getMeasuredHeight());
        return mView.getMeasuredHeight() > titleHeight;
    }

    //整个Block是否可见
    public boolean isVisible() {
        return mViewStub == null && mView.getVisibility() == View.VISIBLE;
    }

    //展开block
    private void animateOpen() {
        stop();

        ValueAnimator va = ValueAnimator.ofInt(titleHeight, originalHeight);
        va.addUpdateListener(aul);
        ObjectAnimator oa = ObjectAnimator.ofFloat(indicator, "rotation", indicator.getRotation(), 90);
        open = new AnimatorSet();
        open.playTogether(va, oa);
        open.start();
    }

    //收起block
    private void animateClose() {
        stop();

        ValueAnimator ha = ValueAnimator.ofInt(mView.getHeight(), titleHeight);
        ha.addUpdateListener(aul);
        ObjectAnimator oa = ObjectAnimator.ofFloat(indicator, "rotation", indicator.getRotation(), 0);
        close = new AnimatorSet();
        close.playTogether(ha, oa);
        close.start();
    }

    //view已被初始化，只会在viewstub被inflate的时候调用一次
    void viewInflated(View view) {

    }

    @Override
    public void onClick(View v) {

    }

    /**
     * 通知自身的某一个字段的值发生了改变，详见{@link #onBlockFieldChanged(BaseBlock, String, Object)}
     *
     * @param fieldName
     * @param object
     */
    final void notifyItemChanged(String fieldName, Object object) {
        mBlockAction.fieldChanged(this, fieldName, object);
    }

    /**
     * 通知某一个block(非自己)的某一个字典项发生了变化
     * eg:
     * 自主交易的运费单价和货物量位于不同的block里面，要计算货物运费总价就必
     * 须监听这两个字段的变化，当有数据发生变化时会通过{@link #notifyItemChanged(String, Object)}函数进行广播
     *
     * @param block
     * @param dictType
     * @param object
     */
    public void onBlockFieldChanged(BaseBlock block, String dictType, Object object) {

    }

    /**
     * 数据获取，{@link CargoForecastActivity}在选择回调block时会根据name字段进行选择
     * 回调函数是{@link #onActivityResult(int, Intent)}
     *
     * @param cls
     * @param requestCode
     * @param bundle
     */
    protected final void startActivityForResult(Class cls, int requestCode, Bundle bundle) {
        mBlockAction.startActivity(this, cls, bundle, requestCode);
    }

    protected final void startActivity(Class cls, Bundle bundle) {
        mTransactionView.toActivity(cls, bundle, null);
    }

    /**
     * {@link #startActivityForResult(Class, int, Bundle)}的回调函数
     *
     * @param requestCode
     * @param data
     */
    public void onActivityResult(int requestCode, Intent data) {

    }

    /**
     * 显示字典项选择器，最终结果会回调{@link #onItemSelected(DictionaryItem, int)}函数
     *
     * @param fieldName 字典项的key
     * @param title     选择器的title
     * @param extra
     */
    void showSelector(String fieldName, String title, int extra) {
        mBlockAction.fetch(fieldName, title, extra, this);
    }

    /**
     * 字典项选择器的结果
     *
     * @param item  返回的选中的item，
     * @param extra 标记View的id或者数组的下标，页面有多个picker选择，可以共享此监听
     */
    @Override
    public void onItemSelected(DictionaryItem item, int extra) {

    }

    //需要预加载的项目，使用fieldName作为text，extra作为id
    //注意：这里是预加载，是指没有没有数据的时候加载的默认值，如果某一栏已经有数据
    // 了就不要放进来的，那样只会把原来的值覆盖掉
    public List<DictionaryItem> prefetchItems() {
        return null;
    }

    void showMessage(String s) {
        IBaseView baseView = (IBaseView) mView.getContext();
        baseView.handleError(ErrorFactory.getParamError(s));
    }

    public Context getContext() {
        if (mViewStub != null)
            return mViewStub.getContext();
        return mView.getContext();
    }

    public int getBillingType() {
        return billingType;
    }

    CargoForecastActivity.BlockAction getBlockAction() {
        return mBlockAction;
    }

    //block内的可填写（选择）区域是否已全部填入数据
    //这里只做数据完整性检查，不校验数据是否满足要求
    abstract boolean isBlockSatisfied();

    //校验数据，如果有未填写项目，或填写的数据格式不正确，返回错误信息，全部正确则返回null
    //这个函数和isBlockSatisfied不同，isBlockSatisfied只检查所有必填项是否有数据，不关心数据格式是否满足要求
    public abstract String getErrorMessage();

    protected View findViewById(int id) {
        return mView.findViewById(id);
    }

    //清除某个输入项的数据
    final void clear(CommonInputView civ) {
        civ.setText(null);
        civ.setTag(null);
    }

    final double getValue(EditText et) {
        final String s = et.getText().toString();
        if (s.length() > 0)
            return Double.parseDouble(s);
        return 0f;
    }

    final double getValue(CommonInputView civ) {
        return getValue(civ.getInput());
    }

    private int getIntValue(EditText et) {
        final String s = et.getText().toString();
        if (s.length() > 0)
            return Integer.parseInt(s);
        return 0;
    }

    final int getIntValue(CommonInputView civ) {
        return getIntValue(civ.getInput());
    }

    DictionaryItem getTag(View view) {
        return (DictionaryItem) view.getTag();
    }

    //这个函数只负责把已有数据填写到Source里面，不负责数据完整性校验
    //用于货源预览和草稿功能
    public void fillSource(Source source) {

    }

    //非自主交易提交给服务端的参数
    public void getBlockParams(HashMap<String, String> params) {

    }

    //自主交易用于提交给服务端的参数
    public void getSelfTradeBlockParams(HashMap<String, String> params) {

    }
}
