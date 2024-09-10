package com.v.base.utils

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.fondesa.recyclerviewdivider.DividerBuilder
import com.fondesa.recyclerviewdivider.StaggeredDividerBuilder
import com.fondesa.recyclerviewdivider.dividerBuilder
import com.fondesa.recyclerviewdivider.staggeredDividerBuilder

/**
 * 对分割线做一次封装
 * https://github.com/fondesa/recycler-view-divider
 */
class RecyclerViewItemDecoration constructor(
    private val recyclerView: RecyclerView
) {

    //LinearLayoutManager GridLayoutManager 使用
    var dividerBuilder: DividerBuilder? = null

    //StaggeredGridLayoutManager 使用
    var staggeredDividerBuilder: StaggeredDividerBuilder? = null


    init {

        when (recyclerView.layoutManager) {
            is StaggeredGridLayoutManager -> {
                staggeredDividerBuilder = recyclerView.context.staggeredDividerBuilder()
            }

            else -> {
                dividerBuilder = recyclerView.context.dividerBuilder()
            }
        }

    }


    /**
     * 是否设置了分割线的资源 如果没有分割线会设置为透明
     */
    private var isDrawable = false

    /**
     *  显示顶部间隔 [LinearLayoutManager GridLayoutManager]独有
     */
    var isHeadView = false

    /**
     * 显示底部间隔 [LinearLayoutManager GridLayoutManager]独有
     */
    var isEndVisible = false

    /**
     * 统一设置顶部底部间隔 [LinearLayoutManager GridLayoutManager]独有
     */
    var isCludeVisible
        get() = isHeadView && isEndVisible
        set(value) {
            isHeadView = value
            isEndVisible = value
        }

    /**
     *  显示顶部间隔 [LinearLayoutManager GridLayoutManager]独有
     */
    fun showFirstDivider() {
        isHeadView = true
    }

    /**
     * 显示底部间隔 [LinearLayoutManager GridLayoutManager]独有
     */
    fun showLastDivider() {
        isEndVisible = true
    }

    /**
     * 显示顶部 底部间隔[LinearLayoutManager GridLayoutManager]独有
     */
    fun showFirstLastDivider() {
        isCludeVisible = true
    }

    fun setDrawable(drawable: Drawable) {
        isDrawable = true
        dividerBuilder?.drawable(drawable)
    }


    fun setDrawable(@DrawableRes drawableRes: Int) {
        isDrawable = true
        dividerBuilder?.drawableRes(drawableRes)
    }


    fun setColor(@ColorInt color: Int) {
        isDrawable = true
        dividerBuilder?.color(color)
        staggeredDividerBuilder?.color(color)
    }

    fun setColor(color: String) {
        isDrawable = true
        dividerBuilder?.color(Color.parseColor(color))
        staggeredDividerBuilder?.color(Color.parseColor(color))
    }

    fun setColorRes(@ColorRes color: Int) {
        isDrawable = true
        dividerBuilder?.colorRes(color)
        staggeredDividerBuilder?.colorRes(color)
    }

    /**
     * 设置间隔距离
     */
    fun setDivider(size: Int = 1) {
        dividerBuilder?.size(size, TypedValue.COMPLEX_UNIT_DIP)
        staggeredDividerBuilder?.size(size, TypedValue.COMPLEX_UNIT_DIP)
    }


    /**
     * 设置分隔线的间距。[LinearLayoutManager GridLayoutManager]独有
     * @param start - 每个分隔线使用的起始大小。
     * @param  end - 用于每个分隔线的结束大小。
     */
    fun setMargin(start: Int = 0, end: Int = 0) {
        dividerBuilder?.insets(start.vbDp2px2Int(), end.vbDp2px2Int())
    }

    /**
     * 设置第一个Item前面的padding或者最后一个item后面的padding
     * @param start 第一个Item
     * @param end 最后一个Item
     */
    fun setPadding(start: Int = -1, end: Int = -1) {
        if (start != -1) {
            showFirstDivider()
        }
        if (end != -1) {
            showLastDivider()
        }
        dividerBuilder?.offsetProvider { grid, divider, dividerSide, size ->
            val h = size.vbPx2dp2Int()
            if (start != -1 && divider.isFirstDivider) {
                start.vbDp2px2Int() - (h - 5)
            } else if (end != -1 && divider.isLastDivider) {
                end.vbDp2px2Int() - (h - 5)
            } else {
                h + 4 //估计底层写的有点问题,所以这里强行加4 使间隔差不多
            }
        }

    }

    /**
     * 显示侧边分割线 [LinearLayoutManager GridLayoutManager]独有
     *
     */
    fun showSideDividers() {
        dividerBuilder?.showSideDividers()
    }

    /**
     * 隐藏侧边分割线 [StaggeredGridLayoutManager]独有
     */
    fun hideSideDividers() {
        staggeredDividerBuilder?.hideSideDividers()
    }

    fun create() {

        if (isCludeVisible) {
            dividerBuilder?.showFirstDivider()
            dividerBuilder?.showLastDivider()
        } else {
            if (isHeadView) {
                dividerBuilder?.showFirstDivider()
            }
            if (isEndVisible) {
                dividerBuilder?.showLastDivider()
            }
        }
        if (!isDrawable) {
            dividerBuilder?.asSpace()
            staggeredDividerBuilder?.asSpace()
        }

        when (recyclerView.layoutManager) {
            is StaggeredGridLayoutManager -> {
                staggeredDividerBuilder?.build()?.addTo(recyclerView)
            }

            else -> {
                dividerBuilder?.build()?.addTo(recyclerView)
            }
        }

    }

}
