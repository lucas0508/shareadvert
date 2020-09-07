package com.qujiali.shareadvert.common.state

import android.content.Context
import com.qujiali.shareadvert.common.state.callback.CollectListener
import com.qujiali.shareadvert.common.utils.Constant.KEY_IMGPATH
import com.qujiali.shareadvert.common.utils.Constant.KEY_USERNAME
import com.qujiali.shareadvert.common.utils.startActivity
import com.qujiali.shareadvert.module.mine.view.PersonalActivity


class LoginState : UserState {


    override fun login(context: Context) {}

    override fun startPersonalActivity(context: Context) {
        startActivity<PersonalActivity>(context){
        }
    }

//
//    override fun startCollectActivity(context: Context) {
//        startActivity<CollectArticleListActivity>(context)
//    }
//
//    override fun startShareActivity(context: Context) {
//        startActivity<MeShareActivity>(context)
//    }
//
//    override fun startAddShareActivity(context: Context) {
//        startActivity<ShareArticleActivity>(context)
//    }
//
//    override fun startTodoActivity(context: Context) {
//        startActivity<TodoActivity>(context)
//    }
//
//    override fun startEditTodoActivity(context: Context) {
//        startActivity<EditTodoActivity>(context) {
//            putExtra(Constant.KEY_TODO_HANDLE_TYPE, Constant.ADD_TODO)
//        }
//    }
}