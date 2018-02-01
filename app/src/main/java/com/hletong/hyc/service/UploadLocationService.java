package com.hletong.hyc.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.http.AnimCallBack;
import com.orhanobut.logger.Logger;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.error.EasyError;
import com.xcheng.okhttp.request.ExecutorCall;
import com.xcheng.okhttp.request.GetRequest;
import com.xcheng.okhttp.request.OkCall;

/**
 * Created by dongdaqing on 2017/7/28.
 */

public class UploadLocationService extends Service implements AMapLocationListener {
    private AMapLocationClient mClient;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mClient = new AMapLocationClient(this);
        mClient.setLocationListener(this);
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);//可选，设置定位模式，可选的模式有高精度、仅设备、仅网络。默认为高精度模式
        option.setGpsFirst(true);//可选，设置是否gps优先，只在高精度模式下有效。默认关闭
        option.setHttpTimeOut(4000);//可选，设置网络请求超时时间。
        option.setInterval(1000);//可选，设置定位间隔。默认为2秒
        option.setNeedAddress(false);//可选，设置是否返回逆地理地址信息。默认是true
        option.setOnceLocation(true);//可选，设置是否单次定位。默认是false
        mClient.startLocation();
        return START_NOT_STICKY;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        mClient.stopLocation();
        mClient.unRegisterLocationListener(this);
        mClient.onDestroy();
        if (aMapLocation.getErrorCode() == 0) {
            //定位失败
            //结束service
            Logger.d("locate failed");
            stopService();
        } else {
            //定位成功
            GetRequest request = EasyOkHttp.get(Constant.LOCATION_UPLOAD)
                    .param("trailType", String.valueOf(BuildConfig.app_type))
                    .param("longitude", aMapLocation.getLongitude())
                    .param("latitude", aMapLocation.getLatitude())
                    .build();

            new ExecutorCall<CommonResult>(request).enqueue(new AnimCallBack<CommonResult>(null) {
                @Override
                public void onSuccess(OkCall<CommonResult> okCall, CommonResult response) {
                    Logger.d("upload succeed");
                    stopService();
                }

                @Override
                public void onError(OkCall<CommonResult> okCall, EasyError error) {
                    Logger.e("upload failed, " + error.getMessage());
                    stopService();
                }
            });
        }
    }

    private void stopService() {
        //结束service
        Logger.d("stop upload service");
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("upload service destroyed");
    }
}
