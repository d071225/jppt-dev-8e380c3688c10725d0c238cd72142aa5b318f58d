package com.hletong.hyc.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.TradeType;
import com.hletong.hyc.ui.activity.cargo.CargoCompanyCertActivity;
import com.hletong.hyc.ui.activity.cargo.CargoPersonalCertActivity;
import com.hletong.mob.architect.view.IDialogView;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

import java.util.List;

/**
 * Created by ddq on 2017/3/13.
 */

public class TradeTypeAdapter extends HFRecyclerAdapter<TradeType> {
    private int selected = -1;
    private ITransactionView mTransactionView;
    private IDialogView mDialogView;

    public TradeTypeAdapter(Context context, ITransactionView transactionView, IDialogView dialogView, List<TradeType> data, int selected) {
        super(context, data);
        this.selected = selected;
        mTransactionView = transactionView;
        mDialogView = dialogView;
    }

    @Override
    protected BaseHolder<TradeType> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = getInflater().inflate(R.layout.recycler_item_trade_type, parent, false);
        if (viewType == 1)
            return new Normal(view);
        else {
            LinearLayout linearLayout = new LinearLayout(getContext());
            linearLayout.setOrientation(LinearLayout.VERTICAL);
            linearLayout.addView(view);
            return new Disabled(getInflater().inflate(R.layout.recycler_item_trade_type_extra, linearLayout, true));
        }
    }

    @Override
    public int getViewType(int position) {
        if (getItem(position).isAvailable())
            return 1;
        return 2;
    }

    private class Normal extends BaseHolder<TradeType> implements View.OnClickListener {
        private TextView title;
        private TextView content;
        private ImageView image;
        private TextView status;
        private TradeType mTradeType;

        public Normal(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.title);
            content = (TextView) itemView.findViewById(R.id.content);
            image = (ImageView) itemView.findViewById(R.id.image);
            status = (TextView) itemView.findViewById(R.id.status);
        }

        public void setData(TradeType tradeType) {
            this.mTradeType = tradeType;
            title.setText(tradeType.getTitle());
            content.setText(tradeType.getDescription());

            status.setVisibility(View.VISIBLE);
            if (tradeType.isNeedUploadPaper())
                status.setText("待认证");
            else if (tradeType.isReviewing())
                status.setText("审核中");
            else if (tradeType.isNotAuthorized()){
                status.setText("未授权");
            }else {
                status.setVisibility(View.GONE);
            }
            boolean isSelected = tradeType.isAvailable() && tradeType.getType() == TradeTypeAdapter.this.selected;
            title.setSelected(isSelected);
            content.setSelected(isSelected);
            image.setSelected(isSelected);
        }

        @Override
        public void onClick(View v) {
            Activity activity = (Activity) v.getContext();
            Intent intent = new Intent();
            intent.putExtra("tradeType", mTradeType);
            activity.setResult(Activity.RESULT_OK, intent);
            activity.finish();
        }

        public TradeType getTradeType() {
            return mTradeType;
        }
    }

    private class Disabled extends Normal {
        private TextView extraText;

        public Disabled(View itemView) {
            super(itemView);
            extraText = (TextView) itemView.findViewById(R.id.extra_text);
        }

        @Override
        public void setData(TradeType tradeType) {
            super.setData(tradeType);
            extraText.setText(tradeType.getDisableMessage());
        }

        @Override
        public void onClick(View v) {
            if (getTradeType().isNeedUploadPaper()){
                mDialogView.showDialog(false, null, "尊敬的会员：请您签订入会协议书，并补充相关证件资料和用户信息", "去签订", "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (AppTypeConfig.isCargo()) {
                            if (LoginInfo.isCompany()) {
                                mTransactionView.toActivity(CargoCompanyCertActivity.class, null, null);
                            } else {
                                mTransactionView.toActivity(CargoPersonalCertActivity.class, null, null);
                            }
                        }
                    }
                }, null);
            }
        }
    }
}
