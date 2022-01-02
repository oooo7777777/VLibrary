package com.v.base


import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import com.v.base.utils.ext.vbGetStatusBarHeight
import com.v.base.utils.ext.vbOnClickAnimator
import com.v.base.utils.ext.vbSetViewLayoutParams
import com.v.base.utils.isWhiteColor
import com.v.base.utils.vbTextBold

/**
 * @Author : ww
 * desc    :
 * time    : 2021/11/2
 */
class VBTitleBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {


    private var ivStatusBar: ImageView
    private var toolbar: Toolbar

    private var tvTitle: TextView

    private var flLeft: FrameLayout
    private var tvLeft: TextView
    private var ivLeft: ImageView


    private var flRight: FrameLayout
    private var tvRight: TextView
    private var ivRight: ImageView

    private var ivLine: ImageView

    init {
        View.inflate(context, R.layout.vb_title_bar, this)

        ivStatusBar = findViewById(R.id.ivStatusBar)
        toolbar = findViewById(R.id.toolbar)
        ivLine = findViewById(R.id.ivLine)

        tvTitle = findViewById(R.id.tvTitle)

        flLeft = findViewById(R.id.flLeft)
        tvLeft = findViewById(R.id.tvLeft)
        ivLeft = findViewById(R.id.ivLeft)


        flRight = findViewById(R.id.flRight)
        tvRight = findViewById(R.id.tvRight)
        ivRight = findViewById(R.id.ivRight)

        ivStatusBar.vbSetViewLayoutParams(h = context.vbGetStatusBarHeight())
    }


    fun useToolbar(show: Boolean) {
        toolbar.visibility = if (show) View.VISIBLE else View.GONE
        ivLine.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun useLine(show: Boolean) {
        ivLine.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun useStatusBar(show: Boolean) {
        ivStatusBar.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun setStatusBarColor(color: Int) {
        ivStatusBar.setBackgroundColor(color)
    }

    fun setToolbarColor(color: Int) {
        toolbar.setBackgroundColor(color)
    }

    fun setTitle(
        title: String = "",
        titleColor: Int = Color.BLACK,
        isShowBottomLine: Boolean = true,
        listener: OnClickListener? = null
    ) {
        tvTitle.text = title
        tvTitle.setTextColor(titleColor)
        tvTitle.vbTextBold(true)
        if (listener != null) {
            tvTitle.vbOnClickAnimator {
                listener.onClick(it)
            }
        }

        ivLine.visibility = if (isShowBottomLine) View.VISIBLE else View.GONE
    }


    fun setLeft(
        title: String = "",
        titleColor: Int = Color.BLACK,
        listener: OnClickListener? = null
    ) {
        ivLeft.visibility = View.GONE
        tvLeft.visibility = View.VISIBLE

        tvLeft.text = title
        tvLeft.setTextColor(titleColor)

        if (listener != null) {
            flLeft.vbOnClickAnimator {
                listener.onClick(it)
            }
        }
    }

    fun setLeft(
        res: Int = R.mipmap.vb_ic_back_black,
        listener: OnClickListener? = null
    ) {

        ivLeft.visibility = View.VISIBLE
        tvLeft.visibility = View.GONE

        ivLeft.setImageResource(res)

        if (listener != null) {
            flLeft.vbOnClickAnimator {
                listener.onClick(it)
            }
        }
    }


    fun setRight(
        title: String = "",
        titleColor: Int = Color.BLACK,
        listener: OnClickListener? = null
    ) {
        ivRight.visibility = View.GONE
        tvRight.visibility = View.VISIBLE

        tvRight.text = title
        tvRight.setTextColor(titleColor)

        if (listener != null) {
            flRight.vbOnClickAnimator {
                listener.onClick(it)
            }
        }

    }

    fun setRight(
        res: Int = R.mipmap.vb_ic_back_black,
        listener: OnClickListener? = null
    ) {

        ivRight.visibility = View.VISIBLE
        tvRight.visibility = View.GONE

        ivRight.setImageResource(res)

        if (listener != null) {
            flRight.vbOnClickAnimator {
                listener.onClick(it)
            }
        }
    }


    fun getTitleView(): TextView {
        return tvTitle
    }

    fun getLeftLayout(): FrameLayout {
        return flLeft
    }

    fun getLeftTextView(): TextView {
        return tvLeft
    }

    fun getLeftImageView(): ImageView {
        return ivLeft
    }


    fun getRightLayout(): FrameLayout {
        return flRight
    }

    fun getRightTextView(): TextView {
        return tvRight
    }

    fun getLine(): ImageView {
        return ivLine
    }

}
