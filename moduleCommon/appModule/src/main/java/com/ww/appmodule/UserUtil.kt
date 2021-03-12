package com.ww.appmodule

import android.text.TextUtils
import com.v.base.bean.UserBean
import com.v.base.utils.SPUtil
import com.v.base.utils.ext.toBean


object UserUtil {

    private var token = ""
    private var userBean: UserBean? = null

    private val SP_TOKEN = "SP_TOKEN"
    private val SP_USER_INFO = "SP_USER_INFO"
    private fun emptyData() {
        token = ""
        SPUtil.instance.remove(SP_TOKEN)
    }

    fun isLogIn(): Boolean {
        return !(TextUtils.isEmpty(getToken()))
    }

    fun setUserInfo(json: String) {
        json.toBean(UserBean::class.java)?.run {
            SPUtil.instance.put(SP_USER_INFO, json)
        }

    }

    fun getUserBean(): UserBean {
        if (userBean == null) {
            SPUtil.instance.getString(SP_USER_INFO, "")?.run {
                userBean = this.toBean(UserBean::class.java)
            }
        }
        return userBean!!
    }

    fun getToken(): String {
        if (token.isNullOrEmpty()) {
            token = SPUtil.instance.getString(SP_TOKEN, "")
        }
        return token
    }

    fun setToken(s: String) {
        token = s
        SPUtil.instance.put(SP_TOKEN, token)
    }


}