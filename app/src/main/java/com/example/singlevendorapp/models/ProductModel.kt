package com.example.singlevendorapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class ProductModel(
    val name: String = "",
    val price: Int = 0,
    val image: String = "",
    @PrimaryKey(autoGenerate = true) val quantity: Int = 0
): Serializable {
//
//    fun getName() = name
//    fun getPrice() = price
//    fun getImageUri() = image
//    fun getQuantity() = quantity


}