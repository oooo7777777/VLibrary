package com.v.demo

import android.view.View
import androidx.lifecycle.Observer
import com.hitomi.tilibrary.style.index.NumberIndexIndicator
import com.hitomi.tilibrary.transfer.TransferConfig
import com.hitomi.tilibrary.transfer.Transferee
import com.v.base.BaseApplication
import com.v.base.BaseFragment
import com.v.base.utils.*
import com.v.base.annotaion.RecyclerViewItemOrientation
import com.v.demo.adapter.BannerAdapter
import com.v.demo.adapter.OneFragmentAdapter
import com.v.demo.databinding.FragmentOneBinding
import com.v.demo.databinding.FragmentOneHeaderBinding
import com.v.demo.model.DemoViewModel
import com.vansz.glideimageloader.GlideImageLoader

/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/11 15:44
 */
class OneFragment : BaseFragment<FragmentOneBinding, DemoViewModel>() {

    private var page = 1

    private val mAdapter by lazy {
        mViewBinding.recyclerView.divider {
            setDrawable(R.drawable.shape_divider_horizontal)
            orientation = RecyclerViewItemOrientation.GRID
            startVisible = true
            endVisible = true
        }.grid(OneFragmentAdapter(), 4) as OneFragmentAdapter
    }

    private val mAdapterHeaderView by lazy {
        mContext.getViewBinding<FragmentOneHeaderBinding>(R.layout.fragment_one_header)
    }

    private val mTransferee by lazy { Transferee.getDefault(mContext) }

    private fun getTransferConfig(index: Int): TransferConfig {
        return TransferConfig.build()
            .setSourceUrlList(mAdapter.getImgList())
            .setNowThumbnailIndex(index)
            .setIndexIndicator(NumberIndexIndicator())
            .setImageLoader(GlideImageLoader.with(BaseApplication.getContext()))
            .enableScrollingWithPageChange(true) // 是否启动列表随着页面的切换而滚动你的列表，默认关闭
            .bindRecyclerView(
                mViewBinding.recyclerView,
                R.id.ivIcon
            )
    }


    override fun initData() {
        mAdapter.setHeaderView(mAdapterHeaderView.root)
        mViewBinding.refreshLayout.autoRefresh()
        mViewModel.getList(page)
    }


    override fun createObserver() {
        mViewModel.girlBean.observe(this, Observer {
            mAdapter.loadData(mViewBinding.refreshLayout,
                it,
                page,
                onRefresh = {
                    page = 1
                    mViewModel.getList(page)
                },
                onLoadMore = {
                    page = it
                    mViewModel.getList(page)
                },
                onItemClick = { view: View, i: Int ->
                    mTransferee.apply(getTransferConfig(i)).show();
                })
        })


        mViewModel.bannerBean.observe(this, Observer {
            val mBannerAdapter = BannerAdapter(mContext, it)
            mAdapterHeaderView.loopViewPager.adapter = mBannerAdapter
            mAdapterHeaderView.loopViewPager.startAutoScroll()
        })
    }
}