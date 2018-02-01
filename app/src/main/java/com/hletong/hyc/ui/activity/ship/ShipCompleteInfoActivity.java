package com.hletong.hyc.ui.activity.ship;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.InfoCompleteContract;
import com.hletong.hyc.model.PaperPhoto;
import com.hletong.hyc.model.RegisterPhoto;
import com.hletong.hyc.model.validate.Paperwork;
import com.hletong.hyc.model.validate.ship.ShipVerifyInfo;
import com.hletong.hyc.presenter.InfoCompletePresenter;
import com.hletong.hyc.ui.activity.UploadPhotoActivity;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.util.DatePickerUtil;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.dialog.selector.BottomSelectorDialog;
import com.hletong.mob.util.SimpleDate;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hletong.hyc.model.validate.ship.RegisterPersonalShipInfo.ShipInfo;

/**
 * 车主版个人信息补全
 * Created by chengxin on 2017/6/12.
 */
public class ShipCompleteInfoActivity extends BaseActivity
//        implements InfoCompleteContract.View
{
//    //车牌号
//    @BindView(R.id.et_shipNumber)
//    EditText etShipNumber;
//    //车牌号
//    @BindView(R.id.tv_jumpShipInfo)
//    TextView tv_jumpShipInfo;
//    @BindView(R.id.layout_container)
//    LinearLayout layoutContainer;
//    @BindView(R.id.btn_commit)
//    Button btnCommit;
//    private ShipInfo mShipInfo;
//
//    private InfoCompleteContract.Presenter mPresenter;
//    private List<PaperPhoto> mPaperPhotos = new ArrayList<>();
//    private BottomSelectorDialog<PaperPhoto> mBottomSelectorDialog;

    @Override
    public int getLayoutId() {
        return R.layout.activity_ship_complete_info;
    }

//    @Override
//    public void initView(@Nullable Bundle savedInstanceState) {
//        super.initView(savedInstanceState);
//        ButterKnife.bind(this);
//        mPaperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传水路运输经营许可证"}, 0.62f), "水路运输经营许可", 7));
//        mPaperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传船舶营运证"}, 0.62f), "船舶营运证", 26));
//        mPaperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传船舶所有权证"}, 0.62f), "船舶所有权证", 29));
//        mPaperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传船舶国籍证书封面照片", "请上传船舶国籍证书第一页照片"}, new int[]{R.drawable.src_ship_license_cover, R.drawable.src_ship_license_first}, 1.33f), "船舶国籍证书", 25));
//        mPaperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传船舶检验证书"}, 0.62f), "船舶检验证书", 28));
//        mPaperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传船舶适航证书"}, 0.62f), "船舶适航证书", 27));
//        mPresenter = new InfoCompletePresenter(this);
//
//        mBottomSelectorDialog = new BottomSelectorDialog<PaperPhoto>(this) {
//            @Override
//            protected String getTitle() {
//                return "选择上传的证件类型";
//            }
//
//            @Override
//            protected void onLoad() {
//                showDicts(mPaperPhotos, true);
//            }
//        };
//        mBottomSelectorDialog.setOnItemSelected(new BottomSelectorDialog.OnItemSelectedListener<PaperPhoto>() {
//            @Override
//            public void onItemSelected(final PaperPhoto item, int extra) {
//                for (PaperPhoto temp : mPaperPhotos) {
//                    if (temp.getPaperType() == item.getPaperType() && temp.hasGenerateId()) {
//                        showMessage("该证件已选择");
//                        return;
//                    }
//                }
//                item.setGenerateId();
//                final View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_paper_photo, layoutContainer, false);
//                itemView.setId(item.getGenerateId());
//                layoutContainer.addView(itemView);
//                TextView tvPhotoName = (TextView) itemView.findViewById(R.id.tv_photoName);
//                tvPhotoName.setText(item.getValue());
//                itemView.findViewById(R.id.iv_photo).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(getContext(), UploadPhotoActivity.class);
//                        intent.putExtra(RegisterPhoto.class.getSimpleName(), item.getRegisterPhoto());
//                        startActivityForResult(intent, item.getPaperType());
//                    }
//                });
//
//                itemView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        item.reset();
//                        layoutContainer.removeView(itemView);
//                    }
//                });
//                final TextView endDate = (TextView) itemView.findViewById(R.id.tv_endDate_select);
//
//                endDate.setOnClickListener(new View.OnClickListener() {
//                    private DatePickerUtil datePickerUtil = new DatePickerUtil(getContext(), new DatePickerUtil.OnDateSetListener() {
//                        @Override
//                        public void onDateSet(int year, int monthOfYear, int dayOfMonth, int tag) {
//                            SimpleDate simpleDate = new SimpleDate();
//                            simpleDate.setDate(year, monthOfYear, dayOfMonth);
//                            String dataStr = simpleDate.dateString(true, "-");
//                            endDate.setText(dataStr);
//                            item.setDate(simpleDate.dateString(true, ""));
//                        }
//                    }, Calendar.getInstance());
//
//                    @Override
//                    public void onClick(View v) {
//                        datePickerUtil.showDatePicker(getFragmentManager(), 0);
//                    }
//                });
//            }
//        });
//    }
//
//    @OnClick({R.id.tv_selectUpPhoto, R.id.tv_jumpShipInfo, R.id.btn_commit})
//    @Override
//    public void onClick(View v) {
//        super.onClick(v);
//        if (R.id.tv_selectUpPhoto == v.getId()) {
//            mBottomSelectorDialog.showBlock();
//        } else if (R.id.tv_jumpShipInfo == v.getId()) {
//            Intent intent = new Intent(getContext(), AddShipInfoActivity.class);
//            intent.putExtra(ShipInfo.class.getSimpleName(), mShipInfo);
//            startActivityForResult(intent, 30);
//        } else if (R.id.btn_commit == v.getId()) {
//            boolean hasPaper = false;
//            for (PaperPhoto info : mPaperPhotos) {
//                if (info.hasGenerateId()) {
//                    if (!info.isDataComplete()) {
//                        showMessage(info.getEmptyDataMsg());
//                        return;
//                    } else {
//                        hasPaper = true;
//                    }
//                }
//            }
//            if (!hasPaper) {
//                showMessage("至少选择一种证件");
//                return;
//            }
//            List<Paperwork> list = new ArrayList<>();
//            for (int index = 0; index < mPaperPhotos.size(); index++) {
//                PaperPhoto temp = mPaperPhotos.get(index);
//                if (temp.hasGenerateId()) {
//                    list.add(new Paperwork(String.valueOf(temp.getPaperType()), temp.getRegisterPhoto(), temp.getDate()));
//                }
//            }
//            mPresenter.submit(new ShipVerifyInfo(
//                    etShipNumber.getText().toString().trim(),
//                    mShipInfo,
//                    list));
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.phone_selector, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == R.id.call) {
//            CallPhoneDialog.getInstance().showBlock(getSupportFragmentManager());
//            return true;
//        }
//        return false;
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == 30) {
//                mShipInfo = (ShipInfo) data.getSerializableExtra(ShipInfo.class.getSimpleName());
//                tv_jumpShipInfo.setText("已选择");
//            } else {
//                for (int index = 0; index < mPaperPhotos.size(); index++) {
//                    PaperPhoto temp = mPaperPhotos.get(index);
//                    if (temp.getPaperType() == requestCode) {
//                        RegisterPhoto registerPhoto = (RegisterPhoto) data.getSerializableExtra(RegisterPhoto.class.getSimpleName());
//                        temp.setRegisterPhoto(registerPhoto);
//                        ImageView imageView = (ImageView) findViewById(temp.getGenerateId()).findViewById(R.id.iv_photo);
//                        Glide.with(this).load(registerPhoto.getPhotos()[0]).fitCenter().into(imageView);
//                        return;
//                    }
//                }
//            }
//        }
//    }
//
//    @Override
//    public void success(String message) {
//        showMessage(message);
//    }
}
