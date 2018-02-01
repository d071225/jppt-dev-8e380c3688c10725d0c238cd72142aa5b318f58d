package com.component.simple;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by ddq on 2016/11/7.
 */
public class NumberTextWatcher implements TextWatcher {
    private int decimal;//整数部分位数
    private int decimalFraction;//小数部分位数

    public NumberTextWatcher(int decimal, int decimalFraction) {
        this.decimal = decimal;
        this.decimalFraction = decimalFraction;
        if (decimal < 0)
            this.decimal = 0;
        if (decimalFraction < 0)
            this.decimalFraction = 0;
    }

    public NumberTextWatcher() {
        this(9, 3);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        int index = s.toString().indexOf(".");
        if (index > -1) {
            if (index == 0) {
                s.insert(0, "0");
            }

            if (index < s.length() - 1) {
                CharSequence sub = s.subSequence(index + 1, s.length());
                if (sub.length() > decimalFraction) {
                    s.delete(index + decimalFraction + 1, s.length());
                }
            }

            if (index > decimal) {
                s.delete(decimal, index);
            }
        } else {
            if (s.length() > decimal) {
                s.delete(decimal, s.length());
            }
        }
    }
}
