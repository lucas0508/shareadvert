package com.qujiali.shareadvert.module.home.model
import com.google.gson.annotations.SerializedName


data class NoticeModel(
    @SerializedName("createBy")
    var createBy: String,
    @SerializedName("createTime")
    var createTime: String,
    @SerializedName("dataScope")
    var dataScope: Any,
    @SerializedName("noticeContent")
    var noticeContent: String,
    @SerializedName("noticeId")
    var noticeId: Int,
    @SerializedName("noticeTitle")
    var noticeTitle: String,
    @SerializedName("noticeType")
    var noticeType: String,
    @SerializedName("params")
    var params: Params,
    @SerializedName("remark")
    var remark: String,
    @SerializedName("searchValue")
    var searchValue: Any,
    @SerializedName("status")
    var status: String,
    @SerializedName("updateBy")
    var updateBy: String,
    @SerializedName("updateTime")
    var updateTime: String
)

class Params(
)