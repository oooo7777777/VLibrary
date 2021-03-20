# VLibrary40

#### 介绍

- 基于MVVM模式组件化集成谷歌官方推荐的JetPack组件库：LiveData、ViewModel、Lifecycle组件
- 使用kotlin语言，添加大量拓展函数，简化代码
- 加入Retrofit网络请求,协程，帮你简化各种操作，让你快速请求网络
- 封装Base类，添加常用工具类

#### 搭配代码模板插件[VLibraryPlugin](https://github.com/oooo7777777/VLibrary40)使用开发效率翻倍 :exclamation:  :exclamation: 
#### 也可以直接下载此项目[TTProject](https://github.com/oooo7777777/TTProject)直接开发,无需任何配置:exclamation:  :exclamation: 

#### APK下载
[APK下载](http://d.zqapps.com/uvpj?release_id=6054588c23389f29bcb025ad)


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
  implementation  'com.vlibrary:VLibrary:Latest'
}
```

- **3. 在app build.gradle中，android 模块下开启ViewDataBinding

```
android {
    ...
   buildFeatures {
        dataBinding = true
    }
}
 
```

- 继承BaseApplication

```
open class MyApplication : BaseApplication() {

    override fun isDebug(): Boolean {
        return true//是否开启日志打印(日志TAG为 PRETTY_LOGGER)
    }

    override fun initData() {


    }

}
```

#### 继承基类

一般我们项目中都会有一套自己定义的符合业务需求的基类 ***BaseActivity/BaseFragment***，所以我们的基类需要**继承本框架的Base类**



- 使用Activity(基类为AppCompatActivity)
```
/**
 * @param MeActivityBinding ViewDataBinding
 * @param BlankViewModel BaseViewModel(如果该页面不使用ViewModel 使用BlankViewModel)
 */
class MeActivity : BaseActivity<MeActivityBinding, BlankViewModel>() {
    
    /**
     * 重写显示Toolbar
     * @param title 标题(默认是空字符串 空字符则表示不显示Toolbar)
     * @param title 标题颜色
     */
    override fun toolBarTitle(title: String, titleColor: Int) {
        super.toolBarTitle(title, titleColor)
    }

    /**
     * 重写Toolbar左边按钮事件
     * @param resId 按钮图片资源(默认箭头)
     * @param listener 按钮事件(默认onBackPressed)
     */
    override fun toolBarLift(resId: Int, listener: View.OnClickListener) {
        super.toolBarLift(resId, listener)
    }

    /**
     * 重写Toolbar右边按钮图片事件
     * @param resId 按钮资源
     * @param listener 按钮事件
     */
    override fun toolBarRight(resId: Int, listener: View.OnClickListener?) {
        super.toolBarRight(resId, listener)
    }

    /**
     * 重写Toolbar右边按钮文字事件
     * @param text 按钮资源
     * @param listener 按钮事件
     */
    override fun toolBarRight(text: String, textColor: Int, listener: View.OnClickListener?) {
        super.toolBarRight(text, textColor, listener)
    }

    override fun initData() {

    }

    override fun createObserver() {
    }
}
```

- 使用Fragment(基类为Fragment)
```
/**
 * @param MeFragmentBinding ViewDataBinding
 * @param MeViewModel BaseViewModel(如果该页面不使用ViewModel 使用BlankViewModel)
 */
class MeFragment : BaseFragment<MeFragmentBinding, MeViewModel>() {

    override fun initData() {

    }
    override fun createObserver() {

    }

    /**
     *懒加载 对用户第一次可见
     */
    override fun onFragmentFirstVisible() {
        super.onFragmentFirstVisible()
    }

    /**
     *懒加载 对用户可见
     */
    override fun onFragmentPause() {
        super.onFragmentPause()
    }
    
    /**
     *懒加载 对用户不可见
     */
    override fun onFragmentResume() {
        super.onFragmentResume()
    }
}
```

- 使用ViewModel
#### 因为每个人的网络请求与拦截器不一样,所以没有封装到库里面,网络请求请自己写,或者可以直接用我demo里面的
```
class DataViewModel : BaseViewModel() {
    
fun getList(page: Int) {
        
        //获取去壳数据(经过BaseResponse处理过的数据)
        request({
            //请求体
            RetrofitManager.instance.get("data/category/Girl/type/Girl/page/$page/count/20")
        }, success = {
            //成功
            listBean.value = it.toString().toList(OneBean::class.java)
        }, error = {
            //失败
        }, dialog = true)//开启加载框


        //获取未去壳数据
        requestDefault({
            RetrofitManager.instance.getDefault("Girl/type/Girl/page/$page/count/20")
        }, success = {
            listBean.value = it.toString().toList(OneBean::class.java)
        })

    }


   fun demoAsync(success: (String) -> Unit) {
        scopeAsync({
            delay(1000)
            "模拟耗时处理"
        }, success = {
            success(it!!)
        }, dialog = true)
    }

    
}
```



- 使用Dialog(基类为DialogFragment)

```
class MainDialog : BaseDialogFragment<DialogMainBinding, BlankViewModel>() {

    /**
     * 设置返回键不退出dialog
     */
    override fun useCancellation(): Boolean {
        return super.useCancellation()
    }

    /**
     * 设置dialog弹出时界面是否变暗
     */
    override fun useDim(): Boolean {
        return super.useDim()
    }

    /**
     * 设置dialog弹出方向 [DialogOrientation]
     * 支持   TOP, BOTTOM, LEFT, RIGHT, CENTRE
     * 添加相对于的弹出动画
     */
    override fun useDirection(): DialogOrientation {
        return super.useDirection()
    }

    override fun initData() {

    }

    override fun createObserver() {

    }

}
```
#### 其他Dialog

- HintDialog(提示弹出框)

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
- ListDialog(列表弹出框)

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

- ImgSelectDialog(图片选择弹出框,此Dialog只是做了简单的使用,如需自定义请自行处理)
  - 可选择拍照,相册
  - 直接拿到选择图片file
  - 通过CoCo[一款小而美的的Android系统相机拍照、系统相册选择、裁剪库](https://github.com/soulqw/CoCo)
  - 图片是否压缩 KLuban[基于Luban算法，重构后实现的图片压缩框架](https://github.com/forJrking/KLuban)

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
- Adapter使用[BaseRecyclerViewAdapterHelper](https://github.com/CymChad/BaseRecyclerViewAdapterHelper)
- 刷新框架使用[SmartRefreshLayout](https://github.com/scwang90/SmartRefreshLayout)

- 表格布局

```
recyclerView.grid(**Adapter(), 4) as **Adapter      
```

- 竖向布局
```
recyclerView.linear(**Adapter()) as **Adapter
```

- 横向布局
```
recyclerView.linearHorizontal(**Adapter()) as **Adapter
```

- 设置分割线(其他用法请自行查看 com.v.base.utils\RecyclerViewItemDecoration)
```
recyclerView.divider {
            setDrawable(R.drawable.dm_divider_horizontal)
            orientation = RecyclerViewItemOrientation.GRID
            startVisible = true
        }
```

#### 整体使用
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


#### 图片加载(使用的Glide [Glide](https://github.com/bumptech/glide))


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

- 代码使用
```
imageView.loadCircle(file)
imageView.load(file, 10f)
```

- xml里面使用
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


#### 此库部分代码参照了[JetpackMvvm](https://github.com/hegaojian/JetpackMvvm)