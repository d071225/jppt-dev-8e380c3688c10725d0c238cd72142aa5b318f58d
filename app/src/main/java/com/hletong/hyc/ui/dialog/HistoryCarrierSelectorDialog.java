package com.hletong.hyc.ui.dialog;

import android.content.Context;
import android.os.Bundle;

import com.hletong.hyc.model.ContractCarrierInfo;
import com.hletong.mob.architect.error.DataError;
import com.hletong.mob.architect.error.ErrorState;
import com.hletong.mob.dialog.selector.BottomSelectorDialog;
import com.xcheng.okhttp.util.ParamUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ddq on 2017/1/25.
 */

public class HistoryCarrierSelectorDialog extends BottomSelectorDialog<ContractCarrierInfo> {

    public HistoryCarrierSelectorDialog(Context context) {
        super(context);
    }

    @Override
    protected String getTitle() {
        return "历史提货人信息";
    }

    public void prefetch(int extra, List<ContractCarrierInfo> infos) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("list", new ArrayList<>(infos));
        setArguments(bundle);
        super.prefetch(extra);
    }

    @Override
    protected void onLoad() {
        ArrayList<ContractCarrierInfo> list = getArguments().getParcelableArrayList("list");
        if (!ParamUtil.isEmpty(list)) {
            showList(list, true);
        } else {
            handleError(new DataError(ErrorState.NO_DATA,"没有历史提货人数据"));
        }
    }

    public void show(List<ContractCarrierInfo> infos, int extra) {
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("list", new ArrayList<>(infos));
        setArguments(bundle);
        super.show(extra);
    }
}
