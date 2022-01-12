package com.v.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.TypeReference
import com.v.base.net.VBAppException
import com.v.base.net.VBExceptionHandle
import com.v.base.bean.VBResponse
import com.v.base.utils.EventLiveData
import com.v.base.utils.vbToBean
import kotlinx.coroutines.*
import java.lang.reflect.Type


abstract class VBViewModel : ViewModel() {

    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }

    inner class UiLoadingChange {
        val showDialog by lazy { EventLiveData<String>() }
        val dismissDialog by lazy { EventLiveData<Boolean>() }
        val netError by lazy { EventLiveData<Boolean>() }
    }

    /**
     * 协程请求(返回处理过的数据)
     * @param block 协程体
     * @param success 成功回调
     * @param error 失败回调
     * @param code 返回的code
     * @param dialog 是否显示请求框
     * @param isToast 网络请求失败 是否显示toast
     */
    fun <T> vbRequest(
        block: suspend () -> VBResponse<T>,
        success: (T) -> Unit,
        error: (VBAppException) -> Unit = {},
        code: (Int) -> Unit = {},
        dialog: Boolean = false,
        isToast: Boolean = true
    ): Job {
        return viewModelScope.launch {
            runCatching {
                if (dialog) {
                    loadingChange.showDialog.postValue("")
                }
                block()
            }.onSuccess {
                loadingChange.dismissDialog.postValue(false)
                code(it.getResponseCode())
                runCatching {
                    executeResponse(it) { t -> success(t) }
                }.onFailure { e ->
                    error(VBExceptionHandle.handleException(e, isToast))
                    loadingChange.netError.postValue(false)

                }
            }.onFailure { e ->
                loadingChange.dismissDialog.postValue(false)
                loadingChange.netError.postValue(false)
                error(VBExceptionHandle.handleException(e, isToast))
            }
        }
    }

    /**
     * 协程请求(返回最原始的数据)
     * @param block 协程体
     * @param success 成功回调
     * @param error 失败回调
     * @param dialog 是否显示请求框
     * @param isToast 网络请求失败 是否显示toast
     */
    fun <T> vbRequestDefault(
        block: suspend () -> T,
        success: (T) -> Unit,
        error: (VBAppException) -> Unit = {},
        dialog: Boolean = false,
        isToast: Boolean = true
    ): Job {
        return viewModelScope.launch {
            runCatching {
                if (dialog) {
                    loadingChange.showDialog.postValue("")
                }
                block()
            }.onSuccess {
                loadingChange.dismissDialog.postValue(false)
                success(it)
            }.onFailure { e ->
                loadingChange.dismissDialog.postValue(false)
                loadingChange.netError.postValue(false)
                error(VBExceptionHandle.handleException(e,isToast))
            }
        }
    }


    /**
     * 协程请求(返回泛型数据)
     * @param block 协程体
     * @param resultState MutableLiveData<T>
     * @param error 失败回调
     * @param dialog 是否显示请求框
     * @param isToast 网络请求失败 是否显示toast
     */
    inline fun <reified T> vbRequest(
        crossinline block: suspend CoroutineScope.() -> Any,
        resultState: MutableLiveData<T>,
        crossinline error: (VBAppException) -> Unit = {},
        dialog: Boolean = false,
        isToast: Boolean = true
    ): Job {
        return viewModelScope.launch {
            runCatching {
                if (dialog) {
                    loadingChange.showDialog.postValue("")
                }
                block()
            }.onSuccess {
                loadingChange.dismissDialog.postValue(false)
                runCatching {
                    val type: Type = object : TypeReference<T>() {}.type
                    resultState.postValue(it.toString().vbToBean(type) as T)
                }.onFailure { e ->
                    error(VBExceptionHandle.handleException(e,isToast))
                    loadingChange.netError.postValue(false)

                }
            }.onFailure { e ->
                loadingChange.dismissDialog.postValue(false)
                loadingChange.netError.postValue(false)
                error(VBExceptionHandle.handleException(e,isToast))
            }
        }
    }


    /**
     * 原始的数据处理
     */
    suspend fun <T> executeResponse(
        response: VBResponse<T>,
        success: suspend CoroutineScope.(T) -> Unit
    ) {
        coroutineScope {
            if (!response.isSuccess()) {
                throw VBAppException(
                    response.getResponseCode(),
                    response.getResponseMsg()
                )
            } else {
                success(response.getResponseData())
            }
        }
    }


    /**
     *  调用协程
     * @param block 操作耗时操作任务
     * @param success 成功回调
     * @param error 失败回调 可不给
     */
    fun <T> vbScopeAsync(
        block: suspend () -> T,
        success: (T) -> Unit,
        error: (Throwable) -> Unit = {},
        dialog: Boolean = false
    ) {
        viewModelScope.launch {
            runCatching {
                if (dialog) {
                    loadingChange.showDialog.postValue("")
                }
                withContext(Dispatchers.IO) {
                    block()
                }
            }.onSuccess {
                loadingChange.dismissDialog.postValue(false)
                success(it)
            }.onFailure {
                loadingChange.dismissDialog.postValue(false)
                loadingChange.netError.postValue(false)
                error(it)
            }
        }
    }


}




