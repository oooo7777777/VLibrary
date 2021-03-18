package com.zlzw.home

import com.v.base.BaseActivity
import android.view.View
import androidx.lifecycle.Observer
import com.v.base.utils.linear
import com.v.base.utils.loadData
import com.zlzw.home.adapter.TestActivityAdapter
import com.zlzw.home.model.TestViewModel
import com.zlzw.home.databinding.HActivityTestBinding
import com.zlzw.home.R

/**
 * author  :
 * desc    :
 * time    : 2021-03-18 16:51:48
 */
class TestActivity : BaseActivity<HActivityTestBinding, TestViewModel>(), View.OnClickListener {

    private var page = 1

    private val mAdapter by lazy {
        mViewBinding.recyclerView.linear(TestActivityAdapter()) as TestActivityAdapter
    }

    override fun toolBarTitle(title: String, titleColor: Int) {
        super.toolBarTitle(this.getString(R.string.h_string_activity_test_title), titleColor)
    }

    override fun initData() {
        mViewBinding.v = this
        mViewBinding.refreshLayout.autoRefresh()
        mViewModel.getList(page)
    }

    override fun createObserver() {
        mViewModel.listBean.observe(this, Observer {
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
                })
        })
    }

    override fun onClick(v: View) {
        when (v.id) {

        }
    }

}