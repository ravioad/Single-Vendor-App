package com.example.singlevendorapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.singlevendorapp.MyApplication
import com.example.singlevendorapp.R
import com.example.singlevendorapp.models.ProductModel
import com.example.singlevendorapp.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class FavoritesRecyclerViewAdapter(
    val context: Context,
    private val list: ArrayList<ProductModel>
) : RecyclerView.Adapter<FavoritesRecyclerViewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(context, inflater, parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
        holder.remove?.setOnClickListener {
            runBlocking {
                removeProductFromDatabase(list[position])
            }
            list.removeAt(position)
            notifyDataSetChanged()
        }
    }

    private suspend fun removeProductFromDatabase(product: ProductModel) {
        withContext(Dispatchers.IO) {
            MyApplication.db.productModelDao().delete(product)
        }
    }

    class MyViewHolder(val context: Context, inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(R.layout.favorites_list_item, parent, false)
        ), View.OnClickListener {

        var name: TextView? = null
        var Image: ImageView? = null
        var price: TextView? = null
        var remove: ImageView? = null

        var product: ProductModel? = null

        init {
            name = itemView.findViewById(R.id.favorite_title)
            Image = itemView.findViewById(R.id.favorite_image)
            price = itemView.findViewById(R.id.favorite_price)
            remove = itemView.findViewById(R.id.favorite_remove)
//            remove?.setOnClickListener(this)

            itemView.setOnClickListener {
                context.toast("Item listener to be implemented")
            }

        }

        fun bind(product: ProductModel) {
            this.product = product
            name?.text = product.name
            Glide.with(context).load(product.image).into(Image!!)
            val priceRs = "Rs. ${product.price}"
            price?.text = priceRs
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.favorite_remove -> {

                }
            }
        }


    }

}