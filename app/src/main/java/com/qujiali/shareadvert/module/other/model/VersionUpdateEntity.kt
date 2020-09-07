package com.qujiali.shareadvert.module.other.model
import com.google.gson.annotations.SerializedName


data class VersionUpdateEntity(
    @SerializedName("createBy")
    var createBy: Any,
    @SerializedName("createTime")
    var createTime: String,
    @SerializedName("downloadUrl")
    var downloadUrl: String,
    @SerializedName("enable")
    var enable: Int,
    @SerializedName("id")
    var id: Long,
    @SerializedName("keyword")
    var keyword: String,
    @SerializedName("largeVersion")
    var largeVersion: String,
    @SerializedName("minorVersion")
    var minorVersion: String,
    @SerializedName("orderBy")
    var orderBy: String,
    @SerializedName("remark")
    var remark: String,
    @SerializedName("status")
    var status: String,
    @SerializedName("updateBy")
    var updateBy: Any,
    @SerializedName("updateTime")
    var updateTime: String
)