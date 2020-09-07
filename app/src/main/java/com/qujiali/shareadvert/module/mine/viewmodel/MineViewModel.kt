package com.qujiali.shareadvert.module.mine.viewmodel

import androidx.lifecycle.MutableLiveData
import com.qujiali.shareadvert.base.viewmodel.BaseViewModel
import com.qujiali.shareadvert.module.mine.model.UserInfoResponse
import com.qujiali.shareadvert.module.mine.repsitory.MineRepository
import com.qujiali.shareadvert.network.initiateRequest
import com.qujiali.shareadvert.network.response.BaseResponse
import com.qujiali.shareadvert.network.response.EmptyResponse

class MineViewModel : BaseViewModel<MineRepository>() {


    var mUserInfo: MutableLiveData<BaseResponse<UserInfoResponse>> = MutableLiveData()

    var mModifyData: MutableLiveData<EmptyResponse> = MutableLiveData()

    var uploadFile: MutableLiveData<String> = MutableLiveData()

    fun loadUserInfo() {
        initiateRequest(
            {
                mUserInfo.value = mRepository.onLoadUserInfo()
            }, loadState
        )
    }

    fun loadModifyPersonData(map: Map<String, Any>){
        initiateRequest({
            mModifyData.value=mRepository.onModifyPersonData(map)
        },loadState)
    }


    fun uploadFile(mFilePath: String) {
        initiateRequest({
            uploadFile.value = mRepository.onUploadFile(mFilePath)
        }, loadState)
    }
}