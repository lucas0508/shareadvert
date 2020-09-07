package com.qujiali.shareadvert.module.demand.viewmodel

import androidx.lifecycle.MutableLiveData
import com.qujiali.shareadvert.base.viewmodel.BaseViewModel
import com.qujiali.shareadvert.module.banner.model.BannerResponse
import com.qujiali.shareadvert.module.demand.repository.DemandRepository
import com.qujiali.shareadvert.module.resources.model.ResourcesResponse
import com.qujiali.shareadvert.network.initiateRequest
import com.qujiali.shareadvert.network.response.BaseResponse
import com.qujiali.shareadvert.network.response.EmptyDataResponse
import com.qujiali.shareadvert.network.response.EmptyResponse

class HomeFragmentDemandModel : BaseViewModel<DemandRepository>() {

    var uploadFiles: MutableLiveData<MutableList<String>> = MutableLiveData()

    var uploadFile: MutableLiveData<String> = MutableLiveData()

    var demandList: MutableLiveData<List<ResourcesResponse>> = MutableLiveData()

    var demandSingleData: MutableLiveData<BaseResponse<ResourcesResponse>> = MutableLiveData()

    var demandData: MutableLiveData<BaseResponse<EmptyDataResponse>> = MutableLiveData()

    var demandLt: MutableLiveData<List<ResourcesResponse>> = MutableLiveData()

    var demandOffline: MutableLiveData<EmptyResponse> = MutableLiveData()

    var demandLookPhone: MutableLiveData<EmptyResponse> = MutableLiveData()

    val onQueryBanner: MutableLiveData<String> = MutableLiveData()


    fun onQueryBanner(cityCode: String, type: String) {
        initiateRequest({onQueryBanner.value = mRepository.onQueryBanner(cityCode, type)}, loadState)
    }
    /**
     * 查询要求列表
     */
    fun onLoadDemandAnonList(pageNum: Int, area: String, search: String) {
        initiateRequest({
            demandList.value = mRepository.onLoadDemandAnonList(pageNum, area, search)
        }, loadState)
    }

    /**
     * 查询需求详情页
     */
    fun loadDemandSingleData(id: Int) {
        initiateRequest({
            demandSingleData.value = mRepository.loadDemandSingleData(id)
        }, loadState)
    }


    /**
     * 新增需求
     */
    fun loadDemandAdd(map: Map<String, Any>) {
        initiateRequest({
            demandData.value = mRepository.loadDemandData(map)
        }, loadState)
    }

    /**
     * 查询列表
     */
    fun loadDemandLt(pageNum: Int) {
        initiateRequest({
            demandLt.value = mRepository.loadDemandLt(pageNum)
        }, loadState)
    }

    fun onLoadDemandOfflineOffline(resourcesId: Int) {
        initiateRequest({
            demandOffline.value = mRepository.onLoadDemandOffline(resourcesId)
        }, loadState)
    }

    fun loadDemandLookPhone(id: Int) {
        initiateRequest({
            demandLookPhone.value = mRepository.onLoadDemandLookPhone(id)
        }, loadState)
    }


    /**
     * 上传图片
     */
    fun uploadFiles(mFilePath: MutableList<String>) {
        initiateRequest({
            uploadFiles.value = mRepository.onUploadFiles(mFilePath)
        }, loadState)
    }

    fun uploadFile(mFilePath: String) {
        initiateRequest({
            uploadFile.value = mRepository.onUploadFile(mFilePath)
        }, loadState)
    }
}