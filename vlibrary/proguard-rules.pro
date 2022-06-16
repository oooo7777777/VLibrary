#ViewDataBinding
-keep public class * extends androidx.databinding.ViewDataBinding { *; }

# Lifecycle
-keep public class * extends androidx.lifecycle.ViewModel { *; }


#枚举
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

#glide
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep class * extends com.bumptech.glide.module.AppGlideModule {
 <init>(...);
}
-keep public enum com.bumptech.glide.load.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
-keep class com.bumptech.glide.load.data.ParcelFileDescriptorRewinder$InternalRewinder {
  *** rewind();
}

# Retrofit
-keepattributes Signature, InnerClasses, EnclosingMethod
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault

-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn javax.annotation.**
-dontwarn kotlin.Unit

-dontwarn retrofit2.KotlinExtensions

-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>

-keep,allowobfuscation,allowshrinking interface retrofit2.Call

-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

# fastjson.
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**{*;}
#kotlin data class

-keepattributes *Annotation*
-keep class kotlin.** { *; }
-keep class org.jetbrains.** { *; }

#实体类
-dontwarn **.*bean*.**
-keep class **.*bean*.** { *;}
-keep public class * extends com.v.base.bean.VBApiResponse { *; }
-keep public class * extends com.v.base.bean.VBResponse { *; }


#BaseRecyclerViewAdapterHelper
-keep public class * extends com.chad.library.adapter.base.viewholder.BaseViewHolder{
 <init>(...);
}
-keepclassmembers  class **$** extends com.chad.library.adapter.base.viewholder.BaseViewHolder {
     <init>(...);
}

#状态栏
-keep class com.gyf.immersionbar.* {*;}
 -dontwarn com.gyf.immersionbar.**