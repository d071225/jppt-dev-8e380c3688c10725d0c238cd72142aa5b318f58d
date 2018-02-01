package com.hletong.hyc.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.hletong.hyc.R;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.mob.util.AppManager;
import com.qiyukf.nimlib.sdk.NimIntent;
import com.qiyukf.unicorn.api.ConsultSource;
import com.qiyukf.unicorn.api.SavePowerConfig;
import com.qiyukf.unicorn.api.StatusBarNotificationConfig;
import com.qiyukf.unicorn.api.Unicorn;
import com.qiyukf.unicorn.api.YSFOptions;
import com.qiyukf.unicorn.api.YSFUserInfo;

/**
 * Created by chengxin on 2017/8/14.
 */

public class UnicornHelper {
    // 如果返回值为null，则全部使用默认参数。

    public static void init() {
        Unicorn.init(AppManager.getContext(), "a3e6d94eff312c2ff557c0d3d4cfc581", options(), new GlideImageLoader(AppManager.getContext()));
        setUserInfo();
    }

    private static YSFOptions options() {
        YSFOptions options = new YSFOptions();
        options.statusBarNotificationConfig = new StatusBarNotificationConfig();
        options.savePowerConfig = new SavePowerConfig();
        return options;
    }

    public static void notifyApp(Activity activity, Intent intent) {
        if (intent.hasExtra(NimIntent.EXTRA_NOTIFY_CONTENT)) {
            // 打开客服窗口
            String title = activity.getString(R.string.kehufuwu);
            ConsultSource source = new ConsultSource("首页", "首页", "noCustom");
            Unicorn.openServiceActivity(activity, title, source);
            // 最好将intent清掉，以免从堆栈恢复时又打开客服窗口
            activity.setIntent(new Intent());
        }
    }

    public static void enter(Context context) {
        String title = context.getString(R.string.kehufuwu);
        ConsultSource source = new ConsultSource("设置页面", "设置", "noCustom");
        Unicorn.openServiceActivity(context, title, source);
    }

    public static void setUserInfo() {
        if (LoginInfo.hasLogin()) {
            YSFUserInfo userInfo = new YSFUserInfo();
            userInfo.userId = LoginInfo.getLoginInfo().getMember_code();
            userInfo.data = "[{\"key\":\"real_name\", \"value\":\"" + userInfo.userId + "\"}," +
                    " {\"key\":\"mobile_phone\", \"hidden\":true}, " +
                    "{\"key\":\"email\",  \"hidden\":true}]";
            Unicorn.setUserInfo(userInfo);
        }
    }
}
