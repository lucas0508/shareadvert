package com.qujiali.shareadvert.module.settlein.view

import android.content.Intent
import android.os.Parcelable
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ListView
import android.widget.TextView
import com.google.gson.Gson
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.base.view.BaseActivity
import com.qujiali.shareadvert.base.view.BaseApplication
import com.qujiali.shareadvert.common.utils.CommonUtil
import com.qujiali.shareadvert.custom.CustomPopWindow
import com.qujiali.shareadvert.module.settlein.model.AdressDataEntity_oldddd
import kotlinx.android.synthetic.main.activity_address.*
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.util.*

class AddressActivity22222222222 : BaseActivity() {


    //存储用户选择的地区
    var mapsData: List<AdressDataEntity_oldddd.RowsBean.ChildBeanXX.ChildBeanX.ChildBean> =
        ArrayList<AdressDataEntity_oldddd.RowsBean.ChildBeanXX.ChildBeanX.ChildBean>()
    var mListPopWindow: CustomPopWindow? = null
    private var adressEntity: AdressDataEntity_oldddd? = null
    private var citybeanlist: List<AdressDataEntity_oldddd.RowsBean.ChildBeanXX> =
        ArrayList<AdressDataEntity_oldddd.RowsBean.ChildBeanXX>()
    private var areabeanlist: List<AdressDataEntity_oldddd.RowsBean.ChildBeanXX.ChildBeanX> =
        ArrayList<AdressDataEntity_oldddd.RowsBean.ChildBeanXX.ChildBeanX>()

    //    private List<AdressDataEntity.RowsBean.ChildBeanXX.ChildBeanX.ChildBean> checkList;//第三级选中的数据
    private var provinceList //第一级选中的数据
            : AdressDataEntity_oldddd.RowsBean.ChildBeanXX.ChildBeanX.ChildBean? = null
    private var cityList //第二级选中的数据
            : AdressDataEntity_oldddd.RowsBean.ChildBeanXX.ChildBeanX.ChildBean? = null
    private var arealist //第一级选中的数据
            : AdressDataEntity_oldddd.RowsBean.ChildBeanXX.ChildBeanX.ChildBean? = null

    private val childBeanXX: AdressDataEntity_oldddd.RowsBean = AdressDataEntity_oldddd.RowsBean()
    override fun getLayoutId(): Int = R.layout.activity_address


    override fun initView() {
        super.initView()
        textView2.visibility=View.GONE
        tv_province.setOnClickListener(View.OnClickListener {
            showPopListView(tv_province, "1")
            tv_city.setText("")
            tv_area.setText("")
            cityList = null
            if (null != arealist) arealist = null
        })
        tv_city.setOnClickListener(View.OnClickListener {
            if (tv_province.getText().toString().trim({ it <= ' ' }) == "全国") {
                tv_city.setText("不限")
                cityList = null
            } else {
                showPopListView(tv_city, "2")
                tv_area.setText("")
            }
            if (null != arealist) arealist = null
        })

        tv_area.setOnClickListener(View.OnClickListener {
            if (tv_province.getText().toString().trim { it <= ' ' } == "全国") {
                tv_area.setText("不限")
            } else {
                showPopListView(tv_area, "3")
            }
        })

        tv_chooseAddress.setOnClickListener(View.OnClickListener {
            Log.e("onClick", "第一极: " + Gson().toJson(provinceList))
            Log.e("onClick", "第二极: " + Gson().toJson(cityList))
            Log.e("onClick", "第三极: " + Gson().toJson(arealist))
            if (provinceList == null) {
                CommonUtil.showToast(this, "请选择省")
                return@OnClickListener
            }
            if (cityList == null) {
                CommonUtil.showToast(this, "请选择市")
                return@OnClickListener
            }
            if (arealist == null) {
                CommonUtil.showToast(this, "请选择区")
                return@OnClickListener
            }
            passByValue()
        })
        iv_cancel.setOnClickListener(View.OnClickListener { finish() })


        val jsonSB = StringBuilder()
        try {
            val addressJsonStream = BufferedReader(
                InputStreamReader(
                    BaseApplication.instance.getAssets().open("data.json")
                )
            )
            var line: String?
            while (addressJsonStream.readLine().also { line = it } != null) {
                jsonSB.append(line)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        adressEntity = Gson().fromJson(jsonSB.toString(), AdressDataEntity_oldddd::class.java)
//        childBeanXX.adCode = "100000"
//        childBeanXX.enable = "1"
//        childBeanXX.name = "全国"
//        adressEntity?.rows?.add(0, childBeanXX)
    }

    private fun showPopListView(view: TextView, type: String) {
        val contentView: View =
            LayoutInflater.from(this).inflate(R.layout.pop_list, null)
        //处理popWindow 显示内容
        handleListView(contentView, type, view)
        //创建并显示popWindow
        mListPopWindow =
            CustomPopWindow.PopupWindowBuilder(this).setOutsideTouchable(true).setFocusable(false)
                .setView(contentView)
                .size(view.width, ViewGroup.LayoutParams.WRAP_CONTENT) //显示大小
                .create()
                .showAsDropDown(view, 0, 0)
    }

    private fun handleListView(
        contentView: View,
        type: String,
        textView: TextView
    ) {
        val listView =
            contentView.findViewById<View>(R.id.lv_pop) as ListView
        if (type == "1") {
            val listAdapter = ListPopAdapter(adressEntity?.rows, this)
            listView.adapter = listAdapter
            listView.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    provinceList =
                        AdressDataEntity_oldddd.RowsBean.ChildBeanXX.ChildBeanX.ChildBean()
                    val data = adressEntity?.rows?.get(position)
                    if (data!!.name != "全国") {
                        citybeanlist = data.child!!
                    }
                    provinceList?.adCode = data.adCode
                    provinceList?.enable = data.enable
                    provinceList?.name = data.name
                    textView.text = data.name
                    mListPopWindow!!.dissmiss()
                }
        } else if (type == "2") {
            val listAdapter = ListPop2Adapter(citybeanlist, this)
            listView.adapter = listAdapter
            listView.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    areabeanlist = citybeanlist[position].child!!
                    cityList = AdressDataEntity_oldddd.RowsBean.ChildBeanXX.ChildBeanX.ChildBean()
                    cityList?.enable = citybeanlist[position].enable
                    cityList?.adCode = citybeanlist[position].adCode
                    cityList?.addresscode =
                        provinceList?.adCode.toString() + "-" + citybeanlist[position]
                            .adCode
                    cityList?.name = citybeanlist[position].name
                    textView.text = citybeanlist[position].name
                    mListPopWindow!!.dissmiss()
                }
        } else if (type == "3") {
            /** 存储点击选中的数据 */
            arealist = AdressDataEntity_oldddd.RowsBean.ChildBeanXX.ChildBeanX.ChildBean()
            // checkList = new ArrayList<AdressDataEntity.RowsBean.ChildBeanXX.ChildBeanX>();
            val listPop3Adapter = ListPop3Adapter(areabeanlist, this)
            listView.adapter = listPop3Adapter
            textView.text = ""
            listView.onItemClickListener =
                AdapterView.OnItemClickListener { parent, view, position, id ->
                    arealist?.enable = (areabeanlist[position].enable)
                    arealist?.adCode = (areabeanlist[position].adCode)
                    arealist?.name = (areabeanlist[position].name)
                    textView.text = areabeanlist[position].name
                    mListPopWindow!!.dissmiss()

                }
        }
    }


    fun passByValue() {
        val result: MutableList<AdressDataEntity_oldddd.RowsBean.ChildBeanXX.ChildBeanX.ChildBean?> =
            ArrayList<AdressDataEntity_oldddd.RowsBean.ChildBeanXX.ChildBeanX.ChildBean?>()
        val intent = Intent()
        if (null == arealist) { //区
            if (null == cityList) { //市
                if (null == provinceList) { //省
                    CommonUtil.showToast(this, "请选择地区")
                    return
                } else {
                    result.add(provinceList)
                }
            } else {
                result.add(cityList)
            }
        } else {
            result.add(arealist)
        }
        if (TextUtils.isEmpty(tv_area.text)) {
            tv_area.setText("不限")
        }
        if (TextUtils.isEmpty(tv_city.getText())) {
            tv_city.setText("不限")
        }
        intent.putExtra(
            "CityName",
            tv_province.getText().toString() + "-" + tv_city.getText() + "-" + tv_area.getText()
        )
        intent.putExtra(
            "adCode",
            result[0]!!.adCode
        )
        intent.putParcelableArrayListExtra(
            "string",
            result as ArrayList<out Parcelable?>
        )
        setResult(0X4444, intent)
        finish()
    }
}