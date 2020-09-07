package com.qujiali.shareadvert.module.account.viewmodel

import android.text.TextUtils
import androidx.lifecycle.MutableLiveData
import com.qujiali.shareadvert.base.view.BaseApplication
import com.qujiali.shareadvert.base.viewmodel.BaseViewModel
import com.qujiali.shareadvert.module.account.model.LoginResponse
import com.qujiali.shareadvert.module.account.repository.LoginRepository
import com.qujiali.shareadvert.network.initiateRequest
import org.jetbrains.anko.toast


class LoginViewModel : BaseViewModel<LoginRepository>() {


    var msgData: MutableLiveData<String> = MutableLiveData()


    var loginData: MutableLiveData<String> = MutableLiveData()


    fun onSendMsg(phone: String) {
        if (TextUtils.isEmpty(phone)) {
            BaseApplication.instance.toast("请输入正确的手机号！")
            return
        }
        initiateRequest({
            msgData.value = mRepository.onSendMsg(phone)
        }, loadState)
    }

    fun onLogin(pwd: String, username: String, uuid: String) {
        if (TextUtils.isEmpty(username)) {
            BaseApplication.instance.toast("请输入正确的手机号！")
            return
        }
        if (TextUtils.isEmpty(pwd)) {
            BaseApplication.instance.toast("请输入正确的验证码！")
            return
        }
        initiateRequest({
            val map= mapOf("code" to pwd, "username" to username, "uuid" to uuid)
            loginData.value = mRepository.onLogin(map)
        }, loadState)
    }


}