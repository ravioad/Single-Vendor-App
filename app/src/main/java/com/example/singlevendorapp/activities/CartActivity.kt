package com.example.singlevendorapp.activities

import android.app.Dialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.singlevendorapp.*
import com.example.singlevendorapp.FactoryClasses.ProductDependencyInjectorUtility
import com.example.singlevendorapp.adapters.CartRecyclerViewAdapter
import com.example.singlevendorapp.models.CartItemModel
import com.example.singlevendorapp.models.OrderModel
import com.example.singlevendorapp.viewmodels.CartViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_cart.*
import kotlinx.android.synthetic.main.activity_singup.*
import kotlinx.android.synthetic.main.cart_checkout_custom_dialog.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CartActivity : MyBaseClass() {

    private val cartViewModel: CartViewModel by viewModels {
        ProductDependencyInjectorUtility.getCartViewModelFactory()
    }

    lateinit var database: DatabaseReference
    lateinit var list: ArrayList<CartItemModel>
    var orderCounter = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        setSupportActionBar(cart_topAppBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        database = Firebase.database.reference.child("orders")
        addCommonViews(cart_rootLayout, this)
        addAlertBox(
            isSingleButton = false,
            firstButtonText = "Cancel",
            secondButtonText = "Check Out"
        )
        assignOrderCounter()
        addAlertBoxListeners(firstButton = {
            this.toast("first button")
            hideAlertBox()
        }, secondButton = {
            this.toast("second button")
        })

        cart_goto_menu_button.setOnClickListener {
            finish()
        }

        cart_order_button.setOnClickListener {
            orderDialog()
        }
        getAndUpdateData()
        //TODO: create Orders references in firebase and OrderModel


    }

    private fun getAndUpdateData() {
        cartViewModel.getCartList().observe(this, Observer {
            when (it) {
                is Status.Success -> {
                    hideProgressBar()
                    list = ArrayList(it.data!!)
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

    private fun orderDialog() {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy hh:mm")
        val currentDateTime = dateFormat.format(Date())
        Log.e("dateTimeDebug: ", currentDateTime)
        val dialogView = Dialog(this)
        dialogView.setContentView(R.layout.cart_checkout_custom_dialog)
        val positiveButton = dialogView.findViewById<Button>(R.id.checkoutDialogPositiveButton)
        val negativeButton = dialogView.findViewById<Button>(R.id.checkoutDialogNegativeButton)
        positiveButton.setOnClickListener {
            if (validateDialogForm(dialogView)) {

                val UID = FirebaseAuth.getInstance().currentUser!!.uid
                val name = dialogView.findViewById<EditText>(R.id.checkout_dialog_name)
                val phone = dialogView.findViewById<EditText>(R.id.checkout_dialog_phone)
                val address = dialogView.findViewById<EditText>(R.id.checkout_dialog_address)
                // orderId contains uid of the user, this helps making multiple orders, and retrieving those orders easily.
                val orderID = UID + saveAndGetOrderCounter().toString()
                val order = OrderModel(
                    orderId = orderID,
                    name = name.text.toString(),
                    phone = phone.text.toString(),
                    address = address.text.toString(),
                    products = list,
                    timestamp = currentDateTime
                )
                //TODO: only ONE order is storing/overwriting using UID reference, multiple orders should be stored
                database.child(orderID).setValue(order).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        this.toast("order successful")
                        dialogView.dismiss()
                    } else {
                        this.toast(task.exception?.message.toString())
                    }

                }
            }
        }
        dialogView.window?.decorView?.setBackgroundResource(android.R.color.transparent)
        dialogView.show()
        negativeButton.setOnClickListener {
            dialogView.dismiss()
        }
    }

    private fun validateDialogForm(dialog: Dialog): Boolean {
        val checkout_dialog_name = dialog.findViewById<EditText>(R.id.checkout_dialog_name)
        val checkout_dialog_phone = dialog.findViewById<EditText>(R.id.checkout_dialog_phone)
        val checkout_dialog_address = dialog.findViewById<EditText>(R.id.checkout_dialog_address)
        var result = true
        if (TextUtils.isEmpty(checkout_dialog_name.text.toString())) {
            checkout_dialog_name.error = "Please enter name"
            result = false
        } else {
            checkout_dialog_name.error = null
        }
        if (TextUtils.isEmpty(checkout_dialog_phone.text.toString())) {
            checkout_dialog_phone.error = "Please enter phone"
            result = false
        } else {
            checkout_dialog_phone.error = null
        }
        if (TextUtils.isEmpty(checkout_dialog_address.text.toString())) {
            checkout_dialog_address.error = "Please enter address"
            result = false
        } else {
            checkout_dialog_address.error = null
        }
        return result
    }

    private fun saveAndGetOrderCounter(): Int {
        val preferences: SharedPreferences =
            applicationContext.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()
        orderCounter += 1
        editor.putInt("orderCounter", orderCounter)
        editor.apply()

        return orderCounter
    }

    private fun assignOrderCounter() {
        val preferences: SharedPreferences =
            applicationContext.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        orderCounter = if (preferences.getInt("orderCounter", -1) > 0) {
            preferences.getInt("orderCounter", -1)
        } else {
            0
        }
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