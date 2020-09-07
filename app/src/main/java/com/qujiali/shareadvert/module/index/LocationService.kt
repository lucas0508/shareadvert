package com.qujiali.shareadvert.module.index

import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.constraintlayout.widget.Constraints
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.orhanobut.logger.Logger
import com.qujiali.shareadvert.common.utils.Constant
import com.qujiali.shareadvert.common.utils.SPreference

class LocationService : Service() {
    //声明AMapLocationClient类对象
    var mLocationClient: AMapLocationClient? = null

    private var cityCode: String by SPreference(Constant.SP_APP_CITY_CODE, "")


    //声明AMapLocationClientOption对象
    var mLocationOption: AMapLocationClientOption? = null

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        Logger.d("进入定位Service")
        initMap(2000)
    }

    fun initMap(time: Int) {
        //初始化定位
        mLocationClient = AMapLocationClient(applicationContext)

        //初始化AMapLocationClientOption对象
        mLocationOption = AMapLocationClientOption()
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption!!.locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        //设置为单次定位
        mLocationOption!!.isOnceLocation = true
        //获取一次定位结果：
        mLocationOption!!.interval = time.toLong()
        //给定位客户端对象设置定位参数
        mLocationClient!!.setLocationOption(mLocationOption)
        //设置定位回调监听
        mLocationClient!!.setLocationListener(mLocationListener)
        //启动定位
        mLocationClient!!.startLocation()
    }

   /* override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        return START_REDELIVER_INTENT
    }*/

    //声明定位回调监听器
    var mLocationListener = AMapLocationListener { amapLocation ->
        Log.e(
            Constraints.TAG,
            "onLocationChanged: 开启定位"
        )
        Logger.d("开启定位数据：$amapLocation")
        if (amapLocation.errorCode == 12 && Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
            return@AMapLocationListener
        }
        val citycode: String
        citycode = if (amapLocation.adCode == null || "" == amapLocation.adCode) {
            ""
        } else if (amapLocation.errorCode != 0) {
            ""
        } else {
            amapLocation.adCode
        }
        cityCode = citycode
        return@AMapLocationListener
    }

    override fun onDestroy() {
        stopLocation()
        destroyLocation()
        super.onDestroy()
    }

    /**
     * 停止定位
     *
     * @author
     *
     * @since 2.8.0
     */
    fun stopLocation() {
        // 停止定位
        mLocationClient!!.stopLocation()
    }

    fun destroyLocation() {
        if (null != mLocationClient) {
            mLocationClient!!.onDestroy()
            mLocationClient = null
            mLocationOption = null
        }
    }

    companion object {
        lateinit var instance: LocationService
    }
}