package com.hletong.hyc.contract;

import android.text.Spanned;

import com.google.gson.internal.LinkedTreeMap;
import com.hletong.hyc.model.ContractCarrierInfo;
import com.hletong.hyc.model.ContractItemForDetails;
import com.hletong.hyc.model.Order;
import com.hletong.hyc.model.validate.CargoContractInfo;
import com.hletong.hyc.model.validate.ContractInfo;
import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.view.CountDownView;
import com.hletong.mob.architect.view.ICommitSuccessView;
import com.hletong.mob.architect.view.ITransactionView;

import java.util.List;

/**
 * Created by ddq on 2017/1/4.
 * 车船货的合同
 */

public interface Contract {
    interface View extends CountDownView, ITransactionView, ICommitSuccessView {
        void showHtmlContract(Spanned contract);//显示合同

        void showPasswordArea();//初始化密码区域，签约合同时需要输入密码

        void hideTimeView();//用户签合同

        void showTimeOutView();//查看历史合同
    }

    interface TransporterView extends View {
        void showContractArea();//自主开票货源货物运输三方协议

        void showContractInfo(List<Order> list, int billingType, LinkedTreeMap<String, List<ContractCarrierInfo>> map, String unit);//这个是车船签合同时

        void showSignedContractInfo(List<ContractItemForDetails.Order> list, int billingType, String unit);//这个是车船查看合同时(历史)

        void showProtocolTransportHint(CharSequence hint);//兜底运输提示
    }

    interface Presenter extends IBasePresenter {
        //已经超过了能签约的时间
        void signTimeOut();
    }

    interface TransporterPresenter extends Presenter {
        //查看三方合同
        void viewContract();

        //签合同
        void submitContract(ContractInfo contractInfo);
    }

    interface CargoPresenter extends Presenter {
        //签合同
        void submitContract(CargoContractInfo contractInfo);
    }
}
