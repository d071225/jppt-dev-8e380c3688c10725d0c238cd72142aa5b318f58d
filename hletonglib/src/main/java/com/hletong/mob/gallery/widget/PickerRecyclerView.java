package com.hletong.mob.gallery.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.IntDef;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.hletong.mob.gallery.builder.PickerBuilder;
import com.hletong.mob.gallery.builder.PreViewBuilder;
import com.orhanobut.logger.Logger;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;


/**
 * 宽度要设置match_parent
 * 展示多张图片封装的RecyclerView
 * Created by cx on 2016/8/15 0015.
 */
public class PickerRecyclerView extends RecyclerView implements PhotoAdapter.OnPrePhotoClickListener {

    @IntDef({PreViewBuilder.SELECT, PreViewBuilder.PREVIEW})
    @Retention(RetentionPolicy.SOURCE)
    public @interface RvAction {
    }

    @RvAction
    private int action;
    /***
     * 每行个数
     **/
    private int columnCount;
    private int maxCount;
    private PhotoAdapter photoAdapter;
    private Activity mActivity;
    private Fragment mFragment;
    private boolean isFragmentTarget;

    public PickerRecyclerView(Context context) {
        this(context, null, 0);
    }

    public PickerRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PickerRecyclerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void init(Builder builder) {
        mActivity = builder.getActivity();
        isFragmentTarget = builder.isFragmentTarget();
        mFragment = builder.getFragment();
        this.action = builder.action;
        this.columnCount = builder.columnCount;
        this.maxCount = builder.maxCount;
        setLayoutManager(new LinearLayoutManager(getContext(), OrientationHelper.HORIZONTAL, false));
        RequestManager requestManager = isFragmentTarget ? Glide.with(mFragment) : Glide.with(mActivity);
        photoAdapter = new PhotoAdapter(mActivity, requestManager, builder.selectedPhotos, maxCount);
        if (builder.mObserver != null)
            photoAdapter.registerAdapterDataObserver(builder.mObserver);
        photoAdapter.setOnPrePhotoClickListener(this);
        photoAdapter.setAction(action);
        setAdapter(photoAdapter);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        if (getWidth() > 0) {
            photoAdapter.setItemWidth(getWidth() / columnCount);
            super.setAdapter(adapter);
        } else {
            getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    getViewTreeObserver().removeOnPreDrawListener(this);
                    setAdapter(photoAdapter);
                    return true;
                }
            });
        }
    }

    /**
     * 添加从外部拍照的照片
     **/
    public void addTakePics(String takePath) {
        if (takePath != null) {
            photoAdapter.add(takePath);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //选择图片模式下需要刷新列表
        if (action == PreViewBuilder.SELECT) {
            if (resultCode == Activity.RESULT_OK) {
                ArrayList<String> photos = data.getStringArrayListExtra(PickerBuilder.KEY_SELECTED_PHOTOS);
                if (requestCode == PickerBuilder.REQUEST_CODE_PICKER) {//第一次，选择图片后返回
                    if (photos != null) {
                        photoAdapter.refresh(photos);
                    }
                } else if (requestCode == PickerBuilder.REQUEST_CODE_PREVIEW) {//如果是预览与删除后返回
                    if (photos != null) {
                        photoAdapter.refresh(photos);
                    }
                }
            } else {
                if (requestCode == PickerBuilder.REQUEST_CODE_PICKER) {
                    //只有选择图片才做选择
                    Logger.d("取消选择");
                }
            }
        }
    }

    public void toPicker() {
        PickerBuilder builder =
                PickerBuilder.builder()
                        .setMaxPhotoCount(maxCount)
                        .setSelectPhotos(photoAdapter.getSelectPhotos());
        if (isFragmentTarget) {
            builder.start(mFragment);
        } else {
            builder.start(mActivity);
        }
    }

    @Override
    public void onItemClick(View view, int position) {
        PreViewBuilder preViewBuilder = PreViewBuilder.builder()
                .setSelectPhotos(photoAdapter.getSelectPhotos())
                .setAction(action == PreViewBuilder.SELECT ? PreViewBuilder.DELETE : action)
                .setCurrentItem(position);
        if (isFragmentTarget) {
            preViewBuilder.start(mFragment);
        } else {
            preViewBuilder.start(mActivity);
        }
    }

    @Override
    public void onAddClick(View view, int selectCount, int maxCount) {
        if (selectCount == maxCount) {
            Toast.makeText(getContext(), "已选了" + maxCount + "张图片", Toast.LENGTH_SHORT).show();
        } else {
            if (mOnTakeClickListener != null) {
                mOnTakeClickListener.onClick(view);
            } else {
                toPicker();
            }
        }
    }

    public ArrayList<String> getPhotos() {
        return photoAdapter.getSelectPhotos();
    }

    OnClickListener mOnTakeClickListener;

    public void setOnTakeClickListener(OnClickListener l) {
        mOnTakeClickListener = l;
    }

    public static class Builder {
        //启动pickerActivity的Fragment 或者Activity
        private Object target;
        private int action = PreViewBuilder.SELECT;
        //最多展示多少张图片
        int maxCount = PickerBuilder.DEFAULT_MAX_COUNT;
        //监听数据变化
        private AdapterDataObserver mObserver;
        /***
         * 每行个数
         **/
        private int columnCount = 4;
        ArrayList<String> selectedPhotos;

        public Builder(Activity target) {
            this.target = target;
        }

        public Builder(Fragment target) {
            this.target = target;
        }

        public Builder maxCount(int maxCount) {
            this.maxCount = maxCount;
            return this;
        }

        public Builder action(@RvAction int action) {
            this.action = action;
            return this;
        }

        public Builder columnCount(int columnCount) {
            this.columnCount = columnCount;
            return this;
        }

        public Builder selectedPhotos(ArrayList<String> selectedPhotos) {
            this.selectedPhotos = selectedPhotos;
            return this;
        }

        public Builder observe(AdapterDataObserver observer) {
            mObserver = observer;
            return this;
        }

        public void build(PickerRecyclerView pickerRecyclerView) {
            pickerRecyclerView.init(this);
        }

        private boolean isFragmentTarget() {
            return target instanceof Fragment;
        }

        private Activity getActivity() {
            if (target instanceof Activity) {
                return (Activity) target;
            } else {
                return ((Fragment) target).getActivity();
            }
        }

        private Fragment getFragment() {
            if (target instanceof Fragment) {
                return (Fragment) target;
            }
            return null;
        }
    }
}
