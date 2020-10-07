package com.example.singlevendorapp.activities

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.singlevendorapp.FactoryClasses.ProductDependencyInjectorUtility
import com.example.singlevendorapp.MyBaseClass
import com.example.singlevendorapp.R
import com.example.singlevendorapp.Status
import com.example.singlevendorapp.adapters.FavoritesRecyclerViewAdapter
import com.example.singlevendorapp.models.ProductModel
import com.example.singlevendorapp.viewmodels.FavoritesViewModel
import kotlinx.android.synthetic.main.activity_favorites.*

class FavoritesActivity : MyBaseClass() {
    private val favoritesViewModel: FavoritesViewModel by viewModels {
        ProductDependencyInjectorUtility.getFavoritesViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
        setSupportActionBar(favorite_topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        addCommonViews(favorites_rootLayout, this)

        favorite_goto_menu_button.setOnClickListener {
            finish()
        }

        favoritesViewModel.getFavoritesItems().observe(this, Observer {
            when (it) {
                is Status.Success -> {
                    hideProgressBar()
                    val list: ArrayList<ProductModel> = ArrayList(it.data!!)
                    if (list.size == 0) {
                        favorite_recyclerview.visibility = View.GONE
                        no_favorites_added.visibility = View.VISIBLE
                        favorite_goto_menu_button.visibility = View.VISIBLE

                    } else {
                        favorite_recyclerview.visibility = View.VISIBLE
                        no_favorites_added.visibility = View.GONE
                        favorite_goto_menu_button.visibility = View.GONE
                    }
                    favorite_recyclerview.apply {
                        layoutManager = LinearLayoutManager(this@FavoritesActivity)
                        adapter = FavoritesRecyclerViewAdapter(this@FavoritesActivity, list)
                    }
                }
                is Status.Loading -> {
                   showProgressBar(true)
                }
                is Status.Error -> {
                    hideProgressBar()
                    showAlertBox(it.message!!)
                }
            }
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.favorites_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when(item.itemId){

        }
        return super.onOptionsItemSelected(item)
    }
}