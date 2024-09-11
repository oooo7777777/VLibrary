package com.v.demo.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.v.base.VBViewModel
import com.v.base.net.executeResponse
import com.v.base.net.vbNetApi
import com.v.base.net.vbRequest
import com.v.demo.bean.BannerBean
import com.v.demo.bean.HomeBean
import com.v.demo.net.Network.Companion.apiService
import com.v.demo.net.NetworkApiResponse
import com.v.log.util.log
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

/**
 * author  :
 * desc    :  网络请求演示
 * time    : 2021-03-20 11:47:48
 */
class NetworkViewModel : VBViewModel() {

    var data1 = MutableLiveData<String>()
    var data2 = MutableLiveData<BannerBean>()
    var data3 = MutableLiveData<NetworkApiResponse<ArrayList<BannerBean.Data>>>()
    var data4 = MutableLiveData<BannerBean>()
    var data5 = MutableLiveData<String>()

    //使用VLibrary库
    //传入泛型String
    fun getData1() {
        vbRequest<String>(
            {
                vbNetApi.get("banner/json")
            },
            success = {
                data1.postValue(it)
            },
            error = {
                data1.postValue("getData1请求失败:")
            },
            loadingDialog = true,
            loadingDialogMsg = "正在加载",
            showErrorToast = true
        )
    }

    //使用VLibrary库
    //传入泛型MutableLiveData<HomeBean>
    //如果传入的是MutableLiveData SDK内部会自动调用postValue()
    fun getData2() {
        vbRequest({
            vbNetApi.get("banner/json")
        }, data2, loadingDialog = true)
    }

    //使用VLibrary库 获取成功的数据
    //data3使用了NetworkApiResponse包装,而NetworkApiResponse又继承了BaseResponse,所以网络请求结果会根据NetworkApiResponse里面的isSuccess判断
    fun getData3() {
        vbRequest({
            vbNetApi.get("banner/json")
        }, data3, loadingDialog = true)
    }


    //使用自定义的Api请求
    fun getData4() {
        vbRequest({
            apiService.getBannerBean()
        }, data4, loadingDialog = true)
    }

    //串行请求
    fun getData5() = viewModelScope.launch {



        val result1 = async {
            vbNetApi.get("banner/json")
        }.await().executeResponse<BannerBean>()


        val result2 =
            async {
                vbNetApi.get("banner/json")
            }.await().executeResponse<NetworkApiResponse<ArrayList<BannerBean.Data>>>()


        val result3 =
            async {
                vbNetApi.get("banner/json")
            }.await().executeResponse<String>()


        if (result1 != null && result2 != null && result3 != null) {
            val sb = StringBuffer()
            sb.append(result1)
            sb.append("\n\n")
            sb.append(result2)
            sb.append("\n\n")
            sb.append(result3)
            data5.postValue(sb.toString())
        } else {
            data5.postValue("ViewModel串行请求失败")
        }

    }

}