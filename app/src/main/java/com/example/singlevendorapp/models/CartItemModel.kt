package com.example.singlevendorapp.models

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CartItemModel(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String = "",
    val image: String = "",
    val unitPrice: Int = 0,
    val totalPrice: Int = 0,
    val quantity: Int = 0
) {
    //cart items will be initially stored in local database
    // after checkout the order will be stored in Firebase database with new Reference "Orders"
}