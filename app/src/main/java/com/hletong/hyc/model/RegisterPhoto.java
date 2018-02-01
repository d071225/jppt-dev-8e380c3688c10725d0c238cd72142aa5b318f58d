package com.hletong.hyc.model;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;

import com.hletong.hyc.R;
import com.hletong.hyc.util.Constant;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 注册的图片资源
 **/
public class RegisterPhoto implements Serializable {
    private static final long serialVersionUID = -7783721077208360886L;
    private final String[] photosDescriptions;
    private final String[] photos;
    private final int[] photosDemo;
    //比例
    private final float ratio;

    private String fileGroupId;
    //是否需要全部添加
    private boolean isFullAdd = true;
    //设置上传地址，如果为null 会有默认地址
    private String upLoadUrl;
    //列表中的位置
    private int index;
    //是否从服务器导入的
    private boolean isHttp;

    //提交的图片是否不需要登录就可以查看
    private boolean isPublic;

    /**
     * @param photosDescriptions
     * @param photosDemo
     * @param ratio              高/宽
     */
    public RegisterPhoto(@NonNull @Size(min = 1) String[] photosDescriptions, @NonNull @Size(min = 1) int[] photosDemo, float ratio) {
        this.photosDescriptions = photosDescriptions;
        this.photosDemo = photosDemo;
        this.photos = new String[photosDescriptions.length];
        this.ratio = ratio;
        this.isPublic = false;
    }

    public RegisterPhoto(@NonNull @Size(min = 1) String[] photosDescriptions, float ratio) {
        this(photosDescriptions, new int[photosDescriptions.length], ratio);
    }


    public void reset() {
        for (int index = 0; index < photos.length; index++) {
            photos[index] = null;
        }
        fileGroupId = null;
    }

    @DrawableRes
    public int getDefaultSrc(int index) {
        if (photosDemo[index] != 0) {
            return photosDemo[index];
        }
        return R.drawable.add_upload;
    }

    public boolean isFitCenter(int index) {
        return photosDemo[index] != 0;
    }

    public float getRatio() {
        if (ratio == 0) {
            return 1.6f;
        }
        return ratio;
    }

    public String getFileGroupId() {
        return fileGroupId;
    }

    public void setFileGroupId(String fileGroupId) {
        this.fileGroupId = fileGroupId;
    }

    public String[] getPhotos() {
        return photos;
    }

    /**
     * isFullAdd 如果为false 存在部分图片上传的情况
     *
     * @return
     */
    public ArrayList<String> getUpLoadPhotos() {
        ArrayList<String> upLoadPhotos = new ArrayList<>(0);
        for (String photo : photos) {
            if (photo != null) {
                upLoadPhotos.add(photo);
            }
        }
        return upLoadPhotos;
    }

    /**
     * 是否已经全部添加了
     **/
    public boolean canUpload() {
        //如果从网络下载 认证导入的照片，没有url 只有fileGroupId
        if (isHttp) {
            return true;
        }
        if (isFullAdd) {
            for (String photo : photos) {
                if (photo == null) {
                    return false;
                }
            }
            return true;
        } else {
            for (String photo : photos) {
                if (photo != null) {
                    return true;
                }
            }
            return false;
        }
    }

    /**
     * 不能重复添加相同的照片
     *
     * @param photo 图片地址 null表示删除某张照片
     * @param index 图片位置
     * @return 是否添加成功
     */
    public boolean setPhotoIndex(@Nullable String photo, int index) {
        if (photo != null) {
            for (String temp : photos) {
                if (photo.equals(temp)) {
                    return false;
                }
            }
        }
        photos[index] = photo;
        return true;
    }

    public String[] getPhotosDescriptions() {
        return photosDescriptions;
    }

    public void setFullAdd(boolean fullAdd) {
        isFullAdd = fullAdd;
    }

    public void setUpLoadUrl(@NonNull String upLoadUrl) {
        this.upLoadUrl = upLoadUrl;
    }

    public String getUpLoadUrl() {
        if (upLoadUrl == null) {
            return Constant.getUrl(Constant.IMAGE_PUBLIC_UPLOAD);
        }
        return upLoadUrl;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public boolean isHttp() {
        return isHttp;
    }

    public void setHttp(boolean http) {
        isHttp = http;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    public boolean isPublic() {
        return isPublic;
    }
}