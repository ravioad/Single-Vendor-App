package com.example.singlevendorapp.mycustomviews

import com.example.singlevendorapp.R
import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.size
import androidx.core.view.updateLayoutParams
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.my_alert_box_layout.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

interface Type2ListenerCallback {
    fun button1ClickListener()
    fun button2ClickListener()
}

interface Type1ListenerCallback {
    fun buttonClickListener()
}

class MyAlertBox(context: Context, val blurView: BlurView, val isSingleButton: Boolean) :
    RelativeLayout(context) {
    var bottomPos = 0f
    var centerPos = 0f
    var animator: ObjectAnimator? = null
    var sizeLoaded = MutableLiveData<Boolean>()
    var listener: Type2ListenerCallback? = null
    var listener2: Type1ListenerCallback? = null

    init {
        sizeLoaded.value = false
        initializeView()

    }

    private fun initializeView() {
        View.inflate(context, R.layout.my_alert_box_layout, this)
        this.elevation = 85f

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val parentHeight = (this.parent as View).height
        bottomPos = 0f - (this.height)
        centerPos = (parentHeight / 2).toFloat() - (this.height / 2)
//        this.y = centerPos
        this.visibility = View.INVISIBLE
//        this.y = centerPos
        sizeLoaded.value = true
    }

    fun showDialog() {
        (context as AppCompatActivity).lifecycleScope.launch {
            if (sizeLoaded.value == true) {
                showLoadedView()
            } else {
                sizeLoaded.observe(context as AppCompatActivity, Observer {
                    if (sizeLoaded.value == true) {
                        showLoadedView()
                    }
                })
            }
        }
    }
//
//    private suspend fun waitForViewTobeLoaded() {
//        withContext(Dispatchers.IO) {
//            while (!sizeLoaded) {
//                //waiting for size load
//            }
//        }
//    }

//    private fun scaleUp() {
//        val x = PropertyValuesHolder.ofFloat(View.SCALE_X, 1.3f)
//        val y = PropertyValuesHolder.ofFloat(View.SCALE_Y, 1.3f)
//        animator = ObjectAnimator.ofPropertyValuesHolder(image, x, y)
//        animator?.duration = 1000
//        animator?.repeatCount = ObjectAnimator.INFINITE
//        animator?.repeatMode = ObjectAnimator.REVERSE
//        animator?.start()
//    }


    fun setDialogMessage(message: String = "") {
        main_dialog_message.text = message
    }

    private fun showLoadedView() {
        if (isSingleButton) {
            dialogType1()
        } else {
            dialogType2()
        }
        blurView.visibility = View.VISIBLE
        this.y = bottomPos
        this.visibility = View.VISIBLE
        animator = ObjectAnimator.ofFloat(this, "Y", centerPos)
        animator?.duration = 600
        animator?.start()


    }

    fun dialogType1(text1: String = "Cancel") {
        val button = Button(context)
        dialog_layout.addView(button)
        button.layoutParams = getButtonParams(
            button,
            text1,
            RelativeLayout.CENTER_HORIZONTAL,
            R.drawable.rounded_button
        )

        button.setOnClickListener {
            listener2?.buttonClickListener()
        }
    }

    fun dialogType2(text1: String = "Ok", text2: String = "Cancel") {
        val button1 = Button(context)
        val button2 = Button(context)
        dialog_layout.addView(button1)
        dialog_layout.addView(button2)
        button1.layoutParams = getButtonParams(
            button1,
            text1,
            RelativeLayout.ALIGN_PARENT_START,
            R.drawable.rounded_button2
        )
        button2.layoutParams = getButtonParams(
            button2,
            text2,
            RelativeLayout.ALIGN_PARENT_END,
            R.drawable.rounded_button
        )

        button1.setOnClickListener {
            listener?.button1ClickListener()
        }

        button2.setOnClickListener {
            listener?.button2ClickListener()
        }
    }

    fun getButtonParams(button: Button, text: String, align: Int, background: Int): LayoutParams {
        val params: RelativeLayout.LayoutParams = button.layoutParams as RelativeLayout.LayoutParams
        button.setBackgroundResource(background)
        button.text = text
        button.setTextColor(Color.parseColor("#FFFFFF"))
        params.topMargin = 90
        params.width = 500
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        params.addRule(align)
        return params
    }

    fun setOnClickListener(listenerCallback: Type2ListenerCallback) {
        this.listener = listenerCallback
    }

    fun setOnClickListener(listenerCallback: Type1ListenerCallback) {
        this.listener2 = listenerCallback
    }

    fun hideDialog() {
        animator = ObjectAnimator.ofFloat(this, "Y", bottomPos)
        animator?.duration = 600
        animator?.start()
        animator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                this@MyAlertBox.visibility = View.INVISIBLE
//                this@CustomLoadingView.showLoadedView()
                //  showLoading()
                blurView.visibility = View.INVISIBLE

            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {


            }

        })
    }
}