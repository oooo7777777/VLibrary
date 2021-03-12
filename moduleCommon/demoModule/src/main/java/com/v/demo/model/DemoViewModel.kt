package com.v.demo.model

import androidx.lifecycle.MutableLiveData
import com.v.base.BaseViewModel
import com.v.base.utils.ext.toList
import com.v.demo.bean.BannerBean
import com.v.demo.bean.OneBean
import com.ww.appmodule.net.RetrofitManager

/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/12 16:11
 */
class DemoViewModel : BaseViewModel() {

    var listBean = MutableLiveData<List<OneBean>>()
    var bannerBean = MutableLiveData<List<BannerBean>>()

    fun getList(page: Int) {
        if (page == 1) {
            getBanner()
        }
        request({
            RetrofitManager.instance.get("data/category/Girl/type/Girl/page/$page/count/20")
        }, success = {
            listBean.value = it.toString().toList(OneBean::class.java)
        })
    }

    private fun getBanner() {
        request({
            RetrofitManager.instance.get("banners")
        }, success = {
            bannerBean.value = it.toString().toList(BannerBean::class.java)
        })
    }

    fun updateBanner() {
        var list = ArrayList<BannerBean>()
        bannerBean.value = list
    }


    //获取没有处理过的数据
    fun getDataDefault(page: Int, success: (String) -> Unit) {
        requestDefault({
            RetrofitManager.instance.getDefault("Girl/type/Girl/page/$page/count/20")
        }, success = {
            success(it.toString())
        })
    }
}