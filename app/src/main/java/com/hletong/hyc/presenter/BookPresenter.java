package com.hletong.hyc.presenter;

import android.content.DialogInterface;
import android.text.TextUtils;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.BookContract;
import com.hletong.hyc.contract.SourceContract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.ui.activity.cargo.CargoCompanyCertActivity;
import com.hletong.hyc.ui.activity.cargo.CargoPersonalCertActivity;
import com.hletong.hyc.ui.activity.ship.ShipCompanyChildCertActivity;
import com.hletong.hyc.ui.activity.ship.ShipCompanyMainCertActivity;
import com.hletong.hyc.ui.activity.ship.ShipPersonalChildCertActivity;
import com.hletong.hyc.ui.activity.truck.TruckCompanyChildCertActivity;
import com.hletong.hyc.ui.activity.truck.TruckCompanyMainCertActivity;
import com.hletong.hyc.ui.activity.truck.TruckPersonalChildCertActivity;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.error.BusiError;
import com.hletong.mob.architect.error.DataError;
import com.hletong.mob.architect.error.ErrorState;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.architect.view.IProgress;
import com.hletong.mob.http.AnimCallBack;
import com.hletong.mob.util.NumberUtil;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.OkCall;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ddq on 2017/1/5..
 * 竞挂价，报价
 */

public class BookPresenter<T extends BookContract.View> extends BasePresenter<T> implements BookContract.Presenter {
    private Source mSource;
    private LoginInfo mLoginInfo;

    private String price;
    private String cargo;

    public BookPresenter(T view) {
        super(view);
        mLoginInfo = LoginInfo.getLoginInfo();
    }

    @Override
    public void start() {
        getView().register(SourceContract.SOURCE_LOADED);
        getView().showSourceDetailFragment();
    }

    @Override
    public void sourceLoaded(Source source) {
        //货源详情加载成功，开始初始化界面
        mSource = source;
        if (isQuote()) {
            //如果是国联数据
            getView().showTitle("报价");
            getView().showButton("电话议价");
        } else {
            //1.承运重量还是承运数量
            String cargoHint = "货源剩余" + source.getRemainCargoDescWithoutUnit();
            String unit = source.getCargoUnit();
            if (source.getBook_ref_type() == 0) {
                getView().showInputView("承运重量", cargoHint, unit, true);
            } else {
                getView().showInputView("承运数量", cargoHint, unit, "立方" .equals(unit));
            }

            //整车运输，禁止输入
            if ("1" .equals(source.getMulti_transport_flag())) {
                getView().disableInputView(BuildConfig.multi_transport_label, NumberUtil.format3f(source.getRem_qt()));
            }

            //2.是否需要选择抵扣税率
            if (source.shouldDisplayDeductRt()) {
                getView().showDeductRateSelector(source.getDeduction_tax_rate());
            }

            //3.是否展示价格栏：自主开票竞价和报价货源要展示
            if (source.getTrans_type() == 1 && source.getBilling_type() == 2) {
                getView().showPrice("竞价价格", "元/" + unit, "请填写金额");
            } else if (source.getTrans_type() == 300) {
                getView().showPrice("报价", "元/" + unit, "请填写报价");
            }

            //4.展示合计价格栏
            String label = getSource().getBilling_type() == 1 ? "总收入" : "交易运费总额";
            if (source.getTrans_type() == 1) {
                getView().showIncome(label, R.color.yellow);
            } else if (source.getTrans_type() == 2) {
                getView().showIncome(label, R.color.green);
            } else if (source.getTrans_type() == 300) {
                getView().showIncome("报价运费总额", R.color.colorAccent);
            }

            //5.界面标题和提交按钮
            if (source.getTrans_type() == 1) {
                getView().showTitle("竞价投标");
                getView().showButton("确认投标");
            } else if (source.getTrans_type() == 2) {
                getView().showTitle("挂价摘牌");
                getView().showButton("确认摘牌");
            } else if (source.getTrans_type() == 300) {
                getView().showTitle("报价");
                //议价按钮在子类里面操作
            }

            //如果是兜底货源，展示兜底信息
            if (source.getWrtrTips() != null) {
                getView().showProtocolTransportHint(source.getWrtrTips().getTipsMsg());
            }
        }

        getView().showTransporter(BuildConfig.transporter_label);
    }

    @Override
    public void priceChanged(String price) {
        this.price = price;
        //计算总运费
        calculateTotalPrice(price, cargo);
    }

    @Override
    public void cargoChanged(String cargo) {
        this.cargo = cargo;
        //计算总运费
        calculateTotalPrice(price);
    }

    public Source getSource() {
        return mSource;
    }

    /**
     * 计算总价，议价以及议价摘牌子类重写函数处理
     *
     * @param unitPrice  单价
     * @param cargoCount 数(重)量
     */
    public void calculateTotalPrice(String unitPrice, String cargoCount) {
        double u, c;
        if (TextUtils.isEmpty(unitPrice))
            u = 0;
        else {
            u = Double.parseDouble(unitPrice);
        }
        if (TextUtils.isEmpty(cargoCount))
            c = 0;
        else
            c = Double.parseDouble(cargoCount);

        switch (getSource().getBilling_type()) {
            case 1://平开
                if (getSource().getTrans_type() == 1) {//竞价
                    getView().updateIncome(calculatePlatformBiddingPrice(c));
                } else {//挂价
                    getView().updateIncome(NumberUtil.format2f(c * (getSource().getTransport_unit_price() + getSource().getOther_unit_amt())));
                }
                break;
            case 2://自开
                if (getSource().getTrans_type() == 1) {//竞价
                    getView().updateIncome(NumberUtil.format2f(u * c));
                } else {//挂价
                    getView().updateIncome(NumberUtil.format2f(c * getSource().getTransport_unit_price()));
                }
                break;
            case 3://自主交易
                if (getSource().getTrans_type() == 300) {
                    //议价
                    getView().updateIncome(NumberUtil.format2f(u * c));
                } else {
                    //普通自主交易
                    getView().updateIncome(NumberUtil.format2f(c * getSource().getTransport_unit_price()));
                }
                break;
        }
    }

    public final void calculateTotalPrice(String unitPrice) {
        calculateTotalPrice(unitPrice, cargo);
    }

    //计算平台开票竞价货源的总收入
    protected String calculatePlatformBiddingPrice(double cargo) {
        return "0";
    }

    //是否是国联议价数据，国联议价只能打电话，不能输入任何内容
    protected boolean isQuote() {
        return mSource.getTrans_type() == 300 && "4" .equals(mSource.getCargo_src_type());
    }

    public LoginInfo getLoginInfo() {
        return mLoginInfo;
    }

    protected void bookFailedWhileAdvanceAvailable() {
        //自主交易摘牌失败，但是会员管理单位可垫付
    }

    abstract class DataHandler<Q> extends AnimCallBack<Q> {

        DataHandler(IProgress progress) {
            super(progress);
        }

        @Override
        public void onError(OkCall<Q> okCall, EasyError error) {
            if (error instanceof BusiError) {
                BusiError bi = (BusiError) error;
                switch (bi.getCommonResult().getErrorNo()) {
                    case 410://需要补充资料
                        getView().showAlertForUserInfoNotComplete(bi.getCommonResult().getErrorInfo(), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (AppTypeConfig.isTruck()) {
                                    if (LoginInfo.isChildAccount()) {
                                        if (LoginInfo.isCompany())
                                            getView().toActivity(TruckCompanyChildCertActivity.class, null, null);
                                        else
                                            getView().toActivity(TruckPersonalChildCertActivity.class, null, null);
                                    } else {
                                        if (LoginInfo.isCompany())
                                            getView().toActivity(TruckCompanyMainCertActivity.class, null, null);
                                        else
                                            //主账号不处理
                                            handleMessage("流程错误");
                                    }
//                                    getView().startActivity(TruckCompleteInfoActivity.class, null, 999, null);
                                } else if (AppTypeConfig.isShip()) {
                                    if (LoginInfo.isChildAccount()) {
                                        if (LoginInfo.isCompany())
                                            getView().toActivity(ShipCompanyChildCertActivity.class, null, null);
                                        else
                                            getView().toActivity(ShipPersonalChildCertActivity.class, null, null);
                                    } else {
                                        if (LoginInfo.isCompany())
                                            getView().toActivity(ShipCompanyMainCertActivity.class, null, null);
                                        else
                                            //主账号不处理
                                            handleMessage("流程错误");
                                    }
//                                    getView().startActivity(ShipCompleteInfoActivity.class, null, 999, null);
                                } else if (AppTypeConfig.isCargo()) {
                                    if (LoginInfo.isCompany()) {
                                        getView().toActivity(CargoCompanyCertActivity.class, null, null);
                                    } else {
                                        getView().toActivity(CargoPersonalCertActivity.class, null, null);
                                    }
                                } else {
                                    handleMessage("流程错误");
                                }
                            }
                        });
                        break;
                    case 500://会员管理单位可垫付保证金
                        bookFailedWhileAdvanceAvailable();//弹出对话框让用户选择
                        break;
                    case 502://e商贸通未开通，同时不能垫付
                    case 600://资料审核中，提示用户
                    case 700://会员无权限摘牌，提示用户
                        getView().showDialog(false, null, bi.getCommonResult().getErrorInfo(), "确定", null, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getView().finishWithOptions(null);
                            }
                        }, null);
                        break;
                    default://未知错误码，默认处理
                        getView().handleError((BaseError) error);
                        break;
                }
            } else if (error instanceof DataError && ((DataError) error).getCode() == ErrorState.NO_DATA) {
                DataError de = (DataError) error;
                try {
                    JSONObject o = new JSONObject(de.getResponse());
                    CommonResult cr = new CommonResult(o.getInt("errorNo"), o.getString("errorInfo"));
                    BusiError be = new BusiError(cr, de.getResponse());
                    onError(okCall, be);
                } catch (JSONException e) {
                    e.printStackTrace();
                    getView().handleError((BaseError) error);
                }
            } else {
                getView().handleError((BaseError) error);
            }
        }
    }
}
