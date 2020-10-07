package com.example.singlevendorapp.adapters

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.singlevendorapp.R
import com.example.singlevendorapp.activities.OrderActivity
import com.example.singlevendorapp.models.OrderModel

class PendingOrdersFragmentAdapter(val context: Context, private val list: ArrayList<OrderModel>) : RecyclerView.Adapter<PendingOrdersFragmentAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PendingOrdersFragmentAdapter.MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyViewHolder(context, inflater, parent)
    }

    override fun onBindViewHolder(holder: PendingOrdersFragmentAdapter.MyViewHolder, position: Int) {
        holder.bind(list[position])
        holder.itemView.setOnClickListener {
            val intent = Intent(context, OrderActivity::class.java).apply {
                putExtra("orderData", list[position])
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }


    inner class MyViewHolder(val context: Context, inflater: LayoutInflater, parent: ViewGroup) :
        RecyclerView.ViewHolder(
            inflater.inflate(R.layout.pending_orders_list_item, parent, false)
        ), View.OnClickListener {

        var name: TextView? = null
        var price: TextView? = null
        var quantity: TextView? = null
        var timestamp: TextView? = null

        //var order: OrderModel? = null

        init {
            name = itemView.findViewById(R.id.pendingOrder_title)
            timestamp = itemView.findViewById(R.id.pendingOrder_timestamp)
            price = itemView.findViewById(R.id.pendingOrder_totalPrice)
            quantity = itemView.findViewById(R.id.pendingOrder_quantity)

//            remove = itemView.findViewById(R.id.favorite_remove)
//            remove?.setOnClickListener {
//                layoutPosition.also { position ->
//                    runBlocking {
//                        removeProductFromDatabase(list[position])
//                    }
//                    if (position == list.size - 1 && list.size == 1) {
//                        (context as Activity).findViewById<TextView>(R.id.no_favorites_added).visibility =
//                            View.VISIBLE
//                        (context).findViewById<Button>(R.id.favorite_goto_menu_button).visibility =
//                            View.VISIBLE
//                    }
//                    list.removeAt(position)
//                    notifyItemRemoved(position)
//                    Handler().postDelayed({
//                        notifyDataSetChanged()
//                    }, 500)
//                }
//
//            }

        }

        fun bind(order: OrderModel) {
//            this.order = order

            name?.text = order.name
            price?.text = getTotalPrice(order).toString()
            quantity?.text = getTotalCount(order).toString()
            timestamp?.text = getDateTime(order.timestamp)
        }

        private fun getDateTime(timestamp: String): String {
            val date = timestamp.substring(0, 11)
            val time = timestamp.substring(12)
            Log.e("dateTimeDebeg: ", "$time\n$date")
            return "$time\n$date"

        }

        private fun getTotalPrice(order: OrderModel): Int {
            val list = order.products!!
            var totalPrice = 0
            list.forEach {
                totalPrice += it.totalPrice
            }
            return totalPrice
        }

        private fun getTotalCount(order: OrderModel): Int {
            val list = order.products!!
            var count = 0
            list.forEach {
                count += it.quantity
            }
            return count
        }

        override fun onClick(v: View?) {
            when (v?.id) {
                R.id.favorite_remove -> {

                }
            }
        }

    }
}