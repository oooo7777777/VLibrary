package com.v.demo.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import com.v.base.BaseViewModel
import com.v.base.utils.ext.logD
import com.v.base.utils.ext.toList
import com.v.demo.bean.BannerBean
import com.v.demo.bean.OneBean
import com.ww.appmodule.net.RetrofitManager
import kotlinx.coroutines.delay
import java.io.File

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


    //获取没有处理过的数据
    fun getDataDefault(page: Int, success: (String) -> Unit) {
        requestDefault({
            RetrofitManager.instance.getDefault("Girl/type/Girl/page/$page/count/20")
        }, success = {
            success(it.toString())
        })
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