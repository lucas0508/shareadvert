package com.qujiali.shareadvert.module.home.repository

import androidx.lifecycle.MutableLiveData
import com.qujiali.shareadvert.base.repository.ApiRepository
import com.qujiali.shareadvert.common.state.State
import com.qujiali.shareadvert.module.home.model.CompanyDataResponse
import com.qujiali.shareadvert.network.dataConvert
import com.qujiali.shareadvert.network.response.EmptyResponse


class HomeDetailRepository(private val loadState: MutableLiveData<State>) : ApiRepository(){


    suspend fun loadCompanyData(companyId: Int?): CompanyDataResponse {
        return apiService.onLoadCompanyData(companyId).dataConvert(loadState)
    }

    suspend fun onQueryBanner(cityCode: String, type: String): String {
        return apiService.onQueryBanner(cityCode, type).dataConvert(loadState)
    }

    suspend fun onLoadResourcesLookPhone(id: Int): EmptyResponse {
        return apiService.onLoadResourceLookPhone(id = id).dataConvert(loadState)
    }

}