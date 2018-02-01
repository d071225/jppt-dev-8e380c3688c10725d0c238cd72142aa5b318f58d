package com.hletong.hyc.ui.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.R;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.VersionInfo;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.activity.MainActivity;
import com.hletong.hyc.ui.dialog.UpgradeFragment;
import com.hletong.hyc.util.AddressTimeUtil;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.LoginFilterDelegate;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.dialog.selector.BottomSelectorDialog;
import com.hletong.mob.dialog.selector.ItemSelectorAdapter;
import com.hletong.mob.pullrefresh.DividerItemDecoration;
import com.hletong.mob.pullrefresh.EmptyView;
import com.hletong.mob.util.ParamsHelper;
import com.orhanobut.logger.Logger;
import com.xcheng.view.util.LocalDisplay;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ddq on 2017/2/10.
 * 货源公告
 */

public class SourceListFragmentPublic extends BaseSourceListFragment implements AddressTimeUtil.DataChanged, BottomSelectorDialog.OnItemSelectedListener<DictionaryItem> {
    @BindView(R.id.selector)
    View selector;
    @BindView(R.id.layout_root)
    FrameLayout mFrameLayout;

    private AddressTimeUtil mUtil;
    private PopupWindow pop;
    private ValueAnimator mAnimator;
    private String billingType;
    private String shareUuid;

    private static LoginFilterDelegate sLoginFilterDelegate;

    public static LoginFilterDelegate getLoginFilterDelegate() {
        return sLoginFilterDelegate;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null && getArguments().getString("share") != null) {
            Uri uri = Uri.parse(getArguments().getString("share"));
            shareUuid = uri.getQueryParameter("shareUuid");
        }

        //监听强制更新广播
        register(MainActivity.FORCE_UPDATE);
        Logger.e("register force update broadcast listener");
    }

    @Override
    public void onReceive(Context context, String action, Intent intent) {
        broadcast(new Intent(MainActivity.FORCE_UPDATE_RECEIVED));//让MainActivity取消发送FORCE_UPDATE广播
        VersionInfo versionInfo = intent.getParcelableExtra("version");
        UpgradeFragment.getInstance(versionInfo).show(getChildFragmentManager(), UpgradeFragment.class.getName());
    }

    private void filterResume() {
        if (LoginInfo.getLoginInfo() == null && sLoginFilterDelegate == null) {
            sLoginFilterDelegate = new LoginFilterDelegate(getActivity());
        }
        if (sLoginFilterDelegate != null) {
            sLoginFilterDelegate.handleResume();
            if (LoginInfo.getLoginInfo() != null) {
                mAdapter.showFooter();
            } else {
                //防止弹出sessionTimeout的窗口
                mAdapter.hideFooter();
            }
        }
    }

    private boolean willFilter(View.OnClickListener listener) {
        return sLoginFilterDelegate != null && sLoginFilterDelegate.willFilter(listener);
    }

    @Override
    public void onResume() {
        super.onResume();
        filterResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sLoginFilterDelegate = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.history, menu);
        MenuItem menuItem = menu.getItem(0);
        menuItem.setTitle("常跑路线");
    }

    private void toFavoriteRoutineFragment() {
        toActivity(
                CommonWrapFragmentActivity.class,
                CommonWrapFragmentActivity.getBundle(
                        getString(R.string.favorite_routine),
                        FavoriteRoutineFragment.class), null);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        if (willFilter(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toFavoriteRoutineFragment();
            }
        })) {
            return true;
        }
        toFavoriteRoutineFragment();
        return true;
    }

    @Override
    protected ParamsHelper getRequestJson(boolean refresh) {
        ParamsHelper jsonObject = new ParamsHelper();
        jsonObject
                .put("start", refresh ? 1 : mAdapter.getDataCount() + 1)
                .put("limit", mAdapter.getLength())
                .put("loadingProvince", mUtil.getStartLoc().getProvinceForQuery())
                .put("loadingCity", mUtil.getStartLoc().getCityForQuery())
                .put("loadingCountry", mUtil.getStartLoc().getCountyForQuery())
                .put("unloadProvince", mUtil.getEndLoc().getProvinceForQuery())
                .put("unloadCity", mUtil.getEndLoc().getCityForQuery())
                .put("unloadCountry", mUtil.getEndLoc().getCountyForQuery())
                .put("orginCargonKindName", "")
                .put("dateTypeEnum", mUtil.getDateType())
                .put("transportType", BuildConfig.carrier_type)
                .put("billingType", billingType)
                .put("shareUuid", shareUuid);
        return jsonObject;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        mUtil = new AddressTimeUtil(selector, getActivity(), this, this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        BaseActivity baseActivity = getBaseActivity();
        TextView textView = baseActivity.getTitleView();
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(textView, 0, 0, R.drawable.hygg_nav_button, 0);
        textView.setCompoundDrawablePadding(LocalDisplay.dp2px(4));
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSourceFilter();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mUtil.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.GOODS_NOTICE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_source_list;
    }

    @Override
    public void addressChanged(int type, Address address) {
        mPtrFrameLayout.autoRefresh(true);
    }

    @Override
    public void timeChanged(String type) {
        mPtrFrameLayout.autoRefresh(true);
    }

    @Override
    public EmptyView getEmptyView() {
        return super.getEmptyView().setEmptyLogo(R.drawable.icon_empty_source_list);
    }

    private void showSourceFilter() {
        if (pop == null) {
            List<DictionaryItem> list = new ArrayList<>();
            list.add(new DictionaryItem("", "全部货源"));
            list.add(new DictionaryItem("1", "平台开票交易货源"));
            list.add(new DictionaryItem("2", "自主开票交易货源"));
            list.add(new DictionaryItem("3", "自主交易货源"));
            list.add(new DictionaryItem("4", "撮合交易货源"));

            ItemSelectorAdapter mAdapter = new ItemSelectorAdapter<>(getContext(), list, this);
            mAdapter.setCustomLayout(R.layout.recycler_item_single_text_with_radio);
            //初始化RecyclerView
            RecyclerView recyclerView = new RecyclerView(getContext());
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.addItemDecoration(new DividerItemDecoration());
            //初始化PopupWindow
            pop = new PopupWindow();
            pop.setContentView(recyclerView);
            pop.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            pop.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            pop.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
            pop.setFocusable(true);
            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    mAnimator.reverse();
                }
            });

            int DIM_ALPHA = (int) (0.33f * 255);
            mAnimator = ValueAnimator.ofObject(new ArgbEvaluator(), Color.TRANSPARENT, Color.argb(DIM_ALPHA, 0, 0, 0));
            mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mFrameLayout.setForeground(new ColorDrawable((Integer) animation.getAnimatedValue()));
                }
            });
        }

        PopupWindowCompat.showAsDropDown(pop, selector, 0, -selector.getHeight(), Gravity.NO_GRAVITY);
        mAnimator.start();
    }

    @Override
    public void onItemSelected(DictionaryItem item, int extra) {
        pop.dismiss();
        setTitle(item.getText());
        billingType = item.getId();
        mPtrFrameLayout.autoRefresh(true);
    }
}
