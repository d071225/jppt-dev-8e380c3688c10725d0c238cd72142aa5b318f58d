package com.hletong.hyc.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.google.android.flexbox.FlexboxLayoutManager;
import com.hletong.hyc.R;
import com.hletong.hyc.adapter.AppointCarrierAdapter;
import com.hletong.hyc.adapter.SelectedCarrierAdapter;
import com.hletong.hyc.model.AppointCarrier;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;
import com.hletong.mob.widget.BorderTextView;
import com.xcheng.okhttp.util.ParamUtil;
import com.xcheng.view.util.LocalDisplay;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/3/20.
 * 指定车船
 */

public class AppointCarrierFragment extends BaseRefreshFragment<AppointCarrier> implements AppointCarrierAdapter.ItemChangedListener<AppointCarrier> {

    @BindView(R.id.search_input)
    EditText searchInput;
    @BindView(R.id.search_box)
    FrameLayout searchBox;
    @BindView(R.id.select_title)
    BorderTextView selectTitle;
    @BindView(R.id.select)
    View select;
    @BindView(R.id.selectedCarrier)
    RecyclerView selectedCarrier;

    private int billingType;

    public static Bundle getBundle(int billingType, int transporterType, ArrayList<AppointCarrier> carriers) {
        Bundle bundle = new Bundle();
        bundle.putInt("billingType", billingType);
        bundle.putInt("transporterType", transporterType);
        if (carriers != null)
            bundle.putParcelableArrayList("carriers", carriers);
        return bundle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        billingType = getBillingType();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_appoint_carrier;
    }

    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.APPOINT_CARRIER);
    }


    @Override
    protected ParamsHelper getRequestJson(boolean refresh){
        if (billingType == 2) {
            return super.getRequestJson(refresh).put("billingType", billingType).put("transportType", getTransporterType()).put("carrierNo", searchInput.getText().toString().trim());
        } else {
            return super.getRequestJson(refresh).put("billingType", billingType).put("transportType", getTransporterType());
        }
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<AppointCarrier> createAdapter() {
        return new AppointCarrierAdapter(getContext(), getTransporterType(), getCarriers(), this);
    }

    @Override
    protected boolean isAutoLoad() {
        return billingType == 1;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        if (billingType == 2) {
            select.setVisibility(View.VISIBLE);
            searchBox.setVisibility(View.VISIBLE);

            ArrayList<AppointCarrier> carriers = getCarriers();
            if (!ParamUtil.isEmpty(carriers)){
                selectTitle.setVisibility(View.VISIBLE);
                selectedCarrier.setVisibility(View.VISIBLE);
                initRecycler(carriers);
            }
        }
    }

    @OnClick({R.id.query, R.id.ok})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.query:
                mPtrFrameLayout.autoRefresh(true);
                break;
            case R.id.ok:
                AppointCarrierAdapter adapter = (AppointCarrierAdapter) mAdapter;
                ArrayList<AppointCarrier> carriers = adapter.getSelectedItems();
                if (ParamUtil.isEmpty(carriers)) {
                    showMessage("至少选择一个车船");
                } else {
                    Intent intent = new Intent();
                    intent.putParcelableArrayListExtra("data", carriers);
                    getActivity().setResult(Activity.RESULT_OK, intent);
                    getActivity().finish();
                }
                break;
        }
    }

    private int getTransporterType() {
        if (getArguments() == null)
            return -1;
        return getArguments().getInt("transporterType", -1);
    }

    private int getBillingType() {
        if (getArguments() == null)
            return -1;
        return getArguments().getInt("billingType", -1);
    }

    private ArrayList<AppointCarrier> getCarriers() {
        if (getArguments() == null)
            return null;
        return getArguments().getParcelableArrayList("carriers");
    }

    private void initRecycler(ArrayList<AppointCarrier> carriers){
        selectedCarrier.addItemDecoration(new SelectedDecoration(getContext()));
        selectedCarrier.setLayoutManager(new FlexboxLayoutManager());
        selectedCarrier.setAdapter(new SelectedCarrierAdapter(getContext(), carriers, getTransporterType()));
    }

    @Override
    public void itemSelected(AppointCarrier item) {
        if (billingType == 1)
            return;

        if (selectedCarrier.getAdapter() == null){
            initRecycler(new ArrayList<AppointCarrier>());
        }

        selectTitle.setVisibility(View.VISIBLE);
        selectedCarrier.setVisibility(View.VISIBLE);

        SelectedCarrierAdapter adapter = (SelectedCarrierAdapter) selectedCarrier.getAdapter();
        adapter.add(item);
    }

    @Override
    public void itemUnSelected(AppointCarrier item) {
        if (billingType == 1)
            return;

        SelectedCarrierAdapter adapter = (SelectedCarrierAdapter) selectedCarrier.getAdapter();
        adapter.remove(item);

        if (adapter.isEmpty()){
            selectTitle.setVisibility(View.GONE);
            selectedCarrier.setVisibility(View.GONE);
        }
    }

    private class SelectedDecoration extends RecyclerView.ItemDecoration {
        private final int left;
        private final int top;

        public SelectedDecoration(Context context) {
            left =  LocalDisplay.dp2px( 15);
            top =  LocalDisplay.dp2px( 6);
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.set(left, top, 0, 0);
        }
    }
}
