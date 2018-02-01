package com.hletong.hyc.ui.dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.contract.TruckContract;
import com.hletong.hyc.model.TransporterData;
import com.hletong.hyc.model.Truck;
import com.hletong.hyc.presenter.TruckPresenter;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.requestvalue.ListRequestValue;
import com.hletong.mob.util.ParamsHelper;

/**
 * Created by ddq on 2016/11/4.
 */

public class TrucksSelectorDialogFragment extends TransporterSelectorDialog<Truck> {
    private TruckContract.Presenter mPresenter;

    public TrucksSelectorDialogFragment(Context context) {
        super(context);
        mPresenter = new TruckPresenter(this);
    }

    public static TrucksSelectorDialogFragment getInstance(String title, String url, String cargoUuid, @NonNull Context context) {
        TrucksSelectorDialogFragment mFragment = new TrucksSelectorDialogFragment(context);
        Bundle bundle = new Bundle();
        bundle.putString("dialog_title", title);//加载数据出错时，对话框使用的标题
        bundle.putString("url", url);
        if (cargoUuid != null) {
            bundle.putString("cargoUuid", cargoUuid);
        }
        mFragment.setArguments(bundle);
        return mFragment;
    }

    @Override
    protected String getTitle() {
        return "选择车辆";
    }

    @Override
    protected void onLoad() {
        final String url = getArguments().getString("url");
        if (Constant.TRUCKS.equals(url)) {//加载车辆信息-运力预报
            mPresenter.loadList(new ListRequestValue<Truck>(hashCode(), Constant.getUrl(Constant.TRUCKS), null){}, true);
        } else if (Constant.TRANSPORTER_ZP.equals(url)) {
            //加载车辆信息-摘牌页面
            mPresenter.loadItem(
                    new ItemRequestValue<TransporterData<Truck>>(
                            hashCode(),
                            Constant.getUrl(Constant.TRANSPORTER_ZP),
                            new ParamsHelper().put("transportType", BuildConfig.carrier_type).put("cargoUuid", getArguments().getString("cargoUuid"))){});
        }
    }
}
