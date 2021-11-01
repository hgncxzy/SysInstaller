package com.xzy.installer

import android.app.Activity
import android.os.Bundle
import com.xzy.installer.utils.LogUtil

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LogUtil.wInfo("activity启动")
    }

    override fun onStop() {
        super.onStop()
        LogUtil.wInfo("activity finish")
        finish()
    }
}
