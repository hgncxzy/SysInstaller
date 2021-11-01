package com.xzy.installer.utils

import android.app.ActivityManager
import android.content.Context

class OtherUtil {
    companion object {
        fun isRunning(context: Context, packName: String): Boolean {
            val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
            val list = am.getRunningTasks(100)
            for (info1 in list) {
                if (info1.topActivity.packageName ==
                    packName && info1.baseActivity.packageName == packName) {
                    LogUtil.wInfo("running! $packName")
                    return true
                }
            }
            LogUtil.wInfo("not running! $packName")
            return false
        }
    }
}