package com.hletong.mob.dialog.selector;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hletong.mob.R;
import com.hletong.mob.pullrefresh.BaseHolder;

import java.util.List;

/**
 * Created by ddq on 2016/12/29.
 */

public class ItemSelectorAdapter<T extends IItemShow> extends SelectAdapter<T> {
    private BottomSelectorDialog.OnItemSelectedListener<T> mOnItemSelectedListener;
    private @LayoutRes int customLayout = 0;//自定义布局文件，自定义的布局文件必须有且只能有一个TextView

    public ItemSelectorAdapter(Context context, List<T> data, BottomSelectorDialog.OnItemSelectedListener<T> onItemSelectedListener) {
        super(context, data);
        this.mOnItemSelectedListener = onItemSelectedListener;
    }

    @Override
    protected BaseHolder<T> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        int layoutId = R.layout.bottom_item_selector;
        if (customLayout != 0)
            layoutId = customLayout;
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false));
    }

    public void setCustomLayout(@LayoutRes int customLayout) {
        this.customLayout = customLayout;
    }

    public class ViewHolder extends BaseHolder<T> implements View.OnClickListener {

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void setData(T data) {
            TextView itemText = (TextView) itemView;
            itemText.setText(data.getValue());
            itemText.setSelected(isSelected(getAdapterPosition()));
        }

        @Override
        public void onClick(View v) {
            mOnItemSelectedListener.onItemSelected(getItem(getAdapterPosition()), getAdapterPosition());
            setSelected(getAdapterPosition());
        }

        public String getText(){
            return getItem(getAdapterPosition()).getValue();
        }
    }
}
