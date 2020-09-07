package com.qujiali.shareadvert.base.view

import android.text.TextUtils
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
import com.gyf.immersionbar.ImmersionBar
import com.kingja.loadsir.callback.SuccessCallback
import com.orhanobut.logger.Logger
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.base.viewmodel.BaseViewModel
import com.qujiali.shareadvert.common.callback.EmptyCallBack
import com.qujiali.shareadvert.common.callback.ErrorCallBack
import com.qujiali.shareadvert.common.callback.LoadingCallBack
import com.qujiali.shareadvert.common.state.State
import com.qujiali.shareadvert.common.state.StateType
import com.qujiali.shareadvert.common.utils.CommonUtil


abstract class BaseLifeCycleActivity<VM : BaseViewModel<*>> :
    BaseActivity() {
    /**
     * Activity, Fragment 公共方法处理类
     */
    protected lateinit var mViewModel: VM


    override fun initView() {
        showLoading()

        mViewModel = ViewModelProvider(this).get(CommonUtil.getClass(this))

        mViewModel.loadState.observe(this, observer)

        // 初始化View的Observer
        initDataObserver()
    }

    open fun initDataObserver() {}


    open fun showLoading() {
        loadService.showCallback(LoadingCallBack::class.java)
    }

    open fun showSuccess() {
        loadService.showCallback(SuccessCallback::class.java)
    }

    open fun showError(msg: String) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
        }
        loadService.showCallback(ErrorCallBack::class.java)
    }

    open fun showTip(msg: String) {
        if (!TextUtils.isEmpty(msg)) {
            MaterialDialog(this).show {
                title(R.string.title)
                message(text = msg)
                cornerRadius(8.0f)
                negativeButton(R.string.done)
            }
            false
        }
        loadService.showCallback(SuccessCallback::class.java)
    }

    open fun showEmpty() {
        loadService.showCallback(EmptyCallBack::class.java)
    }

    /**
     * 分发应用状态
     */
    private val observer by lazy {
        Observer<State> {

            it?.let {
                Logger.d("BaseLifeCycleActivity 返回Code：${it.code}")
                when (it.code) {
                    StateType.SUCCESS -> showSuccess()
                    StateType.LOADING -> showLoading()
                    StateType.ERROR -> showTip(it.message)
                    StateType.NETWORK_ERROR -> showError("网络出现问题啦")
                    StateType.EMPTY -> showEmpty()
                }
            }
        }
    }
}