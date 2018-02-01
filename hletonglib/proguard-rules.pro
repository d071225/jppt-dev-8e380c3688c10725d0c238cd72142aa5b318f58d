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

-keepattributes Exceptions,InnerClasses,Signature

-keepclasseswithmembers class * {
    public <init>(android.content.Context);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int, int);
}

-keepclassmembers class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public <init>(android.content.Context, android.util.AttributeSet, int, int);
    void set*(***);
    *** get*();
}

#-keep class com.hletong.mob.pullrefresh.BaseRecyclerAdapter {
#    *;
#}
#
#-keep class com.hletong.mob.pullrefresh.BaseHolder {
#    *;
#}
#
#-keep class com.hletong.mob.dialog.BaseDialog {
#    *;
#}

#-keepnames class * extends android.view.View
#-keep class com.hletong.mob.pullrefresh.*
#-keep class com.hletong.mob.net.*
#-keep class com.hletong.mob.ipresenter.*
#-keep class com.hletong.mob.base.*
#-keep class com.hletong.mob.iview.*
#-keep class com.hletong.mob.security.*
#-keep class com.hletong.mob.util.*
#-keep class com.hletong.mob.HLApplication
#
-keep class com.hletong.mob.***{
    *;
}

#删除log
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}





