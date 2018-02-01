package com.hletong.hyc.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.xcheng.view.util.LocalDisplay;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/3/9.
 * 通用选择模块-货源预报
 */

public abstract class BaseEditAdapter<T extends Parcelable> extends HFRecyclerAdapter<T> {
    private boolean modeEdit;//是否是编辑模式
    private int layout;
    private int selected;
    private OnActionClicked<T> deleteAction;
    private OnActionClicked<T> editAction;
    private OnActionClicked<T> setDefaultAction;

    public BaseEditAdapter(Context context, List<T> data, boolean modeEdit, int layout, int selected) {
        super(context, data);
        this.modeEdit = modeEdit;
        this.layout = layout;
        this.selected = selected;
    }

    @Override
    protected final BaseHolder<T> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return getSelectedViewHolder(viewInflated(getInflater().inflate(layout, parent, false)));
        } else {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.bottomMargin = LocalDisplay.dp2px(36);
            layoutParams.gravity = Gravity.BOTTOM;
            FrameLayout frameLayout = (FrameLayout) getInflater().inflate(R.layout.recycler_item_cargo_forecast_edit_template, parent, false);
            frameLayout.addView(viewInflated(getInflater().inflate(layout, frameLayout, false)), layoutParams);
            return getEditViewHolder(frameLayout);
        }
    }

    protected View viewInflated(View view) {
        return view;
    }
//
//    @Override
//    protected final void onBindItemViewHolder(BaseHolder<T> holder, int position, T t) {
//        if (getViewType(position) == 1) {
//            @SuppressWarnings("unchecked")
//            SelectViewHolder svh = (SelectViewHolder) holder;
//            svh.setData(t);
//        } else {
//            @SuppressWarnings("unchecked")
//            EditViewHolder evh = (EditViewHolder) holder;
//            evh.setData(t);
//        }
//    }

    public void setDeleteAction(OnActionClicked<T> deleteAction) {
        this.deleteAction = deleteAction;
    }

    public void setEditAction(OnActionClicked<T> editAction) {
        this.editAction = editAction;
    }

    public void setDefaultAction(OnActionClicked<T> setDefaultAction) {
        this.setDefaultAction = setDefaultAction;
    }

    @Override
    public final int getViewType(int position) {
        if (!modeEdit)
            return 1;
        return 2;
    }

    protected SelectViewHolder getSelectedViewHolder(View view) {
        return new SelectViewHolder(view);
    }

    protected EditViewHolder getEditViewHolder(View view) {
        return new EditViewHolder(view);
    }

    protected class SelectViewHolder extends BaseHolder<T> implements View.OnClickListener {
        private ImageView mImageView;

        public SelectViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            mImageView = (ImageView) itemView.findViewById(R.id.image);
        }

        public void setData(T t) {
            if (selected == getAdapterPosition()) {
                mImageView.setSelected(true);
            }
            bindData(t, this);
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            intent.putExtra("data", getItem(getAdapterPosition()));
            Activity activity = (Activity) v.getContext();
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
        }
    }

    protected class EditViewHolder extends BaseHolder<T> implements View.OnClickListener {
        @BindView(R.id.actionBar)
        View actionBar;
        @BindView(R.id.setDefault)
        TextView setDefault;
        @BindView(R.id.delete)
        TextView tvDelete;
        @BindView(R.id.edit)
        TextView tvEdit;
        private T source;

        public EditViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(T t) {
            source = t;
            actionBar.setVisibility(View.VISIBLE);
            setDefault.setText(getCheckText(source, this));
            tvDelete.setVisibility(deleteAction != null ? View.VISIBLE : View.GONE);
            tvEdit.setVisibility(editAction != null ? View.VISIBLE : View.GONE);
            setDefault.setVisibility(setDefaultAction != null ? View.VISIBLE : View.GONE);
            bindData(t, this);
        }

        @OnClick({R.id.setDefault, R.id.edit, R.id.delete})
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.image:
                case R.id.setDefault:
                    setDefaultAction.actionClicked(v, source, this);
                    break;
                case R.id.edit:
                    editAction.actionClicked(v, source, this);
                    break;
                case R.id.delete:
                    deleteAction.actionClicked(v, source, this);
                    break;
            }
        }
    }

    protected abstract void bindData(T t, BaseHolder<T> holder);

    protected String getCheckText(T t, BaseHolder<T> holder) {
        return "设为默认";
    }

    public interface OnActionClicked<T> {
        void actionClicked(View view, T t, BaseHolder<T> holder);
    }
}