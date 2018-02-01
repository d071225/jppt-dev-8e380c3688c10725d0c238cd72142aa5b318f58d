package com.hletong.hyc.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.MakePhoneCall;
import com.hletong.hyc.adapter.QuoteAdapter;
import com.hletong.hyc.contract.QuoteContract;
import com.hletong.hyc.model.CallInfo;
import com.hletong.hyc.model.Quote;
import com.hletong.hyc.presenter.QuotePresenter;
import com.hletong.hyc.ui.activity.ApplyDepositCardActivity;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.gallery.builder.PreViewBuilder;
import com.hletong.mob.gallery.widget.PickerRecyclerView;
import com.hletong.mob.pullrefresh.EmptyView;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.xcheng.view.controller.dialog.BottomOptionDialog;
import com.xcheng.view.controller.dialog.SimpleSelectListener;
import com.xcheng.view.enums.RequestState;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ddq on 2017/3/27.
 * 货方待办消息-查看车船方的报价详情
 */

public class QuoteProgressFragment extends BaseRefreshFragment<Quote> implements QuoteContract.View {

    private QuoteContract.Presenter mPresenter;
    private Dialog mApplyDialog;
    private LinearLayout headerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ArrayList<Quote> list = getArguments().getParcelableArrayList("list");
        mPresenter = new QuotePresenter(this, getArguments().getString("file"), list, getArguments().getInt("bookRefType"), getArguments().getString("unit"));
    }

    public void update(List<Quote> list) {
        mPresenter.refresh(list);
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<Quote> createAdapter() {
        QuoteAdapter adapter = new QuoteAdapter(getContext(), new MakePhoneCall() {
            @Override
            public void call(String contact, String contactTel, String src) {
                CallPhoneDialog dialog = CallPhoneDialog.getInstance();
                String[] s = src.split(",");
                dialog.setOnCalledListener(new Record(contact, s[0], contactTel, s[1]));
                dialog.show(getFragmentManager(), "电话议价", "确定要联系承运方议价吗？", contactTel);
            }
        }, new MakePhoneCall() {
            @Override
            public void call(String contact, String contactTel, String src) {
                CallPhoneDialog.getInstance().show(getFragmentManager(), "联系会员管理单位", "确定要联系会员管理单位吗？", contactTel);
            }
        }, mPresenter);
        adapter.hideFooter();
        return adapter;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (headerView == null) {
            headerView = new LinearLayout(getContext());
            headerView.setOrientation(LinearLayout.VERTICAL);
            LayoutInflater.from(getContext()).inflate(R.layout.activity_source_details_extra_zzjy, headerView);
            CommonInputView volume = headerView.findViewById(R.id.volume);
            volume.setText(getArguments().getString("volume"));
            CommonInputView doc = headerView.findViewById(R.id.bus_doc_no);
            doc.setText(getArguments().getString("doc"));
        }
        mAdapter.setHeader(headerView);
        mPresenter.start();
    }

    @Override
    protected boolean onlyView() {
        return true;
    }

    @Override
    public void showAlert500(String message) {
        if (mApplyDialog == null) {
            mApplyDialog = new BottomOptionDialog.Builder(getContext())
                    .optionTexts("会员管理单位垫付", "办理惠龙卡")
                    .onSelectListener(new SimpleSelectListener() {
                        @Override
                        public void onOptionSelect(View view, int position) {
                            if (position == 0) {
                                mPresenter.acceptWithMU();
                            } else if (position == 1) {
                                toActivity(ApplyDepositCardActivity.class, null, null);
                            }
                        }
                    }).create();
        }
        mApplyDialog.show();
    }

    @Override
    public void showAlertForUserInfoNotComplete(String content, DialogInterface.OnClickListener mListener) {
        DialogFactory.showAlertWithNegativeButton(getActivity(), false, "接受报价", content, "认证", mListener, "取消", null);
    }

    @Override
    public void initQuote(String cargoUnit, int bookRefType) {
        QuoteAdapter adapter = (QuoteAdapter) mAdapter;
        adapter.setExtra(cargoUnit, bookRefType);
    }

    @Override
    public void showAlert501(String message) {
        DialogFactory.showAlertWithNegativeButton(getContext(), false, null, message, "惠龙卡介绍", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                toActivity(ApplyDepositCardActivity.class, null, null);
            }
        }, "取消", null);
    }

    @Override
    public void success(String message) {
        showMessage(message);
    }

    @Override
    public EmptyView getEmptyView() {
        return super.getEmptyView().setEmptyLogo(R.drawable.icon_empty_quote).setCustomState(RequestState.NO_DATA, "暂无报价信息");
    }

    @Override
    public void showImages(List<String> images) {
        PickerRecyclerView view = new PickerRecyclerView(getContext());
        view.setBackgroundColor(Color.WHITE);
        int padding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 15, getResources().getDisplayMetrics());
        view.setPadding(padding, 0, padding, padding * 2 / 3);
        headerView.addView(view, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        new PickerRecyclerView.Builder((Activity) getContext()).selectedPhotos(new ArrayList<>(images)).action(PreViewBuilder.PREVIEW).build(view);
    }

    private class Record implements CallPhoneDialog.OnCalledListener {
        private CallInfo mCallInfo;

        public Record(String contact, String contactCode, String tel, String quoteUuid) {
            mCallInfo = CallInfo.QUOTE_PROGRESS(contact, contactCode, tel, quoteUuid);
        }

        @Override
        public void onCalled() {
            mPresenter.record(mCallInfo);
        }
    }
}
