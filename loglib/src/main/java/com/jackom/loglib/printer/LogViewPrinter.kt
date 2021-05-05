package com.jackom.loglib.printer

import android.app.Activity
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jackom.loglib.LogPriority
import com.jackom.loglib.LogViewController
import com.jackom.loglib.R
import com.jackom.loglib.bean.LogViewBean


/**
 * @author：jackom
 * @date：5/1/21 on 8:59 AM
 * @desc：
 */
class LogViewPrinter(activity: Activity) : ILogPrinter {

    private val datas = mutableListOf<LogViewBean>()
    private val mAdapter: LogViewAdapter
    private val mRecyclerView: RecyclerView
    private val mLogViewController: LogViewController

    init {
        val rootView = activity.findViewById<FrameLayout>(android.R.id.content)
        mRecyclerView = RecyclerView(activity)
        mRecyclerView.layoutManager = LinearLayoutManager(activity)
        mAdapter = LogViewAdapter(activity)
        mRecyclerView.adapter = mAdapter
        mLogViewController = LogViewController(rootView, mRecyclerView)
    }

    fun getLogViewController(): LogViewController {
        return mLogViewController
    }

    override fun printLogs(@LogPriority.Priority priority: Int, tag: String, contents: String) {
        datas.add(LogViewBean(priority, tag, contents))
        mAdapter.notifyItemInserted(datas.size - 1)
        mRecyclerView.smoothScrollToPosition(datas.size - 1)
    }

    inner class LogViewAdapter(private val activity: Activity): RecyclerView.Adapter<LogViewAdapter.MyViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val view: View = LayoutInflater.from(activity).inflate(R.layout.item_log, parent, false)
            return MyViewHolder(view)
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val bean = datas[position]
            with(holder, {
                val priorityColorPair = getLogcatColor(bean.priority)
                titleTv.text = (priorityColorPair.first + bean.title)
                contentsTv.text = bean.getFormatMsg(bean.priority, bean.title, bean.contents)
                contentsTv.setTextColor(priorityColorPair.second)
            })
        }

        private fun getLogcatColor(@LogPriority.Priority priority : Int): Pair<String, Int> {
            when (priority) {
                LogPriority.A -> {
                    return Pair("Log.ASSERT: ", Color.parseColor("#E170D6"))
                }
                LogPriority.D -> {
                    return Pair("Log.DEBUG: ", Color.parseColor("#0091E1"))
                }
                LogPriority.E -> {
                    return Pair("Log.ERROR: ", Color.parseColor("#FF5F5F"))
                }
                LogPriority.V -> {
                    return Pair("Log.VERBOSE: ", Color.parseColor("#E1E1E1"))
                }
                LogPriority.W -> {
                    return Pair("Log.WARN: ", Color.parseColor("#E1E125"))
                }
            }
            //默认返回info
            return Pair("Log.INFO: ", Color.parseColor("#56E13E"))
        }

        override fun getItemCount(): Int {
            return datas.size
        }

        inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val titleTv: AppCompatTextView = itemView.findViewById(R.id.tv_title)
            val contentsTv: AppCompatTextView = itemView.findViewById(R.id.tv_content)
        }

    }

    override fun isSupport(): Boolean = true

}