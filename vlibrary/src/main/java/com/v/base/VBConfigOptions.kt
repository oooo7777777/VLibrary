package com.v.base

import android.graphics.Color
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.horizontal.SmartRefreshHorizontal
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator
import com.scwang.smart.refresh.layout.wrapper.RefreshFooterWrapper
import com.v.base.net.VBNetOptions


/**
 * author  : ww
 * desc    : 全局配置
 * time    : 2022-02-16
 */
class VBConfigOptions(builder: Builder) {


    //顶部栏背景颜色
    var toolbarColor: Int

    //顶部栏返回按钮
    var toolbarBackRes: Int

    //顶部栏Title颜色
    var toolbarTitleColor: Int

    //顶部栏下面的横线是否显示 默认显示
    var toolbarLine: Boolean


    //设置状态栏颜色 状态栏颜色趋近于白色时 会智能将状态栏字体颜色变换为黑色
    var statusBarColor: String

    //底部导航栏颜色
    var navigationBarColor: String

    //设置默认RecyclerView 数据为空界面 只会在page为1并且没有数据的时候显示
    var recyclerViewEmptyLayout: Int

    //全局设置SmartRefreshLayout Header
    var smartRefreshHeader: DefaultRefreshHeaderCreator

    //全局设置SmartRefreshLayout Footer
    var smartRefreshFooter: DefaultRefreshFooterCreator

    //全局设置SmartRefreshLayoutHorizontal Footer
    var smartRefreshFooterHorizontal: DefaultRefreshFooterCreator

    //smartRefresh 是否启用越界拖动
    var refreshScrollDrag: Boolean

    //点击是否加入点击动画
    var clickAnimator: Boolean

    //点击防抖动间隔
    var clickTime: Long

    //网络请求配置
    var netOptions: VBNetOptions

    //dialog变暗系数
    var dialogDimAmount: Float

    //图片加载错误图片
    var errorResId: Int

    init {
        this.statusBarColor = builder.statusBarColor
        this.navigationBarColor = builder.navigationBarColor
        this.toolbarColor = builder.toolbarColor
        this.toolbarBackRes = builder.toolbarBackRes
        this.toolbarTitleColor = builder.toolbarTitleColor
        this.toolbarLine = builder.toolbarLine
        this.recyclerViewEmptyLayout = builder.recyclerViewEmptyLayout
        this.smartRefreshHeader = builder.smartRefreshHeader
        this.smartRefreshFooter = builder.smartRefreshFooter
        this.smartRefreshFooterHorizontal = builder.smartRefreshFooterHorizontal
        this.refreshScrollDrag = builder.refreshScrollDrag
        this.netOptions = builder.netOptions
        this.clickAnimator = builder.clickAnimator
        this.clickTime = builder.clickTime
        this.dialogDimAmount = builder.dialogDimAmount
        this.errorResId = builder.errorResId
    }

    class Builder {

        internal var statusBarColor = "#ffffff"
        internal var navigationBarColor = "#ffffff"
        internal var toolbarColor = Color.parseColor("#FFFFFF")
        internal var toolbarTitleColor = Color.parseColor("#000000")
        internal var toolbarLine = true
        internal var toolbarBackRes = R.mipmap.vb_ic_back_black
        internal var recyclerViewEmptyLayout = R.layout.vb_layout_empty
        internal var clickAnimator = true
        internal var clickTime = 300L
        internal var dialogDimAmount = 0.5f
        internal var errorResId = R.mipmap.vb_iv_empty

        internal var netOptions = VBNetOptions.Builder().build()

        internal var smartRefreshHeader = DefaultRefreshHeaderCreator { context, layout ->
            ClassicsHeader(context)
        }

        internal var smartRefreshFooter = DefaultRefreshFooterCreator { context, layout ->
            ClassicsFooter(context)
        }

        internal var smartRefreshFooterHorizontal = DefaultRefreshFooterCreator { context, layout ->
            DetailHorizontalFooter(context)
        }

        internal var refreshScrollDrag = true

        /**
         *  设置状态栏颜色 状态栏颜色趋近于白色时 会智能将状态栏字体颜色变换为黑色
         *  @param statusBarColor  "#ffffff"
         */
        fun setStatusBarColor(statusBarColor: String): Builder {
            this.statusBarColor = statusBarColor
            return this
        }

        /**
         *  底部导航栏颜色
         *  @param navigationBarColor  "#ffffff"
         */
        fun setNavigationBarColor(navigationBarColor: String): Builder {
            this.navigationBarColor = navigationBarColor
            return this
        }

        /**
         * 顶部栏背景颜色
         */
        fun setToolbarColor(toolbarColor: Int): Builder {
            this.toolbarColor = toolbarColor
            return this
        }

        /**
         * 顶部栏Title颜色
         */
        fun setToolbarTitleColor(toolbarTitleColor: Int): Builder {
            this.toolbarTitleColor = toolbarTitleColor
            return this
        }

        /**
         *
         * 顶部栏下面的横线是否显示 默认显示
         */
        fun setToolbarLine(boolean: Boolean): Builder {
            this.toolbarLine = boolean
            return this
        }

        /**
         * 顶部栏返回按钮
         */
        fun setToolbarBackRes(toolbarBackRes: Int): Builder {
            this.toolbarBackRes = toolbarBackRes
            return this
        }

        /**
         * 设置默认RecyclerView 数据为空界面 只会在page为1并且没有数据的时候显示
         */
        fun setRecyclerViewEmptyLayout(recyclerViewEmptyLayout: Int): Builder {
            this.recyclerViewEmptyLayout = recyclerViewEmptyLayout
            return this
        }


        /**
         * 全局设置SmartRefreshLayout Header
         */
        fun setSmartRefreshHeader(header: DefaultRefreshHeaderCreator): Builder {
            this.smartRefreshHeader = header
            return this
        }

        /**
         * 全局设置SmartRefreshLayout Footer
         */
        fun setSmartRefreshFooter(footer: DefaultRefreshFooterCreator): Builder {
            this.smartRefreshFooter = footer
            return this
        }

        /**
         * 全局设置SmartRefreshLayoutHorizontal Footer
         */
        fun setSmartRefreshFooterHorizontal(footer: DefaultRefreshFooterCreator): Builder {
            this.smartRefreshFooterHorizontal = footer
            return this
        }

        /**
         * smartRefresh 是否启用越界拖动
         */
        fun setRefreshScrollDrag(isScrollDrag: Boolean): Builder {
            this.refreshScrollDrag = isScrollDrag
            return this
        }

        /**
         * 点击是否加入点击动画
         */
        fun setClickAnimator(open: Boolean): Builder {
            this.clickAnimator = open
            return this
        }

        /**
         * 点击防抖动间隔
         */
        fun setClickTime(time: Long): Builder {
            this.clickTime = time
            return this
        }

        /**
         * dialog变暗系数
         */
        fun setDialogDimAmount(dimAmount: Float): Builder {
            this.dialogDimAmount = dialogDimAmount
            return this
        }

        /**
         * 设备固件加载错误站位图
         */
        fun setErrorResId(errorResId: Int): Builder {
            this.errorResId = errorResId
            return this
        }

        /**
         * 网络请求配置
         */
        fun setNetOptions(netOptions: VBNetOptions): Builder {
            this.netOptions = netOptions
            return this
        }

        fun build(): VBConfigOptions {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator(this.smartRefreshHeader)
            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator(this.smartRefreshFooter)

            SmartRefreshHorizontal.setDefaultRefreshHeaderCreator(this.smartRefreshHeader)
            //设置全局的横向Footer构建器
            SmartRefreshHorizontal.setDefaultRefreshFooterCreator(this.smartRefreshFooterHorizontal)
            return VBConfigOptions(this)
        }
    }


}