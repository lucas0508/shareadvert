package com.qujiali.shareadvert.module.home.viewmodel

import androidx.lifecycle.MutableLiveData
import com.qujiali.shareadvert.base.viewmodel.BaseViewModel
import com.qujiali.shareadvert.module.banner.model.BannerResponse
import com.qujiali.shareadvert.module.home.model.CompanyDataResponse
import com.qujiali.shareadvert.module.home.model.NoticeModel
import com.qujiali.shareadvert.module.home.repository.HomeRepository
import com.qujiali.shareadvert.module.mine.model.UserInfoResponse
import com.qujiali.shareadvert.network.initiateRequest
import com.qujiali.shareadvert.network.response.BaseResponse
import com.qujiali.shareadvert.network.response.BaseResponseRow


class HomeViewModel :
    BaseViewModel<HomeRepository>() {


    // 使用协程 + Retrofit2.6以上版本
    var mBannerData: MutableLiveData<String> = MutableLiveData()
    var mCompanyDataList: MutableLiveData<List<CompanyDataResponse>> = MutableLiveData()
    var mNoticeDataList: MutableLiveData<BaseResponseRow<List<NoticeModel>>> = MutableLiveData()
    var mUserInfo: MutableLiveData<BaseResponse<UserInfoResponse>> = MutableLiveData()

    fun loadBannerCo(cityCode: String, type: String) {
        initiateRequest({
            mBannerData.value = mRepository.loadBannerCo(cityCode, type)
        }, loadState)
    }

    fun loadCompanyDataList(page: Int, cityCode: String, search: String) {
        initiateRequest({
            mCompanyDataList.value = mRepository.loadCompanyDataList(page, cityCode, search)
        }, loadState)
    }


    fun loadNoticeDataList() {
        initiateRequest({
            mNoticeDataList.value=mRepository.loadNoticeDataList()
        },loadState)
    }

    fun loadUserInfo() {
        initiateRequest(
            {
                mUserInfo.value = mRepository.onLoadUserInfo()
            }, loadState
        )
    }
}