package com.qujiali.shareadvert.module.home.view

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.gson.Gson
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter
import com.orhanobut.logger.Logger
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.base.view.Adapter
import com.qujiali.shareadvert.base.view.BaseApplication
import com.qujiali.shareadvert.base.view.BaseLifeCycleFragment
import com.qujiali.shareadvert.base.view.ViewHolder
import com.qujiali.shareadvert.common.state.UserInfo
import com.qujiali.shareadvert.common.utils.*
import com.qujiali.shareadvert.custom.address.Address2PickerView
import com.qujiali.shareadvert.custom.address.YwpAddressBean
import com.qujiali.shareadvert.module.home.model.CompanyDataResponse
import com.qujiali.shareadvert.module.home.viewmodel.HomeViewModel
import com.qujiali.shareadvert.module.settlein.view.SettleInCompanyActivity
import com.qujiali.shareadvert.module.settlein.view.SettleInCompanyStatusActivity
import kotlinx.android.synthetic.main.fragment_mine.*
import kotlinx.android.synthetic.main.fragment_navigation.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class HomeFragment : BaseLifeCycleFragment<HomeViewModel>() {

    private var cityCode: String by SPreference(Constant.SP_APP_CITY_CODE, "")

    private var mCityCode: String = ""
    private var mCurrentPage = 1

    private var mAdapter: Adapter<CompanyDataResponse>? = null

    private var isRefresh = false

    private var searchContent: String = ""

    private val popupWindow: PopupWindow? = null

    private var userRole = "NONE"

    companion object {
        fun getInstance(): HomeFragment? {
            return HomeFragment()
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_navigation

    override fun initView() {
        super.initView()
        initRecyclerView()

        //解决滑动冲突
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            refreshLayout.isEnabled = verticalOffset >= 0
        })
        mViewModel.loadUserInfo()
        mViewModel.loadBannerCo(cityCode, "2")
        mViewModel.loadNoticeDataList()
        // mViewModel.loadBannerCo("150125", "2")
        mViewModel.loadCompanyDataList(mCurrentPage, mCityCode, searchContent)
        chooseCity.setOnClickListener {
            if (null==popupWindow || !popupWindow.isShowing){
                showAddressPickerPop()
            }
        }
        iv_singin.setOnClickListener {
            if (mApp?.isLogin()!!) {
                if ("ENTERPRISE" == userRole) {
                    /**
                     * 入驻之后
                     */
                    startActivity<SettleInCompanyStatusActivity>(requireContext()) {
                    }
                } else {
                    /**
                     * 第一次入驻 +  修改
                     */
                    startActivity<SettleInCompanyActivity>(requireContext())
                }
            } else {
                UserInfo.instance.login(requireActivity())
            }
        }
        et_search.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                searchContent = s.toString()
                Log.e("TAG", "onMultiClick: $searchContent")
            }
        })
        tv_search.setOnClickListener(object : OnMultiClickListener() {
            override fun onMultiClick(v: View?) {
                //if (TextUtils.isEmpty(mCityCode)) cityCode else mCityCode
                isRefresh = true
                mViewModel.loadCompanyDataList(
                    1
                    , mCityCode
                    , searchContent
                )
            }
        })
        initFabButton()
    }

    private fun initRecyclerView() {

        val myLinearLayoutManager = LinearLayoutManager(activity)
        navCardRecycler.setLayoutManager(myLinearLayoutManager)
        mAdapter = object : Adapter<CompanyDataResponse>(activity) {
            override fun OnCreateViewHolder(
                parent: ViewGroup?,
                viewType: Int
            ): BaseViewHolder<*>? {
                return object : ViewHolder<CompanyDataResponse?>(parent, R.layout.home_item) {
                    private var home_title: TextView? = null
                    private var home_time: TextView? = null
                    private var home_content: TextView? = null
                    private var home_city: TextView? = null
                    private var home_review: TextView? = null
                    private var home_type1: TextView? = null
                    private var home_type2: TextView? = null
                    private var home_type3: TextView? = null
                    private var hone_image: ImageView? = null

                    override fun initView() {

                        home_title = `$`(R.id.home_title)
                        home_time = `$`(R.id.home_time)
                        home_content = `$`(R.id.home_content)
                        home_city = `$`(R.id.home_city)
                        home_review = `$`(R.id.home_review)
                        hone_image = `$`(R.id.hone_image)
                        home_type1 = `$`(R.id.home_type1)
                        home_type2 = `$`(R.id.home_type2)
                        home_type3 = `$`(R.id.home_type3)

                    }

                    override fun setData(data: CompanyDataResponse?) {
                        super.setData(data)
                        Logger.d("adapyer数据返回：(${data})")
                        home_title!!.text = data?.abbreviation
                        home_time!!.text = data?.createTime
                        home_content!!.text = data?.info
                        home_city!!.text = data?.areaList?.joinToString("")
                        home_review!!.text=data?.review
                        Glide.with(this@HomeFragment)
                            .applyDefaultRequestOptions(MyAppGlideModule.getRequestOptions())
                            .load(data?.img!!.split(",")[0]).into(hone_image!!)

                        if (!TextUtils.isEmpty(data.businessLicenseImg)) {
                            val split = data.businessLicenseImg.split(",")
                            Glide.with(BaseApplication.instance)
                                .applyDefaultRequestOptions(MyAppGlideModule.getRequestOptions())
                                .load(split[0])
                                .into(hone_image!!)
                        } else {
                            hone_image!!.setImageResource(R.mipmap.ic_launcher)
                        }
                        val split1 = data.mainBusiness.split(",")
                        when (split1.size) {
                            1 -> {
                                home_type1!!.text = split1[0]
                                home_type1!!.visibility = View.VISIBLE
                                home_type2!!.visibility = View.GONE
                                home_type3!!.visibility = View.GONE
                            }
                            2 -> {
                                home_type1!!.text = split1[0]
                                home_type2!!.text = split1[1]
                                home_type1!!.visibility = View.VISIBLE
                                home_type2!!.visibility = View.VISIBLE
                                home_type3!!.visibility = View.GONE
                            }
                            3 -> {
                                home_type1!!.text = split1[0]
                                home_type2!!.text = split1[1]
                                home_type3!!.text = split1[2]
                                home_type1!!.visibility = View.VISIBLE
                                home_type2!!.visibility = View.VISIBLE
                                home_type3!!.visibility = View.VISIBLE
                            }
                            else -> {
                            }
                        }

                    }
                }
            }
        }
        navCardRecycler.setAdapterWithProgress(mAdapter)

        refreshLayout.autoRefresh()

        refreshLayout.setOnRefreshListener {
            isRefresh = true
            onRefreshData()
        }

        mAdapter!!.setMore(object : RecyclerArrayAdapter.OnMoreListener {
            override fun onMoreShow() {
                isRefresh = false
                onLoadMoreData()
            }

            override fun onMoreClick() {}
        })
        mAdapter!!.setOnItemClickListener {
            if (mApp?.isLoginToDialog()!!) {
                var item = mAdapter!!.getItem(it)
                startActivity<HomeDetailActivity>(requireActivity()) {
                    putExtra(Constant.KEY_COMPANYID, item!!.companyId)
                }
            }
        }
        // navCardRecycler.setEmptyView(R.layout.recycler_nomore_data)
    }


    private fun initFabButton() {
        fab_add.setOnClickListener {
//            navCardRecycler.smoothScrollToPosition(0)
            navCardRecycler.scrollToPosition(0)
            val objectAnimatorX =
                ObjectAnimator.ofFloat(fab_add, "scaleX", 1.0f, 1.2f, 0.0f)
            objectAnimatorX.interpolator = AccelerateDecelerateInterpolator()
            val objectAnimatorY =
                ObjectAnimator.ofFloat(fab_add, "scaleY", 1.0f, 1.2f, 0.0f)
            objectAnimatorY.interpolator = AccelerateDecelerateInterpolator()
            val animatorSet = AnimatorSet()
            animatorSet.playTogether(objectAnimatorX, objectAnimatorY)
            animatorSet.duration = 1000
            animatorSet.start()
        }
    }

    override fun initData() {
        super.initData()
    }

    override fun initDataObserver() {
        mViewModel.mUserInfo.observe(this, Observer {
            it?.let {
                if (it.code == 200) {
                    userRole = it.data.userRole
                }
            }
        })
        mViewModel.mBannerData.observe(this, Observer { response ->
            response?.let { loginResponse ->
                Glide.with(BaseApplication.instance)
                    .applyDefaultRequestOptions(MyAppGlideModule.getRequestOptions())
                    .load(loginResponse).into(iv_banner)
            }
        })

        mViewModel.mCompanyDataList.observe(this, Observer {
            addData(it)
        })

        mViewModel.mNoticeDataList.observe(this, Observer {
            it.let {
             marqueeView.setContent(it.rows[0].noticeContent)
            }
        })
    }

    private fun addData(articleList: List<CompanyDataResponse>) {
        // 返回列表为空显示加载完毕
        Logger.d("数据返回：(${articleList})")
        if (isRefresh) {
            mAdapter!!.update(articleList)
        } else {
            mAdapter!!.addAll(articleList)
        }
        refreshLayout.finishRefresh()
        refreshLayout.finishLoadMore() //结束加载（加载失败）

    }

    private fun onRefreshData() {
        mCurrentPage = 1
        mViewModel.loadCompanyDataList(
            mCurrentPage
            , mCityCode
            , searchContent
        )
    }

    private fun onLoadMoreData() {
        mViewModel.loadCompanyDataList(
            ++mCurrentPage
            , mCityCode
            , searchContent
        )
    }


    /**
     * 显示地址选择的pop
     */
    private fun showAddressPickerPop() {

        var popupWindow = PopupWindow(requireContext())
        val rootView: View =
            LayoutInflater.from(requireContext())
                .inflate(R.layout.pop_addresstwo_picker, null, false)
        val addressView: Address2PickerView = rootView.findViewById(R.id.apvAddress)
        addressView.setOnAddressPickerSure(object : Address2PickerView.OnAddressPickerSureListener {
            override fun onSureClick(
                allAddress: String?,
                address: String?,
                provinceCode: String?,
                cityCode: String?,
                districtCode: String?
            ) {

                mCityCode = cityCode!!
                chooseCity.text = address
                popupWindow.dismiss()
                chooseCity.isEnabled = true
                isRefresh = true
                //(if (TextUtils.isEmpty(mCityCode)) cityCode else mCityCode).toString()
                mViewModel.loadCompanyDataList(
                    1, mCityCode
                    , searchContent
                )
            }

        }, true)
        popupWindow.contentView = rootView
        popupWindow.setBackgroundDrawable(null)
        popupWindow.isOutsideTouchable = true
        popupWindow.isTouchable = true
        popupWindow.width = ViewGroup.LayoutParams.MATCH_PARENT
        popupWindow.height = ViewGroup.LayoutParams.MATCH_PARENT
        popupWindow.isFocusable = true;
        popupWindow.showAsDropDown(chooseCity)
        val jsonSB = StringBuilder()
        try {
            val addressJsonStream = BufferedReader(
                InputStreamReader(
                    BaseApplication.instance.assets.open("address.json")
                )
            )
            var line: String?
            while (addressJsonStream.readLine().also { line = it } != null) {
                jsonSB.append(line)
            }
        } catch (e: IOException) {
        }
        var mYwpAddressBean: YwpAddressBean? = null
        // 将数据转换为对象
        mYwpAddressBean = Gson().fromJson(jsonSB.toString(), YwpAddressBean::class.java)
        addressView.initData(mYwpAddressBean)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        if (hidden) {
            //相当于Fragment的onPause()
        } else {
            // 相当于Fragment的onResume()
            mViewModel.loadUserInfo()
            mViewModel.loadBannerCo(cityCode, "2")
        }
    }

//    override fun reLoad() {
//        showLoading()
//        onRefreshData()
//        super.reLoad()
//    }
}