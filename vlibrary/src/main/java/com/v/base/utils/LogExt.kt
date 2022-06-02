package com.v.base.utils

import android.util.Log
import com.orhanobut.logger.Logger
import com.v.base.VBApplication

const val TAG = "PRETTY_LOGGER"

private enum class LEVEL {
    V, D, I, W, E
}

fun Any.logV(tag: String = TAG) =
    log(
        LEVEL.V,
        tag,
        this.toString()
    )

fun Any.logD(tag: String = TAG) =
    log(
        LEVEL.D,
        tag,
        this.toString()
    )

fun Any.logI(tag: String = TAG) =
    log(
        LEVEL.I,
        tag,
        this.toString()
    )

fun Any.logW(tag: String = TAG) =
    log(
        LEVEL.W,
        tag,
        this.toString()
    )

fun Any.logE(tag: String = TAG) =
    log(
        LEVEL.E,
        tag,
        this.toString()
    )

fun Any.log(tag: String = TAG) = run {
    if (VBApplication.isLog()) {
        Log.i(tag, this.toString())
    }
}


private fun log(level: LEVEL, tag: String, message: String) {

    when (level) {
        LEVEL.V -> Logger.t(tag).v(message)
        LEVEL.D -> Logger.t(tag).d(message)
        LEVEL.I -> Logger.t(tag).i(message)
        LEVEL.W -> Logger.t(tag).w(message)
        LEVEL.E -> Logger.t(tag).e(message)
    }

}