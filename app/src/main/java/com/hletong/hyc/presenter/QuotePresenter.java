package com.hletong.hyc.presenter;

import android.content.DialogInterface;
import android.content.Intent;

import com.hletong.hyc.contract.QuoteContract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.Quote;
import com.hletong.hyc.ui.activity.CargoCompanyCompleteInfoActivity;
import com.hletong.hyc.ui.activity.CargoPersonCompleteInfoActivity;
import com.hletong.hyc.ui.activity.cargo.CargoCompanyCertActivity;
import com.hletong.hyc.ui.activity.cargo.CargoPersonalCertActivity;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.error.BusiError;
import com.hletong.mob.architect.error.DataError;
import com.hletong.mob.architect.error.ErrorState;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.model.repository.DataCallback;
import com.hletong.mob.architect.model.repository.SimpleCallback;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.requestvalue.ListRequestValue;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.okhttp.util.ParamUtil;

import java.util.List;

/**
 * Created by ddq on 2017/3/28.
 * 货方-议价
 */

public class QuotePresenter extends DirectQuotePresenter<QuoteContract.View> implements QuoteContract.Presenter {
    private String quoteUuid;
    private List<Quote> mList;
    private int bookRefType;
    private String cargoUnit;

    public QuotePresenter(QuoteContract.View view, String file, List<Quote> list, int bookRefType, String cargoUnit) {
        super(view, file);
        this.mList = list;
        this.bookRefType = bookRefType;
        this.cargoUnit = cargoUnit;
    }

    @Override
    public void start() {
        super.start();
        getView().initQuote(cargoUnit, bookRefType);
        refresh(mList);
    }

    @Override
    public void accept(String quoteUuid) {
        this.quoteUuid = quoteUuid;
        ItemRequestValue<CommonResult> requestValue = getRequestValue(quoteUuid, Constant.ACCEPT_QUOTE);

        if (requestValue == null) return;

        getDataRepository().loadItem(requestValue, new DataCallback<CommonResult>(getView()) {
            @Override
            public void onSuccess(CommonResult response) {
                getView().success(response.getErrorInfo());
                //通知外部更新数据
                getView().broadcast(new Intent("quote_state_changed"));
            }

            @Override
            public void onError(BaseError error) {
                if (error.isBusiError()) {
                    BusiError busiError = (BusiError) error;
                    int busiCode = busiError.getBusiCode();
                    if (busiCode == 500) {//会员管理单位垫付
                        getView().showAlert500(error.getMessage());
                    } else if (busiCode == 501) {//需要去开通E商贸通
                        getView().showAlert501(error.getMessage());
                    } else if (busiCode == 410) {
                        getView().showDialog(null, "尊敬的会员：请您签订入会协议书，并补充相关证件资料和账户信息", "去签订", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (LoginInfo.isCompany()) {
                                    getView().toActivity(CargoCompanyCertActivity.class, null, null);
                                } else {
                                    getView().toActivity(CargoPersonalCertActivity.class, null, null);
                                }
                            }
                        });
                    } else {
                        getView().handleError(error);
                    }
                } else {
                    getView().handleError(error);
                }
            }
        });
    }

    //会员信息是否完整
    private boolean isUserInfoComplete(int userInfo) {
        if (userInfo == 1)
            return true;
        else if (userInfo == 0) {
            //用户信息不全，需要补填信息
            getView().showAlertForUserInfoNotComplete("货主认证通过后才能找车运货，请填写您的真实信息", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    if (AppTypeConfig.isCargo()) {
                        LoginInfo loginInfo = LoginInfo.getLoginInfo();
                        if (loginInfo == null) {
                            handleMessage("数据获取出错");
                            return;
                        }
                        if (LoginInfo.isCompany()) {
                            getView().toActivity(CargoCompanyCompleteInfoActivity.class, null, 999, null);
                        } else {
                            getView().toActivity(CargoPersonCompleteInfoActivity.class, null, 999, null);
                        }
                    } else {
                        handleMessage("流程错误");
                    }
                }
            });
        } else {
            handleMessage("无法获取会员资料");
        }
        return false;
    }

    @Override
    public void acceptWithMU() {
        //用户选择了会员管理单位垫付
        ItemRequestValue<CommonResult> requestValue = new ItemRequestValue<CommonResult>(
                getView().hashCode(),
                Constant.getUrl(Constant.ACCEPT_QUOTE),
                new ParamsHelper()
                        .put("quoteUuid", quoteUuid)
                        .put("payMode", 1)//垫付操作
        ) {
        };

        getDataRepository().loadItem(requestValue, new SimpleCallback<CommonResult>(getView()) {
            @Override
            public void onSuccess(CommonResult response) {
                getView().success("已申请垫付");
                //通知外部更新数据
                getView().broadcast(new Intent("quote_state_changed"));
            }
        });
    }

    //拒绝车船会员的报价
    @Override
    public void refuse(String quoteUuid) {
        ItemRequestValue<CommonResult> requestValue = new ItemRequestValue<CommonResult>(
                getView().hashCode(),
                Constant.getUrl(Constant.CANCEL_QUOTE),
                new ParamsHelper().put("quoteUuid", quoteUuid)
        ) {
        };

        getDataRepository().loadItem(requestValue, new SimpleCallback<CommonResult>(getView()) {
            @Override
            public void onSuccess(CommonResult response) {
                getView().success(response.getErrorInfo());
                //通知外部更新数据
                getView().broadcast(new Intent("quote_state_changed"));
            }
        });
    }

    @Override
    public void refresh(List<Quote> list) {
        if (!ParamUtil.isEmpty(list)) {
            list.add(0, new Quote());//这是head
            getView().showList(list, true);
        } else {
            BaseError error = new DataError(ErrorState.NO_DATA);
            error.setId(ListRequestValue.REFRESH);
            getView().handleError(error);
        }
    }

    private ItemRequestValue<CommonResult> getRequestValue(String quoteUuid, String url) {
        if (quoteUuid == null) {
            handleMessage("参数出错");
            return null;
        }

        return new ItemRequestValue<CommonResult>(getView().hashCode(), Constant.getUrl(url), new ParamsHelper().put("quoteUuid", quoteUuid)) {
        };
    }
}
