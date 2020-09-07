package com.qujiali.shareadvert.network.response
import com.google.gson.annotations.SerializedName


/**
 * Created with Android Studio.
 * Description:
 * @author: Wangjianxian
 * @date: 2020/02/25
 * Time: 20:42
 */
data class EmptyDataResponse(
    @SerializedName("code")
    var code: Int,
    @SerializedName("msg")
    var msg: String
)