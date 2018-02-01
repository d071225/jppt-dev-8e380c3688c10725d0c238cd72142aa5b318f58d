package com.hletong.mob.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.view.View;

import com.hletong.mob.R;

/**
 * 只提供取消（id_cancel）按钮的监听事件，页面独立实现,负责实现一些没有复用性的Dialog
 * Created by cx on 2016/11/7.
 */
public final class CommonBottomDialog extends BottomDialog {
    private OnAccessDialogListener mOnAccessDialogListener;

    public CommonBottomDialog(Context context) {
        super(context);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        if (mOnAccessDialogListener != null) {
            mOnAccessDialogListener.initView(getWindow().getDecorView(), savedInstanceState);
            int[] ids = mOnAccessDialogListener.getLayoutAndViewIds();
            for (int index = 1; index < ids.length; index++) {
                findViewById(ids[index]).setOnClickListener(this);
            }
            View cancelView = findViewById(R.id.id_cancel);
            if (cancelView != null) {
                cancelView.setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (mOnAccessDialogListener != null && v.getId() != R.id.id_cancel) {
            mOnAccessDialogListener.onClick(v);
        }
    }

    @Override
    public int getLayoutId() {
        if (mOnAccessDialogListener != null) {
            return mOnAccessDialogListener.getLayoutAndViewIds()[0];
        }
        return 0;
    }

    public CommonBottomDialog setOnAccessDialogListener(OnAccessDialogListener l) {
        mOnAccessDialogListener = l;
        return this;
    }

    public interface OnAccessDialogListener extends View.OnClickListener {
        void initView(View rootView, Bundle savedInstanceState);

        /**
         * 返回 布局的id和需要监听点击的id的数组 eg new int[]{R.layout.dialog_exit,R.id.ok.R.id.cancel},第一位必须为布局ID
         *
         * @return
         */
        @Size(min = 1)
        @NonNull
        int[] getLayoutAndViewIds();
    }

    public static class SimpleAccessDialogListener implements OnAccessDialogListener{
        @Override
        public void initView(View rootView, Bundle savedInstanceState) {

        }

        @NonNull
        @Override
        public int[] getLayoutAndViewIds() {
            return new int[1];
        }

        @Override
        public void onClick(View v) {

        }
    }
}
