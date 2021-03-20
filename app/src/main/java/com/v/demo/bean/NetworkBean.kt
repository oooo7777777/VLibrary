package com.v.demo.bean

/**
 * author  :
 * desc    :  网络请求演示
 * time    : 2021-03-20 11:47:48
 */
data class NetworkBean(
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