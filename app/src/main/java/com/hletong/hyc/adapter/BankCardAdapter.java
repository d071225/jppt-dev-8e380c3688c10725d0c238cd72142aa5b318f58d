package com.hletong.hyc.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.BankCard;
import com.hletong.hyc.ui.activity.BankCardInputActivity;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.xcheng.view.util.JumpUtil;
import com.xcheng.view.util.LocalDisplay;
import com.xcheng.view.util.ShapeBinder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dongdaqing on 2017/5/31.
 * 国联议价车船方拨号历史
 */

public class BankCardAdapter extends HFRecyclerAdapter<BankCard> {
    public BankCardAdapter(Context context, List<BankCard> bankCards) {
        super(context, bankCards, Integer.MAX_VALUE);
    }

    @Override
    protected BaseHolder<BankCard> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new BankCardHolder(getInflater().inflate(R.layout.item_bankcard_edit, parent, false));
    }


    class BankCardHolder extends BaseHolder<BankCard> {
        @BindView(R.id.cv_cardBank)
        CommonInputView cv_cardBank;
        @BindView(R.id.cv_cardNumber)
        CommonInputView cv_cardNumber;

        @BindView(R.id.cb_default)
        TextView cbDefault;
        @BindView(R.id.edit)
        TextView edit;
        @BindView(R.id.delete)
        TextView delete;

        private BankCardHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ShapeBinder.with(getContext(), R.color.common_white).stroke(ContextCompat.getColor(getContext(), R.color.themeColor)).strokeWidth(LocalDisplay.dp2px(0.8f)).drawableTo(itemView);
        }

        @Override
        public void setData(BankCard data) {
            super.setData(data);
            cv_cardBank.setText(data.getBankEnum().getText());
            cv_cardNumber.setText(data.getBankCode());
            cbDefault.setSelected(data.isDefault());
        }

        @OnClick({R.id.edit, R.id.delete, R.id.cb_default})
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.edit) {
                int pos = getAdapterPosition();
                BankCard card = getItem(pos);
                card.setIndex(pos);
                JumpUtil.toActivityForResult((Activity) getContext(), BankCardInputActivity.class, Constant.EDIT_REQUEST, JumpUtil.getBundle(BankCard.class.getSimpleName(), getItem(getAdapterPosition())));
            } else if (v.getId() == R.id.delete) {
                remove(getAdapterPosition());
            } else if (v.getId() == R.id.cb_default) {
                for (int index = 0; index < getDataCount(); index++) {
                    getItem(index).setDefault(index == getAdapterPosition());
                }
                notifyDataSetChanged();
            }
        }
    }
}
