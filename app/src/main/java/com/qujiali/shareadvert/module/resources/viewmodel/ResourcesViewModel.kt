package com.qujiali.shareadvert.module.resources.viewmodel

import androidx.lifecycle.MutableLiveData
import com.orhanobut.logger.Logger
import com.qujiali.shareadvert.base.viewmodel.BaseViewModel
import com.qujiali.shareadvert.module.banner.model.BannerResponse
import com.qujiali.shareadvert.module.resources.model.ResourcesResponse
import com.qujiali.shareadvert.module.resources.repository.ResourcesRepository
import com.qujiali.shareadvert.network.initiateRequest
import com.qujiali.shareadvert.network.response.BaseResponse
import com.qujiali.shareadvert.network.response.EmptyDataResponse
import com.qujiali.shareadvert.network.response.EmptyResponse

class ResourcesViewModel : BaseViewModel<ResourcesRepository>() {

    var uploadFiles: MutableLiveData<MutableList<String>> = MutableLiveData()
    var uploadFile: MutableLiveData<String> = MutableLiveData()


    var resourcesLt: MutableLiveData<List<ResourcesResponse>> = MutableLiveData()

    var resourcesData: MutableLiveData<BaseResponse<EmptyDataResponse>> =  MutableLiveData()

    var resourcesSingleData: MutableLiveData<BaseResponse<ResourcesResponse>> = MutableLiveData()

    var resourcesOffline: MutableLiveData<EmptyResponse> = MutableLiveData()

    var resourceAnonList: MutableLiveData<List<ResourcesResponse>> = MutableLiveData()

    var resourceLookPhone: MutableLiveData<EmptyResponse> = MutableLiveData()


    val onQueryBanner: MutableLiveData<String> = MutableLiveData()


    fun onQueryBanner(cityCode: String, type: String) {
        initiateRequest(
            { onQueryBanner.value = mRepository.onQueryBanner(cityCode, type) },
            loadState
        )
    }

    /**
     * 查询首页资源列表
     */

    fun loadResourceAnonList(pageNum: Int, area: String, search: String) {
        initiateRequest({
            resourceAnonList.value = mRepository.loadResourceAnonList(pageNum, area, search)
        }, loadState)
    }


    /**
     * 查询列表
     */

    fun loadResourcesLt(pageNum: Int) {
        initiateRequest({
            resourcesLt.value = mRepository.loadResourcesLt(pageNum)
        }, loadState)
    }

    /**
     * 新增资源
     */
    fun loadResourcesAdd(map: Map<String, Any>) {
        initiateRequest({
            resourcesData.value = mRepository.loadResourcesAdd(map)
        }, loadState)
    }

    fun loadResourcesSingleData(resourcesId: Int) {
        initiateRequest({
            resourcesSingleData.value = mRepository.loadResourcesSingleData(resourcesId)
        }, loadState)
    }


    fun onLoadResourcesOffline(resourcesId: Int) {
        initiateRequest({
            resourcesOffline.value = mRepository.onLoadResourcesOffline(resourcesId)
        }, loadState)
    }

    fun loadResourcesLookPhone(id: Int) {
        initiateRequest({
            resourceLookPhone.value = mRepository.onLoadResourcesLookPhone(id)
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