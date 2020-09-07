package com.qujiali.shareadvert.network
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.orhanobut.logger.Logger
import com.qujiali.shareadvert.base.repository.BaseRepository
import com.qujiali.shareadvert.base.viewmodel.BaseViewModel
import com.qujiali.shareadvert.common.state.State
import com.qujiali.shareadvert.common.state.StateType
import com.qujiali.shareadvert.common.state.UserInfo
import com.qujiali.shareadvert.common.utils.Constant
import com.qujiali.shareadvert.network.response.BaseResponse
import com.qujiali.shareadvert.network.response.BaseResponseRow
import kotlinx.coroutines.launch

/**
 * Description:数据解析扩展函数
 */
fun <T> BaseResponse<T>.dataConvert(
    loadState: MutableLiveData<State>
): T {


    when (code) {
        Constant.SUCCESS -> {
            if (data is List<*>) {
                if ((data as List<*>).isEmpty()) {
                    loadState.postValue(State(StateType.EMPTY))
                }
            }
            loadState.postValue(State(StateType.SUCCESS))
            return data
        }
        Constant.NOT_LOGIN -> {
            UserInfo.instance.logoutSuccess()
            loadState.postValue(State(StateType.ERROR, message = "请重新登录"))
            return data
        }
        else -> {
            loadState.postValue(State(StateType.ERROR, message = msg))
            return data
        }
    }
}


fun <T> BaseResponseRow<T>.dataConvert(
    loadState: MutableLiveData<State>
): T {
    when (code) {
        Constant.SUCCESS -> {
            if (rows is List<*>) {
                if ((rows as List<*>).isEmpty()) {
                    loadState.postValue(State(StateType.EMPTY))
                }
            }
            loadState.postValue(State(StateType.SUCCESS))
            return rows
        }
        Constant.NOT_LOGIN -> {
            UserInfo.instance.logoutSuccess()
            loadState.postValue(State(StateType.ERROR, message = "请重新登录"))
            return rows
        }
        else -> {
            loadState.postValue(State(StateType.ERROR, message = msg))
            return rows
        }
    }
}


fun <T : BaseRepository> BaseViewModel<T>.initiateRequest(
    block: suspend () -> Unit,
    loadState: MutableLiveData<State>
) {
    viewModelScope.launch {
        runCatching {
            block()
        }.onSuccess {
        }.onFailure {
            it.printStackTrace()
            Logger.d("异常：(${it.message})")
            NetExceptionHandle.handleException(it, loadState)
        }
    }
}
