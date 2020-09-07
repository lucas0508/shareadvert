package com.qujiali.shareadvert.module.other.view

import android.widget.TextView
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.base.view.BaseActivity
import kotlinx.android.synthetic.main.layout_toolbar.*

class ProductBriefActivity : BaseActivity() {


    override fun getLayoutId(): Int = R.layout.activity_product_brief

    override fun initView() {
        super.initView()
        toolbar.setNavigationIcon(R.mipmap.icon_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbarTextView.text = "产品简介"
    }



    override fun onBackPressed() {
        finish()
    }
    override fun showCreateReveal(): Boolean = false

    override fun showDestroyReveal(): Boolean = false

}