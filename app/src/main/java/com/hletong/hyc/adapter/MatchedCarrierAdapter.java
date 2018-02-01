package com.hletong.hyc.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.hletong.hyc.R;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.MatchedCarrier;
import com.hletong.hyc.ui.activity.MatchedCarrierDetailsActivity;
import com.hletong.hyc.ui.widget.RatingBar;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

/**
 * Created by ddq on 2017/3/27.
 * 匹配车船
 */

public class MatchedCarrierAdapter extends HFRecyclerAdapter<MatchedCarrier> {
    private MakePhoneCall mMakePhoneCall;

    public MatchedCarrierAdapter(Context context, MakePhoneCall callContact) {
        super(context, null);
        this.mMakePhoneCall = callContact;
    }

    @Override
    protected BaseHolder<MatchedCarrier> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1:
                return new ForecastViewHolder(getInflater().inflate(R.layout.recycler_item_matched_carrier_forecast_carrier, parent, false));
            case 2:
                return new MatchedViewHolder(getInflater().inflate(R.layout.recycler_item_matched_carrier_located_carrier, parent, false));
        }
        return null;
    }

    @Override
    public int getViewType(int position) {
        if (getItem(position).getForecastUuid() != null)
            return 1;
        return 2;
    }

    private class ForecastViewHolder extends MatchedViewHolder {

        public ForecastViewHolder(View itemView) {
            super(itemView);
        }

        public void setData(MatchedCarrier fti) {
            setCommonView(fti);
            setText(R.id.startProvince, Address.chooseCity(fti.getOriginProvince(), fti.getOriginCity()));//不是显示省，而是市
            setText(R.id.startCity, fti.getOriginCountry());//不是显示市，而是区
            setText(R.id.endProvince, Address.chooseCity(fti.getDestinationProvince(), fti.getDestinationCity()));//不是显示省，而是市
            setText(R.id.endCity, fti.getDestinationCountry());//不是显示市，而是区
        }
    }

    private class MatchedViewHolder extends BaseHolder<MatchedCarrier> implements View.OnClickListener {

        public MatchedViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.findViewById(R.id.call).setOnClickListener(this);
        }

        public void setData(MatchedCarrier fti) {
            setCommonView(fti);
            setText(R.id.address, fti.getForecastUuid() == null ? fti.getNowAddress() : fti.getOriginAddress().buildAddress());
        }

        final void setCommonView(MatchedCarrier matchedCarrier) {
            setText(R.id.carrier, matchedCarrier.getCarrierNo());
            setText(R.id.favor_rate, matchedCarrier.getMemberGrade());
            setText(R.id.trade_ct, matchedCarrier.getTradeAccount());
            RatingBar ratingBar = getView(R.id.rate);
            ratingBar.setProgress(matchedCarrier.getMemberGradeAsFloat());
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.call) {
                MatchedCarrier fti = getItem(getAdapterPosition());
                mMakePhoneCall.call(fti.getCarrierNo(), fti.getContactTel(), null);
            } else {
                Bundle bundle = new Bundle();
                bundle.putParcelable("fti", getItem(getAdapterPosition()));
                toActivity(MatchedCarrierDetailsActivity.class, bundle);
            }
        }
    }
}
