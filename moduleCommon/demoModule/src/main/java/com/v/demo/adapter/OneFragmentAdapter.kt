package com.v.demo.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.v.base.utils.load
import com.v.demo.R
import com.v.demo.bean.OneBean
import com.v.demo.databinding.DmFragmentOneItemBinding

/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/12 16:09
 */
class OneFragmentAdapter :
    BaseQuickAdapter<OneBean, BaseDataBindingHolder<DmFragmentOneItemBinding>>(R.layout.dm_fragment_one_item) {

    override fun convert(holder: BaseDataBindingHolder<DmFragmentOneItemBinding>, item: OneBean) {
        holder.dataBinding?.run {
            bean = item
            executePendingBindings()
        }
    }

    fun getImgList(): ArrayList<String> {
        var list = ArrayList<String>()
        data.forEach {
            list.add(it.url)
        }

        return list
    }
}