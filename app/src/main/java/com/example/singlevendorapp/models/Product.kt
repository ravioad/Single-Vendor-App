package com.example.singlevendorapp.models

class Product (
    private val title: String,
    private val image: Int,
    private val price: Int
) {
    fun getTitle() = title
    fun getImage() = image
    fun getPrice() = price
}