package com.qujiali.shareadvert.module.home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.qujiali.shareadvert.base.viewmodel.BaseViewModel
import com.qujiali.shareadvert.module.banner.model.BannerResponse
import com.qujiali.shareadvert.module.home.model.CompanyDataResponse
import com.qujiali.shareadvert.module.home.repository.HomeDetailRepository
import com.qujiali.shareadvert.module.home.repository.HomeRepository
import com.qujiali.shareadvert.network.initiateRequest
import com.qujiali.shareadvert.network.response.EmptyResponse


class HomeDetailViewModel :
    BaseViewModel<HomeDetailRepository>() {

    // 使用协程 + Retrofit2.6以上版本
    var mCompanyData: MutableLiveData<CompanyDataResponse> = MutableLiveData()

    var resourceLookPhone: MutableLiveData<EmptyResponse> = MutableLiveData()

    val onQueryBanner: MutableLiveData<String> = MutableLiveData()

    fun loadCompanyData(companyId: Int?) {
        initiateRequest({
            mCompanyData.value = mRepository.loadCompanyData(companyId)
        }, loadState)
    }

    fun onQueryBanner(cityCode: String, type: String) {
        initiateRequest(
            { onQueryBanner.value = mRepository.onQueryBanner(cityCode, type) },
            loadState
        )
    }

    fun loadResourcesLookPhone(id: Int) {
        initiateRequest({
            resourceLookPhone.value = mRepository.onLoadResourcesLookPhone(id)
        }, loadState)
    }
}