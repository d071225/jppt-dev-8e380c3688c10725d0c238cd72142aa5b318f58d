package com.hletong.hyc.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.xcheng.view.util.LocalDisplay;

/**
 * Created by dongdaqing on 2017/7/13.
 */

public class SmoothClickCheckBox extends LinearLayout implements View.OnClickListener {
    private CheckBox labelView;
    private TextView[] spanViews;
    private int[] res;
    private int textSize;
    private int textColor;
    private int spanColor;
    private int IMAGE_SIZE;
    private int space;
    private OnSpanClickListener mListener;
    private CompoundButton.OnCheckedChangeListener mCheckedChangeListener;

    public SmoothClickCheckBox(Context context) {
        this(context, null);
    }

    public SmoothClickCheckBox(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SmoothClickCheckBox(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public SmoothClickCheckBox(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    @SuppressWarnings("ResourceType")
    private void init(AttributeSet attrs) {
        IMAGE_SIZE = LocalDisplay.dp2px(20);
        space = LocalDisplay.dp2px(8);

        setOrientation(VERTICAL);

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.SmoothClickCheckBox);
        spanColor = array.getColor(R.styleable.SmoothClickCheckBox_spanColor, ContextCompat.getColor(getContext(), R.color.colorAccent));
        CharSequence[] spans = array.getTextArray(R.styleable.SmoothClickCheckBox_spans);
        array.recycle();

        final int[] textStyle = {android.R.attr.textColor, android.R.attr.textSize, android.R.attr.text};
        TypedArray ts = getContext().obtainStyledAttributes(attrs, textStyle);
        textColor = ts.getColor(0, ContextCompat.getColor(getContext(), R.color.text_black));
        textSize = ts.getDimensionPixelSize(1, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 14, getResources().getDisplayMetrics()));
        CharSequence label = ts.getText(2);
        ts.recycle();

        setLabel(label);
        setSpans(spans);
    }

    public void setLabel(CharSequence label) {
        if (label != null) {
            if (labelView == null) {
                labelView = new CheckBox(getContext());
                labelView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                labelView.setTextColor(textColor);
                labelView.setText(label);
                labelView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (mCheckedChangeListener != null) {
                            mCheckedChangeListener.onCheckedChanged(labelView, labelView.isChecked());
                        }
                    }
                });
                addView(labelView, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            }
            labelView.setVisibility(VISIBLE);
        } else {
            if (labelView != null)
                labelView.setVisibility(GONE);
        }
    }

    public void setSpans(CharSequence[] spans) {
        if (spans == null)
            return;

        if (spanViews != null) {
            for (int i = 0; i < spanViews.length; i++) {
                spanViews = null;
                removeAllViews();

                if (labelView != null)
                    addView(labelView);
            }
        }

        spanViews = new TextView[spans.length];
        for (int i = 0; i < spans.length; i++) {
            TextView tv = new TextView(getContext());
            spanViews[i] = tv;
            tv.setTextColor(spanColor);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
            tv.setText(spans[i]);
            tv.setTag(i);
            tv.setOnClickListener(this);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.topMargin = space;
            lp.leftMargin = LocalDisplay.dp2px(24);
            addView(tv, lp);
        }
    }

    @Override
    public void onClick(View v) {
        mListener.onSpanClick((Integer) v.getTag());
    }

    public void setListener(OnSpanClickListener listener) {
        mListener = listener;
    }

    public void setCheckedChangeListener(CompoundButton.OnCheckedChangeListener checkedChangeListener) {
        mCheckedChangeListener = checkedChangeListener;
    }

    public interface OnSpanClickListener {
        void onSpanClick(int index);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {

    }
}
