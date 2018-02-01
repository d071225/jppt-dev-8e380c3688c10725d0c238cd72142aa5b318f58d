package com.hletong.hyc.ui.fragment;

import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.CargoSourceAdapter;
import com.hletong.hyc.model.CargoSourceItem;
import com.hletong.hyc.ui.activity.CargoForecastActivity;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.FirstVisibleItemChangedListener;
import com.hletong.hyc.util.HistoryHeader;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.AnimationUtil;
import com.hletong.mob.util.ParamsHelper;
import com.hletong.mob.util.SimpleDate;
import com.orhanobut.logger.Logger;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/3/6.
 * 货方个人货源列表
 */

public class CargoSourceListFragment extends CopySourceFragment {
    @BindView(R.id.id_positiveBtn)
    TextView pb;
    @BindView(R.id.id_negativeBtn)
    TextView nb;
    @BindView(R.id.button_container)
    View bc;
    @BindView(R.id.scroll_to_top)
    View stp;

    private final int SCROLL_THRESHOLD = 15;
    private SourceFilterPanel mSourceFilterPanel;
    private ValueAnimator mAnimator;
    private FrameLayout mRoot;


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.phone_selector, menu);
        MenuItem menuItem = menu.findItem(R.id.call);
        menuItem.setTitle("筛选");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mSourceFilterPanel == null)
            mSourceFilterPanel = new SourceFilterPanel();
        mSourceFilterPanel.showFilter();
        return true;
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<CargoSourceItem> createAdapter() {
        return new CargoSourceAdapter(getContext(), this);
    }

    @Override
    protected ParamsHelper getRequestJson(boolean refresh) {
        if (mSourceFilterPanel == null)
            return super.getRequestJson(refresh).put("status", "");
        return mSourceFilterPanel.getQueryParams(super.getRequestJson(refresh));
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new HistoryHeader((ViewGroup) getActivity().findViewById(R.id.layout_root), 1);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        nb.setText(R.string.forecast_source);
        nb.setTextColor(Color.WHITE);
        pb.setText(R.string.copy_source);
        pb.setTextColor(Color.WHITE);
        bc.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.normal_btn_dark));
        mRecyclerView.addOnScrollListener(new FirstVisibleItemChangedListener(new FirstVisibleItemChangedListener.VisibleItemChanged() {
            @Override
            public void onItemChanged(int oldFirstItem, int newFirstItem) {
                if (newFirstItem > oldFirstItem) {
                    if (newFirstItem >= SCROLL_THRESHOLD && oldFirstItem < SCROLL_THRESHOLD) {
                        Logger.d("scale in");
                        AnimationUtil.scaleIn(stp);
                    }
                } else {
                    if (newFirstItem < SCROLL_THRESHOLD && oldFirstItem >= SCROLL_THRESHOLD) {
                        Logger.d("scale out");
                        AnimationUtil.scaleOut(stp);
                    }
                }
            }
        }));
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_cargo_source_list;
    }

    @OnClick({R.id.draft, R.id.scroll_to_top, R.id.id_negativeBtn, R.id.id_positiveBtn})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.draft:
                toActivity(CommonWrapFragmentActivity.class, CommonWrapFragmentActivity.getBundle("草稿", DraftFragment.class), null);
                break;
            case R.id.scroll_to_top:
                mRecyclerView.smoothScrollToPosition(0);
                break;
            case R.id.id_negativeBtn:
                toActivity(CargoForecastActivity.class, null, 110, null);
                break;
            case R.id.id_positiveBtn:
                if (mSourceFilterPanel == null) {
                    toActivity(CommonWrapFragmentActivity.class, CommonWrapFragmentActivity.getBundle(getString(R.string.copy_source), CopySourceFragment.class), null);
                    return;
                }

                Bundle bundle = new Bundle();
                ParamsHelper helper = new ParamsHelper();
                mSourceFilterPanel.getQueryParams(helper);
                JSONObject object = helper.getObject();
                Iterator<String> iterator = object.keys();
                String r = "";
                while (iterator.hasNext()) {
                    final String key = iterator.next();
                    r += key;
                    r += "=";
                    try {
                        r += object.getString(key);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    r += "&";
                }
                bundle.putString("extra", r);
                toActivity(CommonWrapFragmentActivity.class, CommonWrapFragmentActivity.getBundle(getString(R.string.copy_source), CopySourceFragment.class, bundle), null);
                break;
        }
    }

    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.PERSONAL_SOURCE_LIST);
    }

    @Override
    public void onActivityResultOk(int requestCode, Intent data) {
        super.onActivityResultOk(requestCode, data);
        mPtrFrameLayout.autoRefresh(true);
    }

    private class SourceFilterPanel {
        private PopupWindow filterPanel;
        /**
         * 这里为了方便用了tag的方式，每个item对应类型在其parentView的tag里面，
         * 而每个item自己的tag就是查询参数，具体参考布局文件
         */
        private ArrayMap<String, ArrayList<View>> mView;
        private View.OnClickListener l = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.id_positiveBtn) {
                    filterPanel.dismiss();
                    mPtrFrameLayout.autoRefresh();
                } else if (v.getId() == R.id.id_negativeBtn) {
                    deSelectAllView();
                } else {
                    selectView(v);
                }
            }
        };

        private void showFilter() {
            if (filterPanel == null) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_cargo_source_list_filter, null);
                TextView positive = (TextView) view.findViewById(R.id.id_positiveBtn);
                positive.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.normal_btn_dark));
                positive.setTextColor(Color.WHITE);
                positive.setOnClickListener(l);
                TextView negative = (TextView) view.findViewById(R.id.id_negativeBtn);
                negative.setText("重置");
                negative.setOnClickListener(l);
                prepare(view);

                filterPanel = new PopupWindow();
                filterPanel.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
                DisplayMetrics manager = getResources().getDisplayMetrics();
                filterPanel.setWidth((int) (manager.widthPixels * 0.8f));
                filterPanel.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                filterPanel.setFocusable(true);
                filterPanel.setContentView(view);
                filterPanel.setAnimationStyle(R.style.SourceFilterAnimation);
                filterPanel.setOnDismissListener(new PopupWindow.OnDismissListener() {
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
                        mRoot.setForeground(new ColorDrawable((Integer) animation.getAnimatedValue()));
                    }
                });

                mRoot = (FrameLayout) getActivity().findViewById(R.id.container);
            }
            filterPanel.showAtLocation(getActivity().getWindow().getDecorView(), Gravity.RIGHT, 0, 0);
            mAnimator.start();
        }

        //将所有的item按照分类放到mView里面
        private void prepare(View v) {
            LinearLayout layout = (LinearLayout) v.findViewById(R.id.container);
            for (int i = 0; i < layout.getChildCount(); i++) {
                View view = layout.getChildAt(i);
                if (isViewContainer(view)) {
                    LinearLayout linearLayout = (LinearLayout) view;
                    for (int j = 0; j < linearLayout.getChildCount(); j++) {
                        if (mView == null)
                            mView = new ArrayMap<>();
                        ArrayList<View> children = mView.get(view.getTag());
                        if (children == null) {
                            mView.put((String) view.getTag(), new ArrayList<View>());
                            children = mView.get(view.getTag());
                        }
                        if (linearLayout.getChildAt(j).getTag() != null) {
                            children.add(linearLayout.getChildAt(j));
                        }
                    }
                }
            }

            //设置所有item的点击事件
            for (ArrayList<View> views : mView.values()) {
                for (View view : views)
                    view.setOnClickListener(l);
            }
        }

        //每一个分组内的item是单选
        private void selectView(View v) {
            View parent = (View) v.getParent();
            ArrayList<View> views = mView.get(parent.getTag());
            View lastSelectedView = null;
            for (View view : views) {
                if (view.isSelected()) {
                    lastSelectedView = view;
                    break;
                }
            }

            if (v != lastSelectedView)
                v.setSelected(true);

            if (lastSelectedView != null)
                lastSelectedView.setSelected(false);
        }

        private void deSelectAllView() {
            for (ArrayList<View> views : mView.values()) {
                for (View view : views)
                    view.setSelected(false);
            }
        }

        //这个函数判断是View的tag，这个是写在布局文件里面的，所以修改布局文件时要注意对照不能乱
        private boolean isViewContainer(View view) {
            final String tag = (String) view.getTag();
            return tag != null && (
                    "status".equals(tag) ||//货源状态
                            "date".equals(tag) ||//发布时间
                            "type".equals(tag) ||//交易类型
                            "price".equals(tag) ||//长期协议价
                            "transporter".equals(tag));//运输方式
        }

        public ParamsHelper getQueryParams(ParamsHelper object) {
//            object.put("isWrtrUnit", "1");
            object.put("transType", getParam("type"));
            object.put("transportType", getParam("transporter"));
            object.put("agrt_price_flag", getParam("price"));
            object.put("status", getParam("status"));

            SimpleDate sd = getTimeString(getParam("date"));
            if (sd != null) {
                object.put("createTsBegin", sd.dateString(true, "") + sd.timeString(true, ""));
                object.put("createTsEnd", getEndTimeString(sd));
            }
            return object;
        }

        private SimpleDate getTimeString(String tag) {
            long timeShift = 0;
            Calendar calendar = Calendar.getInstance();
            switch (tag) {
                case "1"://今天
                    break;
                case "2"://昨天
                    timeShift = SimpleDate.DAY_MILLISECONDS;
                    break;
                case "3"://前天
                    timeShift = SimpleDate.DAY_MILLISECONDS * 2;
                    break;
                case "4"://本周，我们概念里的一周是从周一开始的，而取出来的是从周日开始的,所以要进行校准
                {
                    calendar.setFirstDayOfWeek(Calendar.MONDAY);
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                    timeShift = SimpleDate.DAY_MILLISECONDS * dayOfWeek;
                    break;
                }
                case "5"://上周
                {
                    calendar.setFirstDayOfWeek(Calendar.MONDAY);
                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
                    timeShift = SimpleDate.DAY_MILLISECONDS * (dayOfWeek + 7);
                    break;
                }
                case "6"://更早
                default:
                    return null;
            }

            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 1);

            calendar.setTimeInMillis(calendar.getTimeInMillis() - timeShift);
            return new SimpleDate(calendar);
        }

        private String getEndTimeString(SimpleDate sd) {
            if (sd == null)
                return null;
            Calendar calendar = Calendar.getInstance();
            calendar.set(sd.getYear(), sd.getMonthOfYear() - 1, sd.getDayOfMonth());
            long shift = 0;
            switch (getParam("date")) {
                case "2"://昨天
                case "3"://前天
                    shift = SimpleDate.DAY_MILLISECONDS;
                    break;
                case "5"://上周
                    shift = SimpleDate.DAY_MILLISECONDS * 7;
                    break;
                default://其他情况不需要结束时间
                    return null;
            }
            calendar.setTimeInMillis(calendar.getTimeInMillis() + shift);
            SimpleDate simpleDate = new SimpleDate(calendar);
            return simpleDate.dateString(true, "") + simpleDate.timeString(true, "");
        }

        //获取查询参数，具体的值都写在View的tag里面，详见布局文件
        private String getParam(String tag) {
            ArrayList<View> views = mView.get(tag);
            View sv = null;
            for (View v : views) {
                if (v.isSelected()) {
                    sv = v;
                    break;
                }
            }
            if (sv == null)
                return "";
            else
                return (String) sv.getTag();
        }
    }
}
