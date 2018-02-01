package com.hletong.hyc.presenter.cargo;

import android.content.Intent;
import android.support.annotation.NonNull;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.CargoForecastContract;
import com.hletong.hyc.model.Cargo;
import com.hletong.hyc.model.Images;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.fragment.CargoForecastBaseSelectFragment;
import com.hletong.hyc.ui.fragment.SelectCargoFragment;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.Constant;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.callback.UICallback;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by dongdaqing on 2017/8/25.
 */
public class BlockCargoPresenter extends BlockBasePresenter<CargoForecastContract.BlockCargoView> implements CargoForecastContract.BlockCargoPresenter {
    private Cargo mCargo;

    public BlockCargoPresenter(@NonNull CargoForecastContract.BlockCargoView baseView) {
        super(baseView);
    }

    @Override
    protected void handleBillingType(int billingType) {
        if (isSelfTrade()) {
            getView().hideBlock();
        } else {
            getView().showBlock();
        }
    }

    @Override
    public Map<String, String> getParams() {
        return null;
    }

    @Override
    public void selectCargo() {
        getView().toActivity(
                CommonWrapFragmentActivity.class,
                CommonWrapFragmentActivity.getBundle("选择货物",
                        SelectCargoFragment.class,
                        CargoForecastBaseSelectFragment.getDefaultBundle(false, -1, "添加货物", "管理货物")),
                188, null);
    }

    @Override
    public void onActivityResult(int requestCode, Intent data) {
        if (requestCode == 188) {
            mCargo = data.getParcelableExtra("data");
            //显示选择的货物信息
            getView().showCargo(
                    mCargo.getOrgin_cargon_kind_name(),
                    "计量方式：" + mCargo.getMeasure_type(),
                    "单位：" + mCargo.getCargoUnit(),
                    "运输损耗：" + mCargo.getMaxWastageRtWithUnit(),
                    "长/宽/高：" + mCargo.getVolume("/"));
            if (1 == mCargo.getTransport_type()) {
                getView().showCarrierModel("车型要求：" + mCargo.getCarrier_model_type_());
                getView().showCarrierLength("车长要求：" + (mCargo.getCarrier_length_type_() == null ? "无" : mCargo.getCarrier_length_type_()));
            } else if (2 == mCargo.getTransport_type()) {
                getView().showCarrierModel("船型要求：" + mCargo.getCarrier_model_type_());
            }

            if (mCargo.withFraction()) {
                getView().updateUnitMethod(CommonInputView.NUMBER_DECIMAL);
            } else {
                getView().updateUnitMethod(CommonInputView.NUMBER);
            }

            //隐藏提示和图片
            getView().hideView(R.id.hint, R.id.gallery);

            //尝试加载图片
            if (mCargo.getCargoFileId() != null){
                download(mCargo.getCargoFileId());
            }
        }
    }

    public void download(String id) {
        OkRequest request = EasyOkHttp.get(String.format(Constant.FETCH_GROUP_PICTURES_URL, id)).build();
        new ExecutorCall<Images>(request).enqueue(new UICallback<Images>() {
            @Override
            public void onError(OkCall<Images> okCall, EasyError error) {

            }

            @Override
            public void onSuccess(OkCall<Images> okCall, Images response) {
                if (response.empty())
                    return;

                List<String> paths = new ArrayList<>(response.getList().size());
                for (int index = 0; index < response.getList().size(); index++) {
                    paths.add(response.get(index).getFileDownloadUrl());
                }
                getView().showImages(paths);
            }
        });
    }
}
