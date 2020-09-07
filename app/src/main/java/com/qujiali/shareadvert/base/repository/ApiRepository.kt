package com.qujiali.shareadvert.base.repository

import com.qujiali.shareadvert.network.ApiService
import com.qujiali.shareadvert.network.RetrofitFactory


abstract class ApiRepository : BaseRepository() {
    protected val apiService: ApiService by lazy {
        RetrofitFactory.instance.create(ApiService::class.java)
    }
}