package com.v.base.net

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONException
import com.alibaba.fastjson.TypeReference
import com.v.base.VBViewModel
import com.v.base.bean.VBResponse
import com.v.base.utils.vbToast
import com.v.log.util.logE
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
 * @param block 协程体
 * @param resultState MutableLiveData<T>
 * @param state 请求步骤(0开始请求 1请求结束)
 * @param success 成功回调
 * @param error 失败回调
 * @param loadingDialogMsg 加载dialog显示的时候的提示语
 * @param loadingDialog loadingDialog只有在继承VBViewModel的类里面起效,其他类请通过state()自行做弹窗处理
 * @param showErrorToast 错误的的提示语是否使用Toast弹出提醒
 */
inline fun <reified T> Any.vbRequest(
    crossinline block: suspend CoroutineScope.() -> Any, // 支持任意返回类型
    resultState: MutableLiveData<T>? = null,
    crossinline state: (Int) -> Unit = {},
    crossinline success: (T?) -> Unit = {},
    crossinline error: (String) -> Unit = {},
    loadingDialogMsg: String = "",
    loadingDialog: Boolean = false,
    showErrorToast: Boolean = true
): Job {
    return when (this) {
        is ViewModel -> viewModelScope
        is AppCompatActivity -> lifecycleScope
        is Fragment -> lifecycleScope
        else -> throw ClassCastException("请求只能在 ViewModel, AppCompatActivity, Fragment 上使用")
    }.launch {

        runCatching {
            state.invoke(0)
            if (loadingDialog) {
                if (this@vbRequest is VBViewModel) {
                    loadingChange.showDialog.postValue(loadingDialogMsg)
                } else {
                    "loadingDialog只有在继承VBViewModel的类里面起效,其他类请通过state()自行做弹窗处理".logE()
                }
            }
            withContext(Dispatchers.IO) {
                val response = block()
                // 处理普通数据或者继承 BaseResponse 的情况
                val result = handleBaseOrRawResponse<T>(response)
                withContext(Dispatchers.Main) {
                    success.invoke(result)
                    resultState?.postValue(result)
                }

            }
        }.onSuccess {
            if (loadingDialog && this@vbRequest is VBViewModel) {
                loadingChange.dismissDialog.postValue(false)
            }
            state.invoke(1)
        }.onFailure {
            withContext(Dispatchers.Main) {
                error(this@vbRequest, showErrorToast, loadingDialog, it) {
                    error.invoke(it)
                    state.invoke(1)
                }
            }
        }
    }
}


/**
 * 处理 BaseResponse 或者非 ApiResponse 类型的返回数据
 */
inline fun <reified T> handleBaseOrRawResponse(response: Any): T {
    val type: Type = object : TypeReference<T>() {}.type//传入的类型
    val responseType: Type = response::class.java

    // 处理直接匹配的情况
    if (responseType == type) {
        return response as T
    }

    // 尝试反序列化为指定类型
    return try {
        // 判断是否是 BaseResponse 类型
        val isSubclass = isSubtypeOfBaseResponse(type, VBResponse::class.java)
        val parsedData = JSON.parseObject(response.toString(), type) as T
        if (isSubclass) {
            val baseResponse = parsedData as VBResponse<*>
            if (baseResponse.isSuccess()) {
                parsedData
            } else {
                throw VBAppException(
                    baseResponse.getResponseCode(),
                    baseResponse.getResponseMsg(),
                    type.toString()
                )
            }
        } else {
            parsedData
        }
    } catch (e: Exception) {
        when (e) {
            is VBAppException -> {
                throw e
            }

            is JSONException -> {
                throw VBAppException(
                    0,
                    "JSON解析类型不匹配",
                    "$type  \n$response \n${Log.getStackTraceString(e)}"
                )
            }

            else -> {
                throw VBAppException(
                    0,
                    e.message,
                    "$type  \n$response \n${Log.getStackTraceString(e)}"
                )
            }
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
    loadingDialog: Boolean,
    throwable: Throwable,
    error: (String) -> Unit = {}
) {
    val msg = VBNetworkConfig.options.exceptionHandling.onException(throwable)
    if (any is VBViewModel) {
        if (loadingDialog) {
            any.loadingChange.dismissDialog.postValue(false)
        }
        if (msg.isNotEmpty() && showErrorToast) {
            any.loadingChange.showToast.postValue(msg)
        }
    }

    if (msg.isNotEmpty() && showErrorToast) {
        msg.vbToast()
    }

    error.invoke(msg)
}


inline fun <reified T> String.executeResponse(
    crossinline error: (String) -> Unit = {},
): T? {
    var result: T? = null
    runCatching {
        result = handleBaseOrRawResponse<T>(this)
    }.onFailure {
        error(null, false, false, it) {
            error.invoke(it)
        }
    }
    return result
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