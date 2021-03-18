package com.v.base.utils


import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.v.base.utils.DeviceIdUtil
import java.io.File
import java.util.*



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
