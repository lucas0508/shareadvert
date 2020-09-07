package com.qujiali.shareadvert.module.demand.view

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.base.view.BaseApplication
import com.qujiali.shareadvert.base.view.BaseLifeCycleActivity
import com.qujiali.shareadvert.common.utils.CommonUtil
import com.qujiali.shareadvert.custom.takePhoto.GridViewAddImgesAdpter
import com.qujiali.shareadvert.custom.takePhoto.PhotoUtils
import com.qujiali.shareadvert.module.demand.viewmodel.HomeFragmentDemandModel
import com.qujiali.shareadvert.module.settlein.model.AdressDataEntity_oldddd
import com.qujiali.shareadvert.module.settlein.view.AddressActivity
import com.qujiali.shareadvert.module.settlein.view.AddressActivity22222222222
import kotlinx.android.synthetic.main.activity_rublish_resources.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.util.*

class PublishDemandActivity : BaseLifeCycleActivity<HomeFragmentDemandModel>() {

    private val mMap = mutableMapOf<String, Any>()
    private var cityString: ArrayList<AdressDataEntity_oldddd.RowsBean.ChildBeanXX.ChildBeanX.ChildBean>? =
        null
    private var cityName: String? = ""
    private var adCode: String = ""
    private var mPathList = ArrayList<String>()
    private var mGridViewAddImgesAdpter: GridViewAddImgesAdpter? = null
    private var mImageList = ArrayList<String>()


    override fun getLayoutId(): Int = R.layout.activity_rublish_demand


    override fun initView() {
        super.initView()
        toolbar.setNavigationIcon(R.mipmap.icon_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbarTextView.text = "发布需求"
//        toolbarTextViewRight.text = "发布规则"
        initGridView()
        showSuccess()
        btn_submit.setOnClickListener {
            sendData()
        }
        tv_YourRegion.setOnClickListener {
            showAddressPickerPop()
        }
//        toolbarTextViewRight.setOnClickListener {
//            showPublishingRule()
//        }
    }

    private fun initGridView() {
        mGridViewAddImgesAdpter = GridViewAddImgesAdpter(mPathList, this)
        gridview.adapter = mGridViewAddImgesAdpter
        gridview.setOnItemClickListener { _, _, _, _ ->
            runSelectPic(false)
        }
    }

    private fun runSelectPic(isCrap: Boolean) {
        mApp?.getSelectPicDialog()
            ?.show { position ->
                if (position == 0) {
                    PhotoUtils.autoObtainCameraPermission(
                        this,
                        isCrap
                    )
                } else if (position == 1) {
                    PhotoUtils.autoObtainStoragePermission(
                        this,
                        isCrap
                    )
                }
            }
    }


    override fun initDataObserver() {


        mViewModel.uploadFile.observe(this, androidx.lifecycle.Observer {
            it.let {
                Logger.e("success--展示图片-->$mImageList")
                mPathList.add(it)
                mGridViewAddImgesAdpter!!.notifyDataSetChanged(mPathList)
            }
        })

        mViewModel.demandData.observe(this, androidx.lifecycle.Observer {
            if (it.code == 200) {
                CommonUtil.showToast(this, "提交成功")
                finish()
            } else {
                showTip(msg = it.msg)
            }
        })
    }

    private fun sendData() {
        if (TextUtils.isEmpty(tvResourceTitle.text.toString())) {
            CommonUtil.showToast(this, "请输入需求标题")
            return
        }


        if (TextUtils.isEmpty(et_Phone.text.toString())) {
            CommonUtil.showToast(this, "请输入联系电话")
            return
        }

        if (TextUtils.isEmpty(et_detail.text.toString())) {
            CommonUtil.showToast(this, "请输入详情介绍")
            return
        }

        mMap["title"] = tvResourceTitle.text.toString()
        mMap["phone"] = et_Phone.text.toString()

        mMap["info"] = et_detail.text.toString()

        mMap["img"] = CommonUtil.listToString(mPathList)!!
        mMap["location"] = adCode

        mViewModel.loadDemandAdd(mMap)
    }


    private fun showAddressPickerPop() {
        startActivityForResult(Intent(this, AddressActivity22222222222::class.java), 0x2222)
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

                adCode = data.extras!!["adCode"] as String

                Log.e("传值", "选择城市------>: $cityName")
                Log.e("传值", "选择编码------>: $adCode")
                Log.e("传值", "onActivityResult: " + Gson().toJson(cityString))
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

    private fun showPublishingRule() {
        val items = arrayOf(
            "1.所有发布内容需真实、有效，不得发布虚假内容;",
            "2.不得发布法律法规所禁止的广告内容;",
            "3.所有发布内容均不能含有电话号码及其他联系方式,一经发现将无法通过审核;",
            "4.如是同行代加工资源请在关键字处注明同行."
        )
        val listDialog =
            AlertDialog.Builder(this)
        listDialog.setTitle("广告资源发布规则")
        listDialog.setItems(items) { dialog, which ->
            // which 下标从0开始
            // ...To-do
            //                Toast.makeText(NewResourcesActivity.this,
            //                        "你点击了" + items[which],
            //                        Toast.LENGTH_SHORT).show();
        }
        listDialog.show()
    }

    override fun showCreateReveal(): Boolean = true

    override fun showDestroyReveal(): Boolean = false

    override fun onBackPressed() = finish()
}