package com.hletong.hyc.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hletong.hyc.R;
import com.hletong.hyc.model.PaperPhoto;
import com.hletong.hyc.model.RegisterPhoto;
import com.hletong.hyc.ui.activity.UploadPhotoActivity;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.DatePickerUtil;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.SimpleDate;
import com.xcheng.view.util.JumpUtil;
import com.xcheng.view.util.LocalDisplay;
import com.xcheng.view.util.ShapeBinder;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dongdaqing on 2017/5/31.
 * 国联议价车船方拨号历史
 */

public class PaperPhotoAdapter extends HFRecyclerAdapter<PaperPhoto> {

    public PaperPhotoAdapter(Context context, List<PaperPhoto> bankCards) {
        super(context, bankCards, Integer.MAX_VALUE);
    }

    @Override
    protected BaseHolder<PaperPhoto> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new PaperPhotoHolder(getInflater().inflate(R.layout.item_paper_photo, parent, false));
    }


    class PaperPhotoHolder extends BaseHolder<PaperPhoto> implements DatePickerUtil.OnDateSetListener {
        @BindView(R.id.iv_photo)
        ImageView iv_photo;
        @BindView(R.id.tv_photoName)
        TextView tv_photoName;
        @BindView(R.id.tv_photoTip)
        TextView tv_photoTip;

        @BindView(R.id.tv_endDate_select)
        TextView tv_endDate_select;
        DatePickerUtil datePickerUtil;
        PaperPhoto paperPhoto;

        private PaperPhotoHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            ShapeBinder.with(getContext(), R.color.common_white).stroke(ContextCompat.getColor(getContext(), R.color.themeColor)).strokeWidth(LocalDisplay.dp2px(0.8f)).drawableTo(itemView);
        }

        @Override
        public void setData(PaperPhoto data) {
            super.setData(data);
            paperPhoto = data;
            tv_photoName.setText(data.getValue());
            String photoPath = data.getRegisterPhoto().getPhotos()[0];
            if (photoPath != null) {
                Glide.with(getContext()).load(photoPath).fitCenter().into(iv_photo);
            } else {
                iv_photo.setImageResource(R.drawable.take_photo);
            }
            if (data.getDate() != null) {
                tv_endDate_select.setText(SimpleDate.format(data.getDate(), new SimpleDateFormat("yyyyMMdd", Locale.getDefault())));
            } else {
                tv_endDate_select.setText("");
            }
            RegisterPhoto registerPhoto = data.getRegisterPhoto();
            if (registerPhoto != null && registerPhoto.getFileGroupId() != null) {
                tv_photoTip.setText("照片已上传");
            } else {
                tv_photoTip.setText(R.string.upload_photo_please);
            }
        }

        @OnClick({R.id.delete, R.id.iv_photo, R.id.tv_endDate_select})
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.delete) {
                paperPhoto.reset();
                remove(getAdapterPosition());
            } else if (v.getId() == R.id.iv_photo) {
                RegisterPhoto photo = paperPhoto.getRegisterPhoto();
                photo.setIndex(getAdapterPosition());
                JumpUtil.toActivityForResult((Activity) getContext(), UploadPhotoActivity.class, Constant.PHOTO_ADD_REQUEST,
                        JumpUtil.getBundle(RegisterPhoto.class.getSimpleName(), photo));
            } else if (v.getId() == R.id.tv_endDate_select) {
                if (datePickerUtil == null) {
                    datePickerUtil = new DatePickerUtil(getContext(), this, Calendar.getInstance());
                }
                datePickerUtil.showDatePicker(((Activity) getContext()).getFragmentManager(), getAdapterPosition());
            }
        }

        @Override
        public void onDateSet(int year, int monthOfYear, int dayOfMonth, int tag) {
            SimpleDate simpleDate = new SimpleDate();
            simpleDate.setDate(year, monthOfYear, dayOfMonth);
            paperPhoto.setDate(simpleDate.dateString(true, ""));
            notifyItemChanged(getAdapterPosition());
        }
    }
}
