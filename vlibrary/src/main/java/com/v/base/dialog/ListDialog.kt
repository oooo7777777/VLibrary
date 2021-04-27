package com.v.base.dialog

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.v.base.BlankViewModel
import com.v.base.R
import com.v.base.annotaion.DialogOrientation
import com.v.base.databinding.BaseDialogListBinding
import com.v.base.annotaion.RecyclerViewItemOrientation
import com.v.base.utils.ext.divider
import com.v.base.utils.ext.linear

/**
 * author  : ww
 * desc    : 列表提示框
 * time    : 2021-03-16 09:52:45
 */
class ListDialog : BaseDialogFragment<BaseDialogListBinding, BlankViewModel>() {

    private var list = ArrayList<String>()

    private var listener: ListDialogListener? = null

    override fun useDirection(): DialogOrientation {
        return DialogOrientation.BOTTOM
    }

    private val mAdapter by lazy {
        mViewBinding.recyclerView.divider {
            orientation = RecyclerViewItemOrientation.VERTICAL
        }.linear(MyAdapter()) as MyAdapter
    }


    override fun initData() {
        mAdapter.setList(list)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            listener?.run {
                onItem(
                    this@ListDialog,
                    mAdapter.getItem(position).toString(),
                    position
                )
            }
        }
    }

    override fun createObserver() {
    }

    fun setList(list: ArrayList<String>): ListDialog {
        this.list = list
        return this
    }

    fun setListDialogListener(listener: ListDialogListener): ListDialog {
        this.listener = listener
        return this
    }

    interface ListDialogListener {
        fun onItem(dialog: ListDialog, result: String, position: Int)
    }

    class MyAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.base_dialog_list_item) {
        init {
            addChildClickViewIds(R.id.tvContent)
        }

        override fun convert(helper: BaseViewHolder, item: String) {
            helper.setText(R.id.tvContent, item)
        }

    }


}