package com.example.singlevendorapp.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.singlevendorapp.MyApplication
import com.example.singlevendorapp.R
import com.example.singlevendorapp.adapters.OrderExpandableListAdapter
import com.example.singlevendorapp.models.CartItemModel
import com.example.singlevendorapp.models.OrderModel
import kotlinx.android.synthetic.main.activity_order.*
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class OrderActivity : AppCompatActivity() {
    lateinit var hashMap: HashMap<String, ArrayList<CartItemModel>>
    lateinit var headerList: ArrayList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order)
        setSupportActionBar(order_topAppBar)
        hashMap = HashMap()
        val intent: Intent? = intent
        getData(intent)
        val adapter = OrderExpandableListAdapter(this, headerList, hashMap)
        order_listView.setAdapter(adapter)
        order_listView.setOnChildClickListener(object : ExpandableListView.OnChildClickListener {
            override fun onChildClick(
                parent: ExpandableListView?,
                v: View?,
                groupPosition: Int,
                childPosition: Int,
                id: Long
            ): Boolean {
                showProduct(hashMap[headerList[0]]!![childPosition])
                return true
            }

        })

    }

    private fun getData(intent: Intent?) {
        if (intent != null) {
            val order = intent.getSerializableExtra("orderData") as OrderModel?
            Log.e("orderDebug: ", order!!.name)
            order_title.text = order.name
            order_phone.text = order.phone
            order_date.text = getDate(order.timestamp)
            order_address.text = getAddress(order.address)
            hashMap[order.name] = order.products!!
            headerList = ArrayList(hashMap.keys)
        }
    }

    private fun getAddress(address: String): String {
        order_address.setOnClickListener {
            Log.e("addressDebug: ", address)
            showAddress(address)
        }

        val upperBound = if (address.length > 10) {
            10
        } else {
            address.length
        }
        return "${address.substring(0, upperBound)}..."
    }

    private fun getDate(timestamp: String): String {
        val date = timestamp.substring(0, 10)
        val time = timestamp.substring(11)
        return "$time [$date]"
    }

    private fun showProduct(product: CartItemModel) {
        val dialogView = Dialog(this)
        dialogView.setContentView(R.layout.order_show_product_dialog_layout)
        val image = dialogView.findViewById<ImageView>(R.id.orderDialog_product_image)
        val title = dialogView.findViewById<TextView>(R.id.orderDialog_product_title)
        val price = dialogView.findViewById<TextView>(R.id.orderDialog_product_price)
        val favorite = dialogView.findViewById<ImageView>(R.id.orderDialog_product_favorite)
        Glide.with(this).load(product.image).into(image)
        runBlocking {
            if (checkIfAvailable(product.image)) {
                Glide.with(this@OrderActivity).load(R.drawable.favorited).fitCenter()
                    .into(favorite)

            } else {
                Glide.with(this@OrderActivity).load(R.drawable.favorited_border).fitCenter()
                    .into(favorite)

            }
        } // Assigning Favorite Icon
        title.text = product.name
        price.text = "Rs. ${product.unitPrice}"
        dialogView.window?.decorView?.setBackgroundResource(android.R.color.transparent)
        dialogView.show()
    }

    private fun showAddress(addr: String) {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        val viewGroup: ViewGroup = findViewById(android.R.id.content)
        val dialogView: View =
            LayoutInflater.from(this)
                .inflate(R.layout.order_show_address_dialog_layout, viewGroup, false)
        builder.setView(dialogView).setPositiveButton("Ok") { dialog, _ ->
            dialog.dismiss()
        }
        val address = dialogView.findViewById<TextView>(R.id.order_dialog_address)
        address.text = addr
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private suspend fun checkIfAvailable(image: String): Boolean =
        withContext(Dispatchers.IO) {
            MyApplication.db.productModelDao().isAvailable(image)
        }
}
