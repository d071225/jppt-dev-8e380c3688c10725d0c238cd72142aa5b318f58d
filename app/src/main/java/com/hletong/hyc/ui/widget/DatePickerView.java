package com.hletong.hyc.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.util.DatePickerUtil;
import com.hletong.hyc.util.TimePickerUtil;
import com.hletong.mob.util.SimpleDate;

import java.util.Calendar;

/**
 * Created by ddq on 2017/3/13.
 */

public class DatePickerView extends LinearLayout implements DatePickerUtil.OnDateSetListener, TimePickerUtil.OnTimeSetListener, View.OnClickListener {
    private TextView[] tvs;
    private int pickerType;
    private DatePickerUtil mDatePickerUtil;
    private TimePickerUtil mTimePickerUtil;
    private boolean view = false;

    public DatePickerView(Context context) {
        this(context, null);
    }

    public DatePickerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DatePickerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DatePickerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        setOrientation(HORIZONTAL);

        inflate(getContext(), R.layout.custom_widget_date_picker, this);
        tvs = new TextView[2];
        tvs[0] = (TextView) findViewById(R.id.from);
        TextView separator = (TextView) findViewById(R.id.separator);
        tvs[1] = (TextView) findViewById(R.id.to);
        init();

        TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.DatePickerView);
        final String se = array.getString(R.styleable.DatePickerView_pickerSeparator);
        if (!TextUtils.isEmpty(se)) {
            separator.setText(se);
        }

        pickerType = array.getInt(R.styleable.DatePickerView_pickerType, 2);
        array.recycle();

        if (pickerType == 1) {
            setHint("选择时间");
            mTimePickerUtil = new TimePickerUtil(getContext(), this);
        } else {
            setHint("选择日期");
            mDatePickerUtil = new DatePickerUtil(getContext(), this, Calendar.getInstance());
            mDatePickerUtil.setMinimalDate(Calendar.getInstance());
        }
    }

    private void init() {
        for (int i = 0; i < tvs.length; i++) {
            tvs[i].setOnClickListener(this);
            tvs[i].setTag(i);
        }
    }

    private void setHint(String hint) {
        for (TextView tv : tvs) {
            tv.setHint(hint);
        }
    }

    @Override
    public void onDateSet(int year, int monthOfYear, int dayOfMonth, int tag) {
        SimpleDate simpleDate = (SimpleDate) tvs[tag].getTag(R.id.picker_data);
        if (simpleDate == null) {
            simpleDate = new SimpleDate();
            tvs[tag].setTag(R.id.picker_data, simpleDate);
        }
        simpleDate.setDate(year, monthOfYear, dayOfMonth);
        tvs[tag].setText(simpleDate.dateString());
    }

    @Override
    public void onTimeSet(int hourOfDay, int minute, int second, int tag) {
        SimpleDate simpleDate = (SimpleDate) tvs[tag].getTag(R.id.picker_data);
        if (simpleDate == null) {
            simpleDate = new SimpleDate();
            tvs[tag].setTag(R.id.picker_data, simpleDate);
        }
        simpleDate.setTime(hourOfDay, minute, second);
        tvs[tag].setText(simpleDate.timeString());
    }

    @Override
    public void onClick(View v) {
        if (view)
            return;

        FragmentActivity fa = (FragmentActivity) getContext();
        if (pickerType == 1) {
            mTimePickerUtil.show(fa.getFragmentManager(), (Integer) v.getTag());
        } else {
            mDatePickerUtil.showDatePicker(fa.getFragmentManager(), (Integer) v.getTag());
        }
    }

    public SimpleDate getStart() {
        return (SimpleDate) tvs[0].getTag(R.id.picker_data);
    }

    public SimpleDate getEnd() {
        return (SimpleDate) tvs[1].getTag(R.id.picker_data);
    }

    public void setStart(String s) {
        set(s, 0);
    }

    public void setEnd(String s) {
        set(s, 1);
    }

    private void set(String s, int index) {
        SimpleDate sd = SimpleDate.parse(s);
        if (sd != null) {
            tvs[index].setTag(R.id.picker_data, sd);
            if (pickerType == 1) {
                tvs[index].setText(sd.timeString());
            } else {
                tvs[index].setText(sd.dateString());
            }
        }
    }

    public void setView(boolean view) {
        this.view = view;
    }

    public boolean isStartEarlierThanEnd() {
        SimpleDate sd = getStart();
        SimpleDate ed = getEnd();
        return sd.compareTo(ed) < 0;
    }
}
