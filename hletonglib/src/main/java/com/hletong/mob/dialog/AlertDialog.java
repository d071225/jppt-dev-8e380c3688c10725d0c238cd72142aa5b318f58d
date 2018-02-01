package com.hletong.mob.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LayoutRes;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialog;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.WindowManager;
import android.widget.TextView;

import com.hletong.mob.R;
import com.hletong.mob.util.ViewUtil;
import com.xcheng.view.util.LocalDisplay;

/**
 * Created by ddq on 2016/11/30.
 */
public class AlertDialog extends AppCompatDialog {

    protected AlertDialog(Context context) {
        super(context, 0);
    }

    protected AlertDialog(Context context, int theme) {
        super(context, theme == 0 ? R.style.DialogNoTitle : theme);
    }

    private AlertDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public static class Builder {
        private CharSequence positiveText;
        private OnClickListener positiveListener;
        private CharSequence negativeText;
        private OnClickListener negativeListener;
        private boolean cancelable = true;
        private OnCancelListener cancelListener;
        private CharSequence title;
        private CharSequence content;
        private Context context;
        private boolean rebuild = false;
        private AlertDialog dialog;
        private int titleGravity = Gravity.CENTER;
        private int contentGravity = Gravity.NO_GRAVITY;//不要默认居中
        private View contentView;//自定义内容区域
        private int contentViewRes = 0;//自定义内容区域
        private View customButtonArea;//自定义按钮区域

        public Builder(Context context) {
            this.context = context;
        }

        public Builder setPositiveButton(int cs, OnClickListener listener) {
            this.positiveText = context.getString(cs);
            this.positiveListener = listener;
            rebuild = true;
            return this;
        }

        public Builder setPositiveButton(CharSequence cs, OnClickListener listener) {
            this.positiveText = cs;
            this.positiveListener = listener;
            rebuild = true;
            return this;
        }

        public Builder setNegativeButton(int cs, OnClickListener listener) {
            this.negativeText = context.getString(cs);
            this.negativeListener = listener;
            rebuild = true;
            return this;
        }

        public Builder setNegativeButton(CharSequence cs, OnClickListener listener) {
            this.negativeText = cs;
            this.negativeListener = listener;
            rebuild = true;
            return this;
        }

        public Builder setContent(CharSequence content) {
            this.content = content;
            rebuild = true;
            return this;
        }

        public Builder setContent(CharSequence content, int contentGravity) {
            this.content = content;
            this.contentGravity = contentGravity;
            rebuild = true;
            return this;
        }

        public Builder setTitle(CharSequence title) {
            this.title = title;
            rebuild = true;
            return this;
        }

        public Builder setTitle(CharSequence title, int titleGravity) {
            this.title = title;
            this.titleGravity = titleGravity;
            rebuild = true;
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
            rebuild = true;
            return this;
        }

        public Builder setCancelable(boolean cancelable, OnCancelListener listener) {
            this.cancelable = cancelable;
            this.cancelListener = listener;
            rebuild = true;
            return this;
        }

        public Builder setContentView(View view) {
            contentView = view;
            rebuild = true;
            return this;
        }

        public Builder setContentView(@LayoutRes int view) {
            contentViewRes = view;
            rebuild = true;
            return this;
        }

        public Builder setCustomButtonArea(View view) {
            customButtonArea = view;
            rebuild = true;
            return this;
        }

        public AlertDialog build() {
            if (!rebuild && dialog != null)
                return dialog;

            View view = LayoutInflater.from(context).inflate(R.layout.alert_dialog_common, null);

            if (customButtonArea != null) {
                ViewGroup viewGroup = (ViewGroup) view;
                viewGroup.addView(customButtonArea, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            } else {
                if (positiveText == null && negativeText == null)
                    throw new IllegalStateException("dialog must have at least one button");

                final float radius = context.getResources().getDimension(R.dimen.tipDialog_conner);
                if (negativeText == null) {
                    ViewStub single = (ViewStub) view.findViewById(R.id.single_button);
                    TextView tv = (TextView) single.inflate();

                    tv.setId(R.id.id_positiveBtn);
                    tv.setText(positiveText);
                    ViewUtil.setDialogViewBackground(tv, ContextCompat.getColor(context, R.color.dialog_grey_bg), new float[]{0, 0, 0, 0, radius, radius, radius, radius});
                    tv.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            if (positiveListener != null) {
                                positiveListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                            } else {
                                dialog.dismiss();
                            }
                        }
                    });
                } else {
                    ViewStub two_button = (ViewStub) view.findViewById(R.id.two_button);
                    View buttons = two_button.inflate();

                    TextView po = (TextView) buttons.findViewById(R.id.id_positiveBtn);
                    ViewUtil.setDialogViewBackground(po, ContextCompat.getColor(context, R.color.dialog_grey_bg), new float[]{0, 0, 0, 0, radius, radius, 0, 0});
                    po.setText(positiveText);
                    po.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (positiveListener != null) {
                                positiveListener.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
                            } else {
                                dialog.dismiss();
                            }
                        }
                    });
                    TextView ne = (TextView) buttons.findViewById(R.id.id_negativeBtn);
                    ViewUtil.setDialogViewBackground(ne, ContextCompat.getColor(context, R.color.dialog_grey_bg), new float[]{0, 0, 0, 0, 0, 0, radius, radius});
                    ne.setText(negativeText);
                    ne.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (negativeListener != null) {
                                negativeListener.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
                            } else {
                                dialog.dismiss();
                            }
                        }
                    });
                }
            }

            TextView titleTv = (TextView) view.findViewById(R.id.title);
            if (title != null) {
                titleTv.setText(title);
            } else {
                titleTv.setVisibility(View.GONE);
            }
            titleTv.setGravity(titleGravity);
            TextView contentTv = (TextView) view.findViewById(R.id.content);
            if (contentViewRes != 0) {
                contentView = LayoutInflater.from(context).inflate(contentViewRes, (ViewGroup) view, false);
            }

            if (contentView != null) {
                ViewGroup vg = (ViewGroup) view.findViewById(R.id.content_container);
                vg.removeView(contentTv);
                ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams.topMargin = LocalDisplay.dp2px(15);
                vg.addView(contentView, layoutParams);
            } else {
                contentTv.setText(content);
                contentTv.setGravity(contentGravity);

                if (content != null)
                    titleTv.setTypeface(Typeface.DEFAULT_BOLD);

                if (content == null) {
                    contentTv.setVisibility(View.GONE);
                }
            }

            return build(view);
        }

        private AlertDialog build(View contentView) {
            dialog = new AlertDialog(context);
            dialog.setCancelable(cancelable);
            if (cancelable)
                dialog.setOnCancelListener(cancelListener);
            dialog.setContentView(contentView);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            dialog.getWindow().setLayout((int) (metrics.widthPixels * 0.8), WindowManager.LayoutParams.WRAP_CONTENT);
            return dialog;
        }

        public void show() {
            if (dialog == null && rebuild) {
                dialog = build();
            }

            if (dialog != null)
                dialog.show();
        }
    }
}
