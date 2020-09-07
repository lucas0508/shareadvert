package com.qujiali.shareadvert.module.other.view.viewmodel

import androidx.lifecycle.MutableLiveData
import com.qujiali.shareadvert.base.viewmodel.BaseViewModel
import com.qujiali.shareadvert.module.mine.repsitory.MineRepository
import com.qujiali.shareadvert.module.other.repsitroy.AccessFeedbackRepository
import com.qujiali.shareadvert.network.initiateRequest
import com.qujiali.shareadvert.network.response.BaseResponse
import com.qujiali.shareadvert.network.response.EmptyResponse

class AccessFeedBackModel  : BaseViewModel<AccessFeedbackRepository>(){


    var feedbackData: MutableLiveData<BaseResponse<EmptyResponse>> = MutableLiveData()


    fun loadFeedbackData(map: Map<String, Any>){
        initiateRequest({
            feedbackData.value=mRepository.onLoadFeedback(map)
        },loadState)
    }
}