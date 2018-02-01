package com.hletong.hyc.presenter;

import android.content.DialogInterface;

import com.hletong.hyc.contract.MainContract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.http.parse.JpptParse;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.ui.activity.cargo.CargoCompanyCertActivity;
import com.hletong.hyc.ui.activity.cargo.CargoPersonalCertActivity;
import com.hletong.hyc.ui.activity.ship.ShipCompanyChildCertActivity;
import com.hletong.hyc.ui.activity.ship.ShipCompanyMainCertActivity;
import com.hletong.hyc.ui.activity.ship.ShipPersonalChildCertActivity;
import com.hletong.hyc.ui.activity.truck.TruckCompanyChildCertActivity;
import com.hletong.hyc.ui.activity.truck.TruckCompanyMainCertActivity;
import com.hletong.hyc.ui.activity.truck.TruckPersonalChildCertActivity;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.error.BusiError;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.http.AnimCallBack;
import com.hletong.mob.util.StringUtil;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

/**首页权限检测，版本更新
 * Created by dongdaqing on 2017/7/31.
 */
public class MainPresenter extends UpgradePresenter<MainContract.View> implements MainContract.Presenter {
    private boolean checkUpdate;

    public MainPresenter(MainContract.View v) {
        super(v);
        checkUpdate = true;
    }

    @Override
    public void start() {
        //如果用户未登录，直接检查版本信息
        //如果用户已登录，先检查用户权限
        if (!LoginInfo.hasLogin())
            checkVersionInfo();
        else
            checkAuthority();
    }

    @Override
    public void checkAuthority() {
        //检查用户权限
        OkRequest request = EasyOkHttp.get(Constant.CHECK_USER_AUTHORITY).extra(JpptParse.SESSION_NOT_CHECKED, true).build();
        new ExecutorCall<CommonResult>(request).enqueue(new AnimCallBack<CommonResult>(null) {
            @Override
            public void onError(OkCall<CommonResult> okCall, EasyError error) {
                if (error instanceof BusiError) {
                    BusiError busiError = (BusiError) error;
                    if (busiError.getBusiCode() != -1) {
                        getView().handleError(busiError);
                        return;
                    }
                    String tips;
                    String title = null;
                    String errorInfo = busiError.getCommonResult().getErrorInfo();
                    if (!StringUtil.isTrimBlank(errorInfo)) {
                        String[] data = errorInfo.split("\\|");
                        if (data.length == 2) {
                            title = data[0];
                            tips = data[1];
                        } else {
                            tips = errorInfo;
                        }
                    } else {
                        tips = "尊敬的会员：请您签订入会协议书，并补充相关证件资料和用户信息";
                    }
                    getView().showDialog(false, title, tips, "去签订", "退出登录", new DialogInterface.OnClickListener() {
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
                            } else if (AppTypeConfig.isCargo()) {
                                if (LoginInfo.isCompany()) {
                                    getView().toActivity(CargoCompanyCertActivity.class, null, null);
                                } else {
                                    getView().toActivity(CargoPersonalCertActivity.class, null, null);
                                }
                            }
                        }
                    }, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            new ExecutorCall<CommonResult>(EasyOkHttp.get(Constant.LOGOUT).extra(JpptParse.SESSION_NOT_CHECKED, true).build()).enqueue(new AnimCallBack<CommonResult>(getView()) {
                                @Override
                                public void onError(OkCall<CommonResult> okCall, EasyError error) {
                                    getView().logout();
                                }

                                @Override
                                public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                                    getView().logout();
                                }
                            });
                        }
                    });
                } else {
                    //检查失败，不管了，直接检查更新
                    checkVersionInfo();
                }
            }

            @Override
            public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                //权限ok，检查更新
                checkVersionInfo();
            }
        });
    }

    @Override
    public void checkVersionInfo() {
        if (checkUpdate) {
            super.checkVersionInfo();
            checkUpdate = false;
        }
    }
}
