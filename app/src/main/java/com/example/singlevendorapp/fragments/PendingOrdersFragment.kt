package com.example.singlevendorapp.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singlevendorapp.FactoryClasses.ProductDependencyInjectorUtility
import com.example.singlevendorapp.R
import com.example.singlevendorapp.Status
import com.example.singlevendorapp.adapters.PendingOrdersFragmentAdapter
import com.example.singlevendorapp.adapters.PizzaFragmentAdapter
import com.example.singlevendorapp.models.OrderModel
import com.example.singlevendorapp.viewmodels.ProductViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class PendingOrdersFragment : Fragment() {
    private val productViewModel: ProductViewModel by viewModels {
        ProductDependencyInjectorUtility.getProductViewModelFactory(
            FirebaseDatabase.getInstance().reference.child(
                "orders"
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //TODO: get Data from firebase to show pending orders here
        val screenLayout: View =
            inflater.inflate(R.layout.fragment_pending_orders, container, false)

        val recyclerView: RecyclerView = screenLayout.findViewById(R.id.pendingOrders_recyclerview)
        productViewModel.getProducts().observe(context as AppCompatActivity, Observer {
            when (it) {
                is Status.Success -> {
//                    shimmer?.visibility = View.GONE
//                    shimmer?.stopShimmer()
                    Log.e("pendingOrdersDebug: ", "Success!!")
                    val dataSnapshot = it.data
                    val list = ArrayList<OrderModel>()
                    for (product in dataSnapshot!!.children) {
                        val order = product.getValue(OrderModel::class.java)!!
                        if (order.orderId.contains(FirebaseAuth.getInstance().currentUser!!.uid)) {
                            list.add(order)
                        }
                    }
                    recyclerView.apply {
                        layoutManager = LinearLayoutManager(context)
                        adapter = PendingOrdersFragmentAdapter(context, list)
                    }
                }
                is Status.Loading -> {
                    //it should show errorDialog if internet is not connected
                    //because if internet is not connected it will keep showing the loadingView
//                    shimmer?.visibility = View.VISIBLE
//                    shimmer?.startShimmer()

                }
                is Status.Error -> {
//                    shimmer?.visibility = View.GONE
//                    shimmer?.stopShimmer()
//                    thisContext!!.toast(it.message.toString())
                }
            }
        })

        return screenLayout
    }

}