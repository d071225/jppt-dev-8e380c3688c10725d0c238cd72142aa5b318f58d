package com.hletong.hyc.ui.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hletong.hyc.R;
import com.hletong.hyc.http.parse.JpptParse;
import com.hletong.hyc.model.Action;
import com.hletong.hyc.model.Candidate;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.RegisterPhoto;
import com.hletong.hyc.model.Voter;
import com.hletong.hyc.ui.dialog.DriverActionDialog;
import com.hletong.hyc.ui.fragment.CandidateFragment;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.hyc.util.RegexUtil;
import com.hletong.mob.architect.error.BusiError;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.http.AnimCallBack;
import com.hletong.mob.util.BitmapUtils;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;
import com.xcheng.view.util.JumpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * 好司机和特困司机报名页面，投票报名的话取消
 */
public class GoodDriverActivity extends BaseActivity {
    @BindView(R.id.tv_voter_label)
    TextView mTvVoterLabel;
    @BindView(R.id.tv_voterTime)
    TextView mTvVoterTime;
    @BindView(R.id.cv_chinaName)
    CommonInputView mCvChinaName;
    @BindView(R.id.cv_plate)
    CommonInputView mCvPlate;
    @BindView(R.id.cv_mobile)
    CommonInputView mCvMobile;
    @BindView(R.id.et_verifyCode)
    EditText mEtVerifyCode;
    @BindView(R.id.tv_getVerificationCode)
    Button mBtnGetVerificationCode;
    @BindView(R.id.et_feeling)
    EditText mEtFeeling;
    @BindView(R.id.tv_feeling)
    TextView tvFeeling;
    @BindView(R.id.iv_selfPic)
    ImageView mIvSelfPic;
    @BindView(R.id.iv_driverLicense)
    ImageView mIvDriverLicense;
    @BindView(R.id.iv_idCard)
    ImageView mIvIdCard;
    @BindView(R.id.iv_cyzgz)
    ImageView mIvCyzgz;
    @BindView(R.id.tv_voterRule)
    TextView mTvVoterRule;
    @BindView(R.id.ev_id_submit)
    Button mBtnSubmit;
    IWXAPI api;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        private int count = 60;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            count--;
            if (count == 0) {
                count = 60;
                mBtnGetVerificationCode.setText("获取验证码");
                mBtnGetVerificationCode.setEnabled(true);
            } else {
                mBtnGetVerificationCode.setText(count + "秒后重试");
                mHandler.sendEmptyMessageDelayed(0, 1000);
            }
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.activity_driver_base;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constant.WX_APPID, true);
        api.registerApp(Constant.WX_APPID);

        ButterKnife.bind(this);
        mCvPlate.setText(LoginInfo.getLoginInfo().getLogin_name());
        mCvPlate.getInput().setEnabled(false);
        //如果不是报名参加候选人
        if (!DriverActionDialog.sIsCandidateSign) {
            mTvVoterRule.setVisibility(View.GONE);
            mEtFeeling.setVisibility(View.GONE);
            tvFeeling.setVisibility(View.GONE);
            mTvVoterLabel.setText("在线投票");
            mBtnSubmit.setText("提交资料");
        }
        Action.Notice notice = DriverActionDialog.sNotice;
        if (notice != null) {
            String leftLabel = DriverActionDialog.sIsCandidateSign ? "报名时间:" : "投票时间:";
            mTvVoterTime.setText(leftLabel + notice.formatBegin() + "-" + notice.formatEnd());
        }


        setCustomTitle(DriverActionDialog.sIsGoodDriver ? "中国好司机" : "中国特困司机");
        photoSelf.setPublic(true);
    }

    RegisterPhoto photoSelf = new RegisterPhoto(new String[]{"上传本人照片"}, new int[]{R.drawable.src_action_head}, 1.3f);
    RegisterPhoto photoDriver = new RegisterPhoto(new String[]{"上传行驶证"}, new int[]{R.drawable.src_action_driver}, 1.3f);
    RegisterPhoto photoIdCard = new RegisterPhoto(new String[]{"上传身份证"}, new int[]{R.drawable.src_identify_front}, 0.67f);
    RegisterPhoto photoCyzgz = new RegisterPhoto(new String[]{"上传从业资格证"}, new int[]{R.drawable.src_action_cyzgz}, 0.67f);

    @Optional
    @OnClick({R.id.tv_rule, R.id.iv_selfPic, R.id.iv_driverLicense, R.id.iv_idCard, R.id.iv_cyzgz, R.id.tv_getVerificationCode, R.id.ev_id_submit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_rule:
                toActivity(ActionRuleActivity.class, null, null);
                break;
            case R.id.iv_selfPic:
                toActivity(UploadPhotoActivity.class, JumpUtil.getBundle(RegisterPhoto.class.getSimpleName(), photoSelf), 1, null);
                break;
            case R.id.iv_driverLicense:
                toActivity(UploadPhotoActivity.class, JumpUtil.getBundle(RegisterPhoto.class.getSimpleName(), photoDriver), 2, null);
                break;
            case R.id.iv_idCard:
                toActivity(UploadPhotoActivity.class, JumpUtil.getBundle(RegisterPhoto.class.getSimpleName(), photoIdCard), 3, null);
                break;
            case R.id.iv_cyzgz:
                toActivity(UploadPhotoActivity.class, JumpUtil.getBundle(RegisterPhoto.class.getSimpleName(), photoCyzgz), 4, null);
                break;
            case R.id.tv_getVerificationCode:
                getVerifyCode();
                break;
            case R.id.ev_id_submit:
                if (DriverActionDialog.sIsCandidateSign) {
                    commitCandidate();
                } else {
                    commitVoter();
                }
                break;
        }
    }

    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        super.onActivityResultOk(requestCode, data);
        final RegisterPhoto registerPhoto = (RegisterPhoto) data.getSerializableExtra(RegisterPhoto.class.getSimpleName());
        String photoPath = registerPhoto.getPhotos()[0];
        switch (requestCode) {
            case 1:
                photoSelf = registerPhoto;
                Glide.with(this).load(photoPath).fitCenter().into(mIvSelfPic);
                break;
            case 2:
                photoDriver = registerPhoto;
                Glide.with(this).load(photoPath).fitCenter().into(mIvDriverLicense);
                break;
            case 3:
                photoIdCard = registerPhoto;
                Glide.with(this).load(photoPath).fitCenter().into(mIvIdCard);
                break;
            case 4:
                photoCyzgz = registerPhoto;
                Glide.with(this).load(photoPath).fitCenter().into(mIvCyzgz);
                break;
        }
    }

    private void getVerifyCode() {
        String telephone = mCvMobile.getInputValue();
        if (TextUtils.isEmpty(telephone)) {
            showMessage("请输入手机号");
        } else if (!RegexUtil.isPhoneNumber(telephone)) {
            showMessage("手机号格式不正确");
        } else {
            final OkRequest request = EasyOkHttp.form(Constant.VOTER_CHECKCODE).tag(hashCode())
                    .param("telephone", telephone)
                    .build();
            new ExecutorCall<CommonResult>(request).enqueue(new AnimCallBack<CommonResult>(this) {

                @Override
                public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                    showMessage(response.getErrorInfo());
                    mBtnGetVerificationCode.setEnabled(false);
                    mHandler.sendEmptyMessage(0);
                }
            });
        }

    }

    private void commitCandidate() {
        String name = mCvChinaName.getInputValue();
        String checkCode = mEtVerifyCode.getText().toString();
        String telephone = mCvMobile.getInputValue();
        String carNo = mCvPlate.getInputValue();
        String remark = mEtFeeling.getText().toString();
        String isMember = "1";
        String selfieAddressId = photoSelf.getFileGroupId();
        String identifyAddressId = photoIdCard.getFileGroupId();
        String vehicleAddressId = photoDriver.getFileGroupId();
        String qualificationAddressId = photoCyzgz.getFileGroupId();
        String type = DriverActionDialog.sIsGoodDriver ? "1" : "2";
        String regResource = "0";
        if (TextUtils.isEmpty(name)) {
            showMessage("请输入姓名");
        } else if (TextUtils.isEmpty(telephone)) {
            showMessage("请输入手机号");
        } else if (!RegexUtil.isPhoneNumber(telephone)) {
            showMessage("手机号格式不正确");
        } else if (TextUtils.isEmpty(checkCode)) {
            showMessage(("验证码不能为空"));
        } else if (checkCode.length() != 6) {
            showMessage("验证码必须为6位");
        } else if (TextUtils.isEmpty(remark)) {
            showMessage("请输入参选感言");
        } else if (selfieAddressId == null) {
            showMessage("请上传本人照片");
        } else if (vehicleAddressId == null) {
            showMessage("请上传行驶证");
        } else if (identifyAddressId == null) {
            showMessage("请上传身份证");
        } else if (qualificationAddressId == null) {
            showMessage("请上传从业资格证");
        } else {
            OkRequest request = EasyOkHttp.form(Constant.CANDIDATE_REGISTER).tag(hashCode())
                    .param("name", name)
                    .param("telephone", telephone)
                    .param("checkCode", checkCode)
                    .param("carNo", carNo)
                    .param("remark", remark)
                    .param("isMember", isMember)
                    .param("selfieAddressId", selfieAddressId)
                    .param("identityAddressId", identifyAddressId)
                    .param("vehicleAddressId", vehicleAddressId)
                    .param("qualificationAddressId", qualificationAddressId)
                    .param("type", type)
                    .param("regResource", regResource)
                    .build();
            new ExecutorCall<CommonResult>(request).enqueue(new AnimCallBack<CommonResult>(this) {
                @Override
                public void onError(OkCall<CommonResult> okCall, EasyError error) {
                    if (error instanceof BusiError) {
                        int busiCode = ((BusiError) error).getBusiCode();
                        if (busiCode == Candidate.ACT_CANDIDATE_REGISTERED) {
                            showMessage("好司机已报名，请不要重复报名");
                            return;
                        } else if (busiCode == Candidate.ACT_VOTE_REGISTERED) {
                            showMessage("投票已报名，请不要重复报名");
                            return;
                        }
                    }
                    showMessage(error.getMessage());
                }

                @Override
                public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                    showMessage("报名成功");
                    DialogFactory.showAlert(getContext(), null, "分享至朋友圈，报名方可生效！", "分享", false, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            toTimeline();
                            dialog.dismiss();
                            finish();
                        }
                    });
                }
            });
        }
    }

    private void commitVoter() {
        String name = mCvChinaName.getInputValue();
        String checkCode = mEtVerifyCode.getText().toString();
        String telephone = mCvMobile.getInputValue();
        String carNo = mCvPlate.getInputValue();
        String selfieAddressId = photoSelf.getFileGroupId();
        String identifyAddressId = photoIdCard.getFileGroupId();
        String vehicleAddressId = photoDriver.getFileGroupId();
        String qualificationAddressId = photoCyzgz.getFileGroupId();
        String regResource = "0";
        if (TextUtils.isEmpty(name)) {
            showMessage("请输入姓名");
        } else if (TextUtils.isEmpty(telephone)) {
            showMessage("请输入手机号");
        } else if (!RegexUtil.isPhoneNumber(telephone)) {
            showMessage("手机号格式不正确");
        } else if (TextUtils.isEmpty(checkCode)) {
            showMessage(("验证码不能为空"));
        } else if (checkCode.length() != 6) {
            showMessage("验证码必须为6位");
        } else if (selfieAddressId == null) {
            showMessage("请上传本人照片");
        } else if (vehicleAddressId == null) {
            showMessage("请上传行驶证");
        } else if (identifyAddressId == null) {
            showMessage("请上传身份证");
        } else if (qualificationAddressId == null) {
            showMessage("请上传从业资格证");
        } else {
            OkRequest request = EasyOkHttp.form(Constant.VOTER_REGISTER).tag(hashCode())
                    .extra(JpptParse.ENTRY, "data")
                    .param("voterName", name)
                    .param("voterPhone", telephone)
                    .param("checkCode", checkCode)
                    .param("voterType", 2)
                    .param("carrierNo", carNo)
                    .param("selfieAddressId", selfieAddressId)
                    .param("identityAddressId", identifyAddressId)
                    .param("vehicleAddressId", vehicleAddressId)
                    .param("qualificationAddressId", qualificationAddressId)
                    .param("regResource", regResource)
                    .build();
            new ExecutorCall<Voter>(request).enqueue(new AnimCallBack<Voter>(this) {
                @Override
                public void onError(OkCall<Voter> okCall, EasyError error) {
                    if (error instanceof BusiError) {
                        int busiCode = ((BusiError) error).getBusiCode();
                        if (busiCode == Candidate.ACT_CANDIDATE_REGISTERED) {
                            showMessage("好司机已报名，请不要重复报名");
                            return;
                        } else if (busiCode == Candidate.ACT_VOTE_REGISTERED) {
                            showMessage("投票已报名，请不要重复报名");
                            return;
                        }
                    }
                    showMessage(error.getMessage());
                }

                @Override
                public void onSuccess(OkCall<Voter> okCall, Voter response) {
                    DriverActionDialog.sVoter = response;
                    DriverActionDialog.sSignCode = Candidate.ACT_VOTE_REGISTERED;
                    showMessage("报名成功");
                    JumpUtil.toActivity(GoodDriverActivity.this, CommonWrapFragmentActivity.class
                            , CommonWrapFragmentActivity.getBundle(DriverActionDialog.sIsGoodDriver ? "好司机投票" : "特困司机投票", CandidateFragment.class));
                    finish();
                }
            });
        }
    }

    private void toTimeline() {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://mp.weixin.qq.com/s/bdon8jEaZwB9BJrEQCgfNg";
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "“关爱司机”惠龙易通在行动！";
        msg.description = "“关爱司机”惠龙易通在行动！";
        Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_truck);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, Constant.THUMB_SIZE, Constant.THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = BitmapUtils.bmpToByteArray(thumbBmp, true);
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = "webpage" + String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        api.sendReq(req);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeMessages(0);
    }
}
