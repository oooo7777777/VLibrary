package com.v.demo.bean

/**
 * @Author : ww
 * desc    :
 * time    : 2021/11/1
 */
data class HomeBean(
    val `data`: Data = Data(),
    val errorCode: Int = 0, // 0
    val errorMsg: String = ""
) {
    data class Data(
        val curPage: Int = 0, // 1
        val datas: List<Datas> = listOf(),
        val offset: Int = 0, // 0
        val over: Boolean = false, // false
        val pageCount: Int = 0, // 784
        val size: Int = 0, // 20
        val total: Int = 0 // 15666
    ) {
        data class Datas(
            val adminAdd: Boolean = false, // false
            val apkLink: String = "",
            val audit: Int = 0, // 1
            val author: String = "",
            val canEdit: Boolean = false, // false
            val chapterId: Int = 0, // 502
            val chapterName: String = "", // 自助
            val collect: Boolean = false, // false
            val courseId: Int = 0, // 13
            val desc: String = "",
            val descMd: String = "",
            val envelopePic: String = "",
            val fresh: Boolean = false, // true
            val host: String = "",
            val id: Int = 0, // 28898
            val isAdminAdd: Boolean = false, // false
            val link: String = "", // https://juejin.cn/post/7409954858126393379
            val niceDate: String = "", // 23小时前
            val niceShareDate: String = "", // 23小时前
            val origin: String = "",
            val prefix: String = "",
            val projectLink: String = "",
            val publishTime: Long = 0, // 1725334511000
            val realSuperChapterId: Int = 0, // 493
            val selfVisible: Int = 0, // 0
            val shareDate: Long = 0, // 1725334511000
            val shareUser: String = "", // hp1451193026
            val superChapterId: Int = 0, // 494
            val superChapterName: String = "", // 广场Tab
            val tags: List<Tag> = listOf(),
            val title: String = "", // Android常用设计模式系列：
            val type: Int = 0, // 0
            val userId: Int = 0, // 2058
            val visible: Int = 0, // 1
            val zan: Int = 0 // 0
        ) {
            data class Tag(
                val name: String = "", // 公众号
                val url: String = "" // /wxarticle/list/408/1
            )
        }
    }
}