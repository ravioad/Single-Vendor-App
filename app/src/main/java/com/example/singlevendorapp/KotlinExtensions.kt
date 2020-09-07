package com.example.singlevendorapp

import android.content.Context
import android.widget.Toast

fun Context.toast(message:String){
    Toast.makeText(this, message , Toast.LENGTH_LONG).show()
}

fun Context.shortToast(message:String){
    Toast.makeText(this, message , Toast.LENGTH_SHORT).show()
}