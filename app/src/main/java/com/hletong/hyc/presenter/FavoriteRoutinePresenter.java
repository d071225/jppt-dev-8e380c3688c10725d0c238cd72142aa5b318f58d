package com.hletong.hyc.presenter;

import android.content.Intent;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.contract.FavoriteRoutineContract;
import com.hletong.hyc.http.parse.JpptParse;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.architect.view.IProgress;
import com.hletong.mob.http.EasyCallback;
import com.hletong.mob.util.FinishOptions;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ddq on 2017/2/10.
 * 常跑路线
 */

public class FavoriteRoutinePresenter extends BasePresenter<FavoriteRoutineContract.View> implements FavoriteRoutineContract.Presenter {
    private Address sa;//出发地
    private Address ea;//目的地

    public FavoriteRoutinePresenter(FavoriteRoutineContract.View view) {
        super(view);
    }

    public FavoriteRoutinePresenter(FavoriteRoutineContract.View view, Address sa, Address ea) {
        super(view);
        this.sa = sa;
        this.ea = ea;
    }

    @Override
    public void add(Address sa, Address ea) {
        if (Validator.isNotNull(sa, getView(), "请选择常跑地址")
                && Validator.isNotNull(ea, getView(), "请选择常跑地址")) {
            try {
                JSONObject object = new JSONObject();
                object
                        .put("loadingProvince", sa.getProvinceForQuery())
                        .put("loadingCity", sa.getCityForQuery())
                        .put("loadingCountry", sa.getCountyForQuery())
                        .put("unloadProvince", ea.getProvinceForQuery())
                        .put("unloadCity", ea.getCityForQuery())
                        .put("unloadCountry", ea.getCountyForQuery())
                        .put("vehiclesType", BuildConfig.carrier_type);
                OkRequest request = EasyOkHttp.get(Constant.ADD_FAVORITE_ROUTINE)
                        .tag(tag())
                        .extra(JpptParse.ENTRY, "data")
                        .param("jsontxt", object.toString())
                        .build();

                new ExecutorCall<String>(request).enqueue(new EasyCallback<String>(getView()) {
                    @Override
                    public void onSuccess(OkCall<String> okCall, String response) {
                        getView().success("添加成功");
                        Intent intent = new Intent();
                        //添加常跑路线，返回值是memberCode
                        intent.putExtra("memberCode", response);
                        getView().finishWithOptions(FinishOptions.FORWARD_RESULT(intent));
                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void delete(String routineUuid, IProgress progress) {
        if (Validator.isNotNull(routineUuid, getView(), "删除线路参数出错")) {
            OkRequest request = EasyOkHttp.get(Constant.DELETE_FAVORITE_ROUTINE)
                    .tag(tag())
                    .param("routeUuid", routineUuid)
                    .build();
            new ExecutorCall<CommonResult>(request).enqueue(new SubmitForwardResultCallback(getView(), getView(), getView()));
        }
    }

    @Override
    public boolean isCargoParamValid() {
        return sa != null && ea != null;
    }

    @Override
    public Address getStartAddress() {
        return sa;
    }

    @Override
    public Address getEndAddress() {
        return ea;
    }

}
