package com.hletong.hyc.ui.activity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.MessageContract;
import com.hletong.hyc.model.MessageInfo;
import com.hletong.hyc.model.Upcoming;
import com.hletong.hyc.presenter.MessagePresenter;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.mob.gallery.builder.PreViewBuilder;
import com.hletong.mob.gallery.widget.PickerRecyclerView;
import com.hletong.mob.util.ViewUtil;
import com.xcheng.okhttp.util.ParamUtil;
import com.xcheng.view.processbtn.ProcessButton;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ddq on 2016/10/28.
 * 消息界面:
 * 1.待办消息详情
 * 2.一般消息详情
 */

public class MessageInfoActivity extends ImageSelectorActivityNew<MessageContract.Presenter> implements MessageContract.View {
    public static final int MESSAGE = 0;//一般消息
    public static final int TODO = 1;//待办消息（违约单，守约单，滞压单，补签合同确认，卸货地变更通知）

    @BindView(R.id.tv_messageInfo)
    TextView tvMessageInfo;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.buttons)
    ViewStub buttons;
    @BindView(R.id.submit)
    ProcessButton submit;
    @BindView(R.id.image_upload)
    ViewStub image_upload;

    private PickerRecyclerView recyclerGridView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_messageinfo;
    }

    @Override
    protected ProcessButton getProcessBtn() {
        return submit;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        setCustomTitle(getIntent().getStringExtra("title"));
        getPresenter().start();
    }

    @Override
    public boolean isSupportActionBar() {
        return false;
    }

    private void initButtons(int type) {
        View view = buttons.inflate();
        TextView negative = (TextView) view.findViewById(R.id.id_negativeBtn);
        negative.setText(R.string.customer_service);
        ViewUtil.setBackground(negative, new ColorDrawable(Color.parseColor("#1f2733")));
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallPhoneDialog.getInstance().show(getSupportFragmentManager());
            }
        });
        TextView positive = (TextView) view.findViewById(R.id.id_positiveBtn);
        positive.setTag(type);
        ViewUtil.setBackground(positive, new ColorDrawable(Color.parseColor("#1f2733")));
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch ((int) v.getTag()) {
                    case 11://违约单
                    case 12://守约单
                        showAlert(getString(R.string.legal_doc_confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getPresenter().legalConfirm();
                            }
                        });
                        break;
                    case 61://货方补签合同
                        showAlert(getString(R.string.resign_contract_confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getPresenter().resignContract();
                            }
                        });
                        break;
                    case 62://货方补签合同后的通知
                        showAlert(getString(R.string.resign_contract_confirm), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                getPresenter().contractConfirm();
                            }
                        });
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.submit) {
            if (!ParamUtil.isEmpty(recyclerGridView.getPhotos())) {
                getPresenter().upload(recyclerGridView.getPhotos(), Constant.IMAGE_UPLOAD,false);//上传滞压单图片
            } else {
                showMessage(R.string.image_list_empty_hint);
            }
        }
    }

    @Override
    public void initCommonMessage(String message, String date) {
        tvMessageInfo.setText(Html.fromHtml(message));
        tvDate.setVisibility(View.VISIBLE);
        tvDate.setText(date);
    }

    @Override
    public void initUpcomingWithBottomBar(String message, int type) {
        tvMessageInfo.setText(message);
        initButtons(type);
    }

    @Override
    public void initStagnation(Spanned message) {
        //任务栏增加拨打电话menu
        getToolbar().inflateMenu(R.menu.phone_selector);
        getToolbar().setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                CallPhoneDialog.getInstance().show(getSupportFragmentManager());
                return true;
            }
        });

        tvMessageInfo.setText(message);
        //初始化图片上传模块
        submit.setVisibility(View.VISIBLE);
        submit.setOnClickListener(this);
        View view = image_upload.inflate();
        view.setPadding(0, 0, 0, 0);
        ViewUtil.setBackground(view, new ColorDrawable(Color.WHITE));
        TextView textView = (TextView) view.findViewById(R.id.upload_title);
        textView.setText(getIntent().getStringExtra("title"));
        recyclerGridView = (PickerRecyclerView) view.findViewById(R.id.recycler_GridView);
        new PickerRecyclerView.Builder(this).maxCount(3).action(PreViewBuilder.SELECT).build(recyclerGridView);
        recyclerGridView.setOnTakeClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSelector(recyclerGridView);
            }
        });
    }

    public void showAlert(String content, DialogInterface.OnClickListener listener) {
        DialogFactory.showAlertWithNegativeButton(this, getIntent().getStringExtra("title"), content, listener);
    }

    @Override
    protected MessageContract.Presenter createPresenter() {
        if (getIntent().getIntExtra("content", MESSAGE) == MESSAGE) {
            return new MessagePresenter(this, (MessageInfo) getParcelable("message"), getIntent().getBooleanExtra("unRead", false));
        } else {
            return new MessagePresenter(this, (Upcoming) getParcelable("upcoming"));
        }
    }
}
