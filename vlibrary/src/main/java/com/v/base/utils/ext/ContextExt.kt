package com.v.base.utils.ext

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.v.base.VBViewModel
import com.v.base.utils.DeviceIdUtil
import com.v.base.utils.toast
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.reflect.ParameterizedType

/**
 * @Author : ww
 * desc    :
 * time    : 2021/4/26 9:32
 */

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
 * 获取getDataBinding
 */
fun <VB : ViewDataBinding> Context.vbGetDataBinding(
    @LayoutRes id: Int,
    @Nullable root: ViewGroup? = null
): VB =
    run {
        return DataBindingUtil.bind(this.vbGetLayoutView(id))!!
    }


/**
 * 获取渠道名称
 * 默认获取UMENG_CHANNEL
 */
fun Context.vbGetChannelName(channel: String = "UMENG_CHANNEL"): String = run {
    var channelName = " "
    var appInfo: ApplicationInfo? = null
    try {
        appInfo = this.packageManager
            .getApplicationInfo(
                this.packageName,
                PackageManager.GET_META_DATA
            )
        channelName = appInfo!!.metaData.getString(channel).toString()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    channelName
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
 */
fun vbCountDownCoroutines(
    total: Long,
    timeMillis: Long = 1000,
    onTick: (Long) -> Unit,
    onFinish:( () -> Unit)?=null,
    scope: CoroutineScope = GlobalScope
): Job {
    return flow {

        for (i in total downTo 0) {
            emit(i)
            delay(timeMillis)
        }

    }.flowOn(Dispatchers.Default)
        .onCompletion { onFinish?.invoke() }
        .onEach { onTick.invoke(it) }
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

