package com.v.base.utils

import android.app.Activity
import android.app.Application
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
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.v.base.BaseApplication
import java.util.*


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
    viewModelClass: Class<T>
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
 * 获取string 资源
 */
fun Context.getString(@StringRes id: Int): String = run {
    return resources.getString(id)
}

/**
 * 获取color资源
 */
fun Context.getColor(@ColorRes id: Int): Int = run {
    return ContextCompat.getColor(this, id)
}


/**
 * 获取LayoutView
 */
fun Context.getLayoutView(@LayoutRes id: Int, @Nullable root: ViewGroup? = null): View =
    run {
        return LayoutInflater.from(this).inflate(id, root)
    }


/**
 * 重设 view 的宽高
 */
fun View.setViewLayoutParams(
    w: Int = ViewGroup.LayoutParams.MATCH_PARENT,
    h: Int = ViewGroup.LayoutParams.WRAP_CONTENT
) = run {
    var lp = this.layoutParams
    lp.width = w
    lp.height = h
    this.layoutParams = lp

}

/**
 * 获取ViewBinding
 */
fun <VB : ViewDataBinding> Context.getViewBinding(
    @LayoutRes id: Int,
    @Nullable root: ViewGroup? = null
): VB =
    run {
        return DataBindingUtil.bind(this.getLayoutView(id))!!
    }


/**
 * 点击动画效果
 */
fun View.onClickAnimator(clickTime: Long = 500L, onClick: ((v: View) -> Unit)) = run {
    ViewClickAnimatorUtil(this, clickTime, onClick)
    this
}


/**
 * 判断应用是否存在
 */
fun String.checkBrowser(context: Context): Boolean =
    run {
        if (this == null || "" == this)
            return false
        return try {
            val pm = context.packageManager
            val info = pm.getApplicationInfo(this, PackageManager.GET_META_DATA)
            true
        } catch (e: PackageManager.NameNotFoundException) {
            false
        }

    }


/**
 * 获取渠道名称
 */
fun Context.getChannelName(): String = run {
    var channelName: String = " "
    var appInfo: ApplicationInfo? = null
    try {
        appInfo = this.packageManager
            .getApplicationInfo(
                this.packageName,
                PackageManager.GET_META_DATA
            )
        channelName = appInfo!!.metaData.getString("UMENG_CHANNEL").toString()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    channelName
}

/**
 * 获取App版本码
 */
fun Context.getAppVersionCode(packageName: String = this.packageName): Int {
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
 * 获取App版本号
 * @return App版本号
 */
fun Context.getAppVersionName(packageName: String = this.packageName): String = run {
    val pi = this.packageManager.getPackageInfo(packageName, 0)
    pi?.versionName.toString()
}


/**
 * 获取设备唯一码
 */
fun Context.getDeviceId(): String = run {
    DeviceIdUtil.getDeviceId(this)

}

/**
 * activity跳转
 */
fun Activity.goActivity(cls: Class<*>, bundle: Bundle? = null, requestCode: Int = 0) = run {
    val intent = Intent(this, cls)
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    if (requestCode == 0) {
        startActivity(intent)
    } else {
        this.startActivityForResult(intent, requestCode)
    }
}

/**
 * activity跳转
 */
fun Activity.finish(requestCode: Int = 0, bundle: Bundle? = null) = run {
    val intent = Intent()
    if (bundle != null) {
        intent.putExtras(bundle)
    }
    if (requestCode != 0) {
        this.setResult(requestCode, intent)
    }
    this.finish()
}


/**
 * dp转px
 */
fun Float.dp2px(): Int =
    run {
        val scale = BaseApplication.getContext().resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }

/**
 * dp转px
 */
fun View.dp2px(dp: Int): Int =
    run {
        val scale = BaseApplication.getContext().resources.displayMetrics.density
        return (dp * scale + 0.5f).toInt()
    }


/**
 * 获取屏幕的高度（单位：px
 */
fun Context.getScreenHeight(): Int = run {
    resources.displayMetrics.heightPixels
}


/**
 * 获取屏幕的宽度
 */
fun Context.getScreenWidth(): Int = run {
    resources.displayMetrics.widthPixels
}

/**
 * 获取状态栏高度
 */
fun Context.getStatusBarHeight(): Int = run {
    var result = -1
    val resourceId = this.resources.getIdentifier("status_bar_height", "dimen", "android")
    if (resourceId > 0) {
        result = this.resources.getDimensionPixelSize(resourceId)
    }
    return result
}


/**
 * 随机颜色
 */
val randomColor: Int
    get() {
        var c = 0xffffff
        val random = Random()
        val color = -0x1000000 or random.nextInt(0xffffff)
        c = color
        return c
    }

/**
 * 生成随机数
 */
fun randomNumber(number: Int): Int {
    val random = Random()
    return random.nextInt(number)
}

/**
 * 生成随机数
 */
fun randomNumber(min: Int, max: Int): Int {
    val random = Random()
    return random.nextInt(max) % (max - min + 1) + min
}


/**
 * 手机星号
 */
fun String.phoneNumberFormat(): String = run {
    return this.replace(("(\\d{3})\\d{4}(\\d{4})").toRegex(), "$1****$2")
}

/**
 * toast
 */
fun Any.toast() {
    Toast.makeText(BaseApplication.getContext(), this.toString(), Toast.LENGTH_SHORT).show()
}

/**
 * 复制文本到粘贴板
 */
fun Context.copyToClipboard(text: String) {

    val cm = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager?
    val mClipData = ClipData.newPlainText("Label", text)
    cm!!.setPrimaryClip(mClipData)
    "内容已复制到粘贴板".toast()
}
