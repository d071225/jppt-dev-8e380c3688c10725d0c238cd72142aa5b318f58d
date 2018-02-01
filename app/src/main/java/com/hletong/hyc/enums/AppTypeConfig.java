package com.hletong.hyc.enums;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.R;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.Source;
import com.hletong.hyc.ui.activity.CargoContractActivity;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.activity.TransportContractActivity;
import com.hletong.hyc.ui.activity.cargo.CargoCompanyCertActivity;
import com.hletong.hyc.ui.activity.cargo.CargoPersonalCertActivity;
import com.hletong.hyc.ui.activity.ship.ShipCompanyChildCertActivity;
import com.hletong.hyc.ui.activity.ship.ShipPersonalChildCertActivity;
import com.hletong.hyc.ui.activity.truck.TruckCompanyChildCertActivity;
import com.hletong.hyc.ui.activity.truck.TruckPersonalChildCertActivity;
import com.hletong.hyc.ui.fragment.AppCargoFragment;
import com.hletong.hyc.ui.fragment.AppTruckShipFragment;
import com.hletong.hyc.ui.fragment.CargoOrderHistoryFragment;
import com.hletong.hyc.ui.fragment.HistoryOrderFragment;
import com.hletong.hyc.ui.fragment.MessageCollectionFragment;
import com.hletong.hyc.ui.fragment.MessageFragment;
import com.hletong.hyc.ui.fragment.PersonalCargoRegisterFragment;
import com.hletong.hyc.ui.fragment.PersonalShipRegisterFragment;
import com.hletong.hyc.ui.fragment.PersonalTruckRegisterFragment;
import com.hletong.hyc.util.Constant;
import com.xcheng.view.EasyView;
import com.xcheng.view.adapter.TabInfo;
import com.xcheng.view.util.JumpUtil;

import java.util.List;


/**
 * Created by ddq on 2017/2/22.
 * 不同app（车船货）配置
 */

public class AppTypeConfig {
    public static void toHistoryOrder(Fragment baseFragment) {
        if (!AppTypeConfig.isCargo()) {
            JumpUtil.toActivity(baseFragment, CommonWrapFragmentActivity.class,
                    CommonWrapFragmentActivity.getBundle(EasyView.getString(R.string.historyOrder),
                            HistoryOrderFragment.class, null));
        } else {
            JumpUtil.toActivity(baseFragment, CommonWrapFragmentActivity.class,
                    CommonWrapFragmentActivity.getBundle("货源历史",
                            CargoOrderHistoryFragment.class, null));
        }

    }

    public static void toMemberCert(Activity activity) {
        boolean isCompany = LoginInfo.isCompany();
        Bundle bundle = new Bundle();
        bundle.putString("update_from", "2");
        if (isTruck()) {
            if (isCompany) {
                JumpUtil.toActivity(activity, TruckCompanyChildCertActivity.class, bundle);
            } else {
                JumpUtil.toActivity(activity, TruckPersonalChildCertActivity.class, bundle);
            }
        } else if (isShip()) {
            if (isCompany) {
                JumpUtil.toActivity(activity, ShipCompanyChildCertActivity.class, bundle);
            } else {
                JumpUtil.toActivity(activity, ShipPersonalChildCertActivity.class, bundle);
            }
        } else if (isCargo()) {
            if (isCompany) {
                JumpUtil.toActivity(activity, CargoCompanyCertActivity.class, bundle);
            } else {
                JumpUtil.toActivity(activity, CargoPersonalCertActivity.class, bundle);
            }
        }
    }

    public static int getOsType() {
        if (isCargo()) {
            return 1;
        } else if (isTruck()) {
            return 3;
        } else {
            return 5;
        }
    }

    /**
     * @param activity 当前Activity对象
     * @return 是否主动补充资料
     */
    public static boolean removeMustInput(Activity activity) {
        Bundle bundle = activity.getIntent().getExtras();
        if (bundle == null) return false;
        //1 代表被动业务补充 ，2代表主动补充，屏蔽必填项
        String update_from = bundle.getString("update_from", "1");
        if (update_from.equals("2")) {
            clearTextMustInputFlag(activity.getWindow().getDecorView());
            return true;
        }
        return false;
    }

    private static void clearTextMustInputFlag(View rootView) {
        if (rootView instanceof ViewGroup) {
            ViewGroup vp = (ViewGroup) rootView;
            for (int i = 0; i < vp.getChildCount(); i++) {
                View child = vp.getChildAt(i);
                if (child instanceof TextView && !(child instanceof EditText)) {
                    TextView textView = (TextView) child;
                    String text = textView.getText().toString();
                    if (text.contains("*")) {
                        textView.setText(text.replace("*", ""));
                    }
                } else {
                    clearTextMustInputFlag(child);
                }
            }
        }
    }

    public static String getUndoTypes() {
        if (isTransporter()) {
            return "01,02,03,04,06,10,11,12,14,50,61,62,201,202,203,205";
            //return "01,02,03,04,06,10,11,12,14,50,61,62";
        }
        return "03,04,05,11,12,61,62,200,204,300";

    }

    public static Class<? extends Fragment> getAppFragment() {
        if (isTransporter()) {
            return AppTruckShipFragment.class;
        }
        return AppCargoFragment.class;
    }

    public static String getUserGuideUrl() {
        if (isCargo()) {
            return Constant.getUrl(Constant.USER_GUIDE_CARGO);
        }
        return Constant.getUrl(Constant.USER_GUIDE_TRUCK_OR_SHIP);
    }

    public static int[] getGuidePic() {
        if (isTruck() || isShip()) {
            return new int[]{
                    R.drawable.guide_1, R.drawable.guide_2, R.drawable.guide_3, R.drawable.guide_4};
        } else {
            return new int[]{
                    R.drawable.guide_cargo_1, R.drawable.guide_cargo_2, R.drawable.guide_cargo_3, R.drawable.guide_cargo_4};
        }
    }

    /**
     * 检车船号
     **/
    public static String getCheckPlateOrShipPath() {
        if (isTruck()) {
            return "checkPlate";
        } else if (isShip()) {
            return "checkShip";
        }
        return null;
    }

    public static String getAppTypePath() {
        if (isTruck()) {
            return "truck";
        } else if (isShip()) {
            return "ship";
        } else if (isCargo()) {
            return "cargo";
        }
        return null;
    }

    public static String getExitMemberPath() {
        if (isTruck()) {
            return "truckRefundApp";
        } else if (isShip()) {
            return "shipRefundApp";
        } else if (isCargo()) {
            return "cargoRefundApp";
        }
        return null;
    }

    public static String getExitMemberUUIDKey() {
        if (isTruck()) {
            return "truckUuid";
        } else if (isShip()) {
            return "shipUuid";
        }
        //货主版没有UUID的EKY
        return null;
    }

    public static Class<? extends Fragment> getRegisterPersonFragmentClazz() {
        if (isTruck()) {
            return PersonalTruckRegisterFragment.class;
        } else if (isShip()) {
            return PersonalShipRegisterFragment.class;
        } else if (isCargo()) {
            return PersonalCargoRegisterFragment.class;
        }
        return null;
    }

    //获取待办消息的type参数
    public static String getUpcomingMessageType() {
        if (isTransporter()) {
            //车船方
            return "11,12,14,50,61,62,201,203,205";
        }
        //货方
        return "11,12,61,62,200,204,300";
    }

    public static String getContractHistorySI() {
        if (isTransporter())
            return Constant.getUrl(Constant.CONTRACT_HISTORY_TRANSPORTER);//车船
        //货方
        return Constant.getUrl(Constant.CONTRACT_HISTORY_CARGO);
    }

    //车船货预览未签合同
    public static String getContractPreviewSI() {
        if (isCargo())
            return Constant.CARGO_CONTRACT_BY_CARGO_UUID;//货方
        return Constant.TRANSPORT_CONTRACT_BY_BOOK_UUID;//车船
    }

    public static Class<? extends Activity> getContractActivity() {
        if (isCargo())
            return CargoContractActivity.class;//货方
        return TransportContractActivity.class;//车船
    }

    //历史合同详情接口
    public static String getHistoryContractDetailsSI() {
        if (isCargo()) {//货方
            return Constant.CARGO_CONTRACT_BY_CONTRACT_UUID;
        }
        //车船
        return Constant.TRANSPORT_CONTRACT_BY_CONTRACT_UUID;
    }

    //车船类型要求标签
    public static String getTransporterTypeLabel(Source source) {
        if (isCargo()) {
            switch (source.getTransport_type()) {
                case 1://车辆
                    return "车型要求";
                case 2://船舶
                    return "船舶要求";
            }
            return "";
        }
        return BuildConfig.transporter_require;
    }

    //整车船要求标签
    public static String getMultiTransportTypeLabel(Source source) {
        if (isCargo()) {
            switch (source.getTransport_type()) {
                case 1://车辆
                    return "整车运输";
                case 2://船舶
                    return "整船运输";
            }
            return "";
        }
        return BuildConfig.multi_transport_label;
    }

    public static String[] getRegisterContract() {
        String m[] = {"《货方会员入会协议》", "《车辆会员入会协议》", "《船舶会员入会协议》"};
        String[] contract = {null, "《会员守则》", "《违规违约处理细则》", "《结算细则》"};
        contract[0] = m[BuildConfig.app_type - 1];
        return new String[]{contract[0]};
    }

    public static void getMessageTabs(List<TabInfo> tabInfos) {
        tabInfos.add(new TabInfo("all", "全部", MessageFragment.class, MessageCollectionFragment.getBundle(null, null)));
        tabInfos.add(new TabInfo("un_read", "未读", MessageFragment.class, MessageCollectionFragment.getBundle("1", null)));
        if (isTransporter()) {//车船
            tabInfos.add(new TabInfo("startNotify", "启程通知", MessageFragment.class, MessageCollectionFragment.getBundle(null, "5")));
            tabInfos.add(new TabInfo("leaveNotify", "驶离通知", MessageFragment.class, MessageCollectionFragment.getBundle(null, "6")));
            tabInfos.add(new TabInfo("jiaofeitongzhi", "缴费通知", MessageFragment.class, MessageCollectionFragment.getBundle(null, "7")));
            tabInfos.add(new TabInfo("wodetoubiao", "我的投标", MessageFragment.class, MessageCollectionFragment.getBundle(null, "18")));
        } else {//货方
            tabInfos.add(new TabInfo("jiaofeitongzhi", "缴费通知", MessageFragment.class, MessageCollectionFragment.getBundle(null, "7")));
            tabInfos.add(new TabInfo("legal_notify", "法务通知", MessageFragment.class, MessageCollectionFragment.getBundle(null, "9，10")));
            tabInfos.add(new TabInfo("cargo_notify", "收发货通知单", MessageFragment.class, MessageCollectionFragment.getBundle(null, "13")));
        }
        tabInfos.add(new TabInfo("payNotify", "支付通知", MessageFragment.class, MessageCollectionFragment.getBundle(null, "40,41,42")));
        tabInfos.add(new TabInfo("piaojuxinxi", "票据信息", MessageFragment.class, MessageCollectionFragment.getBundle(null, "50,51")));
        tabInfos.add(new TabInfo("trade", "自主交易", MessageFragment.class, MessageCollectionFragment.getBundle(null, "60,61,62,63,64,65")));
        tabInfos.add(new TabInfo("platformNotice", "平台公告", MessageFragment.class, MessageCollectionFragment.getBundle(null, "71")));

    }

    //自主交易完成的确认参数
    public static int getSelfTradeConfirmType() {
        if (isTransporter())
            return 2;
        else
            return 1;
    }

    public static String getAppName() {
        if (isCargo())
            return "惠龙易通货主版";
        else if (isTruck())
            return "惠龙易通车主版";
        else
            return "惠龙易通船主版";
    }

    private static boolean isType(int type) {
        return BuildConfig.app_type == type;
    }

    //货主
    public static boolean isCargo() {
        return isType(1);
    }

    //车主
    public static boolean isTruck() {
        return isType(2);
    }

    //船主
    public static boolean isShip() {
        return isType(3);
    }

    //承运方
    public static boolean isTransporter() {
        return isTruck() || isShip();
    }

    //拨打电话的主叫方
    public static int getCaller() {
        //货方呼叫车船
        if (isCargo())
            return 1;
        else//车船呼叫货方
            return 2;
    }

    //拨打电话的被叫方
    public static int getReverseCaller() {
        if (isTransporter())//车船呼叫货方
            return 1;
        //货方呼叫车船
        return 2;
    }
}
