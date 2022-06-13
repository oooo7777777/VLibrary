package com.v.base.dialog

import android.content.Context
import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.v.base.R
import com.v.base.annotaion.VBDialogOrientation
import com.v.base.databinding.VbDialogListBinding
import com.v.base.utils.vbDivider
import com.v.base.utils.vbLinear


/**
 * author  : ww
 * desc    : 列表提示框
 * time    : 2021-03-16 09:52:45
 */
class VBListDialog(mContext: Context) : VBDialog<VbDialogListBinding>(mContext) {

    private var list = ArrayList<String>()

    private var listener: ((dialog: VBListDialog, result: String, position: Int) -> Unit)? = null

    override fun useDirection(): VBDialogOrientation {
        return VBDialogOrientation.BOTTOM
    }

    private val mAdapter by lazy {
        mDataBinding.recyclerView.vbDivider {
        }.vbLinear(MyAdapter()) as MyAdapter
    }


    override fun initData() {
        mAdapter.setList(list)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            listener?.run {
                invoke(
                    this@VBListDialog,
                    mAdapter.getItem(position).toString(),
                    position
                )
            }
        }
    }


    fun setItems(vararg items: String): VBListDialog {
        items.forEach {
            this.list.add(it)
        }
        return this
    }

    fun setTitle(title: String): VBListDialog {
        if (!title.isNullOrEmpty()) {
            mDataBinding.layoutTitle.visibility = View.VISIBLE
            mDataBinding.tvTitle.text = title
        }
        return this
    }

    fun setClickListener(listener: ((dialog: VBListDialog, result: String, position: Int) -> Unit)): VBListDialog {
        this.listener = listener
        return this
    }


    class MyAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.vb_dialog_list_item) {
        init {
            addChildClickViewIds(R.id.tvContent)
        }

        override fun convert(helper: BaseViewHolder, item: String) {
            helper.setText(R.id.tvContent, item)
        }

    }


}