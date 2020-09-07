package com.example.singlevendorapp.FactoryClasses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.singlevendorapp.repositories.CartRepository
import com.example.singlevendorapp.viewmodels.CartViewModel

@Suppress("UNCHECKED_CAST")
class CartViewModelFactoryClass: ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CartViewModel(CartRepository()) as T
    }
}