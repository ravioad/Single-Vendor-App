package com.example.singlevendorapp.adapters

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.singlevendorapp.MyApplication
import com.example.singlevendorapp.R
import com.example.singlevendorapp.models.CartItemModel
import com.example.singlevendorapp.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class CartRecyclerViewAdapter(
    val context: Context,
    private val list: ArrayList<CartItemModel>
) : RecyclerView.Adapter<CartRecyclerViewAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(context, inflater, parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    private suspend fun getUpdatedItemCount(): Int {
        var count = 0
        withContext(Dispatchers.IO) {
            count = MyApplication.db.cartItemModelDao().getCount()
        }
        return count
    }

    private suspend fun getUpdatedTotal(): Int {
        var total = 0
        withContext(Dispatchers.IO) {
            val list = MyApplication.db.cartItemModelDao().getAllProducts()
            for (item in list) {
                total += item.totalPrice
            }
        }
        return total
    }

    private suspend fun removeProductFromDatabase(cartItem: CartItemModel) {
        withContext(Dispatchers.IO) {
            MyApplication.db.cartItemModelDao().delete(cartItem)
        }
    }

    inner class MyViewHolder(val context: Context, inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(R.layout.cart_list_item, parent, false)
        ) {

        var name: TextView? = null
        var Image: ImageView? = null
        var unitPrice: TextView? = null
        var totalPrice: TextView? = null
        var quantity: TextView? = null
        var remove: ImageButton? = null


        init {
            name = itemView.findViewById(R.id.cart_item_title)
            Image = itemView.findViewById(R.id.cart_item_image)
            unitPrice = itemView.findViewById(R.id.cart_item_unitPrice)
            totalPrice = itemView.findViewById(R.id.cart_item_totalPrice)
            quantity = itemView.findViewById(R.id.cart_item_quantity)
            remove = itemView.findViewById(R.id.cart_item_remove)
            val grandTotal = (context as Activity).findViewById<TextView>(R.id.cart_grand_total)
            val itemCount = (context).findViewById<TextView>(R.id.cart_total_items)

            remove?.setOnClickListener {
                layoutPosition.also { position ->
                    //TODO: last item removal causes app crash due to IndexOutOfBound Exception
                    runBlocking {
                        removeProductFromDatabase(list[position-1])
                        val updatedTotal = "Rs. ${getUpdatedTotal()}"
                        grandTotal.text = updatedTotal
                        val updatedCount = "${getUpdatedItemCount()} Item(s) in Cart"
                        itemCount.text = updatedCount
                    }
                    if (position == list.size && list.size == 1) {
                        (context).findViewById<Button>(R.id.cart_order_button).visibility =
                            View.GONE
                        (context).findViewById<TextView>(R.id.cart_empty).visibility = View.VISIBLE
                        (context).findViewById<Button>(R.id.cart_goto_menu_button).visibility =
                            View.VISIBLE
                    }
                    list.removeAt(position-1)
                    notifyItemRemoved(position-1)
                    Handler().postDelayed({
                        notifyDataSetChanged()
                    }, 500)
                }
            }
           
        }

        fun bind(product: CartItemModel) {
            name?.text = product.name
            Glide.with(context).load(product.image).into(Image!!)
            val unitPriceRs = "Rs. ${product.unitPrice}"
            unitPrice?.text = unitPriceRs
            val totalPriceRs = "Rs. ${product.totalPrice}"
            totalPrice?.text = totalPriceRs
            quantity?.text = product.quantity.toString()
        }

    }

}