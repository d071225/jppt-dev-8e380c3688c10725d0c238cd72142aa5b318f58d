package com.hletong.hyc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.UploadPhotoContract;
import com.hletong.hyc.model.RegisterPhoto;
import com.hletong.hyc.presenter.UploadPhotoPresenter;
import com.hletong.mob.gallery.builder.PreViewBuilder;
import com.xcheng.view.processbtn.ProcessButton;
import com.xcheng.view.util.LocalDisplay;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UploadPhotoActivity extends ImageSelectorActivityNew<UploadPhotoContract.Presenter> implements UploadPhotoContract.View {

    @BindView(R.id.processBtn)
    ProcessButton processButton;
    @BindView(R.id.layout_container)
    LinearLayout layoutContainer;

    @Override
    public int getLayoutId() {
        return R.layout.activity_select_photo;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setCustomTitle(R.string.upload_photo);
        getPresenter().start();
    }

    @OnClick(R.id.processBtn)
    @Override
    public void onClick(View v) {
        if (v == processButton) {
            getPresenter().upload();
        } else {
            String path = (String) v.getTag(R.id.picker_data);
            if (path != null) {
                setPicView(v);
                toPreViewActivity(path);
            } else {
                showSelector(v);
            }
        }
    }

    private void toPreViewActivity(String path) {
        ArrayList<String> paths = new ArrayList<>();
        paths.add(path);
        PreViewBuilder.builder().setAction(PreViewBuilder.DELETE).setSelectPhotos(paths).start(this);
    }

    @Override
    public void photoCaptured(String path, View picView) {
        int index = (int) picView.getTag(R.id.image_index);
        getPresenter().photoSelected(index, path);
    }

    @Override
    public void photoDelete(View picView) {
        int index = (int) picView.getTag(R.id.image_index);
        getPresenter().photoSelected(index, null);
    }

    @Override
    protected UploadPhotoContract.Presenter createPresenter() {
        return new UploadPhotoPresenter(this, (RegisterPhoto) getSerializable(RegisterPhoto.class.getSimpleName()));
    }

    @Override
    public void inflateImageView(int index, float ratio, String des) {
        int sw = LocalDisplay.widthPixel();
        int photoWidth = sw > 720 ? 720 : sw - 2 * LocalDisplay.dp2px(15);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(photoWidth, (int) (photoWidth * ratio));
        ViewGroup viewGroup = (ViewGroup) getLayoutInflater().inflate(R.layout.item_select_photo, null);
        TextView textView = viewGroup.findViewById(R.id.tvDescription);
        textView.setText(des);
        ImageView imageView = viewGroup.findViewById(R.id.ivPhoto);
        /**设置imageView对应在数组中的下标**/
        imageView.setTag(R.id.image_index, index);
        imageView.setLayoutParams(lp);
        imageView.setOnClickListener(this);
        layoutContainer.addView(viewGroup, index);
    }

    @Override
    public void showImage(int index, String url) {
        ImageView iv = layoutContainer.getChildAt(index).findViewById(R.id.ivPhoto);
        iv.setTag(R.id.picker_data, url);
        super.photoCaptured(url, iv);
    }

    @Override
    protected ProcessButton getProcessBtn() {
        return processButton;
    }

    @Override
    public void showImage(int index, int src, boolean center) {
        ImageView iv = layoutContainer.getChildAt(index).findViewById(R.id.ivPhoto);
        iv.setTag(R.id.picker_data, null);
        if (center)
            Glide.with(this).load(src).fitCenter().into(iv);
        else
            Glide.with(this).load(src).into(iv);
    }

    @Override
    public void changeButtonState(boolean enable) {
        processButton.setEnabled(enable);
    }


}
