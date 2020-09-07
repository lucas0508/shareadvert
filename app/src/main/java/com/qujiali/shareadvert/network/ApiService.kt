package com.qujiali.shareadvert.network

import com.qujiali.shareadvert.module.home.model.CompanyDataResponse
import com.qujiali.shareadvert.module.home.model.NoticeModel
import com.qujiali.shareadvert.module.mine.model.UserInfoResponse
import com.qujiali.shareadvert.module.other.model.VersionUpdateEntity
import com.qujiali.shareadvert.module.resources.model.ResourcesResponse
import com.qujiali.shareadvert.network.response.BaseResponse
import com.qujiali.shareadvert.network.response.BaseResponseRow
import com.qujiali.shareadvert.network.response.EmptyDataResponse
import com.qujiali.shareadvert.network.response.EmptyResponse
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {

    /**
     * Banner
     */
    @GET("/advertiseIndustryMobile/anon/banner/")
    suspend fun onQueryBanner(
        @Query("cityCode") cityCode: String,
        @Query("type") type: String
    ): BaseResponse<String>


    /**http://192.168.1.142
     * 个人信息
     */
    @GET("/advertiseIndustryMobile/system/mobileUser/info")
    suspend fun onLoadUserInfo(): BaseResponse<UserInfoResponse>

    /**
     * 短信验证码
     */
    @GET("/advertiseIndustryMobile/anon/verify/send")
    suspend fun onSendMsg(@Query("phone") phone: String): BaseResponse<String>


    /**
     * 手机号登录
     */
    @POST("/advertiseIndustryMobile/anon/mobileLogin")
    suspend fun onLoginMobile(
        @Body map: Map<String, String>
    ): BaseResponse<String>

    /**
     * 单个文件上传
     */
    @POST("/advertiseIndustryMobile/image/upload")
    suspend fun onUploadFile(
        @Body requestBody: RequestBody
    ): BaseResponse<String>

    /**
     * 多个文件上传
     */
    @POST("/advertiseIndustryMobile/image/multiUpload")
    suspend fun onUploadFiles(
        @Body requestBody: RequestBody
    ): BaseResponse<MutableList<String>>

    /**
     * 企业入驻
     */
    @POST("/advertiseIndustryMobile/system/company/add")
    suspend fun onCompanySettleIn(
        @Body map: @JvmSuppressWildcards Map<String, Any>
    ): BaseResponse<EmptyDataResponse>

    /**
     * 首页公司列表数据
     */
    @GET("/advertiseIndustryMobile/anon/company/list")
    suspend fun onLoadCompanyDataList(
        @Query("pageNum") pageNum: Int,
        @Query("area") serviceArea: String,
        @Query("searchValue") search: String
    ): BaseResponseRow<List<CompanyDataResponse>>

    /**
     * 首页公司详情数据
     */
    @GET("/advertiseIndustryMobile/anon/company")
    suspend fun onLoadCompanyData(
        @Query("companyId") id: Int?
    ): BaseResponse<CompanyDataResponse>


    /**
     * 查询我的页面资源
     */
    @GET("/advertiseIndustryMobile/system/resource/list")
    suspend fun onLoadResourceList(
        @Query("pageNum") pageNum: Int
    ): BaseResponseRow<List<ResourcesResponse>>


    /**
     * 查询首页资源列表
     */
    @GET("/advertiseIndustryMobile/anon/resource/list")
    suspend fun onLoadResourceAnonList(
        @Query("pageNum") pageNum: Int,
        @Query("area") serviceArea: String,
        @Query("searchValue") search: String
    ): BaseResponseRow<List<ResourcesResponse>>


    /**
     * 新增资源
     */
    @POST("/advertiseIndustryMobile/system/resource/add")
    suspend fun onLoadResourcesData(
        @Body map: @JvmSuppressWildcards Map<String, Any>
    ): BaseResponse<EmptyDataResponse>

    /**
     * 查询单个资源
     */
    @GET("/advertiseIndustryMobile/anon/resource/details/{resourceId}")
    suspend fun onLoadResourcesSingleData(
        @Path("resourceId") id: Int
    ): BaseResponse<ResourcesResponse>


    /**
     * 下线单个资源
     */
    @GET("/advertiseIndustryMobile/system/resource/out/{resourceId}")
    suspend fun onLoadResourcesOffline(
        @Path("resourceId") id: Int
    ): EmptyResponse



    /**
     * 需求列表
     */
    @GET("/advertiseIndustryMobile/anon/demand/list")
    suspend fun onLoadDemandAnonList(
        @Query("pageNum") pageNum: Int,
        @Query("location") serviceArea: String,
        @Query("searchValue") search: String
    ): BaseResponseRow<List<ResourcesResponse>>


    /**
     * 查询详情
     */
    @GET("/advertiseIndustryMobile/anon/demand/{demandId}")
    suspend fun onLoadDemandSingleData(
        @Path("demandId") id: Int
    ): BaseResponse<ResourcesResponse>


    /**
     * 新增需求
     */
    @POST("/advertiseIndustryMobile/system/demand/add")
    suspend fun onLoadDemandData(
        @Body map: @JvmSuppressWildcards Map<String, Any>
    ): BaseResponse<EmptyDataResponse>


    /**
     * 我的页面 我的需求列表
     */
    @GET("/advertiseIndustryMobile/system/demand/list")
    suspend fun onLoadDemandList(
        @Query("pageNum") pageNum: Int
    ): BaseResponseRow<List<ResourcesResponse>>

    /**
     * 需求下线
     */

    @GET("/advertiseIndustryMobile/system/demand/out/{demandId}")
    suspend fun onLoadDemandOffline(
        @Path("demandId") id: Int
    ): BaseResponse<EmptyResponse>


    /**
     * 查看需求手机号
     */
    @GET("/advertiseIndustryMobile/anon/resource/call/{resourceId}")
    suspend fun onLoadResourceLookPhone(
        @Path("resourceId") id: Int
    ):BaseResponse<EmptyResponse>


    /**
     * 查看资源手机号
     */
    @GET("/advertiseIndustryMobile/anon/demand/call/{demandId}")
    suspend fun onLoadDemandLookPhone(
        @Path("demandId") id: Int
    ):BaseResponse<EmptyResponse>


    /**
     * 注销公司
     *
     */
    @DELETE("/advertiseIndustryMobile/system/company/remove/{id}")
    suspend fun onRemoveCompany(
        @Path("id") id: Int
    ):BaseResponse<EmptyResponse>


    /**
     * 修改个人信息
     */
    @PUT("/advertiseIndustryMobile/system/mobileUser")
    suspend fun onModifyPersonData(
        @Body map: @JvmSuppressWildcards Map<String, Any>
    ):BaseResponse<EmptyResponse>

    /**
     * 意见反馈
     */
    @POST("/advertiseIndustryMobile/anon/feedback")
    suspend fun onLoadFeedbackData(
        @Body map: @JvmSuppressWildcards Map<String, Any>
    ):BaseResponse<EmptyResponse>

    /**
     * 版本升级
     */
    @GET("/advertiseIndustryMobile/anon/version")
    suspend fun onUpdateVersion():BaseResponse<VersionUpdateEntity>


    /**
     * 通知
     */
    @GET("/advertiseIndustryMobile/anon/notice/list")
    suspend fun onLoadNoticeData():BaseResponseRow<List<NoticeModel>>

}