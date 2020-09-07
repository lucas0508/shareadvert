package com.qujiali.shareadvert.base.view

import android.text.TextUtils
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.afollestad.materialdialogs.MaterialDialog
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


abstract class BaseLifeCycleFragment<VM : BaseViewModel<*>> :
    BaseFragment<VM>() {

    protected lateinit var mViewModel: VM

    override fun initView() {
        showLoading()

        mViewModel = ViewModelProvider(this).get(CommonUtil.getClass(this))

        mViewModel.loadState.observe(this, observer)

        initDataObserver()
    }

    abstract fun initDataObserver()

    open fun showLoading() {
        loadService.showCallback(LoadingCallBack::class.java)
    }

    open  fun showSuccess() {
        loadService.showCallback(SuccessCallback::class.java)
    }


    private fun showError(msg: String) {
        if (!TextUtils.isEmpty(msg)) {
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
        loadService.showCallback(ErrorCallBack::class.java)
    }

    open fun showTip(msg: String) {
        if (!TextUtils.isEmpty(msg)) {
            MaterialDialog(requireContext()).show {
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

    private val observer by lazy {
        Observer<State> {
            it?.let {
                Logger.d("BaseLifeCycleFragment 返回Code：${it.code}")
                when (it.code) {
                    StateType.SUCCESS -> showSuccess()
                    StateType.LOADING -> showLoading()
                    StateType.ERROR -> showTip(it.message)
                    StateType.NETWORK_ERROR -> showError("网络异常")
                    StateType.TIP -> showTip(it.message)
                    StateType.EMPTY -> showEmpty()
                }
            }
        }
    }


    override fun reLoad() {
        showLoading()
        super.reLoad()
    }

}