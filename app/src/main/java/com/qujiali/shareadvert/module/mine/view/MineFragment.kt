package com.qujiali.shareadvert.module.mine.view

import android.content.Intent
import android.view.View
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.base.view.BaseLifeCycleFragment
import com.qujiali.shareadvert.common.state.UserInfo
import com.qujiali.shareadvert.common.utils.CommonUtil
import com.qujiali.shareadvert.common.utils.MyAppGlideModule
import com.qujiali.shareadvert.module.mine.model.UserInfoResponse
import com.qujiali.shareadvert.module.mine.viewmodel.MineViewModel
import com.qujiali.shareadvert.module.resources.view.MineResourcesActivity
import com.qujiali.shareadvert.module.settlein.view.SettleInCompanyActivity
import kotlinx.android.synthetic.main.fragment_mine.*
import com.qujiali.shareadvert.common.utils.startActivity
import com.qujiali.shareadvert.custom.dialog.ConfirmDialog
import com.qujiali.shareadvert.module.demand.view.MineDemandActivity
import com.qujiali.shareadvert.module.other.view.AboutActivity
import com.qujiali.shareadvert.module.other.view.AccessFeedbackActivity
import com.qujiali.shareadvert.module.settlein.view.SettleInCompanyStatusActivity
import kotlinx.android.synthetic.main.layout_toolbar.*

class MineFragment : BaseLifeCycleFragment<MineViewModel>(),
    View.OnClickListener {


    companion object {
        fun getInstance(): MineFragment? {
            return MineFragment()
        }
    }

    private var userRole = "NONE"

    private var mUserName = ""

    private var mUserImage = ""


    override fun getLayoutId(): Int = R.layout.fragment_mine

    override fun initView() {
        super.initView()

        mineLogin.setOnClickListener(this)
        mineResources.setOnClickListener(this)
        mineRequire.setOnClickListener(this)
        mineComSetin.setOnClickListener(this)
        mineAboutus.setOnClickListener(this)
        mineFeedBack.setOnClickListener(this)
        signOut.setOnClickListener(this)
        showSuccess()
    }

    override fun onResume() {
        super.onResume()
        mViewModel.loadUserInfo()
    }




    override fun initDataObserver() {
        mViewModel.mUserInfo.observe(this, Observer {
            it?.let {
                if (it.code == 401) {
                    loginSuccess.visibility = View.GONE
                    goLogin.visibility = View.VISIBLE
                } else if (it.code == 200) {
                    goLogin.visibility = View.GONE
                    userRole = it.data.userRole
                    setUserInfo(it.data)
                }
            }
        })
    }

    private fun setUserInfo(it: UserInfoResponse) {
        loginSuccess.visibility = View.VISIBLE
        goLogin.visibility = View.GONE
        mUserName = it.name
        mUserImage = it.profile
        if (it.userRole == "ENTERPRISE") {
            settleIn.visibility = View.VISIBLE
        } else {
            settleIn.visibility = View.GONE
        }
        Glide.with(this).apply {
            MyAppGlideModule.getRequestOptions()
        }.load(it.profile).into(userImage)
        userName.text = it.name
        join.text = "您已加入广告圈${it.dayNumber}天"
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.mineLogin -> {
                if (UserInfo.instance.isLogin) {
                    UserInfo.instance.startPersonalActivity(requireContext())
                } else {
                    UserInfo.instance.login(requireActivity())
                }
            }
            R.id.mineResources -> {
                if (mApp?.isLogin()!!) {
                    startActivity<MineResourcesActivity>(requireContext())
                } else {
                    UserInfo.instance.login(requireActivity())
                }
            }
            R.id.mineRequire -> {
                if (mApp?.isLogin()!!) {
                    startActivity<MineDemandActivity>(requireContext())
                } else {
                    UserInfo.instance.login(requireActivity())
                }
            }
            R.id.mineComSetin -> {
                if (mApp?.isLogin()!!) {
                    if ("ENTERPRISE" == userRole) {
                        startActivity<SettleInCompanyStatusActivity>(requireContext()) {
                        }
                    } else {
                        startActivity<SettleInCompanyActivity>(requireContext()){
                        }
                    }
                } else {
                    UserInfo.instance.login(requireActivity())
                }
            }
            R.id.mineAboutus -> {
                startActivity<AboutActivity>(requireContext())

            }
            R.id.mineFeedBack -> {
                startActivity<AccessFeedbackActivity>(requireContext())
            }
            R.id.signOut -> {
                mApp!!.getConfirmDialog()!!.show("确定退出登录吗？", object : ConfirmDialog.ConfirmCallback {
                    override fun onOk() {
                        UserInfo.instance.logoutSuccess()
                        UserInfo.instance.login(requireActivity())
                    }
                    override fun onCancel() {}
                })
            }

        }
    }

}