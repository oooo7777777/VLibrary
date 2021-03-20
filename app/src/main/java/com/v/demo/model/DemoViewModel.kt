package com.v.demo.model

import androidx.lifecycle.MutableLiveData
import com.v.base.BaseViewModel
import com.v.demo.bean.BannerBean
import com.v.demo.bean.GirlBean
import com.v.demo.net.apiService
import kotlinx.coroutines.delay

/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/12 16:11
 */
class DemoViewModel : BaseViewModel() {

    var girlBean = MutableLiveData<List<GirlBean>>()
    var bannerBean = MutableLiveData<List<BannerBean>>()

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

    //自定义api 网络请求 得到去壳的数据
    private fun getBanner() {
        request({
            apiService.getBanner()
        }, success = {
            bannerBean.value = it
        })
    }

    //自定义api 得到未去壳数据
    fun getListDefault() {
        requestDefault({
            apiService.getBanner()
        }, success = {

        })
    }




    //获取没有处理过的数据
    fun getDataDefault(page: Int, success: (String) -> Unit) {
//        requestDefault({
//            RetrofitManager.instance.getDefault("Girl/type/Girl/page/$page/count/20")
//        }, success = {
//            success(it.toString())
//        })


    }


    fun demoAsync(success: (String) -> Unit) {
        scopeAsync({
            delay(1000)
            "模拟耗时处理"
        }, success = {
            success(it!!)
        }, dialog = true)
    }


}