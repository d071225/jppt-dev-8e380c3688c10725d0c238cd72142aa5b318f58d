package com.hletong.hyc.contract;

import com.hletong.hyc.model.DictionaryItem;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.model.DataSource;
import com.hletong.mob.architect.model.repository.DataCallback;
import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.requestvalue.ListRequestValue;
import com.hletong.mob.architect.view.IBaseView;
import com.xcheng.okhttp.callback.UICallback;

import java.util.List;
import java.util.Map;

/**
 * 车辆类型，车辆颜色，船舶类型等
 * Created by ddq on 2017/1/5.
 */

public interface DictItemContract {
    interface View extends IBaseView {
        void showDicts(List<DictionaryItem> list);
    }

    interface Presenter extends IBasePresenter {
        /**
         * 可以从缓存获取
         **/
        void loadDictItems(String fieldName, String url);
    }

    interface DictLocalSource {
        void loadDict(String key, UICallback<List<DictionaryItem>> callback);

        void saveDict(Map<String, List<DictionaryItem>> map, UICallback<CommonResult> callback);
    }
}
