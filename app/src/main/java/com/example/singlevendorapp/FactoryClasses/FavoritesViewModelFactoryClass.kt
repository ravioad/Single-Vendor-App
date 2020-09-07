package com.example.singlevendorapp.FactoryClasses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.singlevendorapp.repositories.FavoritesRepository
import com.example.singlevendorapp.viewmodels.FavoritesViewModel

@Suppress("UNCHECKED_CAST")
class FavoritesViewModelFactoryClass : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return FavoritesViewModel(FavoritesRepository()) as T
    }
}