package com.hletong.hyc.ui.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hletong.hyc.R;
import com.hletong.hyc.model.Action;
import com.hletong.hyc.model.Candidate;
import com.hletong.hyc.ui.dialog.DriverActionDialog;
import com.hletong.hyc.ui.fragment.CandidateFragment;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.view.IProgress;
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
import com.xcheng.view.util.ToastLess;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Optional;

public class CandidateInfoActivity extends BaseActivity {
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_plate)
    TextView tvPlate;
    @BindView(R.id.tv_vote_num)
    TextView tvVoteNum;
    @BindView(R.id.tv_feeling)
    TextView tvFeeling;
    @BindView(R.id.ev_id_headerPhoto)
    ImageView ivHeader;
    @BindView(R.id.ev_id_submit)
    View btnVoter;
    private Candidate mCandidate;
    IWXAPI api;

    @Override
    public int getLayoutId() {
        return R.layout.activity_candidate_info;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        api = WXAPIFactory.createWXAPI(this, Constant.WX_APPID, true);
        api.registerApp(Constant.WX_APPID);
        ButterKnife.bind(this);
        if (!DriverActionDialog.sIsGoodDriver){
            setCustomTitle("中国特困司机");
        }
        mCandidate = getSerializable(Candidate.class.getName());
        tvName.setText("姓名：" + mCandidate.getReg_name());
        tvPlate.setText("车牌号：" + mCandidate.getCarrier_no());
        tvVoteNum.setText("得票数：" + mCandidate.getVote_num());
        tvFeeling.setText(mCandidate.getReadme());
        Glide.with(getContext()).load(Constant.formatActionImageUrl(mCandidate.getImg_group_id())).into(ivHeader);
    }

    private void toTimeline() {
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "https://test.hletong.com/jppt/wechat/hsj/hsj/tp.html?carNo=" + mCandidate.getCarrier_no();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.share, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_share) {
            toTimeline();
            return true;
        }
        return false;
    }


    @Optional
    @OnClick({R.id.ev_id_submit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ev_id_submit:
                if (!Action.canVote()) {
                    ToastLess.showToast("活动尚未开始");
                    return;
                }
                OkRequest request = EasyOkHttp.form(Constant.VOTER_FOR_CANDIDATE)
                        .tag(getContext().hashCode())
                        .param("voterUuid", DriverActionDialog.sVoter.getVoterUuid())
                        .param("candidateUuids", mCandidate.getReg_uuid())
                        .param("machineUuid", Constant.getDeviceId())
                        .param("voteResource", 0)
                        .param("voteType", DriverActionDialog.sIsGoodDriver ? "1" : "2")
                        .build();
                new ExecutorCall<CommonResult>(request).enqueue(new AnimCallBack<CommonResult>((IProgress) getContext()) {
                    @Override
                    public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                        ToastLess.showToast("投票成功");
                        mCandidate.upVote_num();
                        tvVoteNum.setText("得票数：" + mCandidate.getVote_num());
                        EventBus.getDefault().post(new CandidateFragment.EventVoter(mCandidate));
                    }

                    @Override
                    public void onError(OkCall<CommonResult> okCall, EasyError error) {
                        ToastLess.showToast(error.getMessage());
                    }
                });
                break;
        }
    }
}
