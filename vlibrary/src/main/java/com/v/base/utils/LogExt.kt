package com.v.base.utils

//import android.util.Log
//import com.android.jesse.log.ALog
//import com.android.jesse.log.stragety.CsvFormatStrategy
//import com.android.jesse.log.util.LogUtils
//import com.orhanobut.logger.Logger
//import com.v.base.VBApplication
//
//const val TAG = "PRETTY_LOGGER"
//
//enum class LEVEL {
//    V, D, I, W, E
//}
//
//fun Any.logV(tag: String = TAG, save: Boolean = true) =
//    log(
//        LEVEL.V,
//        tag,
//        this.toString(),
//        save
//    )
//
//fun Any.logD(tag: String = TAG, save: Boolean = true) =
//    log(
//        LEVEL.D,
//        tag,
//        this.toString(),
//        save
//    )
//
//fun Any.logI(tag: String = TAG, save: Boolean = true) =
//    log(
//        LEVEL.I,
//        tag,
//        this.toString(),
//        save
//    )
//
//fun Any.logW(tag: String = TAG, save: Boolean = true) =
//    log(
//        LEVEL.W,
//        tag,
//        this.toString(),
//        save
//    )
//
//fun Any.logE(tag: String = TAG, save: Boolean = true) =
//    log(
//        LEVEL.E,
//        tag,
//        this.toString(),
//        save
//    )
//
//fun Any.log(tag: String = TAG, level: LEVEL = LEVEL.I, save: Boolean = true) = run {
//    if (VBApplication.isLog()) {
//        when (level) {
//            LEVEL.V -> {
//                Log.v(tag, this.toString())
//                if (save)
//                    ALog.v(tag, this.toString())
//            }
//            LEVEL.D -> {
//                Log.d(tag, this.toString())
//                if (save)
//                    ALog.d(tag, this.toString())
//            }
//            LEVEL.I -> {
//                val builder = StringBuilder()
//
//                val classAndMethodName = LogUtils.getClassAndMethodName()
//                if (classAndMethodName.first != null) {
//                    builder.append(",")
//                    builder.append(classAndMethodName.first)
//                }
//                if (classAndMethodName.second != null) {
//                    builder.append(",")
//                    builder.append(classAndMethodName.second)
//                }
//                builder.append(",")
//                builder.append(this.toString())
//                Log.i(tag, this.toString())
////                if (save)
////                    ALog.i(tag, this.toString())
//            }
//            LEVEL.W -> {
//                Log.w(tag, this.toString())
//                if (save)
//                    ALog.w(tag, this.toString())
//            }
//            LEVEL.E -> {
//                Log.e(tag, this.toString())
//                if (save)
//                    ALog.e(tag, this.toString())
//            }
//        }
//
//    }
//}
//
//fun logCurrentThreadName(tag: String = TAG, save: Boolean = true) = run {
//    if (VBApplication.isLog()) {
//        log(LEVEL.I, tag, Thread.currentThread().name, save)
//    }
//}
//
//
//private fun log(level: LEVEL, tag: String, message: String, save: Boolean = true) {
//    //tag最长为70 不然会打印不出来
//    var tagFormat = tag
//    if (tag.length > 70) {
//        tagFormat = tag.substring(0, 70)
//    }
//
//    when (level) {
//        LEVEL.V -> {
//            Logger.t(tagFormat).v(message)
//            if (save)
//                ALog.v(tagFormat, message)
//        }
//        LEVEL.D -> {
//            Logger.t(tagFormat).d(message)
//            if (save)
//                ALog.d(tagFormat, message)
//        }
//        LEVEL.I -> {
//            Logger.t(tagFormat).i(message)
//            if (save)
//                ALog.i(tagFormat, message)
//        }
//        LEVEL.W -> {
//            Logger.t(tagFormat).w(message)
//            if (save)
//                ALog.w(tagFormat, message)
//        }
//        LEVEL.E -> {
//            Logger.t(tagFormat).e(message)
//            if (save)
//                ALog.e(tagFormat, message)
//        }
//    }
//
//}