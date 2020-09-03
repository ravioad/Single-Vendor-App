package com.example.singlevendorapp.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.singlevendorapp.FactoryClasses.ProductDependencyInjectorUtility
import com.example.singlevendorapp.R
import com.example.singlevendorapp.Status
import com.example.singlevendorapp.adapters.PizzaFragmentAdapter
import com.example.singlevendorapp.models.ProductModel
import com.example.singlevendorapp.viewmodels.ProductViewModel
import com.google.firebase.database.FirebaseDatabase

class PizzaFragment : Fragment() {
    companion object {
        fun newInstance() = PizzaFragment()
    }

    private val productViewModel: ProductViewModel by viewModels {
        ProductDependencyInjectorUtility.getProductViewModelFactory(
            FirebaseDatabase.getInstance().reference.child(
                "products"
            ).child("pizzas")
        )
    }

    var thisContext: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        thisContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val screenLayout: View = inflater.inflate(R.layout.fragment_pizza, container, false)
        val recyclerView: RecyclerView = screenLayout.findViewById(R.id.pizza_recyclerview)
        productViewModel.getProducts().observe(context as AppCompatActivity, Observer {
            when (it) {
                is Status.Success -> {
//                    hideLoadingView()
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
//                    val spacing: Int = resources.getDimensionPixelSize(R.dimen.spacing)
//                    recyclerView.addItemDecoration(RecyclerItemSpacing(spacing))
                }
                is Status.Loading -> {
                    //it should show errorDialog if internet is not connected
                    //because if internet is not connected it will keep showing the loadingView
//                    showLoadingView()
                }
                is Status.Error -> {
                    //showError
//                    hideLoadingView()
//                    showAlertBox("Error Occurred!")
//                    myAlertBox!!.setOnClickListener(object : Type1ListenerCallback {
//                        override fun buttonClickListener() {
//                            hideDialog()
//                        }
//                }
//                    )
//                    this.toast(it.message.toString())
//                Log.e("data null check error", (it.data == null).toString())
                }
            }
        })
        return screenLayout
    }

}