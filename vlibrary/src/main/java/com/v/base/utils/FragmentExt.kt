package com.v.base.utils

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.v.log.util.log

/**
 * @Author : ww
 * desc    :
 * time    : 2020/12/29 14:31
 */

private fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commitAllowingStateLoss()
}

private fun Any.getFragmentManager(): FragmentManager? {
    return when (this) {
        is AppCompatActivity ->
            supportFragmentManager

        is Fragment ->
            childFragmentManager

        else ->
            null
    }
}


/**
 * 在fragment的arguments里面添加tag并且返回tag
 */
private fun Fragment.bundleFormat(tag: String = this.javaClass.name, mBundle: Bundle?): String {
    val bundle = Bundle()
    val tagKey = "vbGetFragmentTag"
    var tagV = tag
    this.arguments?.run {
        if (this.containsKey(tagKey)) {
            tagV = this.getString(tagKey).toString()
        }
    }
    bundle.putString(tagKey, tagV)
    mBundle?.run {
        bundle.putAll(this)
    }
    this.arguments = bundle
    return tagV
}

/**
 * 添加Fragment
 * @param fragment Fragment对象
 * @param frameId 需要显示Fragment的layout
 * @param bundle 传值
 */
fun Context.vbAddFragment(fragment: Fragment, frameId: Int, bundle: Bundle? = null) = run {
    val tag = fragment.bundleFormat(mBundle = bundle)
    this.getFragmentManager()?.inTransaction { add(frameId, fragment, tag) }
}

/**
 * 替换Fragment
 * @param fragment Fragment对象
 * @param frameId 需要显示Fragment的layout
 * @param bundle 传值
 */
fun Context.vbReplaceFragment(fragment: Fragment, frameId: Int, bundle: Bundle? = null) =
    run {
        val tag = fragment.bundleFormat(mBundle = bundle)
        this.getFragmentManager()?.inTransaction { replace(frameId, fragment, tag) }
    }

/**
 * 移除Fragment
 * @param fragment Fragment对象
 */
fun Context.vbRemoveFragment(fragment: Fragment) = run {
    this.getFragmentManager()?.inTransaction { remove(fragment) }
}


/**
 * 隐藏Fragment
 * @param fragment Fragment对象
 */
fun Context.vbHideFragment(fragment: Fragment) = run {
    this.getFragmentManager()?.inTransaction { hide(fragment) }
}


/**
 * 显示Fragment
 * @param fragment Fragment对象
 */
fun Context.vbShowFragment(fragment: Fragment) = run {
    this.getFragmentManager()?.inTransaction { show(fragment) }
}


/**
 * Activity 获取Fragment
 * 如果通过tag能获取得到Fragment则返回 否则新建(请确保tag为唯一值)
 * @param tag Fragment tag
 * @param clazz Fragment对象
 * @param bundle 传值
 */
fun <T : Fragment> AppCompatActivity.vbGetFragment(
    tag: String,
    clazz: Class<T>,
    bundle: Bundle? = null,
): Fragment = run {
    val fragment = supportFragmentManager.findFragmentByTag(tag).run {
        this ?: clazz.newInstance()
    }
    fragment.bundleFormat(tag, bundle)
    return fragment
}


/**
 * Fragment  获取Fragment
 * 如果通过tag能获取得到Fragment则返回 否则新建
 * @param tag Fragment tag
 * @param clazz Fragment对象
 * @param bundle 传值
 */
fun <T : Fragment> Fragment.vbGetFragment(
    tag: String,
    clazz: Class<T>,
    bundle: Bundle? = null,
): Fragment =
    run {
        val fragment = childFragmentManager.findFragmentByTag(tag).run {
            this ?: clazz.newInstance()
        }
        fragment.bundleFormat(tag, bundle)
        return fragment
    }


/**
 *清空所有Fragment
 */
fun Any.vbRemoveAllFragment() = run {
    if (this is AppCompatActivity) {
        for (fragment in supportFragmentManager.fragments) {
            supportFragmentManager.inTransaction { remove(fragment) }
        }
    } else if (this is Fragment) {
        for (fragment in childFragmentManager.fragments) {
            childFragmentManager.inTransaction { remove(fragment) }
        }
    }
}

/**
 * 通过tag查询fragment是否存在
 */
fun Any.vbFindFragment(tag: String): Boolean {
    return this.getFragmentManager().let {
        if (it == null) {
            return false
        } else {
            return it.findFragmentByTag(tag) != null
        }
    }
}
