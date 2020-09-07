package com.qujiali.shareadvert.network.response


//Description: 返回数据基类
open class BaseResponse<T>(var data: T, var code: Int = -1, var msg: String = "")


//Description: 返回数据基类
open class BaseResponseRow<T>(var rows: T, var code: Int = -1, var msg: String = "")