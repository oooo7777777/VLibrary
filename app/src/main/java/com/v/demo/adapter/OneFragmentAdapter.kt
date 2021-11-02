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
    BaseQuickAdapter<HomeBean.DatasDTO, BaseDataBindingHolder<FragmentOneItemBinding>>(R.layout.fragment_one_item) {

    override fun convert(holder: BaseDataBindingHolder<FragmentOneItemBinding>, item: HomeBean.DatasDTO) {
        holder.dataBinding?.run {
            bean = item
            executePendingBindings()
        }
    }

    fun getImgList(): ArrayList<String> {
        var list = ArrayList<String>()
        data.forEach {
            list.add(it.link)
        }

        return list
    }
}