package com.v.base.utils

import android.app.Activity
import android.app.Application
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
import androidx.lifecycle.viewModelScope
import com.hjq.toast.ToastParams
import com.hjq.toast.Toaster
import com.hjq.toast.style.CustomToastStyle
import com.v.base.VBApplication
import com.v.base.VBConfig
import com.v.base.VBViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.Random
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
fun <T : ViewModel> getApplicationViewModel(
    application: Application,
    viewModelClass: Class<T>,
): T {
    if (viewModelMap.containsKey(viewModelClass)) {
        return viewModelMap[viewModelClass] as T
    }
    val t =
        ViewModelProvider.AndroidViewModelFactory.getInstance(application).create(viewModelClass)
    viewModelMap[viewModelClass] = t
    return t
}


/**
 * dp2px
 */
fun Number.vbDp2px2Int(context: Context? = null): Int = run {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(), (context ?: VBApplication.getApplication()).resources.displayMetrics
    ).roundToInt()
}

/**
 * dp2px
 */
fun Number.vbDp2px2Float(context: Context? = null): Float = run {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(), (context ?: VBApplication.getApplication()).resources.displayMetrics
    )
}

/**
 * sp2px
 */
fun Number.vbSp2px2Int(context: Context? = null): Int = run {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(), (context ?: VBApplication.getApplication()).resources.displayMetrics
    ).roundToInt()
}

/**
 * sp2px
 */
fun Number.vbSp2px2Float(context: Context? = null): Float = run {
    return TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_SP,
        this.toFloat(), (context ?: VBApplication.getApplication()).resources.displayMetrics
    )
}

/**
 * px2dp
 */
fun Number.vbPx2dp2Int(context: Context? = null): Int = run {
    val scale: Float = (context ?: VBApplication.getApplication()).resources.displayMetrics.density
    return (this.toFloat() / scale + 0.5f).roundToInt()
}

/**
 * px2dp
 */
fun Number.vbPx2dp2Float(context: Context? = null): Float = run {
    val scale: Float = (context ?: VBApplication.getApplication()).resources.displayMetrics.density
    return (this.toFloat() / scale + 0.5f)
}

/**
 * px2sp
 */
fun Number.vbPx2sp2Int(context: Context? = null): Int = run {
    val fontScale: Float =
        (context ?: VBApplication.getApplication()).resources.displayMetrics.scaledDensity
    return (this.toFloat() / fontScale + 0.5f).roundToInt()
}

/**
 * px2sp
 */
fun Number.vbPx2sp2Float(context: Context? = null): Float = run {
    val fontScale: Float =
        (context ?: VBApplication.getApplication()).resources.displayMetrics.scaledDensity
    return (this.toFloat() / fontScale + 0.5f)
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
 * @param isLong 是否显示长toast
 * @param gravity 重心
 * @param xOffset 偏移x
 * @param yOffset 偏移y
 * @param layoutId 自定义view当次生效
 * @param isCancel 清除toast
 */
fun Any.vbToast(
    isLong: Boolean = false,
    gravity: Int = Gravity.CENTER,
    xOffset: Int = 0,
    yOffset: Int = 0,
    layoutId: Int = 0,
    isCancel: Boolean = false
) {
    if (this.toString().isNullOrEmpty()) {
        return
    }
    if (isCancel) Toaster.cancel()

    Toaster.setGravity(gravity, xOffset, yOffset)
    if (layoutId != 0) {
        Toaster.show(ToastParams().apply {
            this.text = this@vbToast.toString()
            this.style = CustomToastStyle(layoutId)
            this.duration = if (isLong) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
        })
    } else {
        if (isLong) Toaster.showLong(this) else Toaster.showShort(this)
    }
}


/** 根据百分比改变颜色透明度  */
private fun vbChangeAlpha(color: Int, fraction: Float): Int {
    val red = Color.red(color)
    val green = Color.green(color)
    val blue = Color.blue(color)
    val alpha = (Color.alpha(color) * fraction).toInt()
    return Color.argb(alpha, red, green, blue)
}


/**
 * 判断当前颜色视为为白色
 */
fun vbIsWhiteColor(color: Int): Boolean {
    //颜色转换成灰度值
    //灰度值
    val blue = color and 0x000000FF
    val green = color and 0x0000FF00 shr 8
    val red = color and 0x00FF0000 shr 16
    val grey = red * 38 + green * 75 + blue * 15 shr 7
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
    } else {
        throw IllegalStateException("Any 只能是 Context")
    }
}


/**
 * activity finish带返回
 */
fun Activity.vbFinish(requestCode: Int = AppCompatActivity.RESULT_OK, bundle: Bundle? = null) =
    run {
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
    "内容已复制到粘贴板".vbToast()
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
 * 获取view所有子view
 */
fun View.vbGetAllChildViews(): List<View> {
    val list = ArrayList<View>()
    if (this is ViewGroup) {
        for (i in 0 until this.childCount) {
            val viewchild = this.getChildAt(i)
            list.add(viewchild)
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
        p.setMargins(
            left.vbDp2px2Int(),
            top.vbDp2px2Int(),
            right.vbDp2px2Int(),
            bottom.vbDp2px2Int()
        )
        this.requestLayout()
    }
}

/**
 * 获取view的宽度
 */
fun View.vbGetViewWidth(): Int {
    this.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    return this.measuredWidth
}

/**
 * 获取view的高度
 */
fun View.vbGetViewHeight(): Int {
    this.measure(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
    return this.measuredHeight
}

/**
 * 显示view
 */
fun View.vbVisible() {
    visibility = View.VISIBLE
}

/**
 * 隐藏view
 */
fun View.vbGone() {
    visibility = View.GONE
}

/**
 * view不可见
 */
fun View.vbInvisible() {
    visibility = View.INVISIBLE
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
 *  调用携程
 * @param block 操作耗时操作任务
 * @param success 成功回调
 * @param error 失败回调 可不给
 */
fun <T> VBViewModel.vbLaunch(
    block: () -> T,
    success: (T) -> Unit,
    error: (Throwable) -> Unit = {}
) {
    viewModelScope.launch {
        kotlin.runCatching {
            withContext(Dispatchers.IO) {
                block()
            }
        }.onSuccess {
            success(it)
        }.onFailure {
            error(it)
        }
    }
}

/**
 * 保留小数 使用这种写法若小数点后均为零，则保留一位小数
 * @param scale 小数位数
 * @param mode 转换模型  RoundingMode.UP 不遵循四舍五入直接进1  RoundingMode.DOWN不四舍五入直接减1
 * @param mode 转换模型  RoundingMode.HALF_UP 四舍五入  RoundingMode.HALF_DOWN 四舍五入
 */
fun Number.vbFormatDecimal(
    scale: Int = 2,
    mode: RoundingMode = RoundingMode.HALF_UP,
): Double {
    val bd = BigDecimal(this.toDouble())
    return bd.setScale(scale, mode).toDouble()
}

/**
 * 将double格式化为指定小数位的String，不足小数位用0补全
 * @param scale 小数点后保留几位
 * @return
 */
fun Double.vbFormatZero(scale: Int = 2): String {
    if (scale == 0) {
        return DecimalFormat("0").format(this)
    }
    var formatStr = "0."
    for (i in 0 until scale) {
        formatStr += "0"
    }
    return DecimalFormat(formatStr).format(scale)
}



