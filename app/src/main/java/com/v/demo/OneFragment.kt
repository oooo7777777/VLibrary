package com.v.demo

import androidx.lifecycle.Observer
import com.v.base.VBFragment
import com.v.base.utils.*
import com.v.demo.adapter.BannerAdapter
import com.v.demo.adapter.OneFragmentAdapter
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
            isCludeVisible = true
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
                    item.title.vbToast()
                },
                onItemChildClick = { _, view, position ->
                    when (view.id) {

                    }
                },
                emptyViewClickListener = {
                    "点击了全局设置的空布局".vbToast()
                },
                emptyView = vbEmptyView(mContext,
                    res = R.mipmap.ic_movie,
                    listener = {
                        "点击了自定义空布局".vbToast()
                    })
            )
        } as OneFragmentAdapter
    }


    private val mAdapterBanner by lazy {
        mDataBinding.recyclerViewHorizontal.vbDivider {
            setDivider(10)
            isCludeVisible = true
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
                }
            )
        } as BannerAdapter
    }


    override fun initData() {
        mViewModel.getList(page)
    }


    override fun createObserver() {
        mViewModel.homeBean.observe(this, Observer {
            it?.data?.let {
                page = mAdapter.vbLoad(it.datas, page, mDataBinding.refreshLayout)
            }
        })


        mViewModel.bannerBean.observe(this, Observer {
            mAdapterBanner.vbLoad(
                it.data!!,
                refreshLayout = mDataBinding.refreshLayoutHorizontal
            )
        })
    }

}