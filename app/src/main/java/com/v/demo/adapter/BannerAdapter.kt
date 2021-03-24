package com.v.demo.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.v.base.utils.getViewBinding
import com.v.demo.R
import com.v.demo.bean.BannerBean
import com.v.demo.databinding.FragmentOneHeaderItemBinding


class BannerAdapter(internal var context: Context, var data: List<BannerBean>?) : PagerAdapter() {
    private var listener: ItemClickListener? = null

    override fun getCount(): Int {
        return data!!.size
    }

    fun setItemClickListener(listener: ItemClickListener) {
        this.listener = listener
    }

    interface ItemClickListener {
        fun onClick(position: Int)
    }


    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var viewBinding =
            context.getViewBinding<FragmentOneHeaderItemBinding>(R.layout.fragment_one_header_item)
        viewBinding.bean = data!![position]
        viewBinding.executePendingBindings()
        container.addView(viewBinding.root)
        return viewBinding.root
    }

    override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
        return arg0 === arg1
    }
}
