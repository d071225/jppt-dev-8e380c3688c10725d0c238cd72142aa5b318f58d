package com.hletong.mob.architect.model.repository;


import com.hletong.mob.architect.model.DataSource;
import com.hletong.mob.architect.requestvalue.FileRequestValue;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.requestvalue.ListRequestValue;
import com.hletong.mob.architect.requestvalue.MixListRequestValue;

import java.io.File;
import java.util.List;

/**
 * Created by ddq on 2016/12/6.
 */
public class DataRepository implements DataSource {

    private DataSource mLocalDataSource;
    private DataSource mRemoteDataSource;

    private static DataRepository mDataRepository;

    public DataRepository() {
        mRemoteDataSource = new RemoteDataSource();
    }

    public static DataSource getInstance() {
        if (mDataRepository == null)
            mDataRepository = new DataRepository();
        return mDataRepository;
    }

    @Override
    public <R> void loadItem(ItemRequestValue<R> mRequestValue, DataCallback<R> mCallback) {
        mRemoteDataSource.loadItem(mRequestValue, new UIDataCallback<>(mCallback));
    }

    @Override
    public <R> void loadList(ListRequestValue<R> mRequestValue, DataCallback<List<R>> mCallback) {
        mRemoteDataSource.loadList(mRequestValue, new UIDataCallback<>(mCallback));
    }

    @Override
    public <R> void uploadFile(ItemRequestValue<R> mRequestValue, DataCallback<R> mCallback) {
        mRemoteDataSource.uploadFile(mRequestValue, new UIDataCallback<>(mCallback));
    }

    @Override
    public void loadFile(FileRequestValue mRequestValue, DataCallback<File> mCallback) {
        mRemoteDataSource.loadFile(mRequestValue, new UIDataCallback<>(mCallback));
    }

    @Override
    public void loadMixList(MixListRequestValue mRequestValue, DataCallback<List<Object>> mCallback) {
        mRemoteDataSource.loadMixList(mRequestValue, new UIDataCallback<>(mCallback));
    }
}
