package com.v.base

import android.graphics.Color
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator
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

    //设置状态栏颜色 状态栏颜色趋近于白色时 会智能将状态栏字体颜色变换为黑色
    var statusBarColor: Int

    //设置默认RecyclerView 数据为空界面 只会在page为1并且没有数据的时候显示
    var recyclerViewEmptyLayout: Int

    //全局设置SmartRefreshLayout Header
    var smartRefreshHeader: DefaultRefreshHeaderCreator

    //全局设置SmartRefreshLayout Footer
    var smartRefreshFooter: DefaultRefreshFooterCreator


    //点击是否加入点击动画
    var clickAnimator = true

    //网络请求配置
    var netOptions: VBNetOptions

    init {
        this.statusBarColor = builder.statusBarColor
        this.toolbarColor = builder.toolbarColor
        this.toolbarBackRes = builder.toolbarBackRes
        this.toolbarTitleColor = builder.toolbarTitleColor
        this.recyclerViewEmptyLayout = builder.recyclerViewEmptyLayout
        this.smartRefreshHeader = builder.smartRefreshHeader
        this.smartRefreshFooter = builder.smartRefreshFooter
        this.netOptions = builder.netOptions
        this.clickAnimator = builder.clickAnimator
    }

    class Builder {

        internal var statusBarColor = Color.parseColor("#000000")
        internal var toolbarColor = Color.parseColor("#FFFFFF")
        internal var toolbarTitleColor = Color.parseColor("#000000")
        internal var toolbarBackRes = R.mipmap.vb_ic_back_black
        internal var recyclerViewEmptyLayout = R.layout.vb_layout_empty
        internal var clickAnimator = true

        internal var netOptions = VBNetOptions.Builder().build()

        internal var smartRefreshHeader = DefaultRefreshHeaderCreator { context, layout ->
            ClassicsHeader(context)
        }

        internal var smartRefreshFooter = DefaultRefreshFooterCreator { context, layout ->
            ClassicsFooter(context)
        }


        fun setStatusBarColor(statusBarColor: Int): Builder {
            this.statusBarColor = statusBarColor
            return this
        }

        fun setToolbarColor(toolbarColor: Int): Builder {
            this.toolbarColor = toolbarColor
            return this
        }

        fun setToolbarTitleColor(toolbarTitleColor: Int): Builder {
            this.toolbarTitleColor = toolbarTitleColor
            return this
        }

        fun setToolbarBackRes(toolbarBackRes: Int): Builder {
            this.toolbarBackRes = toolbarBackRes
            return this
        }

        fun setRecyclerViewEmptyLayout(recyclerViewEmptyLayout: Int): Builder {
            this.recyclerViewEmptyLayout = recyclerViewEmptyLayout
            return this
        }


        fun setSmartRefreshHeader(header: DefaultRefreshHeaderCreator): Builder {
            this.smartRefreshHeader = header
            return this
        }

        fun setSmartRefreshFooter(footer: DefaultRefreshFooterCreator): Builder {
            this.smartRefreshFooter = footer
            return this
        }

        fun setClickAnimator(open: Boolean): Builder {
            this.clickAnimator = open
            return this
        }

        fun setNetOptions(netOptions: VBNetOptions): Builder {
            this.netOptions = netOptions
            return this
        }


        fun build(): VBConfigOptions {
            //设置全局的Header构建器
            SmartRefreshLayout.setDefaultRefreshHeaderCreator(this.smartRefreshHeader)
            //设置全局的Footer构建器
            SmartRefreshLayout.setDefaultRefreshFooterCreator(this.smartRefreshFooter)
            return VBConfigOptions(this)
        }
    }


}