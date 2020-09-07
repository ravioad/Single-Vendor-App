package com.example.singlevendorapp.repositories

import com.example.singlevendorapp.MyApplication
import com.example.singlevendorapp.Status
import com.example.singlevendorapp.models.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class FavoritesRepository {

    suspend fun getFavoritesList(): Status<List<ProductModel>> {
        return try{
            Status.Success<List<ProductModel>>(getList())
        }catch (e: Exception){
            Status.Error(e.message!!)
        }
    }

    private suspend fun getList(): List<ProductModel>{
        var list: List<ProductModel>? = null
        withContext(Dispatchers.IO){
            list = MyApplication.db.productModelDao().getAllProducts()
        }
        return list!!
    }
}