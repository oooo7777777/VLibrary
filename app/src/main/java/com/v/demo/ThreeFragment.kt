package com.v.demo

import android.content.Intent
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.hjq.language.MultiLanguages
import com.v.base.VBApplication.Companion.getApplication
import com.v.base.VBBlankViewModel
import com.v.base.VBFragment
import com.v.base.dialog.VBHintDialog
import com.v.base.dialog.VBListDialog
import com.v.base.utils.goActivity
import com.v.base.utils.vbCountDownCoroutines
import com.v.base.utils.vbToast
import com.v.demo.bean.TestListBean
import com.v.demo.databinding.FragmentThreeBinding
import com.v.log.util.log
import kotlinx.coroutines.Job
import java.util.*


/**
 * @Author : ww
 * desc    :
 * time    : 2021/1/11 15:44
 */
class ThreeFragment : VBFragment<FragmentThreeBinding, VBBlankViewModel>(), View.OnClickListener {
    override fun viewModelSyn(): Boolean {
        return true
    }


    private var job: Job? = null
    private var countDown = 5L

    // 是否需要重启
    private var restart = false

    override fun initData() {
        mDataBinding.v = this

        mDataBinding.tvLanguageActivity.text =
            this.resources.getString(R.string.string_current_language)

        mDataBinding.tvLanguageApplication.text =
            getApplication().resources.getString(R.string.string_current_language)

        mDataBinding.tvLanguageSystem.text = MultiLanguages.getLanguageString(
            mContext,
            MultiLanguages.getSystemLanguage(),
            R.string.string_current_language
        )
    }

    override fun createObserver() {

    }

    override fun onClick(v: View) {
        when (v.id) {
            mDataBinding.bt1.id -> {
                countDown = 5
                countDownStart()
            }
            mDataBinding.bt2.id -> {
                countDown = Long.MAX_VALUE
                countDownStart()
            }

            mDataBinding.bt3.id -> {
                goActivity(NetworkActivity::class.java)
            }

            mDataBinding.bt4.id -> {
                VBHintDialog(mContext).setTitle("提示")
                    .setContent("确定保存吗?")
                    .setButtonText("取消", "确定")
                    .setClickListener { hintDialog, position ->
                        hintDialog.dismiss()
                    }
                    .show()
            }
            mDataBinding.bt5.id -> {

                val list = ArrayList<TestListBean>()
                repeat(3)
                {
                    list.add(TestListBean("content$it", it))
                }

                VBListDialog(mContext)
                    .setTitle("List")
                    .setList(list)
                    .setClickListener { dialog, item, position ->
                        val bean = item as TestListBean
                        "${bean.content}  ${bean.code}".vbToast()
                        dialog.dismiss()
                    }.show()
            }

            mDataBinding.bt6.id -> {
                // 中文
                restart = MultiLanguages.setAppLanguage(mContext, Locale.CHINA)
                languages()
            }
            mDataBinding.bt7.id -> {
                // 英语
                restart = MultiLanguages.setAppLanguage(mContext, Locale.ENGLISH)
                languages()
            }
            mDataBinding.bt8.id -> {
                // 日文
                restart = MultiLanguages.setAppLanguage(mContext, Locale.JAPAN)
                languages()
            }
            mDataBinding.bt9.id -> {
                //跟随系统
                restart = MultiLanguages.clearAppLanguage(mContext)
                languages()
            }
            mDataBinding.bt10.id -> {
                //韩语(没有配置这个语言,如果没有 则会拿去values里面配置的语言)
                restart = MultiLanguages.setAppLanguage(mContext, Locale.KOREAN)
                languages()
            }
        }
    }

    private fun languages() {
        // 1.使用recreate来重启Activity的效果差，有闪屏的缺陷
        // recreate();

        // 2.使用常规startActivity来重启Activity，有从左向右的切换动画，稍微比recreate的效果好一点，但是这种并不是最佳的效果
        // startActivity(new Intent(this, LanguageActivity.class));
        // finish();

        // 3.我们可以充分运用 Activity 跳转动画，在跳转的时候设置一个渐变的效果，相比前面的两种带来的体验是最佳的
        if (restart) {
            startActivity(Intent(mContext, MainActivity::class.java))
            mContext.finish()
        }
    }

    private fun countDownStart() {
        countDownStop()
        if (job == null) {
            job = vbCountDownCoroutines(
                countDown,
                onStart = {
                    "倒计时开始".log()
                },
                onTick = {
                    mDataBinding.tvContent.text = it.toString()
                    "正在倒计时$it".log()

                },
                onFinish = {
                    mDataBinding.tvContent.text = "倒计时结束"
                    "倒计时结束".log()

                },
                onCancel = {
                    "倒计时取消".log()
                },
                scope = lifecycleScope
            )
        }
    }

    private fun countDownStop() {
        if (job != null) {
            job?.cancel()
            job = null
        }
    }


}

