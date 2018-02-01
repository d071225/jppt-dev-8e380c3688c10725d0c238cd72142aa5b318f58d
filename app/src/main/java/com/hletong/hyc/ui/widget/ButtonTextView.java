package com.hletong.hyc.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.mob.util.ViewUtil;
import com.xcheng.view.util.LocalDisplay;

/**
 * Created by ddq on 2016/10/24.
 */

public class ButtonTextView extends LinearLayout{
    private int startColor;
    private int endColor;
    private String title;
    private String text;

    public ButtonTextView(Context context) {
        this(context,null,0);
    }

    public ButtonTextView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public ButtonTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ButtonTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        inflate(getContext(), R.layout.text_button,this);
        if (attrs == null) return;
        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.ButtonTextView);
        startColor = array.getColor(R.styleable.ButtonTextView_bg_startColor, Color.WHITE);
        endColor = array.getColor(R.styleable.ButtonTextView_bg_endColor, Color.WHITE);
        title = array.getString(R.styleable.ButtonTextView_button_title);
        text = array.getString(R.styleable.ButtonTextView_button_text);
        array.recycle();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadius( LocalDisplay.dp2px(8));
        if (ViewUtil.hasJellyBean()){
            drawable.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
            drawable.setColors(new int[]{startColor,endColor});
        }
        TextView textView = (TextView) findViewById(R.id.text);
        textView.setText(this.text);
        ViewUtil.setBackground(textView,drawable);

        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(this.title);
    }
}
