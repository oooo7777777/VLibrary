package com.v.base.utils

import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.*
import com.chad.library.adapter.base.BaseQuickAdapter
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.v.base.R
import com.v.base.VBConfig


/**
 * 线性列表
 */
fun <T : BaseQuickAdapter<*, *>> RecyclerView.vbLinear(
    adapter: T
): T {
    layoutManager = LinearLayoutManager(context)
    (this.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    this.adapter = adapter
    return adapter
}

/**
 * 横向列表
 */
fun <T : BaseQuickAdapter<*, *>> RecyclerView.vbLinearHorizontal(
    adapter: T
): T {
    layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
    (this.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    this.adapter = adapter
    return adapter
}

/**
 * 表格列表
 * @param count 每一列的数据
 */
fun <T : BaseQuickAdapter<*, *>> RecyclerView.vbGrid(
    adapter: T,
    count: Int,
): T {
    layoutManager = GridLayoutManager(context, count)
    (this.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    this.adapter = adapter
    return adapter
}

/**
 * 瀑布流列表
 * @param count 每一列的数据
 */
fun <T : BaseQuickAdapter<*, *>> RecyclerView.vbGridStaggered(
    adapter: T,
    count: Int,
    orientation: Int = StaggeredGridLayoutManager.HORIZONTAL,
): T {
    layoutManager = StaggeredGridLayoutManager(count, orientation)
    (this.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
    this.adapter = adapter
    return adapter
}

/**
 * 自定义LayoutManager
 * LayoutManager
 */
fun <T : BaseQuickAdapter<*, *>> RecyclerView.vbLayoutManager(
    adapter: T,
    layoutManager: RecyclerView.LayoutManager,
): T {
    this.layoutManager = layoutManager
    this.adapter = adapter
    return adapter
}


/**
 * 设置分割线
 */
fun <T : BaseQuickAdapter<*, *>> T.vbDivider(
    block: RecyclerViewItemDecoration.() -> Unit
): T {
    val recyclerView = this.recyclerView

    // 检查是否设置了 LayoutManager
    if (recyclerView.layoutManager == null) {
        throw IllegalStateException("LayoutManager未设置, 请在设置LayoutManager后再调用vbDivider")
    }

    // 应用分割线
    RecyclerViewItemDecoration(recyclerView).apply(block).create()

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
fun <T : BaseQuickAdapter<*, *>> T.vbConfig(
    refreshLayout: SmartRefreshLayout? = null,
    onRefresh: (() -> Unit)? = null,
    onLoadMore: (() -> Unit)? = null,
    onItemClick: ((adapter: T, view: View, position: Int) -> Unit)? = null,
    onItemLongClick: ((adapter: T, view: View, position: Int) -> Unit)? = null,
    onItemChildClick: ((adapter:T, view: View, position: Int) -> Unit)? = null,
    onItemChildLongClick: ((adapter: T, view: View, position: Int) -> Unit)? = null,
    emptyView: View? = null,
    emptyViewClickListener: View.OnClickListener? = null,
    refreshScrollDrag: Boolean = VBConfig.options.refreshScrollDrag
) {


    if (refreshLayout != null) {
        refreshLayout.setEnableOverScrollDrag(refreshScrollDrag)
        if (onRefresh == null) {
            refreshLayout.setEnableRefresh(false)
        } else {
            refreshLayout.setEnableRefresh(true)
            refreshLayout.setOnRefreshListener {
                onRefresh.invoke()
            }
        }

        if (onLoadMore == null) {
            refreshLayout.setEnableLoadMore(false)
        } else {
            refreshLayout.setEnableLoadMore(true)
            refreshLayout.setOnLoadMoreListener {
                onLoadMore.invoke()
            }
        }
    }

    if (onItemClick != null) {
        setOnItemClickListener { adapter, view, position ->
            if (ClickEventUtils.isFastClick) {
                onItemClick.invoke(adapter as T, view, position)
            }
        }
    }


    if (onItemLongClick != null) {
        setOnItemLongClickListener { adapter, view, position ->
            onItemLongClick.invoke(adapter as T, view, position)
            true
        }
    }


    if (onItemChildClick != null) {
        setOnItemChildClickListener { adapter, view, position ->
            if (ClickEventUtils.isFastClick) {
                onItemChildClick.invoke(adapter as T, view, position)
            }
        }
    }

    if (onItemChildLongClick != null) {
        setOnItemChildLongClickListener { adapter, view, position ->
            onItemChildLongClick.invoke(adapter as T, view, position)
            true
        }

    }

    if (emptyView == null) {
        VBConfig.options.recyclerViewEmptyLayout.run {
            setEmptyView(this)
            emptyLayout?.vbOnClickListener {
                emptyViewClickListener?.onClick(it)
            }
        }
    } else {
        setEmptyView(emptyView)
    }
    emptyLayout?.visibility = View.GONE

}


/**
 * 列表数据的加载
 * @param list 数据集合
 * @param mCurrentPageNum 当前分页
 * @param refreshLayout SmartRefreshLayout
 * @param isEmptyViewShow 是否展示空布局
 * @param scrollToTop 加载第一页的时候 recyclerView是否需要滑动到顶部
 * @return 返回下一页的page
 */
fun <T> BaseQuickAdapter<T, *>.vbLoad(
    list: List<T>,
    mCurrentPageNum: Int = 1,
    refreshLayout: SmartRefreshLayout? = null,
    isEmptyViewShow: Boolean = true,
    scrollToTop: Boolean = true
): Int {

    refreshLayout?.finishRefresh()
    refreshLayout?.finishLoadMore()

    if (mCurrentPageNum == 1) {
        setList(list)
        if (scrollToTop) {
            recyclerView.scrollToPosition(0)
        }
        if (hasHeaderLayout() || hasFooterLayout()) {
            removeEmptyView()
        } else {
            if (isEmptyViewShow) {
                emptyLayout?.visibility = View.VISIBLE
            } else {
                emptyLayout?.visibility = View.GONE
            }
        }

    } else {
        addData(list)
    }

    return if (list.isEmpty()) {
        mCurrentPageNum
    } else {
        mCurrentPageNum + 1
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
    msg: String = context.vbGetString(R.string.vb_string_temporarily_no_data),
    listener: View.OnClickListener? = null,
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
    if (msg.isEmpty()) {
        tvEmptyHint.visibility = View.GONE
    } else {
        tvEmptyHint.text = msg
        tvEmptyHint.visibility = View.VISIBLE
    }

    listener?.run {
        view.vbOnClickListener {
            this.onClick(it)
        }
    }

    return view
}

/**
 * RecyclerView动画滑动到指定位置
 * @param  position 先滑动的某个位置
 * @param  selectPosition 在滑动到最终的位置
 */
fun RecyclerView.vbScrollToUp(position: Int = 10, selectPosition: Int = 0) {
    //先滑动到指定item 然后在动画滑动
    if (position != -1) {
        this.scrollToPosition(position)
    }
    val smoothScroller =
        object : LinearSmoothScroller(this.context) {
            override fun getVerticalSnapPreference(): Int {
                return SNAP_TO_START
            }
        }
    smoothScroller.targetPosition = selectPosition//position是item的位置
    this.layoutManager!!.startSmoothScroll(smoothScroller)//通过RecyclerView的layoutManager来实现移动动画效果
}