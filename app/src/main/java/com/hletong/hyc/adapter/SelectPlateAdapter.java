package com.hletong.hyc.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.VehicleInfo;
import com.hletong.mob.dialog.selector.SelectAdapter;
import com.hletong.mob.pullrefresh.BaseHolder;

import java.util.List;
import java.util.Locale;

public class SelectPlateAdapter extends SelectAdapter<VehicleInfo> {
    private OnPlateSelected mOnPlateSelected;

    public SelectPlateAdapter(Context context, List<VehicleInfo> data, OnPlateSelected onPlateSelected) {
        super(context, data, Integer.MAX_VALUE);
        mOnPlateSelected = onPlateSelected;
    }

    @Override
    public void refresh(@Nullable List<VehicleInfo> newData) {
        super.refresh(newData);
        //如果只有一条数据 默认选中第一条
        if (newData != null && newData.size() == 1) {
            if (mOnPlateSelected != null) {
                mOnPlateSelected.plateSelected(getItem(0));
            }
            setSelected(0);
        }
    }

    @Override
    protected BaseHolder<VehicleInfo> onCreateItemViewHolder(ViewGroup parent, int viewType) {//浙B23333 这这这
        return new ViewHolder(getInflater().inflate(R.layout.recycler_item_select_plate, parent, false));
    }

    private class ViewHolder extends BaseHolder<VehicleInfo> implements View.OnClickListener {
        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        public void setData(VehicleInfo vehicleInfo) {
            TextView textView = (TextView) itemView;
            textView.setText(String.format(Locale.CHINA, "%s %s", vehicleInfo.getPlate(), vehicleInfo.getStateStr()));
            textView.setSelected(isSelected(getAdapterPosition()));
        }

        @Override
        public void onClick(View v) {
            if (mOnPlateSelected != null) {
                mOnPlateSelected.plateSelected(getItem(getAdapterPosition()));
            }
            setSelected(getAdapterPosition());
        }
    }

    public interface OnPlateSelected {
        void plateSelected(VehicleInfo vehicleInfo);
    }
}




