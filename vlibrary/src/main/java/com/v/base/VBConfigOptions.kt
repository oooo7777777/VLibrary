package com.v.base

import android.graphics.Color
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.scwang.smart.refresh.footer.ClassicsFooter
import com.scwang.smart.refresh.header.ClassicsHeader
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator
import com.v.base.net.VBNetOptions
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit


/**
 * author  : ww
 * desc    : 配置
 * time    : 2022-02-16
 */
class VBConfigOptions(builder: Builder) {


    //设置状态栏颜色 状态栏颜色趋近于白色时 会智能将状态栏字体颜色变换为黑色
    var statusBarColor: Int

    //设置默认RecyclerView 数据为空界面
    var recyclerViewEmptyLayout: Int

    //设置默认RecyclerView错误界面 只会在page为1并且没有数据的时候显示
    var recyclerViewErrorLayout: Int

    //全局设置SmartRefreshLayout Header
    var smartRefreshHeader: DefaultRefreshHeaderCreator


    //全局设置SmartRefreshLayout Footer
    var smartRefreshFooter: DefaultRefreshFooterCreator

    //网络请求配置
    var netOptions: VBNetOptions

    init {
        this.statusBarColor = builder.statusBarColor
        this.recyclerViewEmptyLayout = builder.recyclerViewEmptyLayout
        this.recyclerViewErrorLayout = builder.recyclerViewErrorLayout
        this.smartRefreshHeader = builder.smartRefreshHeader
        this.smartRefreshFooter = builder.smartRefreshFooter
        this.netOptions = builder.netOptions

    }

    class Builder {

        internal var statusBarColor = Color.parseColor("#000000")
        internal var recyclerViewEmptyLayout = R.layout.vb_layout_empty
        internal var recyclerViewErrorLayout = R.layout.vb_layout_error

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

        fun setRecyclerViewEmptyLayout(recyclerViewEmptyLayout: Int): Builder {
            this.recyclerViewEmptyLayout = recyclerViewEmptyLayout
            return this
        }

        fun setRecyclerViewErrorLayout(recyclerViewErrorLayout: Int): Builder {
            this.recyclerViewErrorLayout = recyclerViewErrorLayout
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