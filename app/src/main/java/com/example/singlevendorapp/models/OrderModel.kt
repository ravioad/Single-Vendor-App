package com.example.singlevendorapp.models

import java.io.Serializable

data class OrderModel(
    val orderId: String = "",
    val name: String = "",
    val phone: String = "",
    val address: String = "",
    val products: ArrayList<CartItemModel>? = null,
    val timestamp: String = ""
) : Serializable {

}