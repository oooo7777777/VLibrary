# VLibrary

[![](https://jitpack.io/v/oooo7777777/VLibrary.svg)](https://jitpack.io/#oooo7777777/VLibrary)


#### 介绍

- 基于MVVM模式组件化集成谷歌官方推荐的JetPack组件库：DataBinding、LiveData、ViewModel、Lifecycle组件
- 使用kotlin语言，添加大量拓展函数，简化代码
- 加入Retrofit网络请求,协程，帮你简化各种操作，让你快速请求网络
- 封装Base类，添加常用工具类

#### 搭配代码模板插件[VLibraryPlugin](https://github.com/oooo7777777/VLibraryPlugin)使用开发效率翻倍 :exclamation:  :exclamation: 


#### 使用集成

- **1. 在root build.gradle中加入Jitpack仓库**

```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

- **2. 在app build.gradle中添加依赖**

```
dependencies {
  ...
  implementation 'com.github.oooo7777777:VLibrary:latestVersion'
}
```

- **3. 在app build.gradle中，android 模块下开启DataBinding**

```

android {
    ...
   buildFeatures {
        dataBinding = true
    }
}
 
```

- **4. 继承VBApplication**

```
open class *** : VBApplication() {

    override fun initData() {
        val netOptions = VBNetOptions.Builder()
            .setBaseUrl("https://www.wanandroid.com/")
            .build()

        val options = VBConfigOptions.Builder()
            .setNetOptions(netOptions)
            .setStatusBarColor(Color.parseColor("#ffffff"))
            .build()

        VBConfig.init(options)
    }
    
}
```

- **5. 设置AndroidManifest.xml主题**

```
 android:theme="@style/vb_app_theme"
```

#### 使用文档

**具体使用方法以及属性内容请查看wiki**

**重要的事情说3遍!!!**

- **[wiki](https://github.com/oooo7777777/VLibrary/wiki)**

- **[wiki](https://github.com/oooo7777777/VLibrary/wiki)**

- **[wiki](https://github.com/oooo7777777/VLibrary/wiki)**


#### proguard-rules.pro
>此资源库自带混淆规则，并且会自动导入，正常情况下无需手动导入，仅限于3.3.3版本及以上版本。
>3.3.3之前请自行添加以下混淆代码 

```
#ViewDataBinding
-keep public class * extends androidx.databinding.ViewDataBinding { *; }

#Lifecycle
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

#Retrofit
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
-dontwarn retrofit2.KotlinExtensions$*
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class kotlin.coroutines.Continuation

#fastjson.
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
```
