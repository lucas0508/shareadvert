package com.qujiali.shareadvert.module.home.model
import com.google.gson.annotations.SerializedName
import com.qujiali.shareadvert.module.resources.model.ReMsg


data class CompanyDataResponse(
    @SerializedName("abbreviation")
    var abbreviation: String,
    @SerializedName("area")
    var area: String,
    @SerializedName("areaList")
    var areaList: List<Any>,
    @SerializedName("auditTime")
    var auditTime: Any,
    @SerializedName("businessLicenseImg")
    var businessLicenseImg: String,
    @SerializedName("companyId")
    var companyId: Int,
    @SerializedName("createBy")
    var createBy: String,
    @SerializedName("createTime")
    var createTime: String,
    @SerializedName("dataScope")
    var dataScope: Any,
    @SerializedName("enable")
    var enable: Int,
    @SerializedName("img")
    var img: String,
    @SerializedName("info")
    var info: String,
    @SerializedName("mainBusiness")
    var mainBusiness: String,
    @SerializedName("name")
    var name: String,
    @SerializedName("phone")
    var phone: String,
    @SerializedName("phoneHide")
    var phoneHide: Any,
    @SerializedName("profile")
    var profile: String,
    @SerializedName("refuseMsg")
    var refuseMsg: String,
    @SerializedName("remark")
    var remark: Any,
    @SerializedName("searchValue")
    var searchValue: Any,
    @SerializedName("status")
    var status: Int,
    @SerializedName("updateBy")
    var updateBy: Any,
    @SerializedName("updateTime")
    var updateTime: Any,
    @SerializedName("review")
    var review: String
)


