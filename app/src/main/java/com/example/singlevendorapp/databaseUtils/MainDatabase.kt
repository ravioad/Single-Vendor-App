package com.example.singlevendorapp.databaseUtils

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.singlevendorapp.models.CartItemModel
import com.example.singlevendorapp.models.ProductModel

@Database(entities = [ProductModel::class, CartItemModel::class], version = 2)
abstract class MainDatabase : RoomDatabase() {

    abstract fun productModelDao(): ProductModelDao
    abstract fun cartItemModelDao(): CartItemModelDao
}