package com.zlzw.home.bean

/**
 * author  :
 * desc    :
 * time    : 2021-03-18 16:51:48
 */
data class TestBean(
    val _id: String,
    val author: String,
    val category: String,
    val createdAt: String,
    val desc: String,
    val images: List<String>,
    val likeCounts: Int,
    val publishedAt: String,
    val stars: Int,
    val title: String,
    val type: String,
    val url: String,
    val views: Int
)