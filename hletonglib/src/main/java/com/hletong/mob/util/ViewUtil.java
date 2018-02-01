package com.hletong.mob.util;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.xcheng.view.util.ColorUtil;

/**
 * Created by ddq on 2016/10/24.
 */

public class ViewUtil {
    public static void setBackground(View v, Drawable drawable) {
        if (hasJellyBean()) {
            v.setBackground(drawable);
        } else {
            v.setBackgroundDrawable(drawable);
        }
    }

    public static void setBackground(View view, @DrawableRes int id) {
        setBackground(view, ContextCompat.getDrawable(view.getContext(), id));
    }

    public static void setDialogViewBackground(View view, int color, float[] radii) {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, getShapeDrawable(ColorUtil.pressed(color), radii));
        drawable.addState(new int[]{-android.R.attr.state_pressed}, getShapeDrawable(color, radii));
        setBackground(view, drawable);
    }

    private static Drawable getShapeDrawable(int color, float[] radius) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setColor(color);
        drawable.setCornerRadii(radius);
        return drawable;
    }

    public static boolean hasJellyBean() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN;
    }

    public static boolean hasKitkat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
    }

    public static boolean hasLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static boolean hasNougat() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.N;
    }
}
