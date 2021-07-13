package com.v.demo.model

import androidx.lifecycle.MutableLiveData
import com.v.base.VBViewModel


/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/12 16:11
 */
class DataViewModel : VBViewModel() {

    var content = MutableLiveData<String>()

    fun setContent(s: String) {
        content.value = s
    }

}