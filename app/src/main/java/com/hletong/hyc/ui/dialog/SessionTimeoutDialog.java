package com.hletong.hyc.ui.dialog;

import android.content.DialogInterface;
import android.os.Bundle;

import com.hletong.hyc.R;
import com.hletong.hyc.ui.activity.LoginActivity;
import com.hletong.mob.annotation.LoginAction;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.dialog.AlertDialog;
import com.hletong.mob.util.FinishOptions;

/**
 * Created by xincheng on 2016/12/14.
 */
public class SessionTimeoutDialog extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AlertDialog dialog = new AlertDialog.Builder(this).setTitle("重新登录")
                .setContent("您长时间未操作，为保证安全请重新登录")
                .setPositiveButton(getString(R.string.login), getListener())
                .build();
        dialog.setCancelable(false);
        dialog.show();
    }

    private DialogInterface.OnClickListener getListener() {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Bundle bundle = createLoginBundle(LoginAction.SESSION_TIMEOUT);
                toActivity(LoginActivity.class, bundle, FinishOptions.FINISH_ONLY());
            }
        };
    }

    @Override
    public int getLayoutId() {
        return 0;
    }
}
