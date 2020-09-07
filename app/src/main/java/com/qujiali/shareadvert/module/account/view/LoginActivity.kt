package com.qujiali.shareadvert.module.account.view

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import androidx.lifecycle.Observer
import com.orhanobut.logger.Logger
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.base.view.BaseLifeCycleActivity
import com.qujiali.shareadvert.common.state.UserInfo
import com.qujiali.shareadvert.common.utils.CommonUtil
import com.qujiali.shareadvert.common.utils.Constant
import com.qujiali.shareadvert.common.utils.SPreference
import com.qujiali.shareadvert.module.account.viewmodel.LoginViewModel
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import java.util.*

class LoginActivity : BaseLifeCycleActivity<LoginViewModel>(), View.OnClickListener {
    //  private var appToken: String by SPreference(Constant.SP_APP_TOKEN, "")

    override fun getLayoutId(): Int = R.layout.activity_login

    private var uuid: String? = ""

    private var mTimer: Timer? = null
    private var mTimerTask: TimerTask? = null
    private var counter = 0 // 计数器
    fun getCounter(): Int { // 获取计数时间
        return counter
    }

    fun setCounter(counter: Int) { // 设置计数器数值，并反馈给用户
        if (counter <= 0) {
            b_send_validate_code.setEnabled(true)
            b_send_validate_code.setText("重新发送")
            mTimer!!.cancel()
            mTimer = null
            mTimerTask = null
        } else {
            b_send_validate_code.setText(counter.toString() + "秒")
        }
        this.counter = counter
    }

    override fun initView() {
        super.initView()
        toolbar.setNavigationIcon(R.mipmap.icon_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbarTextView.text = "登录"
        b_send_validate_code.setOnClickListener(this)
        b_login.setOnClickListener(this)
        initPhoneEditText()
        showSuccess()
    }

    override fun initDataObserver() {
        mViewModel.msgData.observe(this, Observer {
            if (!TextUtils.isEmpty(it)) {
                uuid = it
                CommonUtil.showToast(this, "验证码已发送，请留意您的手机！")
                setCounter(120)
                if (mTimer == null) mTimer = Timer()
                if (mTimerTask == null) mTimerTask = object : TimerTask() {
                    override fun run() {
                        runOnUiThread(Runnable {
                            if (getCounter() <= 0) {
                                mTimer!!.cancel()
                                mTimerTask!!.cancel()
                                mTimer = null
                                mTimerTask = null
                            }
                            setCounter(getCounter() - 1)
                        })
                    }
                }
                mTimer!!.schedule(mTimerTask, 1000, 1000)
            }
        })

        mViewModel.loginData.observe(this, Observer {
            it.let {
//                if(it!=null){
//                    appToken = "Bearer " + it
//                }
                Logger.d("登录1日志：" + it)
                UserInfo.instance.loginSuccess(it)
                Logger.d("登录2日志：" + it)
                finish()
                Logger.d("登录3日志：" + it)
                CommonUtil.showToast(this, "登录成功~")
            }
        })

    }

    private fun initPhoneEditText() {
        et_phone.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                charSequence: CharSequence,
                i: Int,
                i1: Int,
                i2: Int
            ) {
            }

            override fun onTextChanged(
                charSequence: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
                if (charSequence.isEmpty()) {
                    return
                }
                val stringBuilder = StringBuilder()
                for (i in 0 until charSequence.length) {
                    if (i != 3 && i != 8 && charSequence[i] == ' ') {
                        continue
                    } else {
                        stringBuilder.append(charSequence[i])
                        if ((stringBuilder.length == 4 || stringBuilder.length == 9)
                            && stringBuilder[stringBuilder.length - 1] != ' '
                        ) {
                            stringBuilder.insert(stringBuilder.length - 1, ' ')
                        }
                    }
                }
                if (stringBuilder.toString() != charSequence.toString()) {
                    var index = start + 1
                    if (stringBuilder[start] == ' ') {
                        if (before == 0) {
                            index++
                        } else {
                            index--
                        }
                    } else {
                        if (before == 1) {
                            index--
                        }
                    }
                    et_phone.setText(stringBuilder.toString())
                    et_phone.setSelection(index)
                }
            }

            override fun afterTextChanged(editable: Editable) {}
        })
    }

//    override fun showCreateReveal(): Boolean = true
//
//    override fun showDestroyReveal(): Boolean = false

    override fun onBackPressed() = finish()


    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.b_send_validate_code -> {
                mViewModel.onSendMsg(et_phone.getText().toString().replace(" ".toRegex(), ""))
            }
            R.id.b_login -> {
                mViewModel.onLogin(
                    et_validate_code.text.toString(),
                    et_phone.getText().toString().replace(" ".toRegex(), ""),
                    uuid!!
                )
            }
            R.id.tv_login_agreement_user_agreement -> {
                CommonUtil.startWebView(this, Constant.AGREEMENT_URL, "用户协议")
            }
            R.id.tv_login_agreement_privacyPolicy -> {
                CommonUtil.startWebView(this, Constant.PRIVACYPOLICY_URL, "隐私政策")
            }
        }
    }
}

