package com.v.base.utils.ext

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction

/**
 * @Author : ww
 * desc    :
 * time    : 2020/12/29 14:31
 */

private fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) {
    beginTransaction().func().commit()
}

fun Context.addFragment(fragment: Fragment, frameId: Int, bundle: Bundle? = null) = run {
    if (bundle == null) {
        fragment.arguments = bundle
    }
    if (this is AppCompatActivity) {
        supportFragmentManager.inTransaction { add(frameId, fragment) }
    } else if (this is Fragment) {
        childFragmentManager.inTransaction { add(frameId, fragment) }
    }
}

fun Context.replaceFragment(fragment: Fragment,frameId: Int, bundle: Bundle? = null) =
    run {
        if (bundle == null) {
            fragment.arguments = bundle
        }
        if (this is AppCompatActivity) {
            supportFragmentManager.inTransaction { replace(frameId, fragment) }
        } else if (this is Fragment) {
            childFragmentManager.inTransaction { replace(frameId, fragment) }
        }

    }

fun Context.removeFragment(fragment: Fragment) = run {
    if (this is AppCompatActivity) {
        supportFragmentManager.inTransaction { remove(fragment) }
    } else if (this is Fragment) {
        childFragmentManager.inTransaction { remove(fragment) }
    }
}


fun Context.hideFragment(fragment: Fragment) = run {

    if (this is AppCompatActivity) {
        supportFragmentManager.inTransaction { hide(fragment) }
    } else if (this is Fragment) {
        childFragmentManager.inTransaction { hide(fragment) }
    }
}


fun Context.showFragment(fragment: Fragment) = run {

    if (this is AppCompatActivity) {
        supportFragmentManager.inTransaction { show(fragment) }
    } else if (this is Fragment) {
        childFragmentManager.inTransaction { show(fragment) }
    }
}


fun <T : Fragment> AppCompatActivity.getFragment(
    tag: String,
    clazz: Class<T>,
    bundle: Bundle? = null
): Fragment = run {
    var fragment = supportFragmentManager.findFragmentByTag(tag) as T?
    if (fragment == null) {
        fragment = clazz.newInstance()
    }
    if (bundle != null) {
        fragment.arguments = bundle
    }
    return fragment!!
}


fun <T : Fragment> Fragment.getFragment(
    tag: String,
    clazz: Class<T>,
    bundle: Bundle? = null
): Fragment =
    run {
        var fragment = childFragmentManager.findFragmentByTag(tag) as T
        if (fragment == null) {
            fragment = clazz.newInstance()
        }
        if (bundle != null) {
            fragment.arguments = bundle
        }
        return fragment!!
    }


fun Any.removeAllFragment() = run {
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
