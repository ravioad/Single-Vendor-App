package com.example.singlevendorapp

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.singlevendorapp.mycustomviews.*
import com.google.firebase.auth.FirebaseAuth

open class MyBaseClass : AppCompatActivity() {
    private var rootLayout: RelativeLayout? = null
    private lateinit var context: Activity
    lateinit var myBlurView: BlurView
    private var myLoadingView: MyLoadingView? = null
    private var progressBar: ProgressBar? = null
    private var myAlertBox: MyAlertBox? = null
    fun addCommonViews(rootRl: RelativeLayout?, context: Activity) {
        this.rootLayout = rootRl
        this.context = context
        addBlurView()
        addAlertBox()
        addProgressBar()
        addLoadingView()
    }

    private fun addProgressBar() {
        progressBar = ProgressBar(context)
        rootLayout?.addView(progressBar)
        val layoutParams =
            progressBar?.layoutParams as RelativeLayout.LayoutParams
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE)
        progressBar?.layoutParams = layoutParams
        progressBar?.elevation = 30f
        hideProgressBar()
    }

    private fun addLoadingView() {
        myLoadingView = MyLoadingView(context, myBlurView)
        rootLayout?.addView(myLoadingView)
    }

    private fun addBlurView() {
        myBlurView = BlurView(this)
        rootLayout!!.addView(myBlurView)
        myBlurView.visibility = View.INVISIBLE
    }

    fun showLoadingView() {
        myLoadingView?.showLoading()
    }

    fun hideLoadingView() {
        myLoadingView?.hideLoading()
    }

    fun addAlertBox(isSingleButton: Boolean = true, firstButtonText: String = "Ok", secondButtonText: String = "Cancel") {
        myAlertBox = MyAlertBox(context, myBlurView, isSingleButton, firstButtonText, secondButtonText)
        rootLayout!!.addView(myAlertBox)
    }

    fun addAlertBoxListeners(firstButton: () -> Unit, secondButton: () -> Unit) {
        myAlertBox?.setOnClickListener(object : Type2ListenerCallback {
            override fun button1ClickListener() {
                firstButton.invoke()
            }

            override fun button2ClickListener() {
                secondButton.invoke()
            }

        })
    }

    fun addAlertBoxListener(buttonListener: () -> Unit) {
        myAlertBox?.setOnClickListener(object: Type1ListenerCallback{
            override fun buttonClickListener() {
                buttonListener.invoke()
            }

        })
    }

    fun showAlertBox(message: String = "") {
        myAlertBox!!.setDialogMessage(message)
        myAlertBox!!.showDialog()
    }

    fun hideDialog() {
        myAlertBox!!.hideDialog()
    }

    ///////////////////////////////////////
    val uid: String
        get() = FirebaseAuth.getInstance().currentUser!!.uid

    fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun showProgressBar(blockUI: Boolean) {
        progressBar?.visibility = View.VISIBLE
        if (blockUI) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
        }
    }

    fun hideProgressBar() {
        progressBar?.visibility = View.GONE
        window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}