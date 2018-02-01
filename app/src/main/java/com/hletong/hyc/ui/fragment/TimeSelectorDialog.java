package com.hletong.hyc.ui.fragment;

import android.content.Context;

import com.hletong.hyc.R;
import com.hletong.mob.dialog.selector.BottomSelectorDialog;
import com.hletong.mob.dialog.selector.IItemShow;

import java.util.ArrayList;
import java.util.List;

/**
 * 时间选择器
 * Created by ddq on 2016/11/2.
 */

public class TimeSelectorDialog extends BottomSelectorDialog<TimeSelectorDialog.TimePicker> {
    public TimeSelectorDialog(Context context) {
        super(context);
    }

    public static TimeSelectorDialog getDialogInstance(Context context) {
        return new TimeSelectorDialog(context);
    }

    @Override
    protected String getTitle() {
        return "选择时间";
    }

    @Override
    protected void onLoad() {
        List<TimePicker> timePickers = new ArrayList<>();
        String[] times = getContext().getResources().getStringArray(R.array.timePickers);
        for (String temp : times) {
            timePickers.add(new TimePicker(temp));
        }
        showList(timePickers, true);
        hideLoading();
    }

    public static class TimePicker implements IItemShow {
        private String time;

        public TimePicker(String time) {
            this.time = time;
        }

        @Override
        public String getValue() {
            return time;
        }
    }
}
