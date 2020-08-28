package com.example.singlevendorapp.repositories

import androidx.lifecycle.LiveData
import com.example.singlevendorapp.Status
import com.google.firebase.database.*

class FirebaseQueryLiveData(refer: DatabaseReference) : LiveData<Status<DataSnapshot>>() {

    init {
        value = Status.Loading()
    }

    val reference: DatabaseReference = refer
//    private var reference: DatabaseReference =
//        FirebaseDatabase.getInstance().reference.child("products")


    private val lis = object : ValueEventListener {
        override fun onCancelled(error: DatabaseError) {
            value = Status.Error(error.message)
        }

        override fun onDataChange(snapshot: DataSnapshot) {
            value = Status.Success(snapshot)
        }
    }

    override fun onActive() {
        reference.addValueEventListener(lis)
    }

    override fun onInactive() {
        reference.removeEventListener(lis)
    }


//    class MyValueEventListener : ValueEventListener {
//        override fun onCancelled(error: DatabaseError) {
//        }
//
//        override fun onDataChange(snapshot: DataSnapshot) {
//
//        }


}