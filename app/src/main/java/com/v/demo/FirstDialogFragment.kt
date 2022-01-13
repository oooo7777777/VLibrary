package com.v.demo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.v.base.utils.ext.log

/**
 * author  : ww
 * desc    :
 * time    : 2022/1/12 8:23 下午
 */
class FirstDialogFragment : DialogFragment() {
    private var result = "第一次"
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        //确定Dialog布局
        "onCreateView".log()
        result.log()
        return inflater.inflate(R.layout.view_tab, container, false)
    }

    override fun onAttach(context: Context) {
        "onAttach".log()
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        "onCreate".log()
        super.onCreate(savedInstanceState)
    }

    override fun onStart() {
        "onStart".log()
        super.onStart()
    }

    override fun onResume() {
        "onResume".log()
        super.onResume()
    }

    override fun onPause() {
        "onPause".log()
        result = "第二次"
        super.onPause()
    }

    override fun onDestroy() {
        "onDestroy".log()
        super.onDestroy()
    }

    override fun onDestroyView() {
        "onDestroyView".log()
        super.onDestroyView()
    }

    override fun onDetach() {
        "onDetach".log()
        super.onDetach()
    }
}