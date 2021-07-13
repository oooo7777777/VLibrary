package com.v.demo.model

import androidx.lifecycle.MutableLiveData
import com.v.base.VBViewModel

/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/27 14:55
 */
class AppViewModel: VBViewModel() {

    var string = MutableLiveData<String>()
}