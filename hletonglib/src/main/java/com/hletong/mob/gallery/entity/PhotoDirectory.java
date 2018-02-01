package com.hletong.mob.gallery.entity;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**图片文件夹
 * Created by  cx on 15/6/28.cx
 */
public class PhotoDirectory {

    private String id;
    private String coverPath;
    private String name;
    private long dateAdded;
    private final List<String> photos = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhotoDirectory)) return false;
        PhotoDirectory directory = (PhotoDirectory) o;
        boolean hasId = !TextUtils.isEmpty(id);
        boolean otherHasId = !TextUtils.isEmpty(directory.id);
        if (hasId && otherHasId) {
            if (!TextUtils.equals(id, directory.id)) {
                return false;
            }
            return TextUtils.equals(name, directory.name);
        }
        return false;
    }

    @Override
    public int hashCode() {
        if (TextUtils.isEmpty(id)) {
            if (TextUtils.isEmpty(name)) {
                return 0;
            }
            return name.hashCode();
        }
        int result = id.hashCode();
        if (TextUtils.isEmpty(name)) {
            return result;
        }
        result = 31 * result + name.hashCode();
        return result;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCoverPath() {
        return coverPath;
    }

    public void setCoverPath(String coverPath) {
        this.coverPath = coverPath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    //获取图片地址
    public List<String> getPhotos() {
        return photos;
    }

    /**
     * 获取图片数量
     *
     * @return 列表长度
     */
    public int getPhotoSize() {
        return photos.size();
    }

    public void addPhoto(String photoPath) {
        photos.add(photoPath);
    }

    public void addPhoto(int index, String photoPath) {
        photos.add(index, photoPath);
    }
}
