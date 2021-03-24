package com.v.demo.adapter

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseDataBindingHolder
import com.v.demo.R
import com.v.demo.bean.NetworkBean
import com.v.demo.databinding.ItemActivityNetworkBinding

/**
 * author  :
 * desc    :  网络请求演示
 * time    : 2021-03-20 11:47:48
 */
class NetworkActivityAdapter :
    BaseQuickAdapter<NetworkBean, BaseDataBindingHolder<ItemActivityNetworkBinding>>(R.layout.item_activity_network) {

    override fun convert(
        holder: BaseDataBindingHolder<ItemActivityNetworkBinding>,
        item: NetworkBean
    ) {
        holder.dataBinding?.run {
            bean = item
            executePendingBindings()
        }

    }

}