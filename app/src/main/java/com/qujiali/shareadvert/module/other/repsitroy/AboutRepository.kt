package com.qujiali.shareadvert.module.other.repsitroy

import androidx.lifecycle.MutableLiveData
import com.qujiali.shareadvert.base.repository.ApiRepository
import com.qujiali.shareadvert.common.state.State
import com.qujiali.shareadvert.module.other.model.VersionUpdateEntity
import com.qujiali.shareadvert.network.response.BaseResponse
import com.qujiali.shareadvert.network.response.EmptyResponse

class AboutRepository(private val loadState: MutableLiveData<State>) : ApiRepository(){

    suspend fun onUpdateVersion(): BaseResponse<VersionUpdateEntity> {
        return apiService.onUpdateVersion()
    }

}