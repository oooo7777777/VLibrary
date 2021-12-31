package com.v.base.utils.ext

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.v.base.R
import com.v.base.utils.RecyclerViewItemDecoration


/**
 * 线性列表
 */
fun RecyclerView.vbLinear(
    adapter: BaseQuickAdapter<*, *>
): BaseQuickAdapter<*, *> {
    layoutManager = LinearLayoutManager(context)
    this.adapter = adapter
    return adapter
}

/**
 * 横向列表
 */
fun RecyclerView.vbLinearHorizontal(
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
fun RecyclerView.vbGrid(
    adapter: BaseQuickAdapter<*, *>,
    count: Int
): BaseQuickAdapter<*, *> {
    layoutManager = GridLayoutManager(context, count)
    this.adapter = adapter
    return adapter
}

/**
 * 瀑布流列表
 * @param count 每一列的数据
 */
fun RecyclerView.vbGridStaggered(
    adapter: BaseQuickAdapter<*, *>,
    count: Int,
    orientation: Int = StaggeredGridLayoutManager.HORIZONTAL
): BaseQuickAdapter<*, *> {
    layoutManager = StaggeredGridLayoutManager(count, orientation)
    this.adapter = adapter
    return adapter
}

/**
 * 瀑布流列表
 * LayoutManager
 */
fun RecyclerView.vbLayoutManager(
    adapter: BaseQuickAdapter<*, *>,
    layoutManager: RecyclerView.LayoutManager
): BaseQuickAdapter<*, *> {
    this.layoutManager = layoutManager
    this.adapter = adapter
    return adapter
}


/**
 * 函数配置分割线
 * 具体配置参数查看[RecyclerViewItemDecoration]
 */
fun RecyclerView.vbDivider(
    block: RecyclerViewItemDecoration.() -> Unit
): RecyclerView {
    val itemDecoration = RecyclerViewItemDecoration(context).apply(block)
    addItemDecoration(itemDecoration)
    return this
}


/**
 * 列表数据的加载
 * @param refreshLayout SmartRefreshLayout
 * @param onRefresh 是否要下拉加载
 * @param onLoadMore 是否要上拉加载
 * @param onItemClick item的点击
 * @param onItemLongClick item的长按
 * @param onItemChildClick itemChild的点击
 * @param onItemChildLongClick itemChild的长按
 */
fun <T> BaseQuickAdapter<T, *>.vbConfig(
    refreshLayout: SmartRefreshLayout? = null,
    onRefresh: (() -> Unit)? = null,
    onLoadMore: (() -> Unit)? = null,
    onItemClick: ((view: View, position: Int) -> Unit)? = null,
    onItemLongClick: ((view: View, position: Int) -> Unit)? = null,
    onItemChildClick: ((view: View, position: Int) -> Unit)? = null,
    onItemChildLongClick: ((view: View, position: Int) -> Unit)? = null

) {


    if (refreshLayout != null) {

        if (onRefresh == null) {
            refreshLayout.setEnableRefresh(false)
        } else {
            refreshLayout.setEnableRefresh(true)
            refreshLayout.setOnRefreshListener {
                onRefresh.invoke()
            }
        }


        if (onLoadMore == null && data.size <= 0) {
            refreshLayout.setEnableLoadMore(false)
        } else {
            refreshLayout.setEnableLoadMore(true)
            refreshLayout.setOnLoadMoreListener {
                onLoadMore!!.invoke()
            }
        }
    }

    if (onItemClick != null) {
        setOnItemClickListener { _, view, position ->
            onItemClick.invoke(view, position)
        }
    }


    if (onItemLongClick != null) {
        setOnItemLongClickListener { _, view, position ->
            onItemLongClick.invoke(view, position)
            true
        }
    }


    if (onItemChildClick != null) {
        setOnItemChildClickListener { _, view, position ->
            onItemChildClick.invoke(view, position)
        }
    }

    if (onItemChildLongClick != null) {
        setOnItemChildLongClickListener { _, view, position ->
            onItemChildLongClick.invoke(view, position)
            true
        }

    }


}


/**
 * 列表数据的加载
 * @param list 数据集合
 * @param mCurrentPageNum 当前分页
 * @param refreshLayout SmartRefreshLayout
 * @param emptyView 数据空布局(当列表有headerLayout或者footerLayout时,不会加载空布局)
 * @param onSuccess 数据设置成功
 */
fun <T> BaseQuickAdapter<T, *>.vbLoad(
    list: List<T>,
    mCurrentPageNum: Int = 1,
    refreshLayout: SmartRefreshLayout? = null,
    emptyView: View? = null,
    onSuccess: ((Int) -> Unit)? = null
) {

    if (refreshLayout != null) {
        if (mCurrentPageNum == 1) {
            refreshLayout.finishRefresh()
        } else {
            refreshLayout.finishLoadMore()
        }
    }

    if (mCurrentPageNum == 1) {
        setNewInstance(list.toMutableList())
        if (list.isNullOrEmpty()) {
            if (headerLayout == null && footerLayout == null) {
                if (emptyView == null) {
                    setEmptyView(R.layout.vb_layout_empty)
                } else {
                    setEmptyView(emptyView)
                }
            }
        }
    } else {
        addData(list)
    }


    onSuccess?.let {
        if (list.isNullOrEmpty()) {
            it.invoke(mCurrentPageNum)
        } else {
            it.invoke(mCurrentPageNum + 1)
        }
    }


}

/**
 * 列表数据空布局
 * @param context Context
 * @param res 空数据图(传0则不显示)
 * @param msg 空数据提示
 * @param listener 空布局点击监听
 */
fun vbEmptyView(
    context: Context,
    res: Int = R.mipmap.vb_iv_data_empty,
    msg: String = "暂无数据",
    listener: View.OnClickListener? = null
): View {

    val view: View = context.vbGetLayoutView(R.layout.vb_layout_empty)
    val ivEmpty = view.findViewById<ImageView>(R.id.ivEmpty)
    val tvEmptyHint = view.findViewById<TextView>(R.id.tvEmptyHint)

    if (res == 0) {
        ivEmpty.run {
            visibility = View.GONE
        }
    } else {
        ivEmpty.run {
            visibility = View.VISIBLE
            setImageResource(res)
        }
    }
    if (msg.isNullOrEmpty()) {
        tvEmptyHint.visibility = View.GONE
    } else {
        tvEmptyHint.text = msg
        tvEmptyHint.visibility = View.VISIBLE
    }

    listener?.run {
        view.setOnClickListener(this)
    }

    return view
}


/**
 * RecyclerView动画滑动到顶部
 */
fun RecyclerView.vbScrollToUp(position: Int = 10) {
    //先滑动到指定item 然后在动画滑动
    this.scrollToPosition(position)
    val smoothScroller =
        object : androidx.recyclerview.widget.LinearSmoothScroller(this.context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
    smoothScroller.targetPosition = 0//position是item的位置
    this.layoutManager!!.startSmoothScroll(smoothScroller)//通过RecyclerView的layoutManager来实现移动动画效果
}