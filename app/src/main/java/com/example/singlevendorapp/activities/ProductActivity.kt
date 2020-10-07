package com.example.singlevendorapp.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import com.bumptech.glide.Glide
import com.example.singlevendorapp.MyApplication
import com.example.singlevendorapp.MyBaseClass
import com.example.singlevendorapp.R
import com.example.singlevendorapp.models.CartItemModel
import com.example.singlevendorapp.models.ProductModel
import com.example.singlevendorapp.mycustomviews.BlurView
import com.example.singlevendorapp.mycustomviews.SlideView
import com.example.singlevendorapp.toast
import com.google.android.material.appbar.MaterialToolbar
import kotlinx.android.synthetic.main.activity_product.*
import kotlinx.android.synthetic.main.product_slideview_layout.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ProductActivity : MyBaseClass() {

    private var slideView: SlideView? = null
    private var product: ProductModel? = null
    private var productCount = 1
    private var calculatedPrice = 0
    private var isBackPressDisable = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
        val toolbar: MaterialToolbar? = findViewById(R.id.product_topAppBar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        addCommonViews(product_rootLayout, this)
        addAlertBox(
            isSingleButton = false,
            firstButtonText = "Go To Cart",
            secondButtonText = "Go To Menu"
        )
        val blurView = BlurView(this)
        val receivedIntent = intent
        if (receivedIntent != null) {
            product = receivedIntent.getSerializableExtra("product") as ProductModel
            productTitle.text = product!!.name
            val price = "Rs.\n${product!!.price}"
            productPrice.text = price
            Glide.with(this).load(product!!.image).into(productImage)
            runBlocking {
                if (checkIfAvailable(product!!.image)) {
                    Glide.with(this@ProductActivity).load(R.drawable.favorited).fitCenter()
                        .into(product_favorite)

                } else {
                    Glide.with(this@ProductActivity).load(R.drawable.favorited_border).fitCenter()
                        .into(product_favorite)
                    product_favorite.setColorFilter(Color.parseColor("#FFFFFF"))
                }
            } // Assigning Favorite Icon

        }
        product_rootLayout.post {
            product_rootLayout.addView(blurView)
            slideView = SlideView(this, 40, blurView)
            product_rootLayout.addView(slideView)
            handleSlideViewClickListeners(slideView!!)
        }

        addAlertBoxListeners(firstButton = {
            navigateThroughDialog(Intent(this, CartActivity::class.java))
        }, secondButton = {
            navigateThroughDialog(Intent(this, MainActivity::class.java))
        })


    }

    private fun handleSlideViewClickListeners(slideView: SlideView) {
        val realPrice = product?.price
        product_slideview_title.text = product?.name
        calculatedPrice = (productCount * realPrice!!)
        product_total_price.text = "Rs. $calculatedPrice"
        product_quantity.text = productCount.toString()
        runBlocking {
            setCartItemsCount()
        }
        product_addProduct.setOnClickListener {
            productCount += 1
            product_quantity.text = productCount.toString()
            calculatedPrice = (productCount * realPrice)
            product_total_price.text = "Rs. $calculatedPrice"
        }
        product_removeProduct.setOnClickListener {
            if (productCount > 1) {
                productCount -= 1
                product_quantity.text = productCount.toString()
                calculatedPrice = (productCount * realPrice!!)
                product_total_price.text = "Rs. $calculatedPrice"
            }
        }
        product_add_cart_button.setOnClickListener {

            val cartItem = CartItemModel(
                name = product?.name!!,
                image = product?.image!!,
                unitPrice = realPrice,
                totalPrice = calculatedPrice,
                quantity = productCount
            )

            runBlocking {
                storeProductToDataBase(cartItem)
                this@ProductActivity.toast("Added to Cart")
            }
            slideView.slideDown()
            slideView.showSlideView(false)
            isBackPressDisable = true
            showAlertBox("Item Added to Cart")
        }
    }

    private suspend fun setCartItemsCount() {
        withContext(Dispatchers.IO) {
            val countString =
                "${MyApplication.db.cartItemModelDao().getCount()} Item(s) in Your Cart"
            product_total_cartItems.text = countString
        }
    }

    private suspend fun storeProductToDataBase(cartItem: CartItemModel) =
        withContext(Dispatchers.IO) {
            MyApplication.db.cartItemModelDao().insert(cartItem)
        }

    private suspend fun checkIfAvailable(image: String): Boolean =
        withContext(Dispatchers.IO) {
            MyApplication.db.productModelDao().isAvailable(image)
        }

    private fun navigateThroughDialog(intent: Intent) {
        isBackPressDisable = false
        startActivity(intent)
        finish()
    }

    override fun onBackPressed() {
        if (isBackPressDisable) {
            // Do Nothing on BackPress
        } else {
            super.onBackPressed()
        }
    }
}