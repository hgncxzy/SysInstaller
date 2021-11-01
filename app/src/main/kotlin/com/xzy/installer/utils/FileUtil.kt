package com.xzy.installer.utils

import java.io.File

object FileUtil {

    fun mkdir(path: String): Boolean {
        val dir = File(path)
        return if (!dir.exists()) {
            dir.mkdirs()
        } else true
    }

    fun rmDir(path: String) {
        val file = File(path)
        if (file.isDirectory) {
            val childFile = file.listFiles()
            if (childFile == null || childFile.size == 0) {
            } else {
                for (f in childFile) {
                    rmfile(f)
                }
            }
        }
    }

    fun rmfile(fileName: String) {
        val file = File(fileName)
        if (file.exists()) {
            rmfile(file)
        }
    }

    private fun rmfile(file: File) {
        if (file.isFile) {
            file.delete()
            return
        }
        if (file.isDirectory) {
            val childFile = file.listFiles()
            if (childFile == null || childFile.size == 0) {
                file.delete()
                return
            }
            for (f in childFile) {
                rmfile(f)
            }
            file.delete()
        }
    }
}
