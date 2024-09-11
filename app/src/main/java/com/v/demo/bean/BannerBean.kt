package com.v.demo.bean

/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/12 16:00
 */
data class BannerBean(
    val `data`: List<Data> = listOf(),
    val errorCode: Int = 0, // 0
    val errorMsg: String = ""
) {
    data class Data(
        val desc: String = "", // 我们支持订阅啦~
        val id: Int = 0, // 30
        val imagePath: String = "", // https://www.wanandroid.com/blogimgs/42da12d8-de56-4439-b40c-eab66c227a4b.png
        val isVisible: Int = 0, // 1
        val order: Int = 0, // 2
        val title: String = "", // 我们支持订阅啦~
        val type: Int = 0, // 0
        val url: String = "" // https://www.wanandroid.com/blog/show/3352
    )
}