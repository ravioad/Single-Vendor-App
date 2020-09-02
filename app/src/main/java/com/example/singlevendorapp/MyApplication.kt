package com.example.singlevendorapp

import android.app.Application
import android.util.Log
import androidx.room.Room

class MyApplication : Application() {

    companion object {
        lateinit var db: MainDatabase
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("application onCreate", "Inside it")
        db = Room.databaseBuilder(
            applicationContext,
            MainDatabase::class.java, "MyRoomDatabase"
        ).build()
    }
}