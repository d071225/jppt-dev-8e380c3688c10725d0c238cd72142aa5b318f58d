package com.hletong.hyc.job;

import android.support.annotation.NonNull;

import com.amap.api.location.AMapLocation;
import com.evernote.android.job.Job;
import com.evernote.android.job.JobRequest;
import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.HyApplication;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.util.SPUtils;
import com.hletong.mob.util.SimpleDate;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.callback.UICallback;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.GetRequest;
import com.xcheng.okhttp.request.OkCall;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

/**
 * 定时上传位置
 * Created by dongdaqing on 2017/7/28.
 */

public class UploadLocationJob extends Job {
    private static int count = 0;
    public static final String LOCATION = "com.hletong.hyc.job.upload_location";

    @NonNull
    @Override
    protected Result onRunJob(Params params) {
        SimpleDate sd = new SimpleDate(Calendar.getInstance());
     //   Logger.e("upload job running, time:" + (sd.dateString() + " " + sd.timeString()) + ", count => " + ++count);
        upLoadLocation();
        return Result.SUCCESS;
    }

    public static void scheduleJob() {
        new JobRequest.Builder(LOCATION)
                .setPeriodic(SimpleDate.MINUTE_MILLISECONDS * 15)
                .setRequiredNetworkType(JobRequest.NetworkType.CONNECTED)
                .setPersisted(true)
                .setUpdateCurrent(true)
                .build()
                .schedule();
    }

    /**
     * 是否提交了位置请求
     */
    public static boolean upLoadLocation() {
        if (!LoginInfo.hasLogin())
            return false;
        HyApplication.startLocation();
        //定位成功
        AMapLocation location = HyApplication.getAMapLocation();
        SimpleDate sd = new SimpleDate(Calendar.getInstance());
        SPUtils.putString("time", (sd.dateString() + " " + sd.timeString()));
        SPUtils.putString("location", location != null ? location.toString() : "null");

        if (location != null && location.getErrorCode() == 0) {
            GetRequest request = EasyOkHttp.get(Constant.LOCATION_UPLOAD)
                    .param("trailType", BuildConfig.app_type)
                    .param("longitude", location.getLongitude())
                    .param("latitude", location.getLatitude())
                    .build();

            new ExecutorCall<CommonResult>(request).enqueue(new UICallback<CommonResult>() {
                @Override
                public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                   // Logger.d("upload succeed");
                }

                @Override
                public void onError(OkCall<CommonResult> okCall, EasyError error) {
                   // Logger.e("upload failed, " + error.getMessage());
                }
            });
            return true;
        }
        return false;
    }

    //定时5分钟传一次
    public static void timerLocation() {
        Timer timer = new Timer("locationTimer", true);
        timer.schedule(new TimerTask() {
            boolean isUpLocation = true;

            @Override
            public void run() {
                boolean hasCommit = false;
                if (isUpLocation) {
                    hasCommit = UploadLocationJob.upLoadLocation();
                }
                isUpLocation = !(isUpLocation && hasCommit);
            }
        }, 0, TimeUnit.SECONDS.toMillis(150));
    }
}
