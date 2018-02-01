package com.hletong.hyc.ui.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.BillingInfoAdapter;
import com.hletong.hyc.model.SourceInvoice;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.EmptyView;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.NumberUtil;
import com.xcheng.view.enums.RequestState;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by ddq on 2017/3/9.
 */

public class BillingInfoFragment extends BaseRefreshFragment<SourceInvoice.InvoiceDetail>{
    @BindView(R.id.total_billing_money)
    TextView tbm;
    @BindView(R.id.wait_billing_money)
    TextView wbm;
    @BindView(R.id.status_start)
    TextView ss;
    @BindView(R.id.status_end)
    TextView se;

    private AnimatorSet mAnimatorSet;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.phone_selector,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CallPhoneDialog.getInstance().show(getFragmentManager());
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_billing_info;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        ss.setText(R.string.billed);
        SourceInvoice si = getSourceInvoice();
        if (si != null)
            showBillingInfo(si.getsTotalBilling(), si.getBilled(), si.getRemainBilling(), NumberUtil.format3f(si.getBilledItem()));
    }

    public void showBillingInfo(double total, double billed, double unBilling, String billed_item) {
        se.setText(String.format("共%s张，%s元", billed_item, billed));
        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setDuration(1000);
        mAnimatorSet.playTogether(animate(total, tbm), animate(unBilling, wbm));
        mAnimatorSet.start();
    }

    private Animator animate(double value, final TextView textView) {
        ValueAnimator animator = ValueAnimator.ofFloat(0, (float) value);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText(String.valueOf(animation.getAnimatedValue()));
            }
        });
        return animator;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mAnimatorSet != null)
            mAnimatorSet.end();
    }

    @Override
    protected boolean onlyView() {
        return true;
    }

    @Override
    public EmptyView getEmptyView() {
        return super.getEmptyView().setEmptyLogo(R.drawable.icon_empty_billing_info).setCustomState(RequestState.NO_DATA, "暂无开票记录");
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<SourceInvoice.InvoiceDetail> createAdapter() {
        BillingInfoAdapter adapter = new BillingInfoAdapter(getContext(), getList());
        adapter.hideFooter();
        return adapter;
    }

    public static Bundle getBundle(SourceInvoice si){
        Bundle bundle = new Bundle();
        bundle.putParcelable("SourceInvoice",si);
        return bundle;
    }

    private List<SourceInvoice.InvoiceDetail> getList(){
        SourceInvoice si = getSourceInvoice();
        if (si != null)
            return si.getInvoiceDetailList();
        return null;
    }

    private SourceInvoice getSourceInvoice(){
        if (getArguments() != null)
            return getArguments().getParcelable("SourceInvoice");
        return null;
    }
}
