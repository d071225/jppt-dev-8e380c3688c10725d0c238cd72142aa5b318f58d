package com.hletong.hyc.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ScrollView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.CargoForecastContract;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.TradeType;
import com.hletong.hyc.model.datasource.DraftDataSource;
import com.hletong.hyc.presenter.CargoForecastPresenter;
import com.hletong.hyc.ui.activity.cargoforecast.BaseBlock;
import com.hletong.hyc.ui.activity.cargoforecast.BlockCargoNoneSelfTrade;
import com.hletong.hyc.ui.activity.cargoforecast.BlockCargoOwner;
import com.hletong.hyc.ui.activity.cargoforecast.BlockCargoSelfTrade;
import com.hletong.hyc.ui.activity.cargoforecast.BlockInsurance;
import com.hletong.hyc.ui.activity.cargoforecast.BlockLoad;
import com.hletong.hyc.ui.activity.cargoforecast.BlockOther;
import com.hletong.hyc.ui.activity.cargoforecast.BlockSettle;
import com.hletong.hyc.ui.activity.cargoforecast.BlockTransport;
import com.hletong.hyc.ui.activity.cargoforecast.BlockTransportFee;
import com.hletong.hyc.ui.activity.cargoforecast.BlockUnLoad;
import com.hletong.hyc.ui.dialog.DictItemDialog;
import com.hletong.hyc.ui.fragment.TradeTypeFragment;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.ContractSpan;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.mob.dialog.selector.SelectorPrefetchListener;
import com.xcheng.okhttp.util.ParamUtil;
import com.xcheng.view.processbtn.ProcessButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/2/24.
 * 货源预报，整个界面分成了8块，每一块是一个ViewStub，对应一个Block
 * 具体的处理逻辑在各自的Block里面
 */

public class CargoForecastActivity extends ImageSelectorActivityNew<CargoForecastContract.Presenter> implements CargoForecastContract.View {
    @BindView(R.id.billingType)
    CommonInputView billingType;
    @BindView(R.id.block_CargoInfo_SelfTrade)
    ViewStub cargoInfoSelfTrade;
    @BindView(R.id.block_CargoInfo_NoneSelfTrade)
    ViewStub cargoInfoNoneSelfTrade;
    @BindView(R.id.block_load)
    ViewStub load;
    @BindView(R.id.block_unload)
    ViewStub unload;
    @BindView(R.id.block_transport_fee)
    ViewStub transportFee;
    @BindView(R.id.block_transport)
    ViewStub transport;
    @BindView(R.id.block_billingInfo)
    ViewStub billingInfo;
    @BindView(R.id.block_settle)
    ViewStub settle;
    @BindView(R.id.block_other)
    ViewStub other;
    @BindView(R.id.block_CargoOwner)
    ViewStub cargoOwner;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.preview)
    Button preview;
    @BindView(R.id.submit)
    ProcessButton processSubmit;
    @BindView(R.id.scrollView)
    ScrollView mScrollView;
    /**
     * 0 => 自主交易-货物信息
     * 1 => 非自主交易-货物信息
     * 2 => 投保
     * 3 => 结算
     * 4 => 其他信息
     * 5 => 装货信息
     * 6 => 卸货信息
     * 7 => 运输信息
     * 8 => 运费信息
     * 9 => 自主交易-货方联系人
     */
    private List<BaseBlock> mBlocks;
    private DictItemDialog mDictItemDialog;
    private LinkedBlockingQueue<BaseBlock> wait2Prefetch;//等待预加载
    private BaseBlock prefetching;//正在预加载的
    private String sponsor;
    private boolean saveToDraft;//能否保存到草稿

    @Override
    protected ProcessButton getProcessBtn() {
        return processSubmit;
    }

    /**
     * {@link DictItemDialog} 回调的函数，正在预加载的Block所有的Items全部完成
     */
    private final DictItemDialog.PrefetchListener mPrefetchListener = new DictItemDialog.PrefetchListener() {
        @Override
        public void prefetchFinished() {
//            Logger.d("prefetch complete => " + prefetching.getClass().getSimpleName());
            //预加载结束
            prefetching = wait2Prefetch.poll();
            if (prefetching != null) {
                mDictItemDialog.setOnItemSelected(prefetching);
//                Logger.d("take block and start fetch => " + prefetching.getClass().getSimpleName());
                mDictItemDialog.prefetch(prefetching.prefetchItems(), this);//开始预加载
            }
        }
    };

    //所有block加载字典项都是通过这个接口
    private final BlockAction mBlockAction = new BlockAction() {

        @Override
        public void downloadImage(String fileId, BaseBlock block) {
            getPresenter().download(fileId);
        }

        @Override
        public void fieldChanged(BaseBlock block, String dictName, Object object) {
            for (BaseBlock cblock : mBlocks) {
                if (cblock != block && cblock.isVisible())
                    cblock.onBlockFieldChanged(block, dictName, object);
            }
        }

        @Override
        public void prefetch(BaseBlock block) {
//            Logger.d("try to start prefetch => " + block.getClass().getSimpleName());
            List<DictionaryItem> items = block.prefetchItems();
            if (ParamUtil.isEmpty(items))
                return;

            if (prefetching == null) {
//                Logger.d("no prefetching block, start prefetch => " + block.getClass().getSimpleName());
                prefetching = block;
                mDictItemDialog.setOnItemSelected(prefetching);
                mDictItemDialog.prefetch(items, mPrefetchListener);//开始预加载
            } else {
//                Logger.d(prefetching.getClass().getSimpleName() + " is prefetching, add " + block.getClass().getSimpleName() + " to queue");
                wait2Prefetch.add(block);
            }
        }

        @Override
        public void fetch(String fieldName, String title, int extra, DictItemDialog.OnItemSelectedListener<DictionaryItem> listener) {
            mDictItemDialog.setOnItemSelected(listener);
            mDictItemDialog.showDict(fieldName, title, extra);
        }

        @Override
        public void startActivity(BaseBlock block, Class cls, Bundle bundle, int requestCode) {
            sponsor = block.getClass().getSimpleName();//保存发起方的名字，之后回传的时候用于选择block
            toActivity(cls, bundle, requestCode, null);
        }
    };

    //按返回键或者导航栏返回键
    private void saveData() {
        //外部控制能否保存到草稿箱
        if (!saveToDraft) {
            finish();
            return;
        }

        for (BaseBlock block : mBlocks) {
            if (!block.isVisible())
                continue;

            //所有block都满足内部保存到草稿的条件
            if (!block.canSaveToDraft()) {
                finish();
                return;
            }
        }

        DialogFactory.showAlertWithNegativeButton(this, true, "数据保存", "是否将已填写数据保存到草稿？",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getPresenter().saveData();
                    }
                },
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
    }

    @Override
    public void onBackPressed() {
        saveData();
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_cargo_forecast_main;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        setCustomTitle(R.string.cargo_forecast);

        //能否保存为草稿
        saveToDraft = getIntent().getBooleanExtra("saveToDraft", true);

        getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
        billingType.setLabelTextColor(ContextCompat.getColor(this, R.color.colorAccent));
        mDictItemDialog = new DictItemDialog(this, Constant.getUrl(Constant.CARGO_FORECAST_DICTIONARY));
        mBlocks = new ArrayList<>();
        mBlocks.add(new BlockCargoSelfTrade(cargoInfoSelfTrade, mBlockAction, this));// 0 => 自主交易-货物信息
        mBlocks.add(new BlockCargoNoneSelfTrade(cargoInfoNoneSelfTrade, mBlockAction, this));// 1 => 非自主交易-货物信息
        mBlocks.add(new BlockInsurance(billingInfo, mBlockAction, this));// 2 => 投保
        mBlocks.add(new BlockSettle(settle, mBlockAction, this));// 3 => 结算
        mBlocks.add(new BlockOther(other, mBlockAction, this));// 4 => 其他信息
        mBlocks.add(new BlockLoad(load, mBlockAction, this));// 5 => 装货信息
        mBlocks.add(new BlockUnLoad(unload, mBlockAction, this));// 6 => 卸货信息
        mBlocks.add(new BlockTransport(transport, mBlockAction, this));// 7 => 运输信息
        mBlocks.add(new BlockTransportFee(transportFee, mBlockAction, this));// 8 => 运费信息
        mBlocks.add(new BlockCargoOwner(cargoOwner, mBlockAction, this));//9 => 货方联系人
        wait2Prefetch = new LinkedBlockingQueue<>(mBlocks.size());//初始化预加载队列

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                processSubmit.setEnabled(isChecked);
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPresenter().start();
    }

    @Override
    public void chooseDefaultType() {
        mDictItemDialog.setOnItemSelected(new SelectorPrefetchListener<DictionaryItem>() {
            @Override
            public void onItemSelected(DictionaryItem item, int extra) {
                /**
                 * 这里取到的item里面的内容是会员类型，要把它转换成相应的开票方式
                 */
                switch (item.getId()) {
                    case "1"://平台开票会员
                        getPresenter().tradeTypeSelected(new DictionaryItem("1", "平台开票"), false);
                        break;
                    case "2"://自主开票会员
                        getPresenter().tradeTypeSelected(new DictionaryItem("2", "自主开票"), false);
                        break;
                    case "3"://普通会员
                        getPresenter().tradeTypeSelected(new DictionaryItem("3", "自主交易"), false);
                        break;
                }
            }

            @Override
            public boolean dataRetrieved(List<DictionaryItem> data, int extra) {
                super.dataRetrieved(data, extra);
                /**
                 * 关于这里返回false而不是true
                 * 由于这个函数里面会调用tradeTypeChanged()函数
                 * 而这个tradeTypeChanged()会触发block的prefetch操作，
                 * 由于这里是共享mDictItemDialog，返回true会导致后面的流程都乱掉（prefetchFinished()里面的逻辑导致的），
                 * 这里返回false表示本次的加载操作全部结束了，
                 * BottomSelectorDialog不用调用prefetchFinished()函数了
                 */
                return false;
            }
        });
        mDictItemDialog.prefetch("invoiceType", -1);
    }

    private void setContract(int res) {
        checkbox.setVisibility(View.VISIBLE);
        ContractSpan.setContractHint(res, checkbox).setListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //查看自主交易三方或者是货物运输三方
                Bundle bundle = new Bundle();
                Source source = new Source();
                gatherSource(source);
                bundle.putParcelable("source", source);
                bundle.putBoolean("showConfirmButton", true);
                sponsor = BlockOther.class.getSimpleName();
                toActivity(ThirdPartyContractActivity.class, bundle, 101, null);
            }
        });
    }

    @Override
    public void preview(Source source) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("source", source);
        bundle.putString("title", "货源预览");
        toActivity(SourcePreviewActivity.class, bundle, null);
    }

    @Override
    public boolean gatherSource(Source source) {//把所有可见的block里面的数据放到source里面
        if (billingType.getTag() == null)
            return false;

        source.setBilling_type((Integer) billingType.getTag());
        for (BaseBlock block : mBlocks) {
            if (block.isVisible()) {
                block.fillSource(source);
            }
        }
        return true;
    }

    @Override
    public void tradeTypeChanged(DictionaryItem di, Source source) {
        checkbox.setVisibility(View.GONE);
        checkbox.setChecked(false);
        final int type = di.getIdAsInt();
        if (type == 2) {
            setContract(R.string.hint_transport_contract);
        } else if (type == 3) {
            setContract(R.string.hint_self_trade_contract);
        }
        processSubmit.setEnabled(checkbox.getVisibility() == View.GONE);

        billingType.setLabel(di.getText());
        billingType.setTag(type);//保存开票方式
        for (BaseBlock block : mBlocks)
            block.billingTypeChanged(type, source, getIntent().getBooleanExtra("full_copy", false));

        mScrollView.scrollTo(0, 0);
    }

    @Override
    public void showSelfTradeAlert(String message) {
        DialogFactory.showAlert(getContext(), "自主交易", message);
    }

    @Override
    public void showPasswordDialog() {

    }

    @Override
    public void gatherSubmitData(HashMap<String, String> params, int billingType) {
        if (billingType == 3) {
            for (BaseBlock block : mBlocks)
                block.getSelfTradeBlockParams(params);
        } else {
            for (BaseBlock block : mBlocks)
                block.getBlockParams(params);
        }
    }

    @OnClick({R.id.billingType, R.id.preview, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.billingType:
                toActivity(CommonWrapFragmentActivity.class,
                        CommonWrapFragmentActivity.getBundle(
                                "选择交易",
                                TradeTypeFragment.class,
                                TradeTypeFragment.getExtras(
                                        view.getTag() == null ? -1 : (Integer) view.getTag()
                                )),
                        100,
                        null);
                break;
            case R.id.preview:
                getPresenter().preview();
                break;
            case R.id.submit:
                for (BaseBlock block : mBlocks) {
                    if (!block.isVisible())
                        continue;
                    String message = block.getErrorMessage();
                    if (message != null) {
                        showMessage(message);
                        return;
                    }
                }
                DialogFactory.showAlertWithNegativeButton(getContext(), "货源预报发布", "确定要发布货源预报吗？", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getPresenter().submit(LoginInfo.getLoginInfo().getMember_code());
                    }
                });
                break;
        }
    }

    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        switch (requestCode) {
            case 100://选择开票方式
                final TradeType tradeType = data.getParcelableExtra("tradeType");
                getPresenter().tradeTypeSelected(tradeType.getAsDictionaryItem(), false);
                break;
            case 101://查看合同并确认
                checkbox.setChecked(true);
                break;
            default:
                for (BaseBlock block : mBlocks) {
                    if (block.getClass().getSimpleName().equals(sponsor)) {
                        block.onActivityResult(requestCode, data);
                        break;
                    }
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (BaseBlock block : mBlocks) {
            block.stop();
        }
    }

    @Override
    public void showFiles(List<String> mFiles) {
        //选择的货源里面可能会有图片
        if (mBlocks.get(0).isVisible()) {
            BlockCargoSelfTrade bcns = (BlockCargoSelfTrade) mBlocks.get(0);
            bcns.imageDownloadSucceed(mFiles);
        } else if (mBlocks.get(1).isVisible()) {
            BlockCargoNoneSelfTrade bcns = (BlockCargoNoneSelfTrade) mBlocks.get(1);
            bcns.imageDownloadSucceed(mFiles);
        }
    }

    @Override
    protected CargoForecastContract.Presenter createPresenter() {
        return new CargoForecastPresenter(
                new DraftDataSource(this), this,
                (Source) getParcelable("source"),
                getIntent().getStringExtra("cargoUuid"),
                getIntent().getBooleanExtra("submit", false));
    }

    //Block和Activity的一些交互
    public interface BlockAction {
        void downloadImage(String fileId, BaseBlock block);

        //某一个Block里面的字典项发生了变化
        void fieldChanged(BaseBlock block, String dictName, Object object);

        void prefetch(BaseBlock block);

        void fetch(String fieldName, String title, int extra, DictItemDialog.OnItemSelectedListener<DictionaryItem> listener);

        void startActivity(BaseBlock block, Class cls, Bundle bundle, int requestCode);
    }
}
