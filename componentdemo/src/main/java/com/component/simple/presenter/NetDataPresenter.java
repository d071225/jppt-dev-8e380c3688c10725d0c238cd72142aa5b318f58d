package com.component.simple.presenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.v4.app.FragmentActivity;

import com.hletong.mob.architect.presenter.ListPresenter;
import com.hletong.mob.architect.requestvalue.ListRequestValue;
import com.hletong.mob.architect.view.ListContract;
import com.hletong.mob.gallery.builder.PickerBuilder;
import com.hletong.mob.gallery.entity.PhotoDirectory;
import com.hletong.mob.gallery.utils.MediaStoreHelper;

import java.util.List;

/**
 * Created by chengxin on 2017/3/22.
 */
public class NetDataPresenter extends ListPresenter<String> implements ListContract.Presenter<String> {

    public static Handler HANDLER = new Handler(Looper.getMainLooper());
    private FragmentActivity mFragmentActivity;

    public NetDataPresenter(ListContract.View<String> mView, FragmentActivity activity) {
        super(mView);
        mFragmentActivity = activity;
    }

    @Override
    public void loadList(ListRequestValue<String> requestValue, final boolean refresh) {

        Bundle mediaStoreArgs = new Bundle();
        mediaStoreArgs.putBoolean(PickerBuilder.EXTRA_SHOW_GIF, false);
        MediaStoreHelper.getPhotoDirs(mFragmentActivity, mediaStoreArgs,
                new MediaStoreHelper.PhotosResultCallback() {

                    @Override
                    public void onResultCallback(final List<PhotoDirectory> directories) {
                        //第一次加载获取，剔除不存在的文件
                        getView().showList(directories.get(0).getPhotos(), refresh);
                    }
                });

    }
}
