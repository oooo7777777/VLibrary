package com.v.base.utils.ext

/**
 * @Author : ww
 * desc    :
 * time    : 2021/4/26 9:23
 */

/**
 * 数据
 */
sealed class BooleanExt<out T>

object Otherwise : BooleanExt<Nothing>()
class WithData<T>(val data: T) : BooleanExt<T>()


/**
 * 判断条件为true 时执行block
 */
inline fun <T : Any> Boolean.yes(block: () -> T) =
    when {
        this -> {
            WithData(block())
        }
        else -> {
            Otherwise
        }
    }

/**
 * 判断条件为false 时执行block
 *
 */
inline fun <T> Boolean.no(block: () -> T) = when {
    this -> Otherwise
    else -> {
        WithData(block())
    }
}

/**
 * 与判断条件互斥时执行block
 */
inline fun <T> BooleanExt<T>.otherwise(block: () -> T): T =
    when (this) {
        is Otherwise -> block()
        is WithData -> this.data
    }
