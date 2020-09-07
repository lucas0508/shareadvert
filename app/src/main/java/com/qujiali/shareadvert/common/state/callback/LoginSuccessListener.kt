package com.qujiali.shareadvert.common.state.callback


interface LoginSuccessListener {
    fun loginSuccess(userName : String, userId : String, collectArticleIds : List<Int>?)
}