package com.hletong.hyc.ui.fragment;

import android.content.Context;
import android.os.Bundle;

import com.hletong.hyc.model.Source;
import com.hletong.mob.dialog.selector.BottomSelectorDialog;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ddq on 2017/1/9.
 * 抵扣税率选择
 */

public class DeductTaxSelectorDialog extends BottomSelectorDialog<Source.DeductRt> {

    public DeductTaxSelectorDialog(Context context) {
        super(context);
    }

    public static DeductTaxSelectorDialog getInstance(ArrayList<Source.DeductRt> mDeductRts, Context activity) {
        DeductTaxSelectorDialog mFragment = new DeductTaxSelectorDialog(activity);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("data", mDeductRts);
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @Override
    protected String getTitle() {
        return "选择抵扣税率";
    }

    @Override
    protected void onLoad() {
        hideLoading();//关闭动画
        List<Source.DeductRt> mDeductRtList = getArguments().getParcelableArrayList("data");
        showList(mDeductRtList, true);
    }
}
