package com.v.demo.model

import androidx.lifecycle.MutableLiveData
import com.v.base.BaseApplication.Companion.apiBase
import com.v.base.BaseViewModel
import com.v.demo.bean.BannerBean
import com.v.demo.bean.GirlBean
import com.v.demo.net.ApiResponse
import com.v.demo.net.Network.Companion.apiService


/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/12 16:11
 */
class DemoViewModel : BaseViewModel() {

    var girlBean = MutableLiveData<List<GirlBean>>()

    var bannerBean = MutableLiveData<ApiResponse<List<BannerBean>>>()

    //自定义api 网络请求 得到去壳的数据
    fun getList(page: Int) {
        if (page == 1) {
            getBanner()
        }

        request({
            apiService.getGirlBean(page)
        }, success = {
            girlBean.value = it
        })
    }

    //使用VLibrary库 网络请求
    private fun getBanner() {
        request(
            {
                apiBase.get("banners")
            },
            bannerBean
        )
    }

}