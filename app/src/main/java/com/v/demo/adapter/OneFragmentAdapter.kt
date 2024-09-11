package com.v.demo.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.v.demo.R
import com.v.demo.bean.HomeBean
import com.v.demo.databinding.FragmentOneItemBinding

/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/12 16:09
 */
class OneFragmentAdapter :
    BaseQuickAdapter<HomeBean.Data.Datas, BaseDataBindingHolder<FragmentOneItemBinding>>(R.layout.fragment_one_item) {

    override fun convert(holder: BaseDataBindingHolder<FragmentOneItemBinding>, item: HomeBean.Data.Datas) {
        holder.dataBinding?.run {
            bean = item
            executePendingBindings()
        }
    }

}