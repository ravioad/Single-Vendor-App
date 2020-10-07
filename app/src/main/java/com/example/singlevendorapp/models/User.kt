package com.example.singlevendorapp.models

class User() {
    private var userName: String? = null
    private var userEmail: String? = null
    private var id: String? = null

    constructor(
        name: String,
        email: String,
        id: String
    ) : this() {
        this.userName = name
        this.userEmail = email
        this.id = id
    }

    fun getName() = userName
    fun getEmail() = userEmail
    fun getId() = id

}