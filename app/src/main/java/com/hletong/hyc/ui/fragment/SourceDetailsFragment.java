package com.hletong.hyc.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.HyApplication;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.BookContract;
import com.hletong.hyc.contract.SourceContract;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.Quote;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.presenter.BiddingHistorySourcePresenter;
import com.hletong.hyc.presenter.BookSourcePresenter;
import com.hletong.hyc.presenter.SelfTradePreBookPresenter;
import com.hletong.hyc.presenter.SelfTradeSourcePresenter;
import com.hletong.hyc.presenter.SourcePresenter;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.LocationHelper;
import com.hletong.hyc.util.SimpleTextWatcher;
import com.hletong.mob.base.MvpFragment;
import com.hletong.mob.gallery.builder.PreViewBuilder;
import com.hletong.mob.gallery.widget.PickerRecyclerView;
import com.hletong.mob.util.ViewUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dongdaqing on 2017/4/25.
 * 货源详情、待办-自主交易界面
 * 货源详情可以通过货源公告、挂架摘牌、竞价投标列表界面进入
 * 待办-自主交易通过消息里面的待办任务列表进入
 */

public class SourceDetailsFragment extends MvpFragment<SourceContract.Presenter> implements SourceContract.View {
    @BindView(R.id.special_req)
    TextView instruction;
    @BindView(R.id.cargoName)
    TextView cargoName;
    @BindView(R.id.cargoDesc)
    TextView cargoDesc;
    @BindView(R.id.startAddress)
    CommonInputView startAddress;
    @BindView(R.id.endAddress)
    CommonInputView endAddress;
    @BindView(R.id.model_req)
    CommonInputView modelRequest;
    @BindView(R.id.loading_date)
    CommonInputView loading_date;
    @BindView(R.id.transport_days)
    CommonInputView transDays;
    @BindView(R.id.confer)
    CommonInputView confer;
    @BindView(R.id.gallery)
    PickerRecyclerView gallery;
    @BindView(R.id.contact_info)
    View contact_info;
    @BindView(R.id.contact)
    TextView contact;
    @BindView(R.id.contact_desc)
    TextView contactDesc;

    @BindView(R.id.scrollView)
    View scrollView;
    //非自主交易的货源详情
    @BindView(R.id.extra_normal)
    ViewStub extra_normal;
    //自主交易的货源详情
    @BindView(R.id.extra_zzjy)
    ViewStub extra_zzjy;
    //竞价大厅入口
    @BindView(R.id.cb_hall)
    ViewStub cb_hall;
    //自主交易待办任务的完成交易和违约投诉按钮
    @BindView(R.id.buttons)
    ViewStub buttons;
    @BindView(R.id.member_management_unit)
    ViewStub member_management_unit;
    @BindView(R.id.borrow_insurance_money)
    ViewStub borrow_insurance_money;

    private LocationHelper mLocationHelper;

    public static Bundle getParams(int type, Source source) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", type);
        bundle.putParcelable("source", source);
        return bundle;
    }

    private String getStringExtra(String key) {
        return getArguments().getString(key);
    }

    @Override
    public int getLayoutId() {
        int layout = getArguments().getInt("layout", 0);
        return layout == 0 ? R.layout.fragment_source_details_scroll : layout;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        getPresenter().start();
    }

    @Override
    public void onReceive(Context context, String action, Intent intent) {
        if (action.equals(BookContract.BID_INFO_CHANGED)) {
            confer.setText(intent.getStringExtra("data"));
        } else if (action.equals(BookContract.QUOTE_INFO_CHANGED)) {
            Quote quote = intent.getParcelableExtra("data");
            getPresenter().quoteChanged(quote);
        }
    }

    @Override
    public void showTitle(String title) {
        //整体布局默认隐藏
        scrollView.setVisibility(View.VISIBLE);
        getBaseActivity().setCustomTitle(title);
    }

    @Override
    public void showRequests(String requests) {
        instruction.setVisibility(View.VISIBLE);
        instruction.setText(requests);
    }

    @Override
    public void showCargoName(String cargoName) {
        this.cargoName.setText(cargoName);
    }

    @Override
    public void showCargo(String cargo) {
        cargoDesc.setText(cargo);
    }

    @Override
    public void showAddress(String from, String to) {
        startAddress.setText(from);
        endAddress.setText(to);
    }

    @Override
    public void showTransporterRequire(int icon, String require) {
        modelRequest.setLabelDrawable(icon);
        modelRequest.setText(require);
    }

    @Override
    public void showLoadingDate(String date) {
        loading_date.setText(date);
    }

    @Override
    public void showTransportDays(int icon, String days) {
        transDays.setVisibility(View.VISIBLE);
        transDays.setLabelDrawable(icon);
        transDays.setText(days);
    }

    @Override
    public void showCargoPrice(String cargoPrice) {
        confer.setVisibility(View.VISIBLE);
        confer.setText(cargoPrice);
    }

    @Override
    public void showCargoInfo(String docNo, String value, String volume, String measureType, String chargeType, String billingType, String settleType, String multiTransport, String transportLoss, String tax) {
        extra_normal.inflate();
        ((CommonInputView) findViewById(R.id.cargo_value)).setText(value);//总货值
        ((CommonInputView) findViewById(R.id.volume)).setText(volume);//长宽高
        ((CommonInputView) findViewById(R.id.measure_type)).setText(measureType);//计量方式
        ((CommonInputView) findViewById(R.id.charge_type)).setText(chargeType);//计费依据
        ((CommonInputView) findViewById(R.id.settle_method)).setText(settleType);//结算方式
        //整车(船)运输
        CommonInputView mt = findViewById(R.id.multi_transport);
        mt.setLabel(BuildConfig.multi_transport_label);
        mt.setText(multiTransport);
        ((CommonInputView) findViewById(R.id.transport_loss)).setText(transportLoss);//运输损耗
        ((CommonInputView) findViewById(R.id.bus_doc_no)).setText(docNo);//业务单据号
        ((CommonInputView) findViewById(R.id.billingType)).setText(billingType);//开票方式
        ((CommonInputView) findViewById(R.id.tax)).setText(tax);//综合税率
    }

    @Override
    public void showCargoInfo(String docNo, String volume) {
        View view = extra_zzjy.inflate();//自主交易的详情界面
        ((CommonInputView) view.findViewById(R.id.volume)).setText(volume);//长宽高
        ((CommonInputView) view.findViewById(R.id.bus_doc_no)).setText(docNo);//业务单据号
    }

    @Override
    public void showImages(List<String> images) {
        //成功下载了图片
        gallery.setVisibility(View.VISIBLE);
        new PickerRecyclerView.Builder(this).selectedPhotos(new ArrayList<>(images)).action(PreViewBuilder.PREVIEW).build(gallery);
    }

    @Override
    public void showMenu(int menu, Toolbar.OnMenuItemClickListener listener) {
        getBaseActivity().getToolbar().inflateMenu(menu);
        getBaseActivity().getToolbar().setOnMenuItemClickListener(listener);
    }

    @Override
    public void showCallBlock(String label, String data) {
        contact_info.setVisibility(View.VISIBLE);//打电话界面默认是隐藏的
        contact.setText(data);//名字要做隐藏
        contactDesc.setText("货方联系人");
    }

    @Override
    public void showBiddingPriceView(String unit) {
        findViewById(R.id.tax).setVisibility(View.GONE);//不显示税率

        CommonInputView bidPrice = findViewById(R.id.bidPrice);
        bidPrice.setVisibility(View.VISIBLE);
        bidPrice.setSuffixText(unit);
        bidPrice.getInput().addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                //价格发生了变化，广而告之
                Intent intent = new Intent(SourceContract.PLATFORM_BIDDING_PRICE_CHANGED);
                intent.putExtra("price", s.toString());
                broadcast(intent);
            }
        });
    }

    @Override
    public void hideBiddingPriceView() {
        CommonInputView bidPrice = findViewById(R.id.bidPrice);
        bidPrice.setVisibility(View.GONE);
    }

    @Override
    public void showBiddingButton() {
//        View view = cb_hall.inflate();
//        view.setTag(source.getCargo_uuid());
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = BookActivityBase.getBookActivity(getContext(), SourcePresenter.CB);
//                if (intent == null) return;
//                intent.putExtra("cargoUuid", (String) v.getTag());
//                startActivity(intent);
//            }
//        });
    }

    @Override
    public void showBookInfo(String label, String cargo, String price) {
        ViewStub vs = findViewById(R.id.trade_info);
        View tradeInfo = vs.inflate();
        CommonInputView civ = tradeInfo.findViewById(R.id.unit_ct);
        civ.setText(cargo);//承运数量（重量）
        civ.setLabel(label);//承运的是重量还是数量
        ((CommonInputView) tradeInfo.findViewById(R.id.unit_price)).setText(price);//承运单价
    }

    @Override
    public void showUnitBlock() {
        View mm_member_unit = member_management_unit.inflate();

        LoginInfo loginInfo = LoginInfo.getLoginInfo();
        TextView mm_name = (TextView) mm_member_unit.findViewById(R.id.mm_name);
        mm_name.setText(loginInfo.getMm_unit_name());
        TextView contact = (TextView) mm_member_unit.findViewById(R.id.contact);
        contact.setText(loginInfo.getMm_biz_contact());

        View call_mm_contact = borrow_insurance_money.inflate();
        call_mm_contact.setTag(loginInfo.getMm_biz_contact_tel());
        call_mm_contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CallPhoneDialog.getInstance().show(getFragmentManager(), "申请垫付保证金", "确定要联系会员管理单位申请垫付保证金吗？", (String) v.getTag());
            }
        });
    }

    @Override
    public void showActionButtons() {
        View view = buttons.inflate();
        TextView negative = (TextView) view.findViewById(R.id.id_negativeBtn);
        negative.setText("违约投诉");
        ViewUtil.setBackground(negative, new ColorDrawable(Color.parseColor("#1f2733")));
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().complain();
            }
        });

        TextView positive = (TextView) view.findViewById(R.id.id_positiveBtn);
        positive.setText("完成交易");
        ViewUtil.setBackground(positive, new ColorDrawable(Color.parseColor("#1f2733")));
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPresenter().confirm(HyApplication.getAMapLocation());
            }
        });
    }

    @Override
    public void makeCall(String contact, String tel, CallPhoneDialog.OnCalledListener listener) {
        CallPhoneDialog dialog = CallPhoneDialog.getInstance();
        dialog.setOnCalledListener(listener);
        dialog.show(getFragmentManager(), "拨打电话", "是否要联系货方联系人" + contact + "？", tel);
    }

    @Override
    public String getCarrier() {
        return getArguments().getString("carrier");
    }

    //待办任务-自主交易 确认成功
    @Override
    public void success(String message) {
        showMessage(message);
    }

    @OnClick(R.id.contact_info)
    @Override
    public void onClick(View v) {
        getPresenter().call();
    }

    @Override
    public void startLocating(boolean isForceStart) {
        if (mLocationHelper == null)
            mLocationHelper = new LocationHelper(getActivity());
        mLocationHelper.startLocating(isForceStart);
    }

    @Override
    public void onLocationError(String message) {
        mLocationHelper.onLocationError(message);
    }

    @Override
    protected SourceContract.Presenter createPresenter() {
        int type = getArguments().getInt("type", 0);
        switch (type) {
            case SourcePresenter.GUAJIA://挂价
                return new BookSourcePresenter(this, type, getStringExtra("cargoUuid"), getStringExtra("cargoSrcType"), getStringExtra("quoteUuid"), 2);
            case SourcePresenter.CB://竞价
                return new BookSourcePresenter(this, type, getStringExtra("cargoUuid"), getStringExtra("cargoSrcType"), getStringExtra("quoteUuid"), 1);
            case SourcePresenter.QUOTE://报价
            case SourcePresenter.QUOTE_PROGRESS://议价进度
                return new BookSourcePresenter(this, type, getStringExtra("cargoUuid"), getStringExtra("cargoSrcType"), getStringExtra("quoteUuid"), 300);
            case SourcePresenter.CB_HISTORY://竞价历史
                return new BiddingHistorySourcePresenter(this, type, getStringExtra("bidUuid"));
            case SourcePresenter.TRANSPORTER_UPCOMING_SELF_TRADE://车船交易确认单
            case SourcePresenter.CARGO_UPCOMING_SELF_TRADE://货方交易确认单
                return new SelfTradeSourcePresenter(this, type, getStringExtra("tradeUuid"), getStringExtra("confirmType"));
            case SourcePresenter.PRE_DE_LIST_SELF_TRADE://自主交易预摘牌(会员管理单位垫付)
                return new SelfTradePreBookPresenter(this, type, getStringExtra("cargoUuid"), (Source) getArguments().getParcelable("source"));
        }
        return null;
    }
}
