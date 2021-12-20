package com.v.demo.bean

/**
 * @Author : ww
 * desc    :
 * time    : 2021/11/1
 */
data class HomeBean(
    val curPage: Int = 0, // 2
    val datas: List<Data> = listOf(),
    val offset: Int = 0, // 20
    val over: Boolean = false, // false
    val pageCount: Int = 0, // 580
    val size: Int = 0, // 20
    val total: Int = 0 // 11591
) {
    data class Data(
        val apkLink: String = "",
        val audit: Int = 0, // 1
        val author: String = "",
        val canEdit: Boolean = false, // false
        val chapterId: Int = 0, // 99
        val chapterName: String = "", // 具体案例
        val collect: Boolean = false, // false
        val courseId: Int = 0, // 13
        val desc: String = "",
        val descMd: String = "",
        val envelopePic: String = "",
        val fresh: Boolean = false, // false
        val host: String = "",
        val id: Int = 0, // 20841
        val link: String = "", // https://juejin.cn/post/7041729310029250573
        val niceDate: String = "", // 2021-12-15 23:33
        val niceShareDate: String = "", // 2021-12-15 23:30
        val origin: String = "",
        val prefix: String = "",
        val projectLink: String = "",
        val publishTime: Long = 0, // 1639582380000
        val realSuperChapterId: Int = 0, // 37
        val selfVisible: Int = 0, // 0
        val shareDate: Long = 0, // 1639582218000
        val shareUser: String = "", // 鸿洋
        val superChapterId: Int = 0, // 126
        val superChapterName: String = "", // 自定义控件
        val tags: List<Tag> = listOf(),
        val title: String = "", // Android魔术系列：一步步实现百叶窗效果
        val type: Int = 0, // 0
        val userId: Int = 0, // 2
        val visible: Int = 0, // 1
        val zan: Int = 0 // 0
    ) {
        data class Tag(
            val name: String = "", // 公众号
            val url: String = "" // /wxarticle/list/414/1
        )
    }
}
