package com.xzy.installer.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object TimeUtil {

    /**
     * 获取时间(包含日期)
     *
     * @return
     */
    val currentTime: String
        get() {
            val timeFmt = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            timeFmt.timeZone = TimeZone.getTimeZone("GMT+8")
            return timeFmt.format(Date())
        }

    /**
     * 获取时间(包含日期格式photo)
     *
     * @return
     */
    val currentPhotoTime: String
        get() {
            val photoFmt = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
            photoFmt.timeZone = TimeZone.getTimeZone("GMT+8")
            return photoFmt.format(Date())
        }

    /**
     * 获取日期
     *
     * @return
     */
    val currentDay: String
        get() {
            val dateFmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            dateFmt.timeZone = TimeZone.getTimeZone("GMT+8")
            return dateFmt.format(Date())
        }

    /**
     * 获取当前时分秒
     *
     * @return HH:mm:ss
     */
    val currentHour: String
        get() {
            val hourFmt = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            hourFmt.timeZone = TimeZone.getTimeZone("GMT+8")
            return hourFmt.format(Date())
        }

    /**
     * 获取时间(包含日期格式photo)
     *
     * @return
     */
    val currentTime2: String
        get() {
            val photoFmt = SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault())
            photoFmt.timeZone = TimeZone.getTimeZone("GMT+8")
            return photoFmt.format(Date())
        }

    /**
     * 日期加一天
     */
    fun addCurrentDay(): String {
        val sf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        sf.timeZone = TimeZone.getTimeZone("GMT+8")
        val c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_MONTH, 1)
        return sf.format(c.time)
    }

    fun addDay(days: Int): String {
        val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateTimeFormat.timeZone = TimeZone.getTimeZone("GMT+8")
        val c = Calendar.getInstance()
        c.add(Calendar.DAY_OF_MONTH, days)
        return dateTimeFormat.format(c.time)
    }

    /**
     * 将 hh:mm:ss 格式的时间转为秒
     *
     * @param time
     * @return
     */
    fun getSecond(time: String): Long {
        val oo = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val hour = Integer.parseInt(oo[0]).toLong()
        val min = Integer.parseInt(oo[1]).toLong()
        val s = Integer.parseInt(oo[2]).toLong()
        return s + min * 60 + hour * 60 * 60
    }

    /**
     * 将 hh:mm:ss 格式的时间转为时
     *
     * @param time
     * @return
     */
    fun getHour(time: String): Int {
        val oo = time.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return Integer.parseInt(oo[0])
    }

    /**
     *
     * @Title: getDeltaT
     * @Description: 得到两个时间的差值 (days)
     * @param @param startDate 起始时间
     * @param @param endDate 截至时间
     * @return long 时间差
     * @throws
     */
    fun getDeltaTimeDays(startDate: String, endDate: String): Int {
        val dateFmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateFmt.timeZone = TimeZone.getTimeZone("GMT+8")
        try {
            val d1 = dateFmt.parse(startDate)
            val d2 = dateFmt.parse(endDate)
            val diff = d1.time - d2.time // 这样得到的差值是微秒级别
            return (diff / (1000 * 60 * 60 * 24)).toInt()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return -1
    }
}
