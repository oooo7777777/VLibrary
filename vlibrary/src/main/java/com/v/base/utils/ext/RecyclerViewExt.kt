package com.v.base.utils.ext

import android.view.View
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.v.base.R
import com.v.base.annotaion.RecyclerViewItemOrientation
import com.v.base.utils.RecyclerViewItemDecoration
import com.v.base.utils.ext.isInvalidClick


/**
 * 线性列表
 */
fun RecyclerView.linear(
    adapter: BaseQuickAdapter<*, *>
): BaseQuickAdapter<*, *> {
    layoutManager = LinearLayoutManager(context)
    this.adapter = adapter
    return adapter
}

/**
 * 横向列表
 */
fun RecyclerView.linearHorizontal(
    adapter: BaseQuickAdapter<*, *>
): BaseQuickAdapter<*, *> {
    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    this.adapter = adapter
    return adapter
}

/**
 * 表格列表
 * @param count 每一列的数据
 */
fun RecyclerView.grid(
    adapter: BaseQuickAdapter<*, *>,
    count: Int
): BaseQuickAdapter<*, *> {
    layoutManager = GridLayoutManager(context, count)
    this.adapter = adapter
    return adapter
}


/**
 * 函数配置分割线
 * 具体配置参数查看[RecyclerViewItemDecoration]
 */
fun RecyclerView.divider(
    block: RecyclerViewItemDecoration.() -> Unit
): RecyclerView {
    val itemDecoration = RecyclerViewItemDecoration(context).apply(block)
    addItemDecoration(itemDecoration)
    return this
}

/**
 * 指定Drawable资源为分割线, 分割线的间距和宽度应在资源文件中配置
 * @param drawable 描述分割线的drawable
 */
fun RecyclerView.divider(
    @DrawableRes drawable: Int,
    orientation: RecyclerViewItemOrientation = RecyclerViewItemOrientation.VERTICAL
): RecyclerView {
    return divider {
        setDrawable(drawable)
        this.orientation = orientation
    }
}


/**
 * 列表数据的加载
 * @param refreshLayout SmartRefreshLayout
 * @param list 数据集合
 * @param mCurrentPageNum 当前分页的
 * @param onRefresh 是否要下拉加载
 * @param onLoadMore 是否要上拉加载
 * @param onItemClick item的点击
 * @param onItemChildClick itemChild的点击
 * @param emptyView 空布局设置
 */
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
                var page = mCurrentPageNum
                if (list.isNotEmpty()) {
                    page += 1
                }
                invoke(page)
            }
        }

    }

    setOnItemClickListener { _, view, position ->
        onItemClick?.run {
            if (!view.isInvalidClick()) {
                invoke(view, position)
            }
        }
    }


    setOnItemChildClickListener { _, view, position ->
        onItemChildClick?.run {
            if (!view.isInvalidClick()) {
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

