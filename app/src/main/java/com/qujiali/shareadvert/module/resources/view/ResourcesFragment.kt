package com.qujiali.shareadvert.module.resources.view

import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.jude.easyrecyclerview.adapter.BaseViewHolder
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter.OnMoreListener
import com.orhanobut.logger.Logger
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.base.view.Adapter
import com.qujiali.shareadvert.base.view.BaseApplication
import com.qujiali.shareadvert.base.view.BaseLifeCycleFragment
import com.qujiali.shareadvert.base.view.ViewHolder
import com.qujiali.shareadvert.common.utils.*
import com.qujiali.shareadvert.custom.address.Address2PickerView
import com.qujiali.shareadvert.custom.address.YwpAddressBean
import com.qujiali.shareadvert.module.resources.model.ResourcesResponse
import com.qujiali.shareadvert.module.resources.viewmodel.ResourcesViewModel
import com.scwang.smartrefresh.layout.api.RefreshLayout
import com.scwang.smartrefresh.layout.listener.OnRefreshListener
import kotlinx.android.synthetic.main.fragment_resourcescurrent.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader


class ResourcesFragment : BaseLifeCycleFragment<ResourcesViewModel>() {

    private var cityCode: String by SPreference(Constant.SP_APP_CITY_CODE, "")

    private var mCurrentPage = 1

    private var resourcesAdapter: Adapter<ResourcesResponse>? = null
    private lateinit var mLinearSnapHelper: LinearSnapHelper
    private var searchContent: String = ""
    private var mCityCode: String = ""


    private var isRefresh = false

    companion object {
        fun getInstance(): ResourcesFragment? {
            return ResourcesFragment()
        }
    }

    override fun getLayoutId(): Int = R.layout.fragment_resourcescurrent

    override fun initView() {
        super.initView()
        initRecyclerView()
        iv_release.setBackgroundResource(R.mipmap.icon_release_resource)
        iv_release.setOnClickListener {
            if (mApp?.isLoginToDialog()!!) {
                startActivity<PublishResourcesActivity>(
                    requireContext()
                )
            }

        }
        tv_search.setOnClickListener(object : OnMultiClickListener() {
            override fun onMultiClick(v: View?) {
                isRefresh = true
                mViewModel.loadResourceAnonList(
                    1
                    , mCityCode
                    , searchContent
                )
            }
        })
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

        chooseCity.setOnClickListener {
            showAddressPickerPop()
        }
    }

    private fun initRecyclerView() {
        val myLinearLayoutManager = LinearLayoutManager(activity)
        mRvArticle.setLayoutManager(myLinearLayoutManager)

        resourcesAdapter = object : Adapter<ResourcesResponse>(activity) {
            override fun OnCreateViewHolder(
                parent: ViewGroup?,
                viewType: Int
            ): BaseViewHolder<*>? {
                return object : ViewHolder<ResourcesResponse?>(parent, R.layout.resources_item) {
                    private var home_title: TextView? = null
                    private var home_time: TextView? = null
                    private var home_content: TextView? = null
                    private var home_city: TextView? = null
                    private var home_call: TextView? = null
                    private var home_icon: ImageView? = null
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
                        home_icon = `$`(R.id.home_icon)
                        home_call = `$`(R.id.home_call)
                        home_review = `$`(R.id.home_review)
                        hone_image = `$`(R.id.hone_image)
                        home_type1 = `$`(R.id.home_type1)
                        home_type2 = `$`(R.id.home_type2)
                        home_type3 = `$`(R.id.home_type3)

                    }

                    override fun setData(data: ResourcesResponse?) {
                        super.setData(data)
                        home_title!!.text = data?.title
                        home_time!!.text = data?.createTime
                        home_content!!.text = data?.info
                        home_city!!.text = data?.areaList?.joinToString("")
                        home_call!!.text = data?.callTimes.toString()
                        home_review!!.text = data?.viewTimes.toString()
                        if ( "NONE" != data?.userRole){
                            home_icon!!.visibility = View.VISIBLE
                        }
                        Glide.with(this@ResourcesFragment)
                            .applyDefaultRequestOptions(MyAppGlideModule.getRequestOptions())
                            .load(data?.img!!.split(",")[0]).into(hone_image!!)

                        if (!TextUtils.isEmpty(data.keyword)) {
                            val split1 = data.keyword.split(",")
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
        }
        mRvArticle.setAdapterWithProgress(resourcesAdapter)


        mRvArticle.setRefreshListener(SwipeRefreshLayout.OnRefreshListener {
            isRefresh = true
            mCurrentPage = 1
            mViewModel.loadResourceAnonList(
                mCurrentPage
                , mCityCode
                , searchContent
            )
        })
        resourcesAdapter!!.setMore(object : OnMoreListener {
            override fun onMoreShow() {
                if (!isRefresh) {
                    mViewModel.loadResourceAnonList(
                        ++mCurrentPage
                        , mCityCode
                        , searchContent
                    )
                }
            }

            override fun onMoreClick() {}
        })
        resourcesAdapter!!.setOnItemClickListener {
            if (mApp?.isLoginToDialog()!!) {
                val item = resourcesAdapter!!.getItem(it)
                startActivity<ResourcesDetailActivity>(requireActivity()) {
                    putExtra(Constant.KEY_RESOURCESID, item!!.resourceId)
                }
            }
        }
        mRvArticle.setEmptyView(R.layout.recycler_nomore_data)
    }

    override fun initDataObserver() {
        mViewModel.resourceAnonList.observe(this, Observer {
            it?.let {
                addData(it)
            }
        })
    }

    override fun initData() {
        super.initData()
        mViewModel.loadResourceAnonList(mCurrentPage, mCityCode, searchContent)
    }

    private fun addData(articleList: List<ResourcesResponse>) {
        Logger.d("数据返回：(${articleList})")
        if (isRefresh) {
            resourcesAdapter!!.update(articleList)
        } else {
            resourcesAdapter!!.addAll(articleList)
        }
    }


    /**
     * 显示地址选择的pop
     */
    private fun showAddressPickerPop() {
        val popupWindow = PopupWindow(activity)
        val rootView: View =
            LayoutInflater.from(activity).inflate(R.layout.pop_addresstwo_picker, null, false)
        val addressView: Address2PickerView = rootView.findViewById(R.id.apvAddress)
        addressView.setOnAddressPickerSure({ allAddress, address, provinceCode, cityCode, districtCode ->
            if (cityCode != null) {
                mCityCode = cityCode
            }
            chooseCity.text = address
            popupWindow.dismiss()
            chooseCity.isEnabled = true
            isRefresh = true
            mViewModel.loadResourceAnonList(
                1
                , mCityCode
                , searchContent
            )
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

}