package com.xzy.installer.utils

import android.os.Environment

import java.io.File

/**
 *
 * 配置信息, 包含文件静态配置和SpHelper动态配置
 *
 */
class Settings {
    private val storagePath: String =
        Environment.getExternalStorageDirectory().absolutePath + File.separator + "upgrade"

    val logPath: String
        get() = storagePath + File.separator + "log"

    val isDebug: Boolean
        get() = true

    init {
        FileUtil.mkdir(storagePath)
    }

    companion object {

        var instance: Settings? = null

        fun get(): Settings {
            if (instance == null) {
                instance = Settings()
            }
            return instance as Settings
        }
    }
}
