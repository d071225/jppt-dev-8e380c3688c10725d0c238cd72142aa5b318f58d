package com.hletong.hyc.util;

import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.hletong.hyc.R;

/**
 * Created by chengxin on 2017/6/27.
 */

public class SpanFormat {
    public static void formatSpan(final TextView spanView, String spanText, int start, int end, final View.OnClickListener spanOnClick) {
        SpannableString ss = new SpannableString(spanText);
        ss.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                spanOnClick.onClick(widget);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(ContextCompat.getColor(spanView.getContext(), R.color.colorAccent));
                ds.setUnderlineText(false);    //去除超链接的下划线
            }
        }, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        spanView.setMovementMethod(LinkMovementMethod.getInstance());//必须设置否则无效
        spanView.setText(ss);
    }

    public static void formatSpan(final TextView spanView, @StringRes int spanTextRes, int start, int end, final View.OnClickListener spanOnClick) {
        formatSpan(spanView, spanView.getContext().getString(spanTextRes), start, end, spanOnClick);
    }

    public static void formatSpanEnd(final TextView spanView, @StringRes int spanTextRes, int start, final View.OnClickListener spanOnClick) {
        String spanText = spanView.getContext().getString(spanTextRes);
        formatSpan(spanView, spanView.getContext().getString(spanTextRes), start, spanText.length(), spanOnClick);
    }

    public static void formatSpanEnd(final TextView spanView, String spanText, int start, final View.OnClickListener spanOnClick) {
        formatSpan(spanView, spanText, start, spanText.length(), spanOnClick);
    }
}
