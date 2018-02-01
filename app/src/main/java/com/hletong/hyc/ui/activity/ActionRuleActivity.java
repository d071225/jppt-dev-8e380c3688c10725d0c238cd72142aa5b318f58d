package com.hletong.hyc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.ui.dialog.DriverActionDialog;
import com.hletong.mob.base.BaseActivity;

public class ActionRuleActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_rule_action;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        TextView tv = (TextView) findViewById(R.id.tv_rule);
        if (!DriverActionDialog.sIsCandidateSign) {
            tv.setText("1、活动仅限从事货运的司机参加。\n" +
                    "2、关注“惠龙易通微信公众号”后方可参加报名或投票。\n" +
                    "3、投票需上传本人的头像、身份证、行驶证、从业资格证。\n" +
                    "4、上传的证件资料不齐全、不正确视为投票不成功。");
        }
    }
}
