package com.xzy.installer

import android.app.Application
import com.xzy.installer.utils.LogUtil

class AppContext : Application() {

    override fun onCreate() {
        super.onCreate()
        LogUtil.init()
        // 异常信息注册
        Thread.setDefaultUncaughtExceptionHandler(AppErrorCatchHandler())
        LogUtil.wInfo("AppContext onCreate>>>")
    }

    override fun onTerminate() {
        LogUtil.wInfo("AppContext onTerminate>>>")
        super.onTerminate()
    }
}