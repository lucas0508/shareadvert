package com.qujiali.shareadvert.module.banner.repository

import androidx.lifecycle.MutableLiveData
import com.qujiali.shareadvert.base.repository.ApiRepository
import com.qujiali.shareadvert.common.state.State
import com.qujiali.shareadvert.module.banner.model.BannerResponse
import com.qujiali.shareadvert.network.dataConvert

class BannerRepository(private val loadState: MutableLiveData<State>) : ApiRepository() {


    // 使用协程 + Retrofit2.6
    suspend fun onQueryBanner(cityCode: String, type: String): String {
        return apiService.onQueryBanner(cityCode, type).dataConvert(loadState)
    }


}