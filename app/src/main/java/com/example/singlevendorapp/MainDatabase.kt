package com.example.singlevendorapp

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.singlevendorapp.models.ProductModel

@Database(entities = [ProductModel::class], version = 1)
abstract class MainDatabase : RoomDatabase() {
    abstract fun productModelDao(): ProductModelDao
}