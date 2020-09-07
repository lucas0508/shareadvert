package com.qujiali.shareadvert.custom.dialog

import android.content.Context
import android.text.TextUtils
import com.qujiali.shareadvert.common.state.UserInfo

/**
 * @author QiZai
 * @desc
 * @date 2018/6/6 10:04
 */
class DialogManage(private val mContext: Context) {

    private var mLoadingDialog: LoadingDialog? = null
    private var mSelectPicDialog: SelectPicDialog? = null
    private var mLoginDialog: LoginDialog? = null
    private var mConfirmDialog:ConfirmDialog?=null

    var confirmAgreementDialog: ConfirmAgreementDialog? =
        null
        get() {
            if (field == null) field =
                com.qujiali.shareadvert.custom.dialog.ConfirmAgreementDialog(mContext)
            return field
        }

    open fun getSelectPicDialog(): SelectPicDialog? {
        if (mSelectPicDialog == null) mSelectPicDialog = SelectPicDialog(mContext)
        return mSelectPicDialog
    }

    /**
     * 判断用户是否登录，未登录弹出提醒
     *
     * @return 是否登录
     */
    fun isLoginToDialog(): Boolean? {
        var flag = false
        if (isLogin()) flag = isLogin()
        if (!flag) {
            getLoginDialog()?.show()
        }
        return flag
    }

    /**
     * 获取普通弹窗
     *
     * @return
     */
    fun getConfirmDialog(): ConfirmDialog? {
        if (mConfirmDialog == null) mConfirmDialog = ConfirmDialog(mContext)
        return mConfirmDialog
    }


    /**
     * 判断用户是否登录 - 无弹窗
     *
     * @return
     */
    fun isLogin(): Boolean {
        return !TextUtils.isEmpty(UserInfo.instance.appToken)
    }
    fun getLoginDialog(): LoginDialog? {
        if (mLoginDialog == null) mLoginDialog = LoginDialog(mContext)
        return mLoginDialog
    }

    /**
     * 获取loading弹窗
     *
     * @return
     */
    fun getLoadingDialog(): LoadingDialog? {
        if (mLoadingDialog == null) mLoadingDialog = LoadingDialog(mContext)
        return mLoadingDialog
    }
}