package com.xzy.installer

import com.xzy.installer.utils.LogUtil
import java.lang.Thread.UncaughtExceptionHandler

/**
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录错误报告.
 */
class AppErrorCatchHandler : Exception(), UncaughtExceptionHandler {

    /** 系统默认的UncaughtException处理类  */
    private val mDefaultHandler: UncaughtExceptionHandler? =
        Thread.getDefaultUncaughtExceptionHandler()

    override fun uncaughtException(thread: Thread, ex: Throwable) {
        if (!handleException(ex) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex)
        }
    }

    /**
     * 自定义异常处理:收集错误信息&发送错误报告
     *
     * @param ex
     * @return true:处理了该异常信息;否则返回false
     */
    private fun handleException(ex: Throwable?): Boolean {
        if (ex == null) {
            return false
        }
        ex.printStackTrace()
        LogUtil.wError(ex)
        return true
    }

    companion object {

        private val serialVersionUID = 1L
    }
}
