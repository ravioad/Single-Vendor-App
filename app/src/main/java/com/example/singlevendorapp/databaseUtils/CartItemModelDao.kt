package com.example.singlevendorapp.databaseUtils

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.singlevendorapp.models.CartItemModel

@Dao
interface CartItemModelDao {
    @Query("SELECT EXISTS(SELECT * FROM CartItemModel WHERE image = :image)")
    fun isAvailable(image: String): Boolean

    @Query("SELECT COUNT(unitPrice) FROM CartItemModel")
    fun getCount(): Int

    @Query("SELECT * FROM CartItemModel")
    fun getAllProducts(): List<CartItemModel>

    @Insert
    fun insert(cartItem: CartItemModel)

    @Delete
    fun delete(cartItem: CartItemModel)


}