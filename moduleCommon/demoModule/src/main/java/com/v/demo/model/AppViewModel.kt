package com.v.demo.model

import androidx.lifecycle.MutableLiveData
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.v.base.BaseViewModel
import com.v.demo.bean.OneBean
import com.v.demo.bean.UserBane

/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/27 14:55
 */
class AppViewModel: BaseViewModel() {

    var userBane = MutableLiveData<UserBane>()
}