package com.qujiali.shareadvert.module.other.view

import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.base.view.BaseActivity
import kotlinx.android.synthetic.main.layout_toolbar.*

class CompanyProfileActivity : BaseActivity() {


    override fun getLayoutId(): Int = R.layout.activity_profile

    override fun initView() {
        super.initView()
        toolbar.setNavigationIcon(R.mipmap.icon_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbarTextView.text = "公司简介"
    }



    override fun onBackPressed() {
        finish()
    }
    override fun showCreateReveal(): Boolean = false

    override fun showDestroyReveal(): Boolean = false

}