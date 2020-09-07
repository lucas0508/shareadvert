package com.qujiali.shareadvert.module.resources.repository

import androidx.lifecycle.MutableLiveData
import com.qujiali.shareadvert.base.repository.ApiRepository
import com.qujiali.shareadvert.common.state.State
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
import okhttp3.RequestBody.Companion.create
import java.io.File


class SettleinRepository(private var loadState: MutableLiveData<State>) : ApiRepository() {


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


    suspend fun onCompanySettleIn(map: Map<String, Any>): BaseResponse<EmptyDataResponse> {
        return apiService.onCompanySettleIn(map)
    }


    suspend fun loadCompanyData(companyId: Int?): CompanyDataResponse {
        return apiService.onLoadCompanyData(companyId).dataConvert(loadState)
    }

    suspend fun loadCompanyRemove(companyId: Int):EmptyResponse{
        return apiService.onRemoveCompany(companyId).dataConvert(loadState)
    }
}


