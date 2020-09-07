package com.qujiali.shareadvert.module.settlein.viewmodel

import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import com.qujiali.shareadvert.base.viewmodel.BaseViewModel
import com.qujiali.shareadvert.module.home.model.CompanyDataResponse
import com.qujiali.shareadvert.module.resources.model.ResourcesResponse
import com.qujiali.shareadvert.module.resources.repository.ResourcesRepository
import com.qujiali.shareadvert.module.resources.repository.SettleinRepository
import com.qujiali.shareadvert.network.initiateRequest
import com.qujiali.shareadvert.network.response.BaseResponse
import com.qujiali.shareadvert.network.response.EmptyDataResponse
import com.qujiali.shareadvert.network.response.EmptyResponse

class SettleinViewModel : BaseViewModel<SettleinRepository>() {

    var uploadFile: MutableLiveData<String> = MutableLiveData()
    var uploadFiles: MutableLiveData<MutableList<String>> = MutableLiveData()
    var onCompanySettleIn: MutableLiveData<BaseResponse<EmptyDataResponse>> = MutableLiveData()
//    var onCompanySettleIn: MutableLiveData<BaseResponse<String>> = MutableLiveData()
    var mCompanyData: MutableLiveData<CompanyDataResponse> = MutableLiveData()
    var mCompanyRemove: MutableLiveData<EmptyResponse> = MutableLiveData()
    fun uploadFile(mFilePath: String) {
        initiateRequest({
            uploadFile.value = mRepository.onUploadFile(mFilePath)
        }, loadState)
    }


    fun loadCompanyData() {
        initiateRequest({
            mCompanyData.value = mRepository.loadCompanyData(null)
        }, loadState)
    }

    fun uploadFiles(mFilePath: MutableList<String>) {
        initiateRequest({
            uploadFiles.value = mRepository.onUploadFiles(mFilePath)
        }, loadState)
    }

    fun onCompanySettleIn(map: Map<String, Any>){
        initiateRequest({
            onCompanySettleIn.value = mRepository.onCompanySettleIn(map)
        }, loadState)
    }

    fun loadCompanyRemove(companyId: Int) {
        initiateRequest({
            mCompanyRemove.value = mRepository.loadCompanyRemove(companyId)
        }, loadState)
    }
}