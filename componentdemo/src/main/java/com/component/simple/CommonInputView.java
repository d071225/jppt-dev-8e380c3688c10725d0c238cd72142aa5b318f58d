package com.component.simple;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.TextViewCompat;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hletong.mob.util.LocalDisplay;
import com.hletong.mob.util.NumberUtil;
import com.hletong.mob.validator.FormViewDelegate;
import com.hletong.mob.validator.InputView;
import com.hletong.mob.widget.BorderLinearLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

//@formatter:off

/**
 * Created by ddq on 2017/3/9.
 * 用以应对目前大部分界面中的输入需求和展示需求
 * 目前APP的设计中大部分内容输入控件都满足一下情况，目前定义了三种模式
 * 1.输入模式：INPUT，展示一个输入框，接受用户输入
 * 2.选择模式：SELECT，不能输入，用户选择相应数据之后在这里展示，这个模式默认会添加一个向右的箭头
 * 3.查看模式：VIEW，仅查看
 * <p>
 * 控件样式可以自由定义，比较常用的有
 * 1.输入模式
 * ------------------------------------
 * |label(hint)           input (suffix)|
 * ------------------------------------
 * 2.选择模式，一般结合底部弹出框使用
 * ------------------------------------
 * |label(hint)                   text >|
 * ------------------------------------
 * 上图中的
 * label是输入内容的标签（重量，数量，单价等），
 * hint是可选项，常用于表示该项是可选项
 * input在INPUT模式下是一个EditText，接受用户输入，在其非输入模式下input是一个TextView用于展示内容
 * suffix是可选项，通常用于表示单位，在选择模式下默认会添加一个向右的箭头
 */
//@formatter:on

public class CommonInputView extends BorderLinearLayout implements InputView {
    public static final int SELECT = 1;
    public static final int VIEW = 2;
    public static final int INPUT = 3;

    /**
     * {@link #GRAVITY_START}和{@link #GRAVITY_END}
     * 表示的是Input水平方向的位置，而{@link #GRAVITY_CENTER_VERTICAL}
     * 表示的是整个布局里面所有的child控件垂直居中
     */
    private final int GRAVITY_START = 1;
    private final int GRAVITY_END = 2;
    private final int GRAVITY_CENTER_VERTICAL = 4;
    @Override
    public boolean willValidate() {
        return mode != VIEW && getVisibility() == View.VISIBLE;
    }
    @Override
    public int order() {
        return mFormViewDelegate.order();
    }

    @Override
    public int inputType() {
        return mFormViewDelegate.inputType();
    }

    @NonNull
    @Override
    public String name() {
        return mFormViewDelegate.name();
    }

    @NonNull
    @Override
    public String key() {
        return mFormViewDelegate.key();
    }

    @NonNull
    @Override
    public String emptyMsg() {
        return mFormViewDelegate.emptyMsg();
    }

    @NonNull
    @Override
    public String rangMsg() {
        return mFormViewDelegate.rangMsg();
    }

    @Override
    public int from() {
        return mFormViewDelegate.from();
    }

    @Override
    public int to() {
        return mFormViewDelegate.to();
    }

    @Override
    public CharSequence getText() {
        return getInputValue();
    }

    @Override
    public CharSequence getHint() {
        return null;
    }

    @IntDef({SELECT, VIEW, INPUT})
    @Retention(RetentionPolicy.SOURCE)
    @interface ViewType {
    }

    public static final int NONE = 0;
    public static final int NUMBER = 1;
    public static final int NUMBER_DECIMAL = 2;
    public static final int NUMBER_PASSWORD = 3;
    public static final int PHONE = 4;
    public static final int MONEY = 5;//输入的内容与钱相关，保留两位小数

    @IntDef({NONE, NUMBER, NUMBER_DECIMAL, NUMBER_PASSWORD, PHONE, MONEY})
    @Retention(RetentionPolicy.SOURCE)
    @interface InputMethod {
    }

    private final int[] IDS_ = {R.id.label, R.id.hint, R.id.input, R.id.input_, R.id.suffix};
    private TextView label;
    private TextView labelHint;
    private EditText input;
    private TextView input_;//这个是在非输入模式下用来替代input的，这么做的原因是edittext无法在输入和点击模式下进行切换
    private TextView suffix;

    private int gravity;
    private int mode;
    private int inputType;
    private int maxLength = -1;
    private int alignInput;
    private TextWatcher mTextWatcher;
    private FormViewDelegate mFormViewDelegate;

    public CommonInputView(Context context) {
        this(context, null);
    }

    public CommonInputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonInputView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
        mFormViewDelegate = new FormViewDelegate(this);
        mFormViewDelegate.loadFromAttributes(attrs, 0);
    }

    public CommonInputView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        final int padding = LocalDisplay.dp2px(15);
        setPadding(padding, 0, padding, 0);
        setOrientation(HORIZONTAL);

        inflate(getContext(), R.layout.custom_view_common_input_view, this);
        label = (TextView) findViewById(R.id.label);
        labelHint = (TextView) findViewById(R.id.hint);
        input = (EditText) findViewById(R.id.input);
        input_ = (TextView) findViewById(R.id.input_);
        suffix = (TextView) findViewById(R.id.suffix);

        TypedArray typedValue = getContext().obtainStyledAttributes(attrs, R.styleable.CommonInputView);
        //input默认样式为右边,垂直居中
        gravity = typedValue.getInt(R.styleable.CommonInputView_inputGravity, GRAVITY_CENTER_VERTICAL | GRAVITY_END);
        if ((gravity & GRAVITY_CENTER_VERTICAL) == GRAVITY_CENTER_VERTICAL) {
            setGravity(Gravity.CENTER_VERTICAL);
            setMinimumHeight(LocalDisplay.dp2px(48));
        }

        if ((gravity & GRAVITY_END) == GRAVITY_END) {
            input.setGravity(Gravity.END);
            input_.setGravity(Gravity.END);
        }

        setTextColor(typedValue.getColor(R.styleable.CommonInputView_inputTextColor, ContextCompat.getColor(getContext(), R.color.text_black)));
        setTextSize(typedValue.getDimension(R.styleable.CommonInputView_inputTextSize, TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics())));
        setText(typedValue.getString(R.styleable.CommonInputView_inputText));
        setEditTextHint(typedValue.getString(R.styleable.CommonInputView_inputHint));

        mode = typedValue.getInt(R.styleable.CommonInputView_mode, 3);
        label.setText(typedValue.getString(R.styleable.CommonInputView_inputLabel));
        setLabelDrawable(typedValue.getDrawable(R.styleable.CommonInputView_inputLabelDrawable));
        if (typedValue.getBoolean(R.styleable.CommonInputView_inputLabelAsTitle, false))
            label.setTypeface(null, Typeface.BOLD);

        alignInput = typedValue.getDimensionPixelSize(R.styleable.CommonInputView_inputAlignLeft, Integer.MIN_VALUE);
        labelHint.setText(typedValue.getString(R.styleable.CommonInputView_inputLabelHint));
        suffix.setText(typedValue.getString(R.styleable.CommonInputView_inputTextSuffix));
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(suffix, null, null, typedValue.getDrawable(R.styleable.CommonInputView_inputIconSuffix), null);
        inputType = typedValue.getInt(R.styleable.CommonInputView_inputCustomMethod, 0);//输入方式
        maxLength = typedValue.getInt(R.styleable.CommonInputView_inputCustomLength, -1);//能够输入的最大长度
        typedValue.recycle();

        input.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    input.setSelection(input.getText().length());
                }
            }
        });
        useChanged(-1);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        final int childCount = getChildCount();
        if (childCount == 5) return;
        //只支持一个自定义View，多出来的移除掉
        View[] vs = new View[childCount];
        int firstIndex = -1;
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            if (!isDefaultView(v)) {
                firstIndex = i;
                vs[i] = v;
            }
        }

        //移除所有自定义View
        for (View v : vs) {
            removeView(v);
        }

        //自定义了View用于替代原来的Input
        input.setVisibility(GONE);
        input_.setVisibility(GONE);

        //添加自定义View
        View custom = vs[firstIndex];
        LayoutParams params = (LayoutParams) custom.getLayoutParams();
        if ((gravity & GRAVITY_CENTER_VERTICAL) == GRAVITY_CENTER_VERTICAL)
            params.gravity = Gravity.CENTER_VERTICAL;
        if ((gravity & GRAVITY_END) == GRAVITY_END) {
            /**
             * 这里的意思就是把自定义view内容靠右展示，下面讲几种做法
             * 一、把自定义View的宽度设置为match_parent，然后设置View的gravity属性，这种做法的问题是View不一定有Gravity属性
             * 支持Gravity属性的View有LinearLayout，TextView等，并不是所有的View都支持，所以这种方法不通用
             * 二、设置View属性weight = 1，这种做法问题同上
             * 找不到更好的方式之前，先这样
             */
            if (custom instanceof TextView) {
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                TextView tv = (TextView) custom;
                tv.setGravity(Gravity.END);
            } else if (custom instanceof LinearLayout) {
                params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                LinearLayout linearLayout = (LinearLayout) custom;
                linearLayout.setGravity(Gravity.END);
            }
        }
        params.leftMargin = ((MarginLayoutParams) input.getLayoutParams()).leftMargin;
        addViewInLayout(custom, 2, params);
    }

    private boolean isDefaultView(View v) {
        for (Integer in : IDS_) {
            if (in == v.getId())
                return true;
        }
        return false;
    }

    public void setMode(@ViewType int mode) {
        if (this.mode != mode) {
            final int tmp = this.mode;
            this.mode = mode;
            useChanged(tmp);
        }
    }

    public void setInputType(@InputMethod int type) {
        this.inputType = type;
        int length = maxLength == -1 ? 12 : maxLength;
        switch (inputType) {
            case NUMBER:
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                setTextWatcher(new NumberTextWatcher(length, 0));
                break;
            case NUMBER_DECIMAL:
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                setTextWatcher(new NumberTextWatcher(length - 3, 3));//默认保留三位小数
                break;
            case NUMBER_PASSWORD:
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_PASSWORD);
                setTextWatcher(new NumberTextWatcher(length, 0));
                break;
            case PHONE:
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                setTextWatcher(new NumberTextWatcher(11, 0));//手机号只能为11位
                break;
            case MONEY:
                input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                setTextWatcher(new NumberTextWatcher(length - 2, 2));//只能输入两位小数
                break;
        }
    }

    //将输入框进行左对齐，一般是多个View摆放在一起
    public void alignInput(int dimen) {
        ViewGroup.LayoutParams params = label.getLayoutParams();
        if (mode != VIEW) {
            if (params.width == dimen) {
                params.width = -2;
                label.setLayoutParams(params);
            }
        } else {
            if (params.width != dimen) {
                params.width = dimen;
                label.setLayoutParams(params);
            }
        }
    }

    public void forceAlignViewToLeft(int dimen) {
        this.alignInput = dimen;
        this.input_.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        this.input.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        if (mode != VIEW)
            setMode(VIEW);
        else {
            ViewGroup.LayoutParams params = label.getLayoutParams();
            if (params.width != dimen) {
                params.width = dimen;
                label.setLayoutParams(params);
            }
        }
    }

    public void setMaxLength(int length) {
        this.maxLength = length;
        if (mode == 3) { //仅在输入模式生效
            input.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
        }
    }

    public void setTextSize(float textSize) {
        label.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        labelHint.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        input.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        input_.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        suffix.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    public void setTextColor(int textColor) {
        label.setTextColor(textColor);
        labelHint.setTextColor(textColor);
        input.setTextColor(textColor);
        input_.setTextColor(textColor);
        suffix.setTextColor(textColor);
    }

    public void setInputASuffixTextColor(int textColor) {
        input.setTextColor(textColor);
        input_.setTextColor(textColor);
        suffix.setTextColor(textColor);
    }

    private void useChanged(int previous) {
        if (mode == INPUT) {//输入模式
            input.setVisibility(VISIBLE);
            input_.setVisibility(GONE);
        } else {//非输入模式
            input.setVisibility(GONE);
            input_.setVisibility(VISIBLE);
            //选择模式，默认加一个向右的箭头
            if (mode == SELECT && suffix.getCompoundDrawables()[2] == null) {
                TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(suffix, null, null, ContextCompat.getDrawable(getContext(), R.drawable.ic_enter_arrow), null);
            }

            if (previous == SELECT && suffix.getCompoundDrawables()[2] == null) {
                suffix.setCompoundDrawables(null, null, null, null);
            }
        }

        if (TextUtils.isEmpty(suffix.getText()) && TextViewCompat.getCompoundDrawablesRelative(suffix)[2] == null) {
            suffix.setVisibility(GONE);
        }

        setInputType(inputType);//设置输入方式
//        setMaxLength(maxLength);//设置最大数入长度
        alignInput(alignInput);//左对齐
    }

    public void setSuffixText(String suffixText) {
        suffix.setText(suffixText);
        suffix.setVisibility(VISIBLE);
    }

    public void setSuffixIcon(Drawable drawable) {
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(suffix, null, null, drawable, null);
        suffix.setVisibility(VISIBLE);
    }

    public String getInputValue() {
        return input.getText().toString().trim();
    }

    public void setHint(String s) {
        input_.setHint(s);
        input.setHint(s);
    }

    public void setText(CharSequence s) {
        input.setText(s);
        input_.setText(s);
    }

    public void setText(double s) {
        final String value = NumberUtil.format3f(s);
        input.setText(value);
        input_.setText(value);
    }

    public void setEditTextHint(String s) {
        input.setHint(s);
        input_.setHint(s);
    }

    public EditText getInput() {
        return input;
    }

    public TextView getInput_() {
        return input_;
    }

    public void setLabel(String text) {
        label.setText(text);
    }

    public void setLabelDrawable(int drawable) {
        setLabelDrawable(ContextCompat.getDrawable(getContext(), drawable));
    }

    public void setLabelDrawable(Drawable drawable) {
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(label, drawable, null, null, null);
    }

    public void setLabelTextColor(int color) {
        label.setTextColor(color);
    }

    public TextView getSuffix() {
        return suffix;
    }

    private void setTextWatcher(TextWatcher textWatcher) {
        //删除已存在的，再添加
        input.removeTextChangedListener(this.mTextWatcher);
        this.mTextWatcher = textWatcher;
        input.addTextChangedListener(mTextWatcher);
    }

    public boolean inputHaveValue() {
        return !TextUtils.isEmpty(getInputValue());
    }

    public TextView getLabel() {
        return label;
    }

    public int getMode() {
        return mode;
    }
}
