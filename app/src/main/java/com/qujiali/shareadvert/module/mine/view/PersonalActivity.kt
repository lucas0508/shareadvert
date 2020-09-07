package com.qujiali.shareadvert.module.mine.view

import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.orhanobut.logger.Logger
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.base.view.BaseApplication
import com.qujiali.shareadvert.base.view.BaseLifeCycleActivity
import com.qujiali.shareadvert.common.utils.CommonUtil
import com.qujiali.shareadvert.common.utils.Constant
import com.qujiali.shareadvert.common.utils.MyAppGlideModule
import com.qujiali.shareadvert.custom.takePhoto.PhotoUtils
import com.qujiali.shareadvert.module.mine.viewmodel.MineViewModel
import kotlinx.android.synthetic.main.activity_personal.*
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File

class PersonalActivity : BaseLifeCycleActivity<MineViewModel>() {

//    private val mUserImage: String? by lazy { intent?.getStringExtra(Constant.KEY_IMGPATH) }
//    private val mUserName: String? by lazy { intent?.getStringExtra(Constant.KEY_USERNAME) }

    private var imagePath: String = ""

    private var mUserImage = ""

    override fun getLayoutId(): Int = R.layout.activity_personal

    override fun initView() {
        super.initView()
        toolbar.setNavigationIcon(R.mipmap.icon_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        mViewModel.loadUserInfo()
        toolbarTextView.text = "个人中心"


        riv_headImage.setOnClickListener {
            mApp!!.getSelectPicDialog()?.show { position ->
                if (position == 0) {
                    PhotoUtils.autoObtainCameraPermission(this, true)
                } else if (position == 1) {
                    PhotoUtils.autoObtainStoragePermission(this, true)
                }
            }
        }
        btn_submit.setOnClickListener {
            submitData()
        }
        showSuccess()
    }

    private fun submitData() {
        val mMap = mutableMapOf<String, Any>()
        if (TextUtils.isEmpty(imagePath)){
            mMap["profile"] = mUserImage!!
        }else{
            mMap["profile"] =imagePath
        }
        mMap["name"] = et__person_nickName.text.toString()
        mViewModel.loadModifyPersonData(mMap)
    }

    override fun initDataObserver() {

        mViewModel.mUserInfo.observe(this, Observer {
            it?.let {
                if (it.code == 200) {
                    if (!TextUtils.isEmpty(it.data.profile)){
                        mUserImage=it.data.profile
                        Glide.with(this).applyDefaultRequestOptions(MyAppGlideModule.getRequestOptions())
                            .load(it.data.profile).into(riv_headImage)
                    }
                    et__person_nickName.setText(it.data.name)
                }
            }
        })

        mViewModel.uploadFile.observe(this, Observer {
            imagePath = it
            Glide.with(this).applyDefaultRequestOptions(MyAppGlideModule.getRequestOptions())
                .load(it).into(riv_headImage)

        })

        mViewModel.mModifyData.observe(this, Observer {
            it.let {
                CommonUtil.showToast(this, "修改成功！")
                finish()
            }

        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
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
                            }

                        }).launch()
                }

                override fun handleCropError(data: Intent?) {
                    CommonUtil.showToast(BaseApplication.instance, "图片错误")
                }
            })
    }


    override fun showCreateReveal(): Boolean = true

    override fun showDestroyReveal(): Boolean = false

    override fun onBackPressed() = finish()
}