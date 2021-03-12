package com.v.base.utils

import android.view.View
import com.v.base.R
import com.v.base.utils.ext.log

/**
 * @Author : ww
 * desc    :
 * time    : 2020/12/29 15:22
 */

class FastClickUtils {

     fun isInvalidClick(target: View, defaultTime: Long=500L): Boolean {
        val curTimeStamp = System.currentTimeMillis()
        var lastClickTimeStamp: Long = 0
        val o = target.getTag(R.id.invalid_click)
        if (o == null) {
            target.setTag(R.id.invalid_click, curTimeStamp)
            return false
        }
        lastClickTimeStamp = o as Long
        val isInvalid = curTimeStamp - lastClickTimeStamp < defaultTime
        if (!isInvalid) {
            target.setTag(R.id.invalid_click, curTimeStamp)
        }
        return isInvalid
    }

}