package com.hletong.mob.util;

import android.animation.Animator;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.view.View;

/**
 * Created by ddq on 2017/3/6.
 */

public class AnimationUtil {
    public static void scaleIn(final View v){
        v.animate().cancel();
        if (v.getVisibility() == View.INVISIBLE || v.getVisibility() == View.GONE){
            v.setScaleX(0);
            v.setScaleY(0);
            v.setAlpha(0);
        }

        v.animate().scaleX(1)
                .scaleY(1)
                .alpha(1)
                .setInterpolator(new LinearOutSlowInInterpolator())
                .setListener(new SimpleAnimatorListener(){
                    @Override
                    public void onAnimationStart(Animator animation) {
                        v.setVisibility(View.VISIBLE);
                    }
                }).start();
    }

    public static void scaleOut(final View v){
        v.animate().cancel();
        v.animate().scaleX(0)
                .scaleY(0)
                .alpha(0)
                .setInterpolator(new FastOutLinearInInterpolator())
                .setListener(new SimpleAnimatorListener(){
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        v.setVisibility(View.INVISIBLE);
                    }
                })
                .start();
    }
}
