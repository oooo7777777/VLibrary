package com.example.vlibrary30

import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.launcher.ARouter
import com.example.vlibrary30.databinding.ActivityMainBinding
import com.v.base.BaseActivity
import com.v.base.BaseFragmentAdapter
import com.v.base.BlankViewModel
import com.v.base.utils.ext.toast
import com.ww.appmodule.RouterConstant


class MainActivity : BaseActivity<ActivityMainBinding, BlankViewModel>() {


    override fun initData() {
        val list = java.util.ArrayList<Fragment>()

        val fragmentHome = getFragment(RouterConstant.ROUTER_FRAGMENT_HOME)
        val fragmentMe = getFragment(RouterConstant.ROUTER_FRAGMENT_ME)


        if (fragmentHome == null || fragmentMe == null) {
            "业务组件单独调试不应该跟其他业务Module产生交互".toast()
        } else {
            list.add(fragmentHome)
            list.add(fragmentMe)

            val titles = arrayOf("首页", "我的")
            var adapter = BaseFragmentAdapter(supportFragmentManager, list, titles)
            mViewBinding.viewPage.offscreenPageLimit = list.size
            mViewBinding.viewPage.adapter = adapter

            mViewBinding.tabLayout.setupWithViewPager(mViewBinding.viewPage)
            mViewBinding.tabLayout.getTabAt(0)?.select()
        }

    }

    override fun createObserver() {
    }

    private fun getFragment(path: String): Fragment =
        run {
            ARouter.getInstance().build(path).navigation() as Fragment
        }
}