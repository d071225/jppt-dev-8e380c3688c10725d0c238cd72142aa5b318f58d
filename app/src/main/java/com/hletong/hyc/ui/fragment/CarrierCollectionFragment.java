package com.hletong.hyc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hletong.hyc.adapter.CarrierCollectionAdapter;
import com.hletong.hyc.adapter.MakePhoneCall;
import com.hletong.hyc.adapter.SourceListAdapter;
import com.hletong.hyc.contract.CollectionContract;
import com.hletong.hyc.model.CarrierCollection;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.presenter.CollectionPresenter;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;

/**
 * Created by ddq on 2017/3/27.
 * 货方会员收藏的车船
 */

public class CarrierCollectionFragment extends BaseRefreshFragment<CarrierCollection> implements CollectionContract.View, MakePhoneCall {
    private String memberCode;

    public static Bundle getParam(String cargoUuid) {
        Bundle bundle = new Bundle();
        bundle.putString("cargoUuid", cargoUuid);
        return bundle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memberCode = LoginInfo.getLoginInfo().getMember_code();
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<CarrierCollection> createAdapter() {
        return new CarrierCollectionAdapter(
                getContext(),
                new CollectionPresenter(this),
                this, this,
                getArguments().getString("cargoUuid"));
    }

    @Override
    protected ParamsHelper getRequestJson(boolean refresh) {
        return super.getRequestJson(refresh)
                .put("memberCode", memberCode)
                .put("cargoUuid", getArguments().getString("cargoUuid"));
    }

    @Override
    protected String getEntry() {
        return null;
    }

    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.CARRIER_COLLECTION_LIST);
    }


    @Override
    public void success(String message) {
        showMessage(message);
    }

    @Override
    public void call(String contact, String contactTel, String src) {
        CallPhoneDialog.getInstance().show(getFragmentManager(), "拨打电话", "确定要联系" + contact + "吗？", contactTel);
    }

    @Override
    public void onActivityResultOk(int requestCode, Intent data) {
        super.onActivityResultOk(requestCode, data);
        if (requestCode == SourceListAdapter.BaseViewHolder.REQUEST_CODE_FOR_LIST_ITEM)
            mPtrFrameLayout.autoRefresh(true);
    }
    @Override
    public void updateItem(String carrierMemberCode) {
        int index = -1;
        CarrierCollectionAdapter adapter = (CarrierCollectionAdapter) mAdapter;
        for (int i = 0; i < adapter.getDataCount(); i++) {
            CarrierCollection cc = adapter.getData().get(i);
            if (cc.getCarrierMemberCode().equals(carrierMemberCode)) {
                cc.informed();
                index = i;
                break;
            }
        }

        if (index >= 0)
            adapter.notifyItemChanged(index + adapter.getHeaderCount() + adapter.getEmptyCount());
    }
}
