package com.hletong.hyc.ui.activity.cargoforecast;

import android.content.Intent;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.AppointCarrier;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.ui.activity.CargoForecastActivity;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.fragment.AppointCarrierFragment;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.mob.architect.view.ITransactionView;
import com.xcheng.okhttp.util.ParamUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/3/22.
 * 开票及投保
 */

public class BlockInsurance extends BlockNoneSelfTrade {
    @BindViews({R.id.insureType, R.id.settleType, R.id.manualMatch})
    CommonInputView[] mViews;
    @BindView(R.id.manualMatchCarrier)
    CommonInputView manualMatchCarrier;
    @BindView(R.id.insurance_hint)
    TextView insuranceHint;

    private boolean appoint = false;//能否指定
    private int transporterType = Integer.MIN_VALUE;

    public BlockInsurance(ViewStub insurance, CargoForecastActivity.BlockAction dictItemDialog, ITransactionView switchDelegate) {
        super(insurance, "开票及投保信息", dictItemDialog, switchDelegate);
    }

    @Override
    void initWithSource(Source source, boolean fullCopy) {
        DictionaryItem insure = source.getInsuranceTypeAsDictionaryItem();
        if (insure != null)
            onItemSelected(insure, 0);

        DictionaryItem settle = source.getSettleTypeAsDI();
        if (settle != null)
            onItemSelected(settle, 1);

        DictionaryItem appoint = source.getAppointFlagAsDI();
        if (appoint != null)
            onItemSelected(appoint, 2);

        if (!ParamUtil.isEmpty(source.getAppointTransportList())) {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra("data", new ArrayList<>(source.getAppointTransportList()));
            onActivityResult(108, intent);
        }
    }

    @OnClick({R.id.insureType, R.id.settleType, R.id.manualMatch, R.id.manualMatchCarrier})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.insureType:
                showSelector("insureTypeEnum", "投保方式", 0);
                break;
            case R.id.settleType:
                if (getBillingType() != 1)//平台开票只能是平台结算，也就不用选择了
                    showSelector("settleTypeEnum", "结算方式", 1);
                else {
                    showMessage("平台开票只能使用平台结算方式");
                }
                break;
            case R.id.manualMatch:
                if (appoint) {
                    //平台开票自主投保强制兜底，这种情况也不让用户选
                    if (getBillingType() == 1) {//平台开票
                        DictionaryItem dictionaryItem = (DictionaryItem) mViews[0].getTag();
                        if (dictionaryItem == null) {
                            showMessage("请先选择投保方式");
                        } else if ("自主投保".equals(dictionaryItem.getText())) {
                            showMessage("平台开票自主投保必须选择兜底单位");
                        } else {
                            showSelector("appointTransportFlagEnum", "兜底运输", 2);
                        }
                    } else {//其他情况
                        showSelector("appointTransportFlagEnum", "兜底运输", 2);
                    }
                }
                break;
            case R.id.manualMatchCarrier:
                if (transporterType == Integer.MIN_VALUE) {
                    showMessage("请先选择货源");
                    return;
                }

                String title = "";
                switch (getBillingType()) {
                    case 1:
                        title = "兜底";
                        break;
                    case 2:
                        title = "指定";
                        break;
                }
                switch (transporterType) {
                    case 1:
                        title += "车辆";
                        break;
                    case 2:
                        title += "船舶";
                        break;
                }
                startActivityForResult(CommonWrapFragmentActivity.class, 108, CommonWrapFragmentActivity.getBundle(
                        title, AppointCarrierFragment.class, AppointCarrierFragment.getBundle(getBillingType(), transporterType, (ArrayList<AppointCarrier>) manualMatchCarrier.getTag())));
                break;
        }
    }

    @Override
    public List<DictionaryItem> prefetchItems() {
        List<DictionaryItem> list = new ArrayList<>();
        if (mViews[0].getTag() == null)
            list.add(new DictionaryItem("0", "insureTypeEnum"));
        if (mViews[1].getTag() == null)
            list.add(new DictionaryItem("1", "settleTypeEnum"));
        //mView[2]是选择是否进行兜底运输的选择框，这个
        //组件的显示和隐藏只能通过预加载的方式获取到的数据来判断（见dataRetrieved函数）
        //问题：为什么不向上面一样进行判断？换言之什么情况下预加载时mView[2]的tag里面有数据：复制货源的情况
        list.add(new DictionaryItem("2", "appointTransportFlagEnum"));
        return list;
    }

    @Override
    public void onBlockFieldChanged(BaseBlock block, String dictType, Object object) {
        if ("transportTypeEnum".equals(dictType)) {
            DictionaryItem di = (DictionaryItem) object;
            transporterType = di.getIdAsInt();
            manualMatchCarrier.setText(null);
            manualMatchCarrier.setTag(null);
            if ("车辆".equals(di.getText())) {
                manualMatchCarrier.setLabel("兜底车辆");
            } else {
                manualMatchCarrier.setLabel("兜底船舶");
            }
        }
    }

    @Override
    boolean isBlockSatisfied() {
        boolean r = mViews[0].getTag() != null && mViews[1].getTag() != null;
        if (appoint) {
            DictionaryItem di = (DictionaryItem) mViews[2].getTag();
            if (di == null)
                return false;
            if ("1".equals(di.getId())) {//用户选择了兜底
                r &= manualMatchCarrier.getTag() != null;
            }
        }
        return r;
    }

    @Override
    public String getErrorMessage() {
        if (mViews[0].getTag() == null)
            return "请选择投保方式";
        if (mViews[1].getTag() == null)
            return "请选择结算方式";
        DictionaryItem di = (DictionaryItem) mViews[2].getTag();
        if (di == null)
            return "请选择是否兜底运输";
        if ("是".equals(di.getText()) && manualMatchCarrier.getTag() == null)
            return "请选择兜底车辆";
        return null;
    }

    @Override
    public void getBlockParams(HashMap<String, String> params) {
        params.put("insure_type", getTag(mViews[0]).getId());
        params.put("settle_type", getTag(mViews[1]).getId());
        params.put("appoint_transport_flag", getTag(mViews[2]).getId());
        params.put("appoint_carrier_uuids", getManualMatchCarrier());
    }

    @Override
    public void fillSource(Source source) {
        source.setInsuranceType((DictionaryItem) mViews[0].getTag());
        source.setSettleType((DictionaryItem) mViews[1].getTag());
        source.setAppointFlag((DictionaryItem) mViews[2].getTag());
    }

    @Override
    public boolean dataRetrieved(List<DictionaryItem> data, int extra) {
        if (extra == 2) {
                /*
                  货方会员有个兜底开关，关了就不能选兜底
                  开着的时候，由于平台开票自主投保强制兜底，这种情况也不让用户选

                  如果不能兜底，返回的字典项里面只有一个否选项
                 */
            if (data.size() == 1 && "0".equals(data.get(0).getId())) {
                appoint = false;//不能指定运输
                setViewVisibility(mViews[2], View.GONE);//隐藏兜底选择框
            } else {
                appoint = true;
                setViewVisibility(mViews[2], View.VISIBLE);//显示兜底选择框
            }
            //如果已经有数据了，就不要往下走了，原因见prefetchItems
            if (mViews[2].getTag() != null)
                return true;
        }
        return super.dataRetrieved(data, extra);
    }

    @Override
    public void onItemSelected(DictionaryItem item, int extra) {
        mViews[extra].setTag(item);
        mViews[extra].setText(item.getText());
        switch (extra) {
            case 0://投保方式
                checkInsurance();
                //平台开票，自主投保，强制兜底，一定要选兜底单位
                if (appoint && getBillingType() == 1 && "自主投保".equals(item.getText())) {
                    DictionaryItem dic = new DictionaryItem("1", "是");
                    onItemSelected(dic, 2);
                }
                break;
            case 2://兜底运输
                if ("1".equals(item.getId())) {
                    setViewVisibility(manualMatchCarrier, View.VISIBLE);
                } else {
                    setViewVisibility(manualMatchCarrier, View.GONE);
                }
                //通知兜底运输状态发生了变化
                notifyItemChanged("appointTransportFlagEnum", item);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, Intent data) {
        if (requestCode == 108) {//对应的上传字段是 ：appoint_carrier_uuids，逗号分隔多个车辆
            ArrayList<AppointCarrier> carriers = data.getParcelableArrayListExtra("data");
            manualMatchCarrier.setTag(carriers);
            manualMatchCarrier.setText(getCarriers(carriers));
        }
    }

    private String getManualMatchCarrier() {
        ArrayList<AppointCarrier> carriers = (ArrayList<AppointCarrier>) manualMatchCarrier.getTag();
        if (carriers != null) {
            String s = "";
            for (AppointCarrier ac : carriers) {
                s += ac.getCarrierUuid();
                s += ",";
            }
            return s.substring(0, s.length() - 1);
        }
        return null;
    }

    private String getCarriers(ArrayList<AppointCarrier> carriers) {
        String s = "";
        for (AppointCarrier carrier : carriers) {
            s += carrier.getCarrier(transporterType);
            s += "，";
        }
        return s.substring(0, s.length() - 1);
    }

    @Override
    void billingTypeChangedInternal(int billingType) {
        super.billingTypeChangedInternal(billingType);
        if (!isVisible())
            return;
        checkInsurance();
        checkSettle();
        //清空已选中的兜底车辆(船舶)
        manualMatchCarrier.setText(null);
        manualMatchCarrier.setTag(null);
    }

    private void checkInsurance() {
        DictionaryItem di = (DictionaryItem) mViews[0].getTag();
        //自主开票/平台投保 有一个提示语句
        if (getBillingType() == 2 && di != null && "平台投保".equals(di.getText())) {
            setViewVisibility(insuranceHint, View.VISIBLE);
        } else {
            setViewVisibility(insuranceHint, View.GONE);
        }
    }

    private void checkSettle() {
        if (getBillingType() == 1) {//平台开票，只能是平台结算，不能选择其他的
            DictionaryItem di = new DictionaryItem("1", "平台结算");
            onItemSelected(di, 1);
        }
    }
}
