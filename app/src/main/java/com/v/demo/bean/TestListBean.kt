package com.v.demo.bean

import com.v.base.bean.VBListBean

/**
 * author  : ww
 * desc    :
 * time    : 2023/3/14 13:55
 */
data class TestListBean(val item: String,val code:Int) : VBListBean {
    override val content: String
        get() = item
}