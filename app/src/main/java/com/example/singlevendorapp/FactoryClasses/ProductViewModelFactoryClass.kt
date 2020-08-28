package com.example.singlevendorapp.FactoryClasses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.singlevendorapp.repositories.FirebaseQueryLiveData
import com.example.singlevendorapp.viewmodels.ProductViewModel

@Suppress("UNCHECKED_CAST")
class ProductViewModelFactoryClass(private val liveData: FirebaseQueryLiveData) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProductViewModel(liveData) as T
    }
}