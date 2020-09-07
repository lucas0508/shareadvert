package com.qujiali.shareadvert.module.home.repository

import androidx.lifecycle.MutableLiveData
import com.qujiali.shareadvert.base.repository.ApiRepository
import com.qujiali.shareadvert.common.state.State
import com.qujiali.shareadvert.module.banner.model.BannerResponse
import com.qujiali.shareadvert.module.home.model.CompanyDataResponse
import com.qujiali.shareadvert.module.home.model.NoticeModel
import com.qujiali.shareadvert.module.mine.model.UserInfoResponse
import com.qujiali.shareadvert.network.dataConvert
import com.qujiali.shareadvert.network.response.BaseResponse
import com.qujiali.shareadvert.network.response.BaseResponseRow


class HomeRepository(private val loadState: MutableLiveData<State>) : ApiRepository() {


    // 使用协程 + Retrofit2.6
    suspend fun loadBannerCo(cityCode: String, type: String): String {
        return apiService.onQueryBanner(cityCode, type).dataConvert(loadState)
    }

    suspend fun loadCompanyDataList(page: Int, cityCode: String,search:String): List<CompanyDataResponse> {
        return apiService.onLoadCompanyDataList(page, cityCode,search).dataConvert(loadState)
    }

    suspend fun loadNoticeDataList():BaseResponseRow<List<NoticeModel>>{
        return apiService.onLoadNoticeData()
    }

    suspend fun onLoadUserInfo() : BaseResponse<UserInfoResponse> {
        return apiService.onLoadUserInfo()
    }
}