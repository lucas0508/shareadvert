package com.qujiali.shareadvert.module.demand.repository

import androidx.lifecycle.MutableLiveData
import com.qujiali.shareadvert.base.repository.ApiRepository
import com.qujiali.shareadvert.common.state.State
import com.qujiali.shareadvert.module.banner.model.BannerResponse
import com.qujiali.shareadvert.module.home.model.CompanyDataResponse
import com.qujiali.shareadvert.module.resources.model.ResourcesResponse
import com.qujiali.shareadvert.network.dataConvert
import com.qujiali.shareadvert.network.response.BaseResponse
import com.qujiali.shareadvert.network.response.EmptyDataResponse
import com.qujiali.shareadvert.network.response.EmptyResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class DemandRepository(private val loadState: MutableLiveData<State>) : ApiRepository() {

    suspend fun onLoadDemandAnonList(
        page: Int,
        area: String,
        search: String
    ): List<ResourcesResponse> {
        return apiService.onLoadDemandAnonList(page, area, search).dataConvert(loadState)
    }

    suspend fun loadDemandSingleData(id: Int): BaseResponse<ResourcesResponse> {
        return apiService.onLoadDemandSingleData(id)
    }


    suspend fun loadDemandData(map: Map<String, Any>):BaseResponse<EmptyDataResponse>  {
        return apiService.onLoadDemandData(map)
    }


    suspend fun loadDemandLt(pageNum: Int): List<ResourcesResponse> {
        return apiService.onLoadDemandList(pageNum = pageNum).dataConvert(loadState)
    }

    suspend fun onLoadDemandOffline(id: Int): EmptyResponse {
        return apiService.onLoadDemandOffline(id).dataConvert(loadState)
    }

    suspend fun onLoadDemandLookPhone(id: Int): EmptyResponse {
        return apiService.onLoadDemandLookPhone(id = id).dataConvert(loadState)
    }

    suspend fun onQueryBanner(cityCode: String, type: String): String {
        return apiService.onQueryBanner(cityCode, type).dataConvert(loadState)
    }


    suspend fun onUploadFiles(fileList: MutableList<String>): MutableList<String> {
        val builder =
            MultipartBody.Builder().setType(MultipartBody.FORM)
        val j = 0
        for (i in fileList.indices) {
            val file = File(fileList[i])
            builder.addFormDataPart(
                "shareadvert_file",
                file.name,
                file.asRequestBody("image/jpg".toMediaTypeOrNull())
            )
        }
        builder.addFormDataPart("imagetype", "multipart/form-data").build()
        val requestBody = builder.build()

        return apiService.onUploadFiles(requestBody).dataConvert(loadState)
    }

    suspend fun onUploadFile(mFilePath: String): String {

        val file = File(mFilePath) //mImagePath为上传的文件绝对路径
        //构建body
        val requestBody: RequestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart(
                "shareadvert_file",
                file.name,
                file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
            )
            .build()
        return apiService.onUploadFile(requestBody).dataConvert(loadState)
    }


}