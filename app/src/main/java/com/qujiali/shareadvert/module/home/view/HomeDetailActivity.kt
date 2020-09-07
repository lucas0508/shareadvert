package com.qujiali.shareadvert.module.home.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.SeekBar
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
import com.qujiali.shareadvert.base.view.BaseLifeCycleActivity
import com.qujiali.shareadvert.base.view.ViewHolder
import com.qujiali.shareadvert.common.utils.CommonUtil
import com.qujiali.shareadvert.common.utils.CommonUtil.stringToList
import com.qujiali.shareadvert.common.utils.Constant
import com.qujiali.shareadvert.common.utils.GlideImageLoader
import com.qujiali.shareadvert.common.utils.SPreference
import com.qujiali.shareadvert.custom.captchaview.SwipeCaptchaView
import com.qujiali.shareadvert.module.home.model.CompanyDataResponse
import com.qujiali.shareadvert.module.home.viewmodel.HomeDetailViewModel
import com.youth.banner.BannerConfig
import com.youth.banner.Transformer
import kotlinx.android.synthetic.main.activity_home_detail.*
import kotlinx.android.synthetic.main.layout_toolbar.*


class HomeDetailActivity : BaseLifeCycleActivity<HomeDetailViewModel>() {


    private var cityCode: String by SPreference(Constant.SP_APP_CITY_CODE, "")

    private var phone: String = ""

    private val mCompanyId: Int? by lazy { intent?.getIntExtra(Constant.KEY_COMPANYID, 0) }


    override fun getLayoutId(): Int = R.layout.activity_home_detail

    private var companyData: CompanyDataResponse? = null

    private var mAdapter: Adapter<String>? = null

    override fun initView() {
        super.initView()
        toolbar.setNavigationIcon(R.mipmap.icon_back)
        toolbar.setNavigationOnClickListener {
            finish()
        }
        toolbarTextView.text = "详情"

        tv_look_number.setOnClickListener {
            mViewModel.onQueryBanner(cityCode, "2")
        }
        tv_call_number.setOnClickListener {
            callPhone(phone)
        }
        mBanner.apply {
            setOnBannerListener { position ->
                //CommonUtil.startWebView(requireContext(), urls[position], titles[position])
            }
            setImageLoader(GlideImageLoader())
            setBannerStyle(BannerConfig.NUM_INDICATOR)
            setDelayTime(5000)
            setBannerAnimation(Transformer.DepthPage)
        }
        initRecycler()
        showSuccess()
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
                        Glide.with(this@HomeDetailActivity).load(data).into(iv_image!!);
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

    override fun initData() {
        super.initData()
        mViewModel.loadCompanyData(mCompanyId)
    }

    override fun initDataObserver() {
        super.initDataObserver()
        mViewModel.mCompanyData.observe(this, Observer {
            setData(it)
        })

        mViewModel.onQueryBanner.observe(this, Observer {
            it?.let {
                showCaptchaDialog(it)
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun setData(it: CompanyDataResponse?) {
        phone=it!!.phone
        companyData = it
        tvTitle.text = it?.name
        tvNumber.text = "联系电话： ${it?.phoneHide}"
        tvHomeCity.text = "服务区域：${it?.areaList?.joinToString("")}"
        tv_introduce.text = it?.info
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
        val stringToList = stringToList(it?.img!!)
        mAdapter!!.update(stringToList)

        mBanner.setImages(stringToList)
        mBanner.start()
    }

    override fun onBackPressed() {
        finish()
    }

    override fun showCreateReveal(): Boolean = false

    override fun showDestroyReveal(): Boolean = false


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

                //Toast.makeText(TaskDetailActivity.this, "验证成功", Toast.LENGTH_SHORT).show();
                //swipeCaptcha.createCaptcha();
                tv_look_number.visibility = View.GONE
                tv_call_number.visibility = View.VISIBLE
                tvNumber.text = "联系电话："+phone
                mSeekBar.isEnabled = false
                alertDialog.dismiss()
            }

            override fun matchFailed(swipeCaptchaView: SwipeCaptchaView) {
                CommonUtil.showToast(this@HomeDetailActivity, "验证失败")
                swipeCaptchaView.resetCaptcha()
                mSeekBar.progress = 0
            }
        })
        mSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
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