package com.hletong.hyc.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.CollectionContract;
import com.hletong.hyc.model.CarrierCollection;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.fragment.CarrierHistoryFragment;
import com.hletong.hyc.ui.widget.RatingBar;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;

/**
 * Created by ddq on 2017/3/27.
 * 货方会员收藏的车船
 */

public class CarrierCollectionAdapter extends HFRecyclerAdapter<CarrierCollection> {
    private CollectionContract.Presenter mPresenter;
    private MakePhoneCall mCallContact;
    private ITransactionView mTransactionView;
    private String cargoUuid;

    public CarrierCollectionAdapter(Context context, CollectionContract.Presenter presenter, MakePhoneCall callContact, ITransactionView delegate, String cargoUuid) {
        super(context, null);
        this.mPresenter = presenter;
        this.mCallContact = callContact;
        this.mTransactionView = delegate;
        this.cargoUuid = cargoUuid;
    }

    @Override
    protected BaseHolder<CarrierCollection> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getInflater().inflate(R.layout.recycler_item_carrier_collection, parent, false));
    }

    private class ViewHolder extends BaseHolder<CarrierCollection> implements View.OnClickListener {
        private RatingBar mRatingBar;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.findViewById(R.id.call).setOnClickListener(this);
            itemView.findViewById(R.id.inform).setOnClickListener(this);
            mRatingBar = getView(R.id.rate);
            mRatingBar.setVisibility(View.VISIBLE);
        }

        public void setData(CarrierCollection cc) {
            setText(R.id.carrier, cc.getCarrierWithNo());
            setText(R.id.model, cc.getModel());
            setText(R.id.favor_rate, cc.getMemberGradeWithUnit());
            setText(R.id.trade_ct, cc.getTradeCountWithUnit());
            TextView tv = getView(R.id.inform);
            tv.setEnabled(cc.canInform());
            if (cc.canInform()) {
                tv.setText("通知摘牌");
            } else {
                tv.setText("已通知");
            }
            mRatingBar.setProgress(cc.getMemberGrade() / 100.0f);
        }

        @Override
        public void onClick(View v) {
            CarrierCollection cc = getItem(getAdapterPosition());
            switch (v.getId()) {
                case R.id.call:
                    mCallContact.call(cc.getVehicles(), cc.getContactTel(), null);
                    break;
                case R.id.inform:
                    mPresenter.inform(new ParamsHelper()
                            .put("cargoUuid", cargoUuid)
                            .put("storedUuid", cc.getStoredUuid())
                            .put("vehicles", cc.getVehicles())
                            .put("carrierMemberCode", cc.getCarrierMemberCode()));
                    break;
                default:
                    mTransactionView.toActivity(
                            CommonWrapFragmentActivity.class,
                            CommonWrapFragmentActivity.getBundle(
                                    "承运历史",
                                    CarrierHistoryFragment.class,
                                    CarrierHistoryFragment.getParam(cc.getVehicles(), cc.getTradeUuid())),
                            SourceListAdapter.BaseViewHolder.REQUEST_CODE_FOR_LIST_ITEM,
                            null);
                    break;
            }
        }
    }
}
