package com.hletong.hyc.ui.dialog;

import android.content.Context;
import android.os.Bundle;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.contract.ShipContract;
import com.hletong.hyc.model.Ship;
import com.hletong.hyc.model.TransporterData;
import com.hletong.hyc.presenter.ShipPresenter;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.requestvalue.ListRequestValue;
import com.hletong.mob.util.ParamsHelper;

/**
 * Created by ddq on 2017/1/24.
 */

public class ShipSelectorDialogFragment extends TransporterSelectorDialog<Ship> {
    private ShipContract.Presenter mPresenter;

    public ShipSelectorDialogFragment(Context context) {
        super(context);
        mPresenter = new ShipPresenter(this);
    }

    public static ShipSelectorDialogFragment getInstance(String title, String url, String cargoUuid, Context context) {
        Bundle bundle = new Bundle();
        bundle.putString("dialog_title", title);//加载数据出错时，对话框使用的标题
        bundle.putString("url", url);
        if (cargoUuid != null)
            bundle.putString("cargoUuid", cargoUuid);
        ShipSelectorDialogFragment fragment = new ShipSelectorDialogFragment(context);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected String getTitle() {
        return "选择船舶";
    }

    @Override
    protected void onLoad() {
        final String url = getArguments().getString("url");
        if (Constant.SHIPS.equals(url)) {//加载船舶信息-运力预报
            mPresenter.loadList(new ListRequestValue<Ship>(hashCode(), Constant.getUrl(Constant.SHIPS), null){}, true);
        } else if (Constant.TRANSPORTER_ZP.equals(url)) {
            //加载船舶信息-摘牌页面
            mPresenter.loadItem(
                    new ItemRequestValue<TransporterData<Ship>>(
                            hashCode(),
                            Constant.getUrl(Constant.TRANSPORTER_ZP),
                            new ParamsHelper().put("transportType", BuildConfig.carrier_type).put("cargoUuid", getArguments().getString("cargoUuid"))){});
        }
    }
}
