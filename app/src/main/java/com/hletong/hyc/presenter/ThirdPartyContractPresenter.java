package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;
import android.text.Html;

import com.google.gson.reflect.TypeToken;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.ThirdPartyContract;
import com.hletong.hyc.model.CargoOwnerInfo;
import com.hletong.hyc.model.ContractZZJY;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.TransportContractDetails;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.model.repository.SimpleCallback;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.util.ParamsHelper;
import com.hletong.mob.util.SPUtils;

import java.util.Locale;

/**
 * Created by ddq on 2017/1/10.
 * 两个三方协议：自主交易的和自主开票的，这两者的区别只有两处：1.开头的货方车船会员信息，自主交易的多一些，2、费用结算部分描述
 */

public class ThirdPartyContractPresenter extends BasePresenter<ThirdPartyContract.View> implements ThirdPartyContract.Presenter {
    private TransportContractDetails mContractDetails;
    private Source mSource;
    private int bookRefType = -1;
    private double price;
    private double count;

    public ThirdPartyContractPresenter(ThirdPartyContract.View view, int bookRefType) {
        super(view);
        this.bookRefType = bookRefType;
    }

    public ThirdPartyContractPresenter(ThirdPartyContract.View view, TransportContractDetails contractDetails) {
        super(view);
        mContractDetails = contractDetails;
    }

    public ThirdPartyContractPresenter(ThirdPartyContract.View view, Source source) {
        super(view);
        mSource = source;
    }

    @Override
    public void start() {
        if (mContractDetails != null) {
            mContractDetails.getBookOrder().setTransport_unit_amt(mContractDetails.getOrderDtlList().get(0).getBookUnitAmt());
            getView().showContract(Html.fromHtml(prepare(mContractDetails.getBookOrder(), mContractDetails.getCarrierName(), "", "", "")));
        } else if (mSource != null) {
            CargoOwnerInfo coi = SPUtils.getBean("CargoOwnerInfo", new TypeToken<CargoOwnerInfo>() {
            });
            if (coi != null) {
                mSource.setCargo_owner_sname(coi.getMemberSname());
                mSource.setCargo_owner_addr(coi.getMemberAddressDetail());
                mSource.setCargo_owner_fax(coi.getFax());
                mSource.setCargo_owner_tel(coi.getMemberTel());
            }
            getView().showContract(Html.fromHtml(prepare(mSource, " ", "", "", "")));
        } else {
            getView().setupContractRequest();
        }
    }

    @Override
    public void loadContractByContractID(String contractUUID) {
        ItemRequestValue<ContractZZJY> requestValue = new ItemRequestValue<ContractZZJY>(
                getView().hashCode(),
                Constant.getUrl(Constant.SELF_TRADE_CONTRACT_INFO),
                new ParamsHelper().put("agrtUuid", contractUUID),
                "data") {
        };
        setupRequest(requestValue);
    }

    @Override
    public void loadContractByCargoID(String cargoUUID, double price, double count) {
        this.price = price;
        this.count = count;

        ItemRequestValue<ContractZZJY> requestValue = new ItemRequestValue<ContractZZJY>(
                getView().hashCode(),
                Constant.getUrl(Constant.SELF_TRADE_CONTRACT_INFO_PREVIEW),
                new ParamsHelper().put("cargoUuid", cargoUUID),
                "data") {
        };
        setupRequest(requestValue);
    }

    private void setupRequest(ItemRequestValue<ContractZZJY> requestValue) {
        getDataRepository().loadItem(requestValue, new SimpleCallback<ContractZZJY>(getView()) {
            @Override
            public void onSuccess(@NonNull ContractZZJY response) {
                response.setBilling_type(3);//返回值里面没有，自己加上
                //议价摘牌的时候，这个response里面可能没有价格信息，所以就要从外面传递进来
                if (price != 0)
                    response.setUnit_amt(price);
                if (count != 0)
                    response.setCargo(count);
                if (bookRefType != -1)
                    response.setBookRefType(new DictionaryItem(String.valueOf(bookRefType), ""));

                getView().showContract(Html.fromHtml(prepare(
                        response,
                        response.getCarrier_member_sname() == null ? "" : response.getCarrier_member_sname(),
                        response.getCarrier_member_mobile() == null ? "" : response.getCarrier_member_mobile(),
                        response.getCarrier_member_addr() == null ? "" : response.getCarrier_member_addr(),
                        response.getCarrier_member_tel() == null ? "" : response.getCarrier_member_tel())
                ));
            }
        });
    }

    /**
     * 自主交易三方协议，货物运输三方协议
     *
     * @param mSource
     * @param cn      车船会员名
     * @param cp      车船手机号码
     * @param ca      车船联系地址
     * @param ct      车船电话号码
     * @return
     */
    private String prepare(Source mSource, String cn, String cp, String ca, String ct) {
        return getView().getString(R.string.third_party_sentence_1_platform) +
                (mSource.getBilling_type() == 3 ? //自主交易的
                        String.format(Locale.CHINESE, getView().getString(R.string.third_party_sentence_1_source_provider),
                                mSource.getCargo_owner_sname() == null ? "" : mSource.getCargo_owner_sname(),
                                mSource.getCargo_owner_tel() == null ? "" : mSource.getCargo_owner_tel(),
                                mSource.getCargo_owner_addr() == null ? "" : mSource.getCargo_owner_addr(),
                                mSource.getCargo_owner_fax() == null ? "" : mSource.getCargo_owner_fax())
                        :
                        //自主开票的
                        String.format(Locale.CHINESE, getView().getString(R.string.third_party_sentence_1_provider_and_carrier),
                                mSource.getCargo_owner_sname() == null ? "" : mSource.getCargo_owner_sname(),
                                cn)) +
                (mSource.getBilling_type() == 3 ?
                        //自主交易的内容
                        String.format(Locale.CHINESE, getView().getString(R.string.third_party_sentence_1_carrier), cn, cp, ca, ct)
                        :
                        //自主开票就不需要了
                        "") +
                getView().getString(R.string.third_party_sentence_2) +
                getView().getString(R.string.third_party_sentence_3_block_1) +
                getView().getString(R.string.third_party_sentence_3_block_1_sub_1) +
                getView().getString(R.string.third_party_sentence_3_block_1_sub_2) +
                getView().getString(R.string.third_party_sentence_3_block_1_sub_3) +
                getView().getString(R.string.third_party_sentence_3_block_1_sub_3_sub_1) +
                getView().getString(R.string.third_party_sentence_3_block_1_sub_3_sub_2) +
                getView().getString(R.string.third_party_sentence_3_block_1_sub_3_sub_3) +
                getView().getString(R.string.third_party_sentence_3_block_1_sub_3_sub_4) +
                getView().getString(R.string.third_party_sentence_3_block_1_sub_4) +
                getView().getString(R.string.third_party_sentence_3_block_1_sub_4_sub_1) +
                getView().getString(R.string.third_party_sentence_3_block_1_sub_4_sub_2) +
                String.format(Locale.CHINESE, getView().getString(R.string.third_party_sentence_3_block_2),
                        mSource.getOrgin_cargon_kind_name() == null ? "" : mSource.getOrgin_cargon_kind_name(),
                        mSource.getVolume("/"),
                        mSource.getBook_ref_type() == 0 ? mSource.getCargoWeightWithUnit() : "/",
                        mSource.getBook_ref_type() == 1 ? mSource.getCargoNumbersWithUnit() : "/",
                        mSource.getTransportUnitAmt() + mSource.getUnitForFee(),
                        mSource.getTransportType() == null ? "" : mSource.getTransportType()) + (mSource.getTransport_type() == 1 ?
                //车辆类型，长度
                String.format(Locale.CHINESE, getView().getString(R.string.third_party_sentence_3_block_2_truck),
                        mSource.getTransporterRequests(),
                        mSource.getCarrier_length_type_() == null ? "无" : mSource.getCarrier_length_type_())
                :
                //船舶
                String.format(Locale.CHINESE, getView().getString(R.string.third_party_sentence_3_block_2_ship),
                        mSource.getTransporterRequests()))
                +
                //时间，地址
                String.format(Locale.CHINESE, getView().getString(R.string.third_party_sentence_3_block_2_),
                        mSource.getLoadingPeriod() == null ? "" : mSource.getLoadingPeriod(),
                        mSource.getLoadingAddress(),
                        mSource.getUnLoadingAddress()) +
                getView().getString(R.string.third_party_sentence_3_block_2_sub_1) +
                getView().getString(R.string.third_party_sentence_3_block_2_sub_2) +
                getView().getString(R.string.third_party_sentence_3_block_2_sub_3) +
                getView().getString(R.string.third_party_sentence_3_block_2_sub_4) +
                getView().getString(R.string.third_party_sentence_3_block_2_sub_5) +
                getView().getString(R.string.third_party_sentence_3_block_2_sub_6) +
                getView().getString(R.string.third_party_sentence_3_block_2_sub_7) +
                getView().getString(R.string.third_party_sentence_3_block_2_sub_8) +
                getView().getString(R.string.third_party_sentence_3_block_2_sub_8_sub_1) +
                getView().getString(R.string.third_party_sentence_3_block_2_sub_8_sub_2) +
                getView().getString(R.string.third_party_sentence_3_block_2_sub_8_sub_3) +
                getView().getString(R.string.third_party_sentence_3_block_2_sub_8_sub_4) +
                getView().getString(R.string.third_party_sentence_3_block_2_sub_9) +
                (mSource.getBilling_type() == 3 ?
                        //自主交易的费用结算
                        getView().getString(R.string.third_party_sentence_3_block_2_sub_10_zzjy)
                        :
                        //自主开票的费用结算
                        getView().getString(R.string.third_party_sentence_3_block_2_sub_10_zzkp)) +
                getView().getString(R.string.third_party_sentence_3_block_3);
    }
}
