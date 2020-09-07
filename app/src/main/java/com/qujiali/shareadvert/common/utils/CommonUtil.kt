package com.qujiali.shareadvert.common.utils

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Vibrator
import android.text.TextUtils
import android.widget.Toast
import com.qujiali.shareadvert.module.index.ArticleDetailActivity
import java.lang.reflect.ParameterizedType


object CommonUtil {

    fun <T> getClass(t: Any): Class<T> {
        // 通过反射 获取父类泛型 (T) 对应 Class类
        return (t.javaClass.genericSuperclass as ParameterizedType)
            .actualTypeArguments[0]
                as Class<T>
    }

    fun startWebView(
        context: Context,
        url: String,
        title: String
    ) {
        startActivity<ArticleDetailActivity>(context) {
            putExtra("url", url)
            putExtra("title", title)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    fun showToast(context: Context, string: String) {
        Toast.makeText(context, string, Toast.LENGTH_SHORT).show()
    }

    /**
     * 设置震动
     *
     * @param context
     * @param milliseconds
     */
    fun Vibrate(context: Context, milliseconds: Long) {
        val vibrator =
            context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator
        vibrator.vibrate(milliseconds)
    }


    fun listToString(strings: ArrayList<String>): String? {
        var str = ""
        if (strings.size > 0) {
            val strBuilder = StringBuilder()
            for (letter in strings) {
                strBuilder.append(letter)
                strBuilder.append(",")
            }
            str = strBuilder.toString()
            str = str.substring(0, str.length - ",".length)
            return str
        }
        return ""
    }

    fun stringToList(strings: String): MutableList<String>? {
        val str: MutableList<String> =
            ArrayList()
        if (!TextUtils.isEmpty(strings)) {
            val strBuilder = StringBuilder()
            val split = strings.split(",".toRegex()).toTypedArray()
            str.addAll(listOf(*split))
            return str
        }
        return str
    }

    fun stringToListByXG(strings: String): MutableList<String>? {
        val str: MutableList<String> =
            ArrayList()
        if (!TextUtils.isEmpty(strings)) {
            val strBuilder = StringBuilder()
            val split = strings.split("/".toRegex()).toTypedArray()
            str.addAll(listOf(*split))
            return str
        }
        return str
    }
}