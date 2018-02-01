package com.hletong.hyc.service;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.hletong.hyc.R;
import com.hletong.hyc.util.InstallHelper;
import com.hletong.mob.http.EasyCallback;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.callback.HttpParser;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.OkCall;
import com.xcheng.okhttp.request.OkRequest;

import java.io.File;

/**
 * Created by dongdaqing on 2017/7/31.
 * 下载apk
 */
public class ApkDownloadService extends BaseService {
    public static final String DOWNLOAD_PROGRESS = "com.hletong.hyc.service.apk_download_progress";
    public static final String DOWNLOAD_FAILED = "com.hletong.hyc.service.apk_download_failed";
    public static final String DOWNLOAD_SUCCEED = "com.hletong.hyc.service.apk_download_succeed";
    public static final String CANCEL = "com.hletong.hyc.service.cancel_download_service";

    private final String TAG = "hletong.jppt.file_download_service";
    private final String INSTALL = "install_downloaded_file";

    private Intent mIntent;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mIntent = new Intent();
        mIntent.setAction(DOWNLOAD_PROGRESS);

        register(INSTALL, CANCEL);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        download(intent.getStringExtra("url"), intent.getStringExtra("path"));
        return START_NOT_STICKY;
    }

    @Override
    public void onReceive(Context context, String action, Intent intent) {
        NotificationManagerCompat.from(this).cancel(TAG, 0);
        if (CANCEL.equals(action)) {
            showMessage("下载已取消");
            EasyOkHttp.cancel(TAG);
            cancelNotification();
            stopSelf(START_NOT_STICKY);
        } else if (INSTALL.equals(action)) {
            InstallHelper.with(this).install(intent.getStringExtra("file"));
        }
    }

    private void update(int current, int total) {
        mIntent.putExtra("current", current);
        mIntent.putExtra("total", total);
        broadcast(mIntent);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setProgress(total, current, false);
        builder.setAutoCancel(false);
        builder.setContentTitle("正在下载");
        builder.setSmallIcon(R.drawable.ic_notification);
        NotificationManagerCompat.from(this).notify(TAG, 0, builder.build());
    }


    private void download(String url, String path) {
        File file = new File(path);
        if (file.exists()) {
            broadcast(new Intent(DOWNLOAD_SUCCEED));
            InstallHelper.with(this).install(file);
            stopSelf(START_NOT_STICKY);
            return;
        }
        OkRequest params = EasyOkHttp.get(url).tag(TAG)
                .parserFactory(new HttpParser.Factory() {
                    @NonNull
                    @Override
                    public HttpParser<?> parser(OkRequest request) {
                        return FileParser.INSTANCE;
                    }
                })
                .extra("path", file.getPath())
                .outProgress()
                .build();
        new ExecutorCall<File>(params).enqueue(new EasyCallback<File>(this) {
            @Override
            public void onSuccess(OkCall<File> okCall, File response) {
                showMessage("下载成功");
                downloadSucceed(response);
                broadcast(new Intent(DOWNLOAD_SUCCEED));
                InstallHelper.with(ApkDownloadService.this).install(response);
                stopSelf(START_NOT_STICKY);
            }

            @Override
            public void onError(OkCall<File> okCall, EasyError error) {
                super.onError(okCall, error);
                broadcast(new Intent(DOWNLOAD_FAILED));
                cancelNotification();
                stopSelf(START_NOT_STICKY);
            }

            @Override
            public void outProgress(OkCall<File> okCall, float progress, long total, boolean done) {
                update((int) (progress * total), (int) total);
            }
        });
    }

    private void downloadSucceed(File file) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("下载完成，点此安装");
        builder.setSmallIcon(R.drawable.ic_notification);
        Intent intent = new Intent(INSTALL);
        intent.putExtra("file", file.getPath());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 111, intent, PendingIntent.FLAG_ONE_SHOT);
        builder.setContentIntent(pendingIntent);
        NotificationManagerCompat.from(this).cancel(TAG, 0);
        NotificationManagerCompat.from(this).notify(TAG, 0, builder.build());
    }

    private void cancelNotification() {
        NotificationManagerCompat.from(this).cancel(TAG, 0);
    }
}

