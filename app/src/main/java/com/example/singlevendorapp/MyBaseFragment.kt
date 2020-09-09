package com.example.singlevendorapp

import android.app.Activity
import android.view.View
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment

open class MyBaseFragment : Fragment() {
    private var rootLayout: RelativeLayout? = null
    private lateinit var context: Activity
    private var progressBar: ProgressBar? = null
    fun addCommonViews(rootRl: RelativeLayout?, context: Activity) {
        this.rootLayout = rootRl
        this.context = context
//        addBlurView()
//        addAlertBox()
        addProgressBar()
    }

    private fun addProgressBar() {
        progressBar = ProgressBar(context)
        rootLayout?.addView(progressBar)
        val layoutParams =
            progressBar?.layoutParams as? RelativeLayout.LayoutParams
        layoutParams?.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        progressBar?.layoutParams = layoutParams
        progressBar?.elevation = 30f
        hideProgressBar()
    }

    fun showProgressBar(blockUI: Boolean) {
        progressBar?.visibility = View.VISIBLE
        if (blockUI) {
            context.window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }
    }

    fun hideProgressBar() {
        progressBar?.visibility = View.GONE
        context.window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}

