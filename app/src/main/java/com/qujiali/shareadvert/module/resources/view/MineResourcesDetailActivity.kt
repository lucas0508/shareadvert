package com.qujiali.shareadvert.module.resources.view

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.orhanobut.logger.Logger
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.base.view.Adapter
import com.qujiali.shareadvert.base.view.BaseApplication
import com.qujiali.shareadvert.base.view.BaseLifeCycleActivity
import com.qujiali.shareadvert.base.view.ViewHolder
import com.qujiali.shareadvert.common.utils.CommonUtil
import com.qujiali.shareadvert.common.utils.Constant
import com.qujiali.shareadvert.custom.takePhoto.GridViewAddImgesAdpter
import com.qujiali.shareadvert.custom.takePhoto.PhotoUtils
import com.qujiali.shareadvert.module.resources.model.ReMsg
import com.qujiali.shareadvert.module.resources.model.ResourcesResponse
import com.qujiali.shareadvert.module.resources.viewmodel.ResourcesViewModel
import com.qujiali.shareadvert.module.settlein.model.AdressDataEntity_oldddd
import com.qujiali.shareadvert.network.response.BaseResponse
import kotlinx.android.synthetic.main.activity_mine_resources_detial.*
import kotlinx.android.synthetic.main.activity_mine_resources_detial.btn_submit
import kotlinx.android.synthetic.main.activity_mine_resources_detial.et_Phone
import kotlinx.android.synthetic.main.activity_mine_resources_detial.et_company_skill1
import kotlinx.android.synthetic.main.activity_mine_resources_detial.et_company_skill2
import kotlinx.android.synthetic.main.activity_mine_resources_detial.et_company_skill3
import kotlinx.android.synthetic.main.activity_mine_resources_detial.et_detail
import kotlinx.android.synthetic.main.activity_mine_resources_detial.gridview
import kotlinx.android.synthetic.main.activity_mine_resources_detial.tvResourceTitle
import kotlinx.android.synthetic.main.activity_mine_resources_detial.tv_YourRegion
import kotlinx.android.synthetic.main.layout_toolbar.*
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.util.*

class MineResourcesDetailActivity : BaseLifeCycleActivity<ResourcesViewModel>() {


    private val mResourcesId: Int? by lazy { intent?.getIntExtra(Constant.KEY_RESOURCESID, 0) }
    private var mGridViewAddImgesAdpter: GridViewAddImgesAdpter? = null
    private var selectIndex = 1
    private var mPathList = ArrayList<String>()
    private var cityString: ArrayList<AdressDataEntity_oldddd.RowsBean.ChildBeanXX.ChildBeanX.ChildBean>? =
        null
    private var cityName: String? = ""
    private var mAdapter: Adapter<ReMsg>? = null
    private var mMap = mutableMapOf<String, Any>()
    private var workerSkill = ""
    private var mImageList = ArrayList<String>()
    private var adCode: String? = ""

    override fun getLayoutId(): Int = R.layout.activity_mine_resources_detial


    override fun initView() {
        super.initView()
        toolbar.setNavigationIcon(R.mipmap.icon_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbarTextView.text = "详情"
        initGridView()

        showSuccess()
    }

    override fun initData() {
        super.initData()
        mViewModel.loadResourcesSingleData(resourcesId = mResourcesId!!)
    }


    override fun initDataObserver() {
        mViewModel.resourcesSingleData.observe(this, Observer {
            it?.let {
                setData(it)
            }
        })
        mViewModel.resourcesOffline.observe(this, Observer {
            it.let {
                CommonUtil.showToast(this, "下线成功！")
                finish()
            }
        })

        mViewModel.uploadFile.observe(this, androidx.lifecycle.Observer {
            it.let {
//                mImageList = it as ArrayList<String>
                Logger.e("success--展示图片-->$mImageList")

                mPathList.add(it)
                mGridViewAddImgesAdpter!!.notifyDataSetChanged(mPathList)
            }
        })

        mViewModel.resourcesData.observe(this, androidx.lifecycle.Observer {
            CommonUtil.showToast(
                this, "重新" +
                        "提交成功"
            )
            finish()
        })
    }

    private fun setData(it: BaseResponse<ResourcesResponse>) {
        when (it.data.status) {
            0 -> {
                tvResourceTitle.isEnabled = false
                tv_YourRegion.isEnabled = false
                et_Phone.isEnabled = false
                et_detail.isEnabled = false
                et_company_skill1.isEnabled = false
                et_company_skill2.isEnabled = false
                et_company_skill3.isEnabled = false
                if (!TextUtils.isEmpty(it.data.img)) {
                    val split1: Array<String> = it.data.img.split(",".toRegex()).toTypedArray()
                    mGridViewAddImgesAdpter!!.notifyDataSetChanged(Arrays.asList(*split1), true)
                }
            }//待审核
            1 -> {
                pending(it)

            }//通过
            2 -> {
                reSubmit(it)
            }//拒绝
        }
        if (!TextUtils.isEmpty(it.data.keyword)) {
            val split1 = it.data.keyword.split(",")
            when (split1.size) {
                1 -> {
                    et_company_skill1.setText(split1[0])
                }
                2 -> {
                    et_company_skill1.setText(split1[0])
                    et_company_skill2.setText(split1[1])
                }
                3 -> {
                    et_company_skill1.setText(split1[0])
                    et_company_skill2.setText(split1[1])
                    et_company_skill3.setText(split1[2])
                }
            }
        }
        tvResourceTitle.setText(it.data.title)
        tv_YourRegion.text = it.data.areaList.joinToString("-")
        et_Phone.setText(it.data.phone)
        et_detail.setText(it.data.info)
    }

    /**
     * 待审核
     */
    private fun pending(it: BaseResponse<ResourcesResponse>) {
        tvResourceTitle.isEnabled = false
        tv_YourRegion.isEnabled = false
        et_company_skill1.isEnabled = false
        et_company_skill2.isEnabled = false
        et_company_skill3.isEnabled = false
        et_Phone.isEnabled = false
        et_detail.isEnabled = false
        if (it.data.state != 0) {
            btn_submit.visibility = View.VISIBLE
            btn_submit.text = "下线"
            btn_submit.setOnClickListener {
                mViewModel.onLoadResourcesOffline(resourcesId = mResourcesId!!)
            }
        }
        if (!TextUtils.isEmpty(it.data.img)) {
            val split1: Array<String> = it.data.img.split(",".toRegex()).toTypedArray()
            Logger.e(
                "Arrays.asList(split1" + Arrays.asList(
                    *split1
                )
            )
            mGridViewAddImgesAdpter!!.notifyDataSetChanged(Arrays.asList(*split1), true)
        }
    }

    /**
     * 拒绝重新提交
     */
    private fun reSubmit(it: BaseResponse<ResourcesResponse>) {
        if (!TextUtils.isEmpty(it.data.img)) {
            val split1: Array<String> = it.data.img.split(",".toRegex()).toTypedArray()

            mPathList = split1.toMutableList() as ArrayList<String>
            mGridViewAddImgesAdpter!!.notifyDataSetChanged(mPathList)
        }
        llRefuse.visibility = View.VISIBLE
        initRecyclerView()
        mAdapter?.updateData(it.data.reMsg)
        btn_submit.visibility = View.VISIBLE
        llRefuse.visibility = View.VISIBLE
        btn_submit.text = "重新提交"
        var cityStringArea = it.data.area
        btn_submit.setOnClickListener {
            if (!TextUtils.isEmpty(et_company_skill1.text.toString().trim { it <= ' ' })) {
                workerSkill = et_company_skill1.text.toString().trim { it <= ' ' }
            }
            if (!TextUtils.isEmpty(et_company_skill2.text.toString().trim { it <= ' ' })) {
                if (!TextUtils.isEmpty(workerSkill)) {
                    workerSkill =
                        workerSkill + "," + et_company_skill2.text.toString().trim { it <= ' ' }
                } else {
                    workerSkill = et_company_skill2.text.toString().trim { it <= ' ' }
                }
            }
            if (!TextUtils.isEmpty(et_company_skill3.text.toString().trim { it <= ' ' })) {
                if (!TextUtils.isEmpty(workerSkill)) {
                    workerSkill =
                        workerSkill + "," + et_company_skill3.text.toString().trim { it <= ' ' }
                } else {
                    workerSkill = et_company_skill3.text.toString().trim { it <= ' ' }
                }
            }

            mMap["title"] = tvResourceTitle.text.toString()
            mMap["phone"] = et_Phone.text.toString()

            mMap["info"] = et_detail.text.toString()
            mMap["keyword"] = workerSkill
            mMap["img"] = CommonUtil.listToString(mPathList)!!

           /* if (null != cityString) {
                mMap["area"] = cityString!![0].adCode!!
            } else {
                mMap["area"] = cityStringArea
            }*/
            if (!TextUtils.isEmpty(adCode)) {
                mMap["area"] = adCode!!
            } else {
                mMap["area"] = cityStringArea
            }
            mMap["resourceId"] = mResourcesId!!


            Logger.d("omygad-之前的图片---->" + mPathList)

            Logger.d("omygad-完成提交数据---->" + mMap)

//             mViewModel.loadResourcesAdd(mMap)
        }
    }

    private fun initGridView() {
        mGridViewAddImgesAdpter = GridViewAddImgesAdpter(mPathList, this)
        gridview.setAdapter(mGridViewAddImgesAdpter)
        gridview.setOnItemClickListener { adapterView, view, i, l ->
            mApp!!.getSelectPicDialog()?.show { position ->
                selectIndex = 2
                if (position == 0) {
                    PhotoUtils.autoObtainCameraPermission(this, false)
                } else if (position == 1) {
                    PhotoUtils.autoObtainStoragePermission(this, false)
                }
            }
        }
    }


    private fun initRecyclerView() {
        val layoutManager: LinearLayoutManager =
            object : LinearLayoutManager(this, VERTICAL, false) {
                override fun canScrollHorizontally(): Boolean {
                    return false
                }
            }
        mRecyclerDenialReason.layoutManager = layoutManager
        mRecyclerDenialReason.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )
        mAdapter = object : Adapter<ReMsg>(this, false) {
            override fun OnCreateViewHolder(
                parent: ViewGroup?,
                viewType: Int
            ): BaseViewHolder<*>? {
                return object : ViewHolder<ReMsg?>(parent, R.layout.item_denia_reason) {
                    private var refuse_msg: TextView? = null
                    private var refuse_time: TextView? = null
                    override fun initView() {
                        refuse_msg = `$`(R.id.refuse_msg)
                        refuse_time = `$`(R.id.refuse_time)
                    }

                    override fun setData(data: ReMsg?) {
                        super.setData(data)
                        refuse_msg!!.text = data?.msg
                        refuse_time!!.text = data?.auditTime
                    }
                }
            }
        }
        mRecyclerDenialReason.adapter = mAdapter


    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 0X4444) {
            if (requestCode == 0x2222) {
                cityString =
                    data?.getParcelableArrayListExtra<AdressDataEntity_oldddd.RowsBean.ChildBeanXX.ChildBeanX.ChildBean>(
                        "string"
                    )
                cityName = data?.extras!!["CityName"] as String?
                Log.e("传值", "选择城市------>: $cityName")
                Log.e("传值", "onActivityResult: " + Gson().toJson(cityString))
                 adCode = data.extras!!["adCode"] as String
                if (cityName?.contains("全国")!!) {
                    tv_YourRegion.text = "全国-不限-不限"
                } else {
                    tv_YourRegion.text = cityName
                }
            }
        } else {
            PhotoUtils.onActivityResult(
                requestCode,
                resultCode,
                data,
                this,
                object : PhotoUtils.CropHandler {
                    override fun handleCropResult(uri: Uri, tag: Int) {
                        Logger.d(uri.path)
                        Luban.with(BaseApplication.instance)
                            .load(uri.path)
                            .ignoreBy(100)
                            .setCompressListener(object : OnCompressListener {
                                override fun onStart() {}
                                override fun onSuccess(file: File) {
                                    try {
                                        // mPathList.add(file.absolutePath)
                                        mViewModel.uploadFile(file.absolutePath)
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }

                                override fun onError(e: Throwable?) {
                                    TODO("Not yet implemented")
                                }

                            }).launch()
                    }

                    override fun handleCropError(data: Intent?) {
                        CommonUtil.showToast(BaseApplication.instance, "图片错误")
                    }
                })
        }
    }


    override fun showCreateReveal(): Boolean = true

    override fun showDestroyReveal(): Boolean = false

    override fun onBackPressed() = finish()
}