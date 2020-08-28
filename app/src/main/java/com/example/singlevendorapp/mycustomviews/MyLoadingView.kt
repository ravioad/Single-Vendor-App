package com.example.singlevendorapp.mycustomviews

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.singlevendorapp.R


class MyLoadingView(context: Context, val blurView: BlurView) : RelativeLayout(context) {
    var bottomPos = 0f
    var centrePos = 0f
    var animator: ObjectAnimator? = null

    //var sizeLoaded=false
    val sizeLoaded = MutableLiveData<Boolean>()

    init {
        sizeLoaded.value = false
        initialiseView()
    }

    private fun initialiseView() {
        View.inflate(context, R.layout.my_loading_view_layout, this)
        this.elevation = 85f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val parentHeight = (this.parent as View).height
        bottomPos = parentHeight.toFloat()
        centrePos = (parentHeight / 2).toFloat() - (this.height / 2)
        this.y = bottomPos
        this.visibility = View.INVISIBLE
        sizeLoaded.value = true
    }

    fun showLoading() {
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

    private fun showLoadedView() {
        blurView.visibility = View.VISIBLE
        this@MyLoadingView.y = bottomPos
        this@MyLoadingView.visibility = View.VISIBLE
        animator = ObjectAnimator.ofFloat(this@MyLoadingView, "Y", centrePos)
        animator?.duration = 1000
        animator?.start()
    }


    fun hideLoading() {
        animator = ObjectAnimator.ofFloat(this, "Y", bottomPos)
        animator?.duration = 1000
        animator?.start()
        animator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                this@MyLoadingView.visibility = View.INVISIBLE
                blurView.visibility = View.INVISIBLE
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })
    }
}