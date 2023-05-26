# VLibrary

[![](https://jitpack.io/v/oooo7777777/VLibrary.svg)](https://jitpack.io/#oooo7777777/VLibrary)


#### 介绍

- 基于MVVM模式组件化集成谷歌官方推荐的JetPack组件库：DataBinding、LiveData、ViewModel、Lifecycle组件
- 使用kotlin语言，添加大量拓展函数，简化代码
- 加入Retrofit网络请求,协程，帮你简化各种操作，让你快速请求网络
- 封装Base类，添加常用工具类

#### 搭配代码模板插件[VLibraryPlugin](https://github.com/oooo7777777/VLibraryPlugin)使用开发效率翻倍 :exclamation:  :exclamation: 
#### 如遇不会可参考该项目[TTProject](https://github.com/oooo7777777/TTProject):exclamation:  :exclamation:


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
    //设置全局通用配置
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



#### 使用文档

**具体使用方法以及属性内容请查看wiki**

**重要的事情说3遍!!!**

- **[wiki](https://github.com/oooo7777777/VLibrary/wiki)**

- **[wiki](https://github.com/oooo7777777/VLibrary/wiki)**

- **[wiki](https://github.com/oooo7777777/VLibrary/wiki)**
- 

#### 与master分支差异
1.此版本.VBActivity有实现VBBaseTagInterface
2.此版本.有引入第三方多语言切换库

