package com.hletong.hyc.adapter;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.SourceCarryInfo;
import com.hletong.hyc.model.SourceTrack;
import com.hletong.hyc.ui.activity.WebActivity;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

import java.util.List;

/**
 * Created by ddq on 2017/3/8.
 */

public class SourceTrackAdapter extends HFRecyclerAdapter<SourceTrack> {
    private DataManipulate mDataManipulate;
    private MakePhoneCall mCallContact;
    private Source mSource;

    public SourceTrackAdapter(Context context, DataManipulate dataManipulate, MakePhoneCall callContact, Source source) {
        super(context, null);
        this.mDataManipulate = dataManipulate;
        this.mCallContact = callContact;
        this.mSource = source;
    }

    @Override
    protected BaseHolder<SourceTrack> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 1://title
                return new TitleViewHolder(getInflater().inflate(R.layout.recycler_item_cargo_track_title, parent, false));
            case 2://status
                return new StatusViewHolder(getInflater().inflate(R.layout.recycler_item_cargo_track_status, parent, false));
            case 3://item
                return new ItemViewHolder(getInflater().inflate(R.layout.recycler_item_cargo_track_item, parent, false));
            case 4://expand
                return new ExpandViewHolder(getInflater().inflate(R.layout.recycler_item_cargo_track_expand, parent, false));
            default:
                throw new IllegalArgumentException("类型 => " + viewType + " 没有在SourceTrack中定义，请检查");
        }
    }

    @Override
    public int getViewType(int position) {
        return getItem(position).getType();
    }

    public interface DataManipulate {
        /**
         * @param data
         * @param parent
         * @param status 0 => 收起，1=> 展开
         */
        void manipulate(List<SourceTrack> data, SourceTrack.Status parent, int status);
    }

    public abstract static class BaseViewHolder extends BaseHolder<SourceTrack> implements View.OnClickListener {
        private int type;

        public BaseViewHolder(View itemView, int type) {
            super(itemView);
            this.type = type;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

        }

        public int getType() {
            return type;
        }
    }

    private class TitleViewHolder extends BaseViewHolder {

        public TitleViewHolder(View itemView) {
            super(itemView, 1);
        }

        public void setData(SourceTrack sourceTrack) {
            TextView textView = (TextView) itemView;
            textView.setText(sourceTrack.getMessage());
        }
    }

    private class StatusViewHolder extends BaseViewHolder {
        private TextView ss;
        private TextView se;

        public StatusViewHolder(View itemView) {
            super(itemView, 2);
            ss = (TextView) itemView.findViewById(R.id.status_start);
            se = (TextView) itemView.findViewById(R.id.status_end);
        }

        public void setData(SourceTrack sourceTrack) {
            ss.setText(sourceTrack.getStatus().getStatus());
            se.setText(sourceTrack.getStatus().getTransportInfo());
        }
    }

    private class ItemViewHolder extends BaseViewHolder {
        private TextView cargo;
        private TextView time;
        private SourceCarryInfo mCarryInfo;

        public ItemViewHolder(View itemView) {
            super(itemView, 3);
            cargo = (TextView) itemView.findViewById(R.id.cargo);
            time = (TextView) itemView.findViewById(R.id.time);
            TextView track = (TextView) itemView.findViewById(R.id.bl);
            //只有 车辆运输 同时 非自主交易 的情况才有车辆跟踪
            if (mSource.getTransport_type() == 1) {
                track.setText("车辆跟踪");
                track.setOnClickListener(this);
            } else {
                track.setVisibility(View.GONE);
            }

            TextView call = (TextView) itemView.findViewById(R.id.br);
            if (mSource.getBilling_type() == 3) {//只有自主交易才能打电话
                if (mSource.getTransport_type() == 1) {
                    call.setText("联系车主");
                } else {
                    call.setText("联系船主");
                }
                call.setOnClickListener(this);
            } else {
                call.setVisibility(View.GONE);
            }
        }

        public void setData(SourceTrack sourceTrack) {
            this.mCarryInfo = sourceTrack.getCarryInfo();
            cargo.setText(sourceTrack.getCarryInfo().getCarrierACount());
            time.setText(sourceTrack.getCarryInfo().getTimeByStatus(sourceTrack.getParent().getStatusCode()));
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.bl) {
                //车辆跟踪
                Bundle bundle = new Bundle();
                bundle.putString("url",
                        "/page/public/hletong/template/CdmWayMap/appMapTrack?jsonParam=" +
                                mSource.getLoading_province() + "%7C" +
                                mSource.getLoading_city() + "%7C" +
                                mSource.getLoading_country() + "%7C" +
                                mSource.getUnload_province() + "%7C" +
                                mSource.getUnload_city() + "%7C" +
                                mSource.getUnload_country() + "%7C" +
                                mCarryInfo.getCarrierNo() + "%7C" +
                                mCarryInfo.getInvoiceUploadDttm() + "%7C" +
                                mCarryInfo.getReceiptUploadDttm());
                toActivity(WebActivity.class, bundle);
            } else if (v.getId() == R.id.br) {
                //联系车主
                SourceCarryInfo sci = getItem(getAdapterPosition()).getCarryInfo();
                mCallContact.call(sci.getCarrierChargeName(), sci.getCarrierChargePhone(), null);
            }
        }
    }

    private class ExpandViewHolder extends BaseViewHolder {
        private ImageView mImageView;
        private TextView mTextView;
        private int oppositeStatus;
        private SourceTrack.Status parent;

        public ExpandViewHolder(View itemView) {
            super(itemView, 4);
            mImageView = (ImageView) itemView.findViewById(R.id.image);
            mTextView = (TextView) itemView.findViewById(R.id.text);
        }

        public void setData(SourceTrack sourceTrack) {
            parent = sourceTrack.getParent();
            oppositeStatus = sourceTrack.getExpand().getOppositeStatus();
            mImageView.setImageResource(sourceTrack.getExpand().getImage());
            mTextView.setText(sourceTrack.getExpand().getText());
        }

        @Override
        public void onClick(View v) {
            mDataManipulate.manipulate(getData(), parent, oppositeStatus);
        }
    }
}
