package com.hletong.hyc.presenter;

import com.hletong.hyc.model.FileInfo;
import com.hletong.hyc.contract.ComplainContract;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.util.ParamsHelper;

import java.util.ArrayList;

/**
 * Created by ddq on 2017/3/30.
 */

public class ComplainPresenter extends FilePresenter<ComplainContract.View> implements ComplainContract.Presenter {
    private String mTradeUuid;
    private String confirmType;
    private String content;

    public ComplainPresenter(ComplainContract.View view, String tradeUuid, String confirmType) {
        super(view);
        this.mTradeUuid = tradeUuid;
        this.confirmType = confirmType;
    }

    @Override
    protected void uploadFinished(final int remain, boolean error, FileInfo fileInfo) {
        if (remain == 0 && !error) {
            simpleSubmit(
                    Constant.getUrl(Constant.COMPLAIN),
                    new ParamsHelper()
                            .put("tradeUuid", mTradeUuid)
                            .put("confirmType", confirmType)
                            .put("content", content)
                            .put("fileUuid", getGroupId())
            );
        }
    }

    @Override
    public void submit(String content, ArrayList<String> images) {
        if (Validator.isNotNull(content,getView(),"内容不能为空")){
            this.content = content;
            upload(images, Constant.getUrl(Constant.IMAGE_UPLOAD),false);
        }
    }
}
