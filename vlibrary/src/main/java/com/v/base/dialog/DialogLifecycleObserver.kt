package com.v.base.dialog

import androidx.lifecycle.*
import com.v.log.util.log

/**
 * author  : ww
 * desc    :
 * time    : 2023/4/23 18:23
 */
internal class DialogLifecycleObserver(private val dismiss: () -> Unit) : DefaultLifecycleObserver {
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        dismiss()
    }
}