package com.hletong.hyc.presenter;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.util.ArrayMap;
import android.support.v4.util.SimpleArrayMap;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.SourceTrackContract;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.SourceCarryInfo;
import com.hletong.hyc.model.SourceInvoice;
import com.hletong.hyc.model.SourceTrack;
import com.hletong.hyc.model.SourceTrade;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.error.DataError;
import com.hletong.mob.architect.error.ErrorState;
import com.hletong.mob.architect.model.repository.SimpleCallback;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.requestvalue.ListRequestValue;
import com.xcheng.okhttp.util.ParamUtil;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ddq on 2017/3/8.
 */

public class SourceTrackPresenter extends BasePresenter<SourceTrackContract.View> implements SourceTrackContract.Presenter {
    private Source mSource;
    private SourceInvoice mSourceInvoice;//开票信息
    private ArrayMap<String, ArrayList<SourceCarryInfo>> mMap;
    private SimpleArrayMap<String, String> mStatus;

    public SourceTrackPresenter(SourceTrackContract.View view, Source source) {
        super(view);
        mSource = source;
    }

    @Override
    public void start() {
        mStatus = new SimpleArrayMap<>();
        mStatus.put("00", "待装货");
        mStatus.put("10", "装货中");
        mStatus.put("20", "运输中");
        mStatus.put("30", "待卸货");
        mStatus.put("40", "卸货中");
        mStatus.put("50", "运费结算中");
        mStatus.put("99", "交易完成");
        mStatus.put("100", "已摘牌");
        mStatus.put("A0", "已撤销");
        mStatus.put("A9", "已过期");

        if (mSource != null)
            try {
                getView().initSourceView(mSource);
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }

    @Override
    public void expandData(List<SourceTrack> data, SourceTrack.Status parent) {
        //展开某一块数据
        int index = -1;
        for (int i = 0; i < data.size(); i++) {
            SourceTrack st = data.get(i);
            //找到用户点击的数据，记录其位置
            if (st.getParent() == parent && st.getType() == 4) {
                st.getExpand().update("收起", R.drawable.ic_shrink, 1);
                index = i;
                break;
            }
        }
        getView().notifyItemChanged(index);
        int start = index;
        //将剩下的数据依次插入到记录的位置之前
        ArrayList<SourceCarryInfo> cached = mMap.get(parent.getStatusCode());
        for (int i = 3; i < cached.size(); i++) {
            data.add(index++, new SourceTrack(cached.get(i), parent));
        }
        getView().notifyItemInsertRange(start, index - start);
    }

    @Override
    public void shrinkData(List<SourceTrack> data, SourceTrack.Status parent) {
        //收缩某一块数据
        int index = -1;
        for (int i = 0; i < data.size(); i++) {
            SourceTrack st = data.get(i);
            //找到用户点击的数据，记录其位置
            if (st.getParent() == parent && st.getType() == 4) {
                st.getExpand().update("展开全部" + (mSource.getTransport_type() == 1 ? "车辆" : "船舶"), R.drawable.ic_expand, 0);
                index = i;
                break;
            }
        }

        int end = index - 1;
        //将记录的位置之前的多余的数据移除（收起后只展示3个Item）
        for (int i = parent.getCarNumber() - 3; i > 0; i--) {
            data.remove(--index);//要移除的是用户点击的Item之前的数据，而不是Item本身，故要先减一
        }
        getView().notifyItemRemoveRange(index, end + 1 - index);
        getView().notifyItemChanged(index);
    }

    @Override
    public void prepareData(Bundle bundle) {
        bundle.putParcelable("SourceInvoice", mSourceInvoice);
    }

    @Override
    public void loadTrackInfo(final ItemRequestValue<SourceTrade> requestValue) {
        requestValue.setRequestFlag(ListRequestValue.REFRESH);
        getDataRepository().loadItem(requestValue, new SimpleCallback<SourceTrade>(getView()) {
            @Override
            public void onSuccess(@NonNull SourceTrade response) {
                mSourceInvoice = response.getInvoiceInfo();
                if (!ParamUtil.isEmpty(response.getCarryList())) {
                    getView().showList(sortData(response.getCarryList()), true);
                } else {
                    BaseError error = new DataError(ErrorState.NO_DATA);
                    error.setId(ListRequestValue.REFRESH);
                    onError(error);
                }
            }
        });
    }

    //@formatter:off
    /**
     *
     * 界面上要展示的数据根据不同的类型(待装货，已摘牌，已完成等)分为不同的类别，但是服务端返给我们的数据
     * 并没有进行分类，这里将数据根据不同类型进行分类，然后直接展示，所有的类别定义在{@link #mStatus}里面
     * <p>
     * 整体界面的构成（待装货例子）
     * ——————————————————————————————————————————————————————————————————————————————————————
     *                ————————————————————————————————————————
     * 1.状态栏       |待装货                       共4辆车，43吨|
     *                ————————————————————————————————————————
     *               |                                        |
     *               |  苏LQQ123(16吨)                         |
     * 2.具体内容区域  |                                        |
     *               |  摘牌时间：5月12日10:51:10               |
     *               |————————————————————————————————————————
     *               |                     |车辆跟踪 | 联系车主 |
     *                ————————————————————————————————————————
     *                                    .
     *                                    .
     *                                    .
     * 3.内容展开(收起)区域             展开全部车辆(或者是"收起")
     * <p>
     * 其中3只有在2的个数多余3个的时候才会出现
     * 下面的函数里面构造数据用于展示，就是根据上面的顺序来的
     *
     * @param carryInfos
     * @return
     */
    //@formatter:on
    private ArrayList<SourceTrack> sortData(ArrayList<SourceCarryInfo> carryInfos) {
        //将数据进行分类
        for (SourceCarryInfo carryInfo : carryInfos) {
            carryInfo.setExtra(mSource.getBook_ref_type(), mSource.getCargoUnit());//设置计费依据
            if (mMap == null)
                mMap = new ArrayMap<>();

            //将数据放到同一状态的列表里面
            ArrayList<SourceCarryInfo> sourceCarryInfos = mMap.get(carryInfo.getStatus());
            if (sourceCarryInfos == null) {
                sourceCarryInfos = new ArrayList<>();
                mMap.put(carryInfo.getStatus(), sourceCarryInfos);
            }

            sourceCarryInfos.add(carryInfo);
        }

        //构造用于展示的数据
        ArrayList<SourceTrack> data = new ArrayList<>();
        data.add(new SourceTrack());//标题

        for (Map.Entry<String, ArrayList<SourceCarryInfo>> entry : mMap.entrySet()) {
            ArrayList<SourceCarryInfo> sourceCarryInfos = entry.getValue();
            if (!ParamUtil.isEmpty(entry.getValue())) {
                //当前状态的数据非空至少包含一条数据

                //创建状态栏，函数注释里面的1
                final String status = mStatus.get(entry.getKey());//获取对应的状态中文说明(就是mStatus里面的中文)
                SourceTrack sourceTrack = new SourceTrack(mSource.getTransport_type(),
                        //这里做一下说明，本身SourceCarryInfo里面的status_是status对应的说明，这里优先取本地的对照说明，
                        // 如果取不到（服务端新增了状态）就取默认的status_
                        status != null ? status : sourceCarryInfos.get(0).getStatus_(),
                        entry.getKey(),
                        sourceCarryInfos.size(),
                        0,
                        mSource.getCargoUnit());
                data.add(sourceTrack);
                double totalCount = 0;
                //创建具体的数据，函数注释里面的2
                for (int i = 0; i < sourceCarryInfos.size(); i++) {
                    SourceCarryInfo info = sourceCarryInfos.get(i);
                    totalCount += info.getBookCount();
                    data.add(new SourceTrack(sourceCarryInfos.get(i), sourceTrack.getStatus()));
                    if (i == 2 && sourceCarryInfos.size() > 3) {
                        //函数注释里面的3
                        data.add(new SourceTrack("展开全部" + (mSource.getTransport_type() == 1 ? "车辆" : "船舶"), R.drawable.ic_expand, sourceTrack.getStatus()));
                        //计算货物总量
                        for (int j = 3; j < sourceCarryInfos.size(); j++) {
                            totalCount += sourceCarryInfos.get(j).getBookCount();
                        }
                        break;
                    }
                }
                //状态栏(1)的货物总量描述
                sourceTrack.getStatus().setCargoDesc(totalCount);//设置货物总量
            }
        }
        return data;
    }
}
