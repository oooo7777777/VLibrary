# VLibrary40

[![](https://jitpack.io/v/oooo7777777/VLibrary.svg)](https://jitpack.io/#oooo7777777/VLibrary)


#### 介绍

- 基于MVVM模式组件化集成谷歌官方推荐的JetPack组件库：LiveData、ViewModel、Lifecycle组件
- 使用kotlin语言，添加大量拓展函数，简化代码
- 加入Retrofit网络请求,协程，帮你简化各种操作，让你快速请求网络
- 封装Base类，添加常用工具类

#### 搭配代码模板插件[VLibraryPlugin](https://github.com/oooo7777777/VLibraryPlugin)使用开发效率翻倍 :exclamation:  :exclamation: 

#### APK下载
[APK下载](http://d.zqapps.com/uvpj)


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
  implementation 'com.github.oooo7777777:VLibrary:Latest'
}
```

- **3. 在app build.gradle中，android 模块下开启ViewDataBinding**

```

android {
    ...
   buildFeatures {
        dataBinding = true
    }
}
 
```

- **4. 继承BaseApplication**

```
open class *** : BaseApplication() {

}
```

- **5. 设置AndroidManifest.xml主题**

```
 android:theme="@style/Base_AppTheme"
```

#### 继承基类

一般我们项目中都会有一套自己定义的符合业务需求的基类 ***BaseActivity/BaseFragment***，所以我们的基类需要**继承本框架的Base类**



- **使用Activity(基类为AppCompatActivity)**
```
/**
 * @param MeActivityBinding ViewDataBinding
 * @param BlankViewModel BaseViewModel(如果该页面不使用ViewModel 使用BlankViewModel)
 */
class *** : BaseActivity<***Binding, BlankViewModel>() {
    
 
}
```

- **使用Fragment(基类为Fragment)**
```
/**
 * @param MeFragmentBinding ViewDataBinding
 * @param MeViewModel BaseViewModel(如果该页面不使用ViewModel 使用BlankViewModel)
 */
class *** : BaseFragment<***Binding, MeViewModel>() {

  
}
```

- **使用ViewModel**

```
class ***ViewModel : BaseViewModel() {
    
    
}
```


- **使用Dialog(基类为DialogFragment)**

```
class *** : BaseDialogFragment<***Binding, BlankViewModel>() {

 
}
```



#### 功能展示

- **BaseApplication**

```
class ***: BaseApplication() {

    /**
     * 重写此方法 开启日志打印(日志TAG为 PRETTY_LOGGER)
     */
    override fun isDebug(): Boolean {
        return true
    }

    /**
     * 重写此方法 设置全局状态栏颜色
     */
    override fun statusBarColor(): Int {
        return randomColor
    }

    /**
     * 使用VLibrary库自带的网络请求时候请重写此方法
     * 传入baseUrl
     */
    override fun baseUrl(): String {
        return "https://gank.io/api/v2/"
    }

    /**
     * 一般不用动~~~~~
     * 如需要对VLibrary库自带的网络请求Retrofit.Builder做任意操作，比如添加json解析器，请重写此方法
     *
     */
    override fun retrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder().apply {
            addConverterFactory(FastJsonConverterFactory.create())
            addCallAdapterFactory(CoroutineCallAdapterFactory())
        }
    }

    /**
     * 如需要对VLibrary库自带的网络请求OkHttpClient做任意操作，在这里可以添加拦截器，请求头
     */
    override fun okHttpClient(): OkHttpClient {

        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)   //超时时间 连接、读、写
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(NetworkHeadInterceptor())  //添加公共heads 注意要设置在日志拦截器之前，不然Log中会不显示head信息
            .addInterceptor(BaseLogInterceptor())// 日志拦截器（这里请使用BaseLogInterceptor，不然网络请求日志不会打印出来）
            .build()
    }

}
```
- **BaseActivity**

```
class ***: BaseActivity<***Binding, BlankViewModel>() {
    
    /**
     * 重写此方法
     * 设置显示Toolbar
     * @param title 标题(默认是空字符串 空字符则表示不显示Toolbar)
     * @param title 标题颜色
     */
    override fun toolBarTitle(title: String, titleColor: Int) {
        super.toolBarTitle(title, titleColor)
    }

    /**
     * 重写此方法
     * 设置Toolbar左边按钮事件
     * @param resId 按钮图片资源(默认箭头)
     * @param listener 按钮事件(默认onBackPressed)
     */
    override fun toolBarLift(resId: Int, listener: View.OnClickListener) {
        super.toolBarLift(resId, listener)
    }

    /**
     * 重写此方法
     * 设置Toolbar右边按钮图片事件
     * @param resId 按钮资源
     * @param listener 按钮事件
     */
    override fun toolBarRight(resId: Int, listener: View.OnClickListener?) {
        super.toolBarRight(resId, listener)
    }

    /**
     * 重写此方法
     * 设置Toolbar右边按钮文字事件
     * @param text 按钮资源
     * @param listener 按钮事件
     */
    override fun toolBarRight(text: String, textColor: Int, listener: View.OnClickListener?) {
        super.toolBarRight(text, textColor, listener)
    }

    /**
     * 重写此方法
     * 设置状态栏颜色
     * @param color 颜色资源
     */
    override fun statusBarColor(color: Int) {
        super.statusBarColor(color)
    }

    /**
     * 重写此方法
     * 设置是否隐藏状态栏(布局需要顶入状态栏的时候使用)
     */
    override fun useStatusBar(): Boolean {
        return super.useStatusBar()
    }

    /**
     * 重写此方法
     * 设置是否显示Toolbar
     */
    override fun useTitleBar(): Int {
        return super.useTitleBar()
    }
}
```

- **BaseFragment**

```
class ***: BaseFragment<***Binding, MeViewModel>() {

    /**
     * 重写此方法
     *懒加载 对用户第一次可见
     */
    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
    }

    /**
     * 重写此方法
     *懒加载 对用户可见
     */
    override fun onFragmentPause() {
        super.onFragmentPause()
    }
    
    /**
     * 重写此方法
     *懒加载 对用户不可见
     */
    override fun onFragmentResume() {
        super.onFragmentResume()
    }
}
```

- **BaseDialogFragment**

```
class ***: BaseDialogFragment<***Binding, BlankViewModel>() {

    /**
     * 重写此方法
     * 设置返回键不退出dialog
     */
    override fun useCancellation(): Boolean {
        return super.useCancellation()
    }

    /**
     * 重写此方法
     * 设置dialog弹出时界面是否变暗
     */
    override fun useDim(): Boolean {
        return super.useDim()
    }

    /**
     * 重写此方法
     * 设置dialog弹出方向 [DialogOrientation]
     * 支持   TOP, BOTTOM, LEFT, RIGHT, CENTRE
     * 添加相对于的弹出动画
     */
    override fun useDirection(): DialogOrientation {
        return super.useDirection()
    }

}
```

#### 网络请求

> 第一种方法

- **1. 继承BaseApplication**

```
class DemoApplication : BaseApplication() {

    /**
     * 使用VLibrary库自带的网络请求时候请重写此方法
     * 传入baseUrl
     */
    override fun baseUrl(): String {
        return "https://gank.io/api/v2/"
    }

    /**
     * 一般不用动~~~~~
     * 如需要对VLibrary库自带的网络请求Retrofit.Builder做任意操作，比如添加json解析器，请重写此方法
     *
     */
    override fun retrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder().apply {
            addConverterFactory(FastJsonConverterFactory.create())
            addCallAdapterFactory(CoroutineCallAdapterFactory())
        }
    }

    /**
     * 如需要对VLibrary库自带的网络请求OkHttpClient做任意操作，在这里可以添加拦截器，请求头
     */
    override fun okHttpClient(): OkHttpClient {

        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)   //超时时间 连接、读、写
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(NetworkHeadInterceptor())  //添加公共heads 注意要设置在日志拦截器之前，不然Log中会不显示head信息
            .addInterceptor(BaseLogInterceptor())// 日志拦截器（这里请使用BaseLogInterceptor，不然网络请求日志不会打印出来）
            .build()
    }

 

}
```

- **2. 继承BaseViewModel**

```
class ***: BaseViewModel() {

//get请求
//apiBase.get("url")
//apiBase.get("url",map)

//post请求
//apiBase.post("url")
//apiBase.post("url",map)


    var bean = MutableLiveData<Bean>()

    fun getData() {
        
        //此处返回得数据是泛型,即你传入得是什么类型,得到得就是什么类型
        request(
            {
                apiBase.get("banners")
            },
            bean
        )

    }



}
```

> 第二种方法

- **1. 新建请求配置类继承BaseNetwork**

```
class Network : BaseNetwork() {

    companion object {

        //自定义的api请求
        val apiService: NetworkApi by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            instance.getApi(NetworkApi::class.java, NetworkApi.SERVER_URL)
        }

        private val instance: Network by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            Network()
        }

    }

    /**
     * 实现重写父类的setHttpClientBuilder方法，
     * 在这里可以添加拦截器，可以对 OkHttpClient.Builder 做任意操作
     */
    override fun setHttpClientBuilder(builder: OkHttpClient.Builder): OkHttpClient.Builder {
        builder.apply {
            //示例：添加公共heads 注意要设置在日志拦截器之前，不然Log中会不显示head信息
            addInterceptor(NetworkHeadInterceptor())
            // 日志拦截器
            addInterceptor(BaseLogInterceptor())
            //超时时间 连接、读、写
            connectTimeout(10, TimeUnit.SECONDS)
            readTimeout(5, TimeUnit.SECONDS)
            writeTimeout(5, TimeUnit.SECONDS)
        }
        return builder
    }

    /**
     * 实现重写父类的setRetrofitBuilder方法，
     * 在这里可以对Retrofit.Builder做任意操作，比如添加GSON解析器，protobuf等
     */
    override fun setRetrofitBuilder(builder: Retrofit.Builder): Retrofit.Builder {
        return builder.apply {
            addConverterFactory(FastJsonConverterFactory.create())
            addCallAdapterFactory(CoroutineCallAdapterFactory())
        }
    }

}
```

- **2.如果你请求服务器返回的数据有基类（没有可忽略这一步）**

```
{
    "data": ...,
    "errorCode": 0,
    "errorMsg": ""
}
```
该示例格式是 [玩Android Api](https://www.wanandroid.com/blog/show/2)返回的数据格式，如果errorCode等于0 请求成功，否则请求失败
作为开发者的角度来说，我们主要是想得到脱壳数据-data，且不想每次都判断errorCode==0请求是否成功或失败
这时我们可以在服务器返回数据基类中继承BaseResponse，实现相关方法：

```
class ApiResponse<T>(var status: Int, var msg: String, var data: T)  : BaseResponse<T>() {

    // 这里是示例，wanandroid 网站返回的 错误码为 0 就代表请求成功，请你根据自己的业务需求来编写
    override fun isSuccess() = status == 100

    override fun getResponseCode() = status

    override fun getResponseData() = data!!

    override fun getResponseMsg() = msg
    
}
```

- **3. 在ViewModel中发起请求，所有请求都是在viewModelScope中启动，请求会发生在IO线程，最终回调在主线程上，当页面销毁的时候，请求会统一取消，不用担心内存泄露的风险，框架做了2种请求使用方式**  

```
class ***: BaseViewModel {

  ///自定义api 网络请求 获取去壳数据
    fun getCustom() {

        request({
            //协程请求体
            apiService.getBanner()
        }, success = {
            //成功数据
            string.value = it.toJson()
        }, error = {
            //失败数据
        }, dialog = true)//是否显示加载框
    }

    //自定义api 网络请求 获取原始数据
    fun getCustomDefault() {

        requestDefault({
            apiService.getBanner()
        }, success = {
            string.value = it.toJson()
        }, dialog = true)
    }
}

```

#### 其他功能

- **HintDialog(提示弹出框)**

```
HintDialog()
    .setTitle("提示")//可选 输入则显示title
    .setContent("确定保存吗?")
    .setButtonText("取消", "确定")//最大支持两个按钮 输入一个参数 则只展示一个按钮
    .setHintDialogClickListener(object : HintDialog.HintDialogClickListener {
        override fun onClick(hintDialog: HintDialog, position: Int) {
            hintDialog.dismiss()

        }

    }).show(mContext)
```

- **ListDialog(列表弹出框)**

```
 var list = ArrayList<String>()
    for ( i in 1..3) {
        list.add("Content$i")
    }
    ListDialog()
    .setList(list)
    .setListDialogListener(object : ListDialog.ListDialogListener {
        override fun onItem(dialog: ListDialog, result: String, position: Int) {
            dialog.dismiss()
            result.toast()
        }

    }).show(mContext)
```

- **ImgSelectDialog(图片选择弹出框,此Dialog只是做了简单的使用,如需自定义请自行处理)**
  1. 可选择拍照,相册
  2. 直接拿到选择图片file
  3. 通过CoCo[一款小而美的的Android系统相机拍照、系统相册选择、裁剪库](https://github.com/soulqw/CoCo)
  4. 图片是否压缩 KLuban[基于Luban算法，重构后实现的图片压缩框架](https://github.com/forJrking/KLuban)

```
ImgSelectDialog()
    .setImageCompression(true)//是否开启压缩
    .setIgnoreBy(100)///期望压缩大小,大小和图片呈现质量不能均衡所以压缩后不一定小于此值
    .setPhotoSelectDialogListener(object :
        ImgSelectDialog.PhotoSelectDialogListener {
        override fun onSuccess(file: File) {

        }

    }).show(mContext)
```

#### 列表数据展示
- **Adapter使用[BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)**
- **刷新框架使用[SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)**

- **表格布局**

```
recyclerView.grid(**Adapter(), 4) as **Adapter      
```

- **竖向布局**
```
recyclerView.linear(**Adapter()) as **Adapter
```

- **横向布局**
```
recyclerView.linearHorizontal(**Adapter()) as **Adapter
```

- **设置分割线(其他用法请自行查看 com.v.base.utils\RecyclerViewItemDecoration)**
```
recyclerView.divider {
            setDrawable(R.drawable.dm_divider_horizontal)
            orientation = RecyclerViewItemOrientation.GRID
            startVisible = true
        }
```

> 列表数据展示示例

```
//当前页数
private var page = 1

//设置表格布局
private val mAdapter by lazy {
     mViewBinding.recyclerView.divider {
         orientation = RecyclerViewItemOrientation.GRID
         includeVisible = true
         setColor(Color.parseColor("#ff0000"))
         setDivider(20)
     }.grid(HomeFragmentAdapter(), 3) as HomeFragmentAdapter
 }

override fun initData() {
     //开启自动刷新
     mViewBinding.refreshLayout.autoRefresh()
     //ViewModel获取数据
     mViewModel.getList(page)
 }

override fun createObserver() {
     //得到数据
     mViewModel.listBean.observe(this, Observer {
         mAdapter.loadData(mViewBinding.refreshLayout,
             it,//列表数据
             page,//当前页数
             onRefresh = {//下拉刷新
                 page = 1
                 mViewModel.getList(page)
             },
             onLoadMore = {//上拉加载
                 page = it//it为传入的page 请求成功会自动加1
                 mViewModel.getList(page)
             },
             //item点击
             onItemClick = { view: View, i: Int ->
             },
             //item view 点击
             onItemChildClick = { view: View, i: Int ->

             },
             //空布局设置
             emptyView = {

             })
     })
            
                
   //方法可按需使用(以下为我只要item点击)
    mViewModel.listBean.observe(this, Observer {
         mAdapter.loadData(mViewBinding.refreshLayout,
             it,
             page,
             onItemClick = { view: View, i: Int ->
             }
             )
     })
}

```


#### 图片加载(使用的[Glide](https://github.com/bumptech/glide))

```
/**
 * 加载图片
 * @param any 图片资源Glide所支持的
 * @param roundingRadius 图片圆角角度
 * @param errorResId 加载错误占位图
 */
fun ImageView.load(
    any: Any,
    roundingRadius: Float = 0f,
    errorResId: Int = R.mipmap.base_iv_default
) = loadDispose(this, any, roundingRadius, errorResId)


/**
 * 加载圆形图片
 * @param any 图片资源Glide所支持的
 * @param errorResId 加载错误占位图
 */
fun ImageView.loadCircle(any: Any, errorResId: Int = R.mipmap.base_iv_default) =
    loadDispose(this, any, -1f, errorResId)

```

- **代码使用**
```
imageView.loadCircle(file)
imageView.load(file, 10f)
```

- **xml里面使用**
```
<ImageView
      android:layout_width="match_parent"
      android:scaleType="centerCrop"
      app:imgUrl="@{bean.url}"
      app:imgRadius="@{10f}"
      app:circle="@{true}"
      android:layout_height="match_parent" />
```


#### 使用LiveData 代替EventBus
```
//发送数据
LiveDataBus.with<String>(ConstData.CONTENT).postData("模拟eventBus")

//监听数据
LiveDataBus.with<String>(ConstData.CONTENT).observe(this, Observer {
            
        })
```

#### 全部数据监听
```
//发送数据
 getApplicationViewModel(
                    mContext.application,
                    AppViewModel::class.java
                ).userBane.postValue(bean)

//监听数据
 getApplicationViewModel(mContext.application, AppViewModel::class.java).userBane.observe(
            this,
            androidx.lifecycle.Observer {
                mViewBinding.userBean = it
            })
```


#### 重点 :exclamation: :exclamation: :exclamation: 
#### 拓展函数,以及常用工具类请自行查看com.v.base.utils  一定要看 :exclamation: :exclamation: :exclamation: 



#### 使用到的第三方库

```
"androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0",
"com.google.android.material:material:1.2.1",
"androidx.appcompat:appcompat:1.2.0",
"androidx.recyclerview:recyclerview:1.1.0",
"androidx.activity:activity:1.2.0-beta01",
"androidx.fragment:fragment:1.3.0-beta01",
"androidx.constraintlayout:constraintlayout:2.0.2",
"org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9",
"com.squareup.retrofit2:retrofit:2.9.0",
"com.alibaba:fastjson:1.2.73",
"com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2",
"com.orhanobut:logger:2.2.0",
"com.alibaba:arouter-api:1.5.1",
"com.scwang.smart:refresh-layout-kernel:2.0.1",
"com.scwang.smart:refresh-header-material:2.0.3",
"com.scwang.smart:refresh-footer-classics:2.0.0-alpha-1",
"com.github.CymChad:BaseRecyclerViewAdapterHelper:3.0.4",
"com.qw:coco:1.1.2",
"com.noober.background:core:1.6.5",
"com.github.forJrking:KLuban:1.0.5"
```

