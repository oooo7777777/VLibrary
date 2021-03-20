package com.v.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.v.base.net.AppException
import com.v.base.net.BaseResponse
import com.v.base.net.ExceptionHandle
import kotlinx.coroutines.*

abstract class BaseViewModel : ViewModel() {

    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }

    inner class UiLoadingChange {
        val showDialog by lazy { EventLiveData<String>() }
        val dismissDialog by lazy { EventLiveData<Boolean>() }
    }


    /**
     * 协程请求(返回处理过的数据)
     * @param block 协程体
     * @param success 成功回调
     * @param error 失败回调
     * @param dialog 是否显示请求框
     */
    fun <T> request(
        block: suspend CoroutineScope.() -> BaseResponse<T>,
        success: (T) -> Unit,
        error: (AppException) -> Unit = {},
        dialog: Boolean = false
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
                    executeResponse(it) { t -> success(t) }
                }.onFailure { e ->
                    error(ExceptionHandle.handleException(e))
                }
            }.onFailure { e ->
                loadingChange.dismissDialog.postValue(false)
                error(ExceptionHandle.handleException(e))
            }
        }
    }


    /**
     * 协程请求(返回最原始的数据)
     * @param block 协程体
     * @param success 成功回调
     * @param error 失败回调
     * @param dialog 是否显示请求框
     */
    fun <T> requestDefault(
        block: suspend () -> T,
        success: (T) -> Unit,
        error: (AppException) -> Unit = {},
        dialog: Boolean = false
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
                    success(it)
                }.onFailure { e ->
                    error(ExceptionHandle.handleException(e))
                }
            }.onFailure { e ->
                loadingChange.dismissDialog.postValue(false)
                error(ExceptionHandle.handleException(e))
            }
        }
    }

    /**
     * 原始的数据处理
     */
    private suspend fun <T> executeResponse(
        response: BaseResponse<T>,
        success: suspend CoroutineScope.(T) -> Unit
    ) {
        coroutineScope {
            if (!response.isSuccess()) {
                throw AppException(
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
    fun <T> scopeAsync(
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
                error(it)
            }
        }
    }

}




