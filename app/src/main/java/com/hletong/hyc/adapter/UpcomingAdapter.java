package com.hletong.hyc.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.ReceiptContract;
import com.hletong.hyc.contract.UpcomingContract;
import com.hletong.hyc.model.PersonalCollection;
import com.hletong.hyc.model.QuoteUpcoming;
import com.hletong.hyc.model.Receipt;
import com.hletong.hyc.model.Upcoming;
import com.hletong.hyc.presenter.SourcePresenter;
import com.hletong.hyc.ui.activity.BookActivityBase;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.activity.MessageInfoActivity;
import com.hletong.hyc.ui.activity.QuoteProgressActivity;
import com.hletong.hyc.ui.activity.ReceiptUploadActivityNew;
import com.hletong.hyc.ui.activity.TransporterEvaluationActivity;
import com.hletong.hyc.ui.fragment.SourceDetailsFragment;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.orhanobut.logger.Logger;

/**
 * Created by ddq on 2016/12/14.
 * 消息-待办任务
 */
public class UpcomingAdapter extends HFRecyclerAdapter<Object> {
    private ITransactionView mTransactionView;
    private UpcomingContract.Presenter mPresenter;
    private int actionPosition = -1;

    public UpcomingAdapter(Context context, ITransactionView transactionView, UpcomingContract.Presenter presenter) {
        super(context, null);
        this.mTransactionView = transactionView;
        this.mPresenter = presenter;
    }

    @Override
    protected BaseHolder<Object> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getInflater().inflate(R.layout.recycler_item_message, parent, false);
        switch (viewType) {
            case 1:
                return new LegalViewHolder(view);
            case 2:
                return new ReceiptViewHolder(view);
        }
        return null;
    }

    @Override
    public int getViewType(int position) {
        Object object = getItem(position);
        if (object instanceof Upcoming) {
            return 1;
        } else if (object instanceof Receipt) {
            return 2;
        }
        return 0;
    }

    //收藏/取消操作成功，找到那一行记录，并更新状态
    public void collectStatusChanged(UpcomingContract.Action action) {
        if (!findActionItem(actionPosition, action)) {
            for (int i = 0; i < getDataCount(); i++) {
                if (findActionItem(i, action))
                    break;
            }
        }
    }

    private boolean findActionItem(int index, UpcomingContract.Action action) {
        Object object = getItem(index);
        if (object instanceof Upcoming) {
            Upcoming upcoming = (Upcoming) object;
            PersonalCollection pc = upcoming.getCollection();
            if (pc.getTradeUuid().equals(action.getTradeUuid())) {
                //设置状态
                pc.setStoredStatus(action.isCollect() ? 1 : 0);
                notifyItemChanged(index);
                return true;
            }
        }
        return false;
    }

    class ViewHolder<T> extends BaseHolder<T> implements View.OnClickListener {

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }
    }

    private class LegalViewHolder extends ViewHolder<Object> {

        public LegalViewHolder(View itemView) {
            super(itemView);
            itemView.findViewById(R.id.star).setOnClickListener(this);
        }

        @Override
        public void setData(Object data) {
            Upcoming upcoming = (Upcoming) data;
            ImageView iv = getView(R.id.star);
            if (upcoming.getType() == 200) {
                //货方自主交易确认，可以收藏
                iv.setVisibility(View.VISIBLE);
                PersonalCollection pc = upcoming.getCollection();
                if (pc.isCollected()) {
                    iv.setImageResource(R.drawable.ic_star);
                } else {
                    iv.setImageResource(R.drawable.ic_un_star);
                }
            } else {
                iv.setVisibility(View.GONE);
            }
            setText(R.id.tv_flag, upcoming.getShortType());
            setText(R.id.tv_flagText, upcoming.getType_());
            setText(R.id.tv_date, upcoming.getFormatDate());
            setText(R.id.tv_content, upcoming.getDescription());
        }

        @Override
        public void onClick(View v) {
            Upcoming upcoming = (Upcoming) getItem(getAdapterPosition());
            if (v.getId() == R.id.star && upcoming.getType() == 200) {
                actionPosition = getAdapterPosition();//记录操作的位置
                v.setTag(!upcoming.getCollection().isCollected());//这里添加一个tag，当操作完成之后，通过tag的值判断操作，控制View的变化
                mPresenter.collect(upcoming.getStringFromParams("tradeUuid"), upcoming.getCollection().isCollected());
                return;
            }

            /******************************************************************************************/
            /***** 以下内容有任何改动，请同步更新到{@link com.hletong.hyc.service.GTDataService}中  ********/
            /******************************************************************************************/
            String title = null;
            //自主交易结束确认
            if (upcoming.getType() == 201 || upcoming.getType() == 200) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", upcoming.getType() == 201 ? SourcePresenter.TRANSPORTER_UPCOMING_SELF_TRADE : SourcePresenter.CARGO_UPCOMING_SELF_TRADE);//类型为待办-自主交易
                bundle.putString("tradeUuid", upcoming.getStringFromParams("tradeUuid"));
                bundle.putString("confirmType", upcoming.getStringFromParams("confirmType"));
                mTransactionView.toActivity(
                        CommonWrapFragmentActivity.class,
                        CommonWrapFragmentActivity.getBundle("自主交易", SourceDetailsFragment.class, bundle),
                        SourceListAdapter.BaseViewHolder.REQUEST_CODE_FOR_LIST_ITEM,
                        null);
                return;
            }

            //车船-自主交易预摘牌
            if (upcoming.getType() == 203) {
                Bundle bundle = new Bundle();
                bundle.putInt("type", SourcePresenter.PRE_DE_LIST_SELF_TRADE);
                Logger.d("upcoming => " + upcoming.getStringFromParams("uniqueId"));
                bundle.putString("cargoUuid", upcoming.getStringFromParams("uniqueId"));
                mTransactionView.toActivity(
                        CommonWrapFragmentActivity.class,
                        CommonWrapFragmentActivity.getBundle("自主交易", SourceDetailsFragment.class, bundle),
                        null);
                return;
            }

            //货方会员-自主交易议价进度
            if (upcoming.getType() == 204) {
                Bundle bundle = new Bundle();
                bundle.putString("cargoUuid", upcoming.getQuoteEntity().getCargoUuid());
                mTransactionView.toActivity(QuoteProgressActivity.class, bundle, null);
                return;
            }

            //车船会员-自主交易报价进度
            if (upcoming.getType() == 205) {
                Intent intent = BookActivityBase.getBookActivity(getContext(), SourcePresenter.QUOTE_PROGRESS);
                if (intent == null)
                    return;

                QuoteUpcoming qu = upcoming.getQuoteEntity();
                intent.putExtra("cargoUuid", qu.getCargoUuid());
                intent.putExtra("quoteUuid", qu.getQuoteUuid());
                intent.putExtra("type", SourcePresenter.QUOTE_PROGRESS);
                mTransactionView.toActivity(intent, SourceListAdapter.BaseViewHolder.REQUEST_CODE_FOR_LIST_ITEM, null);
                return;
            }

            //会员评价
            if (upcoming.getType() == 300) {
                Intent intent = new Intent(getContext(), TransporterEvaluationActivity.class);
                intent.putExtra("tradeUuid", upcoming.getStringFromParams("tradeUuid"));
                intent.putExtra("billingType", upcoming.getStringFromParams("billingType"));
                mTransactionView.toActivity(intent, SourceListAdapter.BaseViewHolder.REQUEST_CODE_FOR_LIST_ITEM, null);
                return;
            }

            switch (upcoming.getType()) {
                case 11://违约单
                case 12://守约单
                    title = getString(R.string.legal_title);
                    break;
                case 14://滞压单
                    title = getString(R.string.stagnation_title);
                    break;
                case 61:
                case 62://卸货地变更,补签货方合同，车船确认
                    title = getString(R.string.resign_contract_title);
                    break;
            }

            Intent intent = new Intent(v.getContext(), MessageInfoActivity.class);
            intent.putExtra("title", title);
            intent.putExtra("upcoming", upcoming);
            intent.putExtra("content", MessageInfoActivity.TODO);
            mTransactionView.toActivity(intent, SourceListAdapter.BaseViewHolder.REQUEST_CODE_FOR_LIST_ITEM, null);
        }
    }

    private class ReceiptViewHolder extends ViewHolder<Object> {

        public ReceiptViewHolder(View itemView) {
            super(itemView);
        }

        public void setData(Object o) {
            Receipt receipt = (Receipt) o;
            setText(R.id.tv_flag, "运单");
            setText(R.id.tv_flagText, "运单回传");
            setText(R.id.tv_date, receipt.getCreate_ts());
            setText(R.id.tv_content, receipt.getContentDescription());
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), ReceiptUploadActivityNew.class);
            intent.putExtra("receipt", (Receipt) getItem(getAdapterPosition()));
            intent.putExtra("type", ReceiptContract.FROM_MESSAGE);
            mTransactionView.toActivity(intent, SourceListAdapter.BaseViewHolder.REQUEST_CODE_FOR_LIST_ITEM, null);
        }
    }
}
