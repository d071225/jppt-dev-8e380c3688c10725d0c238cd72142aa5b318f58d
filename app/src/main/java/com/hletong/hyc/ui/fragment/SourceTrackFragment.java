package com.hletong.hyc.ui.fragment;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.MakePhoneCall;
import com.hletong.hyc.adapter.SourceTrackAdapter;
import com.hletong.hyc.contract.SourceTrackContract;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.SourceTrack;
import com.hletong.hyc.model.SourceTrade;
import com.hletong.hyc.presenter.SourceTrackPresenter;
import com.hletong.hyc.ui.activity.BillingInfoFragment;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.EmptyView;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.view.enums.RequestState;
import com.xcheng.view.util.LocalDisplay;

import org.json.JSONException;

import java.util.List;

/**
 * Created by ddq on 2017/3/8.
 */
public class SourceTrackFragment extends BaseRefreshFragment<SourceTrack> implements SourceTrackContract.View, SourceTrackAdapter.DataManipulate, MakePhoneCall {
    private SourceTrackContract.Presenter mPresenter;

    public static Bundle getInstance(Source source) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("source", source);
        return bundle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.phone_selector, menu);
        MenuItem menuItem = menu.findItem(R.id.call);
        menuItem.setTitle("开票信息");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = new Bundle();
        mPresenter.prepareData(bundle);//把相关数据放进去
        toActivity(CommonWrapFragmentActivity.class, CommonWrapFragmentActivity.getBundle("开票信息", BillingInfoFragment.class, bundle), null);
        return true;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        mPresenter = new SourceTrackPresenter(this, (Source) getArguments().getParcelable("source"));
        mPresenter.start();
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_source_track;
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<SourceTrack> createAdapter() {
        SourceTrackAdapter adapter = new SourceTrackAdapter(getContext(), this, this, (Source) getArguments().getParcelable("source"));
        adapter.hideFooter();
        return adapter;
    }

    @Override
    protected boolean onlyView() {
        return true;
    }

    @Override
    public void requestData(boolean refresh) {
        super.requestData(refresh);
    }

    @Override
    public void initSourceView(Source source) throws JSONException {
        setVisibility(R.id.action, View.GONE);
        setVisibility(R.id.flag, View.GONE);
        setText(R.id.title, source.getOrgin_cargon_kind_name());
        setText(R.id.CargoWeight, source.getCargoDescWithUnit());
        setText(R.id.startProvince, Address.chooseCity(source.getLoading_province(), source.getLoading_city()));//不是显示省，而是市
        setText(R.id.startCity, Address.chooseCountry(source.getLoading_province(), source.getLoading_city(), source.getLoading_country()));//不是显示市，而是区
        setText(R.id.endProvince, Address.chooseCity(source.getUnload_province(), source.getUnload_city()));//不是显示省，而是市
        setText(R.id.endCity, Address.chooseCountry(source.getUnload_province(), source.getUnload_city(), source.getUnload_country()));//不是显示市，而是区
        setText(R.id.time, "装货日期：" + source.getLoadingPeriod());
        setVisibility(R.id.cargo, View.VISIBLE);

        mPresenter.loadTrackInfo(
                new ItemRequestValue<SourceTrade>(hashCode(),
                        Constant.getUrl(Constant.SOURCE_TRACK_INFO),
                        new ParamsHelper().put("cargoUuid", source.getCargo_uuid()).put("needInvoice", true)) {
                }
        );
    }

    @Override
    public void notifyItemInsertRange(int from, int count) {
        mAdapter.notifyItemRangeInserted(from, count);
    }

    @Override
    public void notifyItemRemoveRange(int from, int count) {
        mAdapter.notifyItemRangeRemoved(from, count);
    }

    @Override
    public void notifyItemChanged(int index) {
        mAdapter.notifyItemChanged(index);
    }

    @Override
    public void manipulate(List<SourceTrack> data, SourceTrack.Status parent, int status) {
        if (status == 0) {
            mPresenter.shrinkData(data, parent);
        } else {
            mPresenter.expandData(data, parent);
        }
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new SpecialItemDecoration();
    }

    private void setText(int id, String text) {
        TextView textView = findViewById(id);
        textView.setText(text);
    }

    private void setVisibility(int id, int visible) {
        View view = findViewById(id);
        view.setVisibility(visible);
    }

    @Override
    public EmptyView getEmptyView() {
        return super.getEmptyView().setEmptyLogo(R.drawable.icon_empty_cargo_track).setCustomState(RequestState.NO_DATA, "暂无跟踪结果");
    }

    @Override
    public void call(String contact, String contactTel, String src) {
        CallPhoneDialog.getInstance().show(getFragmentManager(), "联系承运人", "确定要联系承运人吗？", contactTel);
    }

    private class SpecialItemDecoration extends RecyclerView.ItemDecoration {
        private final int SPACE = LocalDisplay.dp2px(8);
        private final int HALF_SPACE = SPACE / 2;

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int previousType = -1;

            int index = parent.getChildAdapterPosition(view);

            if (index == -1)
                return;

            if (parent.getChildViewHolder(view) instanceof SourceTrackAdapter.BaseViewHolder) {
                SourceTrackAdapter adapter = (SourceTrackAdapter) parent.getAdapter();

                if (index >= 1) {
                    previousType = adapter.getItem(index - 1).getType();
                }
                final int type = adapter.getItem(index).getType();
                if (type == 1 || type == 2) {
                    outRect.set(0, SPACE, 0, 0);
                } else if (type == 3 && previousType == 3) {
                    outRect.set(0, HALF_SPACE, 0, 0);
                }
            }
        }
    }
}
