package com.hletong.hyc.ui.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.MainContract;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.UnTodo;
import com.hletong.hyc.model.VersionInfo;
import com.hletong.hyc.presenter.MainPresenter;
import com.hletong.hyc.ui.dialog.UpgradeFragment;
import com.hletong.hyc.ui.fragment.SettingFragment;
import com.hletong.hyc.ui.fragment.SourceListFragmentPublic;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.hyc.util.UnicornHelper;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.base.BaseViewPagerActivity;
import com.hletong.mob.dialog.AlertDialog;
import com.hletong.mob.util.AMapLocationManager;
import com.hletong.mob.util.BitmapUtils;
import com.hletong.mob.util.ViewUtil;
import com.orhanobut.logger.Logger;
import com.xcheng.permission.DeniedResult;
import com.xcheng.permission.EasyPermission;
import com.xcheng.permission.OnRequestCallback;
import com.xcheng.view.adapter.TabInfo;
import com.xcheng.view.util.LocalDisplay;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends BaseViewPagerActivity implements MainContract.View {
    public static final String FORCE_UPDATE = "com.hletong.hyc.ui.activity.force_update";
    public static final String FORCE_UPDATE_RECEIVED = "com.hletong.hyc.ui.activity.force_update_received";
    private MainContract.Presenter mPresenter;
    private boolean hasLogin;
    private Dialog locationDialog;
    private TextView tvRedUndo;
    private boolean hasDealPermission;
    private boolean canShowDialog = true;
    private UpgradeFragment mUpgradeFragment;
    /**
     * 强制更新的时候，会间隔2秒发送一次强制更新广播，直到收到{@link #FORCE_UPDATE_RECEIVED}广播为止
     */
    private Timer mForeUpdateTask;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void getTabInfos(List<TabInfo> tabInfos) {
        tabInfos.add(new TabInfo(AppTypeConfig.getAppFragment().getSimpleName(), R.drawable.activity_main_tab_main, getString(R.string.tab_main), AppTypeConfig.getAppFragment()));
        tabInfos.add(new TabInfo(SettingFragment.class.getSimpleName(), R.drawable.activity_main_tab_setting, getString(R.string.setting), SettingFragment.class));
    }

    @NonNull
    @Override
    public View createTabView(int position, TabInfo tabInfo) {
        TextView tabView = new TextView(this);
        tabView.setTextSize(16);
        tabView.setText(tabInfo.title);
        tabView.setBackgroundResource(R.drawable.activity_main_tab_bg);
        TextViewCompat.setCompoundDrawablesRelativeWithIntrinsicBounds(tabView, 0, tabInfo.iconResId, 0, 0);
        tabView.setTextColor(ContextCompat.getColorStateList(this, R.color.activity_main_tab_text_color));
        tabView.setPadding(0, LocalDisplay.dp2px(5), 0, LocalDisplay.dp2px(5));
        tabView.setGravity(Gravity.CENTER_HORIZONTAL);
        return tabView;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        tvRedUndo = findViewById(R.id.tv_redUndo);
        setLogo(R.drawable.logo);
        setNavigationIcon(null);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                setCustomTitle(position == 0 ? R.string.main_title : R.string.setting);
                tvRedUndo.setVisibility(position != 0 ? View.VISIBLE : View.INVISIBLE);
            }
        });

        hasLogin = LoginInfo.hasLogin();
        mPresenter = new MainPresenter(this);
        mPresenter.start();
        UnicornHelper.notifyApp(this, getIntent());
        EventBus.getDefault().register(this);

        //强制更新时用到
        register(FORCE_UPDATE_RECEIVED);

    }

    @Override
    public void onReceive(Context context, String action, Intent intent) {
        //取消任务
        mForeUpdateTask.cancel();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("actionShare", false)) {
            Bundle bundle = new Bundle();
            bundle.putString("share", intent.getStringExtra("share"));
            toActivity(CommonWrapFragmentActivity.class, CommonWrapFragmentActivity.getBundle("货源公告", SourceListFragmentPublic.class, bundle), null);
        } else {
            UnicornHelper.notifyApp(this, intent);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mUpgradeFragment != null) {
            mUpgradeFragment.show(getSupportFragmentManager(), UpgradeFragment.class.getName());
            mUpgradeFragment = null;
        }
        if (!AppTypeConfig.isCargo()) {
            if (!AMapLocationManager.isGpsProvider() && !AMapLocationManager.isNetWorkProvider()) {
                if (locationDialog == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    locationDialog = builder.setCancelable(false)
                            .setTitle("定位服务已关闭")
                            .setContent("您需要打开[" + AppTypeConfig.getAppName() + "]定位服务")
                            .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    AMapLocationManager.toLocationSetting(MainActivity.this);
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    logout();
                                }
                            }).build();
                }
                locationDialog.show();
                return;
            }
            if (!hasDealPermission && !EasyPermission.isGranted(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                requestLocation();
                return;
            }
        }
        //如果用户之前没有扥登录，但是回到主页面已经登录成功，检测权限一次
        if (!hasLogin && LoginInfo.hasLogin()) {
            hasLogin = true;
            mPresenter.checkAuthority();
        }
    }

    /**
     * 强制用户允许GPS权限
     */
    private void requestLocation() {
        EasyPermission.with(this).permissions(Manifest.permission.ACCESS_FINE_LOCATION)
                .request(1228, new OnRequestCallback() {
                    @Override
                    public void onAllowed() {
                    }

                    @Override
                    public void onRefused(DeniedResult deniedResult) {
                        hasDealPermission = true;
                        DialogFactory.showAlert(getContext(), "App需要定位权限才能正常运行,请在设置中打开此权限", null, "退出", false, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                logout();
                            }
                        });
                    }
                });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent backHome = new Intent(Intent.ACTION_MAIN);
            backHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            backHome.addCategory(Intent.CATEGORY_HOME);
            startActivity(backHome);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void handleError(@NonNull BaseError error) {
        Logger.d(error.getMessage());//这里就不要把错误消息show出来了，打印下就行了
    }

    @Override
    public void logout() {
        LoginActivity.toRootLogin(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveUndoEvent(UnTodo.UndoEvent undoEvent) {
        ViewGroup container = (ViewGroup) mIndicator.getChildAt(0);
        TextView tabHome = (TextView) container.getChildAt(0);
        Drawable drawableTop = tabHome.getCompoundDrawables()[1];
        int top = tabHome.getTop() + tabHome.getPaddingTop();
        int left = mIndicator.getWidth() / 4 + (drawableTop != null ? drawableTop.getIntrinsicWidth() / 2 : LocalDisplay.dp2px(10));
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) tvRedUndo.getLayoutParams();
        if (lp.leftMargin != left || lp.topMargin != top) {
            lp.leftMargin = left;
            lp.topMargin = top;
            tvRedUndo.setLayoutParams(lp);
        }
        if (undoEvent.getUndoCount() > 0) {
            if (tvRedUndo.getBackground() == null) {
                ViewUtil.setBackground(tvRedUndo, new BitmapDrawable(getResources(), BitmapUtils.getCircleRedBitmap(LocalDisplay.dp2px(4))));
            }
        } else {
            ViewUtil.setBackground(tvRedUndo, null);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        canShowDialog = false;
    }

    @Override
    public void showUpgradeDialog(VersionInfo versionInfo) {
        //只有车船app会自动跳转到下一级界面
        if (versionInfo.isForced() && AppTypeConfig.isTransporter()) {
            if (mForeUpdateTask == null) {
                mForeUpdateTask = new Timer();
                mForeUpdateTask.schedule(new BroadcastForceUpdate(versionInfo), 2000);
            }
        }

        if (canShowDialog) {
            UpgradeFragment.getInstance(versionInfo).show(getSupportFragmentManager(), UpgradeFragment.class.getName());
        } else {
            mUpgradeFragment = UpgradeFragment.getInstance(versionInfo);
        }
    }

    private class BroadcastForceUpdate extends TimerTask {
        private VersionInfo mVersionInfo;

        public BroadcastForceUpdate(VersionInfo versionInfo) {
            mVersionInfo = versionInfo;
        }

        @Override
        public void run() {
            Intent intent = new Intent(FORCE_UPDATE);
            intent.putExtra("version", mVersionInfo);
            broadcast(intent);
        }
    }
}
