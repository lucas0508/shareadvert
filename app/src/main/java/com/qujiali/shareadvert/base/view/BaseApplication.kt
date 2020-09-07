package com.qujiali.shareadvert.base.view

import android.app.Application
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import com.kingja.loadsir.core.LoadSir
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.LogStrategy
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import com.qujiali.shareadvert.common.callback.EmptyCallBack
import com.qujiali.shareadvert.common.callback.ErrorCallBack
import com.qujiali.shareadvert.common.callback.LoadingCallBack
import com.qujiali.shareadvert.common.utils.Constant
import com.qujiali.shareadvert.common.utils.SPreference


open class BaseApplication : Application() {
    companion object {
        lateinit var instance : BaseApplication
    }
    override fun onCreate() {
        super.onCreate()
        instance = this
        SPreference.setContext(applicationContext)
      //  initMode()
        LoadSir.beginBuilder()
            .addCallback(ErrorCallBack())
            .addCallback(LoadingCallBack())
            .addCallback(EmptyCallBack())
            .commit()

        val formatStrategy = PrettyFormatStrategy.newBuilder()
            .showThreadInfo(false)  // 输出线程信息. 默认输出
            .methodCount(0)         // 方法栈打印的个数，默认是2
            .methodOffset(7)        // 设置调用堆栈的函数偏移值，0的话则从打印该Log的函数开始输出堆栈信息，默认是0
            .logStrategy(LogCatStrategy())
            .build()
        Logger.addLogAdapter(AndroidLogAdapter(formatStrategy))

    }

    private fun initMode() {
        var isNightMode: Boolean by SPreference(Constant.NIGHT_MODE, false)
        AppCompatDelegate.setDefaultNightMode(if (isNightMode) AppCompatDelegate.MODE_NIGHT_YES else AppCompatDelegate.MODE_NIGHT_NO)
    }


    class LogCatStrategy: LogStrategy {
        override fun log(priority: Int, tag: String?, message: String) {
            Log.println(priority, randomKey() + tag!!, message)
        }

        private var last: Int = 0

        private fun randomKey(): String {
            var random = (10 * Math.random()).toInt()
            if (random == last) {
                random = (random + 1) % 10
            }
            last = random
            return random.toString()
        }
    }
}