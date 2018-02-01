package com.hletong.hyc.adapter;

import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.mob.util.SimpleDate;

/**
 * Created by ddq on 2016/12/19.
 */
public class StatusItemUtil {
    private TextView status;
    private TextView timeTv;

    public StatusItemUtil() {
    }

    public View wrapView(View mView) {
        View statusView = LayoutInflater.from(mView.getContext()).inflate(R.layout.recycler_item_extra_status_bar, null);
        status = (TextView) statusView.findViewById(R.id.status);
        timeTv = (TextView) statusView.findViewById(R.id.status_time);
        FrameLayout mFrameLayout = new FrameLayout(mView.getContext());
        mFrameLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        final int statusHeight = mView.getResources().getDimensionPixelSize(R.dimen.recycler_item_status_bar_height);

        FrameLayout.LayoutParams mLayoutParamsForStatus = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusHeight);
        mFrameLayout.addView(statusView, mLayoutParamsForStatus);

        FrameLayout.LayoutParams mLayoutParamsForOrigin = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParamsForOrigin.topMargin = statusHeight;
        mFrameLayout.addView(mView, mLayoutParamsForOrigin);
        mFrameLayout.setTag(this);
        return mFrameLayout;
    }

    public void setData(int color, String text, String time) {
        status.setTextColor(color);
        status.setText(text);
        String tag = (String) timeTv.getTag();
        if (time != null && !time.equals(tag)) {
            SimpleDate mSimpleDate = SimpleDate.parse(time);
            timeTv.setText(mSimpleDate.getDateDescriptionWithTime());
            timeTv.setTag(time);
        }
    }

    public void setData(String text, String time) {
        setData(ContextCompat.getColor(status.getContext(), R.color.colorAccent), text, time);
    }
}
