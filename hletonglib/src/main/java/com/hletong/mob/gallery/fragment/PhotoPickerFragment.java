package com.hletong.mob.gallery.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.hletong.mob.R;
import com.hletong.mob.base.BaseFragment;
import com.hletong.mob.gallery.PhotoPickerActivity;
import com.hletong.mob.gallery.adapter.PhotoPickerAdapter;
import com.hletong.mob.gallery.adapter.PopupDirectoryListAdapter;
import com.hletong.mob.gallery.builder.PickerBuilder;
import com.hletong.mob.gallery.builder.PreViewBuilder;
import com.hletong.mob.gallery.entity.PhotoDirectory;
import com.hletong.mob.gallery.event.OnPhotoPickerListener;
import com.hletong.mob.gallery.utils.ImageCaptureManager;
import com.hletong.mob.gallery.utils.MediaStoreHelper;
import com.xcheng.view.util.ToastLess;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * cx
 * Created by donglua on 15/5/31.
 */
public class PhotoPickerFragment extends BaseFragment {

    private ImageCaptureManager captureManager;
    private PhotoPickerAdapter mPhotoPickerAdapter;

    private PopupDirectoryListAdapter listAdapter;
    //所有photos的路径
    private List<PhotoDirectory> mDirectories;

    private int SCROLL_THRESHOLD = 60;
    private int column;
    //目录弹出框的一次最多显示的目录数目
    public static int COUNT_MAX = 4;
    private ListPopupWindow mDirPopupWindow;
    private RequestManager mRequestManager;
    private int maxCount;
    private Button mSwitchDirButton;
    private RecyclerView mRecyclerView;

    public static PhotoPickerFragment newInstance(Bundle args) {
        PhotoPickerFragment fragment = new PhotoPickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mRequestManager = Glide.with(this);
        mDirectories = new ArrayList<>();
        PickerBuilder builder = PickerBuilder.builder(getArguments());
        final ArrayList<String> selectPhotos = builder.getSelectPhotos();
        column = builder.getColumnCount();
        boolean showCamera = builder.isShowCamera();
        boolean previewEnable = builder.isPreviewEnabled();
        maxCount = builder.getMaxPhotoCount();
        mPhotoPickerAdapter = new PhotoPickerAdapter(getContext(), mRequestManager, column, maxCount);
        mPhotoPickerAdapter.setShowCamera(showCamera);
        mPhotoPickerAdapter.setPreviewEnable(previewEnable);
        Bundle mediaStoreArgs = new Bundle();
        boolean showGif = builder.isShowGif();
        mediaStoreArgs.putBoolean(PickerBuilder.EXTRA_SHOW_GIF, showGif);
        MediaStoreHelper.getPhotoDirs(getActivity(), mediaStoreArgs,
                new MediaStoreHelper.PhotosResultCallback() {

                    @Override
                    public void onResultCallback(List<PhotoDirectory> directories) {
                        //第一次加载获取，剔除不存在的文件
                        if (mDirectories.size() == 0) {
                            selectPhotos.retainAll(directories.get(PickerBuilder.INDEX_ALL_PHOTOS).getPhotos());
                            mPhotoPickerAdapter.setSelectedPhotos(selectPhotos);
                        }
                        mDirectories.clear();
                        mDirectories.addAll(directories);
                        PhotoDirectory curDirectory = mDirectories.get(mPhotoPickerAdapter.getCurrentDirIndex());
                        //系统camera拍照和程序内Camera拍照导致相册文件夹列表位置发生替换
                        mSwitchDirButton.setText(curDirectory.getName().toLowerCase());//默认会大写，这里要改成小写
                        mPhotoPickerAdapter.refresh(curDirectory.getPhotos());
                        listAdapter.notifyDataSetChanged();
                        adjustHeight();
                    }
                });
        captureManager = new ImageCaptureManager();
    }

    @Override
    public int getLayoutId() {
        return R.layout.__picker_fragment_photo_picker;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        listAdapter = new PopupDirectoryListAdapter(mRequestManager, mDirectories);
        mRecyclerView = findViewById(R.id.rv_photos);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), column);
        // layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mPhotoPickerAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mSwitchDirButton = findViewById(R.id.btn_switch_dir);
        mDirPopupWindow = new ListPopupWindow(getActivity());
        mDirPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));//替换背景
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int widths = dm.widthPixels;
        mDirPopupWindow.setWidth(widths);//ListPopupWindow.MATCH_PARENT还是会有边距，直接拿到屏幕宽度来设置也不行，因为默认的background有左右padding值。
  /*  int height = wm.getDefaultDisplay().getHeight();
    mDirPopupWindow.setHeight((int) (height *0.7));*/
        mDirPopupWindow.setAnchorView(mSwitchDirButton);
        mDirPopupWindow.setAdapter(listAdapter);
        mDirPopupWindow.setModal(true);
        mDirPopupWindow.setDropDownGravity(Gravity.BOTTOM);
        mDirPopupWindow.setAnimationStyle(R.style.__picker_popAnim);
    }

    @Override
    public void setListener() {
        super.setListener();
        mDirPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDirPopupWindow.dismiss();
                PhotoDirectory directory = mDirectories.get(position);
                mSwitchDirButton.setText(directory.getName().toLowerCase());//默认会大写，这里要改成小写
                mPhotoPickerAdapter.setCurrentDirIndex(position);
                mPhotoPickerAdapter.refresh(mDirectories.get(mPhotoPickerAdapter.getCurrentDirIndex()).getPhotos());
            }
        });
        mPhotoPickerAdapter.setOnPhotoPickerListener(new OnPhotoPickerListener() {
            @Override
            public void onItemCheck(int position, String path, boolean isCheck, int selectCount) {
                if (maxCount == selectCount && !isCheck) {
                    ToastLess.showToast(getString(R.string.__picker_over_max_count_tips, maxCount), true);
                }
                ((PhotoPickerActivity) getActivity()).supportInvalidateOptionsMenu(selectCount);
            }

            @Override
            public void onCameraClick(View v) {
                try {
                    Intent intent = captureManager.dispatchTakePictureIntent();
                    startActivityForResult(intent, PickerBuilder.REQUEST_CODE_TAKE_PHOTO);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onPhotoClick(View v, /*有效数据的位置*/int position, ArrayList<String> selectPhotos, ArrayList<String> allPhotos) {
                PreViewBuilder.builder()
                        .setSelectPhotos(selectPhotos)
                        .setPreViewPhotos((ArrayList<String>) mPhotoPickerAdapter.getData())
                        .setAction(PreViewBuilder.SELECT)
                        .setMaxPhotoCount(maxCount)
                        .setCurrentItem(position)
                        .start(PhotoPickerFragment.this);
            }
        });
        mSwitchDirButton.setOnClickListener(new OnClickListener() {
            @SuppressWarnings("ConstantConditions")
            @Override
            public void onClick(View v) {
                if (mDirPopupWindow.isShowing()) {
                    mDirPopupWindow.dismiss();
                } else if (!getActivity().isFinishing()) {
                    adjustHeight();
                    mDirPopupWindow.show();
                    mDirPopupWindow.getListView().setVerticalScrollBarEnabled(false);
                    //去掉滑动条,listview 在show之后才建立，所以需要该方法在show之后调用，否则会空指针
                }
            }
        });
        Button btnPreview = findViewById(R.id.btn_preview);
        //预览按钮
        btnPreview.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPhotoPickerAdapter.getSelectedPhotos().size() > 0) {
                    PreViewBuilder.builder()
                            .setSelectPhotos(mPhotoPickerAdapter.getSelectedPhotos())
                            .setAction(PreViewBuilder.SELECT)
                            .setMaxPhotoCount(maxCount)
                            .setCurrentItem(0)
                            .start(PhotoPickerFragment.this);
                } else {
                    ToastLess.showToast("还没有选择图片");
                }
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (Math.abs(dy) > SCROLL_THRESHOLD) {
                    mRequestManager.pauseRequests();
                } else {
                    mRequestManager.resumeRequests();
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    mRequestManager.resumeRequests();
                }
            }
        });
    }

    @Override
    public void onActivityResultOk(int requestCode, Intent data) {
        super.onActivityResultOk(requestCode, data);
        if (requestCode == PickerBuilder.REQUEST_CODE_TAKE_PHOTO) {
            captureManager.galleryAddPic();
            if (mDirectories.size() > 0) {
                String path = captureManager.getCurrentPhotoPath();
                PhotoDirectory directory = mDirectories.get(PickerBuilder.INDEX_ALL_PHOTOS);
                directory.addPhoto(PickerBuilder.INDEX_ALL_PHOTOS, path);
                directory.setCoverPath(path);
                mPhotoPickerAdapter.refresh(mDirectories.get(mPhotoPickerAdapter.getCurrentDirIndex()).getPhotos());
            }
        } else if (requestCode == PickerBuilder.REQUEST_CODE_PREVIEW) {
            ArrayList<String> selectPhotos = data.getStringArrayListExtra(PickerBuilder.KEY_SELECTED_PHOTOS);
            mPhotoPickerAdapter.setSelectedPhotos(selectPhotos);
            mPhotoPickerAdapter.notifyDataSetChanged();
            ((PhotoPickerActivity) getActivity()).supportInvalidateOptionsMenu(mPhotoPickerAdapter.getSelectedItemCount());
        }
    }

    public PhotoPickerAdapter getPhotoPickerAdapter() {
        return mPhotoPickerAdapter;
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        captureManager.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        captureManager.onRestoreInstanceState(savedInstanceState);
        super.onViewStateRestored(savedInstanceState);
    }

    public void adjustHeight() {
        if (listAdapter == null) return;
        int count = listAdapter.getCount();
        count = count < COUNT_MAX ? count : COUNT_MAX;
        if (mDirPopupWindow != null) {
            mDirPopupWindow.setHeight(count * getResources().getDimensionPixelOffset(R.dimen.__picker_item_directory_height));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mDirectories == null) {
            return;
        }
        mDirectories.clear();
        mDirectories = null;
    }
}
