package com.qujiali.shareadvert.module.index

import android.Manifest
import android.content.Intent
import android.os.Handler
import android.view.View
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.base.view.BaseLifeCycleActivity
import com.qujiali.shareadvert.common.permission.PermissionResult
import com.qujiali.shareadvert.common.permission.Permissions
import com.qujiali.shareadvert.common.utils.Constant.SP_IS_FIRST
import com.qujiali.shareadvert.common.utils.SPreference
import com.qujiali.shareadvert.common.utils.startActivity
import com.qujiali.shareadvert.custom.DrawableScaleFadeFactory
import com.qujiali.shareadvert.custom.dialog.ConfirmAgreementDialog
import com.qujiali.shareadvert.module.banner.viewmodel.BannerViewModel
import kotlinx.android.synthetic.main.activity_splash.*
import pub.devrel.easypermissions.AppSettingsDialog
import java.util.*

class SplashActivity : BaseLifeCycleActivity<BannerViewModel>() {

    private var isFirst: Boolean by SPreference(SP_IS_FIRST, true)
    private var recLen = 4;//跳过倒计时提示5秒
    private var timer: Timer = Timer();
    private var handler: Handler? = null
    private var runnable: Runnable? = null

    private val mPermissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    override fun getLayoutId(): Int = R.layout.activity_splash

    override fun initView() {
        super.initView()
//        ImmersionBar.with(this).init()
//        ImmersionBar.setTitleBar(this,time)
//        window.setBackgroundDrawable(null)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        startService(Intent(this@SplashActivity, LocationService::class.java))
        mViewModel.onQueryBanner("", "1")
        initPermission()

    }
    private fun jump() {
        timer.schedule(task, 0, 1000);

        /**
         * 正常情况下不点击跳过
         */
        handler = Handler()
        handler!!.postDelayed(Runnable { //从闪屏界面跳转到首界面
            startIntent()
        }.also { runnable = it }, 5000) //延迟5S后发送handler信息
    }

    override fun initDataObserver() {
        mViewModel.onQueryBanner.observe(this, Observer {
            it?.let {
                Glide.with(this)
                    .load(it)
                    .transition(DrawableTransitionOptions.with(DrawableScaleFadeFactory(350, true)))
                    .into(welcome_ad)
            }
        })
    }


    private fun startIntent() {
        startActivity<MainActivity>(this)
        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out)
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    private fun initPermission() {
        Permissions(this).request(*mPermissions).observe(
            this, Observer {
                when (it) {
                    is PermissionResult.Grant -> {
                        time.visibility = View.VISIBLE
                        if (isFirst) {
                            mApp!!.confirmAgreementDialog!!.show(
                                getString(R.string.agreement),
                                object : ConfirmAgreementDialog.ConfirmCallback {
                                    override fun onCancel() {
                                        finish()
                                    }
                                    override fun onOk() {
                                        jump()
                                    }
                                })
                            isFirst = false
                        } else {
                            jump()
                        }
                        time.setOnClickListener {
                            startIntent()
                            if (runnable!=null)handler!!.removeCallbacks(runnable)
                        }

//                        startIntent()
                    }
                    // 进入设置界面申请权限
                    is PermissionResult.Rationale -> {
                        AppSettingsDialog.Builder(this)
                            .setTitle("申请权限")
                            .setRationale("没有相关权限应用将无法正常运行，点击确定进入权限设置界面来进行更改")
                            .build()
                            .show()
                        finish()
                    }
                    // 进入设置界面申请权限
                    is PermissionResult.Deny -> {
                        AppSettingsDialog.Builder(this)
                            .setTitle("申请权限")
                            .setRationale("没有相关权限应用将无法正常运行，点击确定进入权限设置界面来进行更改")
                            .build()
                            .show()
                        finish()
                    }
                }
            }
        )
    }

    var task: TimerTask = object : TimerTask() {
        override fun run() {
            runOnUiThread {
                time.visibility = View.VISIBLE
                recLen--
                time!!.text = "跳过$recLen"
                if (recLen < 1) {
                    timer.cancel()
                    time.visibility = View.GONE //倒计时到0隐藏字体
                    startIntent()
                }
            }

        }
    }
}
