package com.example.singlevendorapp.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.singlevendorapp.MyApplication
import com.example.singlevendorapp.R
import com.example.singlevendorapp.models.ProductModel
import com.example.singlevendorapp.mycustomviews.BlurView
import com.example.singlevendorapp.mycustomviews.SlideView
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.product_slideview_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ProductActivity : AppCompatActivity() {

    private var slideView: SlideView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        val toolbar: MaterialToolbar? = findViewById(R.id.product_topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val receivedIntent = intent
        if (receivedIntent != null) {
            val product: ProductModel =
                receivedIntent.getSerializableExtra("product") as ProductModel
            productTitle.text = product.name
            val price = "Rs.\n${product.price}"
            productPrice.text = price
            Glide.with(this).load(product.image).into(productImage)
            runBlocking {
                if (checkIfAvailable(product.image)) {
                    Glide.with(this@ProductActivity).load(R.drawable.favorited).fitCenter()
                        .into(product_favorite)
                } else {
                    Glide.with(this@ProductActivity).load(R.drawable.favorited_border).fitCenter()
                        .into(product_favorite)
                }
            }
        }
        val blurView = BlurView(this)
        product_rootLayout.post {
            val myRecycler: View = View.inflate(this, R.layout.product_slideview_child, null)
            slideView = SlideView(this, 40, myRecycler, blurView)
            product_rootLayout.addView(slideView)
            
//            handleRl.setOnClickListener {
//                if (isOpened) {
//                    SlideDown()
//                } else {
//                    slideUp()
//                }
//            }
        }

        productTitle.setOnClickListener {
            slideView?.slideUp()
        }

    }

    private suspend fun checkIfAvailable(image: String): Boolean =
        withContext(Dispatchers.IO) {
            MyApplication.db.productModelDao().isAvailable(image)
        }

}