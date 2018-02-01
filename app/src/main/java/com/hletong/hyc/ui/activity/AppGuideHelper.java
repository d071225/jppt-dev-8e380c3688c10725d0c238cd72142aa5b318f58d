package com.hletong.hyc.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.RectF;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.mob.util.ViewUtil;
import com.xcheng.view.util.LocalDisplay;

import zhy.com.highlight.HighLight;
import zhy.com.highlight.interfaces.HighLightInterface;
import zhy.com.highlight.position.OnBaseCallback;
import zhy.com.highlight.shape.CircleLightShape;

/**
 * Created by chengxin on 2017/3/31.
 */
public class AppGuideHelper {
    private Activity mActivity;
    private HighLight mHighLight;
    private int mTipPosition;
    private int moduleCount;

    public AppGuideHelper(Activity activity) {
        mActivity = activity;
    }

    public void showTruckAndShipTipView() {
        moduleCount = 9;
        mHighLight = new HighLight(mActivity)//
                .autoRemove(false)//设置背景点击高亮布局自动移除为false 默认为true
                .intercept(true)//拦截属性默认为true 使下方callback生效
                .enableNext()//开启next模式并通过show方法显示 然后通过调用next()方法切换到下一个提示布局，直到移除自身
                //  .anchor(mActivity.getWindow().getDecorView())//如果是Activity上增加引导层，不需要设置anchor
                /**运力预报**/
                .addHighLight(R.id.ylyb, R.layout.guide_truck_ship_ylyb, new OnBaseCallback() {

                    /**
                     * @param rightMargin R.id.ylyb 相对到anchor 右侧的距离
                     * @param bottomMargin  R.id.ylyb 相对到anchor 底部的距离
                     * @param rectF  R.id.ylyb 相对到anchor 的位置，类似于子空间在父控件中的位置Rect ,包含 left top right bottom的信息
                     * @param marginInfo 添加 R.layout.guide_truck_ship_ylyb 所需的位置信息
                     */
                    @Override
                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                        marginInfo.rightMargin = rightMargin - dp2px(100);
                        marginInfo.topMargin = rectF.bottom - dp2px(20);
                    }
                }, new CircleLightShape())
                /**挂架摘牌**/
                .addHighLight(R.id.gjzp, R.layout.guide_truck_ship_gjzp, new OnBaseCallback() {
                    @Override
                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                        marginInfo.rightMargin = rightMargin - dp2px(35);
                        marginInfo.topMargin = rectF.bottom - dp2px(20);
                    }
                }, new CircleLightShape())
                /**竞价投标**/
                .addHighLight(R.id.jjtb, R.layout.guide_truck_ship_jjtb, new OnBaseCallback() {
                    @Override
                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                        marginInfo.rightMargin = rectF.width() - dp2px(60);
                        marginInfo.topMargin = rectF.bottom - dp2px(10);
                    }
                }, new CircleLightShape())
                /**网签承运**/
                .addHighLight(R.id.wqcy, R.layout.guide_truck_ship_wqcy, new OnBaseCallback() {
                    @Override
                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                        marginInfo.leftMargin = dp2px(20);
                        marginInfo.topMargin = rectF.bottom - dp2px(20);
                    }
                }, new CircleLightShape())
                /**货源公告**/
                .addHighLight(R.id.hygg, R.layout.guide_truck_ship_hygg, new OnBaseCallback() {
                    @Override
                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                        marginInfo.rightMargin = rightMargin - dp2px(70);
                        marginInfo.topMargin = rectF.bottom - dp2px(20);
                    }
                }, new CircleLightShape())
                /**发货单**/
                .addHighLight(R.id.fhd, R.layout.guide_truck_ship_fhd, new OnBaseCallback() {
                    @Override
                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                        marginInfo.rightMargin = rectF.width() - dp2px(85);
                        marginInfo.topMargin = rectF.bottom - dp2px(20);
                    }
                }, new CircleLightShape())
                /**收货单**/
                .addHighLight(R.id.shd, R.layout.guide_truck_ship_shd, new OnBaseCallback() {

                    @Override
                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                        marginInfo.leftMargin = rectF.left + rectF.width() / 2;
                        marginInfo.bottomMargin = bottomMargin + rectF.height() - dp2px(20);
                    }
                }, new CircleLightShape())
                /**信息**/
                .addHighLight(R.id.xx, R.layout.guide_truck_ship_xx, new OnBaseCallback() {

                    @Override
                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                        marginInfo.leftMargin = rectF.left - dp2px(70);
                        marginInfo.bottomMargin = bottomMargin + rectF.height() - dp2px(20);
                    }
                }, new CircleLightShape())
                /**支付**/
                .addHighLight(R.id.zf, R.layout.guide_truck_ship_zf, new OnBaseCallback() {

                    @Override
                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                        marginInfo.rightMargin = rightMargin + dp2px(20);
                        marginInfo.bottomMargin = bottomMargin + rectF.height() - dp2px(20);
                    }
                }, new CircleLightShape()).setOnShowCallback(new HighLightInterface.OnShowCallback() {
                    @Override
                    public void onShow() {
                        addNextAndSkipView();
                    }
                });
        mHighLight.show();
    }

    private void addNextAndSkipView() {
        mTipPosition++;
        if (mTipPosition > moduleCount)
            return;
        Button nextBtn = new Button(mActivity);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });
        nextBtn.setText(mTipPosition != moduleCount ? "下一步" : "立即体验");
        nextBtn.setBackgroundResource(R.drawable.home_guide_next);
        nextBtn.setTextSize(20);
        nextBtn.setTextColor(Color.WHITE);
        FrameLayout.LayoutParams lpNext = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lpNext.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        lpNext.bottomMargin = dp2px(80);

        TextView shipText = new TextView(mActivity);
        shipText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHighLight.remove();
            }
        });
        shipText.setText("跳过");
        shipText.setTextSize(16);
        shipText.setTextColor(Color.WHITE);
        FrameLayout.LayoutParams lpSkip = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lpSkip.gravity = Gravity.RIGHT | Gravity.TOP;
        //api>=19以内容延伸到状态栏
        lpSkip.topMargin = dp2px(10 + (ViewUtil.hasKitkat() ? 25 : 0));
        lpSkip.rightMargin = dp2px(15);
        if (mHighLight.getHightLightView() != null) {
            mHighLight.getHightLightView().addView(nextBtn, lpNext);
            mHighLight.getHightLightView().addView(shipText, lpSkip);
        }
    }

    public void showCargoTipView() {
        moduleCount = 6;
        mHighLight = new HighLight(mActivity)//
                .autoRemove(false)//设置背景点击高亮布局自动移除为false 默认为true
                .intercept(true)//拦截属性默认为true 使下方callback生效
                .enableNext()//开启next模式并通过show方法显示 然后通过调用next()方法切换到下一个提示布局，直到移除自身
                //  .anchor(mActivity.getWindow().getDecorView())//如果是Activity上增加引导层，不需要设置anchor
                /**货源预报**/
                .addHighLight(R.id.hyyb, R.layout.guide_cargo_hyyb, new OnBaseCallback() {
                    @Override
                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                        marginInfo.rightMargin = rightMargin - dp2px(100);
                        marginInfo.topMargin = rectF.bottom - dp2px(20);
                    }
                }, new CircleLightShape())
                /**货方合同*/
                .addHighLight(R.id.hfht, R.layout.guide_cargo_hfht, new OnBaseCallback() {
                    @Override
                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                        marginInfo.rightMargin = rightMargin - dp2px(35);
                        marginInfo.topMargin = rectF.bottom - dp2px(20);
                    }
                }, new CircleLightShape())
                /**发货单**/
                .addHighLight(R.id.fhd, R.layout.guide_cargo_fhd, new OnBaseCallback() {
                    @Override
                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                        marginInfo.rightMargin = rectF.width() - dp2px(60);
                        marginInfo.topMargin = rectF.bottom - dp2px(10);
                    }
                }, new CircleLightShape())
                /**收货单**/
                .addHighLight(R.id.shd, R.layout.guide_cargo_shd, new OnBaseCallback() {
                    @Override
                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                        marginInfo.leftMargin = dp2px(20);
                        marginInfo.topMargin = rectF.bottom - dp2px(20);
                    }
                }, new CircleLightShape())
                /**信息**/
                .addHighLight(R.id.xx, R.layout.guide_cargo_xx, new OnBaseCallback() {
                    @Override
                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                        marginInfo.rightMargin = rightMargin - dp2px(70);
                        marginInfo.topMargin = rectF.bottom - dp2px(20);
                    }
                }, new CircleLightShape())
                /**支付**/
                .addHighLight(R.id.zf, R.layout.guide_cargo_zf, new OnBaseCallback() {
                    @Override
                    public void getPosition(float rightMargin, float bottomMargin, RectF rectF, HighLight.MarginInfo marginInfo) {
                        marginInfo.rightMargin = rectF.width() - dp2px(30);
                        marginInfo.topMargin = rectF.bottom - dp2px(20);
                    }
                }, new CircleLightShape()).setOnShowCallback(new HighLightInterface.OnShowCallback() {
                    @Override
                    public void onShow() {
                        addNextAndSkipView();
                    }
                });
        mHighLight.show();
    }

    private void next() {
        mHighLight.next();
        addNextAndSkipView();
    }

    private int dp2px(int dp) {
        return LocalDisplay.dp2px(dp);
    }
}
