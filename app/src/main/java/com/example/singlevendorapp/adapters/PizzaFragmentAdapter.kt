package com.example.singlevendorapp.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.singlevendorapp.MyApplication
import com.example.singlevendorapp.R
import com.example.singlevendorapp.activities.ProductActivity
import com.example.singlevendorapp.models.ProductModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class PizzaFragmentAdapter(val context: Context, private val list: ArrayList<ProductModel>) :
    RecyclerView.Adapter<PizzaFragmentAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(context, inflater, parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(context, ProductActivity::class.java).apply {
                putExtra("product", list[position])
            }
            context.startActivity(intent)
        }
    }

    class MyViewHolder(val context: Context, inflator: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflator.inflate(R.layout.pizza_fragment_list_item, parent, false)
        ) {

        var name: TextView? = null
        var Image: ImageView? = null
        var price: TextView? = null
        var addFav: ImageView? = null

        var product: ProductModel? = null

        init {
            name = itemView.findViewById(R.id.title)
            Image = itemView.findViewById(R.id.image)
            price = itemView.findViewById(R.id.price)
            addFav = itemView.findViewById(R.id.addFavorite)
            addFav?.setOnClickListener {
                if (it.contentDescription.equals("addedFalse")) {
                    runBlocking {
                        storeProductToDataBase(product!!)
                    }
                    addFav?.setImageResource(R.drawable.favorited)
                    it.contentDescription = "addedTrue"
                } else if (it.contentDescription.equals("addedTrue")) {
                    addFav?.setImageResource(R.drawable.favorited_border)
                    runBlocking {
                        removeProductFromDatabase(product!!)
                    }
                    it.contentDescription = "addedFalse"
                }
            }
        }

        fun bind(product: ProductModel) {
            this.product = product
            name?.text = product.name
            Glide.with(context).load(product.image).into(Image!!)
            val prices = "Rs. ${product.price}"
            price?.text = prices

            runBlocking {
                if (checkIfAvailable(product.image)) {
                    Glide.with(context).load(R.drawable.favorited).centerCrop().into(addFav!!)
                    addFav?.contentDescription = "addedTrue"
                } else {
                    Glide.with(context).load(R.drawable.favorited_border).centerCrop()
                        .into(addFav!!)
                    addFav?.contentDescription = "addedFalse"
                }

            }
        }

        private suspend fun checkIfAvailable(image: String): Boolean =
            withContext(Dispatchers.IO) {
                MyApplication.db.productModelDao().isAvailable(image)
            }

        private suspend fun storeProductToDataBase(product: ProductModel) =
            withContext(Dispatchers.IO) {
                MyApplication.db.productModelDao().insert(product)
            }

        private suspend fun removeProductFromDatabase(product: ProductModel) {
            withContext(Dispatchers.IO) {
                MyApplication.db.productModelDao().delete(product)
            }
        }
    }

}