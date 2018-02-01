package com.hletong.hyc.util;

import android.app.DatePickerDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.widget.DatePicker;

import com.hletong.mob.util.ViewUtil;

import java.util.Calendar;

/**
 * Created by ddq on 2016/10/25.
 */

public class DatePickerUtil {
    private DatePickerDialog pickerLollipop;
    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog pickerPreLollipop;
    private OnDateSetListener listener;
    private int tag;

    public DatePickerUtil(Context context, final OnDateSetListener listener, int year, int month, int day) {
        this.listener = listener;
        if (ViewUtil.hasLollipop()) {
            pickerLollipop = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    if (DatePickerUtil.this.listener != null)
                        DatePickerUtil.this.listener.onDateSet(year, month + 1, dayOfMonth, tag);
                }
            }, year, month, day);
        } else {
            pickerPreLollipop = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(new com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                    if (DatePickerUtil.this.listener != null)
                        DatePickerUtil.this.listener.onDateSet(year, monthOfYear + 1, dayOfMonth, tag);
                }
            }, year, month, day);
        }
    }

    public DatePickerUtil(Context context, final OnDateSetListener listener, Calendar calendar) {
        this(context, listener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
    }

    public void setMinimalDate(Calendar mCalendar) {
        if (ViewUtil.hasLollipop()) {
            pickerLollipop.getDatePicker().setMinDate(mCalendar.getTimeInMillis());
        } else {
            pickerPreLollipop.setMinDate(mCalendar);
        }
    }

    public void showDatePicker(FragmentManager manager, int tag) {
        this.tag = tag;
        if (ViewUtil.hasLollipop()) {
            pickerLollipop.show();
        } else {
            if (pickerPreLollipop != null && !pickerPreLollipop.isAdded()){
                pickerPreLollipop.show(manager, "date_picker_dialog");
            }
        }
    }

    public interface OnDateSetListener {
        void onDateSet(int year, int monthOfYear, int dayOfMonth, int tag);
    }
}
