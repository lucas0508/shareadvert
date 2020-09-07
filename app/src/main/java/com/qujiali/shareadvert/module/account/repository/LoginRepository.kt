package com.qujiali.shareadvert.module.account.repository

import androidx.lifecycle.MutableLiveData
import com.qujiali.shareadvert.base.repository.ApiRepository
import com.qujiali.shareadvert.common.state.State
import com.qujiali.shareadvert.module.account.model.LoginResponse
import com.qujiali.shareadvert.network.dataConvert

class LoginRepository(private val loadState: MutableLiveData<State>) : ApiRepository() {


    suspend fun onSendMsg(phone: String): String {
        return apiService.onSendMsg(phone).dataConvert(loadState)
    }

    suspend fun onLogin(map: Map<String, String>): String {
        return apiService.onLoginMobile(map).dataConvert(loadState)
    }

}