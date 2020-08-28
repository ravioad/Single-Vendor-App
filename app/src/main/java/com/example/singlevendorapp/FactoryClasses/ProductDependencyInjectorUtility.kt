package com.example.singlevendorapp.FactoryClasses

import com.example.singlevendorapp.repositories.FirebaseQueryLiveData
import com.google.firebase.database.DatabaseReference

object ProductDependencyInjectorUtility {

    fun getProductViewModelFactory(reference: DatabaseReference): ProductViewModelFactoryClass {
//        Log.e("Value check", (vv.value==null).toString())
        return ProductViewModelFactoryClass( FirebaseQueryLiveData(reference))
    }
}