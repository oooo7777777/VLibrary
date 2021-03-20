package com.v.base.utils

import android.app.Application
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.Nullable
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
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