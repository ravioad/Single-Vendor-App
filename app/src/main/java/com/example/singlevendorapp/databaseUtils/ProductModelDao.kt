package com.example.singlevendorapp.databaseUtils

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.singlevendorapp.models.ProductModel

@Dao
interface ProductModelDao {

    @Query("SELECT EXISTS(SELECT * FROM ProductModel WHERE image = :image)")
    fun isAvailable(image: String): Boolean

    @Query("SELECT * FROM ProductModel")
    fun getAllProducts():List<ProductModel>

    @Insert
    fun insert(product: ProductModel)

    @Delete
    fun delete(product: ProductModel)
}