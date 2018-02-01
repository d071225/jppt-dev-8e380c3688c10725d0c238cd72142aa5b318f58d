package com.hletong.hyc.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.hletong.hyc.contract.CargoForecastContract;
import com.hletong.hyc.contract.DraftContract;
import com.hletong.hyc.model.AuthInfo;
import com.hletong.hyc.model.CargoNoContract;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.Draft;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.TradeType;
import com.hletong.hyc.ui.activity.CargoForecastPasswordActivity;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.model.repository.DataCallback;
import com.hletong.mob.architect.model.repository.SimpleCallback;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.http.EasyCallback;
import com.hletong.mob.util.FinishOptions;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.callback.UICallback;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ddq on 2017/3/13.
 * 货源预报
 */

public class CargoForecastPresenter extends FilePresenter<CargoForecastContract.View> implements CargoForecastContract.Presenter {
    private Source mSource;//复制货源时传进来的值，不做他用
    private int billingType = -1;
    private String cargoUuid;
    private String unit;
    private boolean submit;//提交货源，要增加cargo_code和cargo_uuid
    private boolean selfTrade;//能否发布非议价自主交易

    public CargoForecastPresenter(DraftContract.LocalDataSource dataSource, CargoForecastContract.View view, Source source, String cargoUuid, boolean submit) {
        super(dataSource, view);
        mSource = source;
        this.cargoUuid = cargoUuid;
    }

    @Override
    public void start() {
        new ExecutorCall<CommonResult>(EasyOkHttp.get(Constant.E_COMM_CHECK).param("memberCode", LoginInfo.getLoginInfo().getMember_code()).build()).enqueue(new EasyCallback<CommonResult>(getView()) {
            @Override
            public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                //e商贸同已开通
                selfTrade = true;
            }

            @Override
            public void onError(OkCall<CommonResult> okCall, EasyError error) {
                //do nothing
            }
        });

        if (mSource != null) {
            HashMap<String, String> map = new HashMap<>(1);
            map.put("cargoUuid", mSource.getCargo_uuid());
            //加载货源详情
            OkRequest request = EasyOkHttp.get(Constant.COPY_CARGO_INFO).params(map).build();

            new ExecutorCall<Source>(request).enqueue(new UICallback<Source>() {
                @Override
                public void onError(OkCall<Source> okCall, EasyError error) {
                    getView().chooseDefaultType();//失败的时候就按发布货源的步骤走
                }

                @Override
                public void onSuccess(OkCall<Source> okCall, Source response) {
                    mSource = response;
                    DictionaryItem di = TradeType.getDictionaryById(mSource.getBilling_type());
                    tradeTypeSelected(di, true);
                }
            });
        } else if (cargoUuid != null) {
            //从草稿过来的
            DraftContract.LocalDataSource dataSource = (DraftContract.LocalDataSource) getDataRepository();
            dataSource.loadItem(cargoUuid, new DataCallback<Draft>(getView()) {
                @Override
                public void onSuccess(@NonNull Draft response) {
                    mSource = response;
                    DictionaryItem di = TradeType.getDictionaryById(mSource.getBilling_type());
                    tradeTypeSelected(di, true);
                }

                @Override
                public void onError(@NonNull BaseError error) {
                    getView().chooseDefaultType();//失败的时候就按发布货源的步骤走
                }
            });
        } else {
            getView().chooseDefaultType();
        }
    }

    @Override
    public void submit(String memberCode) {
        HashMap<String, String> params = new HashMap<>();
        params.put("operatorType", "submit");

        //提交待提交的货源，需要带上以下参数
        if (submit && mSource != null) {
            params.put("cargo_uuid", mSource.getCargo_uuid());
            params.put("cargo_code", mSource.getCargo_code());
        }

        getView().gatherSubmitData(params, billingType);
        unit = params.remove("units_");//保存单位，后面长期协议价会用到这里用remove是因为这个参数并不需要提交

        if (billingType == 3) {
            /**
             * 自主交易有两种：议价，非议价
             * 其中，目前议价是可以进行垫付的，非议价是不行的，所以这里对于非议价情况要判断用户是否具有自主交易权限，没权限要过滤掉
             */
            //非议价情况
            if (!"300".equals(params.get("transType"))) {
                LoginInfo loginInfo = LoginInfo.getLoginInfo();
                AuthInfo authInfo = loginInfo.getAuthInfo(3);
                if (!authInfo.isAuthorized()) {//无自主交易权限
                    getView().showSelfTradeAlert(authInfo.getAuth_msg());
                    return;
                }
            }
            params.put("cargoOwnerCode", memberCode);
            simpleSubmit(Constant.getUrl(Constant.SUBMIT_SELF_TRADE_CARGO), new ParamsHelper(params));
        } else {
            params.put("billing_type", String.valueOf(billingType));
            submit(Constant.getUrl(Constant.SUBMIT_NONE_SELF_TRADE_CARGO), new ParamsHelper().put("jsonStr", new JSONObject(params).toString()));
        }
    }

    @Override
    protected void simpleSubmitSucceed(CommonResult cr) {
        showMessageAExit("操作成功");
    }

    private void submit(String url, ParamsHelper paramsHelper) {
        ItemRequestValue<String> requestValue = new ItemRequestValue<String>(getView().hashCode(), url, paramsHelper) {
        };
        getDataRepository().loadItem(requestValue, new SimpleCallback<String>(getView()) {
            @Override
            public void onSuccess(String response) {
                if (response != null) {
                    try {
                        JSONObject object = new JSONObject(response);
                        switch (object.getInt("retCode")) {
                            case 2://长期协议价
                                CargoNoContract cargo = new Gson().fromJson(response, CargoNoContract.class);
                                cargo.setUnit(unit);
                                Bundle intent = new Bundle();
                                intent.putParcelable("cargo", cargo);
                                getView().toActivity(CargoForecastPasswordActivity.class, intent, FinishOptions.FORWARD_RESULT());
                                break;
                            default:
                                showMessageAExit("操作成功");
                                break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showMessageAExit("操作成功");
                    }
                } else {
                    showMessageAExit("操作成功");
                }
            }
        });
//        simpleSubmit(url, paramsHelper);
    }

    @Override
    public void tradeTypeSelected(DictionaryItem di, boolean initWithSource) {
        int tmp = di.getIdAsInt();
        if (!initWithSource && tmp == billingType)
            return;

        billingType = tmp;//设置开票方式
        if (initWithSource) {
            getView().tradeTypeChanged(di, mSource);
        } else {
            getView().tradeTypeChanged(di, null);
        }

        if (di.getId().equals("3") && !selfTrade) {
            getView().showDialog(false, "提示", "尊敬的用户，您尚未开通E商贸通，只可发布议价货源预报", "确定", null, null, null);
        }
    }

    @Override
    public void saveData() {
        getView().showLoading();
        DraftContract.LocalDataSource dataSource = (DraftContract.LocalDataSource) getDataRepository();
        Source source = new Source();
        source.setBilling_type(billingType);
        source.setCargo_uuid(cargoUuid);//如果有ID，就设置ID
        getView().gatherSource(source);
        dataSource.modify(source, new SimpleCallback<CommonResult>(getView()) {
            @Override
            public void onSuccess(@NonNull CommonResult response) {
                showMessageAExit(response.getErrorInfo());
            }
        });
    }

    @Override
    public void preview() {
        Source source = new Source();
        if (!getView().gatherSource(source)) {
            handleMessage("参数出错，无法预览");
            return;
        }
        getView().preview(source);
    }

    private void showMessageAExit(String message) {
        getView().success(message);
        getView().finishWithOptions(FinishOptions.FORWARD_RESULT());
    }
}
