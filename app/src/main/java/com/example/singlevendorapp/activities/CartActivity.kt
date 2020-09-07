package com.example.singlevendorapp.activities

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.singlevendorapp.FactoryClasses.ProductDependencyInjectorUtility
import com.example.singlevendorapp.MyApplication
import com.example.singlevendorapp.MyBaseClass
import com.example.singlevendorapp.R
import com.example.singlevendorapp.Status
import com.example.singlevendorapp.adapters.CartRecyclerViewAdapter
import com.example.singlevendorapp.models.CartItemModel
import com.example.singlevendorapp.viewmodels.CartViewModel
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class CartActivity : MyBaseClass() {

    private val cartViewModel: CartViewModel by viewModels {
        ProductDependencyInjectorUtility.getCartViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        setSupportActionBar(cart_topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        addCommonViews(cart_rootLayout, this)

        cart_goto_menu_button.setOnClickListener {
            finish()
        }
        cartViewModel.getCartList().observe(this, Observer {
            when (it) {
                is Status.Success -> {
                    hideProgressBar()
                    val list: ArrayList<CartItemModel> = ArrayList(it.data!!)
                    val grandTotal = "Rs. ${calculateGrandTotal(list)}"
                    cart_grand_total.text = grandTotal
                    if (list.size == 0) {
                        cart_order_button.visibility = View.GONE
                        cart_recyclerview.visibility = View.GONE
                        cart_empty.visibility = View.VISIBLE
                        cart_topLayout.visibility = View.GONE
                        cart_goto_menu_button.visibility = View.VISIBLE
                    } else {
                        cart_order_button.visibility = View.VISIBLE
                        cart_recyclerview.visibility = View.VISIBLE
                        cart_empty.visibility = View.GONE
                        cart_topLayout.visibility = View.VISIBLE
                        cart_goto_menu_button.visibility = View.GONE
                    }
                    cart_recyclerview.apply {
                        layoutManager = LinearLayoutManager(this@CartActivity)
                        adapter = CartRecyclerViewAdapter(this@CartActivity, list)
                    }
                    runBlocking {
                        updateCount()
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


    private suspend fun updateCount() {
        withContext(Dispatchers.IO) {
            val count = MyApplication.db.cartItemModelDao().getCount()
            val totalItemsString = "$count Item(s) in Cart"
            cart_total_items.text = totalItemsString
        }
    }

    private fun calculateGrandTotal(list: ArrayList<CartItemModel>): Int {
        var grandTotal = 0
        for (item in list) {
            grandTotal += item.totalPrice
        }
        return grandTotal
    }
}