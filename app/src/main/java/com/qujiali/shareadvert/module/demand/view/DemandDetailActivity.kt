package com.qujiali.shareadvert.module.demand.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.orhanobut.logger.Logger
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.base.view.Adapter
import com.qujiali.shareadvert.base.view.BaseApplication
import com.qujiali.shareadvert.base.view.BaseLifeCycleActivity
import com.qujiali.shareadvert.base.view.ViewHolder
import com.qujiali.shareadvert.common.utils.CommonUtil
import com.qujiali.shareadvert.common.utils.Constant
import com.qujiali.shareadvert.common.utils.MyAppGlideModule
import com.qujiali.shareadvert.common.utils.SPreference
import com.qujiali.shareadvert.custom.captchaview.SwipeCaptchaView
import com.qujiali.shareadvert.module.banner.viewmodel.BannerViewModel
import com.qujiali.shareadvert.module.demand.viewmodel.HomeFragmentDemandModel
import com.qujiali.shareadvert.module.home.view.HomeDetailActivity
import com.qujiali.shareadvert.module.resources.model.ResourcesResponse
import com.qujiali.shareadvert.network.response.BaseResponse
import kotlinx.android.synthetic.main.activity_home_detail.*
import kotlinx.android.synthetic.main.activity_home_detail.tvHomeCity
import kotlinx.android.synthetic.main.activity_home_detail.tvNumber
import kotlinx.android.synthetic.main.activity_home_detail.tvTitle
import kotlinx.android.synthetic.main.activity_home_detail.tv_introduce
import kotlinx.android.synthetic.main.activity_resources.*
import kotlinx.android.synthetic.main.layout_toolbar.*
import kotlinx.android.synthetic.main.activity_home_detail.recycler_view as recycler_view1
import kotlinx.android.synthetic.main.activity_home_detail.tv_look_number as tv_look_number1
import kotlinx.android.synthetic.main.activity_resources.tv_call_number as tv_call_number1

class DemandDetailActivity : BaseLifeCycleActivity<HomeFragmentDemandModel>() {
    private var cityCode: String by SPreference(Constant.SP_APP_CITY_CODE, "")

    private val mResourcesId: Int? by lazy { intent?.getIntExtra(Constant.KEY_RESOURCESID, 0) }

    private var mAdapter: Adapter<String>? = null

    private var userId: Int = 0

    private var phone: String = ""

    private var userRole: String? = null

    override fun getLayoutId(): Int = R.layout.activity_resources

    override fun initView() {
        super.initView()
        toolbar.setNavigationIcon(R.mipmap.icon_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbarTextView.text = "详情"
        mViewModel.loadDemandSingleData(mResourcesId!!)
        initRecycler()
        llGo.setOnClickListener {
            if ("NONE" != userRole){
                com.qujiali.shareadvert.common.utils.startActivity<HomeDetailActivity>(this) {
                    putExtra(Constant.KEY_COMPANYID, userId)
                }
            }
        }
        tv_look_number.setOnClickListener {
            mViewModel.onQueryBanner(cityCode, "3")
        }
        tv_call_number.setOnClickListener {
            callPhone(phone)
        }
        showSuccess()
    }

    override fun initDataObserver() {
        mViewModel.demandSingleData.observe(this, Observer {
            it?.let {
                setData(it)
            }
        })

        mViewModel.onQueryBanner.observe(this, Observer {
            it?.let {
                showCaptchaDialog(it)
                mViewModel.loadDemandLookPhone(mResourcesId!!)
            }
        })
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
                        Glide.with(this@DemandDetailActivity).load(data).into(iv_image!!);
                    }
                }
            }
        }
        recycler_view.adapter = mAdapter
    }


    private fun setData(it: BaseResponse<ResourcesResponse>) {
        phone = it.data.phone
        userId = it.data.userDetails.userId
        userRole = it.data.userRole
        tvTitle.text = it.data.title
        tvTime.text=it.data.createTime
        tvNumber.text = "联系电话： ${it.data.phoneHide}"
        tvHomeCity.text = "所在位置：${it.data.areaList?.joinToString("")}"
        tv_introduce.text = it.data.info
        tvCompanyName.text = it.data.userDetails.name
        Glide.with(BaseApplication.instance)
            .applyDefaultRequestOptions(MyAppGlideModule.getRequestOptions())
            .load(it.data.userDetails.profile)
            .into(
                riv_image
            )

        val stringToList = CommonUtil.stringToList(it.data.img)
        mAdapter!!.update(stringToList)
    }

    @SuppressLint("CheckResult")
    private fun showCaptchaDialog(url: String) {
        Logger.t("图片路径：：：：").e(url + "")
        val builder =
            AlertDialog.Builder(this, R.style.ActionSheetDialogStyle)
        val view: View =
            LayoutInflater.from(this).inflate(R.layout.dailog_captcha, null)
        val mSwipeCaptchaView: SwipeCaptchaView =
            view.findViewById<View>(R.id.swipeCaptchaView) as SwipeCaptchaView
        val mSeekBar = view.findViewById<View>(R.id.dragBar) as SeekBar
        builder.setView(view)
        builder.setCancelable(false)
        val alertDialog = builder.show()

        //获取当前Activity所在的窗体
        val dialogWindow = alertDialog.window
        //设置Dialog从窗体底部弹出
        dialogWindow!!.decorView.setPadding(0, 0, 0, 0)
        alertDialog.setCanceledOnTouchOutside(false)
        view.findViewById<View>(R.id.btnChange)
            .setOnClickListener {
                mSwipeCaptchaView.createCaptcha()
                mSeekBar.isEnabled = true
                mSeekBar.progress = 0
            }
        mSwipeCaptchaView.setOnCaptchaMatchCallback(object :
            SwipeCaptchaView.OnCaptchaMatchCallback {
            override fun matchSuccess(swipeCaptchaView: SwipeCaptchaView?) {
                //Toast.makeText(TaskDetailActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                //swipeCaptcha.createCaptcha();
                tv_look_number.visibility = View.GONE
                tv_call_number.visibility = View.VISIBLE
                tvNumber.text = "联系电话："+phone
                mSeekBar.isEnabled = false
                alertDialog.dismiss()
            }

            override fun matchFailed(swipeCaptchaView: SwipeCaptchaView) {
                CommonUtil.showToast(this@DemandDetailActivity, "验证失败")
                swipeCaptchaView.resetCaptcha()
                mSeekBar.progress = 0
            }
        })
        mSeekBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(
                seekBar: SeekBar,
                progress: Int,
                fromUser: Boolean
            ) {
                mSwipeCaptchaView.setCurrentSwipeValue(progress)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {
                //随便放这里是因为控件
                mSeekBar.max = mSwipeCaptchaView.getMaxSwipeValue()
            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {
                Log.d(
                    "zxt",
                    "onStopTrackingTouch() called with: seekBar = [$seekBar]"
                )
                mSwipeCaptchaView.matchCaptcha()
            }
        })
        Glide.with(this).asBitmap().listener(object : RequestListener<Bitmap?> {
            override fun onLoadFailed(
                e: GlideException?,
                model: Any,
                target: Target<Bitmap?>,
                isFirstResource: Boolean
            ): Boolean {
                val resource =
                    BitmapFactory.decodeResource(resources, R.mipmap.captcha_icon)
                mSwipeCaptchaView.setImageBitmap(resource)
                mSwipeCaptchaView.createCaptcha()
                mSwipeCaptchaView.setBackgroundResource(R.mipmap.captcha_icon)
                return false
            }

            override fun onResourceReady(
                resource: Bitmap?,
                model: Any,
                target: Target<Bitmap?>,
                dataSource: DataSource,
                isFirstResource: Boolean
            ): Boolean {
                mSwipeCaptchaView.setImageBitmap(resource)
                mSwipeCaptchaView.createCaptcha()
                return false
            }
        }).load(url).into(mSwipeCaptchaView)
    }


    override fun showCreateReveal(): Boolean = true

    override fun showDestroyReveal(): Boolean = false


    override fun onBackPressed() = finish()

    /**
     * 拨打电话（跳转到拨号界面，用户手动点击拨打）
     *
     * @param phoneNum 电话号码
     */
    fun callPhone(phoneNum: String) {
        val intent = Intent(Intent.ACTION_DIAL)
        val data = Uri.parse("tel:$phoneNum")
        intent.data = data
        startActivity(intent)
    }
}