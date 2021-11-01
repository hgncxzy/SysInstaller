package com.xzy.installer.upgrade

import android.os.Build
import com.xzy.installer.utils.LogUtil

import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader

class InstallAPK {
    companion object {
        /**
         * install slient
         *
         * @param context
         * @param filePath
         * @return 0 means normal, 1 means file not exist, 2 means other exception
         * error
         */
        fun installSlient(filePath: String, packName: String): Int {
            LogUtil.wInfo(packName)
            val file = File(filePath)
            if (!file.exists()) {
                return 1
            }
            var args: Array<String>
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                args = arrayOf("pm", "install", "-r", "-i", packName, "--user", "0", filePath)
            } else {
                LogUtil.wInfo("其他版本")
                args = arrayOf("pm", "install", "-r", filePath)
            }
            val processBuilder = ProcessBuilder(*args)

            var process: Process? = null
            var successResult: BufferedReader? = null
            var errorResult: BufferedReader? = null
            val successMsg = StringBuilder()
            val errorMsg = StringBuilder()
            var result: Int
            try {
                process = processBuilder.start()
                successResult = BufferedReader(InputStreamReader(process!!.inputStream))
                errorResult = BufferedReader(InputStreamReader(process.errorStream))
                var s: String?

                do {
                    s = successResult.readLine()
                    if (s != null) {
                        successMsg.append(s)
                    } else
                        break
                } while (true)

                do {
                    s = errorResult.readLine()
                    if (s != null) {
                        errorMsg.append(s)
                    } else
                        break
                } while (true)
            } catch (e: IOException) {
                LogUtil.wError(e)
            } catch (e: Exception) {
                LogUtil.wError(e)
            } finally {
                try {
                    successResult?.close()
                    errorResult?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                process?.destroy()
            }
            if (successMsg.toString().contains("Success") ||
                successMsg.toString().contains("success")
            ) {
                result = 0
            } else {
                result = 2
            }
            LogUtil.wInfo("ErrorMsg:$errorMsg")
            return result
        }
    }
}
