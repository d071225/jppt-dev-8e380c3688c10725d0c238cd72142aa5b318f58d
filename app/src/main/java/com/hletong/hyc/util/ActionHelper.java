package com.hletong.hyc.util;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.xcheng.view.util.LocalDisplay;

/**
 * Created by chengxin on 2017/10/24.
 */

public class ActionHelper {
    private ViewGroup containner;
    private ImageView ivCar;
    private ImageView ivText;

    public ActionHelper(ViewGroup containner) {
        this.containner = containner;
        this.ivCar = (ImageView) containner.getChildAt(0);
        this.ivText = (ImageView) containner.getChildAt(1);
    }

    public void startAnim() {
        float translationX = containner.getTranslationX();
        float runX = LocalDisplay.widthPixel() - LocalDisplay.dp2px(100) - translationX - 10;
        PropertyValuesHolder pvhT = PropertyValuesHolder.ofFloat("translationX", translationX,
                runX);
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("scaleX", 0.5f,
                1);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleY", 0.5f,
                1);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(containner, pvhT, pvhX, pvhY);
        animator.setDuration(3000).start();
        animator.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                //flipAnimatorYShow(ivCar, ivText, 1000);
                //解决在华为7.0之后属性动画导致Vie消失的bug
                roatAnim(ivCar, ivText);
            }
        });
    }

    private void flipAnimatorYShow(final View oldView, final View newView, final long time) {
        ObjectAnimator animator1 = ObjectAnimator.ofFloat(oldView, "rotationY", 0, 90);
        final ObjectAnimator animator2 = ObjectAnimator.ofFloat(newView, "rotationY", -90, 0);
        animator2.setInterpolator(new OvershootInterpolator(2.0f));
        animator2.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                flipAnimatorYShow(newView, oldView, 1000);
            }
        });
        animator1.addListener(new SimpleAnimatorListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                oldView.setVisibility(View.GONE);
                animator2.setDuration(time).start();
                newView.setVisibility(View.VISIBLE);
            }
        });
        animator1.setDuration(time).start();
    }

    private static class SimpleAnimatorListener implements Animator.AnimatorListener {


        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationEnd(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    private void roatAnim(final View oldView, final View newView) {
        ScaleAnimation animHide = new ScaleAnimation(1, 0, 1, 1,
                Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
        animHide.setDuration(2000);
        final ScaleAnimation animShow = new ScaleAnimation(0, 1, 1, 1,
                Animation.RELATIVE_TO_PARENT, 0.5f, Animation.RELATIVE_TO_PARENT, 0.5f);
        animShow.setDuration(2000);
        animHide.setAnimationListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                super.onAnimationEnd(animation);
                oldView.setVisibility(View.INVISIBLE);
                newView.setVisibility(View.VISIBLE);
                newView.setAnimation(animShow);
            }
        });
        animShow.setAnimationListener(new SimpleAnimationListener() {
            @Override
            public void onAnimationEnd(Animation animation) {
                super.onAnimationEnd(animation);
                roatAnim(newView, oldView);
            }
        });
        oldView.startAnimation(animHide);
    }

    private static class SimpleAnimationListener implements Animation.AnimationListener {


        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
