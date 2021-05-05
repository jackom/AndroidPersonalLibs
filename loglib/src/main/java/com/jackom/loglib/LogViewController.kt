package com.jackom.loglib

import android.content.Context
import android.graphics.Color
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView

/**
 * @author：jackom
 * @date：5/4/21 on 5:33 PM
 * @desc：控制LogView的打开与关闭
 */
class LogViewController(private val rootView: ViewGroup, private val recyclerView: RecyclerView) {

    private var mFloatingView: AppCompatTextView? = null
    private var mLogView: FrameLayout? = null
    private var isLogViewOpen: Boolean = false

    open fun showFloatingView() {
        mFloatingView = getFloatingView()
        mFloatingView!!.visibility = View.VISIBLE
        mLogView?.let {
            it.visibility = View.GONE
//            it.removeAllViews()
        }
        isLogViewOpen = false
    }

    private fun showLogView() {
        mLogView = getLogView()
        mFloatingView?.visibility = View.GONE
        mLogView?.visibility = View.VISIBLE
        isLogViewOpen = true
    }

    private fun getFloatingView(): AppCompatTextView {
        mFloatingView = rootView.findViewWithTag<AppCompatTextView>(TAG_FLOATING_VIEW)
        if (null != mFloatingView) {
            return mFloatingView as AppCompatTextView
        }
        mFloatingView = AppCompatTextView(rootView.context)
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, (FrameLayout.LayoutParams.WRAP_CONTENT))
        params.bottomMargin = dp2px(rootView.context, 100f).toInt()
        params.gravity = Gravity.BOTTOM or Gravity.END
        mFloatingView!!.text = "floatingView"
        mFloatingView!!.setBackgroundColor(Color.DKGRAY)
        mFloatingView!!.setOnClickListener {
            if (!isLogViewOpen) {
                showLogView()
            }
        }
        mFloatingView!!.tag = TAG_FLOATING_VIEW

        rootView.addView(mFloatingView, params)

        return mFloatingView as AppCompatTextView
    }

    private fun getLogView(): FrameLayout {
        mLogView = rootView.findViewWithTag<FrameLayout>(TAG_LOG_VIEW)
        if (null != mLogView) {
            return mLogView as FrameLayout
        }
        mLogView = FrameLayout(rootView.context)
        mLogView!!.setBackgroundColor(Color.LTGRAY)
        val params = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, dp2px(rootView.context, 160f).toInt())
        params.gravity = Gravity.BOTTOM
        mLogView!!.addView(recyclerView)

        val closeTextView = AppCompatTextView(rootView.context)
        val closeTvParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, (FrameLayout.LayoutParams.WRAP_CONTENT))
        closeTvParams.gravity = Gravity.END
        closeTextView.text = "closeFloatingView"
        closeTextView.setOnClickListener {
            showFloatingView()
        }
        mLogView!!.tag = TAG_LOG_VIEW
        mLogView!!.addView(closeTextView, closeTvParams)

        rootView.addView(mLogView, params)
        return mLogView as FrameLayout
    }

    private fun dp2px(context: Context, dp: Float): Float = dp * context.resources.displayMetrics.density

    companion object {
        private const val TAG_FLOATING_VIEW = "floatingView"
        private const val TAG_LOG_VIEW = "logView"
    }

}