package com.hletong.hyc.ui.dialog;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.LocalBroadcastManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.hletong.hyc.R;
import com.hletong.hyc.model.VersionInfo;
import com.hletong.hyc.service.ApkDownloadService;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.hyc.util.InstallHelper;
import com.hletong.mob.util.AppManager;
import com.hletong.mob.util.NumberUtil;
import com.xcheng.permission.DeniedResult;
import com.xcheng.permission.EasyPermission;
import com.xcheng.permission.OnRequestCallback;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by dongdaqing on 2017/9/14.
 * 提示更新
 */

public class UpgradeFragment extends DialogFragment {
    private final int STATE_INIT = 0;
    private final int STATE_DOWNLOADING = 1;
    private final int STATE_SUCCEED = 2;
    private final int STATE_ERROR = 3;

    @BindView(R.id.version)
    TextView mVersion;
    @BindView(R.id.introduce)
    TextView mIntroduce;
    @BindView(R.id.size)
    TextView mSize;
    @BindView(R.id.force_update_hint)
    TextView mForceUpdateHint;
    @BindView(R.id.later)
    TextView mLater;
    @BindView(R.id.update)
    TextView mUpdate;
    Unbinder unbinder;
    @BindView(R.id.download_hint)
    TextView mDownloadHint;
    @BindView(R.id.progress)
    ProgressBar mProgress;
    @BindView(R.id.percent)
    TextView mPercent;
    @BindView(R.id.switcher)
    ViewSwitcher mSwitcher;

    private VersionInfo mVersionInfo;
    private BroadcastReceiver mReceiver;
    private int state = STATE_INIT;
    private Intent mIntent;
    private String name;

    public static UpgradeFragment getInstance(VersionInfo versionInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("version", versionInfo);
        UpgradeFragment fragment = new UpgradeFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mVersionInfo = getArguments().getParcelable("version");
        name = AppManager.appId + "_v_" + mVersionInfo.getCurrentVersion() + ".apk";
        mIntent = new Intent(getContext(), ApkDownloadService.class);
        mIntent.putExtra("path", new File(getContext().getExternalCacheDir(), name).getPath());
        mIntent.putExtra("url", mVersionInfo.getFileAddress());

        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                switch (intent.getAction()) {
                    case ApkDownloadService.DOWNLOAD_PROGRESS:
                        changeState(STATE_DOWNLOADING);
                        int c = intent.getIntExtra("current", 1);
                        int t = intent.getIntExtra("total", 1);
                        changeProgress(c, t);
                        break;
                    case ApkDownloadService.DOWNLOAD_FAILED:
                        changeState(STATE_ERROR);
                        break;
                    case ApkDownloadService.DOWNLOAD_SUCCEED:
                        changeProgress(100, 100);
                        changeState(STATE_SUCCEED);
                        break;
                }
            }
        };
    }

    private void changeProgress(int current, int total) {
        float progress = current * 100 / total;
        mProgress.setProgress((int) progress);
        mPercent.setText(NumberUtil.format2f(progress) + "%");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setCancelable(true);
        mVersion.setText(mVersionInfo.getAppVersion());
        mIntroduce.setText(mVersionInfo.getAppDesc());
        mSize.setText(mVersionInfo.getAppSize());
        mProgress.setMax(100);
        mProgress.setProgress(0);
        if (mVersionInfo.isForced()) {
            mLater.setVisibility(View.GONE);
            mForceUpdateHint.setVisibility(View.VISIBLE);
            setCancelable(false);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ApkDownloadService.DOWNLOAD_PROGRESS);
        filter.addAction(ApkDownloadService.DOWNLOAD_FAILED);
        filter.addAction(ApkDownloadService.DOWNLOAD_SUCCEED);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(mReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @OnClick({R.id.later, R.id.update})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.later:
                if (state == STATE_DOWNLOADING) {
                    LocalBroadcastManager.getInstance(getContext()).sendBroadcast(new Intent(ApkDownloadService.CANCEL));
                    LocalBroadcastManager.getInstance(getContext()).unregisterReceiver(mReceiver);
                }
                dismiss();
                break;
            case R.id.update:
                if (state == STATE_INIT || state == STATE_ERROR) {
                    final Activity activity = getActivity();
                    EasyPermission.with(activity).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            .request(1229, new OnRequestCallback() {
                                @Override
                                public void onAllowed() {
                                    if (mIntent != null) {
                                        getActivity().startService(mIntent);
                                    }
                                }

                                @Override
                                public void onRefused(DeniedResult deniedResult) {
                                    if (deniedResult.allNeverAsked) {
                                        DialogFactory.permissionSetting(activity, deniedResult.deniedPerms);
                                    }
                                }
                            });
                } else if (state == STATE_SUCCEED) {
                    InstallHelper.with(getContext()).install(new File(getContext().getExternalCacheDir(), name));
                }
                break;
        }
    }

    private void changeState(int newState) {
        state = newState;
        switch (newState) {
            case STATE_INIT:
                switchTo(0);
                break;
            case STATE_DOWNLOADING:
                switchTo(1);
                mDownloadHint.setText("正在下载，请稍等");
                mLater.setText("取消");
                break;
            case STATE_SUCCEED:
                switchTo(1);
                mDownloadHint.setText("下载完成，准备安装");
                mUpdate.setText("立即安装");
                break;
            case STATE_ERROR:
                switchTo(1);
                mDownloadHint.setText("下载失败，请重试");
                mUpdate.setText("重新下载");
                break;
        }
    }

    private void switchTo(int index) {
        if (mSwitcher.getNextView() == mSwitcher.getChildAt(index)) {
            mSwitcher.showNext();
        }
    }
}
