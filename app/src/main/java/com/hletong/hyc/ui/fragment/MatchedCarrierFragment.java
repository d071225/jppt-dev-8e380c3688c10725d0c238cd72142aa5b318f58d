package com.hletong.hyc.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.hletong.hyc.adapter.MakePhoneCall;
import com.hletong.hyc.adapter.MatchedCarrierAdapter;
import com.hletong.hyc.model.MatchedCarrier;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;

/**
 * Created by ddq on 2017/3/27.
 * 匹配车船列表
 */

public class MatchedCarrierFragment extends BaseRefreshFragment<MatchedCarrier> implements MakePhoneCall {

    public static Bundle getParams(String cargoUuid, String transType) {
        Bundle bundle = new Bundle();
        bundle.putString("cargoUuid", cargoUuid);
        bundle.putString("transType", transType);
        return bundle;
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<MatchedCarrier> createAdapter() {
        return new MatchedCarrierAdapter(getContext(), this);
    }

    @Override
    public void call(String contact, String contactTel, String src) {
        if (TextUtils.isEmpty(contactTel)) {
            showMessage("联系人信息不完整，无法拨打电话");
            return;
        }
        CallPhoneDialog.getInstance().show(getFragmentManager(), "拨打电话", "确定要联系" + contact + "吗?", contactTel);
    }

    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.MATCHED_CARRIER);
    }

    @Override
    protected ParamsHelper getRequestJson(boolean refresh) {
        return super.getRequestJson(refresh)
                .put("cargoUuid", getArguments().getString("cargoUuid"))
                .put("transType", getArguments().getString("transType"));
    }

    @Override
    protected String getEntry() {
        return null;
    }
}
