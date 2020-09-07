package com.example.singlevendorapp.FactoryClasses

import com.example.singlevendorapp.repositories.FirebaseQueryLiveData
import com.google.firebase.database.DatabaseReference

object ProductDependencyInjectorUtility {

    fun getProductViewModelFactory(reference: DatabaseReference): ProductViewModelFactoryClass {
        return ProductViewModelFactoryClass(FirebaseQueryLiveData(reference))
    }

    fun getFavoritesViewModelFactory(): FavoritesViewModelFactoryClass {
        return FavoritesViewModelFactoryClass()
    }

    fun getCartViewModelFactory(): CartViewModelFactoryClass{
        return  CartViewModelFactoryClass()
    }
}