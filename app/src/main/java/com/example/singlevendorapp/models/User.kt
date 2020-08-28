package com.example.singlevendorapp.models

class User(
    private val name: String,
    private val email: String,
    private val userId: String
) {
    fun getName() = name
    fun getEmail() = email
    fun getId() = userId
}