package com.v.demo

import androidx.lifecycle.Observer
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.horizontal.SmartRefreshHorizontal
import com.scwang.smart.refresh.layout.wrapper.RefreshFooterWrapper
import com.v.base.VBFragment
import com.v.base.utils.*
import com.v.demo.adapter.BannerAdapter
import com.v.demo.adapter.OneFragmentAdapter
import com.v.demo.bean.BannerBean
import com.v.demo.bean.HomeBean
import com.v.demo.databinding.FragmentOneBinding
import com.v.demo.model.DemoViewModel

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
            vbConfig(
                mDataBinding.refreshLayout,
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
                onItemChildClick = { _, view, position ->
                    when (view.id) {

                    }
                },
                emptyViewClickListener = {
                    "点击了全局设置的空布局".toast()
                },
                emptyView = vbEmptyView(mContext,
                    res = R.mipmap.ic_movie,
                    listener = {
                        "点击了自定义空布局".toast()
                    })
            )
        } as OneFragmentAdapter
    }


    private val mAdapterBanner by lazy {
        mDataBinding.recyclerViewHorizontal.vbDivider {
            setDivider(10)
            isStartVisible = true
            isEndVisible = true
        }.vbLinearHorizontal(BannerAdapter()).apply {
            vbConfig(
                mDataBinding.refreshLayoutHorizontal,
                onLoadMore = {

                },
                onItemClick = { _, _, position ->

                },
                onItemChildClick = { _, view, position ->
                    when (view.id) {

                    }
                },
                emptyViewClickListener = {
                    "点击了全局设置的空布局".toast()
                },
                emptyView = vbEmptyView(mContext,
                    res = R.mipmap.ic_movie,
                    listener = {
                        "点击了自定义空布局".toast()
                    })
            )
        } as BannerAdapter
    }


//    private val mViewPager by lazy {
//        (mAdapterHeaderView.bannerViewPager as BannerViewPager<BannerBean>).apply {
//            adapter = BannerAdapter()
//            setLifecycleRegistry(lifecycle)
//            setPageMargin(15.vbDp2px())
//            setRevealWidth(15.vbDp2px())
//            setPageStyle(PageStyle.MULTI_PAGE_OVERLAP)
//            setIndicatorSlideMode(IndicatorSlideMode.WORM)
//        }
//    }


    override fun initData() {
        mAdapter
        mAdapterBanner


        mDataBinding.refreshLayout.autoRefresh()



//        mDataBinding.refreshLayoutHorizontal .setRefreshHeader( ClassicsHeader(context));
//        mDataBinding.refreshLayoutHorizontal.setRefreshFooter( RefreshFooterWrapper( ClassicsFooter(mContext)), -1, -2);

    }


    override fun createObserver() {
        mViewModel.homeBean.observe(this, Observer {
            it?.data?.let {
                page = mAdapter.vbLoad(it.datas, page, mDataBinding.refreshLayout)
            }
        })


        mViewModel.bannerBean.observe(this, Observer {

//                mViewPager.create(this.data)

            page = mAdapterBanner.vbLoad(it.data!!, page, mDataBinding.refreshLayout)

        })
    }

}