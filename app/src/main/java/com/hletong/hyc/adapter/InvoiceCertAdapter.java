package com.hletong.hyc.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.InvoiceCert;
import com.hletong.hyc.ui.activity.InvoiceCertInputActivity;
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

public class InvoiceCertAdapter extends HFRecyclerAdapter<InvoiceCert> {
    public InvoiceCertAdapter(Context context, List<InvoiceCert> invoiceCerts) {
        super(context, invoiceCerts, Integer.MAX_VALUE);
    }

    @Override
    protected BaseHolder<InvoiceCert> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new BankCardHolder(getInflater().inflate(R.layout.item_invoice_cert_edit, parent, false));
    }


    class BankCardHolder extends BaseHolder<InvoiceCert> {
        @BindView(R.id.cv_sprName)
        CommonInputView cv_sprName;
        @BindView(R.id.cv_address)
        CommonInputView cv_address;
        @BindView(R.id.cv_phoneNumber)
        CommonInputView cv_phoneNumber;

        @BindView(R.id.edit)
        TextView edit;
        @BindView(R.id.delete)
        TextView delete;
        @BindView(R.id.cb_default)
        TextView cbDefault;

        private BankCardHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ShapeBinder.with(getContext(), R.color.common_white).stroke(ContextCompat.getColor(getContext(), R.color.themeColor)).strokeWidth(LocalDisplay.dp2px(0.8f)).drawableTo(itemView);
        }

        @Override
        public void setData(InvoiceCert data) {
            super.setData(data);
            cv_sprName.setText(data.getSprName());
            if (data.getAddress() != null) {
                cv_address.setText(data.getAddress() );
            } else if (data.getProvince() != null) {
                cv_address.setText(data.getProvince() + " " + data.getCity() + " " + data.getCounty());
            }else{
                cv_address.setText("");
            }
            cv_phoneNumber.setText(data.getTel());
            cbDefault.setSelected(data.isDefault());
        }

        @OnClick({R.id.edit, R.id.delete, R.id.cb_default})
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.edit) {
                int pos = getAdapterPosition();
                InvoiceCert invoiceCert = getItem(pos);
                invoiceCert.setIndex(pos);
                JumpUtil.toActivityForResult((Activity) getContext(),
                        InvoiceCertInputActivity.class, Constant.EDIT_REQUEST, JumpUtil.getBundle(InvoiceCert.class.getSimpleName(), getItem(getAdapterPosition())));
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
