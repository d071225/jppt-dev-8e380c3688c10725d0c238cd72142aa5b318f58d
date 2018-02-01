package com.hletong.hyc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.Address;
import com.hletong.mob.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/4/13.
 */

public class AddressChooserForRoutePlanActivity extends BaseActivity {
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.start_address)
    TextView startAddress;
    @BindView(R.id.end_address)
    TextView endAddress;

    @Override
    public int getLayoutId() {
        return R.layout.add_favorite_routine;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        submit.setText("确认");
        setCustomTitle(R.string.route_plan);
    }

    @OnClick({R.id.start_address, R.id.end_address, R.id.submit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.start_address: {
                Bundle bundle = new Bundle();
                bundle.putString("title", getString(R.string.select_start_address));
                bundle.putInt("address_extra", 6);//能选择全省和全市，没有全国
                toActivity(AddressSelectorActivity.class, bundle, 100, null);
                break;
            }
            case R.id.end_address: {
                Bundle bundle = new Bundle();
                bundle.putInt("address_extra", 6);//能选择全省和全市，没有全国
                bundle.putString("title", getString(R.string.select_destination_address));
                toActivity(AddressSelectorActivity.class, bundle, 101, null);
                break;
            }
            case R.id.submit:
                if (startAddress.getTag() == null || endAddress.getTag() == null) {
                    showMessage("出发地、目的地均不能为空");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putParcelable(PathPlanActivity.START_ADDRESS, (Address) startAddress.getTag());
                bundle.putParcelable(PathPlanActivity.END_ADDRESS, (Address) endAddress.getTag());
                toActivity(PathPlanActivity.class, bundle, null);
                break;
        }
    }

    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        switch (requestCode) {
            case 100: {
                Address address = Address.getAddress(data);
                startAddress.setTag(address);
                startAddress.setText(address.buildAddress());
                break;
            }
            case 101: {
                Address address = Address.getAddress(data);
                endAddress.setTag(address);
                endAddress.setText(address.buildAddress());
                break;
            }
        }
    }
}
