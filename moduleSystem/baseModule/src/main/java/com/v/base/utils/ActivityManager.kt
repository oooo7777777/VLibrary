package com.v.base.utils

import android.app.Activity
import android.content.Context
import java.util.*

/**
 * author  : ww
 * desc    : Activity管理
 * time    : 2021-03-16 09:52:45
 */
class ActivityManager private constructor()
{

    /**
     * 添加Activity到堆栈
     */
    fun addActivity(activity: Activity)
    {
        if (activityStack == null)
        {
            activityStack = Stack()
        }
        activityStack!!.add(activity)
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    fun currentActivity(): Activity
    {
        return activityStack!!.lastElement()
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    fun finishActivity()
    {
        var activity: Activity? = activityStack!!.lastElement()
        if (activity != null)
        {
            activity.finish()
            activity = null
        }
    }

    /**
     * 结束指定的Activity
     */
    fun finishActivity(activity: Activity?)
    {
        var activity = activity
        if (activity != null)
        {
            activityStack!!.remove(activity)
            activity.finish()
            activity = null
        }
    }

    /**
     * 结束指定类名的Activity
     */
    fun finishActivity(cls: Class<*>)
    {
        for (activity in activityStack!!)
        {
            if (activity.javaClass == cls)
            {
                finishActivity(activity)
            }
        }
    }

    /**
     * 结束所有Activity
     */
    fun finishAllActivity()
    {
        var i = 0
        val size = activityStack!!.size
        while (i < size)
        {
            if (null != activityStack!![i])
            {
                activityStack!![i].finish()
            }
            i++
        }
        activityStack!!.clear()
    }

    /**
     * 关闭几个Activity
     *
     * @param size
     */
    fun finishActivity(size: Int)
    {
        var number = 0
        if (activityStack == null) return
        val charArray = activityStack!!.toTypedArray()
        for (i in charArray.indices.reversed())
        {
            if (number != size)
            {
                val activity = activityStack!![i]
                activityStack!!.remove(activity)
                activity.finish()
                number++
            }
        }
    }

    /**
     * 退出应用程序
     */
    fun AppExit(context: Context)
    {
        try
        {
            finishAllActivity()
            val activityMgr = context.getSystemService(Context.ACTIVITY_SERVICE) as android.app.ActivityManager
            activityMgr.restartPackage(context.packageName)
            System.exit(0)
        } catch (e: Exception)
        {
        }

    }

    companion object
    {
        private var activityStack: Stack<Activity>? = null
        private var instance: ActivityManager? = null

        /**
         * 单一实例
         */
        val appManager: ActivityManager
            get()
            {
                if (instance == null)
                {
                    instance = ActivityManager()
                }
                return instance as ActivityManager
            }
    }
}