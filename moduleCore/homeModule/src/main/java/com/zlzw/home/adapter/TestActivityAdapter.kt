package com.zlzw.home.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.zlzw.home.R
import com.zlzw.home.bean.TestBean
import com.zlzw.home.databinding.HItemActivityTestBinding

/**
 * author  :
 * desc    :
 * time    : 2021-03-18 16:51:48
 */
class TestActivityAdapter :
    BaseQuickAdapter<TestBean, BaseDataBindingHolder<HItemActivityTestBinding>>(R.layout.h_item_activity_test) {

    override fun convert(holder: BaseDataBindingHolder<HItemActivityTestBinding>, item: TestBean) {
        holder.dataBinding?.run {
            bean = item
            executePendingBindings()
        }

    }

}