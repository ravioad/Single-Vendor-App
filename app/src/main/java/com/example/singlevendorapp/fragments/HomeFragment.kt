package com.example.singlevendorapp.fragments

import android.animation.ValueAnimator
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.viewpager.widget.ViewPager
import com.example.singlevendorapp.FactoryClasses.ProductDependencyInjectorUtility
import com.example.singlevendorapp.R
import com.example.singlevendorapp.Status
import com.example.singlevendorapp.adapters.HomeTopPagerAdapter
import com.example.singlevendorapp.models.ProductModel
import com.example.singlevendorapp.viewmodels.ProductViewModel
import com.facebook.shimmer.ShimmerFrameLayout
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private val productViewModel: ProductViewModel by viewModels {
        ProductDependencyInjectorUtility.getProductViewModelFactory(
            FirebaseDatabase.getInstance().reference.child(
                "products"
            ).child("deals")
        )
    }
    private var shimmer: ShimmerFrameLayout? = null
    private var viewpager: ViewPager? = null
    private var chipGroup: ChipGroup? = null
    private var selectedChip: Chip? = null
    private var currentPosition = 0
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val screenLayout: View = inflater.inflate(R.layout.activity_home, container, false)
        shimmer = screenLayout.findViewById(R.id.home__viewpager_shimmerContainer)
        chipGroup = screenLayout.findViewById(R.id.home_chipGroup)
        selectedChip = screenLayout.findViewById(R.id.burgerChip)
        viewpager = screenLayout.findViewById(R.id.home_top_viewpager)
        handleFragmentsChips()
        populateData()
        return screenLayout
    }

    private fun handleFragmentsChips() {

        childFragmentManager.beginTransaction()
            .add(R.id.home_fragment_container, BurgerFragment.newInstance()).commitNow()
        selectedChip?.elevation = 20f

        chipGroup?.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.pizzaChip -> {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.home_fragment_container, PizzaFragment.newInstance())
                        .commitNow()
                    animateChip(0)

                }
                R.id.burgerChip -> {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.home_fragment_container, BurgerFragment.newInstance())
                        .commitNow()
                    animateChip(1)
                }
                R.id.rollsChip -> {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.home_fragment_container, RollsFragment.newInstance())
                        .commitNow()
                    animateChip(2)
                }
                R.id.drinksChip -> {
                    childFragmentManager.beginTransaction()
                        .replace(R.id.home_fragment_container, DrinksFragment.newInstance())
                        .commitNow()

                    animateChip(3)
                }
            }
        }
    }

    private fun populateData() {
        productViewModel.getProducts().observe(viewLifecycleOwner, Observer {
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
                    val adapter = HomeTopPagerAdapter(context as Activity, list)
                    home_top_viewpager.adapter = adapter
                    home_tab_indicator.setupWithViewPager(home_top_viewpager)
                    autoScrollViewPager(list)
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
//                    showAlertBox("Error Occurred!")
//                    addAlertBoxListener(buttonListener = {
//                        hideDialog()
//                    })
//                    this.toast(it.message.toString())
                }
            }
        })

    }

    private fun animateChip(index: Int) {
        val list: ArrayList<Boolean> = arrayListOf(false, false, false, false)
        list.add(index, true)
        animateChipElevation(pizzaChip, list[0])
        animateChipElevation(burgerChip, list[1])
        animateChipElevation(rollsChip, list[2])
        animateChipElevation(drinksChip, list[3])
    }

    private fun animateChipElevation(chip: Chip, forward: Boolean) {
        if (!forward && chip.elevation == 0f) {
            return
        }

        val animator = if (forward) {
            ValueAnimator.ofFloat(0f, 20f)
        } else {
            ValueAnimator.ofFloat(20f, 0f)
        }
        animator!!.duration = 600
        animator.start()
        animator.addUpdateListener { animation ->
            val animatedValue = animation?.animatedValue as Float
            chip.elevation = animatedValue
        }
    }

    private fun autoScrollViewPager(list: ArrayList<ProductModel>) {
        val handler = Handler()
        val runnable = Runnable() {
            (context as? Activity)?.runOnUiThread {
                if (currentPosition == list.size) {
                    currentPosition = 0
                }
                viewpager?.currentItem = currentPosition++
            }
        }
        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.post(runnable)
            }
        }, 500, 5000)
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