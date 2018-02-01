package com.hletong.hyc.model;

import com.xcheng.okhttp.util.ParamUtil;

import java.util.List;

/**
 * Created by dongdaqing on 2017/8/16.
 */

public class Images {
    private String fileGroupId;
    private List<FileInfo> list;

    public boolean empty() {
        return ParamUtil.isEmpty(list);
    }

    public List<FileInfo> getList() {
        return list;
    }

    public FileInfo get(int index){
        return list.get(index);
    }
}
