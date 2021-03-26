package com.v.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.alibaba.fastjson.serializer.SerializerFeature
import com.v.base.net.BaseAppException
import com.v.base.net.BaseExceptionHandle
import com.v.base.net.BaseResponse
import com.v.base.utils.EventLiveData
import com.v.base.utils.toBean
import kotlinx.coroutines.*
import java.lang.Exception
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.lang.reflect.WildcardType


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
        error: (BaseAppException) -> Unit = {},
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
                    error(BaseExceptionHandle.handleException(e))
                }
            }.onFailure { e ->
                loadingChange.dismissDialog.postValue(false)
                error(BaseExceptionHandle.handleException(e))
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
        error: (BaseAppException) -> Unit = {},
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
                    error(BaseExceptionHandle.handleException(e))
                }
            }.onFailure { e ->
                loadingChange.dismissDialog.postValue(false)
                error(BaseExceptionHandle.handleException(e))
            }
        }
    }


    /**
     * 协程请求(返回泛型数据)
     * @param block 协程体
     * @param resultState MutableLiveData<T>
     * @param error 失败回调
     * @param dialog 是否显示请求框
     */
    inline fun <reified T> request(
        crossinline block: suspend CoroutineScope.() -> Any,
        resultState: MutableLiveData<T>,
        crossinline  error: (BaseAppException) -> Unit = {},
        dialog: Boolean = false
    ): Job {
        return viewModelScope.launch {
            runCatching {
                if (dialog) {
                    loadingChange.showDialog.postValue("")
                }
                block()
            }.onSuccess {
                val type: Type = object : TypeReference<T>() {}.type
                resultState.postValue(it.toString().toBean(type) as T)
                loadingChange.dismissDialog.postValue(false)
            }.onFailure { e ->
                loadingChange.dismissDialog.postValue(false)
                error(BaseExceptionHandle.handleException(e))
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
                throw BaseAppException(
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




