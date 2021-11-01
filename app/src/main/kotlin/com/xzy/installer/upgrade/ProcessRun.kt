package com.xzy.installer.upgrade

import android.os.Handler
import com.xzy.installer.utils.LogUtil

import java.io.BufferedReader
import java.io.DataOutputStream
import java.io.IOException
import java.io.InputStreamReader

class ProcessRun {
    companion object {
        val COMMAND_SU = "su"
        const val COMMAND_REBOOT = "reboot"
        val COMMAND_SH = "sh"
        private const val COMMAND_EXIT = "exit\n"
        private const val COMMAND_LINE_END = "\n"

        private const val MSG_SHELL_OUTPUT = 10

        fun run(pmParams: String, handler: Handler?) {
            // pmParams = getIP(pmParams);
            val command = StringBuilder()
            command.append(pmParams)
            exec(arrayOf(command.toString()), handler)
        }

        private fun output(content: String, handler: Handler?) {
            if (handler != null) {
                val msg = handler.obtainMessage(MSG_SHELL_OUTPUT)
                msg.obj = content
                handler.sendMessage(msg)
            } else {
                LogUtil.wInfo("process run $content")
            }
        }

        private fun exec(commands: Array<String>?, handler: Handler?) {

            var result: Int
            if (commands == null || commands.isEmpty()) {
                output("commands is null", handler)
                return
            }
            var process: Process? = null
            var successResult: BufferedReader? = null
            var errorResult: BufferedReader? = null

            var find = false

            var os: DataOutputStream? = null
            try {
                process = Runtime.getRuntime().exec("sh")
                os = DataOutputStream(process!!.outputStream)
                for (command in commands) {

                    os.write(command.toByteArray())
                    os.writeBytes(COMMAND_LINE_END)
                    os.flush()
                }
                os.writeBytes(COMMAND_EXIT)
                os.flush()

                Thread.sleep(1000)
                successResult = BufferedReader(InputStreamReader(process.inputStream))
                errorResult = BufferedReader(InputStreamReader(process.errorStream))
                var s: String
                do {
                    s = successResult.readLine()
                    if (s != null) {
                        find = true
                        output(s, handler)
                    } else
                        break
                } while (true)

                do {
                    s = errorResult.readLine()
                    if (s != null) {
                        find = true
                        output(s, handler)
                    } else
                        break
                } while (true)
                result = process.waitFor()
                output("execute result - $result", handler)
                process.destroy()
            } catch (e: Exception) {
                e.printStackTrace()
                output("exception " + e.message, handler)
            } finally {
                try {
                    os?.close()
                    successResult?.close()
                    errorResult?.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                process?.destroy()
            }

            if (!find) {
                output("cant't get output fromr terminal ", handler)
            }
        }
    }
}
