package com.qujiali.shareadvert.custom.address

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import com.google.gson.Gson
import com.qujiali.shareadvert.R
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

class AddressPickerView : RelativeLayout, View.OnClickListener {
    // recyclerView 选中Item 的颜色
    private val defaultSelectedColor = Color.parseColor("#50AA00")

    // recyclerView 未选中Item 的颜色
    private val defaultUnSelectedColor = Color.parseColor("#262626")

    // 确定字体不可以点击时候的颜色
    private val defaultSureUnClickColor = Color.parseColor("#7F7F7F")

    // 确定字体可以点击时候的颜色
    private val defaultSureCanClickColor = Color.parseColor("#50AA00")
    private var mContext: Context? = null
    private val defaultTabCount = 3 //tab 的数量
    private var mTabLayout // tabLayout
            : TabLayout? = null
    private var mRvList // 显示数据的RecyclerView
            : RecyclerView? = null
    private val defaultProvince = "省份" //显示在上面tab中的省份
    private val defaultCity = "城市" //显示在上面tab中的城市
    private var defaultDistrict = "区县" //显示在上面tab中的区县
    private var mRvData // 用来在recyclerview显示的数据
            : MutableList<YwpAddressBean.AddressItemBean?>? = null
    private var mAdapter // recyclerview 的 adapter
            : AddressAdapter? = null
    private var mYwpAddressBean // 总数据
            : YwpAddressBean? = null
    private var mSelectProvice //选中 省份 bean
            : YwpAddressBean.AddressItemBean? = null
    private var mSelectCity //选中 城市  bean
            : YwpAddressBean.AddressItemBean? = null
    private var mSelectDistrict //选中 区县  bean
            : YwpAddressBean.AddressItemBean? = null
    private var mSelectProvicePosition = 0 //选中 省份 位置
    private var mSelectCityPosition = 0 //选中 城市  位置
    private var mSelectDistrictPosition = 0 //选中 区县  位置
    var isSelect = false
    private var mOnAddressPickerSureListener: OnAddressPickerSureListener? = null
    private var mTvSure //确定
            : TextView? = null

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?
    ) : super(context, attrs) {
        init(context)
    }

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int
    ) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    /**
     * 初始化
     */
    private fun init(context: Context) {
        mContext = context
        mRvData = ArrayList()
        // UI
        val rootView =
            View.inflate(mContext, R.layout.address_picker_view, this)
        // 确定
        mTvSure = rootView.findViewById(R.id.tvSure)
        mTvSure!!.setTextColor(defaultSureUnClickColor)
        mTvSure!!.setOnClickListener(this)
        // tablayout初始化
        mTabLayout = rootView.findViewById<View>(R.id.tlTabLayout) as TabLayout
        mTabLayout!!.addTab(mTabLayout!!.newTab().setText(defaultProvince))
        mTabLayout!!.addTab(mTabLayout!!.newTab().setText(defaultCity))
        mTabLayout!!.addTab(mTabLayout!!.newTab().setText(defaultDistrict))
        mTabLayout!!.addOnTabSelectedListener(tabSelectedListener)

        // recyclerview adapter的绑定
        mRvList = rootView.findViewById<View>(R.id.rvList) as RecyclerView
        mRvList!!.layoutManager = LinearLayoutManager(context)
        mAdapter = AddressAdapter()
        mRvList!!.adapter = mAdapter
        // 初始化默认的本地数据  也提供了方法接收外面数据
        mRvList!!.post { initData() }
    }

    /**
     * 初始化数据
     * 拿assets下的json文件
     */
    private fun initData() {
        val jsonSB = StringBuilder()
        try {
            val addressJsonStream = BufferedReader(
                InputStreamReader(
                    context.assets.open("address.json")
                )
            )
            var line: String?
            while (addressJsonStream.readLine().also { line = it } != null) {
                jsonSB.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        // 将数据转换为对象
        mYwpAddressBean =
            Gson().fromJson(jsonSB.toString(), YwpAddressBean::class.java)
        if (mYwpAddressBean != null) {
            mRvData!!.clear()
            mRvData!!.addAll(mYwpAddressBean!!.province!!)
            mAdapter!!.notifyDataSetChanged()
        }
    }

    /**
     * 开放给外部传入数据
     * 暂时就用这个Bean模型，如果数据不一致就需要各自根据数据来生成这个bean了
     */
    fun initData(bean: YwpAddressBean?) {
        if (bean != null) {
            mSelectDistrict = null
            mSelectCity = null
            mSelectProvice = null
            mTabLayout!!.getTabAt(0)!!.select()
            mYwpAddressBean = bean
            mRvData!!.clear()
            mRvData!!.addAll(mYwpAddressBean!!.province!!)
            mAdapter!!.notifyDataSetChanged()
        }
    }

    override fun onClick(v: View) {
        val i = v.id
        if (i == R.id.tvSure) {
            sure()
        }
    }

    //点确定
    private fun sure() {
        if (isSelect) {
            if (mSelectProvice != null && mSelectCity != null) {
                //   回调接口
                if (mOnAddressPickerSureListener != null) {
                    //mSelectProvice.getN() + " " + mSelectCity.getN() + " " + mSelectDistrict.getN() + " "
                    var district = ""
                    var districtName = ""
                    if (mSelectDistrict != null) {
                        district = mSelectDistrict!!.i!!
                        districtName = mSelectDistrict!!.n!!
                    } else {
                        districtName = mSelectCity!!.n!!
                        district = mSelectCity!!.i!!.substring(0, 3)
                    }
                    mOnAddressPickerSureListener!!.onSureClick(
                        null,
                        districtName,
                        mSelectProvice!!.i,
                        mSelectCity!!.i,
                        district
                    )
                }
            } else {
                Toast.makeText(mContext, "地址还没有选完整哦", Toast.LENGTH_SHORT).show()
            }
        } else {
            if (mSelectProvice != null && mSelectCity != null && mSelectDistrict != null) {
                //   回调接口
                if (mOnAddressPickerSureListener != null) {
                    //mSelectProvice.getN() + " " + mSelectCity.getN() + " " + mSelectDistrict.getN() + " "
                    mOnAddressPickerSureListener!!.onSureClick(
                        mSelectProvice!!.n + " " + mSelectCity!!.n + " " + mSelectDistrict!!.n + " ",
                        mSelectDistrict!!.n.toString() + " ",
                        mSelectProvice!!.i,
                        mSelectCity!!.i,
                        mSelectDistrict!!.i
                    )
                }
            } else {
                Toast.makeText(mContext, "地址还没有选完整哦", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mYwpAddressBean = null
    }

    /**
     * TabLayout 切换事件
     */
    var tabSelectedListener: OnTabSelectedListener = object : OnTabSelectedListener {
        override fun onTabSelected(tab: TabLayout.Tab) {
            mRvData!!.clear()
            when (tab.position) {
                0 -> {
                    mRvData!!.addAll(mYwpAddressBean!!.province!!)
                    mAdapter!!.notifyDataSetChanged()
                    // 滚动到这个位置
                    mRvList!!.smoothScrollToPosition(mSelectProvicePosition)
                }
                1 -> {
                    // 点到城市的时候要判断有没有选择省份
                    if (mSelectProvice != null) {
                        for (itemBean in mYwpAddressBean!!.city!!) {
                            if (itemBean.p == mSelectProvice!!.i)
                                mRvData!!.add(itemBean)
                        }
                    } else {
                        Toast.makeText(mContext, "请您先选择省份", Toast.LENGTH_SHORT).show()
                    }
                    mAdapter!!.notifyDataSetChanged()
                    // 滚动到这个位置
                    mRvList!!.smoothScrollToPosition(mSelectCityPosition)
                }
                2 -> {
                    // 点到区的时候要判断有没有选择省份与城市
                    if (mSelectProvice != null && mSelectCity != null) {
                        for (itemBean in mYwpAddressBean!!.district!!) {
                            if (itemBean.p.equals(mSelectCity!!.i)) mRvData!!.add(itemBean)
                        }
                    } else {
                        Toast.makeText(mContext, "请您先选择省份与城市", Toast.LENGTH_SHORT).show()
                    }
                    mAdapter!!.notifyDataSetChanged()
                    // 滚动到这个位置
                    mRvList!!.smoothScrollToPosition(mSelectDistrictPosition)
                }
            }
        }

        override fun onTabUnselected(tab: TabLayout.Tab) {}
        override fun onTabReselected(tab: TabLayout.Tab) {}
    }

    /**
     * 下面显示数据的adapter
     */
    internal inner class AddressAdapter :
        RecyclerView.Adapter<AddressAdapter.ViewHolder>() {
        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): ViewHolder {
            return ViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_address_text, parent, false)
            )
        }

        override fun onBindViewHolder(
            holder: ViewHolder,
            position: Int
        ) {
            val tabSelectPosition = mTabLayout!!.selectedTabPosition
            holder.mTitle.text = mRvData!![position]!!.n
            holder.mTitle.setTextColor(defaultUnSelectedColor)
            when (tabSelectPosition) {
                0 -> if (mRvData!![position] != null && mSelectProvice != null && mRvData!![position]!!.i == mSelectProvice!!.i
                ) {
                    holder.mTitle.setTextColor(defaultSelectedColor)
                }
                1 -> if (mRvData!![position] != null && mSelectCity != null && mRvData!![position]!!.i == mSelectCity!!.i
                ) {
                    holder.mTitle.setTextColor(defaultSelectedColor)
                }
                2 -> if (mRvData!![position] != null && mSelectDistrict != null && mRvData!![position]!!.i == mSelectDistrict!!.i
                ) {
                    holder.mTitle.setTextColor(defaultSelectedColor)
                }
            }
            // 设置点击之后的事件
            holder.mTitle.setOnClickListener {
                // 点击 分类别
                when (tabSelectPosition) {
                    0 -> {
                        mSelectProvice = mRvData!![position]
                        // 清空后面两个的数据
                        mSelectCity = null
                        mSelectDistrict = null
                        mSelectCityPosition = 0
                        mSelectDistrictPosition = 0
                        mTabLayout!!.getTabAt(1)!!.text = defaultCity
                        if (isSelect) {
                            defaultDistrict = "区县（可不限）"
                        }
                        mTabLayout!!.getTabAt(2)!!.text = defaultDistrict
                        // 设置这个对应的标题
                        mTabLayout!!.getTabAt(0)!!.text = mSelectProvice!!.n
                        // 跳到下一个选择
                        mTabLayout!!.getTabAt(1)!!.select()
                        // 灰掉确定按钮
                        mTvSure!!.setTextColor(defaultSureUnClickColor)
                        mSelectProvicePosition = position
                    }
                    1 -> {
                        mSelectCity = mRvData!![position]
                        // 清空后面一个的数据
                        mSelectDistrict = null
                        mSelectDistrictPosition = 0
                        if (isSelect) {
                            defaultDistrict = "区县（可不限）"
                        }
                        mTabLayout!!.getTabAt(2)!!.text = defaultDistrict
                        // 设置这个对应的标题
                        mTabLayout!!.getTabAt(1)!!.text = mSelectCity!!.n
                        // 跳到下一个选择
                        mTabLayout!!.getTabAt(2)!!.select()
                        // 灰掉确定按钮
                        mTvSure!!.setTextColor(defaultSureUnClickColor)
                        mSelectCityPosition = position
                    }
                    2 -> {
                        mSelectDistrict = mRvData!![position]
                        // 没了，选完了，这个时候可以点确定了
                        mTabLayout!!.getTabAt(2)!!.text = mSelectDistrict!!.n
                        notifyDataSetChanged()
                        // 确定按钮变亮
                        mTvSure!!.setTextColor(defaultSureCanClickColor)
                        mSelectDistrictPosition = position
                    }
                }
            }
        }

        override fun getItemCount(): Int {
            return mRvData!!.size
        }

        internal inner class ViewHolder(itemView: View) :
            RecyclerView.ViewHolder(itemView) {
            var mTitle: TextView

            init {
                mTitle = itemView.findViewById<View>(R.id.itemTvTitle) as TextView
            }
        }
    }
    /**
     * 点确定回调这个接口
     */
    interface OnAddressPickerSureListener {
        fun onSureClick(
            allAddress: String?,
            address: String?,
            provinceCode: String?,
            cityCode: String?,
            districtCode: String?
        )
    }

    fun setOnAddressPickerSure(listener: OnAddressPickerSureListener?) {
        mOnAddressPickerSureListener = listener
    }

    fun setOnAddressPickerSure(
        listener: OnAddressPickerSureListener?,
        isSelect: Boolean
    ) {
        mOnAddressPickerSureListener = listener
        this.isSelect = isSelect
    }
}

