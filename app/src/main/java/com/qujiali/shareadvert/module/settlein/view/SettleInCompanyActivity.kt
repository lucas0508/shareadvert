package com.qujiali.shareadvert.module.settlein.view

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.orhanobut.logger.Logger
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.base.view.BaseApplication
import com.qujiali.shareadvert.base.view.BaseLifeCycleActivity
import com.qujiali.shareadvert.common.utils.CommonUtil
import com.qujiali.shareadvert.common.utils.MyAppGlideModule
import com.qujiali.shareadvert.common.utils.OnMultiClickListener
import com.qujiali.shareadvert.custom.takePhoto.GridViewAddImgesAdpter
import com.qujiali.shareadvert.custom.takePhoto.PhotoUtils
import com.qujiali.shareadvert.module.home.model.CompanyDataResponse
import com.qujiali.shareadvert.module.settlein.model.AdressDataEntity_oldddd
import com.qujiali.shareadvert.module.settlein.viewmodel.SettleinViewModel
import kotlinx.android.synthetic.main.activity_settlein_company.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.util.*


class SettleInCompanyActivity : BaseLifeCycleActivity<SettleinViewModel>() {


    private var cityName: String? = ""
    private var adCode: String? = ""
    private var cityString: ArrayList<AdressDataEntity_oldddd.RowsBean.ChildBeanXX.ChildBeanX.ChildBean>? =
        null
    private var licencePhotoPath: String? = ""
    private var companyLogoPath: String? = ""
    private var selectIndex: Int = 1
    private var workerSkill = ""
    private var mGridViewAddImgesAdpter: GridViewAddImgesAdpter? = null
    private var mPathList = ArrayList<String>()//本地图片


    private var profile = ""
    private var img = ""
    private var mImageList = ArrayList<String>()
    private var isPro: Boolean = true
    private val mMap = mutableMapOf<String, Any>()
    private var oldimg: String? = null
    private var oldbusinessLicenseImg: String? = null
    private var oldprofile: String? = null
    private var upImage: Boolean = false
    private var mUploadPathList = ArrayList<String>()  //新选择的粘片需要上传
    private var mPostPathList = ArrayList<String>() // 带http的旧图片
    override fun getLayoutId(): Int = R.layout.activity_settlein_company


    override fun initView() {
        super.initView()
        toolbar.setNavigationIcon(R.mipmap.icon_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbarTextView.text = "公司入驻"
        initOnClick()
        initGridView()
        mViewModel.loadCompanyData()
        showSuccess()
    }

    override fun initDataObserver() {
        mViewModel.mCompanyData.observe(this, Observer {
            it.let {
                if (it != null)
                    setData(it)
            }
        })

        mViewModel.uploadFile.observe(this, Observer {
            it.let {
                mApp!!.getLoadingDialog()!!.hide()
                upImage = true
                if (isPro) profile = it else img = it
            }
        })

        mViewModel.uploadFiles.observe(this, Observer {
            it.let {
                mImageList = it as ArrayList<String>
                for (str in mImageList){
                    mPostPathList.add(str)
                }
                mMap["img"] = CommonUtil.listToString(mPostPathList)!!    //展示图片
                mViewModel.onCompanySettleIn(mMap)
            }
        })
        mViewModel.onCompanySettleIn.observe(this, Observer {
            mApp!!.getLoadingDialog()!!.hide()
            if (it.code == 200) {
                CommonUtil.showToast(this, "提交成功")
                finish()
            } else {
                showTip(msg = it.msg)
            }
        })
    }

    private fun setData(it: CompanyDataResponse?) {
        et_company_name.setText(it?.name)
        et_company_abbreviation.setText(it?.abbreviation)
        et_company_phone.setText(it?.phone)
        et_company_introduce.setText(it?.info)
        tv_company_address.text = it?.areaList!!.joinToString("-")
        adCode = it.area
        Glide.with(BaseApplication.instance)
            .applyDefaultRequestOptions(MyAppGlideModule.getRequestOptions())
            .load(it.profile)
            .into(
                riv_company_logo
            )
        Glide.with(BaseApplication.instance)
            .applyDefaultRequestOptions(MyAppGlideModule.getRequestOptions())
            .load(it.businessLicenseImg)
            .into(
                licencePhoto
            )
        if (it.img.isNotEmpty()) {
            val split1: Array<String> = it.img.split(",".toRegex()).toTypedArray()
            mPathList = split1.toMutableList() as ArrayList<String>
            mGridViewAddImgesAdpter!!.notifyDataSetChanged(mPathList)
        }
        oldimg = it.img
        oldbusinessLicenseImg = it.businessLicenseImg
        oldprofile = it.profile
        if (!TextUtils.isEmpty(it.mainBusiness)) {
            val split1 = it.mainBusiness.split(",")
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
    }

    private fun initOnClick() {
        riv_company_logo.setOnClickListener(object : OnMultiClickListener() {
            override fun onMultiClick(v: View?) {
                selectIndex = 1
                runSelectPic(true)
            }
        })
        licencePhoto.setOnClickListener(object : OnMultiClickListener() {
            override fun onMultiClick(v: View?) {
                selectIndex = 2
                runSelectPic(false)
            }

        })
        tv_company_address.setOnClickListener(object : OnMultiClickListener() {
            override fun onMultiClick(v: View?) {
                showAddressPickerPop()
            }
        })
        btn_company_submit.setOnClickListener(object : OnMultiClickListener() {
            override fun onMultiClick(v: View?) {
                postCompanyData()
            }
        })
    }

    private fun showAddressPickerPop() {
        startActivityForResult(Intent(this, AddressActivity::class.java), 0x2222)
    }

    private fun initGridView() {
        mGridViewAddImgesAdpter = GridViewAddImgesAdpter(mPathList, this)
        gridview.adapter = mGridViewAddImgesAdpter
        gridview.setOnItemClickListener { _, _, _, _ ->
            selectIndex = 3
            runSelectPic(false)
        }
    }

    private fun runSelectPic(isCrap: Boolean) {
        mApp?.getSelectPicDialog()
            ?.show { position ->
                if (position == 0) {
                    PhotoUtils.autoObtainCameraPermission(
                        this@SettleInCompanyActivity,
                        isCrap
                    )
                } else if (position == 1) {
                    PhotoUtils.autoObtainStoragePermission(
                        this@SettleInCompanyActivity,
                        isCrap
                    )
                }
            }
    }

    private fun postCompanyData() {

        if (TextUtils.isEmpty(et_company_name.text.toString().trim { it <= ' ' })) {

            CommonUtil.showToast(this, "请输入公司名称")
            return
        }
        if (TextUtils.isEmpty(et_company_abbreviation.text.toString().trim { it <= ' ' })) {
            CommonUtil.showToast(this, "请输入公司简称")
            return
        }
        if (TextUtils.isEmpty(et_company_phone.text.toString().trim { it <= ' ' })) {
            CommonUtil.showToast(this, "请输入联系方式")
            return
        }
        if (TextUtils.isEmpty(
                et_company_skill1.text.toString().trim { it <= ' ' }
            ) && TextUtils.isEmpty(et_company_skill2.text.toString().trim { it <= ' ' }) &&
            TextUtils.isEmpty(
                et_company_skill3.text.toString().trim { it <= ' ' }
            )
        ) {
            CommonUtil.showToast(this, "请输入技能")
            return
        }
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

        if (TextUtils.isEmpty(et_company_introduce.text.toString())) {
            CommonUtil.showToast(this, "请输入公司介绍")
            return
        }
        if (TextUtils.isEmpty(adCode)) {
            CommonUtil.showToast(this, "请选择服务地区")
            return
        }

        mMap["name"] = et_company_name.text.toString();
        mMap["abbreviation"] = et_company_abbreviation.text.toString()
        mMap["area"] = adCode!!
        mMap["info"] = et_company_introduce.text.toString()
        mMap["mainBusiness"] = workerSkill
        mMap["phone"] = et_company_phone.text.toString()

        if (!TextUtils.isEmpty(companyLogoPath)) {
            if (upImage) {
                mMap["profile"] = profile //展示头像
            }
        } else {
            mMap["profile"] = oldprofile!! //展示头像
        }
        if (!TextUtils.isEmpty(licencePhotoPath)) {
            if (upImage) {
                mMap["businessLicenseImg"] = img //执照
            }
        } else {
            mMap["businessLicenseImg"] = oldbusinessLicenseImg!! //执照
        }
        Logger.d("提交数据：-->" + mMap)

        Logger.d("submit--展示图片：-->" + mPathList)

        mApp!!.getLoadingDialog()!!.show()

        for (image in mPathList) {
            if (!image.startsWith("http")) {
                mUploadPathList.add(image)
            } else {
                mPostPathList.add(image)
            }
        }
        if (mUploadPathList.size > 0) {
            mViewModel.uploadFiles(mUploadPathList)
        } else {
            mMap["img"] = CommonUtil.listToString(mPostPathList)!!    //展示图片
            mViewModel.onCompanySettleIn(mMap)
        }
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
                adCode = data?.extras!!["adCode"] as String?
                Log.e("传值", "选择城市------>: $cityName")
                Log.e("传值", "onActivityResult: " + Gson().toJson(cityString))
                if (cityName!!.contains("全国")) {
                    tv_company_address.text = "全国-不限-不限"
                } else {
                    tv_company_address.setText(cityName)
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
                        Luban.with(BaseApplication.instance)
                            .load(uri.path)
                            .ignoreBy(100)
                            .setCompressListener(object : OnCompressListener {
                                override fun onStart() {}
                                override fun onSuccess(file: File) {
                                    try {
                                        if (selectIndex == 1) {
                                            Glide.with(BaseApplication.instance)
                                                .applyDefaultRequestOptions(MyAppGlideModule.getRequestOptions())
                                                .load(uri.path)
                                                .into(
                                                    riv_company_logo
                                                )
                                            isPro = true
                                            companyLogoPath = uri.path
                                            upImage = false
                                            mApp!!.getLoadingDialog()!!.show()
                                            mViewModel.uploadFile(companyLogoPath.toString())
                                        } else if (selectIndex == 2) {
                                            Glide.with(BaseApplication.instance)
                                                .applyDefaultRequestOptions(MyAppGlideModule.getRequestOptions())
                                                .load(uri.path)
                                                .into(
                                                    licencePhoto
                                                )
                                            licencePhotoPath = uri.path
                                            isPro = false
                                            upImage = false
                                            mApp!!.getLoadingDialog()!!.show()
                                            mViewModel.uploadFile(licencePhotoPath.toString())

                                        } else if (selectIndex == 3) {


                                            mPathList.add(file.absolutePath)
                                            mGridViewAddImgesAdpter!!.notifyDataSetChanged(
                                                mPathList
                                            )
                                            Logger.e(
                                                "choose--展示图片--->" + Arrays.asList(
                                                    mPathList
                                                )
                                            )
                                        }
                                    } catch (e: Exception) {
                                        e.printStackTrace()
                                    }
                                }

                                override fun onError(e: Throwable) {
                                    e.printStackTrace()
                                }
                            }).launch()
                    }

                    override fun handleCropError(data: Intent?) {
                        CommonUtil.showToast(BaseApplication.instance, "图片错误")
                    }
                })
        }
    }

    override fun onBackPressed() = finish()
}


