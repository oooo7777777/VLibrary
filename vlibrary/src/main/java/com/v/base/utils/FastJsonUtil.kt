package com.v.base.utils


import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.serializer.SerializerFeature
import java.lang.reflect.Type


private val features = arrayOf(
    SerializerFeature.WriteMapNullValue, // 输出空置字段
    SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
    SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
    SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
    SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
)

/**
 * 对象转JSON
 */
fun Any.vbToJson(): String =
    run {
        JSON.toJSONString(this@vbToJson, *features)
    }

/**
 * json字符串转化为map
 */
fun String.vbToMap(): Map<*, *> =
    run {
        JSONObject.parseObject(this)
    }

/**
 * 将map转化为string
 */
fun Map<*, *>.vbToJson(): String =
    run {
        JSONObject.toJSONString(this)
    }

/**
 * JSON转对象
 */
fun <T> String.vbToBean(cls: Class<T>): T =
    run {
        if (!this.startsWith("{") && !this.endsWith("}")) {
            JSON.parseObject("{$this}", cls)
        } else {
            JSON.parseObject(this, cls)
        }
    }

/**
 * JSON转对象
 */
fun <T> String.vbToBean(type: Type): T =
    run {
        if (!this.startsWith("{") && !this.endsWith("}")) {
            JSON.parseObject("{$this}", type)
        } else {
            JSON.parseObject(this, type)
        }
    }

/**
 * JSON转List
 */
fun <T> String.vbToList(cls: Class<T>): List<T> =
    run {
        var list: List<T> = ArrayList()
        list = if (!this.startsWith("[") && !this.endsWith("]")) {
            JSON.parseArray("[$this]", cls)
        } else {
            JSON.parseArray(this, cls)
        }
        return list
    }

