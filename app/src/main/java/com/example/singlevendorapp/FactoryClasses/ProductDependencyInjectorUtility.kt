package com.example.singlevendorapp.FactoryClasses

import com.example.singlevendorapp.repositories.FirebaseQueryLiveData

object ProductDependencyInjectorUtility {

    fun getProductViewModelFactory(): ProductViewModelFactoryClass {
        val vv = FirebaseQueryLiveData()
//        Log.e("Value check", (vv.value==null).toString())
        return ProductViewModelFactoryClass(vv)
    }
}