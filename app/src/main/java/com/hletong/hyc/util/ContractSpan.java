package com.hletong.hyc.util;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;

import com.hletong.hyc.R;

/**
 * Created by ddq on 2017/4/10.
 */

public class ContractSpan extends ClickableSpan {
    private final int color;
    private View.OnClickListener mListener;

    public ContractSpan(Context context) {
        color = ContextCompat.getColor(context, R.color.colorAccent);
    }

    public void setListener(View.OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(color);
        ds.setUnderlineText(false);
    }

    @Override
    public void onClick(View widget) {
        if (mListener != null)
            mListener.onClick(widget);
    }

    public static ContractSpan setContractHint(int res, TextView textView) {
        SpannableString ss = new SpannableString(textView.getContext().getString(res));
        ContractSpan contractSpan = new ContractSpan(textView.getContext());
        textView.setMovementMethod(new LinkMovementMethod());
        ss.setSpan(contractSpan, 9, getEnd(res), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(ss);
        return contractSpan;
    }

    private static int getEnd(int res){
        switch (res){
            case R.string.hint_transport_contract:
                return 25;
            case R.string.hint_self_trade_contract:
                return 23;
        }
        return 23;
    }
}
