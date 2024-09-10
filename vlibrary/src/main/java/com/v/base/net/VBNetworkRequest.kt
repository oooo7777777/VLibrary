package com.v.base.net

import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONArray
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.TypeReference
import com.v.base.VBViewModel
import com.v.base.bean.VBResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

/**
 * author  : ww
 * desc    :
 * time    : 2024/3/19 11:19
 */

/**
 * 协程请求(返回处理过的数据)
 * @param block 协程体
 * @param success 成功回调
 * @param error 失败回调
 * @param dialog 是否显示请求框
 * @param isToast 网络请求失败 是否显示toast
 */
//fun <T> Any.vbRequest(
//    block: suspend () -> VBResponse<T>,
//    resultState: MutableLiveData<T>? = null,
//    state: (Int) -> Unit = {},
//    success: (T) -> Unit = {},
//    error: (String) -> Unit = {},
//    loadingMsg: String = "",
//    dialog: Boolean = false,
//    showErrorToast: Boolean = true,
//): Job {
//    return when (this) {
//        is VBViewModel -> viewModelScope
//        is AppCompatActivity -> lifecycleScope
//        is Fragment -> lifecycleScope
//        else -> {
//            throw ClassCastException("类型错误,请求只能在ViewModel,AppCompatActivity,Fragment上使用")
//        }
//    }.launch {
//        runCatching {
//            state.invoke(0)
//            if (dialog && this@vbRequest is VBViewModel) {
//                loadingChange.showDialog.postValue(loadingMsg)
//            }
//            withContext(Dispatchers.IO) {
//                val response = block() // 执行网络请求
//                val type: Type = object : TypeReference<T>() {}.type
//                val data = JSON.parseObject(response.toString(), type) as T
//                if (response.isSuccess()) {
//                    data
//                } else {
//                    throw VBAppException(
//                        response.getResponseCode(),
//                        response.getResponseMsg(),
//                        type.toString()
//                    )
//                }
//            }
//        }.onSuccess {data ->
//            withContext(Dispatchers.Main) {
//                if (dialog && this@vbRequest is VBViewModel) {
//                    loadingChange.dismissDialog.postValue(false)
//                }
//                success.invoke(data)
//                resultState?.postValue(data)
//                state.invoke(1)
//            }
//        }.onFailure {
//            withContext(Dispatchers.Main) {
//                error(this@vbRequest, showErrorToast, dialog, it) {
//                    error.invoke(it)
//                    state.invoke(1)
//                }
//            }
//        }
//    }
//}


/**
 * 协程请求(返回最原始的数据,如果对象继承了BaseResponse,则会判断是否成功)
 * @param block 协程体
 * @param state 请求步骤(0开始请求 1请求结束)
 * @param success 成功回调
 * @param error 失败回调
 * @param dialog 是否显示请求框(只有在MrkViewModel才会生效,其他的请通过state来自行做处理)
 * @param showErrorToast 网络请求失败 是否显示toast
 */
inline fun <reified T> Any.vbRequest(
    crossinline block: suspend CoroutineScope.() -> String,
    resultState: MutableLiveData<T>? = null,
    crossinline state: (Int) -> Unit = {},
    crossinline success: (T) -> Unit = {},
    crossinline error: (String) -> Unit = {},
    loadingMsg: String = "",
    dialog: Boolean = false,
    showErrorToast: Boolean = true,
): Job {
    return when (this) {
        is VBViewModel -> viewModelScope
        is AppCompatActivity -> lifecycleScope
        is Fragment -> lifecycleScope
        else -> {
            throw ClassCastException("类型错误,请求只能在ViewModel,AppCompatActivity,Fragment上使用")
        }
    }.launch {
        runCatching {
            state.invoke(0)
            if (dialog && this@vbRequest is VBViewModel) {
                loadingChange.showDialog.postValue(loadingMsg)
            }
            withContext(Dispatchers.IO) {
                val response = block() // 执行网络请求
                val type: Type = object : TypeReference<T>() {}.type
                val data = JSON.parseObject(response, type) as T

                // 判断 type 是否继承自 VBResponse
                val isSubclass = isSubtypeOfBaseResponse(type, VBResponse::class.java)
                if (isSubclass) {
                    val bean = data as VBResponse<*>
                    if (bean.isSuccess()) {
                        data
                    } else {
                        throw VBAppException(
                            bean.getResponseCode(),
                            bean.getResponseMsg(),
                            type.toString()
                        )
                    }
                } else {
                    data
                }
            }
        }.onSuccess { data ->
            withContext(Dispatchers.Main) {
                if (dialog && this@vbRequest is VBViewModel) {
                    loadingChange.dismissDialog.postValue(false)
                }
                success.invoke(data)
                resultState?.postValue(data)
                state.invoke(1)
            }
        }.onFailure {
            withContext(Dispatchers.Main) {
                error(this@vbRequest, showErrorToast, dialog, it) {
                    error.invoke(it)
                    state.invoke(1)
                }
            }
        }
    }

}


/**
 *  调用携程
 * @param block 操作耗时操作任务
 * @param success 成功回调
 * @param error 失败回调 可不给
 */
fun <T> VBViewModel.launch(
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
 * 错误处理
 * @param showErrorToast 是否显示错误日志(在重试的时候会强制设置为false)
 * @param throwable 错误信息
 * @param error 错误回调
 */
fun error(
    any: Any? = null,
    showErrorToast: Boolean,
    dialog: Boolean,
    throwable: Throwable,
    error: (String) -> Unit = {}
) {
    val msg = VBNetworkConfig.options.exceptionHandling.onException(throwable)

    if (any is VBViewModel) {
        if (dialog) {
            any.loadingChange.dismissDialog.postValue(false)
        }
        if (msg.isNotEmpty() && showErrorToast) {
            any.loadingChange.showToast.postValue(msg)
        }
    }

    if (msg.isNotEmpty() && showErrorToast) {
        when (any) {
            is AppCompatActivity -> {
                Toast.makeText(any, msg, Toast.LENGTH_SHORT).show()
            }

            is Fragment -> {
                val context = any.context
                if (context != null) {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            }

            else -> {
                // 处理其他情况或抛出异常
            }
        }
    }

    error.invoke(msg)
}


/**
 * 判断当前类型是否为一个正常的json
 */
fun isJson(string: String?): Boolean {
    try {
        if (string.isNullOrEmpty()) return false
        return when (JSON.parse(string)) {
            is JSONArray, is JSONObject, is Boolean, is String, is Int, is Long, is Double, is Float -> {
                true
            }

            else -> {
                false
            }
        }
    } catch (e: Exception) {
        e.toString()
        e.printStackTrace()
    }
    return false
}


/**
 * 生成默认的类型数据
 */
inline fun <reified T> getType(any: Any?): Any {
    val type = object : TypeReference<T>() {}.type
    return when (type) {
        object : TypeReference<MutableLiveData<Int>>() {}.type,
        object : TypeReference<Int>() {}.type -> {
            any?.toString()?.toInt() ?: 0
        }

        object : TypeReference<MutableLiveData<Long>>() {}.type,
        object : TypeReference<Long>() {}.type -> {
            any?.toString()?.toLong() ?: 0L
        }

        object : TypeReference<MutableLiveData<String>>() {}.type,
        object : TypeReference<String>() {}.type -> {
            any?.toString() ?: ""
        }

        object : TypeReference<MutableLiveData<Boolean>>() {}.type,
        object : TypeReference<Boolean>() {}.type,
        -> {
            any?.toString()?.toBoolean() ?: false
        }

        else -> {
            if (any == null) {
                ""
            } else {
                JSON.parseObject(any.toString(), type)
            }
        }
    }
}


//检查type是否包含baseType
fun isSubtypeOfBaseResponse(type: Type, baseType: Class<*>): Boolean {
    if (type is Class<*>) {
        // 检查是否为具体类，并且是否继承自BaseResponse
        return baseType.isAssignableFrom(type)
    } else if (type is ParameterizedType) {
        // 检查泛型的原始类型是否继承自BaseResponse
        val rawType = type.rawType
        if (rawType is Class<*> && baseType.isAssignableFrom(rawType)) {
            return true
        }
        // 检查泛型参数中是否有继承自BaseResponse的类型
        for (arg in type.actualTypeArguments) {
            if (isSubtypeOfBaseResponse(arg, baseType)) {
                return true
            }
        }
    }
    return false
}