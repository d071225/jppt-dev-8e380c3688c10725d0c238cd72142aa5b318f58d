package com.hletong.hyc.ui.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.widget.Checkable;

/**
 * Created by dongdaqing on 16/5/13.
 */
public class CheckableText extends AppCompatTextView implements Checkable{
    private boolean checked = false;
    private final int[] state_checked = {android.R.attr.state_checked};

    public CheckableText(Context context) {
        this(context,null);
    }

    public CheckableText(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CheckableText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        final boolean mChecked = attrs.getAttributeBooleanValue("http://schemas.android.com/apk/res/android","checked",false);
        setChecked(mChecked);
    }

    @Override
    public void setChecked(boolean checked) {
        if (this.checked ^ checked){
            this.checked = checked;
            refreshDrawableState();
        }
    }

    @Override
    public boolean isChecked() {
        return checked;
    }

    @Override
    public void toggle() {
        setChecked(!checked);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        if (checked) {
            // We are going to add 1 extra state.
            final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);

            mergeDrawableStates(drawableState, state_checked);
            return drawableState;
        } else {
            return super.onCreateDrawableState(extraSpace);
        }
    }
}
