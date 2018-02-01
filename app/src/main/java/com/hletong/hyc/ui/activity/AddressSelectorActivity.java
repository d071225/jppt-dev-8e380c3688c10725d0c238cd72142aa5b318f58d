package com.hletong.hyc.ui.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.AddressNode;
import com.hletong.hyc.ui.widget.CheckableText;
import com.hletong.hyc.util.AddressParser;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.util.ViewUtil;
import com.xcheng.view.util.LocalDisplay;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dongdaqing on 16/5/13.
 * 地址选择器
 */
public class AddressSelectorActivity extends BaseActivity {
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.selected)
    TextView selected;
    @BindView(R.id.previous)
    TextView previous;

    private AddressParser mAddressParser;

    //保存省市区
    private AddressNode province;
    private AddressNode city;
    private AddressNode county;

    /**
     * 能够选择的最大深度
     * 1 => 只能选择到省
     * 2 => 能选择到省市
     * 3 => 能选择省市区
     */
    private int maxDepth;
    private int currentDepth;//1省，2市，3区

    private int extra;//是否需要添加全国(0x01)全省(0x02)全市(0x04)

    @OnClick({R.id.previous, R.id.cancel})
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.cancel) {
            finish();
            return;
        }

        if (currentDepth == 3) {
            switchToIndex(currentDepth - 1, province.getName());
        } else {
            switchToIndex(1, null);
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_address_selector;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        setCustomTitle(getIntent().getStringExtra("title"));
        init();
    }

    private void init() {
        final int spanCount = 4;
        recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), spanCount));
        extra = getIntent().getIntExtra("address_extra", 0);
        maxDepth = getIntent().getIntExtra("address_depth", 3);
        if (maxDepth > 3) maxDepth = 3;

        recyclerView.setAdapter(new AddressAdapter());

        final int space = LocalDisplay.dp2px(15);
        recyclerView.setPadding(space, 0, space, space);
        recyclerView.addItemDecoration(new BlankSpace(space, spanCount));

        mAddressParser = AddressParser.getParser();
        //界面初始化
        if (!initDefault()) {
            switchToIndex(1, null);
        }
    }

    private void dismiss() {
        Intent intent = new Intent();
        final String[] keys = {"provinceS", "province", "cityS", "city", "countyS", "county"};
        if (province != null) {
            intent.putExtra(keys[1], province.getName());
            intent.putExtra(keys[0], province.getShortName());
        }

        if (city != null) {
            intent.putExtra(keys[3], city.getName());
            intent.putExtra(keys[2], city.getShortName());
        }

        if (county != null) {
            intent.putExtra(keys[4], county.getShortName());
            intent.putExtra(keys[5], county.getName());
        }

        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * 如果其他Activity传了默认值,那么就使用传递过来的参数初始化界面
     *
     * @return 返回false表示应该使用默认方式初始化界面
     */
    private boolean initDefault() {
        final String p = getIntent().getStringExtra("province");
        final String c = getIntent().getStringExtra("city");
        final String ccc = getIntent().getStringExtra("county");

        province = new AddressNode();
        province.setName(p);

        city = new AddressNode();
        city.setName(c);

        county = new AddressNode();
        city.setName(ccc);

//        switchToIndex(COUNTY, cc);
        return false;
    }

    private static class BlankSpace extends RecyclerView.ItemDecoration {
        private int space;
        private int spanCount;

        public BlankSpace(int space, int spanCount) {
            this.space = space;
            this.spanCount = spanCount;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            if (position % spanCount == 0) {
                outRect.set(0, space, 0, 0);
            } else {
                outRect.set(space, space, 0, 0);
            }
        }
    }

    /**
     * 单选状态点击某一个item:
     * 1.item未选中 => item选中
     * 2.item已选中 => item选中
     * <p/>
     * 多选状态点击某一个item:
     * 1.item未选中 => item选中
     * 2.item已选中 => item未选中
     */
    public void itemClicked(AddressNode item, int currentDepth) {
        switch (currentDepth) {
            case 1:
                province = item;
                break;
            case 2:
                city = item;
                break;
            case 3:
                county = item;
                dismiss();
                break;
        }

        if (!item.haveChild()) {
            dismiss();
            return;
        }

        switchToIndex(currentDepth + 1, item.getName());
    }

    /**
     * 切换到相应的tab
     *
     * @param index 1 => 省 2 => 市 3 => 区
     * @param code
     */
    private void switchToIndex(int index, String code) {
        if (index > maxDepth || index < 1)
            return;

        //改变省市区的选中状态
        changeStatus(index);
        //获取相应层级的数据
        ArrayList<AddressNode> cities = mAddressParser.getCities(code, this);
        AddressAdapter addressAdapter = (AddressAdapter) recyclerView.getAdapter();
        //更新相应层级的数据
        currentDepth = index;
        addressAdapter.update(cities);
    }

    private void changeStatus(int current) {
        province = current <= 1 ? null : province;
        city = current <= 2 ? null : city;
        county = current <= 3 ? null : county;

        previous.setVisibility(View.INVISIBLE);

        if (province == null) {
            selected.setText(R.string.select_province);
        } else {
            previous.setVisibility(View.VISIBLE);
            buildAddress();
        }
    }

    private void buildAddress() {
        String s = "";
        if (province != null) {
            s += province.getName() + " ";
            if (city != null) {
                if (!city.getName().contains(province.getName())) {
                    s += city.getName() + " ";
                }
                if (county != null) {
                    s += county.getName();
                }
            }
        }
        selected.setText(s);
    }

    @Override
    protected void onDestroy() {
        //清空缓存，避免共享缓存的问题
        AddressParser.getParser().clean();
        super.onDestroy();
    }

    private class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.AddressItemViewHolder> {
        private ArrayList<AddressNode> cities;

        @Override
        public AddressItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new AddressItemViewHolder(getLayoutInflater().inflate(R.layout.recycler_item_address, parent, false));
        }

        @Override
        public void onBindViewHolder(AddressItemViewHolder holder, int position) {
            holder.setData(cities.get(position));
        }

        @Override
        public int getItemCount() {
            if (cities != null)
                return cities.size();
            return 0;
        }

        public void update(ArrayList<AddressNode> cities) {
            this.cities = cities;
            String name = null;

            //在数据中插入全国，全省，全市这三个数据
            if (currentDepth == 1 && (extra & 0x01) == 0x01) {
                name = "-1";//全国
            }
            if (currentDepth == 2 && (extra & 0x02) == 0x02) {
                name = "-2";//全省
            }
            if (currentDepth == 3 && (extra & 0x04) == 0x04) {
                name = "-3";//全市
            }

            //在当前数据中查找看是否已存在要插入的数据，不存在则放入列表中
            if (name != null) {
                AddressNode mAddressNode = new AddressNode(name);
                if (!cities.contains(mAddressNode)) {
                    cities.add(0, mAddressNode);
                }
            }

            notifyDataSetChanged();
        }

        public class AddressItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private AddressNode addressNode;

            public AddressItemViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
            }

            public void setData(AddressNode addressNode) {
                this.addressNode = addressNode;
                CheckableText text = (CheckableText) itemView;
                if ("-1".equals(addressNode.getName())//全国
                        || "-2".equals(addressNode.getName())//全省
                        || "-3".equals(addressNode.getName())) {//全市
                    ViewUtil.setBackground(text, R.drawable.address_item_bg_special);
                    text.setTextColor(ContextCompat.getColor(getContext(),R.color.address_item_text_color_special));
                } else {
                    ViewUtil.setBackground(text, R.drawable.address_item_bg);
                    text.setTextColor(ContextCompat.getColor(getContext(),R.color.address_item_text_color));
                }
                text.setText(addressNode.getShortName());
            }

            @Override
            public void onClick(View v) {
                itemClicked(addressNode, currentDepth);
            }
        }
    }
}
