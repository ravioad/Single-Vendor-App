package com.example.singlevendorapp.mycustomviews

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.singlevendorapp.R
import kotlinx.android.synthetic.main.product_slideview_layout.view.*

class SlideView(
    context: Context,
    private val percentageHeight: Int? = null,
    val blurView: BlurView? = null
) : CardView(context) {
    private var sizeLoaded: MutableLiveData<Boolean> = MutableLiveData()
    private var downOffsetLoaded: MutableLiveData<Boolean> = MutableLiveData()
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
    private var quantityText: TextView? = null
//
//    private var productCount = 1
//    private val realPrice = product?.price
//    private val title = product?.name
//    private var calculatedPrice = 0

    init {
        View.inflate(context, R.layout.product_slideview_layout, this)
        blurView?.visibility = View.INVISIBLE
        sizeLoaded.value = false
        downOffsetLoaded.value = false
        elevation = 80f
        setContentPadding(0, 0, 0, 0)
        clipToPadding = true
        clipChildren = true
        radius = 40f
    }


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        Log.e("debug: OnMeasure", "called....")
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
        Log.e("debug: onSizeChanged", "Called....")
        super.onSizeChanged(w, h, oldw, oldh)
        parentHeight = (parent as View).height
        moveUpOffset = parentHeight.toFloat() - this.height.toFloat()
        mainRootView = this
        handleRl.viewTreeObserver.addOnGlobalLayoutListener {
            moveDownOffset = parentHeight.toFloat() - handleRl.height.toFloat()
            if (downOffsetLoaded.value == false) {
                downOffsetLoaded.value = true
            }
        }
        initializeViewPosition()
        yUpperLimit = moveUpOffset
        yLowerLimit = parentHeight.toFloat() - (parentHeight * 20 / 100).toFloat()

        if (sizeLoaded.value == false) {
            sizeLoaded.value = true
            handle.setOnClickListener {
                if (isOpened) {
                    slideDown()
                } else {
                    slideUp(moveDownOffset)
                }
            }
            setTouchDrag()
        }
    }

    private fun rotateHandle(isOpen: Boolean) {
        val animator =
            if (isOpen) {
                ObjectAnimator.ofFloat(handle, "rotation", 180f)
            } else {
                ObjectAnimator.ofFloat(handle, "rotation", 0f)
            }
        animator.duration = 400
        animator.start()
    }

    private fun initializeViewPosition() {
        if (downOffsetLoaded.value == true) {
            mainRootView.y = moveDownOffset
        } else {
            downOffsetLoaded.observe((context as AppCompatActivity), Observer {
                if (it == true) {
                    mainRootView.y = moveDownOffset
                }
            })
        }
    }

    fun showSlideView(isVisible: Boolean) {
        if (isVisible) {
            ObjectAnimator.ofFloat(mainRootView, "translationY", moveDownOffset).apply {
                duration = 400
                start()
            }
        } else {
            ObjectAnimator.ofFloat(mainRootView, "translationY", parentHeight.toFloat()).apply {
                duration = 400
                start()
            }
        }
    }

    fun slideUp(currentPosition: Float) {
        if (sizeLoaded.value == true) {
            slideUpOnSizeLoaded(currentPosition)
        } else {
            sizeLoaded.observe((context as AppCompatActivity), Observer {
                if (it == true) {
                    slideUpOnSizeLoaded(currentPosition)
                }
            })
        }
    }

    private fun slideUpOnSizeLoaded(currentPosition: Float) {
//        handleSlideViewClickListeners()
        isOpened = true
        rotateHandle(true)
        this.y = currentPosition
        this.visibility = View.VISIBLE
        animation = ObjectAnimator.ofFloat(mainRootView, "Y", moveUpOffset)
        animation?.duration = 400
        animation?.start()
        animation?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                blurView?.visibility = View.VISIBLE
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationRepeat(animation: Animator?) {

            }

        })
    }

    fun slideDown() {
        rotateHandle(false)
        animation = ObjectAnimator.ofFloat(mainRootView, "Y", moveDownOffset)
        animation?.duration = 400
        animation?.start()
        animation?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                blurView?.visibility = View.INVISIBLE
                currentlyVisible = false
                isOpened = false
            }

            override fun onAnimationCancel(animation: Animator?) {

            }

            override fun onAnimationStart(animation: Animator?) {

            }

        })
    }

    private fun setTouchDrag() {
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
                            if (displacment < moveDownOffset) {
                                this@SlideView.animate()
                                    .y(displacment)
                                    .setDuration(0)
                                    .start()
                            }
                        }
                    }
                    MotionEvent.ACTION_UP -> {
                        if (this@SlideView.y > yLowerLimit) {
                            // this@SlideView.y = event.rawY + dY
                            slideDown()
                        } else {
                            val currentPos = if ((event.rawY + dY) < yUpperLimit) {
                                yUpperLimit
                            } else {
                                (event.rawY + dY)
                            }
                            slideUp(currentPos)
                        }
                    }

                }
                return true
            }
        })
    }
}
