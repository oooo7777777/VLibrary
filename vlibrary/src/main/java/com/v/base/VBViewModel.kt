package com.v.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.TypeReference
import com.kunminx.architecture.ui.callback.UnPeekLiveData
import com.v.base.bean.VBResponse
import com.v.base.net.VBAppException
import com.v.base.net.VBExceptionHandle
import com.v.base.utils.vbToBean
import kotlinx.coroutines.*
import java.lang.reflect.Type


abstract class VBViewModel : ViewModel() {

    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }

    inner class UiLoadingChange {
        val showDialog by lazy { UnPeekLiveData<String>() }
        val dismissDialog by lazy { UnPeekLiveData<Boolean>() }
        val showToast by lazy { UnPeekLiveData<String>() }
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
        success: (T?) -> Unit,
        error: ((VBAppException) -> Unit)? = null,
        code: (Int) -> Unit = {},
        dialog: Boolean = false,
        dialogMsg: String = "",
        isToast: Boolean = true,
    ): Job {
        return viewModelScope.launch {
            runCatching {
                if (dialog)
                    loadingChange.showDialog.postValue(dialogMsg)
                block()
            }.onSuccess {
                if (dialog)
                    loadingChange.dismissDialog.postValue(false)
                code.invoke(it.getResponseCode())
                executeResponse(it) { t -> success.invoke(t) }
            }.onFailure {
                handleException(it, isToast, error)
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
        error: ((VBAppException) -> Unit)? = null,
        dialog: Boolean = false,
        dialogMsg: String = "",
        isToast: Boolean = true,
    ): Job {
        return viewModelScope.launch {
            runCatching {
                if (dialog)
                    loadingChange.showDialog.postValue(dialogMsg)
                block()
            }.onSuccess {
                if (dialog)
                    loadingChange.dismissDialog.postValue(false)
                success.invoke(it)
            }.onFailure {
                handleException(it, isToast, error)
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
        dialogMsg: String = "",
        isToast: Boolean = true,
    ): Job {
        return viewModelScope.launch {
            runCatching {
                if (dialog)
                    loadingChange.showDialog.postValue(dialogMsg)
                block()
            }.onSuccess {
                if (dialog)
                    loadingChange.dismissDialog.postValue(false)
                val type: Type = object : TypeReference<T>() {}.type
                resultState.postValue(it.toString().vbToBean(type) as T)
            }.onFailure {
                val e = VBExceptionHandle.handleException(it)
                error.invoke(e)
                if (isToast && !e.errorMsg.isNullOrEmpty()) {
                    loadingChange.showToast.postValue(e.errorMsg)
                }
                if (dialog)
                    loadingChange.dismissDialog.postValue(false)
            }
        }
    }


    /**
     * 原始的数据处理
     */
    suspend fun <T> executeResponse(
        response: VBResponse<T>,
        success: suspend CoroutineScope.(T?) -> Unit,
    ) {
        coroutineScope {
            if (!response.isSuccess()) {
                throw VBAppException(
                    response.getResponseCode(),
                    response.getResponseMsg()
                )
            } else {
                withContext(Dispatchers.Main) {
                    success(response.getResponseData())
                }
            }
        }
    }

    fun handleException(
        throwable: Throwable,
        isToast: Boolean = false,
        error: ((VBAppException) -> Unit)?,
    ) {
        val e = VBExceptionHandle.handleException(throwable)
        error?.invoke(e)
        if (isToast && !e.errorMsg.isNullOrEmpty()) {
            loadingChange.showToast.postValue(e.errorMsg)
        }
        loadingChange.dismissDialog.postValue(false)
    }

}




