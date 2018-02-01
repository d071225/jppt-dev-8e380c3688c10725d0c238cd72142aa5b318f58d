package com.hletong.hyc;

import com.hletong.mob.util.NumberUtil;

import org.junit.Test;

/**
 * Created by dongdaqing on 2017/4/27.
 */

public class NumberUtilTest {
    private final double[] test = {
            0.029, 16.998800, 0.9999, 0.0900, 0.0980,
            10.000, 12.123, 13.4000, 14.202020, 15.66660000,
            0.0000, 0.3434, 00.44440000, 00.5002003,100000,1234567890
    };

    private final String[] test2 = {
            "13", "25", "10.000", "12.123", "13.4000", "14.202020", " 15.66660000", "0.0000", "0.3434", "00.44440000", "00.5002003"
    };

    @Test
    public void test() {
        for (int i = 0; i < test.length; i++) {
            System.out.println("input value: " + test[i] + ", output value: " + NumberUtil.format2f(test[i]));
        }

        for (int i = 0; i < test2.length; i++) {
            System.out.println("input value: " + test2[i] + ", output value: " + NumberUtil.format2f(test2[i]));
        }
    }
}
