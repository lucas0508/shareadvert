package com.qujiali.shareadvert.module.other.view

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.base.view.BaseLifeCycleActivity
import com.qujiali.shareadvert.common.utils.CommonUtil.showToast
import com.qujiali.shareadvert.module.other.view.viewmodel.AccessFeedBackModel
import kotlinx.android.synthetic.main.activity_access_feedback.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.util.*

/**
 * 问题反馈
 */
class AccessFeedbackActivity : BaseLifeCycleActivity<AccessFeedBackModel>() {
    private val mFormData: MutableMap<String, Any> =
        HashMap()

    override fun getLayoutId(): Int {
        return R.layout.activity_access_feedback
    }

    override fun initView() {
        super.initView()

        toolbar.setNavigationIcon(R.mipmap.icon_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbarTextView.text = "问题反馈"
        mSubmit.setOnClickListener(View.OnClickListener { v: View? -> submitFormData() })
        mContent.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                val content = mContent.getText().toString()
                mNumber.setText(
                    "(" + content.length + "/"
                            + MAX_LENGTH + ")"
                )
            }
        })

        showSuccess()
    }

    private fun submitFormData() {
        val content = mContent!!.text.toString().trim { it <= ' ' }
        if (content.isEmpty() || content.length <= 5) {
            showToast(this, "反馈详情不能为空且必须大于5个字符！")
            return
        }
        val chatType = et_chat_type!!.text.toString().trim { it <= ' ' }
        //!ValidateUtils.validatePhone(chatType) && !ValidateUtils.validateEmail(chatType)
        if (TextUtils.isEmpty(chatType)) {
            showToast(this,"联系方式格式不正确！")
            return
        }
        //        mFormData.put("type", mTypeName);
        mFormData["content"] = content
        mFormData["contactWay"] = chatType
        submit()
    }

    private fun submit() {
        mViewModel.loadFeedbackData(mFormData)
    }

    override fun initDataObserver() {
        super.initDataObserver()
        mViewModel.feedbackData.observe(this, androidx.lifecycle.Observer {
            it.let {
                if (it.code==200){
                    showToast(this,"感谢您的反馈，您的反馈将是我们不断进步的动力！")
                    finish()
                } else {
                    showToast(this,it.msg)
                }
            }
        })
    }

    companion object {
        private const val MAX_LENGTH = "120"
    }


    override fun onBackPressed() = finish()


    override fun showCreateReveal(): Boolean = false

    override fun showDestroyReveal(): Boolean = false
}