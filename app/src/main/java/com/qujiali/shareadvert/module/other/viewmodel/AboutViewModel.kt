package com.qujiali.shareadvert.module.other.viewmodel

import androidx.lifecycle.MutableLiveData
import com.qujiali.shareadvert.base.viewmodel.BaseViewModel
import com.qujiali.shareadvert.module.other.model.VersionUpdateEntity
import com.qujiali.shareadvert.module.other.repsitroy.AboutRepository
import com.qujiali.shareadvert.module.other.repsitroy.AccessFeedbackRepository
import com.qujiali.shareadvert.network.initiateRequest
import com.qujiali.shareadvert.network.response.BaseResponse
import com.qujiali.shareadvert.network.response.EmptyResponse

class AboutViewModel: BaseViewModel<AboutRepository>() {

    var updataversion: MutableLiveData<BaseResponse<VersionUpdateEntity>> = MutableLiveData()


    fun loadUpdateVersion(){
        initiateRequest({
            updataversion.value=mRepository.onUpdateVersion()
        },loadState)
    }
}