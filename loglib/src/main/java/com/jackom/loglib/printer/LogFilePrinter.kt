package com.jackom.loglib.printer

import android.Manifest
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import android.widget.Toast
import androidx.core.content.PermissionChecker
import com.jackom.loglib.LogFacade
import com.jackom.loglib.LogManager
import com.jackom.loglib.LogPriority
import com.jackom.utilslib.FileUtil
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
class LogFilePrinter private constructor(private val context: Context): ILogPrinter {

    init {
        val pkgName = context.packageName
        FileUtil.initRootName(pkgName.substring(pkgName.lastIndexOf(".") + 1))
    }

    private var mCachedPath: String = FileUtil.getFilePath(context)

    private var mSavedFileName = "${TIME_FORMAT.format(Date())}.log"

    /**
     * Log文件缓存路径
     */
    private var mSavedPath = DEFAULT_SAVED_PATH

    private val mSingleExecutor = Executors.newSingleThreadExecutor(DefaultThreadFactory())

    private val mHandler = Handler(Looper.getMainLooper())

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
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (PermissionChecker.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED
                    || (PermissionChecker.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PermissionChecker.PERMISSION_GRANTED)) {
                    // 未授予文件夹读写操作权限
                    mHandler.post {
                        Toast.makeText(context, "应用未授予文件夹读写操作权限!", Toast.LENGTH_SHORT).show()
                    }
                    return@execute
                }
            }

            val savedFilePath = File(mCachedPath, mSavedPath)
            FileUtil.getFolder(savedFilePath)
            var savedFile = File(savedFilePath, mSavedFileName)
            if (!savedFile.exists()) {
                savedFile.createNewFile()
            }

            //往文件中写入内容
            val resultContents = "${TIME_FORMAT.format(Date(System.currentTimeMillis()))}, priority is: $priority, tag is: $tag, contents is: $contents"
            write2File(savedFile, resultContents)

            //判断当前文件大小是否已经超过1M，是的话就得拆分
            if (savedFile.length() >= M_CACHE_SIZE) {
                mSavedFileName = "${TIME_FORMAT.format(Date())}.log"
                savedFile = File(savedFilePath, mSavedFileName)
                savedFile.createNewFile()
            }

            //判断当前存储路径下的文件大小是否已经达到 MAX_CACHE_SIZE
            val curTotalSize = FileUtil.getDirSize(savedFilePath)
            if (curTotalSize >= MAX_CACHE_SIZE) {
                //删除最近最少使用的本地文件
                deleteLeastUseFiles(savedFilePath)
            }
        }
    }

    /**
     * 删除最近最少使用的本地文件
     */
    private fun deleteLeastUseFiles(savedFilePath: File) {
        val fileArrys = savedFilePath.listFiles()
        if (null == fileArrys || fileArrys.isEmpty()) {
            return
        }
        val files = mutableListOf<File>()
        for (file in fileArrys) {
            files.add(file)
        }

        files.sortWith { f1, f2 ->
            f2.lastModified().compareTo(f1.lastModified())
        }

        while (savedFilePath.length() >= MAX_CACHE_SIZE) {
            if (files.size > 0) {
                files.removeAt(0)
            }
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