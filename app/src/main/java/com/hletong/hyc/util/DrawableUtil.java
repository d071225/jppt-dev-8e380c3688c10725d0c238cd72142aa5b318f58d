package com.hletong.hyc.util;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

/**
 * Created by ddq on 2017/3/3.
 */

public class DrawableUtil {
    public static Drawable getDimBlackDrawable(float factor) {
        return getDimBlackDrawable(factor, null);
    }

    public static Drawable getDimBlackDrawable(float factor, ColorDrawable drawable) {
        if (drawable == null)
            drawable = new ColorDrawable();
        if (factor < 0 || factor > 1)
            factor = 0.33F;
        drawable.setColor(Color.argb((int) (factor * 255), 0, 0, 0));
        return drawable;
    }
}
