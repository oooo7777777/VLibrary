# VLibrary

[![](https://jitpack.io/v/oooo7777777/VLibrary.svg)](https://jitpack.io/#oooo7777777/VLibrary)


#### 介绍

- 基于MVVM模式组件化集成谷歌官方推荐的JetPack组件库：DataBinding、LiveData、ViewModel、Lifecycle组件
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

}
```

- **5. 设置AndroidManifest.xml主题**

```
 android:theme="@style/vb_app_theme"
```

#### 继承基类

一般我们项目中都会有一套自己定义的符合业务需求的基类 ***VBActivity/VBFragment***，所以我们的基类需要**继承本框架的Base类**



- **使用Activity(基类为AppCompatActivity)**
```
/**
 * @param MainActivityBinding DataBinding
 * @param VBBlankViewModel VBViewModel(如果该页面不使用ViewModel 使用BlankViewModel)
 */
class *** : VBActivity<MainActivityBinding, VBBlankViewModel>() {
    
 
}
```

- **使用Fragment(基类为Fragment)**
```
/**
 * @param MainFragmentBinding DataBinding
 * @param MeViewModel VBViewModel(如果该页面不使用ViewModel 使用BlankViewModel)
 */
class *** : VBFragment<MainFragmentBinding, VBBlankViewModel>() {

  
}
```

- **使用ViewModel**

```
class MainViewModel : VBViewModel() {
    
    
}
```


- **使用Dialog(基类为DialogFragment)**

```
class *** : VBDialogFragment<***Binding, VBBlankViewModel>() {

 
}
```



#### 功能展示

- **VBApplication**

```
class ***: VBApplication() {

    /**
     * 重写此方法 开启日志打印(日志TAG为 PRETTY_LOGGER)
     */
    override fun isDebug(): Boolean {
        return true
    }

    /**
     * 重写此方法 设置全局状态栏颜色 状态栏颜色趋近于白色时 会智能将状态栏字体颜色变换为黑色
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
     * 如需要对VLibrary库自带的网络请求Retrofit.Builder做任意操作 比如添加json解析器 请重写此方法
     *
     */
    override fun retrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder().apply {
            addConverterFactory(VBFastJsonConverterFactory.create())
            addCallAdapterFactory(CoroutineCallAdapterFactory())
        }
    }

    /**
     * 如需要对VLibrary库自带的网络请求OkHttpClient做任意操作 在这里可以添加拦截器 请求头
     */
    override fun okHttpClient(): OkHttpClient {

        return OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)   //超时时间 连接、读、写
            .readTimeout(5, TimeUnit.SECONDS)
            .writeTimeout(5, TimeUnit.SECONDS)
            .addInterceptor(NetworkHeadInterceptor())  //添加公共heads 注意要设置在日志拦截器之前，不然Log中会不显示head信息
            .addInterceptor(VBLogInterceptor())// 日志拦截器（这里请使用BaseLogInterceptor，不然网络请求日志不会打印出来）
            .build()
    }

}
```
- **VBActivity**

```
class ***: VBActivity<***Binding, VBBlankViewModel>() {
    
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

- **VBFragment**

```
class ***: VBFragment<***Binding, MeViewModel>() {

    /**
     * 重写此方法
     *懒加载 对用户第一次可见
     */
    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
    }

    /**
     * 重写此方法
     *懒加载 对用户不可见
     */
    override fun onFragmentPause() {
        super.onFragmentPause()
    }
    
    /**
     * 重写此方法
     *懒加载 对用户可见
     */
    override fun onFragmentResume() {
        super.onFragmentResume()
    }
}
```

- **VBDialogFragment**

```
class ***: VBDialogFragment<***Binding, VBBlankViewModel>() {

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
     * 设置dialog弹出方向 [VBDialogOrientation]
     * 支持   TOP, BOTTOM, LEFT, RIGHT, CENTRE
     * 添加相对于的弹出动画
     */
    override fun useDirection(): VBDialogOrientation {
        return super.useDirection()
    }

}
```

#### 网络请求

> 第一种方法

- **1. 继承VBApplication**

```
class DemoApplication : VBApplication() {

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
            addConverterFactory(VBFastJsonConverterFactory.create())
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
            .addInterceptor(VBLogInterceptor())// 日志拦截器（这里请使用BaseLogInterceptor，不然网络请求日志不会打印出来）
            .build()
    }

 

}
```

- **2. 继承VBViewModel**

```
class ***: VBViewModel() {

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

- **1. 新建请求配置类继承VBNetwork**

```
class Network : VBNetwork() {

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
class ApiResponse<T>(var status: Int, var msg: String, var data: T)  : VBResponse<T>() {

    // 这里是示例，wanandroid 网站返回的 错误码为 0 就代表请求成功，请你根据自己的业务需求来编写
    override fun isSuccess() = status == 100

    override fun getResponseCode() = status

    override fun getResponseData() = data!!

    override fun getResponseMsg() = msg
    
}
```

- **3. 在ViewModel中发起请求，所有请求都是在viewModelScope中启动，请求会发生在IO线程，最终回调在主线程上，当页面销毁的时候，请求会统一取消，不用担心内存泄露的风险，框架做了2种请求使用方式**  

```
class ***: VBViewModel {

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

- **VBHintDialog(提示弹出框)**

```
HintDialog()
    .setTitle("提示")//可选 输入则显示title
    .setContent("确定保存吗?")
    .setButtonText("取消", "确定")//最大支持两个按钮 输入一个参数 则只展示一个按钮
    .setClickListener 
        { hintDialog, position ->
             hintDialog.dismiss()
             mViewModel.content.value = "点击$position"
        }.show(mContext)
```

- **VBListDialog(列表弹出框)**

```
 var list = ArrayList<String>()
    for ( i in 1..3) {
        list.add("Content$i")
    }
     VBListDialog()
       .setList(list)
       .setClickListener 
        { dialog, result, position ->
              dialog.dismiss()
              mViewModel.content.value = result
         }.show(mContext)
```


#### 列表数据展示
- **Adapter使用[BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)**
- **刷新框架使用[SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)**

- **表格布局**

```
recyclerView.vbGrid(**Adapter(), 4) as **Adapter      
```

- **竖向布局**
```
recyclerView.vbLinear(**Adapter()) as **Adapter
```

- **横向布局**
```
recyclerView.vbLinearHorizontal(**Adapter()) as **Adapter
```

- **设置分割线(其他用法请自行查看 com.v.base.utils\RecyclerViewItemDecoration)**
```
recyclerView.vbDivider {
            setDrawable(R.drawable.dm_divider_horizontal)
            orientation = RecyclerViewItemOrientation.GRID
            isStartVisible = true
        }
```

> 列表数据展示示例

```
//当前页数
private var page = 1

//设置表格布局
private val mAdapter by lazy {
     mDataBinding.recyclerView.vbDivider {
         includeVisible = true
         setColor(Color.parseColor("#ff0000"))
         setDivider(20)
     }.vbGrid(HomeFragmentAdapter(), 3) as HomeFragmentAdapter
 }

override fun initData() {
     //开启自动刷新
     mDataBinding.refreshLayout.autoRefresh()
     //ViewModel获取数据
     mViewModel.getList(page)
 }

override fun createObserver() {
     //得到数据
     mViewModel.listBean.observe(this, Observer {
         mAdapter.vbLoadData(mDataBinding.refreshLayout,
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
         mAdapter.vbLoadData(mDataBinding.refreshLayout,
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
fun ImageView.vbLoad(
    any: Any,
    roundingRadius: Float = 0f,
    errorResId: Int = R.mipmap.base_iv_default
) = loadDispose(this, any, roundingRadius, errorResId)


/**
 * 加载圆形图片
 * @param any 图片资源Glide所支持的
 * @param errorResId 加载错误占位图
 */
fun ImageView.vbLoadCircle(any: Any, errorResId: Int = R.mipmap.base_iv_default) =
    loadDispose(this, any, -1f, errorResId)

```

- **代码使用**
```
imageView.vbLoadCircle(file)
imageView.vbLoad(file, 10f)
```

- **xml里面使用**
```
<ImageView
      android:layout_width="match_parent"
      android:scaleType="centerCrop"
      app:vb_img_url="@{bean.url}"
      app:vb_img_radius="@{10f}"
      app:vb_circle="@{true}"
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
                mDataBinding.userBean = it
            })
```




