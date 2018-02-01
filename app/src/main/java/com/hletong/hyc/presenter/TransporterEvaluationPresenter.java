package com.hletong.hyc.presenter;

import android.content.DialogInterface;

import com.hletong.hyc.contract.TransporterEvaluationContract;
import com.hletong.hyc.model.TransporterEvaluation;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.Validator;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.model.repository.SimpleCallback;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.util.FinishOptions;
import com.hletong.mob.util.ParamsHelper;

/**
 * Created by dongdaqing on 2017/5/15.
 */

public class TransporterEvaluationPresenter extends BasePresenter<TransporterEvaluationContract.View> implements TransporterEvaluationContract.Presenter {
    private String tradeUuid;
    private String billingType;
    private TransporterEvaluation mEvaluation;
    private final int[] SCORES = {50, 60, 70, 90, 100};

    public TransporterEvaluationPresenter(TransporterEvaluationContract.View view, String tradeUuid, String billingType) {
        super(view);
        this.tradeUuid = tradeUuid;
        this.billingType = billingType;
    }

    @Override
    public void start() {
        ItemRequestValue<TransporterEvaluation> requestValue = new ItemRequestValue<TransporterEvaluation>(
                getView().hashCode(),
                Constant.getUrl(Constant.TRANSPORTER_EVALUATION_GET_DETAILS),
                new ParamsHelper().put("tradeUuid", tradeUuid).put("billingType", billingType)){};

        getDataRepository().loadItem(requestValue, new SimpleCallback<TransporterEvaluation>(getView()) {
            @Override
            public void onSuccess(TransporterEvaluation response) {
                mEvaluation = response;
                if (response.getBilling_type() == 3) {
                    getView().initSelfTradeView();
                } else {
                    getView().initNormalView();
                }
                getView().initViewWithData(response, response.getBilling_type() == 3);
            }
        });
    }

    @Override
    public void submit(final int timeliness, final int damage, final int attitude, final String remark) {
        if (Validator.isNotNull(mEvaluation,getView(),"参数有误，无法提交")
                && Validator.isAboveZero(timeliness,getView(),"请评价及时性")
                && Validator.isAboveZero(damage,getView(),"请评价货损货差")
                && Validator.isAboveZero(attitude,getView(),"请评价服务态度")){
            getView().showSubmitAlert(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    simpleSubmit(
                            Constant.getUrl(Constant.TRANSPORTER_EVALUATION_SUBMIT),
                            new ParamsHelper()
                                    .put("tradeUuid", mEvaluation.getTradeUuid())
                                    .put("timeliness", SCORES[timeliness - 1])
                                    .put("damage", SCORES[damage - 1])
                                    .put("attitude", SCORES[attitude - 1])
                                    .put("remark", remark));
                }
            });
        }
    }

    @Override
    protected void simpleSubmitSucceed(CommonResult cr) {
        //提交评价成功
        getView().success(cr.getErrorInfo());
        getView().finishWithOptions(FinishOptions.FORWARD_RESULT());
    }
}
