package com.hletong.hyc.model;

/**
 * Created by ddq on 2016/11/29.
 */
public class FileResult {
    private String status;
    private FileInfo file_info;

    public FileInfo getFile_info(){
        return file_info;
    }

    public boolean isSuccess(){
        return "success".equals(status);
    }
}
