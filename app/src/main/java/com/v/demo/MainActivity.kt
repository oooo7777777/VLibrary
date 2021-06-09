package com.v.demo

import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.v.base.BaseActivity
import com.v.base.BlankViewModel
import com.v.base.utils.ext.getFragment
import com.v.base.utils.ext.otherwise
import com.v.base.utils.ext.yes
import com.v.demo.databinding.MainActivityBinding
import com.v.demo.view.IndicatorZoom
import net.lucode.hackware.magicindicator.ViewPagerHelper
import java.util.*

class MainActivity : BaseActivity<MainActivityBinding, BlankViewModel>() {


    // 请求一组权限
    private var permissions =
        arrayOf(Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA)
    private var permissionsCount = 0

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions: Map<String, Boolean> ->
            permissions.entries.forEach {
                if (it.value) {
                    permissionsCount++
                }
                 (permissionsCount == permissions.size).yes {
                    //权限全部申请成功
                }.otherwise {
                     //部分权限申请失败
                 }
            }
        }


    private val commonNavigator by lazy {

        var titles = resources.getStringArray(R.array.dm_tab)
        var iconOffs = arrayOf(
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher,
            R.mipmap.ic_launcher
        )
        var iconOns = arrayOf(
            R.mipmap.ic_launcher_round,
            R.mipmap.ic_launcher_round,
            R.mipmap.ic_launcher_round,
            R.mipmap.ic_launcher_round
        )

        val fragments = ArrayList<Fragment>()
        fragments.add(getFragment("home", OneFragment::class.java))
        fragments.add(getFragment("home1", TwoFragment::class.java))
        fragments.add(getFragment("home2", ThreeFragment::class.java))
        fragments.add(getFragment("home3", FourFragment::class.java))

        IndicatorZoom(
            this,
            mDataBinding.viewPager,
            fragments,
            titles,
            iconOffs,
            iconOns
        )

    }

    override fun initData() {
        mDataBinding.v = this
        initMg()

        requestMultiplePermissions.launch(permissions)
    }

    override fun createObserver() {
    }


    private fun initMg() {

        mDataBinding.magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(mDataBinding.magicIndicator, mDataBinding.viewPager);
        mDataBinding.viewPager.currentItem = 0
    }


}