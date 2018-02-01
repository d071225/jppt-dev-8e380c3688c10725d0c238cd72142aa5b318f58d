package com.hletong.hyc.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.ETCCard;
import com.hletong.hyc.ui.activity.CardDetailActivity;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.mob.dialog.selector.SelectAdapter;
import com.hletong.mob.pullrefresh.BaseHolder;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SelectCardAdapter extends SelectAdapter<ETCCard> {

    private OnCardSelected mOnCardSelected;

    public SelectCardAdapter(Context context, List<ETCCard> data, OnCardSelected onCardSelected) {
        super(context, data);
        mOnCardSelected = onCardSelected;
        setSelected(0);
    }

    @Override
    protected BaseHolder<ETCCard> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new SelectCardViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_etc_card, parent, false));
    }

    class SelectCardViewHolder extends BaseHolder<ETCCard> implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {
        @BindView(R.id.iv_hlCard)
        ImageView hlCard;
        @BindView(R.id.tv_cardName)
        TextView cardName;
        @BindView(R.id.tv_cardIntroduce)
        TextView desc;
        @BindView(R.id.tv_detail)
        TextView detail;
        @BindView(R.id.cb)
        CheckBox cb;

        SelectCardViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(ETCCard etcCard) {
            cardName.setText(etcCard.getTitle());
            hlCard.setImageResource(etcCard.getAvatar());
            desc.setText(etcCard.getContent());

            cardName.setEnabled(etcCard.isAvailable());
            desc.setEnabled(etcCard.isAvailable());


            detail.setVisibility(View.VISIBLE);
            detail.setOnClickListener(this);
            cb.setVisibility(getAdapterPosition() == 0 ? View.VISIBLE : View.INVISIBLE);
            cb.setOnCheckedChangeListener(this);
            cb.setChecked(isSelected(getAdapterPosition()));

        }

        @Override
        public void onClick(View v) {
            if (getAdapterPosition() == 2) {
                DialogFactory.showAlert(getContext(), null, "1、免收年费、账户管理费\n" +
                        "2、免收柜台存、取现手续费\n" +
                        "3、免收柜台本行、跨行转账手续费\n" +
                        "4、免收建行自助设备存、取现手续费\n" +
                        "5、免收建行自助设备本行、跨行转账手续费\n" +
                        "6、平台现金结算运费快\n" +
                        "7、所有结算流水可增加您信用额度！\n" +
                        "8、平台协同各运营商创新的超值服务应用（园区停车住宿餐饮、加油加气、轮胎、润滑油、车用尿素等优惠），让您更省钱！", true, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            } else {
                Bundle bundle = new Bundle();
                bundle.putParcelable("etc", getItem(getAdapterPosition()));
                bundle.putInt("position", getAdapterPosition());
                toActivity(CardDetailActivity.class, bundle);
            }

        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (mOnCardSelected != null)
                mOnCardSelected.cardSelected(getItem(getAdapterPosition()), isChecked);
        }
    }

    public interface OnCardSelected {
        void cardSelected(ETCCard etcCard, boolean isChecked);
    }
}


