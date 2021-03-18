package com.zlzw.me.model

import androidx.lifecycle.MutableLiveData
import com.v.base.BaseViewModel
import com.v.base.utils.toList
import com.v.common.net.RetrofitManager
import com.zlzw.me.bean.MeBean

/**
 * author  :
 * desc    :
 * time    : 2021-03-03 17:53:52
 */
class MeViewModel : BaseViewModel() {

    var listBean = MutableLiveData<List<MeBean>>()

    fun login(username: String,password:String) {

        val map = mapOf(
            "username" to username,
            "password" to password
        )

        request({
            RetrofitManager.instance.post("http://apiv2.ttmb.aiboom.cn/itaoke.app.user.login", map)
        }, success = {
            listBean.value = it.toString().toList(MeBean::class.java)
        },dialog = true)

    }

}