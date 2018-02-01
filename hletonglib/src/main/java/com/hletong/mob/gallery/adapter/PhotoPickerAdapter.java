package com.hletong.mob.gallery.adapter;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.hletong.mob.R;
import com.hletong.mob.gallery.builder.PickerBuilder;
import com.hletong.mob.gallery.event.OnPhotoPickerListener;
import com.hletong.mob.pullrefresh.BaseHolder;

import java.io.File;
import java.util.ArrayList;


/**
 * 图片GridView
 * Created by cx on 15/5/31.
 */
public class PhotoPickerAdapter extends SelectableAdapter {
    private RequestManager mRequestManager;
    public final static int ITEM_TYPE_CAMERA = 100;
    public final static int ITEM_TYPE_PHOTO = 101;
    private boolean hasCamera = true;
    private boolean previewEnable = true;
    private int imageSize;
    private int mMaxCount;

    public PhotoPickerAdapter(Context context, RequestManager requestManager, int colNum, int maxCount) {
        super(context, null);
        this.mRequestManager = requestManager;
        setColumnNumber(colNum);
        mMaxCount = maxCount;
    }

    private void setColumnNumber(int columnNumber) {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        imageSize = widthPixels / columnNumber;
    }

    @Override
    public int getItemViewType(int position) {
        return (showCamera() && position == 0) ? ITEM_TYPE_CAMERA : ITEM_TYPE_PHOTO;
    }

    @Override
    public BaseHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = getInflater().inflate(R.layout.__picker_item_photo, parent, false);
        return new PhotoViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(BaseHolder baseHolder, int position) {
        PhotoViewHolder holder = (PhotoViewHolder) baseHolder;
        if (holder.getItemViewType() == ITEM_TYPE_PHOTO) {
            final String photo = getItem(position - getBeginIndex());
            mRequestManager.load(new File(photo))
                    .centerCrop()
                    .dontAnimate()
                    .thumbnail(0.5f)
                    .override(imageSize, imageSize)
                    .placeholder(R.drawable.__picker_default_weixin)
                    .error(R.drawable.__picker_ic_broken_image_black_48dp)
                    .into(holder.ivPhoto);
            final boolean isChecked = isSelected(photo);
            holder.photo = photo;
            holder.vSelected.setSelected(isChecked);
            holder.cover.setSelected(isChecked);
            holder.ivPhoto.setOnClickListener(new PickerClickListener(holder));
            holder.vSelected.setOnClickListener(new PickerClickListener(holder));
        } else if (holder.getItemViewType() == ITEM_TYPE_CAMERA) {
            holder.ivPhoto.setImageResource(R.drawable.__picker_camera);
            holder.vSelected.setVisibility(View.GONE);
            holder.ivPhoto.setScaleType(ImageView.ScaleType.CENTER);
            holder.ivPhoto.setOnClickListener(new PickerClickListener(holder));
        }
    }

    @Override
    public int getBeginIndex() {
        return showCamera() ? 1 : 0;
    }

    public static class PhotoViewHolder extends BaseHolder {
        private ImageView ivPhoto;
        private View vSelected;
        private View cover;
        private String photo;

        public PhotoViewHolder(View itemView) {
            super(itemView);
            ivPhoto = (ImageView) itemView.findViewById(R.id.iv_photo);
            vSelected = itemView.findViewById(R.id.v_selected);
            cover = itemView.findViewById(R.id.cover);
        }
    }

    public void setShowCamera(boolean hasCamera) {
        this.hasCamera = hasCamera;
    }

    public void setPreviewEnable(boolean previewEnable) {
        this.previewEnable = previewEnable;
    }

    public boolean showCamera() {
        return (hasCamera && getCurrentDirIndex() == PickerBuilder.INDEX_ALL_PHOTOS);
    }

    @Override
    public void onViewRecycled(BaseHolder baseHolder) {
        PhotoViewHolder holder = (PhotoViewHolder) baseHolder;
        Glide.clear(holder.ivPhoto);
        super.onViewRecycled(holder);
    }

    public void setOnPhotoPickerListener(OnPhotoPickerListener onPhotoPickerListener) {
        mOnPhotoPickerListener = onPhotoPickerListener;
    }

    private OnPhotoPickerListener mOnPhotoPickerListener;

    public class PickerClickListener implements View.OnClickListener {
        private PhotoViewHolder holder;

        public PickerClickListener(PhotoViewHolder holder) {
            this.holder = holder;
        }

        @Override
        public void onClick(View v) {
            if (mOnPhotoPickerListener == null) {
                return;
            }
            int position = holder.getAdapterPosition();
            if (holder.getItemViewType() == ITEM_TYPE_CAMERA) {
                mOnPhotoPickerListener.onCameraClick(v);
            } else if (holder.getItemViewType() == ITEM_TYPE_PHOTO) {
                if (v == holder.ivPhoto) {
                    if (previewEnable) {
                        mOnPhotoPickerListener.onPhotoClick(v, position - getBeginIndex(), getSelectedPhotos(), (ArrayList<String>) getData());
                    } else {
                        holder.vSelected.performClick();
                    }
                } else if (v == holder.vSelected) {
                    int selectCount = getSelectedItemCount();
                    String photo = holder.photo;
                    final boolean isChecked = isSelected(photo);
                    if (isChecked) {
                        toggleSelection(photo);
                        holder.vSelected.setSelected(false);
                        holder.cover.setSelected(false);
                    } else {
                        if (selectCount == mMaxCount) {
                            if (mMaxCount == 1) {
                                clearSelection();
                                toggleSelection(photo);
                                notifyDataSetChanged();
                            }
                        } else {
                            toggleSelection(photo);
                            holder.vSelected.setSelected(true);
                            holder.cover.setSelected(true);
                        }
                    }
                    if (mOnPhotoPickerListener != null) {
                        mOnPhotoPickerListener.onItemCheck(position, photo, isSelected(photo),
                                getSelectedItemCount());
                    }
                }
            }
        }
    }
}
