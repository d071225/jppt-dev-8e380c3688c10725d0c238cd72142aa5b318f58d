package com.hletong.hyc.contract;

/**
 * Created by dongdaqing on 2017/5/23.
 */

public interface LocationView {
    void startLocating(boolean isForceStart);

    /**位置信息不匹配
     * @param message
     */
    void onLocationError(String message);
}
