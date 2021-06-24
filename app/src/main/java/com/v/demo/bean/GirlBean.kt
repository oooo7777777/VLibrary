package com.v.demo.bean

/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/12 16:00
 */

data class GirlBean(
    val _id: String,
    val author: String,
    val category: String,
    val createdAt: String,
    val desc: String,
    val images: List<String>,
    val likeCounts: String,
    val publishedAt: String,
    val stars: String,
    val title: String,
    val type: String,
    val url: String,
    val views: String
)