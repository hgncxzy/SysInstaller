package com.xzy.installer.utils

import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.core.content.FileProvider
import com.xzy.installer.MainActivity
import com.xzy.installer.upgrade.InstallAPK
import com.xzy.installer.upgrade.ProcessRun

import java.io.File

class BroadCast : BroadcastReceiver() {

    private lateinit var context: Context

    override fun onReceive(context: Context, intent: Intent) {
        this.context = context.applicationContext
        val action = intent.action
        LogUtil.wInfo(action ?: "action is null")
        if (ACTION == action) {
            val splashIntent = Intent(context, MainActivity::class.java)
            splashIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(splashIntent)

            val bundle = intent.extras
            val apkPath = bundle?.getString(EXTRA_APK_PATH)
            val packName = bundle?.getString(EXTRA_PKG_NAME)
            val clsName = bundle?.getString(EXTRA_CLS_NAME)
            LogUtil.wInfo("收到广播准备安装$apkPath+$packName+$clsName")
            if (apkPath.isNullOrEmpty() || packName.isNullOrEmpty() || clsName.isNullOrEmpty()) {
                return
            }
            installNew(apkPath, packName, clsName)
        }
    }

    private fun getUri(apkPath: String): Uri {
        val authority = context.packageName + ".FileProvider"
        return FileProvider.getUriForFile(context, authority, File(apkPath))
    }

    private fun installNew(fileName: String, packName: String, clsName: String) {

        val seconds = 2

        val file = File(fileName)
        if (file.exists()) {
            // 判断是否需要启动该app
            installAPK(
                fileName, packName, clsName, seconds
                // OtherUtil.isRunning(context, packName)
            )
        } else {
            LogUtil.wInfo("file not exist!")
        }
    }

    private fun installAPK(
        uri: String,
        packName: String,
        cls: String,
        seconds: Int
    ) {
        object : Thread() {

            override fun run() {
                try {
                    val result = InstallAPK.installSlient(uri, context.packageName)
                    if (result == 0) {
                        LogUtil.wInfo("wait $seconds seconds for install new apk !")
                        sleep((seconds * 1000).toLong())
                    } else if (result == 1) {
                        LogUtil.wInfo("file not exist!")
                    } else {
                        LogUtil.wInfo("exception!")
                    }

                    if (!startAPK(packName, cls)) {
                        LogUtil.wInfo("startTempAPK failed")
                        throw RuntimeException("startTempAPK failed")
                    }
                } catch (e: Exception) {
                    LogUtil.wInfo("exception! $e")
                    LogUtil.wInfo("need reboot ")
                    ProcessRun.run(ProcessRun.COMMAND_REBOOT, null)
                }
            }
        }.start()
    }

    private fun startAPK(packName: String, cls: String): Boolean {

        try {
            LogUtil.wInfo("start new apk! $packName")
            val intent = Intent()
            intent.component = ComponentName(packName, cls)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.action = Intent.ACTION_VIEW
            if (context.packageManager.resolveActivity(
                    intent,
                    PackageManager.MATCH_DEFAULT_ONLY
                ) != null
            ) {
                context.startActivity(intent)
                return true
            }
        } catch (e: Exception) {
            LogUtil.wError(e)
        }

        return false
    }

    companion object {
        const val ACTION = "com.xzy.upgrade.ACTION_INSTALL"
        const val EXTRA_APK_PATH = "com.xzy.upgrade.extra.APK_PATH"
        const val EXTRA_SILENT = "com.xzy.upgrade.extra.SILENT"
        const val EXTRA_PKG_NAME = "com.xzy.upgrade.extra.PKG_NAME"
        const val EXTRA_CLS_NAME = "com.xzy.upgrade.extra.CLS_NAME"
    }
}
