package com.example.singlevendorapp.repositories

import com.example.singlevendorapp.MyApplication
import com.example.singlevendorapp.Status
import com.example.singlevendorapp.models.CartItemModel
import com.example.singlevendorapp.models.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CartRepository {
    suspend fun getCartList(): Status<List<CartItemModel>> {
        return try {
            Status.Success<List<CartItemModel>>(getList())
        } catch (e: Exception) {
            Status.Error(e.message!!)
        }
    }

    private suspend fun getList(): List<CartItemModel> {
        var list: List<CartItemModel>? = null
        withContext(Dispatchers.IO) {
            list = MyApplication.db.cartItemModelDao().getAllProducts()
        }
        return list!!
    }
}