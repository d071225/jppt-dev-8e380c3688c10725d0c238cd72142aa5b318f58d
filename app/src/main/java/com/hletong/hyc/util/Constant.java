package com.hletong.hyc.util;

import android.annotation.SuppressLint;
import android.provider.Settings;
import android.text.TextUtils;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.mob.util.AppManager;

/**
 * Created by cx on 2016/10/17.
 */
public class Constant {

    public static final String WX_APPID = "wx65c47df5fc94e431";
    public static final int THUMB_SIZE = 150;

    public static void initHost(String host) {
        HOST = host;
    }

    public static String getUrl(String urlName) {
        return HOST + urlName;
    }

    //DEV
//    private static String HOST = "http://192.168.2.28:8080/hl/";
    //路径规划 10.20.10.127:8080
    private static String HOST = "http://192.168.2.100:8084/hl/";
    private static String TEST_HOST = "http://192.168.2.24:8080/";
    public static final String LOGIN = "mobile/hletong/api/login";
    public static final String LOGOUT = "mobile/hletong/api/logout";
    public static final String REFUND_APP = "action/hletong/mm/" + AppTypeConfig.getExitMemberPath();//申请退会

    public static String getTestUrl(String urlName) {
        return TEST_HOST + urlName;
    }

    /**
     * 检测车牌号
     **/
    public static final String CHECK_PLATE_OR_SHIP = "mobile/public/hletong/page/memberRegister/" + AppTypeConfig.getCheckPlateOrShipPath();

    public static final String CHECK_COMPANY_MEMBER_NAME = "mobile/public/hletong/page/memberRegister/checkCompanyNameAndMemberName";

    public static final String CHECK_MEMBER_NAME = "mobile/public/hletong/page/memberRegister/checkMemberName";
    public static final String CHECK_ID_CARD = "mobile/public/hletong/page/memberRegister/checkPersonalIdentity";
    /**
     * 获取验证码
     **/
    public static final String GET_VERIFY_CODE = "mobile/public/hletong/page/memberRegister/SecurityCheck";
    /**
     * 检验验证码
     **/
    public static final String CHECK_VERIFY_CODE = "mobile/public/hletong/page/memberRegister/SecurityCheckCode";
    /**
     * 公司注册
     **/
    public static final String COMPANY_REGISTER = "mobile/public/hletong/page/memberRegister/" + AppTypeConfig.getAppTypePath() + "/company";
    /**
     * 个人注册
     **/
    public static final String PERSONAL_REGISTER = "mobile/public/hletong/page/memberRegister/" + AppTypeConfig.getAppTypePath() + "/personal";

    /**
     * 修改密码
     */
    public static final String MODIFY_PASSWORD = "mobile/hletong/api/modifyPasswd";
    /*货源公告**/
    public static final String GOODS_NOTICE = "mobile/public/hletong/cdm/cargoforecast/notice/list";
    public static final String GUAJIA_SOURCE_LIST = "mobile/hletong/api/tasker/todo/listAll";//挂价摘牌列表
    /**
     * cargoUuid:C360960CA83E497CA93A43FF4BCDB644
     * roundUuid:8422D989D5FC4F4E9BE3BFED96F9B21F
     * deductTaxRt:0.11
     * delistItems[0].carrierMemberSubCode:CLHY99999854
     * delistItems[0].carrierNo:浙A88888
     * delistItems[0].bookUnitCt:100
     * delistItems[0].bookWeight:0
     */
    public static final String GUAJIA = "mobile/hletong/api/cdm/cargo_book/fixed_trade/book";//对某项货物进行摘牌操作（提交填写的摘牌信息）
    public static final String COMPETITIVE_BIDDING_LIST = "mobile/hletong/api/tasker/todo/listAll";//竞价投标列表

    /**
     * statusQueryType:1=> 竞价中， 2 => 竞价历史列表
     */
    public static final String BID_LIST = "mobile/hletong/api/cdm/cargo_book/transport_bid_by_condition/query";//竞价列表，通过传递参数来查询历史或者竞价中的列表

    public static final String CALCULATE_BID_PRICE = "mobile/hletong/api/cdm/cargo_book/calcBidTaxAmt";//计算竞价收入
    /**
     * cargoUuid:FF57F41D090D46DB8605EBCFAEDC3353
     * roundUuid:BA58C1CC622E48F384495FA13CF7944B
     * deductTaxRt:0.11
     * delistItems[0].carrierMemberSubCode:CLHY99999854
     * delistItems[0].carrierNo:浙A88888
     * delistItems[0].bookUnitCt:1
     * delistItems[0].bookWeight:0
     * delistItems[0].undefined:0
     * delistItems[0].bookUnitAmt:9.00
     */
    public static final String COMPETITIVE_BIDDING = "mobile/hletong/api/cdm/cargo_book/bid_trade/book";//提交竞价信息
    /**
     * bidUuid:EB762710725940FEBF36EBE9DA8913C4
     * bidUnitAmt:1.8
     */
    public static final String MODIFY_CB_PRICE = "mobile/hletong/api/cdm/cargo_book/transport_did_dtl/update";//修改竞价价格
    public static final String COMPETITIVE_ROUND_TIME_LEFT = "mobile/hletong/api/cdm/cargo_book/bid_round_participant_info/get";//竞价大厅当前场次的剩余时间，和竞价信息

    public static final String TRUCKS = "mobile/hletong/page/mm/truck";//获取车辆信息-运力预报使用
    public static final String SHIPS = "mobile/hletong/page/mm/ship";//获取船舶信息-运力预报使用
    /**
     * transportType:1
     * cargoUuid:E1FC200E55EB46F7BD59D4DF3109D701
     */
    public static final String TRANSPORTER_ZP = "mobile/hletong/api/cdm/cargoforecast/wrtr/transport/get";//获取车辆（船舶）信息-(竞挂价)摘牌使用

    public static final String TRANSPORT_HISTORY = "mobile/hletong/api/CdmTransportForecastQueryApp/getCdmTransportForecastInfo";//运力预报历史
    public static final String SUBMIT_TRANSPORT_FORECAST = "mobile/hletong/api/cdm/transport/forecast/submit";//发布运力预告
    //forecastUuid:4973CEB9D48E47BDB4AFC45FA4BC51D5
    public static final String CANCEL_TRANSPORT_SCHEDULE = "mobile/hletong/api/cdm/transport/forecast/abort";//取消运力预报;

    //    public static final String CONTRACT_HISTORY = "mobile/hletong/api/cdm/history-info/transport-contract/get";//网签承运历史
    public static final String CONTRACT_HISTORY_TRANSPORTER = "action/hletong/api/cdm/transport/contract/hist/list";//网签承运历史（新） 自主交易模块加入后，替换原有接口
    public static final String CONTRACT_HISTORY_CARGO = "action/hletong/api/cdm/cargo/contract/hist/list";//货方承运历史接口
    /**
     * type:02
     * status:2
     * limit:15
     * start:1
     */
    public static final String CONTRACT_UNSIGNED = "mobile/hletong/api/tasker/todo/listAll";//网签承运 待签约

    /**
     * bookUuid:50BCE75116524ED1B684EF1E90ED5918
     * signSubReqDtoList[0].transporterName:ddq
     * signSubReqDtoList[0].transporterIdentityCode:429005198710245214
     * signSubReqDtoList[0].planLoadingDttm:20170119161700
     * signSubReqDtoList[0].bookDtlUuid:5791EBEB69B540A7B015BD601E5A25FF
     * signSubReqDtoList[0].carrierNo:浙A88888
     * signSubReqDtoList[0].checkTransPwd:670b14728ad9902aecba32e22fa4f6bd
     * signSubReqDtoList[0].transPwd:670b14728ad9902aecba32e22fa4f6bd
     * signSubReqDtoList[1].transporterName:佛挡杀佛
     * signSubReqDtoList[1].transporterIdentityCode:429005198710201010
     * signSubReqDtoList[1].planLoadingDttm:20170119165900
     * signSubReqDtoList[1].bookDtlUuid:72E63842EF624CFBA152EE9478062605
     * signSubReqDtoList[1].carrierNo:浙A12345
     * signSubReqDtoList[1].checkTransPwd:670b14728ad9902aecba32e22fa4f6bd
     * signSubReqDtoList[1].transPwd:670b14728ad9902aecba32e22fa4f6bd
     */
    public static final String SIGN_CONTRACT_TRANSPORTER = "mobile/hletong/api/tasker/transport/contract/sign";//合同签订-车船
    public static final String SIGN_CONTRACT_CARGO = "mobile/hletong/api/tasker/cargo/contract/sign";//合同签订-货方

    public static final String TRANSPORT_CONTRACT_BY_BOOK_UUID = "mobile/hletong/api/tasker/transport/contract/view";//通过book_uuid获取合同详情
    public static final String CARGO_CONTRACT_BY_CARGO_UUID = "mobile/hletong/api/tasker/cargo/contract/view";//通过cargo_uuid获取合同详情
    /**
     * contractUuid:FEB4399D246443DD979BE7B6EABDD356
     */
    public static final String TRANSPORT_CONTRACT_BY_CONTRACT_UUID = "mobile/hletong/api/cdm/history-info/transport-contract/get";//通过contractUuid获取合同详情
    public static final String CARGO_CONTRACT_BY_CONTRACT_UUID = "mobile/hletong/api/cdm/history-info/cargo-contract/get";//通过contractUuid获取合同详情
    /**
     * type:03
     * status:2
     * limit:15
     * start:1
     */
    public static final String INVOICE_LIST = "mobile/hletong/api/tasker/todo/listAll";//发货单列表

    public static final String INVOICE_HISTORY = "mobile/hletong/api/tasker/invoice/history";//发货单历史

    /**
     * unitCt:5
     * weight:0
     * deliverPassword:96e79218965eb72c92a549dd5a330112
     * loadingPassword:96e79218965eb72c92a549dd5a330112
     * invoiceUuid:2CF272BBCF8C4A4E8D03DCE9D803DFA3
     * tradeUuid:5CFB2559A92F41338D37E91662628F66
     * loadingLongitude:
     * loadingLatitude:
     */
    public static final String UPLOAD_INVOICE = "mobile/hletong/api/cdm/transport/bizbill/invoice/postback";//上传发货单

    /**
     * memberType:2
     * limit:15
     * start:1
     */
    public static final String RECEIPT_HISTORY = "mobile/hletong/api/tasker/receipt/history";//收货单历史
    /**
     * type:04
     * status:2
     * limit:15
     * start:1
     */
    public static final String RECEIPT_LIST = "mobile/hletong/api/tasker/todo/listAll";//待上传收货单

    /**
     * unitCt:5
     * weight:0
     * receivingPassword:670b14728ad9902aecba32e22fa4f6bd
     * unloadPassword:670b14728ad9902aecba32e22fa4f6bd
     * receiptUuid:35208AF0B3194DA688C3491E20D9E309
     * tradeUuid:5CFB2559A92F41338D37E91662628F66
     * dissentFlag:0
     * unloadLatitude:
     * unloadLongitude:
     */
    //收货单回传
    public static final String UPLOAD_RECEIPT = "mobile/hletong/api/cdm/transport/bizbill/receipt/postback";//上传收货单
    //运单回传
    public static final String UPLOAD_RECEIPT_UPCOMING = "mobile/hletong/api/cdm/transport/bizbill/way-bill/postback";//上传收货单-从待办进入

    /**
     * bidUuid:xxxxxxxx
     */
    public static final String CARGO_DETAIL_CB_HISTORY = "mobile/hletong/api/cdm/cargo_book/transport_bid_list/detail";//货源详情-竞价历史进入
    /**
     * cargoUuid
     */
    public static final String CARGO_DETAIL_GUAJIA = "mobile/public/hletong/cdm/cargoforecast/notice/info/get";//货源详情

    public static final String ORDER_HISTORY_TRANSPORTER = "mobile/hletong/api/cdm/cargo_book/history/list/get";//车船历史订单
    public static final String ORDER_HISTORY_CARGO = "mobile/hletong/api/cdm/cargoforecast/history/trade-info/get";//货方历史订单

    /**
     * tradeUuid:725297FAFF664D749D4DB5D15D670CF0
     */
    public static final String ORDER_HISTORY_DETAILS = "mobile/hletong/api/cdm/cargo_book/history/dtl/query";//承运记录的详情

    /***
     * 消息页面
     ***/
    public static final String MESSAGE = "mobile/hletong/api/tasker/getMessageByStatusAndInformType";//货源详情
    public static final String MARK_AS_READ = "mobile/hletong/api/CcInformApp/informAppEdit";//标记未读消息为已读
    public static final String COMPLAIN = "mobile/hletong/api/cdm/selftrade/transport/bill/complaint";//自主交易违约投诉
    /**
     * type:11,12,14,50,61,62
     * limit:15
     * start:1
     */
    public static final String TODO = "mobile/hletong/api/tasker/todo/page";//待办列表

    public static final String STAGNATION_UPLOAD = "mobile/hletong/api/fc/legalBillDtl/uploadFile";//上传滞压单

    /**
     * legalUuid:732A4B41A23D41B7A1DFB5529B17A730
     * taskerType:11
     */
    public static final String LEGAL_CONFIRM = "mobile/hletong/api/fc/legalBillDtl/confirm";//法务单确认

    /**
     * changeUuid:C7D5B47FF3724760B9F4F8253002BAA4
     */
    public static final String CONTRACT_RESIGN_CONFIRM = "mobile/hletong/api/tasker/unload-change/transport/confirm";//车船补签货方合同确认
    public static final String RESIGN_CONTRACT = "mobile/hletong/api/tasker/cargo/contract/re-sign";//货方补签合同

    public static final String IMAGE_UPLOAD = "mobile/hletong/api/file/upload";
    /**
     * 无需权限
     **/
    public static final String IMAGE_PUBLIC_UPLOAD = "mobile/public/hletong/api/file/upload";


    /**
     * %s是group id
     */
    public static final String FETCH_GROUP_PICTURES_URL = "mobile/hletong/api/file/%s/query";

    /**
     * %s是file id
     */
    public static final String FILE_DOWNLOAD_URL = "mobile/hletong/api/file/%s/download";

    /**
     * 版本更新
     */

    public static final String VERSION_UPDATE = "mobile/public/hletong/api/appVersion/getAppVersion";
    public static final String VERSION_UPDATE_EXTRA = "mobile/public/hletong/api/appVersion/getHiddenInfo";

    /**
     * etc-当前车辆信息
     */

    public static final String VEHICLE_INFO = "action/mobile/etc/get/truck/mesgs";

    /**
     * ETC-提交申请人信息
     */
    public static final String ETC_SUBMIT_INFO = "action/mobile/etc/submit/apply/mesg";
//    public static final String ADD_VEHICLE_INFO = "action/mobile/etc/add/truck/mesgs";

    /**
     * 获取车辆船舶字典自选项
     */
    public static final String TRUCK_OPTIONAL_ITEM = "mobile/public/hletong/sys/dict/getDictItems?dictEntryKeys=plate_color,truck_type,axle_count,trailer_axle,ship_type,etc_vehicle_type,bank_type,sex";

    /**
     * ETC申请记录
     */
    public static final String ETC_APPLY_RECORD = "action/mobile/etc/get/apply/records";

    /**
     * ETC取消申请
     */
    public static final String ETC_CANCEL_APPLY = "action/mobile/etc/revoke/application";


    /**
     * 广告页面
     */

    public static final String HOMEPAGE_AD = "mobile/public/hletong/api/adv/findHomePageAdvInfo";

    /***
     * 待办事项消息http://192.168.2.18/jppt/mobile/hletong/api/tasker/todo/listAll
     ***/
    public static final String UNDO_INFO = "/mobile/hletong/api/tasker/todo/countForTodoType";

    public static final String SELF_TRADE_CONFIRM = "action/hletong/api/cdm/selftrade/transport/bill/confirm";//自主交易确认完成（待办进入）
    public static final String SELF_TRADE_DETAILS = "action/hletong/api/cdm/selftrade/transport/bill/info/get";//自主交易的详情（待办进入）
    public static final String SELF_TRADE_CONTRACT_INFO_PREVIEW = "action/hletong/api/cdm/selftrade/agrt/book/preview";//自主交易之前的三方协议
    public static final String SELF_TRADE_CONTRACT_INFO = "action/hletong/api/cdm/selftrade/agrt/info/get";//自主交易之后的三方协议
    public static final String SELF_TRADE_PRE_DE_LIST_CARGO_DETAILS = "mobile/public/hletong/cdm/cargoforecast/notice/info/apply";//预摘牌货源详情
    public static final String E_COMM_CHECK = "action/hletong/api/cdm/esmt/check/valid";//检查用户是否开通E商贸通

    //常跑路线
    public static final String FAVORITE_ROUTINE = "action/api/vehicles/route/get";//查询常用路线
    public static final String ADD_FAVORITE_ROUTINE = "action/api/vehicles/route/add";//添加常用路线
    public static final String DELETE_FAVORITE_ROUTINE = "action/api/vehicles/route/del";//删除常用路线

    //货方APP
    public static final String PERSONAL_SOURCE_LIST = "mobile/hletong/api/cdm/cargoforecast/info/my-list/get";//个人货源列表
    public static final String SOURCE_TRACK_INFO = "mobile/hletong/api/cdm/cargoforecast/transport/dtl/query";//货源跟踪
    public static final String CARGO_FORECAST_SELECT_DATA = "mobile/hletong/api/cdm/cargoforecast/commonly-used/info/get";//发布货源-选择常用联系人/地址联系人/受票人/付款人/收发货人
    public static final String DELETE_CARGO = "mobile/hletong/api/cdm/cargoforecast/cargo-info/delete";//发布货源-删除常用货源
    public static final String MODIFY_CARGO = "mobile/hletong/api/cdm/cargoforecast/cargo-info/update";//发布货源-修改常用货源
    public static final String ADD_NEW_CARGO = "mobile/hletong/api/cdm/cargoforecast/cargo-info/add";//发布货源-新增常用货源
    public static final String CARGO_FORECAST_DICTIONARY = "mobile/hletong/api/cdm/cargoforecast/info-auth/get";//发布货源-获取所有字典项
    public static final String APPOINT_CARRIER = "mobile/hletong/api/cdm/cargoforecast/appoint-carrier/info/get";//获取指定车船
//
//    @Deprecated
//    public static final String ADD_ADDRESS = "mobile/hletong/api/cdm/cargoforecast/addresss-contact-info/add";//新增装卸货地
//    @Deprecated
//    public static final String DELETE_ADDRESS = "mobile/hletong/api/cdm/cargoforecast/addresss-contact-info/delete";//删除装卸货地
//    @Deprecated
//    public static final String UPDATE_ADDRESS = "mobile/hletong/api/cdm/cargoforecast/addresss-contact-info/update";//修改装卸货地

    public static final String UPDATE_CONSIGNOR = "mobile/hletong/api/cdm/cargoforecast/consignor-consignee/update";//修改收发货人
    public static final String ADD_CONSIGNOR = "mobile/hletong/api/cdm/cargoforecast/consignor-consignee/add";//添加收发货人
    public static final String DELETE_CONSIGNOR = "mobile/hletong/api/cdm/cargoforecast/consignor-consignee/delete";//删除收发货人
    public static final String SUBMIT_NONE_SELF_TRADE_CARGO = "mobile/hletong/api/cdm/cargoforecast/info/submit";//非自主交易货源
    public static final String SUBMIT_SELF_TRADE_CARGO = "action/hletong/api/cdm/selftrade/cargo/forecast/pub/submit";//自主交易货源
    public static final String COPY_CARGO_INFO = "mobile/public/hletong/cdm/cargoforecast/info/get";//复制货源，获取货源详情
    public static final String CARGO_REVOKE = "mobile/hletong/api/cdm/cargoforecast/undo";//撤销货源
    public static final String MATCHED_CARRIER = "action/api/get/forecast/transport";//匹配车船
    public static final String CARRIER_COLLECTION_LIST = "action/api/vehicles/stored/get";//收藏的车船列表
    public static final String ADD_TO_COLLECTION = "action/api/vehicles/stored";//添加到收藏
    public static final String DEL_FROM_COLLECTION = "action/api/vehicles/stored/del";//删除收藏
    public static final String INFORM_CARRIER = "action/api/vehicles/inform";//通知车船进行摘牌
    public static final String CARRIER_TRADE_HISTORY = "action/api/get/forecast/tradeHistroy";//车船交易历史
    public static final String CARGO_REFUSED_REASON = "action/hletong/api/cdm/cargoforecast/reason-info/get";//查看货源审核失败得原因
    public static final String SELF_TRADE_CONTACT = "mobile/hletong/api/cdm/selftrade/cargo/forecast/cargocontact/get";//自主交易货方联系人

    //装卸货联系人 重构
    public static final String CARGO_CONTACT_LIST = "mobile/hletong/api/cdm/contactInfo/getContactInfo";//获取装卸货联系人
    public static final String CARGO_CONTACT_ADD = "mobile/hletong/api/cdm/contactInfo/addContactInfo";//新增装卸货联系人
    public static final String CARGO_CONTACT_DELETE = "mobile/hletong/api/cdm/contactInfo/deleteContactInfo";//删除装卸货联系人
    public static final String CARGO_CONTACT_MODIFY = "mobile/hletong/api/cdm/contactInfo/modifyContactInfo";//修改装卸货联系人
    //议价功能
    public static final String SUBMIT_QUOTE = "action/hletong/api/cdm/selftrade/quote/do";//车船-提交报价信息
    public static final String ACCEPT_QUOTE = "action/hletong/api/cdm/selftrade/quote/agree";//货方-接受报价
    public static final String MODIFY_QUOTE = "action/hletong/api/cdm/selftrade/quote/mod";//车船-修改报价
    public static final String CANCEL_QUOTE = "action/hletong/api/cdm/selftrade/quote/cancel";//车船-取消报价,货方-拒绝报价
    public static final String QUOTE_TRADE_BOOK = "action/hletong/api/cdm/cargo_book/quote_trade/book";//货方接受报价之后，车船方摘牌
    public static final String QUOTE_INFOS_CARRIER = "action/hletong/api/cdm/selftrade/quoteInfo/get";//车船-获取的报价信息，
    public static final String QUOTE_INFOS_CARGO = "action/hletong/api/cdm/selftrade/confirm/quoteInfo/get";//货方-获取货源的报价信息，

    public static final String PATH_PLAN = "page/public/hletong/template/CdmWayMap/appMapPath";//路径规划

    public static final String PHONE_TRACK = "action/hletong/api/cdm/record/call/add";//自主交易拨打电话记录
    public static final String USER_GUIDE_TRUCK_OR_SHIP = "hletong/hlep/transport/dist/index.html#!/home?appType=" + BuildConfig.app_type;//车船版用户手册的地址
    public static final String USER_GUIDE_CARGO = "hletong/hlep/seller/dist/index.html#!/home?appType=" + BuildConfig.app_type;//车船版用户手册的地址

    public static final String PHONE_TRACK_HISTORY = "action/hletong/api/cdm/record/carrier-call/list";//自主交易拨号记录

    //会员评价
    public static final String TRANSPORTER_EVALUATION_GET_DETAILS = "action/hletong/api/eval/get/mesg";//货方上传完发货单之后进行会员评价获取数据
    public static final String TRANSPORTER_EVALUATION_SUBMIT = "action/hletong/api/eval/submit/evaluate";//提交评价信息

    //会员权限
    public static final String GET_AUTHENTICATION_INFO = "mobile/hletong/api/cdm/cdmPaper/get";//获取会员权限信息
    public static final String COMPLETE_VEHICLE_INFO = "mobile/hletong/api/mm/paper/truck/audit";//提交车辆信息
    public static final String COMPLETE_SHIP_INFO = "mobile/hletong/api/mm/paper/ship/audit";//提交船舶信息
    public static final String COMPLETE_COMPANY_INFO = "mobile/hletong/api/mm/paper/company-cargo/audit";//提交公司货方信息
    public static final String COMPLETE_PERSONAL_INFO = "mobile/hletong/api/mm/paper/person-cargo/audit";//提交个人货方信息

    public static final String CHECK_USER_AUTHORITY = "mobile/hletong/api/mm/validate/invoice/reg";//检查会员是否具有平开权限

    public static final String SUBMIT_AGRT_PASSWORD = "mobile/hletong/api/cdm/cargoforecast/agrt-price/loading-unload-pwd/submit";//提交长期协议价收发货密码

    public static final String INFO_EXIST_CERT = "mobile/hletong/api/mm/member/response/info";//已存在的证件资料信息
    public static final String SUBMIT_CERT = "mobile/hletong/api/mm/member/data/complete";//认证资料提交

    //注册获取还原管理单位接口
    public static final String MEMBER_UNIT = "action/public/hletong/mm/unitInfo/getUnitName";
    public static final String LOCATION_UPLOAD = "mobile/hletong/api/mm/trail/postBack";//上传实时位置
    public static final String CARGO_PERMISSION = "mobile/hletong/cdm/biz/oper/auth";//获取货方权限
    //判断证件资料是否正在审核中
    public static final String CHECK_AUDITING = "mobile/hletong/api/mm/member/auditing/check";

    //检测是否已报名
    public static final String CHECK_VOTER = "action/public/hletong/api/activity/voter/applied/mesg";
    //候选人列表
    public static final String CANDIDATE = "action/public/hletong/api/activity/candidate/getCandidateList";
    //投票人，候选人注册
    public static final String VOTER_REGISTER = "action/public/hletong/api/activity/voter/register";
    public static final String CANDIDATE_REGISTER = "action/public/hletong/api/activity/to_register";

    //验证码获取
    public static final String VOTER_CHECKCODE = "action/public/hletong/api/activity/code/get";
    public static final String VOTER_FOR_CANDIDATE = "action/public/hletong/api/activity/voter/voteForCandidate";

    public static final String AD = "mobile/public/hletong/api/adv/get/app/adv";
    //无需权限的下载图片的地址
    public static final String LOAD_PIC_PUBLIC = "mobile/public/hletong/file/%s/download";

    /******
     * 页面跳转 requestCode
     ***/
    public static final int SELECT_ADDRESS_REQUEST = 100;
    public static final int ADD_REQUEST = 101;
    public static final int EDIT_REQUEST = 102;
    public static final int PAPER_REQUEST = 103;
    public static final int PAPER_REQUEST_SINGLE = 104;
    public static final int BANKCARD_REQUEST = 105;
    public static final int INVOICE_REQUEST = 106;
    public static final int PHOTO_ADD_REQUEST = 107;

    public final static class SP_KEY {
        /**
         * 登录信息
         **/
        public static final String KEY_LOGIN_INFO = "loginInfo";
        /**
         * 是否显示功能引导
         ***/
        public static final String KEY_FIRST_ENTER_GUIDE = "enterGuide";
        /**
         * 是否第一次打开引导
         ***/
        public static final String KEY_FIRST_START_GUIDE = "startGuide";

        public static final String KEY_ADINFO_LIST = "adInfo_list";

        /**
         * 字典项
         ***/
        public static final String KEY_DICT_ITEMS = "dictItems";
    }

    public static String formatActionImageUrl(String fileGroupId) {
        return getUrl(String.format("mobile/hletong/api/file/group/%s/download", fileGroupId));
    }

    @SuppressLint("HardwareIds")
    public static String getDeviceId() {
        String id = Settings.System.getString(AppManager.getContext().getContentResolver(), Settings.System.ANDROID_ID);
        if (TextUtils.isEmpty(id)) {
            id = android.os.Build.SERIAL;
        }
        if (TextUtils.isEmpty(id)) {
            id = "NotFoundId";
        }
        return id;
    }
}