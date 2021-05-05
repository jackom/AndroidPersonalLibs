package com.jackom.androidpersonallibs

import android.app.Application
import com.google.gson.Gson
import com.jackom.loglib.LogConfigAbstract
import com.jackom.loglib.LogManager
import com.jackom.loglib.printer.LogConsolePrinter
import com.jackom.loglib.printer.LogViewPrinter

/**
 * @author：jackom
 * @date：4/30/21 on 12:24 PM
 * @desc：
 */
class App: Application() {

    override fun onCreate() {
        super.onCreate()

        //初始化Log库
        LogManager.instance.initLogConfig(object : LogConfigAbstract() {
            override fun injectJsonParser(): JsonParser {
                return object : JsonParser {
                    override fun parse2Json(obj: Any): String {
                        return Gson().toJson(obj)
                    }
                }
            }

            override fun isLogOpen(): Boolean {
                return true
            }

            override fun globalTag(): String {
                return super.globalTag()
            }
        }, LogConsolePrinter())
    }
}