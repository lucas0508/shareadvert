package com.qujiali.shareadvert.module.mine.repsitory

import androidx.lifecycle.MutableLiveData
import com.qujiali.shareadvert.base.repository.ApiRepository
import com.qujiali.shareadvert.base.repository.BaseRepository
import com.qujiali.shareadvert.common.state.State
import com.qujiali.shareadvert.module.mine.model.UserInfoResponse
import com.qujiali.shareadvert.network.dataConvert
import com.qujiali.shareadvert.network.response.BaseResponse
import com.qujiali.shareadvert.network.response.EmptyResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class MineRepository(private val loadState : MutableLiveData<State>): ApiRepository() {


    suspend fun onLoadUserInfo() : BaseResponse<UserInfoResponse>{
       return apiService.onLoadUserInfo()
    }

    suspend fun onModifyPersonData(map: Map<String, Any>): EmptyResponse{
        return apiService.onModifyPersonData(map).dataConvert(loadState)
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