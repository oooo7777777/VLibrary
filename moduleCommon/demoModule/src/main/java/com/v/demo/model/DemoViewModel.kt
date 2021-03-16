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


    fun imgC(file: File, success: (Bitmap) -> Unit) {
        scopeAsy({ getBitmapFromFile(file, 800, 800) }, success = {
            "成功了".logD()
            success(it!!)
        }, dialog = true)
    }


    private fun getBitmapFromFile(dst: File, width: Int, height: Int): Bitmap? {
        if (null != dst && dst.exists()) {
            var opts: BitmapFactory.Options? = null
            if (width > 0 && height > 0) {
                opts = BitmapFactory.Options()
                opts.inJustDecodeBounds = true
                BitmapFactory.decodeFile(dst.path, opts)
                val minSideLength = Math.min(width, height)
                opts.inSampleSize = computeSampleSize(
                    opts, minSideLength,
                    width * height
                )
                opts.inJustDecodeBounds = false
                opts.inInputShareable = true
                opts.inPurgeable = true
            }
            try {
                return BitmapFactory.decodeFile(dst.path, opts)
            } catch (e: OutOfMemoryError) {
                e.printStackTrace()
            }
        }
        return null
    }

    private fun computeSampleSize(
        options: BitmapFactory.Options,
        minSideLength: Int, maxNumOfPixels: Int
    ): Int {
        val initialSize: Int = computeInitialSampleSize(
            options, minSideLength,
            maxNumOfPixels
        )
        var roundedSize: Int
        if (initialSize <= 8) {
            roundedSize = 1
            while (roundedSize < initialSize) {
                roundedSize = roundedSize shl 1
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8
        }
        return roundedSize
    }

    private fun computeInitialSampleSize(
        options: BitmapFactory.Options,
        minSideLength: Int, maxNumOfPixels: Int
    ): Int {
        val w = options.outWidth.toDouble()
        val h = options.outHeight.toDouble()
        val lowerBound =
            if (maxNumOfPixels == -1) 1 else Math.ceil(Math.sqrt(w * h / maxNumOfPixels))
                .toInt()
        val upperBound = if (minSideLength == -1) 128 else Math.min(
            Math.floor(w / minSideLength),
            Math.floor(h / minSideLength)
        ).toInt()
        if (upperBound < lowerBound) {
            return lowerBound
        }
        return if (maxNumOfPixels == -1
            && minSideLength == -1
        ) {
            1
        } else if (minSideLength == -1) {
            lowerBound
        } else {
            upperBound
        }
    }
}