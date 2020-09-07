package com.qujiali.shareadvert.module.other.view

import android.view.View
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.base.view.BaseLifeCycleActivity
import com.qujiali.shareadvert.common.utils.*
import com.qujiali.shareadvert.custom.dialog.VersionUpdateDialog
import com.qujiali.shareadvert.module.other.viewmodel.AboutViewModel
import kotlinx.android.synthetic.main.activity_about.*
import kotlinx.android.synthetic.main.layout_toolbar.*

class AboutActivity : BaseLifeCycleActivity<AboutViewModel>() {
    override fun getLayoutId(): Int = R.layout.activity_about


    override fun initView() {
        super.initView()
        toolbar.setNavigationIcon(R.mipmap.icon_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbarTextView.text = "关于我们"
        tv_version.setText("版本  v" + DevicePermissionsUtils.getAppCurrentVersion())
        mLoginAgreement.setOnClickListener(View.OnClickListener {
            CommonUtil.startWebView(this, Constant.AGREEMENT_URL, "用户协议")
        })
        mLoginAgreementPrivacy.setOnClickListener(View.OnClickListener {
            CommonUtil.startWebView(this, Constant.PRIVACYPOLICY_URL, "隐私政策")
        })

        ll_update_version.setOnClickListener(object : OnMultiClickListener() {
            override fun onMultiClick(v: View?) {
                mViewModel.loadUpdateVersion()
            }
        })

        ll_product_brief.setOnClickListener(object : OnMultiClickListener() {
            override fun onMultiClick(v: View?) {
                startActivity<ProductBriefActivity>(this@AboutActivity)
            }
        })
        company_profile.setOnClickListener(object : OnMultiClickListener() {
            override fun onMultiClick(v: View?) {
                startActivity<CompanyProfileActivity>(this@AboutActivity)
            }
        })

        showSuccess()
    }


    override fun initDataObserver() {
        super.initDataObserver()
        mViewModel.updataversion.observe(this, Observer {
            if (CompareVersions.compare(
                    it.data.minorVersion,
                    DevicePermissionsUtils.getAppCurrentVersion()
                )
            ) {
                val versionUpdateDialog =
                    VersionUpdateDialog(this)
                versionUpdateDialog.showNoticeDialog(it.data.status, it.data.downloadUrl)
            } else {
                CommonUtil.showToast(this, "当前是最新版本")
            }
        })
    }

    override fun onBackPressed() {
        finish()
    }

    override fun showCreateReveal(): Boolean = false

    override fun showDestroyReveal(): Boolean = false

}