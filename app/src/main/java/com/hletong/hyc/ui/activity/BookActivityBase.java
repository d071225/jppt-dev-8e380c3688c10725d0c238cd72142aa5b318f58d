package com.hletong.hyc.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.BookContract;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.TransporterBase;
import com.hletong.hyc.presenter.SourcePresenter;
import com.hletong.hyc.ui.activity.ship.ShipBiddingActivity;
import com.hletong.hyc.ui.activity.ship.ShipBookActivity;
import com.hletong.hyc.ui.activity.ship.ShipQuoteActivity;
import com.hletong.hyc.ui.activity.truck.TruckBiddingActivity;
import com.hletong.hyc.ui.activity.truck.TruckBookActivity;
import com.hletong.hyc.ui.activity.truck.TruckQuoteActivity;
import com.hletong.hyc.ui.fragment.DeductTaxSelectorDialog;
import com.hletong.hyc.ui.fragment.SourceDetailsFragment;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.hyc.util.SimpleTextWatcher;
import com.hletong.mob.dialog.selector.SelectorPrefetchListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
//@formatter:off
/**
 * Created by ddq on 2017/1/5.
 * 摘牌总览
 *          竞价                                 挂价                                                   议价
 *    ———————————————                 ——————————————————————————                       ———————————————————————————————————
 *    |             |                 |           |             |                      |          |           |          |
 * 平台开票         自开              平开         自开         自主交易                  国联        报价        议价      议价摘牌
 *
 * 上述9种情况，其界面都使用一套布局，9种情况界面基本相同，不同之处在于逻辑处理
 *
 * 基类BookActivityBase提供一些公共View处理，基类BookPresenter提供公共逻辑处理
 *
 *                                           竞价投标View基类
 *                                     |--->BiddingBookActivity ----|->ShipBiddingActivity 船舶竞价
 *                                     |                            |->TruckBiddingActivity 车辆竞价
 * Activity的继承结构：BookActivityBase--|    挂价摘牌View基类
 *                                     |--->NormalBookActivity  ----|->ShipBookActivity 船舶摘牌
 *                                                                  |->TruckBookActivity 车辆摘牌
 *                                                                  |
 *                                                                  |  议价View基类
 *                                                                  |->QuoteBookActivity ---|->ShipQuoteActivity 船舶议价
 *                                                                                          |->TruckQuoteActivity 车辆议价
 *
 * Presenter的继承结构与Activity类似，4个基类每一个都对应一个Presenter，各自独特的逻辑都在各自的Presenter里面处理，保持业务逻辑结构清晰
 *
 * 整个摘牌过程，开始于Presenter的start()方法，本类{@link #onCreate(Bundle)}里面调用
 *    1.初始化过程首先是加载货源详情，货源信息加载完毕SourceDetailFragment会发出广播，
 *      本类会接受该广播{@link #onReceive(Context, String, Intent)}然后开始View初始化
 *    2.界面初始化核心部分在{@link com.hletong.hyc.presenter.BookPresenter}里面的sourceLoaded()函数
 */
//@formatter:on

public abstract class BookActivityBase<T extends TransporterBase, P extends BookContract.Presenter> extends TransporterSelectorActivity<T, P> implements BookContract.View {
    @BindView(R.id.plate)
    CommonInputView plate;
    @BindView(R.id.cargo)
    CommonInputView input;
    @BindView(R.id.price)
    CommonInputView price;
    @BindView(R.id.value)
    CommonInputView value;
    @BindView(R.id.deduct)
    CommonInputView deduct;
    @BindView(R.id.chk)
    CheckBox mCheckBox;
    @BindView(R.id.submit)
    Button submit;
    @BindView(R.id.bid)
    Button bidButton;
    @BindView(R.id.protocolHint)
    TextView protocolHint;

    private DeductTaxSelectorDialog mTaxSelector;
    private String cargoUuid;

    @Override
    public final int getLayoutId() {
        return R.layout.activity_book;
    }

    @Override
    protected final CommonInputView getTransporterLabel() {
        return plate;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);//不要挪到onCreate(Bundle)里面
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPresenter().start();//不要挪到initView()里面
    }

    @Override
    public void onReceive(Context context, String action, Intent intent) {
        Source source = intent.getParcelableExtra("source");
        cargoUuid = source.getCargo_uuid();
        getPresenter().sourceLoaded(source);
    }

    @Override
    public void showSubmitAlertDialog(DialogInterface.OnClickListener mListener) {
        DialogFactory.showAlertWithNegativeButton(this, getCommitTitle(), getCommitMessage(), mListener);
    }

    @Override
    public void showProtocolTransportHint(CharSequence message) {
        protocolHint.setVisibility(View.VISIBLE);
        protocolHint.setText(message);
    }

    @Override
    public void showAlertForUserInfoNotComplete(String content, DialogInterface.OnClickListener mListener) {
        DialogFactory.showAlertWithNegativeButton(this, false, getCommitTitle(), content, "认证", mListener, "取消", null);
    }

    @Override
    public void showTitle(String title) {
        setCustomTitle(title);
    }

    @Override
    public void showSourceDetailFragment() {
        //显示货源详情
        final String cargoUuid = getIntent().getStringExtra("cargoUuid");

        Bundle bundle = new Bundle();
        bundle.putInt("type", getIntent().getIntExtra("type", -1));
        bundle.putString("cargoUuid", cargoUuid);
        bundle.putString("quoteUuid", getIntent().getStringExtra("quoteUuid"));
        bundle.putString("cargoSrcType", getIntent().getStringExtra("cargoSrcType"));
        bundle.putInt("layout", R.layout.fragment_source_details);

        Fragment fragment = SourceDetailsFragment.instantiate(this, SourceDetailsFragment.class.getName(), bundle);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.container, fragment, "source_details");
        transaction.commit();
    }

    @Override
    public void showTransporter(String label) {
        plate.setVisibility(View.VISIBLE);
        plate.setLabel(label);
        //加载数据
        prefetch();
    }

    @Override
    public void showInputView(String label, String hint, String unit, boolean fraction) {
        input.setVisibility(View.VISIBLE);
        input.setLabel(label);
        input.setEditTextHint(hint);
        input.setSuffixText(unit);
        input.getInput().addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                //摘牌量发生了变化
                getPresenter().cargoChanged(s.toString());
            }
        });
        if (fraction) {
            input.setInputType(CommonInputView.NUMBER_DECIMAL);
        } else {
            input.setInputType(CommonInputView.NUMBER);
        }
    }

    @Override
    public void disableInputView(String label, String cargo) {
        input.setLabel(label);
        input.setText(cargo);
        input.setMode(CommonInputView.VIEW);
    }

    @Override
    public void showDeductRateSelector(List<Source.DeductRt> list) {
        mTaxSelector = DeductTaxSelectorDialog.getInstance(new ArrayList<>(list), this);
        mTaxSelector.setOnItemSelected(new SelectorPrefetchListener<Source.DeductRt>() {
            @Override
            public void onItemSelected(Source.DeductRt item, int extra) {
                deduct.setText(item.getValue());
                deduct.setTag(item);
            }
        });

        deduct.setVisibility(View.VISIBLE);
        deduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTaxSelector.show();
            }
        });

        mTaxSelector.prefetch(-1);//数据预加载，这里没有用到参数extra，传什么都行
    }

    @Override
    public void showIncome(String label, int colorRes) {
        value.setVisibility(View.VISIBLE);
        value.setLabel(label);
        value.setInputASuffixTextColor(ContextCompat.getColor(this, colorRes));
    }

    @Override
    public void updateIncome(String income) {
        value.setText(income);
    }

    @Override
    public void showPrice(String label, String unit, String hint) {
        price.setVisibility(View.VISIBLE);
        price.setLabel(label);
        price.setSuffixText(unit);
        price.setEditTextHint(hint);
        price.getInput().addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                //金额发生了变化
                getPresenter().priceChanged(s.toString());
            }
        });
    }

    @Override
    public void showButton(String text) {
        submit.setVisibility(View.VISIBLE);
        submit.setText(text);
        submit.setOnClickListener(this);
    }

    public String getCargoUuid() {
        return cargoUuid;
    }

    protected Source.DeductRt getDeductTaxRt() {
        return (Source.DeductRt) deduct.getTag();
    }

    protected String getCommitTitle() {
        return getString(R.string.commit_title_gj);
    }

    protected CharSequence getCommitMessage() {
        Source.DeductRt rt = (Source.DeductRt) deduct.getTag();
        if (rt != null && ("0.11".equals(rt.getValue()) || "11%".equals(rt.getValue()))) {
            final String a = getString(R.string.commit_content_tax);
            SpannableString ss = new SpannableString(getString(R.string.commit_content_without_tax) + a);
            ss.setSpan(new ForegroundColorSpan(Color.RED), ss.length() - a.length(), ss.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            return ss;
        }

        return getString(R.string.commit_content_without_tax);
    }

    /**
     * @param context
     * @param type    竞价、挂价、议价
     * @return
     */
    public static Intent getBookActivity(Context context, int type) {
        switch (BuildConfig.app_type) {
            case 2://车辆
            {
                if (type == SourcePresenter.CB)//竞价
                    return new Intent(context, TruckBiddingActivity.class);
                else if (type == SourcePresenter.GUAJIA)//挂价
                    return new Intent(context, TruckBookActivity.class);
                else if (type == SourcePresenter.QUOTE || type == SourcePresenter.QUOTE_PROGRESS)//议价
                    return new Intent(context, TruckQuoteActivity.class);
                break;
            }
            case 3://船舶
            {
                if (type == SourcePresenter.CB)//竞价
                    return new Intent(context, ShipBiddingActivity.class);
                else if (type == SourcePresenter.GUAJIA)//挂价
                    return new Intent(context, ShipBookActivity.class);
                else if (type == SourcePresenter.QUOTE || type == SourcePresenter.QUOTE_PROGRESS)//议价
                    return new Intent(context, ShipQuoteActivity.class);
                break;
            }
        }
        return null;
    }
}
