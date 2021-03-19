package com.v.demo

import com.v.base.BaseActivity
import android.view.View
import com.v.base.BlankViewModel
import com.v.base.utils.LiveDataBus
import com.v.base.utils.getApplicationViewModel
import com.v.base.utils.randomNumber
import com.v.base.utils.toast
import com.v.demo.R
import com.v.demo.bean.UserBane
import com.v.demo.databinding.DmActivityEventBusDemoBinding
import com.v.demo.model.AppViewModel

/**
 * author  :
 * desc    : 通过liveData 模拟eventBus
 * time    : 2021-03-19 10:28:14
 */
class EventBusDemoActivity : BaseActivity<DmActivityEventBusDemoBinding, BlankViewModel>(),
    View.OnClickListener {

    override fun toolBarTitle(title: String, titleColor: Int) {
        super.toolBarTitle(
            this.getString(R.string.dm_string_activity_event_bus_demo_title),
            titleColor
        )
    }

    override fun initData() {
        mViewBinding.v = this
    }

    override fun createObserver() {

    }

    override fun onClick(v: View) {
        when (v.id) {
            mViewBinding.bt0.id -> {

                var bean = UserBane()
                bean.name = "全局修改"
                getApplicationViewModel(
                    mContext.application,
                    AppViewModel::class.java
                ).userBane.postValue(bean)

                "修改成功请返回查看".toast()
            }
            mViewBinding.bt1.id -> {
                LiveDataBus.with<String>(ConstData.CONTENT).postData("模拟eventBus")

                "修改成功请返回查看".toast()
            }
        }
    }

}