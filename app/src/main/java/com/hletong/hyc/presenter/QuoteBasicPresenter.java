package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;

import com.hletong.hyc.contract.QuoteContract;
import com.hletong.hyc.model.QuoteInfos;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.model.repository.SimpleCallback;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.util.ParamsHelper;

/**
 * Created by dongdaqing on 2017/5/31.
 */

public class QuoteBasicPresenter extends BasePresenter<QuoteContract.BasicView> implements QuoteContract.BasicPresenter {
    private String cargoUuid;

    public QuoteBasicPresenter(String cargoUuid, QuoteContract.BasicView view) {
        super(view);
        this.cargoUuid = cargoUuid;
    }

    @Override
    public void loadDetails() {
        if (cargoUuid == null) {
            handleMessage("参数出错");
            return;
        }

        ItemRequestValue<QuoteInfos> requestValue = new ItemRequestValue<QuoteInfos>(
                getView().hashCode(),
                Constant.getUrl(Constant.QUOTE_INFOS_CARGO),
                new ParamsHelper().put("cargoUuid", cargoUuid)){};

        getDataRepository().loadItem(requestValue, new SimpleCallback<QuoteInfos>(getView()) {
            @Override
            public void onSuccess(@NonNull QuoteInfos response) {
                Source source = response.getCargoMap();
                getView().initWithData(source);
                getView().initList(response, source.getTrans_type() == 300 && "4".equals(source.getCargo_src_type()));
//                if (!RequestState.isEmptyData(response.getQuoteList())) {
//                    List<Quote> copy = new ArrayList<>(response.getQuoteList());
//                    copy.add(0, new Quote());//这是head
//                    mView.showDicts(copy, true);
//                } else {
//                    BaseError error = new DataError(ErrorState.NO_DATA);
//                    error.setId(ListRequestValue.REFRESH);
//                    onError(error);
//                }
            }
        });
    }
}
