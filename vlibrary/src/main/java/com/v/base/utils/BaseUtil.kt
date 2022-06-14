package com.v.base.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.*
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.v.base.R
import com.v.base.VBApplication
import com.v.base.VBConfig
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.util.*
import kotlin.math.roundToInt


private val viewModelMap: MutableMap<Class<*>, ViewModel?> =
    HashMap()

/**
 * 获取全局唯一的ViewModel
 * 常用与跨页面修改数据（并且需要刷新显示），比如在某个页面对该ViewModel里的MutableLiveData进行了observe，
 * 无论在哪里修改ViewModel里面的MutableLiveData的值，这个页面都会收到通知（页面在活跃状态下马上收到，非活跃状态下将在变为活跃状态的那一刻收到），收到通知后调用onChanged()方法（一般是刷新视图）
 *
 * @param application    本项目所设置使用的application实体
 * @param viewModelClass ViewModel对应的类
 * @param <T>
 * @return
 */
fun <T : ViewModel?> getApplicationViewModel(
    application: Application?,
    viewModelClass: Class<T>,
): T {
    if (viewModelMap.containsKey(viewModelClass)) {
        return viewModelMap[viewModelClass] as T
    }
    val t =
        ViewModelProvider.AndroidViewModelFactory.getInstance(application!!).create(viewModelClass)
    viewModelMap[viewModelClass] = t
    return t
}


/**
 * dp2px
 */
fun Int.vbDp2px(): Int = run {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(), VBApplication.getApplication().resources.displayMetrics
    ).roundToInt()

}

/**
 * sp2px
 */
fun Int.vbSp2px(): Int = run {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(), VBApplication.getApplication().resources.displayMetrics
    ).roundToInt()
}

/**
 * px2dp
 */
fun Int.vbPx2dp(): Int = run {
    val scale: Float = VBApplication.getApplication().resources.displayMetrics.density
    return (this / scale + 0.5f).roundToInt()
}

/**
 * px2sp
 */
fun Int.vbPx2sp(): Int = run {
    val fontScale: Float = VBApplication.getApplication().resources.displayMetrics.scaledDensity
    return (this / fontScale + 0.5).roundToInt()
}

/**
 * 随机颜色
 */
val vbGetRandomColor: Int
    get() {
        var c = 0xffffff
        val random = Random()
        val color = -0x1000000 or random.nextInt(0xffffff)
        c = color
        return c
    }


/**
 * 随机数
 */
fun Int.vbGetRandomNumber(): Int = run {
    val random = Random()
    return random.nextInt(this)
}

/**
 * 生成随机数
 * @param min 最小数字
 * @param max 最大数字
 */
fun vbGetRandomNumber(min: Int, max: Int): Int {
    val random = Random()
    return random.nextInt(max) % (max - min + 1) + min
}


/**
 * toast
 */
fun Any.toast(isLong: Boolean = false, gravity: Int = Gravity.CENTER) {
    val toast = Toast.makeText(VBApplication.getApplication(),
        this.toString(),
        if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT)
    toast.setGravity(gravity, 0, 0);
    toast.show()
}

/**
 * 颜色转换成灰度值
 * @param rgb 颜色
 * @return　灰度值
 */
fun toGrey(rgb: Int): Int {
    val blue = rgb and 0x000000FF
    val green = rgb and 0x0000FF00 shr 8
    val red = rgb and 0x00FF0000 shr 16
    return red * 38 + green * 75 + blue * 15 shr 7
}

/**
 * 判断当前颜色视为为白色
 */
fun isWhiteColor(color: Int): Boolean {
    val grey = toGrey(color)
    return grey > 200
}

/**
 * 获取string 资源
 */
fun Context.vbGetString(@StringRes id: Int): String = run {
    return resources.getString(id)
}

/**
 * 获取color资源
 */
fun Context.vbGetColor(@ColorRes id: Int): Int = run {
    return ContextCompat.getColor(this, id)
}


/**
 * 获取LayoutView
 */
fun Context.vbGetLayoutView(@LayoutRes id: Int, @Nullable root: ViewGroup? = null): View =
    run {
        return LayoutInflater.from(this).inflate(id, root)
    }


/**
 * 获取屏幕的高度（单位：px
 */
fun Context.vbGetScreenHeight(): Int = run {
    resources.displayMetrics.heightPixels
}


/**
 * 获取屏幕的宽度
 */
fun Context.vbGetScreenWidth(): Int = run {
    resources.displayMetrics.widthPixels
}

/**
 * 获取状态栏高度
 */
fun Context.vbGetStatusBarHeight(): Int = run {
    var result = -1
    val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = this.resources.getDimensionPixelSize(resourceId)
    }
    return result
}

/**
 * 设置文字大小
 */
fun TextView.vbSetTextSize(textSize: Float) {
    this.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
}


/**
 * 获取getDataBinding
 */
fun <VB : ViewDataBinding> Context.vbGetDataBinding(
    @LayoutRes id: Int,
    @Nullable root: ViewGroup? = null,
): VB =
    run {
        return DataBindingUtil.bind(this.vbGetLayoutView(id))!!
    }


/**
 * 获取App版本码
 */
fun Context.vbGetAppVersionCode(packageName: String = this.packageName): Int {
    return try {
        val pm = this.packageManager
        val pi = pm.getPackageInfo(packageName, 0)
        pi?.versionCode ?: -1
    } catch (e: PackageManager.NameNotFoundException) {
        e.printStackTrace()
        -1
    }

}


/**
 * 判断应用是否存在
 */
fun Context.vbCheckBrowser(packageName: String): Boolean =
    run {
        if (packageName.isNullOrEmpty()) {
            return false
        }
        return try {
            val pm = this.packageManager
            val info = pm.getApplicationInfo(packageName, PackageManager.GET_META_DATA)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }
    }


/**
 * 获取App版本号
 * @return App版本号
 */
fun Context.vbGetAppVersionName(packageName: String = this.packageName): String = run {
    val pi = this.packageManager.getPackageInfo(packageName, 0)
    pi?.versionName.toString()
}


/**
 * 获取设备唯一码
 */
fun Context.vbGetDeviceId(): String = run {
    DeviceIdUtil.getDeviceId(this)

}

/**
 * activity跳转
 */
fun Any.goActivity(cls: Class<*>, bundle: Bundle? = null, requestCode: Int = 0) = run {

    if (this is Activity) {
        val intent = Intent(this, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        if (requestCode == 0) {
            startActivity(intent)
        } else {
            this.startActivityForResult(intent, requestCode)
        }
    } else if (this is Fragment) {
        val intent = Intent(this.context, cls)
        if (bundle != null) {
            intent.putExtras(bundle)
        }
        if (requestCode == 0) {
            startActivity(intent)
        } else {
            this.startActivityForResult(intent, requestCode)
        }
    }
}


/**
 * activity跳转
 */
fun Activity.finish(requestCode: Int = AppCompatActivity.RESULT_OK, bundle: Bundle? = null) = run {
    val intent = Intent()
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    this.setResult(requestCode, intent)
    this.finish()
}


/**
 * 复制文本到粘贴板
 */
fun Context.vbCopyToClipboard(text: String) = run {

    val cm = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    val mClipData = ClipData.newPlainText("Label", text)
    cm!!.setPrimaryClip(mClipData)
    "内容已复制到粘贴板".toast()
}

/**
 * 倒计时
 * @param total 倒计数
 * @param timeMillis 倒计时间隔
 * @param onStart 倒计时开始
 * @param onTick 倒计时数回调
 * @param onFinish 倒计时完成
 * @param onCancel 倒计时取消
 * @param scope  lifecycleScope
 * @param isTimeStart 倒计数是在间隔时间前减去1 还是在倒计时建构后减去1
 */
fun vbCountDownCoroutines(
    total: Long = Long.MAX_VALUE,
    timeMillis: Long = 1000,
    onStart: (() -> Unit)? = null,
    onTick: ((Long) -> Unit)? = null,
    onFinish: (() -> Unit)? = null,
    onCancel: (() -> Unit)? = null,
    scope: CoroutineScope = GlobalScope,
    isTimeStart: Boolean = true,
): Job {
    var num = -1L
    return flow {
        for (i in total downTo 0) {
            if (isTimeStart) {
                emit(i)
                delay(timeMillis)
            } else {
                delay(timeMillis)
                emit(i)
            }
        }
    }.flowOn(Dispatchers.Default)
        .onStart { onStart?.invoke() }
        .onCompletion {
            //如果结束的时候 倒计时为传入的时间 则表示完成
            if (num == total) {
                onFinish?.invoke()
            } else {
                onCancel?.invoke()
            }
        }
        .onEach {
            num++
            onTick?.invoke(it)
        }
        .flowOn(Dispatchers.Main)
        .launchIn(scope)
}


/**
 * 反射获取类的所有属性
 */
inline fun <reified T : Any> T.vbDescription() = this.javaClass.declaredFields
    .map {
        //注意我们访问的 Kotlin 属性对于 Java 来说是 private 的，getter 是 public 的
        it.isAccessible = true
        "${it.name}: ${it.get(this@vbDescription)}"
    }
    .joinToString(separator = ";")


/**
 * 获取Lifecycle
 */
fun Context.vbLifecycleOwner(): LifecycleOwner? {
    var curContext = this
    var maxDepth = 20
    while (maxDepth-- > 0 && curContext !is LifecycleOwner) {
        curContext = (curContext as ContextWrapper).baseContext
    }
    return if (curContext is LifecycleOwner) {
        curContext
    } else {
        null
    }
}


/**
 * 判断当前是否有网络连接,但是如果该连接的网络无法上网，也会返回true
 * @return true 能上网 false不能上网
 */
@SuppressLint("MissingPermission")
fun Context.vbIsNetConnection(): Boolean {

    try {
        val connectivityManager: ConnectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo: NetworkInfo = connectivityManager.activeNetworkInfo!!
        val connected: Boolean = networkInfo.isConnected
        if (networkInfo != null && connected) {
            return networkInfo.state === NetworkInfo.State.CONNECTED
        }
    } catch (e: java.lang.Exception) {
        e.printStackTrace()
    }


    return false
}


/**
 * 获取view所有子view
 */
fun View.vbGetAllChildViews(): List<View> {
    var list = ArrayList<View>();
    if (this is ViewGroup) {
        for (i in 0 until this.childCount) {
            var viewchild = this.getChildAt(i);
            list.add(viewchild);
            list.addAll(viewchild.vbGetAllChildViews());
        }
    }
    return list
}


/**
 * 重设 view 的宽高
 */
fun View.vbSetViewLayoutParams(
    w: Int = ViewGroup.LayoutParams.MATCH_PARENT,
    h: Int = ViewGroup.LayoutParams.WRAP_CONTENT,
) = run {
    val lp = this.layoutParams
    lp.width = w
    lp.height = h
    this.layoutParams = lp

}


/**
 * 重设 view 的margins
 */
fun View.vbSetViewMargins(
    left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0,
) = run {

    if (this.layoutParams is ViewGroup.MarginLayoutParams) {
        val p = this.layoutParams as ViewGroup.MarginLayoutParams
        p.setMargins(left.vbDp2px(), top.vbDp2px(), right.vbDp2px(), bottom.vbDp2px())
        this.requestLayout()
    }
}

/**
 * 防抖动点击
 */
fun View.vbOnClickListener(
    clickAnimator: Boolean = VBConfig.options.clickAnimator,
    onClick: ((View) -> Unit),
) {
    if (clickAnimator) {
        ViewClickAnimatorUtil(this, onClick)
    } else {
        this.setOnClickListener(ThrottleOnClickListener {
            onClick.invoke(this)
        })
    }
}


/**
 * 点击防抖动
 */
class ThrottleOnClickListener(
    private var onClick: (() -> Unit),
) : View.OnClickListener {

    override fun onClick(v: View?) {
        if (ClickEventUtils.isFastClick) {
            onClick.invoke()
        }
    }


}

object ClickEventUtils {
    // 上次点击时间
    private var mLastTime = 0L

    private val LIMIT_TIME = VBConfig.options.clickTime

    //设置标记号
    val isFastClick: Boolean
        get() {
            //设置标记号
            var flag = false
            val currentTime = Calendar.getInstance().timeInMillis
            if (currentTime - mLastTime >= LIMIT_TIME) {
                mLastTime = currentTime
                // 调用点击方法
                flag = true
            }
            return flag

        }


}