package com.v.demo

import androidx.lifecycle.Observer
import com.v.base.VBFragment
import com.v.base.utils.*
import com.v.demo.adapter.BannerAdapter
import com.v.demo.adapter.OneFragmentAdapter
import com.v.demo.databinding.FragmentOneBinding
import com.v.demo.viewmodel.OneViewModel

/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/11 15:44
 */
class OneFragment : VBFragment<FragmentOneBinding, OneViewModel>() {


    private var page = 1

    private val mAdapter by lazy {
        mDataBinding.recyclerView.vbLinear(OneFragmentAdapter()).apply {
            vbConfig(
                mDataBinding.refreshLayout,
                onRefresh = {
                    page = 1
                    mViewModel.getList(page)
                },
                onLoadMore = {
                    mViewModel.getList(page)
                },
                onItemClick = { adapter, _, position ->
                    val item = adapter.data[position]
                    item.title.vbToast()
                },
                onItemChildClick = { adapter, view, position ->
                    when (view.id) {

                    }
                },
                emptyViewClickListener = {
                    "点击了全局设置的空布局".vbToast()
                },
                emptyView = vbEmptyView(mContext,
                    res = R.mipmap.bg_wechatimg11,
                    listener = {
                        "点击了自定义空布局".vbToast()
                    })
            )
        }.vbDivider {
            setDivider(10)
            isCludeVisible = true
        }
    }


    private val mAdapterBanner by lazy {
        mDataBinding.recyclerViewHorizontal.vbLinearHorizontal(BannerAdapter()).apply {
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
        }.vbDivider {
            setDivider(10)
            isCludeVisible = true
        }
    }


    override fun initData() {
        mViewModel.getList(page)
    }


    override fun createObserver() {
        mViewModel.homeBean.observe(this, Observer {
            it?.data?.run {
                page = mAdapter.vbLoad(this.datas, page, mDataBinding.refreshLayout)
            }
        })


        mViewModel.bannerBean.observe(this, Observer {
            it.data?.run {
                mAdapterBanner.vbLoad(
                    this,
                    refreshLayout = mDataBinding.refreshLayoutHorizontal
                )
            }
        })
    }

}