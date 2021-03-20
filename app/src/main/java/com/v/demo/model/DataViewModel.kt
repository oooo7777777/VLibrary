package com.v.demo.model

import androidx.lifecycle.MutableLiveData
import com.v.base.BaseViewModel
import com.v.base.utils.toast
import com.v.demo.net.apiService


/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/12 16:11
 */
class DataViewModel : BaseViewModel() {

    var content = MutableLiveData<String>()

    fun setContent(s: String) {
        content.value = s
    }

}