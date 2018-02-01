package com.hletong.hyc.presenter;

import android.content.DialogInterface;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.TransportContract;
import com.hletong.hyc.model.ForecastTransporterInfo;
import com.hletong.hyc.model.validate.Transport;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkRequest;

/**
 * Created by ddq on 2017/1/4.
 */
public class YunLiPresenter extends BasePresenter<TransportContract.View> implements TransportContract.Presenter {
    private ForecastTransporterInfo mData;

    public YunLiPresenter(TransportContract.View view, ForecastTransporterInfo data) {
        super(view);
        this.mData = data;
    }

    @Override
    public void start() {
        if (mData == null) {
            getView().showTitle("发布运力预报");
            getView().setAddressClickListener();
            getView().prefetchTransporter();
        } else {
            getView().showTitle("运力信息");
            getView().showLoad(mData.getCapacityWithUnit());
            getView().showPlate(mData.getCarrier_no());
            if (TextUtils.isEmpty(mData.getContact_tel())) {
                getView().hideSpare();
            } else {
                getView().showSpare(mData.getContact_tel());
            }
            getView().showAddress(mData.getStartAddress(), mData.getStopAddress());
            getView().showAvailableTime(mData.getAvl_carrier_dt(), mData.getLeave_dt());
            getView().inflateMenu(R.menu.revoke, new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    cancel();
                    return true;
                }
            });
        }
    }

    //发布运力预报
    @Override
    public void submit(Transport transport) {
        //参数校验
        final OkRequest request = transport.validate(getView());
        if (request != null) {
            getView().showDialog("提示", "确定要发布运力预报？", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    new ExecutorCall<CommonResult>(request).enqueue(new SubmitForwardResultCallback(getView(), getView(), getView()));
                }
            });
        }
    }

    //撤销运力预报
    @Override
    public void cancel() {
        if (Validator.isNotNull(mData, getView(), "参数出错")
                && Validator.isNotNull(mData.getForecast_uuid(), getView(), "参数出错")) {
            getView().showDialog("提示", "确定要撤销此运力预报?", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    OkRequest request = EasyOkHttp
                            .get(Constant.CANCEL_TRANSPORT_SCHEDULE)
                            .param("forecastUuid", mData.getForecast_uuid())
                            .build();

                    new ExecutorCall<CommonResult>(request).enqueue(new SubmitForwardResultCallback(getView(), getView(), getView()));
                }
            });
        }
    }
}
