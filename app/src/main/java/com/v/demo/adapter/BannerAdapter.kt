package com.v.demo.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.v.demo.R
import com.v.demo.bean.BannerBean
import com.v.demo.databinding.FragmentOneHeaderItemBinding

class BannerAdapter :
    BaseQuickAdapter<BannerBean.Data, BaseDataBindingHolder<FragmentOneHeaderItemBinding>>(R.layout.fragment_one_header_item) {

    override fun convert(holder: BaseDataBindingHolder<FragmentOneHeaderItemBinding>, item: BannerBean.Data) {
        holder.dataBinding?.run {
            bean = item
            executePendingBindings()
        }
    }

}