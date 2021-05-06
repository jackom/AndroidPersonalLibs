package com.jackom.loglib.printer

import android.content.Context
import android.text.TextUtils
import com.jackom.loglib.LogFacade
import com.jackom.loglib.LogManager
import com.jackom.loglib.LogPriority
import com.quys.utilslib.FileUtil
import java.io.File
import java.io.RandomAccessFile
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

/**
 * @author：jackom
 * @date：5/1/21 on 8:59 AM
 * @desc：
 */
class LogFilePrinter private constructor(context: Context): ILogPrinter {

    private var mCachedPath: String = FileUtil.getFilePath(context)

    private val mCurDate = DATE_FORMAT.format(Date())

    private val mSavedFileName = "$mCurDate.log"

    /**
     * Log文件缓存路径
     */
    private var mSavedPath = DEFAULT_SAVED_PATH

    private val mSingleExecutor = Executors.newSingleThreadExecutor(DefaultThreadFactory())

    /**
     * The default thread factory.
     */
    private class DefaultThreadFactory : ThreadFactory {
        private val group: ThreadGroup?
        private val threadNumber = AtomicInteger(1)
        private val namePrefix: String
        override fun newThread(r: Runnable): Thread {
            val t = Thread(
                group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0
            )
            if (t.isDaemon) t.isDaemon = false
            if (t.priority != Thread.NORM_PRIORITY) t.priority = Thread.NORM_PRIORITY
            return t
        }

        companion object {
            private val poolNumber = AtomicInteger(1)
        }

        init {
            val s = System.getSecurityManager()
            group = if (s != null) s.threadGroup else Thread.currentThread().threadGroup
            namePrefix = "pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-"
        }
    }

    fun setSavedPath(savedPath: String?) {
        if (TextUtils.equals(mSavedPath, DEFAULT_SAVED_PATH)) {
            mSavedPath = if (savedPath.isNullOrEmpty() || TextUtils.equals(savedPath, "null")) {
                DEFAULT_SAVED_PATH
            } else {
                savedPath
            }
        } else {
            LogFacade.log4I(LogManager.instance.logConfig.globalTag(), "had set savedPath, reuse the origin path: $savedPath!")
        }
    }

    override fun printLogs(@LogPriority.Priority priority: Int, tag: String, contents: String) {
        mSingleExecutor.execute {
            val savedFilePath = File(mCachedPath, mSavedPath)
            FileUtil.getFolder(savedFilePath)
            val savedFile = File(savedFilePath, mSavedFileName)
            if (!savedFile.exists()) {
                savedFile.createNewFile()
            }
            //往文件中写入内容
            val resultContents = "${TIME_FORMAT.format(Date(System.currentTimeMillis()))}, priority is: $priority, tag is: $tag, contents is: $contents"
            write2File(savedFile, resultContents)
        }
    }

    private fun write2File(savedFile: File, contents: String) {
        //创建一个RandomAccessFile的对象,并指定模式rw，能读能写，
        //注意：必须是文件，不能是路径
        val raf = RandomAccessFile(savedFile, "rw")
        raf.write(contents.toByteArray())
        raf.close()
    }

    override fun isSupport(): Boolean = true

    companion object {
        private const val M_CACHE_SIZE = 1 * 1024 * 1024
        /**
         * 文件缓存最多缓存20M
         */
        private const val MAX_CACHE_SIZE = 10 * M_CACHE_SIZE

        private const val DEFAULT_SAVED_PATH = "LogSaved"

        private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        private val TIME_FORMAT = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        @Volatile
        var instance: LogFilePrinter? = null

        fun getInstance(context: Context): LogFilePrinter {
            if (instance == null) {
                synchronized(LogFilePrinter::class) {
                    if (instance == null) {
                        instance = LogFilePrinter(context.applicationContext)
                    }
                }
            }
            return instance!!
        }
    }
}