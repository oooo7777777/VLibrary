package com.v.demo

import android.view.View
import androidx.lifecycle.Observer
import com.v.base.VBActivity
import com.v.base.VBFragment
import com.v.base.dialog.VBHintDialog
import com.v.base.utils.ext.*
import com.v.base.utils.toast
import com.v.base.utils.vbDp2px
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
class OneActivity : VBActivity<FragmentOneBinding, DemoViewModel>() {

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
                onItemClick = { view, position ->

                    var item = data[position] as HomeBean.Data
                    item.title.toast()
                },
                onItemLongClick = { view, position ->

                    "onItemLongClickonItemLongClickonItemLongClick".log()
                    VBHintDialog()
                        .setContent("test")
                        .setButtonText("0", "1")
                        .setClickListener { hintDialog, position ->
                            hintDialog.dismiss()
                            "来了老弟来了".log()
                        }.show(mContext)
                }
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

    }


    override fun createObserver() {
        mViewModel.homeBean.observe(this, Observer {

            it?.data?.let {
                mAdapter.vbLoad(it.datas, page, mDataBinding.refreshLayout,
                    emptyView = vbEmptyView(mContext, listener = View.OnClickListener {
                        "点击了空布局".toast()
                    }),
                    onSuccess = { p ->
                        page = p
                    })
            }

        })


        mViewModel.bannerBean.observe(this, Observer {
            it?.apply {
                mViewPager.create(this.data)
            }
        })
    }


}