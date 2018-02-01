package com.hletong.hyc.util;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.ui.activity.AddressSelectorActivity;
import com.hletong.hyc.ui.fragment.TimeSelectorDialog;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.dialog.selector.BottomSelectorDialog;
import com.orhanobut.logger.Logger;

/**
 * Created by ddq on 2016/10/31.
 */

public class AddressTimeUtil implements BottomSelectorDialog.OnItemSelectedListener<TimeSelectorDialog.TimePicker> {
    private final int REQUEST_CODE_FOR_LOADING_LOCATION = 999;
    private final int REQUEST_CODE_FOR_UNLOADING_LOCATION = 998;

    private final int START_LOC = 1;
    private final int END_LOC = 2;
    private FragmentActivity context;
    private TimeSelectorDialog dpw;

    private Address startLoc;
    private Address endLoc;
    /**
     * 时间类型
     * 0 => 不限
     * 1 => 今天
     * 2 => 三天内
     * 3 => 一周内
     * 4 => 一月内
     */
    private TimeSelectorDialog.TimePicker timePicker;
    private DataChanged dataChanged;
    private ITransactionView mTransactionView;
    private View view;

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.startLoc: {
//                    showAddressSelector(v,R.string.departure_title);
                    Intent intent = new Intent(context, AddressSelectorActivity.class);
                    intent.putExtra("address_extra", 7);//能选择全国全省和全市
                    intent.putExtra("title", context.getString(R.string.select_start_address));
                    mTransactionView.toActivity(intent, REQUEST_CODE_FOR_LOADING_LOCATION, null);
                    break;
                }
                case R.id.endLoc: {
//                    showAddressSelector(v,R.string.destination_title);
                    Intent intent = new Intent(context, AddressSelectorActivity.class);
                    intent.putExtra("address_extra", 7);//能选择全国全省和全市
                    intent.putExtra("title", context.getString(R.string.select_destination_address));
                    mTransactionView.toActivity(intent, REQUEST_CODE_FOR_UNLOADING_LOCATION, null);
                    break;
                }
                case R.id.leave_dt: {
                    if (dpw == null) {
                        dpw = TimeSelectorDialog.getDialogInstance(context);
                        dpw.setOnItemSelected(AddressTimeUtil.this);
                    }
                    dpw.show();
                    break;
                }
            }
        }
    };

    public AddressTimeUtil(View view, FragmentActivity context, DataChanged dataChanged, ITransactionView transactionView) {
        this.view = view;
        this.context = context;
        this.dataChanged = dataChanged;
        this.mTransactionView = transactionView;
        view.findViewById(R.id.startLoc).setOnClickListener(listener);
        view.findViewById(R.id.endLoc).setOnClickListener(listener);
        view.findViewById(R.id.leave_dt).setOnClickListener(listener);

        startLoc = new Address();
        endLoc = new Address();
    }

    public Address getStartLoc() {
        return startLoc;
    }

    public Address getEndLoc() {
        return endLoc;
    }

    public String getDateType() {
        Logger.d("timePicker => " + timePicker);
        if (timePicker == null)
            return "";

        return timePicker.getValue();
    }

    @Override
    public void onItemSelected(TimeSelectorDialog.TimePicker item, int extra) {
        if (timePicker == null || !timePicker.getValue().equals(item.getValue())) {
            this.timePicker = item;

            TextView textView = (TextView) view.findViewById(R.id.tot);
            textView.setText(timePicker.getValue());
            dataChanged.timeChanged(timePicker.getValue());
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK)
            return;

        switch (requestCode) {
            case REQUEST_CODE_FOR_LOADING_LOCATION: {
                Address mAddress = Address.getAddress(data);
                updateStartAddress(mAddress);
                break;
            }
            case REQUEST_CODE_FOR_UNLOADING_LOCATION: {
                Address mAddress = Address.getAddress(data);
                updateEndAddress(mAddress);
                break;
            }
        }
    }

    //更新本地View，并通知外部列表刷新
    private void updateStartAddress(Address address) {
        if (!this.startLoc.isSameCounty(address)) {
            TextView textView = (TextView) view.findViewById(R.id.startLoc).findViewById(R.id.sl_text);
            textView.setText(address.getCityForDisplay());
            this.startLoc = address;
            dataChanged.addressChanged(START_LOC, address);
        }
    }

    //更新本地View，并通知外部列表刷新
    private void updateEndAddress(Address address) {
        if (!this.endLoc.isSameCounty(address)) {
            TextView textView = (TextView) view.findViewById(R.id.endLoc).findViewById(R.id.el_text);
            textView.setText(address.getCityForDisplay());
            this.endLoc = address;
            dataChanged.addressChanged(END_LOC, address);
        }
    }

    public interface DataChanged {
        //地址发生了变化
        void addressChanged(int type, Address address);

        //时间发生了变化
        void timeChanged(String time);
    }
}
