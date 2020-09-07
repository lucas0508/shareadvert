package com.qujiali.shareadvert.common.state

import android.app.Activity
import android.content.Context
import com.qujiali.shareadvert.common.state.callback.CollectListener
import com.qujiali.shareadvert.common.state.callback.LoginSuccessState
import com.qujiali.shareadvert.common.utils.Constant
import com.qujiali.shareadvert.common.utils.SPreference
import com.qujiali.shareadvert.common.utils.startActivity
import com.qujiali.shareadvert.module.mine.view.PersonalActivity

class UserInfo private constructor() {

     var appToken: String by SPreference(Constant.SP_APP_TOKEN, "")

    var isLogin: Boolean by SPreference(Constant.LOGIN_KEY, false)


    // 设置默认状态
    var mState: UserState = if (isLogin) LoginState() else LogoutState()

    companion object {
        val instance =
            Holder.INSTANCE
    }

    // 内部类 单利
    private object Holder {
        val INSTANCE = UserInfo()
    }

    // 跳转去登录
    fun login(context: Activity) {
        mState.login(context)
    }

     fun startPersonalActivity(context: Context) {
         mState.startPersonalActivity(context)
    }
    fun loginSuccess(token: String) {
        // 改变 sharedPreferences   isLogin值
        isLogin = true
        instance.mState = LoginState()
        appToken = "Bearer " + token
        //LoginSuccessState.notifyLoginState(username, userId, collectIds)
    }

    fun logoutSuccess() {
        instance.mState = LogoutState()
        // 清除 cookie、登录缓存
        var mCookie by SPreference("cookie", "")
        mCookie = ""
        isLogin = false
        appToken=""
        //LoginSuccessState.notifyLoginState("未登录", "--", null)
    }
}