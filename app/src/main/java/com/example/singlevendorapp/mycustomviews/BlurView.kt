package com.example.singlevendorapp.mycustomviews

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import com.example.singlevendorapp.R

class BlurView : RelativeLayout {

    constructor(context: Context, color:String="#000000") : super(context){
        init(color)

    }

    private fun init(color: String) {
        View.inflate(context, R.layout.blur_view_layout,this)
        this.elevation=80f
        this.setBackgroundColor(Color.parseColor(color))
        background.alpha=214
        this.isClickable=true
        this.isFocusable=true
    }

    constructor(context: Context,attributeSet: AttributeSet) : super(context,attributeSet){

    }



}