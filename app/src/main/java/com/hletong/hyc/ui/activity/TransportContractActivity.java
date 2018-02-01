package com.hletong.hyc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.google.gson.internal.LinkedTreeMap;
import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.Contract;
import com.hletong.hyc.model.ContractCarrierInfo;
import com.hletong.hyc.model.ContractItemForDetails;
import com.hletong.hyc.model.Order;
import com.hletong.hyc.model.validate.ContractInfo;
import com.hletong.hyc.presenter.TransporterContractPresenter;
import com.hletong.hyc.ui.dialog.HistoryCarrierSelectorDialog;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.Condition;
import com.hletong.hyc.util.ContractSpan;
import com.hletong.hyc.util.DatePickerUtil;
import com.hletong.hyc.util.SimpleTextWatcher;
import com.hletong.hyc.util.TimePickerUtil;
import com.hletong.mob.dialog.selector.SelectorPrefetchListener;
import com.hletong.mob.util.NumberUtil;
import com.hletong.mob.util.SimpleDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ddq on 2017/2/24.
 * 车船方合同
 */

public class TransportContractActivity extends ContractActivity<Contract.TransporterPresenter> implements Contract.TransporterView, DatePickerUtil.OnDateSetListener, TimePickerUtil.OnTimeSetListener {
    private DatePickerUtil dpu;//日期选择
    private TimePickerUtil tpu;//时间选择
    //用于处理承运信息的展示
    private ContractInput[] mContractInputs;
    //历史提货人选择器
    private HistoryCarrierSelectorDialog carrierSelector;

    @BindView(R.id.trans_password)
    CommonInputView trans_password;
    @BindView(R.id.trans_password_confirm)
    CommonInputView trans_psw_confirm;
    @BindView(R.id.password_title)
    TextView pswTitle;
    @BindView(R.id.chk)
    CheckBox chk;
    @BindView(R.id.wrtr_hint)
    TextView hintView;

    private TextWatcher mWatcher;

    private SelectorPrefetchListener<ContractCarrierInfo> mPrefetchListener = new SelectorPrefetchListener<ContractCarrierInfo>() {
        @Override
        public void onItemSelected(ContractCarrierInfo item, int extra) {
            mContractInputs[extra].carrierSelected(item);
        }
    };

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mWatcher = new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (submitView.getVisibility() == View.VISIBLE)
                    submitView.setEnabled(check());
            }
        };
    }

    private boolean check() {
        boolean r = true;
        for (int i = 0; i < mContractInputs.length; i++) {
            r &= mContractInputs[i].isOk();
        }

        if (!r)
            return false;

        return trans_password.inputHaveValue() && trans_psw_confirm.inputHaveValue();
    }

    @Override
    public void showContractArea() {
        //阅读货物运输三方协议
        chk.setVisibility(View.VISIBLE);
        ContractSpan.setContractHint(R.string.hint_transport_contract, chk).setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().viewContract();
            }
        });

        chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                submitView.setEnabled(isChecked);
            }
        });
    }

    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        //用户在三方界面点击了"阅读并同意按钮"
        if (requestCode == 101)
            chk.setChecked(true);
    }

    @Override
    public void showContractInfo(List<Order> list, int billingType,
                                 LinkedTreeMap<String, List<ContractCarrierInfo>> map, String unit) {
        mContractInputs = new ContractInput[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ContractInput contractInput = newContractInput(billingType);
            Order order = list.get(i);
            contractInput.addViewBefore(i, map.get(order.getCarrierNo()), order, unit);
            mContractInputs[i] = contractInput;
        }

        //预加载提货人信息
        for (ContractInput contractInput : mContractInputs) {
            contractInput.prefetch();
        }
    }

    @Override
    public void showSignedContractInfo(List<ContractItemForDetails.Order> list, int billingType, String unit) {
        mContractInputs = new ContractInput[list.size()];
        for (int i = 0; i < list.size(); i++) {
            ContractInput contractInput = newContractInput(billingType);
            contractInput.addViewBefore(i, list.get(i), unit);
            mContractInputs[i] = contractInput;
        }
    }

    @Override
    public void showProtocolTransportHint(CharSequence hint) {
        hintView.setVisibility(View.VISIBLE);
        hintView.setText(hint);
    }

    private ContractInput newContractInput(int billingType) {
        if (billingType == 1) {
            return new Platform();
        }
        return new ContractInput();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.btn_sign: {
                //提交合同
                if (mContractInputs == null) {
                    showMessage("信息不完整");
                    return;
                }
                List<ContractInfo.PickerInfo> infos = new ArrayList<>();
                for (ContractInput mContractInput : mContractInputs) {
                    infos.add(mContractInput.getPickerInfo());
                }

                getPresenter().submitContract(new ContractInfo(
                        infos,
                        trans_password.getInputValue(),
                        trans_psw_confirm.getInputValue()
                ));
                break;
            }
            case R.id.pre_load_time: {//选择预装货时间
                if (dpu == null) {
                    Calendar calendar = Calendar.getInstance();
                    dpu = new DatePickerUtil(this, this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    dpu.setMinimalDate(calendar);
                }
                dpu.showDatePicker(getFragmentManager(), (Integer) v.getTag());
                break;
            }
        }
    }

    @Override
    void toggleInputArea(boolean visible) {
        pswTitle.setVisibility(visible ? View.VISIBLE : View.GONE);
        trans_password.setVisibility(visible ? View.VISIBLE : View.GONE);
        trans_psw_confirm.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    @Override
    void inflateInputArea() {
        ViewStub part_transport = (ViewStub) findViewById(R.id.part_transport);
        part_transport.inflate();
        ButterKnife.bind(this);
        trans_password.getInput().addTextChangedListener(mWatcher);
        trans_psw_confirm.getInput().addTextChangedListener(mWatcher);
    }

    //日期选择完毕
    @Override
    public void onDateSet(int year, int monthOfYear, int dayOfMonth, int tag) {
        if (mContractInputs == null || tag >= mContractInputs.length) {
            return;
        }

        SimpleDate sd = mContractInputs[tag].getSimpleDate();
        sd.setDate(year, monthOfYear, dayOfMonth);
        if (tpu == null) {
            tpu = new TimePickerUtil(this, this);
        }
        tpu.show(getFragmentManager(), tag);
    }

    //时间选择完毕
    @Override
    public void onTimeSet(int hourOfDay, int minute, int second, int tag) {
        SimpleDate sd = mContractInputs[tag].getSimpleDate();
        sd.setTime(hourOfDay, minute, second);
        mContractInputs[tag].timeChanged();
        //检查所有项是否都填写了
        submitView.setEnabled(check());
    }

    @Override
    protected Contract.TransporterPresenter createPresenter() {
        return new TransporterContractPresenter(
                this,
                getIntent().getStringExtra("contractUuid"),
                getIntent().getStringExtra("bookUuid"));
    }

    private class ContractInput implements Condition {
        private CommonInputView title;
        private CommonInputView plate;
        private CommonInputView cargoDesc;
        private CommonInputView transportUnitPrice;
        private CommonInputView fee;
        private CommonInputView picker;
        private CommonInputView id;
        private CommonInputView pickTime;
        private int mIndex;
        private List<ContractCarrierInfo> mCarrierInfos;
        private View root;

        private SimpleDate mSimpleDate;//选择的预装货时间

        ContractInfo.PickerInfo getPickerInfo() {
            return new ContractInfo.PickerInfo(picker.getInputValue(), id.getInputValue(), mSimpleDate);
        }

        SimpleDate getSimpleDate() {
            if (mSimpleDate == null)
                mSimpleDate = new SimpleDate();
            return mSimpleDate;
        }

        void timeChanged() {
            pickTime.setText(mSimpleDate.dateString(true, ".") + " " + mSimpleDate.timeString(true, ":"));
        }

        View getBlockRootView() {
            return root;
        }

        /**
         * @param index
         * @param input 界面初始化是做什么用的，是签合同还是查看合同，true为签合同
         */
        protected void init(int index, boolean input) {
            this.mIndex = index;
            root = LayoutInflater.from(getContext()).inflate(R.layout.activity_sign_contract_part_transport_extra, getRootLayout(), false);
            //这个View的插入位置：contractView的后面，顺序排列，所以就是 contractView的Position + 1 + index
            getRootLayout().addView(root, getIndexForChild(getContractView()) + 1 + index, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            title = (CommonInputView) root.findViewById(R.id.title);
            plate = (CommonInputView) root.findViewById(R.id.plate);
            cargoDesc = (CommonInputView) root.findViewById(R.id.desc);
            transportUnitPrice = (CommonInputView) root.findViewById(R.id.unit_price);
            fee = (CommonInputView) root.findViewById(R.id.fee);
            picker = (CommonInputView) root.findViewById(R.id.picker);
            picker.getInput().addTextChangedListener(mWatcher);
            id = (CommonInputView) root.findViewById(R.id.id_no);
            id.getInput().addTextChangedListener(mWatcher);
            pickTime = (CommonInputView) root.findViewById(R.id.pre_load_time);

            if (!input) {
                picker.setMode(CommonInputView.VIEW);
                picker.getSuffix().setVisibility(View.GONE);
                id.setMode(CommonInputView.VIEW);
                pickTime.setMode(CommonInputView.VIEW);
            }

            title.setLabel("1.4." + (index + 1));
        }

        void addViewBefore(int index, List<ContractCarrierInfo> list, Order order, String unit) {
            init(index, true);
            mCarrierInfos = list;

            plate.setLabel(BuildConfig.carrier_name);
            plate.setText(order.getCarrierNo());
            cargoDesc.setText(order.getCargoDesc(unit));
            transportUnitPrice.setSuffixText("元/" + unit);
            transportUnitPrice.setText(NumberUtil.format2f(order.getBookUnitAmt()));
            fee.setText(NumberUtil.format2f(order.getBookAmt()) + " 元");

            //下面的是需要用户填写的
            pickTime.setTag(mIndex);//共享选择器，后面调用选择器的时候，传入这个index便于区分
            pickTime.setOnClickListener(getListener());

            picker.getSuffix().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    carrierSelector.show(mCarrierInfos, mIndex);
                }
            });
        }

        void addViewBefore(int index, ContractItemForDetails.Order order, String unit) {
            init(index, false);

            plate.setLabel(BuildConfig.carrier_name);
            plate.setText(order.getCarrier_no());
            cargoDesc.setText(order.getCargoDesc(unit));
            transportUnitPrice.setSuffixText("元/" + unit);
            transportUnitPrice.setText(order.getUnit_amt());
            fee.setText(order.getTransportFee(true));
            pickTime.setText(order.getPlanLoadingDtTm());
            id.setText(order.getTransporterID());
            picker.setText(order.getTransporterName());

            title.getSuffix().setText(null);
        }

        void carrierSelected(ContractCarrierInfo carrierInfo) {
            picker.setText(carrierInfo.getContact_name());
            id.setText(carrierInfo.getIdentity_no());
        }

        //预加载提货人信息
        public void prefetch() {
            if (carrierSelector == null) {
                carrierSelector = new HistoryCarrierSelectorDialog(TransportContractActivity.this);
                carrierSelector.setOnItemSelected(mPrefetchListener);
            }
            carrierSelector.prefetch(mIndex, mCarrierInfos);
        }

        @Override
        public boolean isOk() {
            return picker.inputHaveValue() && id.inputHaveValue() && mSimpleDate != null;
        }
    }

    private int getIndexForChild(View child) {
        for (int i = 0; i < getRootLayout().getChildCount(); i++) {
            if (child == getRootLayout().getChildAt(i))
                return i;
        }
        return -1;
    }

    //平台开票合同
    private class Platform extends ContractInput {
        private CommonInputView otherPrice;
        private CommonInputView transportPrice;
        private CommonInputView totalPrice;

        @Override
        protected void init(int index, boolean input) {
            super.init(index, input);
            otherPrice = (CommonInputView) getBlockRootView().findViewById(R.id.other_price);
            transportPrice = (CommonInputView) getBlockRootView().findViewById(R.id.unit_price);
            totalPrice = (CommonInputView) getBlockRootView().findViewById(R.id.fee);

            otherPrice.setVisibility(View.VISIBLE);
            transportPrice.setLabel("运输收入");
            totalPrice.setLabel("总收入");
        }

        @Override
        void addViewBefore(int index, List<ContractCarrierInfo> list, Order order, String unit) {
            super.addViewBefore(index, list, order, unit);
            otherPrice.setText(NumberUtil.format3f(order.getOtherUnitAmt()));
            otherPrice.setSuffixText("元/" + unit);

            transportPrice.setText(NumberUtil.format3f(order.getTransportUnitPrice()));
            transportPrice.setSuffixText("元/" + unit);

            totalPrice.setText(NumberUtil.format2f(order.getBookAmt()));
            totalPrice.setSuffixText("元");
        }

        @Override
        void addViewBefore(int index, ContractItemForDetails.Order order, String unit) {
            super.addViewBefore(index, order, unit);
            otherPrice.setText(NumberUtil.format3f(order.getOther_unit_amt()));
            otherPrice.setSuffixText("元/" + unit);

            transportPrice.setText(NumberUtil.format3f(order.getTransport_unit_price()));
            transportPrice.setSuffixText("元/" + unit);

            totalPrice.setText(NumberUtil.format2f(order.getBook_amt()));
            totalPrice.setSuffixText("元");
        }
    }
}
