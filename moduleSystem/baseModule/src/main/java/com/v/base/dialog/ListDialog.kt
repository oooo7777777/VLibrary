package com.v.base.dialog

import androidx.recyclerview.widget.LinearLayoutManager
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.v.base.BlankViewModel
import com.v.base.R
import com.v.base.databinding.BaseDialogListBinding
import com.v.base.views.LineItemDecoration

class ListDialog : BaseDialogFragment<BaseDialogListBinding, BlankViewModel>() {

    private var list = ArrayList<String>()

    private var listener: ListDialogListener? = null

    override fun useDirection(): Int {
        return DIRECTION_BOTTOM
    }


    override fun initData() {

        var mAdapter = MyAdapter(list)
        mViewBinding.recyclerView.adapter = mAdapter
        mViewBinding.recyclerView.layoutManager = LinearLayoutManager(context)
        val decoration = LineItemDecoration(LineItemDecoration.Type.VERTICAL, 1f)
        mViewBinding.recyclerView.addItemDecoration(decoration)

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

    class MyAdapter(list: List<String>) : BaseQuickAdapter<String, BaseViewHolder>(
        R.layout.base_dialog_list_item,
        list.toMutableList()
    ) {
        init {
            addChildClickViewIds(R.id.tvContent)
        }

        override fun convert(helper: BaseViewHolder, item: String) {
            helper.setText(R.id.tvContent, item)
        }

    }


}