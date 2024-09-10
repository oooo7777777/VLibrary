package com.v.demo.model

import androidx.lifecycle.MutableLiveData
import com.v.base.VBViewModel
import com.v.base.net.vbNetApi
import com.v.base.net.vbRequest
import com.v.demo.bean.BannerBean
import com.v.demo.bean.HomeBean
import com.v.demo.net.ApiResponse


/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/12 16:11
 */
class DemoViewModel : VBViewModel() {

    var homeBean = MutableLiveData<ApiResponse<HomeBean>>()

    var bannerBean = MutableLiveData<ApiResponse<ArrayList<BannerBean>>>()

    //使用VLibrary库 网络请求
    fun getList(page: Int) {
        if (page == 1) {
            getBanner()
        }
        vbRequest(
            {
                vbNetApi.get("article/list/$page/json")
            },
            homeBean
        )
    }

    //使用VLibrary库 网络请求
    private fun getBanner() {
        vbRequest(
            {
                vbNetApi.get("banner/json")
            },
            success = {

            }
        )
    }


}