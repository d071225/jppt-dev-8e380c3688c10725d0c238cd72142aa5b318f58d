package com.hletong.hyc.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.hletong.hyc.R;
import com.hletong.hyc.model.MessageInfo;
import com.hletong.hyc.ui.activity.MessageInfoActivity;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

/**
 * Created by chengxin on 16/9/27.
 * 网签承运合同列表
 */
public class MessageAdapter extends HFRecyclerAdapter<MessageInfo> {
    private boolean unRead;
    private ITransactionView mSwitchDelegate;

    public MessageAdapter(Context context, ITransactionView delegate, boolean unRead) {
        super(context, null, Integer.MAX_VALUE);
        this.unRead = unRead;
        this.mSwitchDelegate = delegate;
    }

    @Override
    protected BaseHolder<MessageInfo> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new MessageHolder(getInflater().inflate(R.layout.recycler_item_message, parent, false));
    }

    public class MessageHolder extends BaseHolder<MessageInfo> implements View.OnClickListener {

        public MessageHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        public void setData(MessageInfo data) {
            if (TextUtils.isEmpty(data.getFlag()))
                getView(R.id.tv_flag).setVisibility(View.INVISIBLE);
            else {
                getView(R.id.tv_flag).setVisibility(View.VISIBLE);
                setText(R.id.tv_flag, data.getFlag());
            }

            setText(R.id.tv_flagText, data.getFlagText());
            setText(R.id.tv_content, data.getFormatContent());
            setText(R.id.tv_date, data.getFormatDate());
        }

        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            MessageInfo messageInfo = getItem(getAdapterPosition());
            bundle.putParcelable("message", messageInfo);
            bundle.putBoolean("unRead", unRead);
            bundle.putString("title", messageInfo.getFlagText());
            Intent intent = new Intent(itemView.getContext(), MessageInfoActivity.class);
            intent.putExtras(bundle);
            mSwitchDelegate.toActivity(intent, SourceListAdapter.BaseViewHolder.REQUEST_CODE_FOR_LIST_ITEM, null);
        }
    }
}
