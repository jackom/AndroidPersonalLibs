package com.jackom.androidpersonallibs

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.jackom.loglib.LogConfigAbstract
import com.jackom.loglib.LogFacade
import com.jackom.loglib.LogManager
import com.jackom.loglib.LogPriority
import com.jackom.loglib.printer.LogViewPrinter

/**
 * @author：jackom
 * @date：4/30/21 on 1:02 PM
 * @desc：
 */
class LogDemoActivity : AppCompatActivity(), View.OnClickListener {

    private val mLogViewPrinter: LogViewPrinter by lazy {
        LogViewPrinter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_demo)

        findViewById<Button>(R.id.log_btn).setOnClickListener(this)
        findViewById<Button>(R.id.showlog_btn).setOnClickListener(this)

        //增加LogViewPrinter
        LogManager.instance.addPrinter(mLogViewPrinter)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.log_btn -> {
                testPrintLog()
            }
            R.id.showlog_btn -> {
                showLogView()
            }
        }
    }

    private fun showLogView() {
        mLogViewPrinter.getLogViewController().showFloatingView()
    }

    private fun testPrintLog() {
        //自定义log配置
        LogFacade.log(object : LogConfigAbstract() {
            override fun stackTraceDepth(): Int {
                return 0
            }
        }, LogPriority.E, "-----", "123456")
        LogFacade.log4A(*arrayOf("测试打印日志`1.。。"))
    }


}