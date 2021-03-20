package com.v.base.utils


import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.alibaba.fastjson.serializer.SerializerFeature


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
fun  Any.toJson(): String =
    run {
        JSON.toJSONString(this@toJson, *features)
    }

/**
 * json字符串转化为map
 */
fun String.toMap(): Map<*, *> =
    run {
        JSONObject.parseObject(this)
    }

/**
 * 将map转化为string
 */
fun Map<*, *>.toJson(): String =
    run {
        JSONObject.toJSONString(this)
    }

/**
 * JSON转对象
 */
fun <T> String.toBean(cls: Class<T>): T =
    run {
        if (!this.startsWith("{") && !this.endsWith("}")) {
            JSON.parseObject("{$this}", cls)
        } else {
            JSON.parseObject(this, cls)
        }
    }

/**
 * JSON转List
 */
fun <T> String.toList(cls: Class<T>): List<T> =
    run {
        var list: List<T> = ArrayList()
        list = if (!this.startsWith("[") && !this.endsWith("]")) {
            JSON.parseArray("[$this]", cls)
        } else {
            JSON.parseArray(this, cls)
        }
        return list
    }

