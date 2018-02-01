package com.hletong.hyc.util;

import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.Context;
import android.widget.TimePicker;

import com.hletong.mob.util.ViewUtil;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;

import java.util.Calendar;

/**
 * Created by ddq on 2016/11/14.
 */
public class TimePickerUtil {
    private TimePickerDialog dialog;
    private com.wdullaer.materialdatetimepicker.time.TimePickerDialog timePickerDialog;
    private OnTimeSetListener mOnTimeSetListener;
    private int tag;

    public TimePickerUtil(Context context, OnTimeSetListener mOnTimeSetListener) {
        this.mOnTimeSetListener = mOnTimeSetListener;
        Calendar calendar = Calendar.getInstance();
        if (ViewUtil.hasLollipop()) {
            dialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    dialog.dismiss();
                    if (TimePickerUtil.this.mOnTimeSetListener != null) {
                        TimePickerUtil.this.mOnTimeSetListener.onTimeSet(hourOfDay, minute, 0, tag);
                    }
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        } else {
            timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(new com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
                    timePickerDialog.dismiss();
                    if (TimePickerUtil.this.mOnTimeSetListener != null) {
                        TimePickerUtil.this.mOnTimeSetListener.onTimeSet(hourOfDay, minute, second, tag);
                    }
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        }
    }

    public void show(FragmentManager manager, int tag) {
        this.tag = tag;
        if (ViewUtil.hasLollipop()) {
            dialog.show();
        } else {
            if (timePickerDialog != null && !timePickerDialog.isAdded()) {
                timePickerDialog.show(manager, "time_picker_dialog");
            }
        }
    }

    public void setmOnTimeSetListener(OnTimeSetListener mOnTimeSetListener) {
        this.mOnTimeSetListener = mOnTimeSetListener;
    }

    public interface OnTimeSetListener {
        void onTimeSet(int hourOfDay, int minute, int second, int tag);
    }
}
