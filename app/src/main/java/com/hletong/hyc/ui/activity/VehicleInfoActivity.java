package com.hletong.hyc.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.ETCContract;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.EtcApplyInfo;
import com.hletong.hyc.model.VehicleInfo;
import com.hletong.hyc.model.validate.EtcTransporterInfoPart1;
import com.hletong.hyc.presenter.ETCPresenter;
import com.hletong.hyc.ui.dialog.DictItemDialog;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.hyc.util.SimpleTextWatcher;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.dialog.selector.BottomSelectorDialog;
import com.hletong.mob.dialog.selector.SelectorPrefetchListener;
import com.hletong.mob.net.OkHttpUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class VehicleInfoActivity extends BaseActivity implements BottomSelectorDialog.OnItemSelectedListener<DictionaryItem>, ETCContract.View {

    @BindView(R.id.et_cardId)
    EditText et_carId;
    @BindView(R.id.tv_carId)
    TextView tv_carId;
    @BindView(R.id.hint)
    TextView hint;
    @BindView(R.id.plate_container)
    View plateContainer;
    @BindView(R.id.et_carOwner)
    EditText vehicle_owner;
    @BindView(R.id.et_carCode)
    EditText vehicle_code;
    @BindView(R.id.tv_carStyle)
    TextView vehicle_style;
    @BindView(R.id.et_carLength)
    EditText vehicle_length;
    @BindView(R.id.tv_carColor)
    TextView vehicle_color;
    @BindView(R.id.et_carSeats)
    EditText seats;
    @BindView(R.id.et_vehicleMass)
    EditText etVehicleMass;
    @BindView(R.id.et_loadedVehicleQuality)
    EditText etLoadedVehicleQuality;
    @BindView(R.id.tv_address)
    TextView address;
    @BindView(R.id.et_detailAddress)
    EditText details;

    @BindView(R.id.et_vehicleType)
    EditText etVehicleType;
    @BindView(R.id.et_vehicleModel)
    EditText etVehicleModel;
    @BindView(R.id.et_engineNum)
    EditText etEngineNum;
    @BindView(R.id.et_maintenanceMass)
    EditText etMaintenanceMass;
    @BindView(R.id.et_out_l)
    EditText etOutL;
    @BindView(R.id.et_out_w)
    EditText etOutW;
    @BindView(R.id.et_out_h)
    EditText etOutH;
    @BindView(R.id.et_permittedTowWeight)
    EditText etPermittedTowWeight;

    private final int SELECT_ADDRESS_CODE = 25;
    private final int REQUEST_SUBMIT_RESULT = 20;
    private DictItemDialog mDictItemFragment;
    private ETCContract.Presenter mPresenter;

    @Override
    public int getLayoutId() {
        return R.layout.activity_vehicle_info;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        setCustomTitle(R.string.new_vehicle_information);
        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exit();//退出的时候给个提示框
            }
        });

        mDictItemFragment = new DictItemDialog(this);
        mDictItemFragment.setOnItemSelected(new SelectorPrefetchListener<DictionaryItem>() {
            @Override
            public void onItemSelected(DictionaryItem item, int extra) {
                if (extra == R.id.tv_carStyle) {
                    boolean hide = item.getId().equals("01") || item.getId().equals("02")
                            || item.getId().equals("03") || item.getId().equals("04");

                    ((View) etPermittedTowWeight.getParent()).setVisibility(hide ? View.GONE : View.VISIBLE);
                    etPermittedTowWeight.setText(hide ? "-1" : "");
                    ((View) etVehicleMass.getParent()).setVisibility(hide ? View.GONE : View.VISIBLE);
                    etVehicleMass.setText(hide ? "-1" : "");
                }
                TextView view = findViewById(extra);
                view.setText(item.getText());
                view.setTag(item);
            }

            @Override
            public boolean dataRetrieved(List<DictionaryItem> data, int extra) {
                if (extra == R.id.tv_carStyle)
                    mPresenter.chooseTruckType(data);//车辆类型
                else
                    super.dataRetrieved(data, extra);//车辆颜色
                return true;
            }
        });

        VehicleInfo vehicleInfo = getParcelable("vehicleInfo");
        mPresenter = new ETCPresenter(this, vehicleInfo, getIntent().getStringExtra("CardType"));
        mPresenter.start();

        //预加载车辆类型和车辆颜色
        //取消预加载车辆类型
        // DictionaryItem d1 = new DictionaryItem(String.valueOf(R.id.tv_carStyle), "etc_vehicle_type");
        //DictionaryItem d2 = new DictionaryItem(String.valueOf(R.id.tv_carColor), "plate_color");
        //ArrayList<DictionaryItem> dis = new ArrayList<>();
        //  dis.add(d1);
        //dis.add(d2);
        //mDictItemFragment.prefetch(dis, null);
        if (vehicleInfo != null) {
            mDictItemFragment.getDictItemById(vehicleInfo.getLicensePlateId(), "plate_color", new DictItemDialog.OnGetItemByIdListener() {
                @Override
                public void onFindItem(DictionaryItem item) {
                    TextView view = findViewById(R.id.tv_carColor);
                    view.setText(item.getText());
                    view.setTag(item);
                }
            });
        }
    }

    @OnClick({R.id.btn_next, R.id.tv_carColor})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                mPresenter.vehicleInfoValidate(getVehicleInfo());//检查参数
                break;
            case R.id.tv_carColor:
                mDictItemFragment.showDict("plate_color", "车牌颜色", v.getId());
                break;
            case R.id.tv_carStyle:
                mDictItemFragment.showDict("etc_vehicle_type", "车辆类型", v.getId());
                break;
            case R.id.tv_address:
                Bundle bundle = new Bundle();
                bundle.putString("title", getString(R.string.select_address));
                toActivity(AddressSelectorActivity.class, bundle, SELECT_ADDRESS_CODE, null);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        exit();
    }

    private void exit() {
        DialogFactory.showAlertWithNegativeButton(this, R.string.exit, R.string.etc_hint_exit, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(DictionaryItem item, int extra) {
        TextView view = findViewById(extra);
        view.setText(item.getText());
        view.setTag(item);
    }

    public EtcTransporterInfoPart1 getVehicleInfo() {
        Address addr = (Address) this.address.getTag();

        if (addr != null)
            addr.setDetails(details.getText().toString().trim());

        EtcTransporterInfoPart1 vehicleInfo = new EtcTransporterInfoPart1(
                addr,
                (DictionaryItem) vehicle_style.getTag(),
                (DictionaryItem) vehicle_color.getTag(),
                vehicle_length.getText().toString(),
                vehicle_owner.getText().toString().trim(),
                vehicle_code.getText().toString().trim(),
                seats.getText().toString(),
                etVehicleMass.getText().toString(), //核定载质量--
                etLoadedVehicleQuality.getText().toString(),//总质量--
                getIntent().getStringExtra("CardType"),

                etVehicleType.getText().toString(),
                etVehicleModel.getText().toString(),

                etEngineNum.getText().toString(),

                etMaintenanceMass.getText().toString(),

                etOutL.getText().toString(),

                etOutW.getText().toString(),

                etOutH.getText().toString(),
                etPermittedTowWeight.getText().toString()//准牵引总质量--
        );

        if (plateContainer.getVisibility() == View.VISIBLE) {//手动填写的数据
            vehicleInfo.setPlate(et_carId.getText().toString().trim());
        } else {
            vehicleInfo.setPlate(tv_carId.getText().toString());//自动填充的数据
        }

        return vehicleInfo;
    }

//    @Override
//    public void showDicts(List<Dictionary.Type> list, String fieldName) {
//        for (Dictionary.Type type : list) {
//            if (mVehicleInfo.getTruckType().equals(type.getId())) {
//                vehicle_style.setText(type.getText());
//            }
//        }
//    }

    //新增车辆
    @Override
    public void initAddMode() {
        vehicle_style.setOnClickListener(this);
        address.setOnClickListener(this);
        et_carId.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                OkHttpUtil.cancel(hashCode());//取消当前请求（如果有的话）
                mPresenter.checkPlate(s.toString().trim());//检查车牌号是否可用
            }
        });
    }

    //追加车辆信息
    @Override
    public void initAppendMode(VehicleInfo vehicleInfo) {
        setCustomTitle("车辆信息");

        hint.setVisibility(View.GONE);
        plateContainer.setVisibility(View.GONE);

        tv_carId.setVisibility(View.VISIBLE);
        tv_carId.setText(vehicleInfo.getPlate());

        disableEditText(vehicle_owner);
        vehicle_owner.setText(vehicleInfo.getTruckName());

        if (!TextUtils.isEmpty(vehicleInfo.getIdentify())) {
            disableEditText(vehicle_code);
            vehicle_code.setText(vehicleInfo.getIdentify());
        }

        if (!TextUtils.isEmpty(vehicleInfo.getMaxLength())) {
            // disableEditText(vehicle_length);
            vehicle_length.setText(vehicleInfo.getMaxLength());
        }

        double vehicleMass = vehicleInfo.getVehicleMass();
        if (vehicleMass != 0) {
            // disableEditText(etVehicleMass);
            etVehicleMass.setText(String.valueOf(vehicleMass));
        }

        double loadedVehicleQuality = vehicleInfo.getLoadedVehicleQuality();
        if (vehicleMass != 0) {
            //  disableEditText(etLoadedVehicleQuality);
            etLoadedVehicleQuality.setText(String.valueOf(loadedVehicleQuality));
        }


        // disableEditText(details);
        vehicle_style.setOnClickListener(this);
        // vehicle_style.setCompoundDrawables(null, null, null, null);

        address.setText(vehicleInfo.getAddressForShow());//地址
        // address.setCompoundDrawables(null, null, null, null);
        address.setOnClickListener(this);
        details.setText(vehicleInfo.getMemberAddress());
        address.setTag(vehicleInfo.getAddress());
    }

    @Override
    public void initTruckType(DictionaryItem truckType) {
        vehicle_style.setText(truckType.getText());//车辆类型
        vehicle_style.setTag(truckType);
    }

    @Override
    public void success(EtcApplyInfo transporterInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("vehicleInfo", transporterInfo);
        toActivity(BasicInfoActivity.class, bundle, REQUEST_SUBMIT_RESULT, null);
    }

    private void disableEditText(EditText editText) {
        editText.setFocusable(false);
        editText.setFocusableInTouchMode(false);
    }

    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        switch (requestCode) {
            case SELECT_ADDRESS_CODE: {
                Address addr = Address.getAddress(data);
                this.address.setText(addr.buildAddress());
                this.address.setTag(addr);
                break;
            }
            case REQUEST_SUBMIT_RESULT: {
                finish();
                break;
            }
        }
    }
}
