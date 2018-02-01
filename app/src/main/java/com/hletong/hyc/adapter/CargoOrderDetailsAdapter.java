package com.hletong.hyc.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.hletong.hyc.R;
import com.hletong.hyc.model.CargoSourceItem;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.NumberUtil;


/**
 * Created by dongdaqing on 2017/4/19.
 */

public class CargoOrderDetailsAdapter extends HFRecyclerAdapter<CargoSourceItem.TransporterInfo> {
    private int book_ref_type;
    private String unit;

    public CargoOrderDetailsAdapter(Context context, int bookRefType, String unit) {
        super(context, null);
        this.book_ref_type = bookRefType;
        this.unit = unit;
    }

    @Override
    protected BaseHolder<CargoSourceItem.TransporterInfo> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getInflater().inflate(R.layout.recycler_item_cargo_history_details, parent, false));
    }

    private class ViewHolder extends BaseHolder<CargoSourceItem.TransporterInfo> {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        public void setData(CargoSourceItem.TransporterInfo tp) {
            setText(R.id.title, tp.getCarrierNo());
            setText(R.id.cargo, (book_ref_type == 0 ? "承运重量：" : "承运数量：") + NumberUtil.format3f(tp.getBookCargo(book_ref_type)) + unit);
            setText(R.id.transport_loss, "运输损耗：" + tp.getLoss(book_ref_type) + unit);
            setText(R.id.transport_days, "运输天数：" + tp.getTransportDays());
            setText(R.id.breakC, "违约事项：" + tp.getLegalNum());
            setText(R.id.immediate, "及时性：" + tp.getTimeliness());
            setText(R.id.damage, "货损货差：" + tp.getDamage());
            setText(R.id.service, "服务态度：" + tp.getAttitude());
            setText(R.id.comment, "评价备注：" + tp.getRemark());

            setVisible(R.id.comment, !TextUtils.isEmpty(tp.getRemark()) ? View.VISIBLE : View.GONE);
        }
    }
}
