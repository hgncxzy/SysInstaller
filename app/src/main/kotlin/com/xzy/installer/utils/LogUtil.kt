package com.xzy.installer.utils

import android.os.Environment
import android.util.Log

import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.concurrent.LinkedBlockingQueue

/**
 * 调试日志打印,写文件
 */
class LogUtil {
    companion object {

        fun info(content: String) {
            if (Settings.get().isDebug) {
                val ste = Thread.currentThread().stackTrace[3]
                val fullName = ste.className
                val className = fullName.substring(fullName.lastIndexOf(".") + 1)
                val tag = "[" + className + "." + ste.methodName +
                        ":Line No." + ste.lineNumber + "]"
                Log.i(tag, content)
            }
        }

        /**
         * 写入信息到文件中
         */
        @Synchronized
        private fun writeEx(info: Array<String?>) {
            val logPath = File(Settings.get().logPath)

            val storageState = Environment.getExternalStorageState()
            // 判断SD卡是否挂载
            if (storageState == Environment.MEDIA_MOUNTED) {
                // 判断目录是否存在，不存在就创建
                if (!logPath.exists()) {
                    logPath.mkdir()
                }
                // 再判断文件是否存在
                val log = File(logPath.toString() + File.separator + TimeUtil.currentDay + ".log")
                if (!log.exists()) {
                    try {
                        log.createNewFile()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }

                // 写入文件
                if (log.exists()) {
                    try {
                        val fw = FileWriter(log, true)
                        // false
                        for (i in info.indices) {
                            fw.write(info[i])
                        }
                        fw.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            } else {
                info("sdcard未挂载!")
            }
        }

        lateinit var queue: LinkedBlockingQueue<String>
        private lateinit var writeRunnable: WriteRunnable

        fun init() {
            queue = LinkedBlockingQueue()
            writeRunnable = WriteRunnable()
            Thread(writeRunnable, "LogThread@" +
                    Integer.toHexString(writeRunnable.hashCode())).start()
        }

        internal class WriteRunnable : Runnable {

            override fun run() {
                while (true) {
                    try {

                        val info = queue.take()
                        var size = queue.size
                        val infos = arrayOfNulls<String>(size + 1)
                        infos[0] = info
                        var i = 1
                        while (size > 0) {
                            infos[i] = queue.take()
                            i++
                            size--
                        }
                        writeEx(infos)
                    } catch (e: InterruptedException) {
                    } catch (e: Exception) {
                    }
                }
            }
        }

        private fun write(info: String, level: String) {
            // LogUtil.wInfo(ste.toString())
            val sb = StringBuilder()
            sb.append("\n")
            sb.append("[")
            sb.append(TimeUtil.currentTime)
            sb.append(" ")
            sb.append(level)
            sb.append("]   ")
            sb.append(info)
            sb.append("\n")

            if (Settings.get().isDebug) {
                if ("ERROE" == level) {
                    Log.e("hiwather-$level", sb.toString())
                } else {
                    Log.i("hiwather-$level", sb.toString())
                }
            }

            queue.add(sb.toString())
        }

        /**
         * 写入Info信息到文件中
         */
        fun wInfo(info: String) {
            write(info, "INFO")
        }

        /**
         * 写入Error信息到文件中
         */
        fun wError(info: String) {
            write(info, "ERROR")
        }

        /**
         * 写入Error信息到文件中
         */
        fun wError(e: Throwable) {
            write(Log.getStackTraceString(e), "ERROR")
        }

        /**
         * 以下方法针对工具生成日志调用
         */
        fun aInfo(assist: String, info: String) {
            write(info, "INFO $assist")
        }

        fun aError(assist: String, info: String) {
            write(info, "ERROR $assist")
        }

        fun aError(assist: String, e: Throwable) {
            write(Log.getStackTraceString(e), "ERROR $assist")
        }
    }
}
