package com.example.singlevendorapp.mycustomviews

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.singlevendorapp.R
import kotlinx.android.synthetic.main.product_slideview_layout.view.*

class SlideView(
    context: Context,
    val percentageHeight: Int? = null,
    childView: View,
    val blurView: BlurView? = null
) : CardView(context) {
    private var sizeLoaded: MutableLiveData<Boolean> = MutableLiveData()
    private var parentHeight: Int = 0
    private var animation: ObjectAnimator? = null
    private var moveDownOffset = 0f
    private var moveUpOffset = 0f
    private lateinit var mainRootView: View
    private var currentlyVisible = false

    var dY: Float = 0f
    var yUpperLimit = 0f
    var yLowerLimit = 0f

    private var isOpened = false

    init {
        View.inflate(context, R.layout.product_slideview_layout, this)
        innerMainRlSlideView.addView(childView)
        sizeLoaded.value = false
        elevation = 80f
        setContentPadding(0, 0, 0, 0)
        clipToPadding = true
        clipChildren = true
        radius = 40f
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        parentHeight = (parent as View).height
        if (percentageHeight != null) {
            setMeasuredDimension(
                (parent as View).width,
                ((parent as View).height * percentageHeight / 100)
            )
            val heightMeasureSpecChild = MeasureSpec.makeMeasureSpec(
                (parentHeight * percentageHeight / 100),
                MeasureSpec.EXACTLY
            )
            rootRlSlideView.measure(widthMeasureSpec, heightMeasureSpecChild)
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        parentHeight = (parent as View).height
        moveDownOffset = parentHeight.toFloat() - 85f
        moveUpOffset = parentHeight.toFloat() - this.height.toFloat()
        mainRootView = this
        this.y = moveDownOffset
        yUpperLimit = moveUpOffset
        yLowerLimit = 0f//parentHeight.toFloat() - (parentHeight * 30 / 100).toFloat()
//        mainRootView.y = parentHeight.toFloat()
        if (sizeLoaded.value == false) {
            sizeLoaded.value = true
        }
    }

    fun slideUp() {
        if (sizeLoaded.value == true) {
            slideUpOnSizeLoaded()
        } else {
            sizeLoaded.observe((context as AppCompatActivity), Observer {
                if (it == true) {
                    slideUpOnSizeLoaded()
                }
            })
        }
    }

    private fun slideUpOnSizeLoaded() {
        isOpened = true
        this.y = moveDownOffset
        this.visibility = View.VISIBLE
        blurView?.visibility = View.VISIBLE
        animation = ObjectAnimator.ofFloat(mainRootView, "Y", moveUpOffset)
        animation?.duration = 400
        animation?.start()

        //setTouchDrag()
        doneButton.setOnClickListener {
            SlideDown()
        }
    }

    fun SlideDown() {
        animation = ObjectAnimator.ofFloat(mainRootView, "Y", moveDownOffset)
        animation?.duration = 400
        animation?.start()
        animation?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {
                TODO("Not yet implemented")
            }

            override fun onAnimationEnd(animation: Animator?) {
                blurView?.visibility = View.INVISIBLE
                currentlyVisible = false
            }

            override fun onAnimationCancel(animation: Animator?) {
                TODO("Not yet implemented")
            }

            override fun onAnimationStart(animation: Animator?) {
                TODO("Not yet implemented")
            }

        })
    }

    fun setTouchDrag() {
        handleRl.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        //550             //560
                        dY = this@SlideView.y - event.rawY;

                    }
                    MotionEvent.ACTION_MOVE -> {
                        /* when we slide down/up our view this dy distance must be added to finger's position to maintain constant
                        distance between finer and view's top position*/
                        if (this@SlideView.y >= yUpperLimit) {
                            val displacment = if (event.rawY + dY > yUpperLimit) {
                                //670  + (-10)
                                event.rawY + dY
                            } else {
                                yUpperLimit
                            }
                            this@SlideView.animate()
                                .y(displacment)
                                .setDuration(0)
                                .start();
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        if (this@SlideView.y > yLowerLimit) {
                            // this@SlideView.y = event.rawY + dY
                            // SlideDown()
                        }
                    }

                }
                return true
            }
        })
    }
}
