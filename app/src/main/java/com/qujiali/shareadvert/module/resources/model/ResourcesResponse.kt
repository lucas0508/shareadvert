package com.qujiali.shareadvert.module.resources.model
import com.google.gson.annotations.SerializedName



data class ResourcesResponse(
    @SerializedName("area")
    var area: String,
    @SerializedName("areaList")
    var areaList: List<Any>,
    @SerializedName("auditTime")
    var auditTime: String,
    @SerializedName("callTimes")
    var callTimes: Int,
    @SerializedName("createBy")
    var createBy: String,
    @SerializedName("createTime")
    var createTime: String,
    @SerializedName("enable")
    var enable: Int,
    @SerializedName("img")
    var img: String,
    @SerializedName("info")
    var info: String,
    @SerializedName("initialTime")
    var initialTime: String,
    @SerializedName("keyword")
    var keyword: String,
    @SerializedName("phone")
    var phone: String,
    @SerializedName("phoneHide")
    var phoneHide: String,
    @SerializedName("reMsg")
    var reMsg: List<ReMsg>,
    @SerializedName("resourceId")
    var resourceId: Int,
    @SerializedName("state")
    var state: Int,
    @SerializedName("status")
    var status: Int,
    @SerializedName("title")
    var title: String,
    @SerializedName("updateBy")
    var updateBy: Any,
    @SerializedName("userDetails")
    var userDetails: UserDetails,
    @SerializedName("userRole")
    var userRole: String,
    @SerializedName("viewTimes")
    var viewTimes: Int,
    @SerializedName("location")
    var location:String,
    @SerializedName("demandId")
    var demandId:Int

)

data class ReMsg(
    @SerializedName("auditTime")
    var auditTime: String,
    @SerializedName("msg")
    var msg: String
)

data class UserDetails(
    @SerializedName("name")
    var name: String,
    @SerializedName("profile")
    var profile: String,
    @SerializedName("userId")
    var userId: Int
)