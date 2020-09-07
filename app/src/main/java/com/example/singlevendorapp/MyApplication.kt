package com.example.singlevendorapp

import android.app.Application
import android.util.Log
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.singlevendorapp.databaseUtils.MainDatabase

class MyApplication : Application() {

    companion object {
        lateinit var db: MainDatabase
    }

    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `CartItemModel` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `name` TEXT NOT NULL , `image` TEXT NOT NULL,`unitPrice` INTEGER NOT NULL,`totalPrice` INTEGER NOT NULL )")
        }
    }

    override fun onCreate() {
        super.onCreate()
        Log.e("application onCreate", "Inside it")
        db = Room.databaseBuilder(
            applicationContext,
            MainDatabase::class.java, "MyRoomDatabase"
        ).addMigrations(MIGRATION_1_2).build()
    }
}