package com.example.singlevendorapp.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.singlevendorapp.R
import com.example.singlevendorapp.models.ProductModel

class PizzaFragmentAdapter(val context: Context, private val list: ArrayList<ProductModel>) :
    RecyclerView.Adapter<PizzaFragmentAdapter.MyViewHolder>() {

    class MyViewHolder(val context: Context, inflator: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflator.inflate(R.layout.pizza_fragment_list_item, parent, false)
        ), View.OnClickListener {

        var name: TextView? = null
        var Image: ImageView? = null
        var price: TextView? = null
        var addFav: ImageView? = null

        init {
            name = itemView.findViewById(R.id.title)
            Image = itemView.findViewById(R.id.image)
            price = itemView.findViewById(R.id.price)
            addFav = itemView.findViewById(R.id.addFavorite)
            addFav?.setOnClickListener(this)
        }

        fun bind(product: ProductModel) {
            name?.text = product.name
            Glide.with(context).load(product.image).into(Image!!)
            val prices = "Rs. ${product.price}"
            price?.text = prices
        }

        override fun onClick(v: View?) {
            addFav?.setImageResource(R.drawable.favorited)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(context, inflater, parent)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
    }

}