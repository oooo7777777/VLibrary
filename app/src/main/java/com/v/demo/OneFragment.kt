package com.v.demo

import androidx.lifecycle.Observer
import com.v.base.VBFragment
import com.v.base.utils.*
import com.v.demo.adapter.BannerAdapter
import com.v.demo.adapter.OneFragmentAdapter
import com.v.demo.bean.BannerBean
import com.v.demo.bean.HomeBean
import com.v.demo.databinding.FragmentOneBinding
import com.v.demo.databinding.FragmentOneHeaderBinding
import com.v.demo.model.DemoViewModel
import com.zhpan.bannerview.BannerViewPager
import com.zhpan.bannerview.constants.PageStyle
import com.zhpan.indicator.enums.IndicatorSlideMode

/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/11 15:44
 */
class OneFragment : VBFragment<FragmentOneBinding, DemoViewModel>() {


    private var page = 1

    private val mAdapter by lazy {
        mDataBinding.recyclerView.vbDivider {
            setDivider(10)
            isStartVisible = true
            isEndVisible = true
        }.vbLinear(OneFragmentAdapter()).apply {
            vbConfig(mDataBinding.refreshLayout,
                onRefresh = {
                    page = 1
                    mViewModel.getList(page)
                },
                onLoadMore = {
                    mViewModel.getList(page)
                },
                onItemClick = { _, _, position ->
                    val item = data[position] as HomeBean.Data
                    item.title.toast()
                },
                onItemLongClick = { _, view, position ->

                },
                emptyViewClickListener =  {
                    "点击了全局设置的空布局".toast()
                },
                emptyView = vbEmptyView(mContext,
                    res = R.mipmap.ic_movie,
                    listener =  {
                        "点击了自定义空布局".toast()
                    })
            )
        } as OneFragmentAdapter
    }


    private val mAdapterHeaderView by lazy {
        mContext.vbGetDataBinding<FragmentOneHeaderBinding>(R.layout.fragment_one_header)
    }


    private val mViewPager by lazy {
        (mAdapterHeaderView.bannerViewPager as BannerViewPager<BannerBean>).apply {
            adapter = BannerAdapter()
            setLifecycleRegistry(lifecycle)
            setPageMargin(15.vbDp2px())
            setRevealWidth(15.vbDp2px())
            setPageStyle(PageStyle.MULTI_PAGE_OVERLAP)
            setIndicatorSlideMode(IndicatorSlideMode.WORM)
        }
    }


    override fun initData() {
        mAdapter.setHeaderView(mAdapterHeaderView.root)
        mDataBinding.refreshLayout.autoRefresh()
        mViewPager.setOnPageClickListener { clickedView, position ->
        }

    }


    override fun createObserver() {
        mViewModel.homeBean.observe(this, Observer {
            it?.data?.let {
                page = mAdapter.vbLoad(it.datas, page, mDataBinding.refreshLayout)
            }
        })


        mViewModel.bannerBean.observe(this, Observer {
            it?.apply {
                mViewPager.create(this.data)
            }
        })
    }

}