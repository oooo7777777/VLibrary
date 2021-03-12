package com.v.base.utils.ext

import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.animation.AlphaInAnimation
import com.chad.library.adapter.base.animation.SlideInBottomAnimation
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.v.base.R
import com.v.base.utils.FastClickUtils
import com.v.base.views.LineItemDecoration


fun RecyclerView.linear(
    adapter: BaseQuickAdapter<*, *>,
    interval: Float = 10f
): BaseQuickAdapter<*, *> {
    layoutManager = LinearLayoutManager(context)
    this.addItemDecoration(LineItemDecoration(LineItemDecoration.Type.VERTICAL, interval))
    this.adapter = adapter
    return adapter
}

fun RecyclerView.linearHorizontal(
    adapter: BaseQuickAdapter<*, *>,
    interval: Float = 10f
): BaseQuickAdapter<*, *> {
    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    this.addItemDecoration(LineItemDecoration(LineItemDecoration.Type.HORIZONTAL, interval))
    this.adapter = adapter
    return adapter
}

fun RecyclerView.grid(
    adapter: BaseQuickAdapter<*, *>,
    count: Int,
    interval: Float = 10f,
    isHeader: Boolean = false
): BaseQuickAdapter<*, *> {
    layoutManager = GridLayoutManager(context, count)

    this.addItemDecoration(
        LineItemDecoration(
            if (isHeader) LineItemDecoration.Type.GRID_HEAD else LineItemDecoration.Type.ALL,
            interval
        )
    )
    this.adapter = adapter
    return adapter
}


fun <T> BaseQuickAdapter<T, *>.loadData(
    refreshLayout: SmartRefreshLayout,
    list: List<T>,
    mCurrentPageNum: Int,
    onRefresh: (() -> Unit)? = null,
    onLoadMore: ((Int) -> Unit)? = null,
    onItemClick: ((view: View, position: Int) -> Unit)? = null,
    onItemChildClick: ((view: View, position: Int) -> Unit)? = null,
    emptyView: (() -> Unit)? = null
) {


    refreshLayout.setOnRefreshListener {
        onRefresh?.run {
            invoke()
        }
    }


    if (onLoadMore == null) {
        refreshLayout.setEnableLoadMore(false)
    } else {
        refreshLayout.setEnableLoadMore(true)
        refreshLayout.setOnLoadMoreListener {
            onLoadMore.run {
                var page = mCurrentPageNum + 1
                invoke(page)
            }
        }

    }

    setOnItemClickListener { _, view, position ->
        onItemClick?.run {
            if (!FastClickUtils().isInvalidClick(view)) {
                invoke(view, position)
            }
        }
    }


    setOnItemChildClickListener { _, view, position ->
        onItemChildClick?.run {
            if (!FastClickUtils().isInvalidClick(view)) {
                invoke(view, position)
            }
        }
    }


    if (mCurrentPageNum == 1) {
        setNewInstance(list.toMutableList())
        if (list.isNullOrEmpty()) {
            if (emptyView != null) {
                emptyView.invoke()
            } else if (headerLayout == null && footerLayout == null) {
                setEmptyView(R.layout.base_layout_empty)
            }
        }
        refreshLayout.finishRefresh()
    } else {
        refreshLayout.finishLoadMore()
        addData(list)
    }

}

