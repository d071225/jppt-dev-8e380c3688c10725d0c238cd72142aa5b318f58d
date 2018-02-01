package com.hletong.hyc.model;

import java.util.List;

/**
 * Created by chengxin on 2016/12/21.
 */
public class AdInfo {
    private String advUuid;
    private String advName;
    private String file;
    private List<String> fileList;
    private int showTime;
    private String jumpUrl;

    public String getAdvUuid() {
        return advUuid;
    }

    public String getAdvName() {
        return advName;
    }

    public String getFile() {
        return file;
    }

    public List<String> getFileList() {
        return fileList;
    }

    public int getShowTime() {
        return showTime;
    }

    public String getJumpUrl() {
        return jumpUrl;
    }
}
