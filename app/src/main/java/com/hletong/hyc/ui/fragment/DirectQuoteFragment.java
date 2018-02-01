package com.hletong.hyc.ui.fragment;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.CallHistoryAdapter;
import com.hletong.hyc.adapter.MakePhoneCall;
import com.hletong.hyc.contract.QuoteContract;
import com.hletong.hyc.model.CallHistory;
import com.hletong.hyc.model.CallInfo;
import com.hletong.hyc.presenter.DirectQuotePresenter;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.gallery.builder.PreViewBuilder;
import com.hletong.mob.gallery.widget.PickerRecyclerView;
import com.hletong.mob.pullrefresh.DividerItemDecoration;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dongdaqing on 2017/5/31.
 * 国联议价拨号历史记录
 */

public class DirectQuoteFragment extends BaseRefreshFragment<CallHistory> implements QuoteContract.DirectQuoteView {
    private CallPhoneDialog mPhoneDialog;
    private QuoteContract.DirectQuotePresenter mPresenter;
    private LinearLayout headerView;

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mPresenter = new DirectQuotePresenter<>(this, getArguments().getString("file"));
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<CallHistory> createAdapter() {
        return new CallHistoryAdapter(getContext(), new MakePhoneCall() {
            @Override
            public void call(String contact, String contactTel, String src) {
                if (mPhoneDialog == null) {
                    mPhoneDialog = CallPhoneDialog.getInstance();
                }
                mPhoneDialog.setOnCalledListener(new Record(
                        contact,
                        src,
                        contactTel,
                        getArguments().getString("cargoUuid")
                ));
                mPhoneDialog.show(getFragmentManager(), "电话议价", "确定要联系车船方进行议价吗？", contactTel);
            }
        });
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (headerView == null) {
            headerView = new LinearLayout(getContext());
            headerView.setOrientation(LinearLayout.VERTICAL);
            LayoutInflater.from(getContext()).inflate(R.layout.activity_source_details_extra_zzjy, headerView);
            CommonInputView volume = (CommonInputView) headerView.findViewById(R.id.volume);
            volume.setText(getArguments().getString("volume"));
            CommonInputView doc = (CommonInputView) headerView.findViewById(R.id.bus_doc_no);
            doc.setText(getArguments().getString("doc"));
        }
        mAdapter.setHeader(headerView);
    }

    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.PHONE_TRACK_HISTORY);
    }

    @Override
    protected String getEntry() {
        return null;
    }

    @Override
    protected ParamsHelper getRequestJson(boolean refresh) {
        return new ParamsHelper()
                .put("bizUuid", getArguments().getString("cargoUuid"))
                .put("callEventSrc", "10003000");
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration();
    }

    @Override
    public void showList(List<CallHistory> mList, boolean refresh) {
        mList.add(0, new CallHistory());//head
        super.showList(mList, refresh);
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

        public Record(String contact, String memberCode, String tel, String cargoUuid) {
            mCallInfo = CallInfo.QUOTE_WITH_PHONE(contact, memberCode, tel, cargoUuid);
        }

        @Override
        public void onCalled() {
            mPresenter.record(mCallInfo);
        }
    }
}
