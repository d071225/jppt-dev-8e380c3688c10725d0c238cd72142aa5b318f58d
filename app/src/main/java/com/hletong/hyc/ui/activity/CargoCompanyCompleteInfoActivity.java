package com.hletong.hyc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.InfoCompleteContract;
import com.hletong.hyc.model.PaperPhoto;
import com.hletong.hyc.model.RegisterPhoto;
import com.hletong.hyc.model.validate.CompanyVerifyInfo;
import com.hletong.hyc.model.validate.Paperwork;
import com.hletong.hyc.presenter.InfoCompletePresenter;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.util.DatePickerUtil;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.dialog.selector.BottomSelectorDialog;
import com.hletong.mob.util.SimpleDate;
import com.xcheng.view.controller.dialog.BottomOptionDialog;
import com.xcheng.view.controller.dialog.SimpleSelectListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 货主版个人信息补全
 * Created by chengxin on 2017/6/12.
 */
public class CargoCompanyCompleteInfoActivity extends BaseActivity implements InfoCompleteContract.View {

    @BindView(R.id.layout_container)
    LinearLayout layoutContainer;

    private List<PaperPhoto> mPaperPhotos = new ArrayList<>();
    private BottomSelectorDialog<PaperPhoto> mBottomSelectorDialog;

    private InfoCompleteContract.Presenter mPresenter;

    @BindView(R.id.btn_commit)
    Button btnCommit;

    @Override
    public int getLayoutId() {
        return R.layout.activity_cargo_company_complete_info;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        //mPaperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传三证合一营业执照照片"}, new int[]{R.drawable.src_busi_license_new}, 1.33f), "营业执照", 6));
        //   mPaperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传旧版营业执照照片"}, new int[]{R.drawable.src_busi_license_old}, 0.87f), "营业执照", 2));
        mPaperPhotos.add(new PaperPhoto(null, "营业执照", 0));
        mPaperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传税务登记证"}, 0.62f), "税务登记证", 4));
        mPaperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传组织机构代码"}, 0.62f), "组织机构代码", 3));

        mPresenter = new InfoCompletePresenter(this);
        mBottomSelectorDialog = new BottomSelectorDialog<PaperPhoto>(this) {
            @Override
            protected String getTitle() {
                return "选择上传的证件类型";
            }

            @Override
            protected void onLoad() {
                showList(mPaperPhotos, true);
            }
        };
        mBottomSelectorDialog.setOnItemSelected(new BottomSelectorDialog.OnItemSelectedListener<PaperPhoto>() {
            @Override
            public void onItemSelected(final PaperPhoto item, int extra) {
                for (PaperPhoto temp : mPaperPhotos) {
                    if (temp.getPaperType() == item.getPaperType() && temp.hasGenerateId()) {
                        showMessage("该证件已选择");
                        return;
                    }
                }
                item.setGenerateId();
                final View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_paper_photo, layoutContainer, false);
                itemView.setId(item.getGenerateId());
                layoutContainer.addView(itemView);
                TextView tvPhotoName = (TextView) itemView.findViewById(R.id.tv_photoName);
                tvPhotoName.setText(item.getValue());
                itemView.findViewById(R.id.iv_photo).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (item.getRegisterPhoto() == null) {
                            new BottomOptionDialog.Builder(getContext())
                                    .optionTexts(getString(R.string.busi_license_new), getString(R.string.busi_license_old))
                                    .tipText(getString(R.string.busi_license_tip))
                                    .onSelectListener(new SimpleSelectListener() {
                                        @Override
                                        public void onOptionSelect(View view, int position) {
                                            if (position == 0) {
                                                item.setRegisterPhoto(new RegisterPhoto(new String[]{"请上传三证合一营业执照照片"}, new int[]{R.drawable.src_busi_license_new}, 1.33f));
                                                item.setPaperType(6);
                                            } else if (position == 1) {
                                                item.setRegisterPhoto(new RegisterPhoto(new String[]{"请上传旧版营业执照照片"}, new int[]{R.drawable.src_busi_license_old}, 0.87f));
                                                item.setPaperType(2);
                                            }
                                            toUpLoadPhotoController(item);
                                        }
                                    }).show();
                            return;
                        }
                        toUpLoadPhotoController(item);
                    }
                });

                itemView.findViewById(R.id.delete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        item.reset();
                        layoutContainer.removeView(itemView);
                    }
                });
                final TextView endDate = (TextView) itemView.findViewById(R.id.tv_endDate_select);

                endDate.setOnClickListener(new View.OnClickListener() {
                    private DatePickerUtil datePickerUtil = new DatePickerUtil(getContext(), new DatePickerUtil.OnDateSetListener() {
                        @Override
                        public void onDateSet(int year, int monthOfYear, int dayOfMonth, int tag) {
                            SimpleDate simpleDate = new SimpleDate();
                            simpleDate.setDate(year, monthOfYear, dayOfMonth);
                            String dataStr = simpleDate.dateString(true, "-");
                            endDate.setText(dataStr);
                            item.setDate(simpleDate.dateString(true, ""));
                        }
                    }, Calendar.getInstance());

                    @Override
                    public void onClick(View v) {
                        datePickerUtil.showDatePicker(getFragmentManager(), 0);
                    }
                });
            }
        });
    }

    private void toUpLoadPhotoController(PaperPhoto paperPhoto) {
        Intent intent = new Intent(getContext(), UploadPhotoActivity.class);
        intent.putExtra(RegisterPhoto.class.getSimpleName(), paperPhoto.getRegisterPhoto());
        startActivityForResult(intent, paperPhoto.getPaperType());
    }

    @OnClick({R.id.tv_selectUpPhoto, R.id.btn_commit})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (R.id.tv_selectUpPhoto == v.getId()) {
            mBottomSelectorDialog.show();
        } else if (R.id.btn_commit == v.getId()) {
            boolean hasPaper = false;
            for (PaperPhoto info : mPaperPhotos) {
                if (info.hasGenerateId()) {
                    if (!info.isDataComplete()) {
                        showMessage(info.getEmptyDataMsg());
                        return;
                    } else {
                        hasPaper = true;
                    }
                }
            }
            if (!hasPaper) {
                showMessage("至少选择一种证件");
                return;
            }
            for (PaperPhoto info : mPaperPhotos) {
                if (info.hasGenerateId() && !info.isDataComplete()) {
                    showMessage(info.getEmptyDataMsg());
                    return;
                }
            }
            List<Paperwork> list = new ArrayList<>();
            for (int index = 0; index < mPaperPhotos.size(); index++) {
                PaperPhoto temp = mPaperPhotos.get(index);
                if (temp.hasGenerateId()) {
                    list.add(new Paperwork(String.valueOf(temp.getPaperType()), temp.getRegisterPhoto(), temp.getDate()));
                }
            }
            mPresenter.submit(new CompanyVerifyInfo(list));
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.phone_selector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.call) {
            CallPhoneDialog.getInstance().show(getSupportFragmentManager());
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        for (int index = 0; index < mPaperPhotos.size(); index++) {
            PaperPhoto temp = mPaperPhotos.get(index);
            if (temp.getPaperType() == requestCode) {
                RegisterPhoto registerPhoto = (RegisterPhoto) data.getSerializableExtra(RegisterPhoto.class.getSimpleName());
                temp.setRegisterPhoto(registerPhoto);
                ImageView imageView = (ImageView) findViewById(temp.getGenerateId()).findViewById(R.id.iv_photo);
                Glide.with(this).load(registerPhoto.getPhotos()[0]).fitCenter().into(imageView);
                return;
            }
        }
    }

    @Override
    public void success(String message) {
        showMessage(message);
    }
}
