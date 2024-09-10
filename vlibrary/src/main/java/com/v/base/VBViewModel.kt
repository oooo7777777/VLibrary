package com.v.base

import androidx.lifecycle.ViewModel
import com.kunminx.architecture.ui.callback.UnPeekLiveData

abstract class VBViewModel : ViewModel() {

    val loadingChange: UiLoadingChange by lazy { UiLoadingChange() }

    inner class UiLoadingChange {
        val showDialog by lazy { UnPeekLiveData<String>() }
        val dismissDialog by lazy { UnPeekLiveData<Boolean>() }
        val showToast by lazy { UnPeekLiveData<String>() }
    }
}




