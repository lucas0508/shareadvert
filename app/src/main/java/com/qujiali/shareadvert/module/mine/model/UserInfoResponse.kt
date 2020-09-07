package com.qujiali.shareadvert.module.mine.model

import com.google.gson.annotations.SerializedName


data class UserInfoResponse(

    @SerializedName("birthday")
    var birthday: Any,
    @SerializedName("companyId")
    var companyId: Int,
    @SerializedName("userRole")
    var userRole: String,
    @SerializedName("createBy")
    var createBy: Any,
    @SerializedName("createTime")
    var createTime: String,
    @SerializedName("dataScope")
    var dataScope: Any,
    @SerializedName("delFlag")
    var delFlag: Int,
    @SerializedName("enabled")
    var enabled: Int,
    @SerializedName("id")
    var id: Int,
    @SerializedName("idNumber")
    var idNumber: Any,
    @SerializedName("loginDate")
    var loginDate: Any,
    @SerializedName("loginIp")
    var loginIp: Any,
    @SerializedName("name")
    var name: String,
    @SerializedName("nickname")
    var nickname: Any,
    @SerializedName("params")
    var params: Params,
    @SerializedName("password")
    var password: Any,
    @SerializedName("phone")
    var phone: String,
    @SerializedName("profile")
    var profile: String,
    @SerializedName("remark")
    var remark: Any,
    @SerializedName("remarks")
    var remarks: Any,
    @SerializedName("searchValue")
    var searchValue: Any,
    @SerializedName("sex")
    var sex: Int,
    @SerializedName("updateBy")
    var updateBy: Any,
    @SerializedName("updateTime")
    var updateTime: Any,
    @SerializedName("dayNumber")
    var dayNumber: String
)

class Params(
)