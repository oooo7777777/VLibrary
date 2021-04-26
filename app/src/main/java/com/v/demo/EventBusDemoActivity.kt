package com.v.demo

import android.view.View
import androidx.lifecycle.Observer
import com.v.base.BaseActivity
import com.v.base.BlankViewModel
import com.v.base.utils.LiveDataBus
import com.v.base.utils.getApplicationViewModel
import com.v.base.utils.randomNumber
import com.v.demo.databinding.ActivityEventBusDemoBinding
import com.v.demo.model.AppViewModel

/**
 * author  :
 * desc    : 通过liveData 模拟eventBus
 * time    : 2021-03-19 10:28:14
 */
class EventBusDemoActivity : BaseActivity<ActivityEventBusDemoBinding, BlankViewModel>(),
    View.OnClickListener {


    override fun toolBarTitle(title: String, titleColor: Int) {
        super.toolBarTitle(getString(R.string.dm_string_activity_event_bus_demo_title), titleColor)
    }

    override fun initData() {
        mViewBinding.v = this
    }

    override fun createObserver() {

        //全局数据监听
        getApplicationViewModel(mContext.application, AppViewModel::class.java).string.observe(
            this,
            androidx.lifecycle.Observer {
                mViewBinding.tvContent.text = it
            })


        //单项数据监听
        LiveDataBus.with<String>(ConstData.CONTENT).observe(this, Observer {
            mViewBinding.tvContent.text = it
        })

    }

    override fun onClick(v: View) {
        when (v.id) {
            mViewBinding.bt0.id -> {
                getApplicationViewModel(
                    mContext.application,
                    AppViewModel::class.java
                ).string.postValue("我是全局数据" + 99.randomNumber())

            }
            mViewBinding.bt1.id -> {
                LiveDataBus.with<String>(ConstData.CONTENT)
                    .postData("模拟eventBus" + 99.randomNumber())
            }
        }
    }

}