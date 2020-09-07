package com.qujiali.shareadvert.common.state

import android.content.Context
import com.qujiali.shareadvert.common.state.callback.CollectListener
import com.qujiali.shareadvert.common.utils.startActivity
import com.qujiali.shareadvert.module.mine.view.PersonalActivity


interface UserState {

    fun login(context: Context)

    fun startPersonalActivity(context: Context)


//
//    fun startTodoActivity(context: Context)
//
//    fun startCollectActivity(context: Context)
//
//    fun startShareActivity(context: Context)
//
//    fun startAddShareActivity(context: Context)
//
//    fun startEditTodoActivity(context: Context)
}