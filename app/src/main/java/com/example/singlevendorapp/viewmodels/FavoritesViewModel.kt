package com.example.singlevendorapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.singlevendorapp.Status
import com.example.singlevendorapp.models.ProductModel
import com.example.singlevendorapp.repositories.FavoritesRepository
import kotlinx.coroutines.launch

class FavoritesViewModel(private val favoritesRepository: FavoritesRepository) : ViewModel() {


    private val favoriteItems: MutableLiveData<Status<List<ProductModel>>> by lazy {
        MutableLiveData<Status<List<ProductModel>>>().also {
            loadFavorites(it)
        }
    }

    fun getFavoritesItems(): MutableLiveData<Status<List<ProductModel>>> {
        return favoriteItems
    }

    private fun loadFavorites(it: MutableLiveData<Status<List<ProductModel>>>) {
        this.viewModelScope.launch {
            it.value = Status.Loading()
            val list: Status<List<ProductModel>> = favoritesRepository.getFavoritesList()
            it.value = list
        }
    }
}