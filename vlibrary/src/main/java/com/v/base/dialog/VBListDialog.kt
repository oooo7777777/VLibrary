package com.v.base.dialog

import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.v.base.R
import com.v.base.annotaion.VBDialogOrientation
import com.v.base.bean.VBListBean
import com.v.base.databinding.VbDialogListBinding
import com.v.base.utils.vbDivider
import com.v.base.utils.vbLinear


/**
 * author  : ww
 * desc    : 列表提示框
 * time    : 2021-03-16 09:52:45
 */
class VBListDialog(mContext: AppCompatActivity) : VBDialog<VbDialogListBinding>(mContext) {

    private var list = ArrayList<VBListBean>()

    private var listener: ((dialog: VBListDialog, item: VBListBean, position: Int) -> Unit)? = null


    override fun useDirection(): VBDialogOrientation {
        return VBDialogOrientation.BOTTOM
    }

    private val mAdapter by lazy {
        mDataBinding.recyclerView.vbDivider {
            setDivider(1)
        }.vbLinear(MyAdapter()) as MyAdapter
    }


    override fun initData() {
        mAdapter.setList(list)
        mAdapter.setOnItemChildClickListener { adapter, view, position ->
            listener?.run {
                invoke(
                    this@VBListDialog,
                    mAdapter.getItem(position),
                    position
                )
            }
        }
    }


    fun setItems(vararg items: VBListBean): VBListDialog {
        items.forEach {
            this.list.add(it)
        }
        return this
    }

    fun setList(list: List<VBListBean>): VBListDialog {
        this.list.addAll(list)
        return this
    }

    fun setTitle(title: String): VBListDialog {
        if (!title.isNullOrEmpty()) {
            mDataBinding.line.visibility = View.VISIBLE
            mDataBinding.tvTitle.visibility = View.VISIBLE
            mDataBinding.tvTitle.text = title
        }
        return this
    }

    fun setClickListener(listener: ((dialog: VBListDialog, item: VBListBean, position: Int) -> Unit)): VBListDialog {
        this.listener = listener
        return this
    }


    override fun useDim(): Boolean {
        return false
    }


    class MyAdapter : BaseQuickAdapter<VBListBean, BaseViewHolder>(R.layout.vb_dialog_list_item) {
        init {
            addChildClickViewIds(R.id.tvContent)
        }

        override fun convert(helper: BaseViewHolder, item: VBListBean) {
            helper.setText(R.id.tvContent, item.content)
        }

    }


}