package com.hletong.hyc.model.validate;

import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.view.IBaseView;

/**
 * Created by ddq on 2017/1/4.
 */

public interface Validate<T> {
    //执行数据校验，成功则返回RequestValue，失败返回null
    ItemRequestValue<T> validate(IBaseView baseView);
}
