package com.example.singlevendorapp.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singlevendorapp.FactoryClasses.ProductDependencyInjectorUtility
import com.example.singlevendorapp.MyBaseFragment
import com.example.singlevendorapp.R
import com.example.singlevendorapp.Status
import com.example.singlevendorapp.adapters.PizzaFragmentAdapter
import com.example.singlevendorapp.models.ProductModel
import com.example.singlevendorapp.viewmodels.ProductViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.firebase.database.FirebaseDatabase

class DrinksFragment : MyBaseFragment() {
    companion object {
        fun newInstance() = DrinksFragment()
    }

    private val productViewModel: ProductViewModel by viewModels {
        ProductDependencyInjectorUtility.getProductViewModelFactory(
            FirebaseDatabase.getInstance().reference.child(
                "products"
            ).child("drinks")
        )
    }
    private var shimmer: ShimmerFrameLayout? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val screenLayout: View = inflater.inflate(R.layout.fragment_drinks, container, false)
        val recyclerView: RecyclerView = screenLayout.findViewById(R.id.drinks_recyclerview)
        val rootLayout = screenLayout.findViewById<RelativeLayout>(R.id.drinks_fragment_rootLayout)
        addCommonViews(rootLayout, context as Activity)
        shimmer = screenLayout.findViewById(R.id.drinks_shimmer_container)
        productViewModel.getProducts().observe(context as AppCompatActivity, Observer {
            when (it) {
                is Status.Success -> {
                    shimmer?.visibility = View.GONE
                    shimmer?.stopShimmer()
                    Log.e("Status check", "Success!!")
                    val dataSnapshot = it.data
                    val list = ArrayList<ProductModel>()
                    for (product in dataSnapshot!!.children) {
                        val pro = product.getValue(ProductModel::class.java)!!
                        list.add(pro)
                    }
                    recyclerView.apply {
                        layoutManager = GridLayoutManager(context, 2)
                        adapter = PizzaFragmentAdapter(context, list)
                    }
                }
                is Status.Loading -> {
                    //it should show errorDialog if internet is not connected
                    //because if internet is not connected it will keep showing the loadingView
                    shimmer?.visibility = View.VISIBLE
                    shimmer?.startShimmer()
                }
                is Status.Error -> {
                    //showError
                    shimmer?.visibility = View.GONE
                    shimmer?.stopShimmer()
                }
            }
        })
        return screenLayout
    }

    override fun onResume() {
        super.onResume()
        shimmer?.startShimmer()
    }

    override fun onPause() {
        shimmer?.stopShimmer()
        super.onPause()
    }

}