package com.example.singlevendorapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.singlevendorapp.repositories.FirebaseQueryLiveData
import com.example.singlevendorapp.Status
import com.google.firebase.database.DataSnapshot

class ProductViewModel(private val liveData: FirebaseQueryLiveData) : ViewModel() {


//    private val reference = FirebaseDatabase.getInstance().reference.child("products")
//    private val liveData = FirebaseQueryLiveData()
//
//    fun getDataSnapShotLiveData(): LiveData<Status<ArrayList<ProductModel>>> {
//        return liveData
//    }

//    private val product: MutableLiveData<Status<ArrayList<ProductModel>>> by lazy {
//        MutableLiveData<Status<ArrayList<ProductModel>>>().also {
//            loadProduct(it)
//        }
//    }
//    private val product: MutableLiveData<Status<DataSnapshot>> by lazy {
//        MutableLiveData<Status<DataSnapshot>>().also {
//            loadProduct(it)
//        }
//    }

    fun getProducts(): LiveData<Status<DataSnapshot>> {
        return liveData
    }

//    private fun loadProduct(product: MutableLiveData<Status<DataSnapshot>>) {
//        product.value = Status.Loading()
//        product.value = liveData.value
//    }
}