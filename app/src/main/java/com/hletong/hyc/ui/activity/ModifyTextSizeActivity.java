package com.hletong.hyc.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IntDef;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.ui.widget.SeekBarView;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.pullrefresh.SpaceItemDecoration;
import com.hletong.mob.util.ViewUtil;
import com.xcheng.view.util.LocalDisplay;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ModifyTextSizeActivity extends BaseActivity {
    @BindView(R.id.tv_PreView)
    TextView tvPreView;
    @BindView(R.id.sb_textSize)
    SeekBarView seekBarView;
    @BindView(R.id.tv_smallSize)
    TextView smallSize;
    @BindView(R.id.tv_middleSize)
    TextView middleSize;
    @BindView(R.id.tv_largeSize)
    TextView largeSize;
    @BindView(R.id.id_recyclerView)
    RecyclerView mRecyclerView;

    TextSizeAdapter textSizeAdapter;
    /***
     * 下标和主题一一对应的关系,版本升级的时候要注意
     **/
    public static final int SMALL_INDEX = 0;
    public static final int NORMAL_INDEX = 1;
    public static final int LARGER_INDEX = 2;

    @IntDef({SMALL_INDEX, NORMAL_INDEX, LARGER_INDEX})
    @Retention(RetentionPolicy.SOURCE)
    @interface Index {
    }

    public static final int THEMES[] = new int[]{R.style.HLETAppTheme_SmallSize, R.style.HLETAppTheme_NormalSize, R.style.HLETAppTheme_LargeSize};
    /***
     * END
     **/

    private static int LIGHT_BLUE;
    private static int BLACK;

    @Override
    public int getLayoutId() {
        return R.layout.activity_modify_text_size;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        int savedThemeIndex = ThemeManager.getThemeIndex();
        LIGHT_BLUE = ContextCompat.getColor(this, R.color.themeColor);
        BLACK = ContextCompat.getColor(this, R.color.text_black);
        if (savedThemeIndex == SMALL_INDEX) {
            seekBarView.setProgress(0);
            setTextColor(LIGHT_BLUE, BLACK, BLACK);
        } else if (savedThemeIndex == LARGER_INDEX) {
            seekBarView.setProgress(100);
            setTextColor(BLACK, BLACK, LIGHT_BLUE);
        } else {
            seekBarView.setProgress(50);
            setTextColor(BLACK, LIGHT_BLUE, BLACK);
        }

        initList();
    }

    private void initList() {
        List<Model> models = new ArrayList<>();
        Model model0 = new Model();
        model0.title = "耐磨材料";
        model0.startCity = "长沙市";
        model0.endCity = "承德市";
        model0.startArea = "长沙县";
        model0.endArea = "滦平县";
        model0.time = "06月12日-06月13日";
        model0.type = TextSizeAdapter.YIJIA;
        models.add(model0);

        Model model1 = new Model();
        model1.title = "木材";
        model1.startCity = "三门峡市";
        model1.endCity = "青岛市";
        model1.startArea = "陕州区";
        model1.endArea = "黄岛区";
        model1.time = "06月12日-06月13日";
        model1.type = TextSizeAdapter.ZHAIPAI;
        models.add(model1);
        textSizeAdapter = new TextSizeAdapter(this, models);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new SpaceItemDecoration(LocalDisplay.dp2px(8)));
        mRecyclerView.setAdapter(textSizeAdapter);
    }

    @Override
    public void setListener() {
        super.setListener();
        seekBarView.setOnTextSizeListener(new SeekBarView.OnTextSizeChangeListener() {
            @Override
            public void setSmallSize() {
                setThemeAndTextSize(SMALL_INDEX);
                setTextColor(LIGHT_BLUE, BLACK, BLACK);
            }

            @Override
            public void setMiddleSize() {
                setThemeAndTextSize(NORMAL_INDEX);
                setTextColor(BLACK, LIGHT_BLUE, BLACK);
            }

            @Override
            public void setLargeSize() {
                setThemeAndTextSize(LARGER_INDEX);
                setTextColor(BLACK, BLACK, LIGHT_BLUE);
            }
        });
    }

    private void setTextColor(int small, int middle, int large) {
        smallSize.setTextColor(small);
        middleSize.setTextColor(middle);
        largeSize.setTextColor(large);
    }

    private void setThemeAndTextSize(@Index int themeIndex) {
        ThemeManager.setThemeIndex(ModifyTextSizeActivity.this, themeIndex, false);
        recreate();
    }

    private static class TextSizeAdapter extends HFRecyclerAdapter<Model> {
        static int ZHAIPAI = 0;
        static int YIJIA = 1;

        TextSizeAdapter(Context context, List<Model> data) {
            super(context, data);
        }

        @Override
        protected BaseHolder<Model> onCreateItemViewHolder(ViewGroup parent, int viewType) {
            return new TextSizeHolder(getInflater().inflate(R.layout.recycler_item_source, parent, false));
        }
    }

    private static class TextSizeHolder extends BaseHolder<Model> {

        TextSizeHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void setData(Model data) {
            super.setData(data);
            setText(R.id.title, data.title);
            setText(R.id.CargoWeight, "100吨");
            setText(R.id.startProvince, data.startCity);//不是显示省，而是市
            setText(R.id.startCity, data.startArea);//不是显示市，而是区
            setText(R.id.endProvince, data.endCity);//不是显示省，而是市
            setText(R.id.endCity, data.endArea);//不是显示市，而是区
            setText(R.id.time, "装货日期：" + data.time);
            if (data.type == TextSizeAdapter.YIJIA) {
                setText(R.id.flag, "议");
                setTextColor(R.id.flag, getColor(R.color.colorAccent));
                ViewUtil.setBackground(getView(R.id.flag), ContextCompat.getDrawable(itemView.getContext(), R.drawable.recycler_item_bg_quote));
                setText(R.id.action, "议价");
                setTextColor(R.id.action, getColor(R.color.colorAccent));
                ViewUtil.setBackground(getView(R.id.action), ContextCompat.getDrawable(itemView.getContext(), R.drawable.recycler_item_quote_border));
            }
        }
    }

    private static class Model {
        private String title;
        private String startCity;
        private String startArea;
        private String endCity;
        private String endArea;
        private String time;
        private int type;
    }
}
