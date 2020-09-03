package com.example.singlevendorapp.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.singlevendorapp.MyApplication
import com.example.singlevendorapp.R
import com.example.singlevendorapp.adapters.FavoritesRecyclerViewAdapter
import com.example.singlevendorapp.models.ProductModel
import kotlinx.android.synthetic.main.activity_favorites.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class FavoritesActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        setSupportActionBar(favorite_topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        runBlocking {
            populateData()
        }

    }


    private suspend fun populateData() {
        withContext(Dispatchers.IO) {
            val list = MyApplication.db.productModelDao().getAllProducts()
            val arrayList: ArrayList<ProductModel> = ArrayList(list)
            if(arrayList.size == 0){
                favorite_recyclerview.visibility = View.GONE
                no_favorites_added.visibility = View.VISIBLE

            }else{
                favorite_recyclerview.visibility = View.VISIBLE
                no_favorites_added.visibility = View.GONE
            }
            favorite_recyclerview.apply {
                layoutManager = LinearLayoutManager(this@FavoritesActivity)
                adapter = FavoritesRecyclerViewAdapter(this@FavoritesActivity, arrayList)
            }
        }
    }
}