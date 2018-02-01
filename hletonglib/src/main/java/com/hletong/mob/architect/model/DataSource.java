package com.hletong.mob.architect.model;

import com.hletong.mob.architect.model.repository.DataCallback;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.requestvalue.ListRequestValue;
import com.hletong.mob.architect.requestvalue.FileRequestValue;
import com.hletong.mob.architect.requestvalue.MixListRequestValue;

import java.io.File;
import java.util.List;

/**
 * Created by ddq on 2016/12/6.
 */
public interface DataSource {
    <R> void loadItem(ItemRequestValue<R> mRequestValue, DataCallback<R> mCallback);
    <R> void loadList(ListRequestValue<R> mRequestValue, DataCallback<List<R>> mCallback);
    <R>void uploadFile(ItemRequestValue<R> mRequestValue, DataCallback<R> mCallback);
    void loadFile(FileRequestValue mRequestValue, DataCallback<File> mCallback);
    void loadMixList(MixListRequestValue mRequestValue, DataCallback<List<Object>> mCallback);
}
