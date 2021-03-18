package com.v.base.utils

import androidx.lifecycle.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList


object LiveDataBus {

    //一个事件对应一个LiveData
    private val eventMap = ConcurrentHashMap<String, StickLiveData<*>>()

    //一个事件对应多个页面
    private val lifecycleMap =
        ConcurrentHashMap<String, CopyOnWriteArrayList<LifecycleOwner>>()

    //一个页面对应多个事件
    private val eventTypeMap =
        ConcurrentHashMap<LifecycleOwner, CopyOnWriteArrayList<String>>()

    //获取对应事件的LiveData
    fun <T> with(eventName: String): StickLiveData<T> {
        var liveData = eventMap[eventName]

        if (liveData == null) {
            liveData = StickLiveData<T>(eventName)
            eventMap[eventName] = liveData
        }
        return liveData as StickLiveData<T>
    }

    //获取对应事件关联的页面集合
    private fun getLifecycleOwners(eventName: String): CopyOnWriteArrayList<LifecycleOwner> {
        var lifecycleOwners = lifecycleMap[eventName]
        if (lifecycleOwners == null) {
            lifecycleOwners = CopyOnWriteArrayList<LifecycleOwner>()
            lifecycleMap[eventName] = lifecycleOwners
        }
        return lifecycleOwners
    }

    //添加事件对应的页面
    private fun addLifecycleOwner(eventName: String, lifecycleOwner: LifecycleOwner) {
        val lifecycleOwners = getLifecycleOwners(eventName)
        if (!lifecycleOwners.contains(lifecycleOwner)) {
            lifecycleOwners.add(lifecycleOwner)
        }
    }

    //获取每一个页面对应的事件集合
    private fun getEventTypeList(lifecycleOwner: LifecycleOwner): CopyOnWriteArrayList<String> {
        var eventTypeList = eventTypeMap[lifecycleOwner]
        if (eventTypeList == null) {
            eventTypeList = CopyOnWriteArrayList()
            eventTypeMap[lifecycleOwner] = eventTypeList
        }
        return eventTypeList
    }

    //为页面添加对应的事件
    private fun addEventType(eventName: String, lifecycleOwner: LifecycleOwner) {
        val eventTypeList = getEventTypeList(lifecycleOwner)
        if (!eventTypeList.contains(eventName)) {
            eventTypeList.add(eventName)
        }
    }

    class StickLiveData<T>(private val eventName: String) : LiveData<T>() {

        internal var mStickData: T? = null
        internal var mVersion = 0

        fun setData(data: T) {
            setValue(data)
        }

        fun postData(data: T) {
            postValue(data)
        }

        fun setStickData(data: T) {
            mStickData = data
            setValue(data)
        }

        fun postStickData(data: T) {
            mStickData = data
            postValue(data)
        }

        override fun setValue(value: T) {
            mVersion++
            super.setValue(value)
        }

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            observeStick(owner, observer, false)
        }

        private fun observeStick(
            owner: LifecycleOwner,
            observer: Observer<in T>,
            stick: Boolean = true
        ) {
            super.observe(owner, StickWarpObserver(this, observer, stick))
            addLifecycleOwner(eventName, owner)
            addEventType(eventName, owner)
            owner.lifecycle.addObserver(LifecycleEventObserver { source, event ->
                if (event == Lifecycle.Event.ON_DESTROY) {
                    //页面销毁时，在该事件对应的页面集合中移除该页面
                    getLifecycleOwners(eventName).remove(source)
                    //页面销毁时，获取该页面包含的事件集合，遍历判断每一种事件类型对应的页面集合是否为空，若为空，则移除该事件
                    getEventTypeList(owner).forEach { eventType ->
                        if (getLifecycleOwners(eventType).isEmpty()) {
                            eventMap.remove(eventType)
                        }
                    }
                }
            })
        }
    }

    //装饰者模式对原先的Observer进行包装
    class StickWarpObserver<T>(
        private val stickLiveData: StickLiveData<T>,
        private val observer: Observer<in T>,
        private val stick: Boolean//是否支持黏性事件
    ) : Observer<T> {

        //创建Observer时mLastVersion的默认赋值为LiveData的Version,规避黏性事件
        private var mLastVersion = stickLiveData.mVersion

        override fun onChanged(t: T) {
            if (mLastVersion >= stickLiveData.mVersion) {
                if (stick && stickLiveData.mStickData != null) {
                    observer.onChanged(t)
                }
                return
            }
            mLastVersion = stickLiveData.mVersion
            observer.onChanged(t)
        }
    }
}