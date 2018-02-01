package com.hletong.mob.gallery.widget;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.hletong.mob.R;
import com.hletong.mob.base.BaseRecyclerAdapter;
import com.hletong.mob.gallery.builder.PreViewBuilder;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.xcheng.view.util.LocalDisplay;

import java.util.ArrayList;

/**
 * Created by donglua on 15/5/31.
 */
public class PhotoAdapter extends BaseRecyclerAdapter<String> {
    public final static int ITEM_TYPE_TAKE = 200;
    public final static int ITEM_TYPE_PHOTO = 201;
    // private final ArrayList<String> photoPaths;
    //with fragment 或者Activity能更好的管理生命周期
    private RequestManager mRequestManager;
    private final int maxCount;
    private int padding;
    private int itemWidth;

    public void setAction(@PickerRecyclerView.RvAction int action) {
        this.action = action;
    }

    @PickerRecyclerView.RvAction
    private int action;

    public PhotoAdapter(Context context, RequestManager requestManager, ArrayList<String> photoPaths, int maxCount) {
        super(context, photoPaths);
        this.mRequestManager = requestManager;
        padding = LocalDisplay.dp2px(4);
        this.maxCount = maxCount;
    }

    public ArrayList<String> getSelectPhotos() {
        return new ArrayList<>(getData());
    }

    public void setItemWidth(int itemWidth) {
        this.itemWidth = itemWidth;
    }

    @Override
    public int getItemViewType(int position) {
        if (isShowAddPic() && position == getItemCount() - 1) {
            return ITEM_TYPE_TAKE;
        }
        return ITEM_TYPE_PHOTO;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = getInflater().inflate(R.layout.__picker_item_photo, parent, false);
        if (itemWidth > 0) {
            //noinspection SuspiciousNameCombination
            itemView.setLayoutParams(new RecyclerView.LayoutParams(itemWidth, itemWidth));
        }
        return new PhotoViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(BaseHolder baseHolder, int position) {
        PhotoViewHolder holder = (PhotoViewHolder) baseHolder;
        holder.ivPhoto.setPadding(position == 0 ? 0 : padding, padding, padding, padding);
        holder.ivPhoto.setOnClickListener(new AdapterClickListener(holder));
        if (holder.getItemViewType() == ITEM_TYPE_PHOTO) {
            if (action == PreViewBuilder.SELECT) {
                holder.deleteBtn.setVisibility(View.VISIBLE);
                holder.deleteBtn.setOnClickListener(new AdapterClickListener(holder));
            } else {
                holder.deleteBtn.setVisibility(View.GONE);
            }
            final String path = getItem(position);
            mRequestManager
                    .load(path)
                    .centerCrop()
                    .thumbnail(0.1f)
                    .placeholder(R.drawable.__picker_default_weixin)
                    .error(R.drawable.__picker_icon_pic_error)
                    .into(holder.ivPhoto);
        } else if (holder.getItemViewType() == ITEM_TYPE_TAKE) {
            mRequestManager
                    .load(R.drawable.__picker_icon_pic_take)
                    .centerCrop()
                    .thumbnail(0.1f)
                    .into(holder.ivPhoto);
            holder.deleteBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        int size = getData().size();
        if (isShowAddPic()) {
            return size + 1;
        }
        return size;
    }

    private boolean isShowAddPic() {
        return action == PreViewBuilder.SELECT && getData().size() != maxCount;
    }

    public static class PhotoViewHolder extends BaseHolder {
        private ImageView ivPhoto;
        private View vSelected;
        public View cover;
        public View deleteBtn;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            vSelected = itemView.findViewById(R.id.v_selected);
            vSelected.setVisibility(View.GONE);
            cover = itemView.findViewById(R.id.cover);
            cover.setVisibility(View.GONE);
            deleteBtn = itemView.findViewById(R.id.v_delete);
            deleteBtn.setVisibility(View.GONE);
        }
    }

    public void setOnPrePhotoClickListener(OnPrePhotoClickListener onPrePhotoClickListener) {
        mOnPrePhotoClickListener = onPrePhotoClickListener;
    }

    OnPrePhotoClickListener mOnPrePhotoClickListener;

    public interface OnPrePhotoClickListener {
        void onItemClick(View view, int position);

        void onAddClick(View view, int selectCount, int maxCount);
    }

    public class AdapterClickListener implements View.OnClickListener {
        private PhotoViewHolder holder;

        public AdapterClickListener(PhotoViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public void onClick(View v) {
            if (mOnPrePhotoClickListener == null)
                return;
            int position = holder.getAdapterPosition();
            //点击图片
            if (v == holder.ivPhoto) {
                if (action == PreViewBuilder.PREVIEW) {
                    mOnPrePhotoClickListener.onItemClick(v, position);
                } else if (action == PreViewBuilder.SELECT) {
                    if (isShowAddPic() && position == getItemCount() - 1) {
                        mOnPrePhotoClickListener.onAddClick(v, getDataCount(), maxCount);
                    } else {
                        mOnPrePhotoClickListener.onItemClick(v, position);
                    }
                }
            } else if (v == holder.deleteBtn) {
                //点击删除按钮
                remove(position);
            }
        }
    }
}
