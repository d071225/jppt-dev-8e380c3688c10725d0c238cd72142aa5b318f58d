package com.hletong.hyc.presenter;

import android.text.TextUtils;

import com.hletong.hyc.contract.QuoteContract;
import com.hletong.hyc.model.CallInfo;
import com.hletong.hyc.model.FileInfo;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.http.EasyCallback;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.callback.UICallback;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;
import com.xcheng.okhttp.util.ParamUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongdaqing on 2017/5/31.
 */
public class DirectQuotePresenter<T extends QuoteContract.DirectQuoteView> extends BasePresenter<T> implements QuoteContract.DirectQuotePresenter {
    private String file;

    public DirectQuotePresenter(T view, String file) {
        super(view);
        this.file = file;
    }

    @Override
    public void start() {
        downloadFiles();
    }

    @Override
    public void record(CallInfo callInfo) {
        if (Validator.isNotNull(callInfo, getView(), "状态出错，无法执行操作")) {
            new ExecutorCall<CommonResult>(callInfo.getRequest(getView())).enqueue(new UICallback<CommonResult>() {
                @Override
                public void onError(OkCall<CommonResult> okCall, EasyError error) {
                    //do nothing
                }

                @Override
                public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                    //di nothing
                }
            });
        }
    }

    private void downloadFiles() {
        if (!TextUtils.isEmpty(file)) {
            OkRequest request = EasyOkHttp
                    .get(String.format(Constant.FETCH_GROUP_PICTURES_URL, file))
                    .build();

            new ExecutorCall<Images>(request).enqueue(new EasyCallback<Images>(getView(), true) {
                @Override
                public void onSuccess(OkCall<Images> okCall, Images response) {
                    if (response.empty())
                        return;

                    List<String> images = new ArrayList<>(response.list.size());
                    for (int index = 0; index < response.list.size(); index++) {
                        images.add(response.list.get(index).getFileDownloadUrl());
                    }
                    getView().showImages(images);
                }
            });
        }
    }

    private class Images {
        private String fileGroupId;
        private List<FileInfo> list;

        boolean empty() {
            return ParamUtil.isEmpty(list);
        }
    }
}