package com.hletong.hyc.ui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.xcheng.view.util.LocalDisplay;

/**
 * Created by ddq on 2016/10/25.
 */

public class LabelInputView extends RelativeLayout{
    private String titleString;
    private String hint;
    private boolean editable = true;
    private boolean required = false;
    private int charactersForAlign = 0;

    public LabelInputView(Context context) {
        this(context,null);
    }

    public LabelInputView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LabelInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public LabelInputView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        setMinimumHeight( LocalDisplay.dp2px(48));
        setGravity(Gravity.CENTER_VERTICAL);

        if (attrs != null){
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.LabelInputView);
            titleString = array.getString(R.styleable.LabelInputView_labelText);
            required = array.getBoolean(R.styleable.LabelInputView_required,false);
            editable = array.getBoolean(R.styleable.LabelInputView_editable,true);
            hint = array.getString(R.styleable.LabelInputView_hint);
            charactersForAlign = array.getInt(R.styleable.LabelInputView_align_with_characters,0);
            array.recycle();
        }

        if (editable){
            inflate(getContext(), R.layout.input_with_label,this);
        }else {
            inflate(getContext(), R.layout.custom_view_label_with_text,this);
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        TextView label = (TextView) findViewById(R.id.label);
        if (required){
            SpannableString ss = new SpannableString("* " + titleString);
            ss.setSpan(new ForegroundColorSpan(ContextCompat.getColor(getContext(),R.color.colorAccent)),0,1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            label.setText(ss);
        }else {
            label.setText(titleString);
        }

        TextView input = (TextView) findViewById(R.id.input);
        input.setHint(hint);

        LayoutParams params = (LayoutParams) input.getLayoutParams();
        params.leftMargin = calculateDistanceBetweenViews() + LocalDisplay.dp2px(12);
        input.setLayoutParams(params);
    }

    private int calculateDistanceBetweenViews(){
        if (charactersForAlign == 0)
            return 0;

        TextView label = (TextView) findViewById(R.id.label);
        final String text = label.getText().toString();
        String sample = "";
        for (int i = 0; i < charactersForAlign; i++) {
            sample += "我";
        }
        int currentWidth = label.getWidth();
        if (currentWidth <= 0){
            label.measure(MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));
            currentWidth = label.getMeasuredWidth();
        }

        label.setText(sample);
        label.measure(MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED),
                MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));

        int alignedWidth = label.getMeasuredWidth();
        label.setText(text);
        return alignedWidth - currentWidth;
    }
}
