package com.hletong.mob.architect.requestvalue;

import com.hletong.mob.util.ParamsHelper;

import java.io.File;

/**
 * Created by ddq on 2016/12/27.
 * 文件请求参数
 */

public class FileRequestValue extends ItemRequestValue<File> {
    private String mFilePath;

    public FileRequestValue(int httpFlag, String url, ParamsHelper params, String filePath) {
        super(httpFlag, url, params, "data");
        this.mFilePath = filePath;
    }

    public String getFilePath() {
        return mFilePath;
    }
}
