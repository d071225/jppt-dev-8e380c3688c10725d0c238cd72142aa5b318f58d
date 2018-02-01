package com.hletong.hyc.adapter;


import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.hletong.hyc.R;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

import java.util.List;

public class BankListAdapter extends HFRecyclerAdapter<DictionaryItem> {
    private int selectPos = -1;

    public BankListAdapter(Context context, List<DictionaryItem> data) {
        super(context, data);
    }

    @Override
    protected BaseHolder<DictionaryItem> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new BankHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_bank, parent, false));
    }

    @Override
    public void refresh(@Nullable List<DictionaryItem> newData) {
        super.refresh(newData);
        selectPos = -1;
    }

    public int getSelectPos() {
        return selectPos;
    }

    private class BankHolder extends BaseHolder<DictionaryItem> {
        private DictionaryItem data;

        public BankHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        public void setData(DictionaryItem data) {
            this.data = data;
            int pos = getAdapterPosition();
            CheckBox checkBox = getView(R.id.bankChecked);
            setText(R.id.bankName, data.getValue());
            checkBox.setChecked(pos == selectPos);
        }

        @Override
        public void onClick(View v) {
            selectPos = getAdapterPosition();
            notifyDataSetChanged();
        }
    }
}
