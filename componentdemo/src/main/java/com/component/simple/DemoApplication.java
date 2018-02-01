package com.component.simple;

import com.hletong.mob.HLApplication;
import com.hletong.mob.net.EventSessionTimeout;
import com.hletong.mob.net.OkHttpUtil;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;
import com.xcheng.okhttp.EasyOkHttp;
import com.xcheng.okhttp.request.OkConfig;

/**
 * Created by chengxin on 2017/3/10.
 */
public class DemoApplication extends HLApplication {

    @Override
    public void initHttpClient() {
        OkHttpUtil.initHttps(20, 10, 30, CER_HY_DEV_HTTPS);
        OkConfig okConfig = OkConfig.newBuilder()
                .host("https://test.hletong.com/jppt/")
                .okHttpClient(OkHttpUtil.getOkHttpClient())
                .postUiIfCanceled(false)
                .parseClass(HttpDemoParse.class)
                .build();
        EasyOkHttp.init(okConfig);
        Logger.addLogAdapter(new AndroidLogAdapter(PrettyFormatStrategy.newBuilder().tag("HLET_LOG").methodCount(1).showThreadInfo(false).build()) {
            @Override
            public boolean isLoggable(int priority, String tag) {
                return BuildConfig.DEBUG;
            }
        });
    }

    @Override
    public void handleSessionTimeout(EventSessionTimeout event) {

    }
    public static final String CER_HY_DEV_HTTPS = "-----BEGIN CERTIFICATE-----\n" +
            "MIIGKzCCBROgAwIBAgIQRLQbkc4XnyUQV4TZQRlq3zANBgkqhkiG9w0BAQsFADBm\n" +
            "MQswCQYDVQQGEwJVUzEWMBQGA1UEChMNR2VvVHJ1c3QgSW5jLjEdMBsGA1UECxMU\n" +
            "RG9tYWluIFZhbGlkYXRlZCBTU0wxIDAeBgNVBAMTF0dlb1RydXN0IERWIFNTTCBD\n" +
            "QSAtIEczMB4XDTE2MDQyNjAwMDAwMFoXDTE4MDQyNjIzNTk1OVowGDEWMBQGA1UE\n" +
            "AwwNKi5obGV0b25nLmNvbTCCASIwDQYJKoZIhvcNAQEBBQADggEPADCCAQoCggEB\n" +
            "ANWlBC8Jev82gm5qiaHswgEVD+GEcKFH+oioixvjhRZZyLarN0u4dPuMggfoXk33\n" +
            "Q0aA0TAcUcx0kqs+gQ10BbpwarSNIJQS6DhLVdfNS68llk/SbixDkmQcJESgwt98\n" +
            "p29YobZsdB5d6tguUa8raUCQP8lVCWAEu+lsHWx0LlGnR4xW5NTHI6U2XqzY0E3D\n" +
            "4AV4YQ/JjCk3/GdF1K7ssXxXk60wEETWyMTTiHrHX/iyglJVroKsJ5dVEL02StZF\n" +
            "JNvSVAasZkIyQ9xmEAXetyog4CJxT2fgcUfIohMYJ11c4kjX6HSnrfearojus8H8\n" +
            "7g24AVSGrns496D7rsXpaL0CAwEAAaOCAyEwggMdMCUGA1UdEQQeMByCDSouaGxl\n" +
            "dG9uZy5jb22CC2hsZXRvbmcuY29tMAkGA1UdEwQCMAAwKwYDVR0fBCQwIjAgoB6g\n" +
            "HIYaaHR0cDovL2d0LnN5bWNiLmNvbS9ndC5jcmwwgY8GA1UdIASBhzCBhDCBgQYG\n" +
            "Z4EMAQIBMHcwMgYIKwYBBQUHAgEWJmh0dHBzOi8vd3d3Lmdlb3RydXN0LmNvbS9y\n" +
            "ZXNvdXJjZXMvY3BzMEEGCCsGAQUFBwICMDUMM2h0dHBzOi8vd3d3Lmdlb3RydXN0\n" +
            "LmNvbS9yZXNvdXJjZXMvcmVwb3NpdG9yeS9sZWdhbDAfBgNVHSMEGDAWgBStZSKF\n" +
            "kNA746FJizf58QsdXxegdzAOBgNVHQ8BAf8EBAMCBaAwHQYDVR0lBBYwFAYIKwYB\n" +
            "BQUHAwEGCCsGAQUFBwMCMFcGCCsGAQUFBwEBBEswSTAfBggrBgEFBQcwAYYTaHR0\n" +
            "cDovL2d0LnN5bWNkLmNvbTAmBggrBgEFBQcwAoYaaHR0cDovL2d0LnN5bWNiLmNv\n" +
            "bS9ndC5jcnQwggF/BgorBgEEAdZ5AgQCBIIBbwSCAWsBaQB2AN3rHSt6DU+mIIuB\n" +
            "rYFocH4ujp0B1VyIjT0RxM227L7MAAABVFLKBaoAAAQDAEcwRQIhAO8AmjdgzsHd\n" +
            "1kmN7/YNuHR506Ymead+tVfUZ4qqyBiYAiAKuU4k9Li+QBiW8RhhrbWy8+aJmFWr\n" +
            "PPhJkDhMNEeadQB2AKS5CZC0GFgUh7sTosxncAo8NZgE+RvfuON3zQ7IDdwQAAAB\n" +
            "VFLKBbcAAAQDAEcwRQIgWEqGeM1Ie/4m/rFjWy1qoiyOnsEqTYSblfikhq1NRl8C\n" +
            "IQCm45/aoLNldgxnkc1ZjfMqM9krJdac1EZB6iQD84T1swB3AGj2mPgfZIK+Oozu\n" +
            "uSgdTPxxUV1nk9RE0QpnrLtPT/vEAAABVFLKBcAAAAQDAEgwRgIhAJGryLglgsOi\n" +
            "qdnV8IIY/Omym9RYu5bE52vwxuI7BmoUAiEA5+T0lw/fGm86fadKElDoRH9ryeyk\n" +
            "avWe35s7QiUvXKcwDQYJKoZIhvcNAQELBQADggEBAEHuAInGvfXUzNfW60Edf7aU\n" +
            "z2ZGlNM44nGBmjXZy89H6DYAzivG6sSRLw+J1rNB7S2H6Bc8ekLHL1xvF1/fSrEk\n" +
            "52fPDh70QWwHSDS6DSjGW1P3dD2fr/js4wi5yE66EP5INCJxVTqkQgJ9cjCJKF1x\n" +
            "baXrkvV2PMnT9HmCQVjxP5s69QObz+pwT1ezC+KU2n9DGeMophW/Fzx6jPiZiRxv\n" +
            "NNhX+oH2CCt/c6D2+eHSmKPYoM2kLJsok4RYHdz5YJwhIBzKqQplrS+CvmQvajtI\n" +
            "YVPE5g5KANvoWiBFvLs84cAOEi8X1fx0i334yKRzW2hiXwzeLqWNEG+tAwEtm0E=\n" +
            "-----END CERTIFICATE-----\n";
}
