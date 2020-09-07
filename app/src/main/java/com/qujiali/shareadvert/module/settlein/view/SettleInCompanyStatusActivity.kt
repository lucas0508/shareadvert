package com.qujiali.shareadvert.module.settlein.view

import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.base.view.Adapter
import com.qujiali.shareadvert.base.view.BaseLifeCycleActivity
import com.qujiali.shareadvert.base.view.ViewHolder
import com.qujiali.shareadvert.common.utils.CommonUtil
import com.qujiali.shareadvert.custom.dialog.ConfirmDialog
import com.qujiali.shareadvert.module.home.model.CompanyDataResponse
import com.qujiali.shareadvert.module.resources.model.ReMsg
import com.qujiali.shareadvert.module.settlein.viewmodel.SettleinViewModel
import kotlinx.android.synthetic.main.activity_mine_resources_detial.*
import kotlinx.android.synthetic.main.activity_settlein_company_status.*
import kotlinx.android.synthetic.main.activity_settlein_company_status.mRecyclerDenialReason
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.activity_mine_resources_detial.llRefuse as llRefuse1
import kotlinx.android.synthetic.main.activity_settlein_company_status.btn_submit as btn_submit1

class SettleInCompanyStatusActivity : BaseLifeCycleActivity<SettleinViewModel>() {

    private var companyId: Int? = null
    private var mAdapter: Adapter<String>? = null
    private var mAdapterReason: Adapter<ReMsg>? = null

    override fun getLayoutId(): Int = R.layout.activity_settlein_company_status

    override fun initView() {
        super.initView()
        toolbar.setNavigationIcon(R.mipmap.icon_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbarTextView.text = "公司入驻"
        initRecycler()
        mViewModel.loadCompanyData()

        btn_submit.setOnClickListener {
            finish()
            com.qujiali.shareadvert.common.utils.startActivity<SettleInCompanyActivity>(
                this
            )
        }
        status.setOnClickListener {
            //注销
            mApp!!.getConfirmDialog()!!.show("注销将删除所有发布信息", object : ConfirmDialog.ConfirmCallback {
                override fun onOk() {
                    mViewModel.loadCompanyRemove(companyId!!)
                }

                override fun onCancel() {}
            })
        }
        showSuccess()
    }

    override fun initDataObserver() {
        mViewModel.mCompanyData.observe(this, Observer {
            it.let {
                if (it != null)
                    setData(it)
            }
        })

        mViewModel.mCompanyRemove.observe(this, Observer {
            CommonUtil.showToast(this, "注销成功！")
            finish()
        })
    }

    private fun setData(it: CompanyDataResponse?) {
        companyId = it?.companyId
        when (it?.status) {
            0 -> {//待审核
                status.text = "待审核"
                status.setOnClickListener(null)
            }
            1 -> {//通过
                status.text = "注销公司"
                status.setTextColor(Color.parseColor("#999999"))
                status.setBackgroundResource(R.drawable.background_shape_settlein_status_dark)
                btn_submit.text = "修改信息"
                btn_submit.visibility = View.VISIBLE
            }
            2 -> {//拒绝
                status.setOnClickListener(null)
                status.text = "审核拒绝"
                llRefuse.visibility = View.VISIBLE
                mRecyclerDenialReason.text = it.refuseMsg
                btn_submit.visibility = View.VISIBLE
            }
        }
        tvTitle.text = it?.name
        tvNumber.text = "联系电话： ${it?.phone}"
        tvHomeCity.text = "服务区域：${it?.areaList?.joinToString("")}"
        tv_introduce.text = it?.info
        val stringToList = CommonUtil.stringToList(it?.img!!)
        mAdapter!!.update(stringToList)
        Glide.with(this@SettleInCompanyStatusActivity).load(it.profile).into(image_view_logo);
        Glide.with(this@SettleInCompanyStatusActivity).load(it.businessLicenseImg)
            .into(businessLicense);
        val split1 = it?.mainBusiness?.split(",")
        when (split1?.size) {
            1 -> {
                home_type1.text = split1[0]
                home_type1.visibility = View.VISIBLE
            }
            2 -> {
                home_type1.text = split1[0]
                home_type2.text = split1[1]
                home_type1.visibility = View.VISIBLE
                home_type2.visibility = View.VISIBLE
            }
            3 -> {
                home_type1.text = split1[0]
                home_type2.text = split1[1]
                home_type3.text = split1[2]
                home_type1.visibility = View.VISIBLE
                home_type2.visibility = View.VISIBLE
                home_type3.visibility = View.VISIBLE
            }
        }
    }

    private fun initRecycler() {
        val layoutManager1 =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recycler_view.setLayoutManager(layoutManager1)

        mAdapter = object : Adapter<String>(this, false) {
            override fun OnCreateViewHolder(
                parent: ViewGroup?,
                viewType: Int
            ): BaseViewHolder<*>? {
                return object : ViewHolder<String?>(parent, R.layout.item_settlein_image) {
                    private var iv_image: ImageView? = null
                    override fun initView() {
                        iv_image = `$`(R.id.iv_steps_image)
                    }

                    override fun setData(data: String?) {
                        super.setData(data)
//                        Picasso.with(this@HomeDetailActivity).load(data).into(iv_image)
                        Glide.with(this@SettleInCompanyStatusActivity).load(data).into(iv_image!!);
//                        iv_image!!.setOnClickListener {
//                            val strings =
//                                ArrayList<String>()
//                            strings.add(data)
//                        }
                    }
                }
            }
        }
        recycler_view.adapter = mAdapter
    }

    override fun showCreateReveal(): Boolean = true

    override fun showDestroyReveal(): Boolean = false

    override fun onBackPressed() = finish()
}