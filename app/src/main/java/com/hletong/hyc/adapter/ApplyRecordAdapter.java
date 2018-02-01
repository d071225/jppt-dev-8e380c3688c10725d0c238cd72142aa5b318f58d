package com.hletong.hyc.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.CancelApplyContract;
import com.hletong.hyc.model.ApplyRecordInfo;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class ApplyRecordAdapter extends HFRecyclerAdapter<ApplyRecordInfo> {

    public ApplyRecordAdapter(Context context, List<ApplyRecordInfo> data) {
        super(context, data);
    }

    @Override
    protected BaseHolder<ApplyRecordInfo> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new ApplyRecordHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item_apply_record, parent, false));
    }

    private class ApplyRecordHolder extends BaseHolder<ApplyRecordInfo> implements View.OnClickListener, CancelApplyContract.CancelApplyView {
        private ApplyRecordInfo data;
        private TextView mCancel_apply;

        public ApplyRecordHolder(View itemView) {
            super(itemView);
            mCancel_apply = itemView.findViewById(R.id.cancel_apply);
            mCancel_apply.setOnClickListener(this);
            itemView.findViewById(R.id.apply_progress).setOnClickListener(this);
        }

        public void setData(ApplyRecordInfo data) {
            this.data = data;
            setText(R.id.plate, data.getPlate());
            setText(R.id.date, data.getDate());
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.apply_progress:
                    break;
                case R.id.cancel_apply:
                    /**屏蔽撤销申请**/
//                    CancelApplyPresenter ApplyPresenter = new CancelApplyPresenter(this);
//                    ApplyPresenter.CancelApply();
                    break;
            }
        }

        @Override
        public void CancelSuccess() {
            DialogFactory.showAlert(getContext(), getString(R.string.cancel_apply_success), null, getString(R.string.ok));//
            mCancel_apply.setText(R.string.revoked);
            mCancel_apply.setEnabled(false);
        }

        @Override
        public void CancelDefeat() {
            DialogFactory.showAlert(getContext(), getString(R.string.cancel_apply_default), null, getString(R.string.known));
        }

        public JSONObject getJsonObject() {
            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject.put("plate", data.getPlate());
                jsonObject.put("memberCode", LoginInfo.getLoginInfo().getMember_code());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return jsonObject;
        }
    }
}
