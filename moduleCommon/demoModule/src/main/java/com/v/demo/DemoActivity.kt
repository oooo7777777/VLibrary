package com.v.demo

import android.Manifest
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.v.base.BaseActivity
import com.v.base.BlankViewModel
import com.v.base.utils.ext.getApplicationViewModel
import com.v.base.utils.ext.getFragment
import com.v.base.utils.ext.randomNumber
import com.v.demo.bean.UserBane
import com.v.demo.databinding.DmActivityBinding
import com.v.demo.model.AppViewModel
import com.v.demo.view.IndicatorZoom
import net.lucode.hackware.magicindicator.ViewPagerHelper
import java.util.*

class DemoActivity : BaseActivity<DmActivityBinding, BlankViewModel>(), View.OnClickListener {

    // 请求一组权限
    private var permissions =
        arrayOf(Manifest.permission.ACCESS_WIFI_STATE, Manifest.permission.READ_EXTERNAL_STORAGE)
    private var permissionsCount = 0

    private val requestMultiplePermissions =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions: Map<String, Boolean> ->
            permissions.entries.forEach {
                if (it.value) {
                    permissionsCount++
                }
                if (permissionsCount == permissions.size) {
                    //权限全部申请成功
                }
            }
        }


    private val commonNavigator by lazy {

        var titles = resources.getStringArray(R.array.dm_tab)
        var iconOffs = arrayOf(
            R.mipmap.dm_ic_launcher,
            R.mipmap.dm_ic_launcher,
            R.mipmap.dm_ic_launcher,
            R.mipmap.dm_ic_launcher
        )
        var iconOns = arrayOf(
            R.mipmap.dm_ic_launcher_round,
            R.mipmap.dm_ic_launcher_round,
            R.mipmap.dm_ic_launcher_round,
            R.mipmap.dm_ic_launcher_round
        )

        val fragments = ArrayList<Fragment>()
        fragments.add(getFragment("home", OneFragment::class.java))
        fragments.add(getFragment("home1", TwoFragment::class.java))
        fragments.add(getFragment("home2", ThreeFragment::class.java))
        fragments.add(getFragment("home3", FourFragment::class.java))

        IndicatorZoom(
            this,
            mViewBinding.viewPager,
            fragments,
            titles,
            iconOffs,
            iconOns
        )

    }

    override fun initData() {
        mViewBinding.v = this
        initMg()

        requestMultiplePermissions.launch(permissions)

        mViewBinding.tvTitle.setOnClickListener(this)
    }

    override fun createObserver() {
    }


    private fun initMg() {

        mViewBinding.magicIndicator.navigator = commonNavigator
        ViewPagerHelper.bind(mViewBinding.magicIndicator, mViewBinding.viewPager);
        mViewBinding.viewPager.currentItem = 0
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.tvTitle -> {
                var bean = UserBane()
                bean.name = "name" + randomNumber(100)
                getApplicationViewModel(
                    mContext.application,
                    AppViewModel::class.java
                ).userBane.postValue(bean)
            }
        }
    }
}