package com.v.base.utils

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.v.base.annotaion.VBRecyclerViewItemOrientation
import com.v.base.utils.RecyclerViewItemDecoration.Edge.Companion.computeEdge
import kotlin.math.ceil
import kotlin.math.roundToInt

/**
 * 最强大的分割线工具
 *
 * 1. 分隔图片
 * 2. 分隔颜色
 * 3. 分隔间距
 * 4. 回调函数判断间隔
 * 5. 首尾是否显示分隔线, 可以展 示表格效果
 * 6. 类型池来指定是否显示分割线
 * 7. 支持全部的LayoutManager, 竖向/横向/网格分割线
 * 8. 优于其他框架, 完美支持均布网格分隔物
 * 9. 支持分组条目的分割线
 *
 * @property startVisible 在[GridLayoutManager]/[StaggeredGridLayoutManager]中控制上下是否显示分割线, 在[LinearLayoutManager]中控制顶部是否显示分割线
 * @property endVisible 在[GridLayoutManager]/[StaggeredGridLayoutManager]中控制左右是否显示分割线, 在[LinearLayoutManager]中控制底部是否显示分割线
 * @property orientation 分割线的方向, 仅支持[GridLayoutManager], 其他LayoutManager都是根据其方向自动适应
 * @property typePool 集合内包含的类型才显示分割线
 */
class RecyclerViewItemDecoration constructor(private val context: Context) :
    RecyclerView.ItemDecoration() {

    /**
     *  如果这里设置为true position=0 四周边距则强制改成0 这里适用于addHeadView
     */
    var isHeadView = false

    /**
     * 第一个条目之前是否显示分割线, 当处于[VBRecyclerViewItemOrientation.GRID] 时水平方向顶端和末端是否显示分割线
     */
    var isStartVisible = false

    /**
     * 最后一个条目是否显示分割线, 当处于[VBRecyclerViewItemOrientation.GRID] 时垂直方向顶端和末端是否显示分割线
     */
    var isEndVisible = false

    var isCludeVisible
        get() = isStartVisible && isEndVisible
        set(value) {
            isStartVisible = value
            isEndVisible = value
        }


    var orientation = VBRecyclerViewItemOrientation.VERTICAL

    private var size = 1
    private var marginStart = 0
    private var marginEnd = 0
    private var divider: Drawable? = null


    var typePool: MutableList<Int>? = null


    /**
     * 将图片作为分割线, 图片宽高即分割线宽高
     */
    fun setDrawable(drawable: Drawable) {
        divider = drawable
    }

    /**
     * 将图片作为分割线, 图片宽高即分割线宽高
     */
    fun setDrawable(@DrawableRes drawableRes: Int) {
        val drawable = ContextCompat.getDrawable(context, drawableRes)
            ?: throw IllegalArgumentException("Drawable cannot be find")
        divider = drawable
    }

    /**
     * 设置分割线颜色, 如果不设置分割线宽度[setDivider]则分割线宽度默认为1px
     * 所谓分割线宽度指的是分割线的粗细, 而非水平宽度
     */
    fun setColor(@ColorInt color: Int) {
        divider = ColorDrawable(color)
    }

    /**
     * 设置分割线颜色, 如果不设置分割线宽度[setDivider]则分割线宽度默认为1px
     * 所谓分割线宽度指的是分割线的粗细, 而非水平宽度
     *
     * @param color 16进制的颜色值字符串
     */
    fun setColor(color: String) {
        val parseColor = Color.parseColor(color)
        divider = ColorDrawable(parseColor)
    }

    /**
     * 设置分割线颜色, 如果不设置分割线宽度[setDivider]则分割线宽度默认为1px
     * 所谓分割线宽度指的是分割线的粗细, 而非水平宽度
     *
     * @param color 颜色资源Id
     */
    fun setColorRes(@ColorRes color: Int) {
        val colorRes = ContextCompat.getColor(context, color)
        divider = ColorDrawable(colorRes)
    }

    private var background = Color.TRANSPARENT

    /**
     * 分割线背景色
     * 分割线有时候会存在间距(例如配置[setMargin])或属于虚线, 这个时候暴露出来的是RecyclerView的背景色, 所以我们可以设置一个背景色来调整
     * 可以设置背景色解决不统一的问题, 默认为透明[Color.TRANSPARENT]
     */
    fun setBackground(@ColorInt color: Int) {
        background = color
    }

    /**
     * 分割线背景色
     * 分割线有时候会存在间距(例如配置[setMargin])或属于虚线, 这个时候暴露出来的是RecyclerView的背景色, 所以我们可以设置一个背景色来调整
     * 可以设置背景色解决不统一的问题, 默认为透明[Color.TRANSPARENT]
     *
     * @param colorString 颜色的16进制字符串
     */
    fun setBackground(colorString: String) {
        try {
            background = Color.parseColor(colorString)
        } catch (e: Exception) {
            throw IllegalArgumentException("Unknown color: $colorString")
        }
    }

    /**
     * 分割线背景色
     * 分割线有时候会存在间距(例如配置[setMargin])或属于虚线, 这个时候暴露出来的是RecyclerView的背景色, 所以我们可以设置一个背景色来调整
     * 可以设置背景色解决不统一的问题, 默认为透明[Color.TRANSPARENT]
     *
     */
    fun setBackgroundRes(@ColorRes color: Int) {
        background = ContextCompat.getColor(context, color)
    }


    /**
     * 设置分割线宽度
     * 如果使用[setDrawable]则无效
     * @param width 分割线的尺寸 (分割线垂直时为宽, 水平时为高 )
     * @param dp 是否单位为dp, false即使用像素单位
     */
    fun setDivider(width: Int = 1, dp: Boolean = true) {
        if (!dp) {
            this.size = width
        } else {
            val density = context.resources.displayMetrics.density
            this.size = (density * width).roundToInt()
        }
    }

    /**
     * 设置分隔左右或上下间距, 依据分割线为垂直或者水平决定具体方向间距
     *
     * @param start 分割线为水平则是左间距, 垂直则为上间距
     * @param end 分割线为水平则是右间距, 垂直则为下间距
     * @param dp 是否单位为dp, 默认为true即使用dp单位
     */
    fun setMargin(start: Int = 0, end: Int = 0, dp: Boolean = true) {
        if (!dp) {
            this.marginStart = start
            this.marginEnd = end
        } else {
            val density = context.resources.displayMetrics.density
            this.marginStart = (start * density).roundToInt()
            this.marginEnd = (end * density).roundToInt()
        }
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {

        val layoutManager = parent.layoutManager ?: return

        val position = parent.getChildAdapterPosition(view)

        val height = when {
            divider == null -> size
            divider?.intrinsicHeight != -1 -> divider!!.intrinsicHeight
            divider?.intrinsicWidth != -1 -> divider!!.intrinsicWidth
            else -> size
        }

        val width = when {
            divider == null -> size
            divider?.intrinsicWidth != -1 -> divider!!.intrinsicWidth
            divider?.intrinsicHeight != -1 -> divider!!.intrinsicHeight
            else -> size
        }

        val edge = computeEdge(position, layoutManager)
        adjustOrientation(layoutManager)

        when (orientation) {
            VBRecyclerViewItemOrientation.VERTICAL -> {
                var top = if (isStartVisible && edge.top) height else 0
                var bottom = if ((isEndVisible && edge.bottom) || !edge.bottom) height else 0
                if (edge.top) {
                    top += marginStart
                }
                if (edge.bottom) {
                    bottom += marginEnd
                }
                outRect.set(0, top, 0, bottom)
            }

            VBRecyclerViewItemOrientation.HORIZONTAL -> {
                var left = if (isStartVisible && edge.left) width else 0
                var right = if ((isEndVisible && edge.right) || !edge.right) width else 0
                if (edge.left) {
                    left += marginStart
                }
                if (edge.right) {
                    right += marginEnd
                }
                outRect.set(left, 0, right, 0)
            }

            VBRecyclerViewItemOrientation.GRID -> {

                val spanCount = when (layoutManager) {
                    is GridLayoutManager -> layoutManager.spanCount
                    is StaggeredGridLayoutManager -> layoutManager.spanCount
                    else -> 1
                }

                val spanGroupCount = when (layoutManager) {
                    is GridLayoutManager -> layoutManager.spanSizeLookup.getSpanGroupIndex(
                        state.itemCount - 1,
                        spanCount
                    ) + 1

                    is StaggeredGridLayoutManager -> ceil(state.itemCount / spanCount.toFloat()).toInt()
                    else -> 1
                }

                val spanIndex = when (layoutManager) {
                    is GridLayoutManager -> layoutManager.spanSizeLookup.getSpanIndex(
                        position,
                        spanCount
                    )

                    is StaggeredGridLayoutManager -> (layoutManager.findViewByPosition(position)!!.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
                    else -> 0
                }

                val spanGroupIndex = when (layoutManager) {
                    is GridLayoutManager -> layoutManager.spanSizeLookup.getSpanGroupIndex(
                        position,
                        spanCount
                    )

                    is StaggeredGridLayoutManager -> ceil((position + 1) / spanCount.toFloat()).toInt() - 1
                    else -> 0
                }

                val spanSize = when (layoutManager) {
                    is GridLayoutManager -> layoutManager.spanSizeLookup.getSpanSize(position)
                    else -> 1
                }

                val orientation = when (layoutManager) {
                    is GridLayoutManager -> layoutManager.orientation
                    is StaggeredGridLayoutManager -> layoutManager.orientation
                    else -> RecyclerView.VERTICAL
                }

                val left = when {
                    isEndVisible && orientation == RecyclerView.VERTICAL -> width - spanIndex * width / spanCount
                    isStartVisible && orientation == RecyclerView.HORIZONTAL -> width - spanIndex * width / spanCount
                    else -> spanIndex * width / spanCount
                }

                val right = when {
                    isEndVisible && orientation == RecyclerView.VERTICAL -> (spanIndex + spanSize) * width / spanCount
                    isStartVisible && orientation == RecyclerView.HORIZONTAL -> (spanIndex + spanSize) * width / spanCount
                    else -> width - (spanIndex + spanSize) * width / spanCount
                }

                val top = when {
                    layoutManager is StaggeredGridLayoutManager -> {
                        if (orientation == RecyclerView.VERTICAL) {
                            if (edge.top) if (isStartVisible) height else 0 else 0
                        } else {
                            if (edge.left) if (isEndVisible) width else 0 else 0
                        }
                    }

                    (isStartVisible || isEndVisible) && orientation == RecyclerView.VERTICAL -> height - spanGroupIndex * height / spanGroupCount
                    else -> spanGroupIndex * height / spanGroupCount
                }


                val bottom = when {
                    layoutManager is StaggeredGridLayoutManager -> {
                        if (orientation == RecyclerView.VERTICAL) {
                            if (edge.bottom) if (isStartVisible) height else 0 else height
                        } else {
                            if (edge.right) if (isEndVisible) width else 0 else height
                        }
                    }

                    isStartVisible && orientation == RecyclerView.VERTICAL && layoutManager is StaggeredGridLayoutManager -> if (spanGroupIndex == 0) height else 0
                    (isStartVisible || isEndVisible) && orientation == RecyclerView.VERTICAL -> (spanGroupIndex + 1) * height / spanGroupCount
                    else -> height - (spanGroupIndex + 1) * height / spanGroupCount
                }

                if (position == 0 && isHeadView) {
                    outRect.set(0, 0, 0, 0)
                } else {
                    if (orientation == RecyclerView.VERTICAL) {
                        outRect.set(left, top, right, bottom)
                    } else outRect.set(top, left, bottom, right)
                }
            }

            else -> {

            }
        }
    }


    override fun onDraw(canvas: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val layoutManager = parent.layoutManager
        if (layoutManager == null || divider == null) return

        adjustOrientation(layoutManager)

        when (orientation) {
            VBRecyclerViewItemOrientation.HORIZONTAL -> drawHorizontal(canvas, parent)
            VBRecyclerViewItemOrientation.VERTICAL -> drawVertical(canvas, parent)
            VBRecyclerViewItemOrientation.GRID -> drawGrid(canvas, parent)
            else -> {

            }
        }
    }

    /**
     * 自动调整不同布局管理器应该对应的[orientation]
     */
    private fun adjustOrientation(layoutManager: RecyclerView.LayoutManager) {
        if (layoutManager !is GridLayoutManager && layoutManager is LinearLayoutManager) {
            orientation =
                if ((layoutManager as? LinearLayoutManager)?.orientation == RecyclerView.VERTICAL) VBRecyclerViewItemOrientation.VERTICAL else VBRecyclerViewItemOrientation.HORIZONTAL
        } else if (layoutManager is StaggeredGridLayoutManager || layoutManager is GridLayoutManager) {
            orientation = VBRecyclerViewItemOrientation.GRID
        }
    }

    /**
     * 绘制水平分割线
     */
    private fun drawHorizontal(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val left: Int
        val right: Int

        if (parent.clipToPadding) {
            left = parent.paddingLeft + this.marginStart
            right = parent.width - parent.paddingRight - marginEnd
        } else {
            left = 0 + this.marginStart
            right = parent.width - marginEnd
        }

        loop@ for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)

            val position = parent.getChildAdapterPosition(child)
            val layoutManager = parent.layoutManager ?: return
            val edge = computeEdge(position, layoutManager)

            if (orientation != VBRecyclerViewItemOrientation.GRID && !isEndVisible && edge.bottom) {
                continue@loop
            }

            divider?.apply {
                val decoratedBounds = Rect()
                parent.getDecoratedBoundsWithMargins(child, decoratedBounds)

                val firstBottom =
                    if (intrinsicHeight == -1) decoratedBounds.top + size else decoratedBounds.top + intrinsicHeight
                val firstTop = decoratedBounds.top

                val bottom = decoratedBounds.bottom
                val top = if (intrinsicHeight == -1) bottom - size else bottom - intrinsicHeight

                if (background != Color.TRANSPARENT) {
                    val paint = Paint()
                    paint.color = background
                    paint.style = Paint.Style.FILL

                    if (isStartVisible && edge.top) {
                        val firstRect = Rect(
                            parent.paddingLeft,
                            firstTop,
                            parent.width - parent.paddingRight,
                            firstBottom
                        )
                        canvas.drawRect(firstRect, paint)
                    }

                    val rect =
                        Rect(parent.paddingLeft, top, parent.width - parent.paddingRight, bottom)
                    canvas.drawRect(rect, paint)
                }

                if (isStartVisible && edge.top) {
                    setBounds(left, firstTop, right, firstBottom)
                    draw(canvas)
                }

                setBounds(left, top, right, bottom)
                draw(canvas)
            }
        }
        canvas.restore()
    }

    /**
     * 绘制垂直分割线
     */
    private fun drawVertical(canvas: Canvas, parent: RecyclerView) {
        canvas.save()
        val top: Int
        val bottom: Int

        if (parent.clipToPadding) {
            top = parent.paddingTop + marginStart
            bottom = parent.height - parent.paddingBottom - marginEnd
        } else {
            top = 0 + marginStart
            bottom = parent.height - marginEnd
        }

        val childCount = parent.childCount

        loop@ for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val position = parent.getChildAdapterPosition(child)
            val layoutManager = parent.layoutManager ?: return
            val edge = computeEdge(position, layoutManager)

            if (orientation != VBRecyclerViewItemOrientation.GRID && !isEndVisible && edge.right) {
                continue@loop
            }

            divider?.apply {
                val decoratedBounds = Rect()
                parent.getDecoratedBoundsWithMargins(child, decoratedBounds)

                val firstRight =
                    if (intrinsicWidth == -1) decoratedBounds.left + size else decoratedBounds.left + intrinsicWidth
                val firstLeft = decoratedBounds.left

                val right = (decoratedBounds.right + child.translationX).roundToInt()
                val left = if (intrinsicWidth == -1) right - size else right - intrinsicWidth

                if (background != Color.TRANSPARENT) {
                    val paint = Paint()
                    paint.color = background
                    paint.style = Paint.Style.FILL

                    if (isStartVisible && edge.left) {
                        val firstRect = Rect(
                            firstLeft,
                            parent.paddingTop,
                            firstRight,
                            parent.height - parent.paddingBottom
                        )
                        canvas.drawRect(firstRect, paint)
                    }

                    val rect =
                        Rect(left, parent.paddingTop, right, parent.height - parent.paddingBottom)
                    canvas.drawRect(rect, paint)
                }

                if (isStartVisible && edge.left) {
                    setBounds(firstLeft, top, firstRight, bottom)
                    draw(canvas)
                }

                setBounds(left, top, right, bottom)
                draw(canvas)
            }
        }

        canvas.restore()
    }

    /**
     * 绘制网格分割线
     */
    private fun drawGrid(canvas: Canvas, parent: RecyclerView) {
        canvas.save()

        val childCount = parent.childCount

        loop@ for (i in 0 until childCount) {
            val child = parent.getChildAt(i)

            val position = parent.getChildAdapterPosition(child)
            val layoutManager = parent.layoutManager ?: return
            val edge = computeEdge(position, layoutManager)

            val height = when {
                divider == null -> size
                divider?.intrinsicHeight != -1 -> divider!!.intrinsicHeight
                divider?.intrinsicWidth != -1 -> divider!!.intrinsicWidth
                else -> size
            }

            val width = when {
                divider == null -> size
                divider?.intrinsicWidth != -1 -> divider!!.intrinsicWidth
                divider?.intrinsicHeight != -1 -> divider!!.intrinsicHeight
                else -> size
            }

            divider?.apply {
                val layoutParams = child.layoutParams as RecyclerView.LayoutParams
                val bounds = Rect(
                    child.left + layoutParams.leftMargin,
                    child.top + layoutParams.topMargin,
                    child.right + layoutParams.rightMargin,
                    child.bottom + layoutParams.bottomMargin
                )

                // top
                if (!isEndVisible && edge.right) {
                    setBounds(
                        bounds.left - width,
                        bounds.top - height,
                        bounds.right - marginEnd,
                        bounds.top
                    )
                    draw(canvas)
                } else if (!isEndVisible && !edge.top && edge.left) {
                    setBounds(
                        bounds.left + marginStart,
                        bounds.top - height,
                        bounds.right + width,
                        bounds.top
                    )
                    draw(canvas)
                } else if (!edge.top || (isStartVisible && edge.top)) {
                    setBounds(
                        bounds.left - width,
                        bounds.top - height,
                        bounds.right + width,
                        bounds.top
                    )
                    draw(canvas)
                }

                // bottom
                if (!isEndVisible && edge.right) {
                    setBounds(
                        bounds.left - width,
                        bounds.bottom,
                        bounds.right - marginEnd,
                        bounds.bottom + height
                    )
                    draw(canvas)
                } else if (!isEndVisible && !edge.bottom && edge.left) {
                    setBounds(
                        bounds.left + marginStart,
                        bounds.bottom,
                        bounds.right + width,
                        bounds.bottom + height
                    )
                    draw(canvas)
                } else if (!edge.bottom || (isStartVisible && edge.bottom)) {
                    setBounds(
                        bounds.left - width,
                        bounds.bottom,
                        bounds.right + width,
                        bounds.bottom + height
                    )
                    draw(canvas)
                }

                // left
                if (edge.top && !isEndVisible && !edge.left) {
                    setBounds(
                        bounds.left - width,
                        bounds.top + marginStart,
                        bounds.left,
                        bounds.bottom
                    )
                    draw(canvas)
                } else if (edge.bottom && !isEndVisible && !edge.left) {
                    setBounds(
                        bounds.left - width,
                        bounds.top,
                        bounds.left,
                        bounds.bottom - marginEnd
                    )
                    draw(canvas)
                } else if (!edge.left || (isEndVisible && edge.left)) {
                    setBounds(bounds.left - width, bounds.top, bounds.left, bounds.bottom)
                    draw(canvas)
                }

                // right
                if (edge.top && !isEndVisible && !edge.right) {
                    setBounds(
                        bounds.right,
                        bounds.top + marginStart,
                        bounds.right + width,
                        bounds.bottom
                    )
                    draw(canvas)
                } else if (edge.bottom && !isEndVisible && !edge.right) {
                    setBounds(
                        bounds.right,
                        bounds.top,
                        bounds.right + width,
                        bounds.bottom - marginEnd
                    )
                    draw(canvas)
                } else if (!edge.right || (isEndVisible && edge.right)) {
                    setBounds(bounds.right, bounds.top, bounds.right + width, bounds.bottom)
                    draw(canvas)
                }
            }
        }

        canvas.restore()
    }

    /**
     * 列表条目是否靠近边缘的结算结果
     *
     * @param left 是否靠左
     * @param right 是否靠左
     * @param top 是否靠顶
     * @param bottom 是否靠底
     */
    data class Edge(
        var left: Boolean = false,
        var top: Boolean = false,
        var right: Boolean = false,
        var bottom: Boolean = false,
    ) {

        companion object {

            /**
             * 计算指定条目的边缘位置
             * @param position 指定计算的Item索引
             * @param layoutManager 当前列表的LayoutManager
             */
            fun computeEdge(position: Int, layoutManager: RecyclerView.LayoutManager): Edge {

                val index = position + 1
                val itemCount = layoutManager.itemCount

                return Edge().apply {
                    when (layoutManager) {
                        is StaggeredGridLayoutManager -> {
                            val spanCount = layoutManager.spanCount
                            val spanIndex =
                                (layoutManager.findViewByPosition(position)!!.layoutParams
                                        as StaggeredGridLayoutManager.LayoutParams).spanIndex + 1

                            if (layoutManager.orientation == RecyclerView.VERTICAL) {
                                left = spanIndex == 1
                                right = spanIndex == spanCount
                                top = index <= spanCount
                                bottom = index > itemCount - spanCount
                            } else {
                                left = index <= spanCount
                                right = index > itemCount - spanCount
                                top = spanIndex == 1
                                bottom = spanIndex == spanCount
                            }
                        }

                        is GridLayoutManager -> {
                            val spanSizeLookup = layoutManager.spanSizeLookup
                            val spanCount = layoutManager.spanCount
                            val spanGroupIndex =
                                spanSizeLookup.getSpanGroupIndex(position, spanCount)
                            val maxSpanGroupIndex = ceil(itemCount / spanCount.toFloat()).toInt()
                            val spanIndex = spanSizeLookup.getSpanIndex(position, spanCount) + 1
                            val spanSize = spanSizeLookup.getSpanSize(position)

                            if (layoutManager.orientation == RecyclerView.VERTICAL) {
                                left = spanIndex == 1
                                right = spanIndex + spanSize - 1 == spanCount
                                top =
                                    index <= spanCount && spanGroupIndex == spanSizeLookup.getSpanGroupIndex(
                                        position - 1,
                                        spanCount
                                    )
                                bottom = spanGroupIndex == maxSpanGroupIndex - 1

                            } else {
                                left = spanGroupIndex == 0
                                right = spanGroupIndex == maxSpanGroupIndex - 1
                                top = spanIndex == 1
                                bottom = spanIndex + spanSize - 1 == spanCount
                            }
                        }

                        is LinearLayoutManager -> {
                            if (layoutManager.orientation == RecyclerView.VERTICAL) {
                                left = true
                                right = true
                                top = index == 1
                                bottom = index == itemCount
                            } else {
                                left = index == 1
                                right = index == itemCount
                                top = true
                                bottom = true
                            }
                        }
                    }
                }
            }
        }
    }
}
