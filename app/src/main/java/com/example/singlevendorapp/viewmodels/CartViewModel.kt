package com.example.singlevendorapp.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.singlevendorapp.Status
import com.example.singlevendorapp.models.CartItemModel
import com.example.singlevendorapp.models.ProductModel
import com.example.singlevendorapp.repositories.CartRepository
import kotlinx.coroutines.launch

class CartViewModel(private val cartRepository: CartRepository) : ViewModel() {

    private val cartItems: MutableLiveData<Status<List<CartItemModel>>> by lazy {
        MutableLiveData<Status<List<CartItemModel>>>().also {
            loadCartItems(it)
        }
    }

    fun getCartList(): MutableLiveData<Status<List<CartItemModel>>>{
        return cartItems
    }

    private fun loadCartItems(it: MutableLiveData<Status<List<CartItemModel>>>) {
        this.viewModelScope.launch {
            it.value = Status.Loading()
            val list: Status<List<CartItemModel>> = cartRepository.getCartList()
            it.value = list
        }
    }
}