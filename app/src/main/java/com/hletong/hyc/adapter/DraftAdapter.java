package com.hletong.hyc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.DraftContract;
import com.hletong.hyc.model.Draft;
import com.hletong.hyc.ui.activity.CargoForecastActivity;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ddq on 2017/4/6..
 * 草稿
 */

public class DraftAdapter extends HFRecyclerAdapter<Draft> {
    private boolean edit;
    private DraftContract.Presenter mPresenter;
    private ITransactionView mSwitchDelegate;
    private AppointCarrierAdapter.ItemChangedListener<Draft> mItemChangedListener;

    public DraftAdapter(Context context, DraftContract.Presenter presenter, ITransactionView delegate, AppointCarrierAdapter.ItemChangedListener<Draft> listener) {
        super(context, null);
        this.edit = false;
        mPresenter = presenter;
        this.mSwitchDelegate = delegate;
        this.mItemChangedListener = listener;
    }

    @Override
    protected BaseHolder<Draft> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getInflater().inflate(R.layout.recycler_item_draft, parent, false));
    }

    public boolean toggle() {
        edit = !edit;
        notifyDataSetChanged();
        return edit;
    }

    public void changeState(boolean select) {
        List<Draft> drafts = new ArrayList<>(getData());
        for (Draft draft : drafts) {
            draft.setSelected(select);
        }
        refresh(drafts);
        if (select) {
            mItemChangedListener.itemSelected(null);
        } else {
            mItemChangedListener.itemUnSelected(null);
        }
    }

    public boolean haveSelected() {
        for (int i = 0; i < getDataCount(); i++) {
            if (getItem(i).isSelected())
                return true;
        }
        return false;
    }

    public boolean haveUnSelected() {
        for (int i = 0; i < getDataCount(); i++) {
            if (!getItem(i).isSelected())
                return true;
        }
        return false;
    }

    public ArrayList<Draft> getItem2Delete() {
        ArrayList<Draft> list = new ArrayList<>();
        for (int i = 0; i < getDataCount(); i++) {
            if (getItem(i).isSelected())
                list.add(getItem(i));
        }
        return list;
    }

    private class ViewHolder extends BaseHolder<Draft> implements View.OnClickListener {

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        public void setData(Draft source) {
            setVisible(R.id.checkbox, edit ? View.VISIBLE : View.GONE);
            getView(R.id.checkbox).setSelected(source.isSelected());
            setVisible(R.id.arrow, !edit ? View.VISIBLE : View.GONE);
            setText(R.id.name, "货物名称：" + source.getOrgin_cargon_kind_name());
            setText(R.id.time, source.getFormatCreateTs());
        }

        @Override
        public void onClick(View v) {
            if (edit) {
                ImageView iv = getView(R.id.checkbox);
                iv.setSelected(!iv.isSelected());
                getItem(getAdapterPosition()).setSelected(iv.isSelected());
                if (iv.isSelected()) {
                    mItemChangedListener.itemSelected(null);
                } else {
                    mItemChangedListener.itemUnSelected(null);
                }
            } else {
                Intent intent = new Intent(itemView.getContext(), CargoForecastActivity.class);
                intent.putExtra("cargoUuid", getItem(getAdapterPosition()).getCargo_uuid());
                intent.putExtra("full_copy", true);
                mSwitchDelegate.toActivity(intent, SourceListAdapter.BaseViewHolder.REQUEST_CODE_FOR_LIST_ITEM, null);
            }
        }
    }
}
