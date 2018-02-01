package com.hletong.mob.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.hletong.mob.R;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.pullrefresh.SpaceItemDecoration;
import com.xcheng.view.util.LocalDisplay;
import com.xcheng.view.util.ShapeBinder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 输入框下拉列表选择
 * Created by chengxin on 2017/8/17.
 */
public class PopInputSelect {
    private String saveKey;
    private PopupWindow mPopupWindow;
    private RecyclerView mRecyclerView;
    private Context context;
    private EditText editText;
    private int width;
    private int offWidth;
    private Drawable popBackground;

    public PopInputSelect(EditText editText, int width, String saveKey) {
        this.context = editText.getContext();
        this.saveKey = saveKey;
        this.editText = editText;
        this.width = width;
        this.offWidth = 0;
        getData();
    }

    /**
     * @param editText 输入框
     * @param saveKey  SharedPreferences 保存的key
     * @param offWidth 宽度偏移，正数为加 负数为减
     */
    public PopInputSelect(final EditText editText, String saveKey, final int offWidth) {
        this(editText, editText.getWidth(), saveKey);
        this.offWidth = offWidth;
        if (this.width == 0) {
            editText.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    int etWidth = editText.getWidth();
                    if (etWidth != 0) {
                        width = etWidth;
                        if (mPopupWindow != null) {
                            if (!mPopupWindow.isShowing()) {
                                mPopupWindow.setWidth(width + offWidth);
                            } else {
                                mPopupWindow.update(width + offWidth, -2);
                            }
                        }
                        editText.getViewTreeObserver().removeOnPreDrawListener(this);
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    private void ensurePop() {
        if (mPopupWindow == null) {
            mRecyclerView = new RecyclerView(context);
            mRecyclerView.setMinimumHeight(LocalDisplay.dp2px(100));
            mPopupWindow = new PopupWindow(mRecyclerView, width + offWidth, -2);
            mPopupWindow.setFocusable(true); // 让popWindow获取焦点
            mPopupWindow.setOutsideTouchable(true);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            mRecyclerView.addItemDecoration(new SpaceItemDecoration(LocalDisplay.dp2px(2)));

        }
    }

    /**
     * 设置popWindow的背景
     *
     * @param drawable 背景Drawable
     */
    public void setBackgroundDrawable(Drawable drawable) {
        this.popBackground = drawable;
    }

    public void showPopWindow(int xoff, int yoff) {
        ensurePop();
        //默认灰色
        if (popBackground == null) {
            popBackground = ShapeBinder.with(Color.parseColor("#f7f7f7"))
                    .strokeWidth(1)
                    .stroke(Color.parseColor("#c2c9cc"))
                    .radius(0.5f)
                    .create(false);
        }
        mPopupWindow.setBackgroundDrawable(popBackground);
        InputListAdapter adapter = new InputListAdapter(context, getData());
        TextView emptyView = new TextView(context);
        emptyView.setGravity(Gravity.CENTER);
        emptyView.setTextSize(16);
        emptyView.setText("暂无记录");
        adapter.setEmpty(emptyView);
        mRecyclerView.setAdapter(adapter);
        mPopupWindow.showAsDropDown(editText, xoff, yoff);
    }


    private class InputListAdapter extends HFRecyclerAdapter<String> {

        private InputListAdapter(Context context, List<String> data) {
            super(context, data);
        }

        @Override
        protected BaseHolder<String> onCreateItemViewHolder(ViewGroup parent, int viewType) {
            return new BankHolder(getInflater().inflate(R.layout.item_pop_input, parent, false));
        }

        private class BankHolder extends BaseHolder<String> {

            private BankHolder(View itemView) {
                super(itemView);
                getView(R.id.inputText).setOnClickListener(this);
                getView(R.id.delete).setOnClickListener(this);

            }

            public void setData(String data) {
                setText(R.id.inputText, data);
            }

            @Override
            public void onClick(View v) {
                if (v.getId() == R.id.inputText) {
                    editText.setText(getItem(getAdapterPosition()));
                    mPopupWindow.dismiss();
                } else if (v.getId() == R.id.delete) {
                    deleteInput(getItem(getAdapterPosition()));
                    remove(getAdapterPosition());
                }
            }
        }
    }

    private List<String> getData() {
        Set<String> set = SPUtils.getStringSet(saveKey, null);
        if (set != null) {
            return new ArrayList<>(set);
        }
        return null;
    }

    public void saveInput() {
        Set<String> set = SPUtils.getStringSet(saveKey, null);
        if (set == null) {
            set = new HashSet<>();
        }
        String input = editText.getText().toString();
        if (!set.contains(input)) {
            set.add(input);
            SPUtils.putStringSet(saveKey, set);
        }
    }

    private void deleteInput(String deleteInput) {
        Set<String> set = SPUtils.getStringSet(saveKey, null);
        if (set != null) {
            //不能修改set实例再次保存，会失败
            Set<String> saveSet = new HashSet<>(set);
            saveSet.remove(deleteInput);
            SPUtils.putStringSet(saveKey, saveSet);
        }
    }
}