package com.hletong.hyc.contract;

import org.json.JSONObject;

public interface CancelApplyContract {
    interface CancelApplyView {
        //撤销成功
        void CancelSuccess();
        //撤销失败
        void CancelDefeat();

        JSONObject getJsonObject();
    }

    interface CancelPresenter {
        void CancelApply();
    }
}
