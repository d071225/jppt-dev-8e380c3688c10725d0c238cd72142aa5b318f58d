package com.hletong.hyc.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.hletong.hyc.R;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.model.ContractItemForHistoryList;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.ui.activity.ThirdPartyContractActivity;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

import java.util.List;

/**
 * Created by chengxin on 16/9/27.
 * 历史承运合同列表
 */
public class ContractHistoryAdapter extends HFRecyclerAdapter<ContractItemForHistoryList> {
    private ITransactionView delegate;

    public ContractHistoryAdapter(Context context, List<ContractItemForHistoryList> items, ITransactionView delegate) {
        super(context, items);//这里的20不能改成其他数字
        this.delegate = delegate;
    }

    @Override
    protected BaseHolder<ContractItemForHistoryList> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new ContractViewHolder(new StatusItemUtil().wrapView(getInflater().inflate(R.layout.recycler_item_source, parent, false)), delegate);
    }

    @Override
    public int getViewType(int position) {
        return getItem(position).getRound_trans_type();
    }

    static class ContractViewHolder extends SourceListAdapter.QuoteViewHolder<ContractItemForHistoryList> {

        ContractViewHolder(View itemView, ITransactionView delegate) {
            super(itemView, delegate);
            itemView.setOnClickListener(null);//不要全体点击事件
            setText(R.id.action, R.string.view);
            setVisible(R.id.flag, View.GONE);
        }

        private int type;
        private String contractUuid;

        public void setData(ContractItemForHistoryList contractItem) {
            this.type = contractItem.getBilling_type();
            this.contractUuid = contractItem.getContract_uuid();
            super.setData(contractItem);
            StatusItemUtil mStatusItemUtil = (StatusItemUtil) itemView.getTag();
            mStatusItemUtil.setData("签约成功", contractItem.getAvailableTime());
        }

        @Override
        protected String getCargoDesc(Source source) {
            return source.getCargoDescWithUnit();
        }

        @Override
        protected String getTimeLabelDesc(Source source) {
            if (source.getPlate() != null)
                return source.getPlate() + " " + super.getTimeLabelDesc(source);
            return super.getTimeLabelDesc(source);
        }

        @Override
        public void onClick(View v) {
            if (type == 3) {//自主交易
                Bundle bundle = new Bundle();
                bundle.putString("contractUuid", contractUuid);
                toActivity(ThirdPartyContractActivity.class, bundle);
            } else {//非自主交易
                Intent intent = new Intent(v.getContext(), AppTypeConfig.getContractActivity());
                intent.putExtra("title", "查看合同");
                intent.putExtra("contractUuid", contractUuid);
                startActivityForResult(intent);
            }
        }
    }
}
