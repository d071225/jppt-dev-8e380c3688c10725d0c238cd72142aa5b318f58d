package com.hletong.hyc.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.CallHistory;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.xcheng.view.util.LocalDisplay;

/**
 * Created by dongdaqing on 2017/5/31.
 * 国联议价车船方拨号历史
 */

public class CallHistoryAdapter extends HFRecyclerAdapter<CallHistory> {
    private MakePhoneCall mMakePhoneCall;

    public CallHistoryAdapter(Context context, MakePhoneCall makePhoneCall) {
        super(context, null, Integer.MAX_VALUE);
        mMakePhoneCall = makePhoneCall;
    }

    @Override
    public int getViewType(int position) {
        if (position == 0)
            return 1;
        return 2;
    }

    @Override
    protected BaseHolder<CallHistory> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1)
            return new SimpleViewHolder(getInflater().inflate(R.layout.recycler_item_cargo_track_title, parent, false));
        return new ViewHolder(getInflater().inflate(R.layout.recycler_item_call_history, parent, false));
    }

    private class SimpleViewHolder extends BaseHolder<CallHistory> {

        public SimpleViewHolder(View itemView) {
            super(itemView);
            itemView.setPadding(LocalDisplay.dp2px(15), LocalDisplay.dp2px(16), 0, LocalDisplay.dp2px(8));
            TextView tv = (TextView) itemView;
            tv.setText("电话议价进度");
        }
    }

    private class ViewHolder extends BaseHolder<CallHistory> implements View.OnClickListener {

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.call).setOnClickListener(this);
        }

        public void setData(CallHistory ch) {
            setText(R.id.text, ch.getText());
            itemView.findViewById(R.id.call).setEnabled(!TextUtils.isEmpty(ch.getCallTel()));
        }

        @Override
        public void onClick(View v) {
            CallHistory history = getItem(getAdapterPosition());
            mMakePhoneCall.call(history.getCallMemberSname(), history.getCallTel(), history.getCallMemerbCode());
        }
    }
}
