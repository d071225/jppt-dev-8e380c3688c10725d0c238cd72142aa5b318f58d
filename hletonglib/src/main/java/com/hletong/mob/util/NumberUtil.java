package com.hletong.mob.util;

import android.text.TextUtils;

import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * Created by dongdaqing on 2017/4/27.
 */

public class NumberUtil {
    private static final NumberFormat sFormat = NumberFormat.getInstance();

    /**
     * 格式化浮点数
     *
     * @param num      要格式化的内容
     * @param fraction 要保留的小数位数
     * @return
     */
    private static String format(double num, int fraction) {
        sFormat.setGroupingUsed(false);
        sFormat.setMaximumFractionDigits(fraction);
        sFormat.setRoundingMode(RoundingMode.HALF_UP);
        return sFormat.format(num);
    }

    public static String format2f(double num) {
        return format(num, 2);
    }

    public static String format3f(double num) {
        return format(num, 3);
    }

    public static String format2f(String num) {
        if (TextUtils.isEmpty(num))
            return "";

        try {
            return format(Double.parseDouble(num), 2);
        } catch (NumberFormatException e) {
            return num;
        }
    }

    public static String format3f(String num) {
        if (TextUtils.isEmpty(num))
            return "";
        try {
            return format(Double.parseDouble(num), 3);
        } catch (NumberFormatException e) {
            return num;
        }
    }
}
