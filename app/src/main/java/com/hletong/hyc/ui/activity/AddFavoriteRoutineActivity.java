package com.hletong.hyc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.FavoriteRoutineContract;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.presenter.FavoriteRoutinePresenter;
import com.hletong.mob.base.MvpActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/2/10.
 * 添加常跑路线
 */

public class AddFavoriteRoutineActivity extends MvpActivity<FavoriteRoutineContract.Presenter> implements FavoriteRoutineContract.View {
    @BindView(R.id.start_address)
    TextView start_address;
    @BindView(R.id.end_address)
    TextView end_address;

    @Override
    public int getLayoutId() {
        return R.layout.add_favorite_routine;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        setCustomTitle(R.string.add_favorite_routine);
    }

    @OnClick({R.id.start_address, R.id.end_address, R.id.submit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
                getPresenter().add((Address) start_address.getTag(), (Address) end_address.getTag());
                break;
        }
    }
    
    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        switch (requestCode) {
            case 100: {
                Address address = Address.getAddress(data);
                start_address.setTag(address);
                start_address.setText(address.buildAddress());
                break;
            }
            case 101: {
                Address address = Address.getAddress(data);
                end_address.setTag(address);
                end_address.setText(address.buildAddress());
                break;
            }
        }
    }

    @Override
    protected FavoriteRoutineContract.Presenter createPresenter() {
        return new FavoriteRoutinePresenter(this);
    }
}
