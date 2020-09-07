package com.qujiali.shareadvert.common.state

import android.content.Context
import com.qujiali.shareadvert.common.utils.startActivity
import com.qujiali.shareadvert.module.account.view.LoginActivity
import com.qujiali.shareadvert.module.mine.view.PersonalActivity


class LogoutState : UserState {


    override fun login(context: Context) {
        startLoginActivity(context)
    }

    override fun startPersonalActivity(context: Context) {
        startActivity<PersonalActivity>(context){
        }
    }


//    override fun startRankActivity(context: Context) {
//        startLoginActivity(context)
//    }
//
//    override fun startCollectActivity(context: Context) {
//        startLoginActivity(context)
//    }
//
//    override fun startShareActivity(context: Context) {
//        startLoginActivity(context)
//    }
//
//    override fun startAddShareActivity(context: Context) {
//        startLoginActivity(context)
//    }
//
//    override fun startTodoActivity(context: Context) {
//        startLoginActivity(context)
//    }
//

//
//    override fun startEditTodoActivity(context: Context) {
//        startLoginActivity(context)
//    }


    private fun startLoginActivity(context: Context) {
        context?.let {
            //it.toast(it.getString(R.string.please_login))
            startActivity<LoginActivity>(it)
        }
    }
}