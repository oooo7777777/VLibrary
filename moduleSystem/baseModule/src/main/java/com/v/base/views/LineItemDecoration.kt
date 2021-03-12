package com.v.base.views

import android.graphics.Rect
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.v.base.utils.ext.dp2px

class LineItemDecoration : RecyclerView.ItemDecoration {
    private var type: Type? = null//分割线类型
    private var dividerHeight = 2//分割线尺寸
    private var dividerHeightLeft = 2//左边分割线尺寸

    enum class Type {
        VERTICAL, VERTICAL_TOP, VERTICAL_BOTTOM_NO, HORIZONTAL, GRID_HEAD, ALL, ALL_LEFT_RIGHT_NO
    }

    constructor(type: Type, dividerHeight: Float) {
        this.type = type
        this.dividerHeight = dividerHeight.dp2px()
    }

    constructor(type: Type, dividerHeight: Float, dividerHeightLeft: Float) {
        this.type = type
        this.dividerHeight = dividerHeight.dp2px()
        this.dividerHeightLeft = dividerHeight.dp2px()
    }


    override fun getItemOffsets(outRect: Rect, itemPosition: Int, parent: RecyclerView) {
        val spanCount = getSpanCount(parent)//每一行的子元素数量
        val childCount = parent.adapter!!.itemCount//总子元素
        val lineCount = childCount / spanCount//行数

        when (type) {
            Type.ALL -> if (itemPosition % spanCount == 0)
            //第一个
            {
                if (isLastRow(parent, itemPosition, spanCount, childCount))
                //判断是不是最后一行
                {
                    setMargin(
                        itemPosition,
                        outRect,
                        dividerHeight,
                        dividerHeight,
                        dividerHeight,
                        dividerHeight / 2
                    )
                } else {
                    setMargin(
                        itemPosition,
                        outRect,
                        dividerHeight,
                        0,
                        dividerHeight,
                        dividerHeight / 2
                    )
                }

            } else if (itemPosition % spanCount == spanCount - 1)
            //最后一个
            {

                if (isLastRow(parent, itemPosition, spanCount, childCount))
                //判断是不是最后一行
                {
                    setMargin(
                        itemPosition,
                        outRect,
                        dividerHeight,
                        dividerHeight,
                        dividerHeight / 2,
                        dividerHeight
                    )
                } else {
                    setMargin(
                        itemPosition,
                        outRect,
                        dividerHeight,
                        0,
                        dividerHeight / 2,
                        dividerHeight
                    )
                }
            } else
            //中间
            {
                if (isLastRow(parent, itemPosition, spanCount, childCount))
                //判断是不是最后一行
                {
                    setMargin(
                        itemPosition,
                        outRect,
                        dividerHeight,
                        dividerHeight,
                        dividerHeight / 2,
                        dividerHeight / 2
                    )
                } else {
                    setMargin(
                        itemPosition,
                        outRect,
                        dividerHeight,
                        0,
                        dividerHeight / 2,
                        dividerHeight / 2
                    )
                }
            }
            Type.ALL_LEFT_RIGHT_NO -> if (itemPosition % spanCount == 0)
            //第一个
            {
                if (isLastRow(parent, itemPosition, spanCount, childCount))
                //判断是不是最后一行
                {
                    setMargin(itemPosition, outRect, 0, dividerHeight, 0, dividerHeight / 2)
                } else {
                    setMargin(itemPosition, outRect, 0, dividerHeight, 0, dividerHeight / 2)
                }

            } else if (itemPosition % spanCount == spanCount - 1)
            //最后一个
            {

                if (isLastRow(parent, itemPosition, spanCount, childCount))
                //判断是不是最后一行
                {
                    setMargin(
                        itemPosition,
                        outRect,
                        dividerHeight,
                        dividerHeight,
                        dividerHeight / 2,
                        dividerHeight
                    )
                } else {
                    setMargin(itemPosition, outRect, 0, dividerHeight, dividerHeight / 2, 0)
                }
            } else
            //中间
            {
                if (isLastRow(parent, itemPosition, spanCount, childCount))
                //判断是不是最后一行
                {
                    setMargin(
                        itemPosition,
                        outRect,
                        dividerHeight,
                        dividerHeight,
                        dividerHeight / 2,
                        dividerHeight / 2
                    )
                } else {
                    setMargin(
                        itemPosition,
                        outRect,
                        dividerHeight,
                        0,
                        dividerHeight / 2,
                        dividerHeight / 2
                    )
                }
            }
            Type.VERTICAL -> setMargin(itemPosition, outRect, 0, dividerHeight, 0, 0)
            Type.VERTICAL_TOP -> setMargin(itemPosition, outRect, dividerHeight, 0, 0, 0)
            Type.VERTICAL_BOTTOM_NO -> if (itemPosition == childCount - 1)
            //最后一个
            {
                setMargin(itemPosition, outRect, 0, 0, 0, 0)
            } else {
                setMargin(itemPosition, outRect, 0, dividerHeight, 0, 0)
            }
            Type.HORIZONTAL -> if (itemPosition == 0)
            //第一个
            {
                setMargin(itemPosition, outRect, 0, 0, dividerHeightLeft, 0)
            } else if (itemPosition == childCount - 1)
            //最后一个
            {
                setMargin(itemPosition, outRect, 0, 0, dividerHeight, dividerHeightLeft)
            } else
            //中间
            {
                setMargin(itemPosition, outRect, 0, 0, dividerHeight, 0)
            }

            Type.GRID_HEAD -> {
                if (itemPosition == 0) //第一个
                {
                    setMargin(itemPosition, outRect, 0, dividerHeight, 0, 0)
                } else if (itemPosition == childCount - 1) //最后一个
                {
                    setMargin(
                        itemPosition,
                        outRect,
                        0,
                        dividerHeight,
                        dividerHeight,
                        dividerHeightLeft
                    )
                } else if (itemPosition % spanCount == 0) {
                    setMargin(
                        itemPosition,
                        outRect,
                        0,
                        dividerHeight,
                        dividerHeight / 2,
                        dividerHeight
                    )
                } else {
                    setMargin(
                        itemPosition,
                        outRect,
                        0,
                        dividerHeight,
                        dividerHeight,
                        dividerHeight / 2
                    )
                }
            }
        }
    }


    private fun isLastColumn(
        parent: RecyclerView, pos: Int, spanCount: Int,
        childCount: Int
    ): Boolean {
        var childCount = childCount
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            if ((pos + 1) % spanCount == 0) {// 如果是最后一列，则不需要绘制右边
                return true
            }
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager
                .orientation
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                if ((pos + 1) % spanCount == 0)
                // 如果是最后一列，则不需要绘制右边
                {
                    return true
                }
            } else {
                childCount = childCount - childCount % spanCount
                if (pos >= childCount)
                // 如果是最后一列，则不需要绘制右边
                    return true
            }
        }
        return false
    }

    private fun isLastRow(
        parent: RecyclerView, pos: Int, spanCount: Int,
        childCount: Int
    ): Boolean {
        var childCount = childCount
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            // childCount = childCount - childCount % spanCount;
            val lines =
                if (childCount % spanCount == 0) childCount / spanCount else childCount / spanCount + 1
            return lines == pos / spanCount + 1
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager
                .orientation
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true
            } else {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true
                }
            }
        }
        return false
    }

    private fun isfirstRow(
        parent: RecyclerView, pos: Int, spanCount: Int,
        childCount: Int
    ): Boolean {
        var childCount = childCount
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {
            // childCount = childCount - childCount % spanCount;
            val lines =
                if (childCount % spanCount == 0) childCount / spanCount else childCount / spanCount + 1
            //如是第一行则返回true
            return if (pos / spanCount + 1 == 1) {
                true
            } else {
                false
            }
        } else if (layoutManager is StaggeredGridLayoutManager) {
            val orientation = layoutManager
                .orientation
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount
                // 如果是最后一行，则不需要绘制底部
                if (pos >= childCount)
                    return true
            } else {
                // 如果是最后一行，则不需要绘制底部
                if ((pos + 1) % spanCount == 0) {
                    return true
                }
            }
        }
        return false
    }

    private fun getSpanCount(parent: RecyclerView): Int {
        // 列数
        var spanCount = -1
        val layoutManager = parent.layoutManager
        if (layoutManager is GridLayoutManager) {

            spanCount = layoutManager.spanCount
        } else if (layoutManager is StaggeredGridLayoutManager) {
            spanCount = layoutManager
                .spanCount
        }
        return spanCount
    }


    private fun setMargin(
        itemPosition: Int,
        outRect: Rect,
        top: Int,
        bottom: Int,
        left: Int,
        right: Int
    ) {
        //        if (type == HORIZONTAL)
        //        {
        //            Log.d(TAG, " itemPosition:" + itemPosition + " top:" + top + " bottom:" + bottom + " left:" + left + " right:" + right);
        //        }
        outRect.top = top
        outRect.bottom = bottom
        outRect.left = left
        outRect.right = right
    }
}