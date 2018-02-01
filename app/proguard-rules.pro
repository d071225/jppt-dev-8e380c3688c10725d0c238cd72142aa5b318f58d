# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\droidDev\copysdk\sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
-dontwarn okio.**
-dontwarn org.hamcrest.**
-dontwarn org.junit.**
-dontwarn android.test.**
-dontwarn com.squareup.**
-keepclassmembers class com.hletong.hyc.model.** { *; }
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.hletong.mob.util.permission.PermissionFail <methods>;
    @com.hletong.mob.util.permission.PermissionSuccess <methods>;
    @com.hletong.mob.validator.Form <methods>;
}

#动画混淆
-keep class com.wang.avi.** { *; }
-keep class com.wang.avi.indicators.** { *; }

#微信分享
-keep class com.tencent.mm.opensdk.** {
   *;
}
-keep class com.tencent.wxop.** {
   *;
}
-keep class com.tencent.mm.sdk.** {
   *;
}