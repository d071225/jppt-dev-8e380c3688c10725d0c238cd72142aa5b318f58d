package com.hletong.mob.gallery.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.hletong.mob.R;
import com.hletong.mob.gallery.builder.PickerBuilder;
import com.hletong.mob.gallery.entity.PhotoDirectory;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;
import static android.provider.MediaStore.MediaColumns.SIZE;

/**
 * Created by donglua on 15/5/31.
 */
public class MediaStoreHelper {

    public static void getPhotoDirs(FragmentActivity activity, Bundle args, PhotosResultCallback resultCallback) {
        activity.getSupportLoaderManager()
                .initLoader(0, args, new PhotoDirLoaderCallbacks(activity,resultCallback));
    }

    static class PhotoDirLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

        private WeakReference<Context> context;
        private PhotosResultCallback resultCallback;

        public PhotoDirLoaderCallbacks(Context context,PhotosResultCallback resultCallback ) {
            this.context = new WeakReference<>(context);
            this.resultCallback = resultCallback;
        }

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new PhotoDirectoryLoader(context.get(), args.getBoolean(PickerBuilder.EXTRA_SHOW_GIF, false), args.getInt(PickerBuilder.EXTRA_PHOTO_SIZE, 3600));
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data == null) return;
            List<PhotoDirectory> directories = new ArrayList<>();
            PhotoDirectory photoDirectoryAll = new PhotoDirectory();
            photoDirectoryAll.setName(context.get().getString(R.string.__picker_all_image));
            photoDirectoryAll.setId("ALL");
            while (data.moveToNext()) {
                //图片的id
                int photoId = data.getInt(data.getColumnIndexOrThrow(_ID));
                String dirBucketId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
                String dirName = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
                String photoPath = data.getString(data.getColumnIndexOrThrow(DATA));
                int photoSize = data.getInt(data.getColumnIndexOrThrow(SIZE));
                //尝试创建新的文件夹
                PhotoDirectory photoDirectory = new PhotoDirectory();
                photoDirectory.setName(dirName);
                photoDirectory.setId(dirBucketId);

                if (!directories.contains(photoDirectory)) {
                    photoDirectory.setCoverPath(photoPath);
                    photoDirectory.addPhoto(photoPath);
                    photoDirectory.setDateAdded(data.getLong(data.getColumnIndexOrThrow(DATE_ADDED)));
                    directories.add(photoDirectory);
                } else {
                    directories.get(directories.indexOf(photoDirectory)).addPhoto(photoPath);
                }
                photoDirectoryAll.addPhoto(photoPath);
            }
            if (photoDirectoryAll.getPhotoSize() > 0) {
                photoDirectoryAll.setCoverPath(photoDirectoryAll.getPhotos().get(0));
            }
            directories.add(PickerBuilder.INDEX_ALL_PHOTOS, photoDirectoryAll);
            if (resultCallback != null) {
                resultCallback.onResultCallback(directories);
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    }
    public interface PhotosResultCallback {
        void onResultCallback(List<PhotoDirectory> directories);
    }
}
