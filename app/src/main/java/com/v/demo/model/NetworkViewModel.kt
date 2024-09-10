package com.v.demo.model

import androidx.lifecycle.MutableLiveData
import com.v.base.VBViewModel
import com.v.base.apiBase
import com.v.base.net.vbNetApi
import com.v.base.net.vbRequest
import com.v.base.utils.vbToJson
import com.v.demo.net.Network.Companion.apiService
import kotlinx.coroutines.delay

/**
 * author  :
 * desc    :  网络请求演示
 * time    : 2021-03-20 11:47:48
 */
class NetworkViewModel : VBViewModel() {

    var string = MutableLiveData<String>()

    //自定义api 网络请求 获取去壳数据
    fun getCustom() {
        vbRequest({
            //协程请求体
            apiService.getBanner()
        }, success = {
            //成功数据
            string.value = it?.vbToJson()
        }, error = {
            //失败数据
            string.value = it.errorMsg
        }, code = {
            //code返回 不管成功或者失败
        }, dialog = true//是否显示加载框
        )
    }

    //自定义api 网络请求 获取原始数据
    fun getCustomDefault() {
        vbRequestDefault({
            apiService.getBanner()
        }, success = {
            string.value = it.vbToJson()
        }, dialog = true)
    }

    //使用VLibrary库 网络请求
    fun getBase() {
        //此处返回得数据是泛型,即你传入得是什么类型,得到得就是什么类型
        vbRequest({
            delay(3000)
            vbNetApi.get("article/list/1/json")
        },
            resultState = string,
            dialog = true,
            loadingMsg = "正在加载...",
            error = {
                string.value = it
            })
    }


}