# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\adt-bundle-windows-x86_64-20131030\sdk/tools/proguard/proguard-android.txt
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

-keep public class com.github.ksoichiro.android.observablescrollview.**

-dontnote sun.misc.Unsafe
-keep class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef** {
    *;
}

-keep class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef** {
    *;
}

-dontnote rx.internal.util.PlatformDependent

-keep class com.google.**
-dontnote com.android.**
-dontnote org.apache.**
-dontnote sun.security.**
-dontnote java.util.**
-dontnote org.robovm.**
-dontnote com.google.gson.**
-keep class com.facebook.drawee.**
-keep class com.whoami.voz.delegate.PagerListener
-keep class com.whoami.voz.widget.SlidingTabLayout** {
 *;
  }
#-keep android.net.http.**
#-dontwarn org.apache.http.**
-keep class org.apache.** {*;}
-keep class org.apache.http.**
-keep interface org.apache.http.**
-keep class me.relex.photodraweeview**
-keep class com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory
-keep class com.facebook.imagepipeline.core.ExecutorSupplier
-keep class com.facebook.imagepipeline.animated.factory.AnimatedFactoryImplSupport
-dontnote okhttp3.internal.platform.**
-dontnote com.facebook.imagepipeline.animated.factory.**

# Platform calls Class.forName on types which do not exist on Android to determine platform.
-dontnote retrofit2.Platform
# Platform used when running on Java 8 VMs. Will not be used at runtime.
-dontwarn retrofit2.Platform$Java8
# Retain generic type information for use by reflection by converters and adapters.
-keepattributes Signature
# Retain declared checked exceptions for use by a Proxy instance.
-keepattributes Exceptions
-dontwarn okio.**
-dontwarn javax.annotation.**
#-keepattributes *Annotation*,Signature
-keep class com.facebook.imagepipeline.animated.factory.AnimatedFactoryImplSupport