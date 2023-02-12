package com.v.base.utils

import android.util.Log
import com.orhanobut.logger.Logger
import com.v.base.VBApplication

const val TAG = "PRETTY_LOGGER"

enum class LEVEL {
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

fun Any.log(tag: String = TAG, level: LEVEL = LEVEL.I) = run {
    if (VBApplication.isLog()) {
        when (level) {
            LEVEL.V -> Log.v(tag, this.toString())
            LEVEL.D -> Log.d(tag, this.toString())
            LEVEL.I -> Log.i(tag, this.toString())
            LEVEL.W -> Log.w(tag, this.toString())
            LEVEL.E -> Log.e(tag, this.toString())
        }

    }
}

fun logCurrentThreadName(tag: String = TAG) = run {
    if (VBApplication.isLog()) {
        Log.i(tag, Thread.currentThread().name)
    }
}


private fun log(level: LEVEL, tag: String, message: String) {
    //tag最长为70 不然会打印不出来
    var tagFormat = tag
    if (tag.length > 70) {
        tagFormat = tag.substring(0, 70)
    }

    when (level) {
        LEVEL.V -> Logger.t(tagFormat).v(message)
        LEVEL.D -> Logger.t(tagFormat).d(message)
        LEVEL.I -> Logger.t(tagFormat).i(message)
        LEVEL.W -> Logger.t(tagFormat).w(message)
        LEVEL.E -> Logger.t(tagFormat).e(message)
    }


}