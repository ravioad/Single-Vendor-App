package com.example.singlevendorapp.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.singlevendorapp.Status
import com.example.singlevendorapp.models.ProductModel
import com.google.firebase.database.*
import com.google.firebase.database.ktx.getValue
import kotlinx.coroutines.launch

class HomeRepository {

    private var reference: DatabaseReference =
        FirebaseDatabase.getInstance().reference.child("products")
    private var productData = MutableLiveData<ArrayList<ProductModel>>();

    init {
        getDataFromDatabase()
    }

    suspend fun getProductData(): Status<ArrayList<ProductModel>> {
        return try {
//            reference.addValueEventListener(object : ValueEventListener {
//                override fun onDataChange(snapshot: DataSnapshot) {
//                    for (products in snapshot.children) {
//                        val product: ProductModel = products.getValue(ProductModel::class.java)!!
//                        list.add(product)
//                        Log.e("list Item", product.name)
//                    }
//                }
//
//                override fun onCancelled(error: DatabaseError) {
//
//                }
//            })
//            val list: ArrayList<ProductModel> = arrayListOf(
//                ProductModel(
//                    "waloo",
//                    11,
//                    "https://firebasestorage.googleapis.com/v0/b/single-vendor-app.appspot.com/o/images%2Ff17c7d06-621c-40d7-a06b-73335222aea3?alt=media&token=f5d619d2-97a9-44a6-a220-4f5a2c1584de",
//                    22
//                ),
//                ProductModel(
//                    "kandero",
//                    11,
//                    "https://firebasestorage.googleapis.com/v0/b/single-vendor-app.appspot.com/o/images%2F008e819f-70a8-405a-a579-502ee1b4cb88?alt=media&token=a7606287-6e61-4bba-a8ce-49c3aa28e5ee",
//                    33
//                ),
//                ProductModel(
//                    "sadf",
//                    11,
//                    "https://firebasestorage.googleapis.com/v0/b/single-vendor-app.appspot.com/o/images%2Fa66e8255-f6a1-497f-bb4a-69e413377113?alt=media&token=4f5c0dbb-ed1c-4a0b-9d60-cf068df1a558",
//                    22
//                )
//            )
            Log.e("Last Returning list", productData.value!!.size.toString())
            Status.Success<ArrayList<ProductModel>>(productData.value!!)
        } catch (e: Exception) {
            Status.Error(e.message.toString())
        }
    }

    fun getDataFromDatabase() {

        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = ArrayList<ProductModel>()
                for (products in snapshot.children) {
                    val product: ProductModel = products.getValue(ProductModel::class.java)!!
                    list.add(product)
                    Log.e("list Item", product.name)
                }

                productData.value = list
            }

            override fun onCancelled(error: DatabaseError) {

            }
        })

    }

    internal var products:MutableLiveData<ArrayList<ProductModel>>
        get() { return productData}
        set(value) {productData = value}
}