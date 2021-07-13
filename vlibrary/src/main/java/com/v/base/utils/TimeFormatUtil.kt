package com.v.base.utils

import java.text.SimpleDateFormat
import java.util.*


/**
 * 获取当前毫秒时间戳
 */
fun vbNowTimeMills(): Long {
    return System.currentTimeMillis()
}

/**
 * 获取当前时间戳转为时间字符串
 */
fun vbNowTimeString(): String {
    return System.currentTimeMillis().vbFormatTime()
}

/**
 * 将时间戳转为时间字符串*/
fun Long.vbFormatTime(pattern: String = "yyyy-MM-dd HH:mm:ss"): String =
    run {
        SimpleDateFormat(pattern, Locale.getDefault())
            .format(Date(this))
    }


/**
 * 将毫秒数换算成x天x时x分x秒x毫秒
 */
fun Long.vbFormatTimeYMD(): String =
    run {
        val ss = 1000
        val mi = ss * 60
        val hh = mi * 60
        val dd = hh * 24
        val day = this / dd
        val hour = (this - day * dd) / hh
        val minute = (this - day * dd - hour * hh) / mi
        val second = (this - day * dd - hour * hh - minute * mi) / ss
        val strDay = if (day < 10) "0$day" else "" + day
        val strHour = if (hour < 10) "0$hour" else "" + hour
        val strMinute = if (minute < 10) "0$minute" else "" + minute
        val strSecond = if (second < 10) "0$second" else "" + second
        var result = strDay + "天" + strHour + "时" + strMinute + "分" + strSecond + "秒"
        if ("00" == strDay) {
            result = strHour + "时" + strMinute + "分" + strSecond + "秒"
            if ("00" == strHour) {
                result = strMinute + "分" + strSecond + "秒"
            }
        }
        return result
    }


/**
 * 友好时间
 */
fun Long.vbFormatTimeFriendly(): String =
    run {
        //获取time距离当前的秒数
        val ct = ((System.currentTimeMillis() - this) / 1000).toInt()
        if (ct == 0) {
            return "刚刚"
        }
        if (ct in 1..59) {
            return ct.toString() + "秒前"
        }
        if (ct in 60..3599) {
            return Math.max(ct / 60, 1).toString() + "分钟前"
        }
        if (ct in 3600..86399) {
            return (ct / 3600).toString() + "小时前"
        }
        if (ct in 86400..2591999) { //86400 * 30
            val day = ct / 86400
            return day.toString() + "天前"
        }
        return if (ct in 2592000..31103999) { //86400 * 30
            (ct / 2592000).toString() + "个月前"
        } else (ct / 31104000).toString() + "年前"
    }

