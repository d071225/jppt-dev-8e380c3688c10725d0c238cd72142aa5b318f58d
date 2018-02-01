package com.hletong.hyc.model;

import com.hletong.hyc.util.Constant;

import java.util.Locale;

/**
 * Created by ddq on 2016/11/29.
 */
public class FileInfo {
    private String file_path;
    private String file_ext;
    private String file_group_id;
    private String file_orignal_name;
    private String file_name;
    private String file_id;
    private String file_pre;
    private String file_size;
    private String _userId;

    public void setFile_path(String file_path) {
        this.file_path = file_path;
    }

    public void setFile_ext(String file_ext) {
        this.file_ext = file_ext;
    }

    public void setFile_group_id(String file_group_id) {
        this.file_group_id = file_group_id;
    }

    public void setFile_orignal_name(String file_orignal_name) {
        this.file_orignal_name = file_orignal_name;
    }

    public void setFile_name(String file_name) {
        this.file_name = file_name;
    }

    public void setFile_id(String file_id) {
        this.file_id = file_id;
    }

    public void setFile_pre(String file_pre) {
        this.file_pre = file_pre;
    }

    public void setFile_size(String file_size) {
        this.file_size = file_size;
    }

    public void set_userId(String _userId) {
        this._userId = _userId;
    }

    public String getFile_path() {
        return file_path;
    }

    public String getFile_ext() {
        return file_ext;
    }

    public String getFile_group_id() {
        return file_group_id;
    }

    public String getFile_orignal_name() {
        return file_orignal_name;
    }

    public String getFile_name() {
        return file_name;
    }

    public String getFile_id() {
        return file_id;
    }

    public String getFile_pre() {
        return file_pre;
    }

    public String getFile_size() {
        return file_size;
    }

    public String get_userId() {
        return _userId;
    }

    public String getFileDownloadUrl(){
        return String.format(Locale.ENGLISH,Constant.getUrl(Constant.FILE_DOWNLOAD_URL),file_id);
    }

//    public String GetFileDownloadUrl(){
//        return String.format(Locale.ENGLISH,Constant.GetUrl(Constant.FILE_DOWNLOAD_URL),file_id);
//    }
}
