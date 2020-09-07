package com.qujiali.shareadvert.common.utils

import android.annotation.SuppressLint
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.internal.Intrinsics


object DateUtil {
    /**
     * 获取当前时刻
     *
     * @return
     */
    fun getNowTime(): Date? {
        return formatDate("yyyy-MM-dd", Date(Date().time))
    }

    /**
     * 日期格式化
     *
     * @param formatStyle
     * @param date
     * @return
     */
    fun formatDate(formatStyle: String, date: Date?): Date? {
        Intrinsics.checkParameterIsNotNull(formatStyle, "formatStyle")
        return if (date != null) {
            val sdf = SimpleDateFormat(formatStyle)
            val formatDate = sdf.format(date)
            try {
                val var10000 = sdf.parse(formatDate)
                Intrinsics.checkExpressionValueIsNotNull(var10000, "sdf.parse(formatDate)")
                var10000
            } catch (var6: ParseException) {
                var6.printStackTrace()
                Date()
            }
        } else {
            Date()
        }
    }

    /**
     * 日期格式化
     *
     * @param formatStyle
     * @param date
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    fun formatDate(date: Long): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        return sdf.format(Date(date))

    }



    /**
     * 为了解决跨年问题 润平年问题  精确计算两个日期天数差值
     * 通过文件夹日期名 计算日志是否在有限期时间内
     * @param oldDate 日志文件日期 yyyyMMdd
     * @param newDate 当前日期 yyyyMMdd
     * @param validityTime 有限期时间 单位day
     */
    fun checkDateInValid(oldDate: String, newDate: String, validityTime: Int): Boolean {
        val c1 = Calendar.getInstance()
        val c2 = Calendar.getInstance()
        c1.time = Date(oldDate)
        c2.time = Date(newDate)

        val year1 = c1.get(Calendar.YEAR)
        val year2 = c2.get(Calendar.YEAR)
        if (year2>year1){
            //如果跨年了
            val day1 = c1.get(Calendar.DAY_OF_YEAR)
            val day2 = c2.get(Calendar.DAY_OF_YEAR)
            var days = 0
            for (i in year1..year2){
                if(i == year1){
                    days += if(GregorianCalendar().isLeapYear(year1)){
                        366 - day1
                    }else{
                        365 - day1
                    }
                }else if (i == year2){
                    days += day2
                }else{
                    days += if(GregorianCalendar().isLeapYear(year1)){
                        366
                    }else{
                        365
                    }
                }
            }
            return validityTime > days
        }else if(year2 == year1){
            //如果没跨年
            val days = c2.get(Calendar.DAY_OF_YEAR) - c1.get(Calendar.DAY_OF_YEAR)
            return validityTime > days
        }else{
            return false
        }
    }

}