package com.v.demo.adapter

import android.widget.ImageView
import com.v.base.utils.load
import com.v.demo.R
import com.v.demo.bean.BannerBean
import com.v.demo.databinding.FragmentOneHeaderItemBinding
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder


class BannerAdapter :
    BaseBannerAdapter<BannerBean>() {
    private var listener: ItemClickListener? = null


    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    interface ItemClickListener {
        fun onClick(position: Int)
    }

    override fun bindData(
        holder: BaseViewHolder<BannerBean>,
        data: BannerBean,
        position: Int,
        pageSize: Int
    ) {
        holder.findViewById<ImageView>(R.id.ivIcon).load(data.image, 10F)
    }

    override fun getLayoutId(viewType: Int) = R.layout.fragment_one_header_item


}
