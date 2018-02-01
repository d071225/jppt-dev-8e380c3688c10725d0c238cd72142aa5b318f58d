package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.contract.Contract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.model.CargoContractDetails;
import com.hletong.hyc.model.ContractItemForDetails;
import com.hletong.hyc.model.TransportContractDetails;
import com.hletong.hyc.util.RichTextFormat;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.util.NumberUtil;

/**
 * Created by ddq on 2017/1/4.
 * 合同
 */

public abstract class ContractPresenter<V extends Contract.View> extends BasePresenter<V> implements Contract.Presenter{
    private String contractUuid;//查看历史合同

    public ContractPresenter(@NonNull V baseView, String contractUuid) {
        super(baseView);
        this.contractUuid = contractUuid;
    }

    @Override
    public void start() {
        if (contractUuid != null) {
            //查看合同
            loadHistoryContractDetails(contractUuid);
        } else {
            //签订合同
            loadContractDetails();
        }
    }

    @Override
    public void signTimeOut() {
        if (contractUuid == null){
            //签约合同
            getView().hideTimeView();
        }else {
            getView().showTimeOutView();
        }
    }

    protected abstract void loadContractDetails();

    protected abstract void loadHistoryContractDetails(String contractUuid);

    /**
     * 查看合同信息
     *
     * @param item
     * @param withCarrierInfo 只有承运合同才有承运信息，货方合同是没有的
     * @return
     */
    protected String getContractStr(ContractItemForDetails item, boolean withCarrierInfo) {
        return "合同编号：" +
                item.getContract_code() +
                "<br/>" +
                "平台：" +
                RichTextFormat.getUnderLine(item.getPart_a_name()) +
                "<br/>" +
                BuildConfig.user_type + "：" +
                RichTextFormat.getUnderLine(item.getPart_b_name()) +
                "<br/>" +
                "日期：" +
                RichTextFormat.getUnderLine(item.getCreateTimeInChinese()) +
                "<br/>" +
                "<br/>" +
                "合同基本信息" +
                "<br/>" +
                "1.1　货物名称：" +
                RichTextFormat.getUnderLine(item.getOrgin_cargon_kind_name()) +
                "，计量方式：" +
                RichTextFormat.getUnderLine(item.getMeasure_type()) +
                "，运输损耗：" +
                RichTextFormat.getUnderLine(item.getMaxWastageRtWithUnit()) +
                "<br/>" +
                "1.2　装货地：" +
                RichTextFormat.getUnderLine(item.getLoadingAddress()) +
                "，卸货地：" +
                RichTextFormat.getUnderLine(item.getUnLoadingAddress()) +
                "，装货日期：" +
                RichTextFormat.getUnderLine(item.getLoadingStartDtInChineseFormat()) +
                " 至 " +
                RichTextFormat.getUnderLine(item.getLoadingEndDtInChineseFormat()) +
                "，装货时间：" +
                RichTextFormat.getUnderLine(item.getTimePeriod("～")) +
                "，运输期限：" +
                RichTextFormat.getUnderLine(item.getTransport_days()) +
                "天" +
                "<br/>" +
                "1.3　" + (withCarrierInfo ? "" : ("运输方式：" + RichTextFormat.getUnderLine(item.getTransportType()) + "。")) +
                "计费依据：" +
                RichTextFormat.getUnderLine(item.getChargeReferType()) +
                "，" + AppTypeConfig.getTransporterTypeLabel(item) + "：" +
                RichTextFormat.getUnderLine(item.getTransporterRequests()) +
                "，" + AppTypeConfig.getMultiTransportTypeLabel(item) + "：" +
                RichTextFormat.getUnderLine(item.getMultiTransportFlag()) +
                "，特别要求：" +
                RichTextFormat.getUnderLine(item.getSpecial_req()) +
                "，结算方式：" +
                RichTextFormat.getUnderLine(item.getSettle_type()) +
                "。" +
                (withCarrierInfo ? getTransportContractExtra(item) : getCargoContractExtra(item));
    }

    /**
     * 货方合同和承运合同有些差别，下面两个函数返回的内容就是两个合同的差异之处
     *
     * @param item
     * @return
     */
    protected String getCargoContractExtra(ContractItemForDetails item) {
        return "<br/>货源运输具体信息<br/>2.1　" +
                item.getBookRefType() + "：" + RichTextFormat.getUnderLine(item.getCargoDescWithUnit()) +
                "，运费单价（元/" + item.getCargoUnit() + "含税)：" + RichTextFormat.getUnderLine(item.getTotalUnitAmt()) +
                "，运费额（元）：" + RichTextFormat.getUnderLine(item.getTransportTotalFee());
    }

    //承运合同
    protected String getTransportContractExtra(ContractItemForDetails item) {
        return
                (item.getBilling_type() == 1 ? ""
                        :
                        "运费总额（元）：" + RichTextFormat.getUnderLine(item.getTransportTotalFee()) + "。综合税率：" + RichTextFormat.getUnderLine(item.getTransportTaxRtWithUnit())) +
                        "。<br/>1.4　" + BuildConfig.carrier + "运输信息<br/>";
    }

    //车船合同-签约合同
    protected String getContractStr(TransportContractDetails contractDetails) {
        return "平台：" +
                RichTextFormat.getUnderLine(contractDetails.getBasic().getGzPlatName()) +
                "<br/>" +
                BuildConfig.user_type + "：" +
                RichTextFormat.getUnderLine(contractDetails.getBasic().getMemberName()) +
                "<br/>" +
                "日期：" +
                RichTextFormat.getUnderLine(contractDetails.getBasic().getCurrDateInChinese()) +
                "<br/>" +
                "<br/>" +
                "合同基本信息" +
                "<br/>" +
                "1.1　货物名称：" +
                RichTextFormat.getUnderLine(contractDetails.getBookOrder().getOrgin_cargon_kind_name()) +
                "，计量方式：" +
                RichTextFormat.getUnderLine(contractDetails.getBookOrder().getMeasure_type()) +
                "，运输损耗：" +
                RichTextFormat.getUnderLine(contractDetails.getBookOrder().getMaxWastageRtWithUnit()) +
                "<br/>" +
                "1.2　装货地：" +
                RichTextFormat.getUnderLine(contractDetails.getBookOrder().getLoadingAddress()) +
                "，卸货地：" +
                RichTextFormat.getUnderLine(contractDetails.getBookOrder().getUnLoadingAddress()) +
                "，装货日期：" +
                RichTextFormat.getUnderLine(contractDetails.getBookOrder().getLoadingStartDtInChineseFormat()) +
                " 至 " +
                RichTextFormat.getUnderLine(contractDetails.getBookOrder().getLoadingEndDtInChineseFormat()) +
                "，装货时间：" +
                RichTextFormat.getUnderLine(contractDetails.getBookOrder().getTimePeriod("～")) +
                "，运输期限：" +
                RichTextFormat.getUnderLine(contractDetails.getBookOrder().getTransport_days()) +
                "天" +
                "<br/>" +
                "1.3　计费依据：" +
                RichTextFormat.getUnderLine(contractDetails.getBookOrder().getChargeReferType()) +
                "，" + BuildConfig.transporter_require + "：" +
                RichTextFormat.getUnderLine(contractDetails.getTransportType()) +
                "，" + BuildConfig.multi_transport_label + "：" +
                RichTextFormat.getUnderLine(contractDetails.getBookOrder().getMultiTransportFlag()) +
                "，特别要求：" +
                RichTextFormat.getUnderLine(contractDetails.getBookOrder().getSpecial_req()) +
                "，结算方式：" +
                RichTextFormat.getUnderLine(contractDetails.getBookOrder().getSettle_type()) +
                (contractDetails.getBookOrder().getBilling_type() != 1 ?
                        ("。运费总额（元）：" + RichTextFormat.getUnderLine(NumberUtil.format2f(contractDetails.getOrderDtlList().get(0).getBookAmt())) + "。综合税率(%)：" + RichTextFormat.getUnderLine(NumberUtil.format2f(contractDetails.getBookOrder().getTransportTaxRt(true))))
                        :
                        "") +
                "。<br/>" +
                "1.4　" + BuildConfig.carrier + "运输信息";
    }

    //货方合同-签订合同
    protected String getContractStr(CargoContractDetails contractDetails) {
        return "平台：" +
                RichTextFormat.getUnderLine(contractDetails.getContractMemInfo().getGzPlatName()) +
                "<br/>" +
                BuildConfig.user_type + "：" +
                RichTextFormat.getUnderLine(contractDetails.getContractMemInfo().getMemberName()) +
                "<br/>" +
                "日期：" +
                RichTextFormat.getUnderLine(contractDetails.getContractMemInfo().getCurrDateInChinese()) +
                "<br/>" +
                "<br/>" +
                "合同基本信息" +
                "<br/>" +
                "1.1　货物名称：" +
                RichTextFormat.getUnderLine(contractDetails.getCargoInfo().getOrgin_cargon_kind_name()) +
                "，计量方式：" +
                RichTextFormat.getUnderLine(contractDetails.getCargoInfo().getMeasure_type()) +
                "，运输损耗：" +
                RichTextFormat.getUnderLine(contractDetails.getCargoInfo().getMaxWastageRtWithUnit()) +
                "<br/>" +
                "1.2　装货地：" +
                RichTextFormat.getUnderLine(contractDetails.getCargoInfo().getLoadingAddress()) +
                "，卸货地：" +
                RichTextFormat.getUnderLine(contractDetails.getCargoInfo().getUnLoadingAddress()) +
                "，装货日期：" +
                RichTextFormat.getUnderLine(contractDetails.getCargoInfo().getLoadingStartDtInChineseFormat()) +
                " 至 " +
                RichTextFormat.getUnderLine(contractDetails.getCargoInfo().getLoadingEndDtInChineseFormat()) +
                "，装货时间：" +
                RichTextFormat.getUnderLine(contractDetails.getCargoInfo().getTimePeriod("～")) +
                "，运输期限：" +
                RichTextFormat.getUnderLine(contractDetails.getCargoInfo().getTransport_days()) +
                "天" +
                "<br/>" +
                "1.3　计费依据：" +
                RichTextFormat.getUnderLine(contractDetails.getCargoInfo().getChargeReferType()) +
                //车型要求
                "，" + AppTypeConfig.getTransporterTypeLabel(contractDetails.getCargoInfo()) + "：" +
                RichTextFormat.getUnderLine(contractDetails.getCargoInfo().getTransporterRequests()) +
                //整车(船)运输
                "，" + AppTypeConfig.getMultiTransportTypeLabel(contractDetails.getCargoInfo()) + "：" +
                RichTextFormat.getUnderLine(contractDetails.getCargoInfo().getMultiTransportFlag()) +
                "，特别要求：" +
                RichTextFormat.getUnderLine(contractDetails.getCargoInfo().getSpecial_req()) +
                "，结算方式：" +
                RichTextFormat.getUnderLine(contractDetails.getCargoInfo().getSettle_type()) +
                "。<br/>货源运输具体信息<br/>2.1　" +
                contractDetails.getCargoInfo().getBookRefType() + "：" +
                RichTextFormat.getUnderLine(contractDetails.getCargoInfo().getCargoDescWithUnit()) +
                "，运费单价（元/" + contractDetails.getCargoInfo().getCargoUnit() + "含税)：" +
                RichTextFormat.getUnderLine(contractDetails.getCargoInfo().getTotalUnitAmt()) +
                "，运费额（元）：" +
                RichTextFormat.getUnderLine(contractDetails.getCargoInfo().getTransportFeeForCargoOwner()) + "。";
    }
}
