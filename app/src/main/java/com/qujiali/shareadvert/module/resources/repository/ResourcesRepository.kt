package com.qujiali.shareadvert.module.resources.repository

import androidx.lifecycle.MutableLiveData
import com.qujiali.shareadvert.base.repository.ApiRepository
import com.qujiali.shareadvert.common.state.State
import com.qujiali.shareadvert.module.banner.model.BannerResponse
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

class ResourcesRepository(private var loadState: MutableLiveData<State>) : ApiRepository() {

    suspend fun loadResourcesLt(pageNum: Int): List<ResourcesResponse> {
        return apiService.onLoadResourceList(pageNum = pageNum).dataConvert(loadState)
    }


    suspend fun loadResourceAnonList(
        page: Int,
        area: String,
        search: String
    ): List<ResourcesResponse> {
        return apiService.onLoadResourceAnonList(page, area,search).dataConvert(loadState)
    }


    suspend fun onQueryBanner(cityCode: String, type: String): String {
        return apiService.onQueryBanner(cityCode, type).dataConvert(loadState)
    }


    suspend fun loadResourcesAdd(map: Map<String, Any>): BaseResponse<EmptyDataResponse> {
        return apiService.onLoadResourcesData(map)
    }

    suspend fun loadResourcesSingleData(id: Int): BaseResponse<ResourcesResponse> {
        return apiService.onLoadResourcesSingleData(id)
    }


    suspend fun onLoadResourcesOffline(id: Int): EmptyResponse {
        return apiService.onLoadResourcesOffline(id)
    }

    suspend fun onLoadResourcesLookPhone(id: Int): EmptyResponse {
        return apiService.onLoadResourceLookPhone(id = id).dataConvert(loadState)
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