package com.hletong.mob.architect.requestvalue;

import com.hletong.mob.architect.model.MixItemSpec;

import java.util.ArrayList;

/**
 * Created by ddq on 2016/12/27.
 */

public class MixListRequestValue extends ListRequestValue<Object> {
    private ArrayList<MixItemSpec> mItemSpecs;

    public MixListRequestValue(ListRequestValue<Object> requestValue) {
        super(requestValue.getHttpFlag(), requestValue.getUrl(), requestValue.getParams(), null);
    }

    public void setItemSpecs(ArrayList<MixItemSpec> itemSpecs) {
        this.mItemSpecs = itemSpecs;
    }

    public ArrayList<MixItemSpec> getItemSpecs() {
        return mItemSpecs;
    }
}
