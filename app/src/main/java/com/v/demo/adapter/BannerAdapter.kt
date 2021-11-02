package com.v.demo.adapter

import android.widget.ImageView
import com.v.base.utils.vbLoad
import com.v.demo.R
import com.v.demo.bean.BannerBean
import com.zhpan.bannerview.BaseBannerAdapter
import com.zhpan.bannerview.BaseViewHolder


class BannerAdapter :
    BaseBannerAdapter<BannerBean>() {

    override fun bindData(
        holder: BaseViewHolder<BannerBean>,
        data: BannerBean,
        position: Int,
        pageSize: Int
    ) {
        holder.findViewById<ImageView>(R.id.ivIcon).vbLoad(data.imagePath, 10F)
    }

    override fun getLayoutId(viewType: Int) = R.layout.fragment_one_header_item


}
