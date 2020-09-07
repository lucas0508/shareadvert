package com.qujiali.shareadvert.base.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.qujiali.shareadvert.base.repository.BaseRepository
import com.qujiali.shareadvert.common.state.State
import com.qujiali.shareadvert.common.utils.CommonUtil
import com.qujiali.shareadvert.network.response.BaseResponse

open class BaseViewModel<T : BaseRepository> : ViewModel() {
    val loadState by lazy {
        MutableLiveData<State>()
    }

    /**
     * 通过反射注入mRepository
     */
    val mRepository : T by lazy {
        // 获取对应Repository 实例 (有参构造函数)
        (CommonUtil.getClass<T>(this))
                // 获取构造函数的构造器，传入参数class
            .getDeclaredConstructor(MutableLiveData::class.java)
                // 传入加载状态
            .newInstance(loadState)
    }



    override fun onCleared() {
        super.onCleared()
        mRepository.unSubscribe()
    }
}

