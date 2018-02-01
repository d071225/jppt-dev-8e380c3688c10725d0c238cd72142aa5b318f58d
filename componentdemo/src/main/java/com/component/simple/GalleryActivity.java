package com.component.simple;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.gallery.builder.PickerBuilder;
import com.hletong.mob.gallery.builder.PreViewBuilder;
import com.hletong.mob.gallery.utils.TakePhotoHelper;
import com.hletong.mob.gallery.widget.PickerRecyclerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public  class GalleryActivity extends BaseActivity {
    @BindView(R.id.pv_3_c)
    PickerRecyclerView pv3c;
    @BindView(R.id.pv_5_c)
    PickerRecyclerView pv5c;
    @BindView(R.id.pv_5_preView)
    PickerRecyclerView pv5PreView;
    @BindView(R.id.pv_5_no_camera)
    PickerRecyclerView pv5NoCamera;
    TakePhotoHelper mTakePhotoHelper;
    @Override
    public int getLayoutId() {
        return R.layout.activity_gallery;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        mTakePhotoHelper = TakePhotoHelper.with(this);
        new PickerRecyclerView.Builder(this).action(PreViewBuilder.SELECT).columnCount(4).maxCount(3).build(pv3c);
        pv3c.setOnTakeClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTakePhotoHelper.showTakeDialog();
                mTakePhotoHelper.setMultiView(pv3c);
            }
        });
        new PickerRecyclerView.Builder(this).action(PreViewBuilder.SELECT).columnCount(4).maxCount(5).build(pv5c);
        pv5c.setOnTakeClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTakePhotoHelper.showTakeDialog();
                mTakePhotoHelper.setMultiView(pv5c);
            }
        });
        new PickerRecyclerView.Builder(this).action(PreViewBuilder.SELECT).columnCount(4).maxCount(5).build(pv5NoCamera);
        pv5NoCamera.setOnTakeClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickerBuilder.builder().setMaxPhotoCount(5).setShowCamera(false).start(GalleryActivity.this);
                mTakePhotoHelper.setMultiView(pv5NoCamera);
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mTakePhotoHelper.onActivityResultFreshView(requestCode, resultCode, data);
        if(pv5c==mTakePhotoHelper.getCurrentPicView()&&resultCode==RESULT_OK&&requestCode==PickerBuilder.REQUEST_CODE_PICKER){
            new PickerRecyclerView.Builder(this).action(PreViewBuilder.PREVIEW).selectedPhotos( data.getStringArrayListExtra(PickerBuilder.KEY_SELECTED_PHOTOS)).columnCount(3).build(pv5PreView);
        }
    }
}
