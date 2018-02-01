package com.hletong.mob.architect.view;

/**
 * Created by ddq on 2017/3/29.
 * 请求提交成功，递交一个成功消息给View
 * 多用于返回的Entity是{@link com.hletong.mob.architect.model.CommonResult}的情况
 */

public interface ICommitSuccessView {
    void success(String message);
}
