package com.qujiali.shareadvert.module.other.repsitroy

import androidx.lifecycle.MutableLiveData
import com.qujiali.shareadvert.base.repository.ApiRepository
import com.qujiali.shareadvert.common.state.State
import com.qujiali.shareadvert.module.mine.model.UserInfoResponse
import com.qujiali.shareadvert.network.response.BaseResponse
import com.qujiali.shareadvert.network.response.EmptyResponse

class AccessFeedbackRepository(private val loadState: MutableLiveData<State>) : ApiRepository() {


    suspend fun onLoadFeedback(map: Map<String, Any>): BaseResponse<EmptyResponse> {
        return apiService.onLoadFeedbackData(map)
    }


}