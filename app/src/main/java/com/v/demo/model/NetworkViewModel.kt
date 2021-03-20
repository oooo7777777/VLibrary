package com.v.demo.model

import androidx.lifecycle.MutableLiveData
import com.v.base.BaseViewModel
import com.v.base.apiBase
import com.v.base.utils.toJson
import com.v.demo.net.apiService
import kotlinx.coroutines.delay

/**
 * author  :
 * desc    :  网络请求演示
 * time    : 2021-03-20 11:47:48
 */
class NetworkViewModel : BaseViewModel() {

    var string = MutableLiveData<String>()

    //自定义api 网络请求 获取去壳数据
    fun getCustom() {

        request({
            //协程请求体
            apiService.getBanner()
        }, success = {
            //成功数据
            string.value = it.toJson()
        }, error = {
            //失败数据
        }, dialog = true)//是否显示加载框
    }

    //自定义api 网络请求 获取原始数据
    fun getCustomDefault() {

        requestDefault({
            apiService.getBanner()
        }, success = {
            string.value = it.toJson()
        }, dialog = true)
    }


    //使用VLibrary库 网络请求
    fun getBase() {

        requestDefault({
            apiBase.get("banners")
        }, success = {
            string.value = it.toString()
        }, dialog = true, error = {
            string.value = it.toString()
        })

    }


    //使用协程加载数据
    fun demoAsync(success: (String) -> Unit) {
        scopeAsync({
            delay(1000)
            getData()
        }, success = {
            success(it!!)
        }, dialog = true)
    }

    private fun getData(): String {
        return "使用协程加载数据"
    }
}