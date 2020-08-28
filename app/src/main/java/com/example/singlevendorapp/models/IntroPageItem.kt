package com.example.singlevendorapp.models

class IntroPageItem(
    private val title: String,
    private val description: String,
    private val imageResource: Int
) {
    fun getTitle() = title
    fun getDescription() = description
    fun getImageResource() = imageResource
}