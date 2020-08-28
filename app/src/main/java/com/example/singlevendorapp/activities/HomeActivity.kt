package com.example.singlevendorapp.activities

import android.animation.ValueAnimator
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.example.singlevendorapp.R
import com.example.singlevendorapp.adapters.HomeTopPagerAdapter
import com.example.singlevendorapp.viewmodels.ProductViewModel
import kotlinx.android.synthetic.main.activity_home.*
import java.util.*
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.example.singlevendorapp.FactoryClasses.ProductDependencyInjectorUtility
import com.example.singlevendorapp.MyBaseClass
import com.example.singlevendorapp.Status
import com.example.singlevendorapp.fragments.BurgerFragment
import com.example.singlevendorapp.fragments.DrinksFragment
import com.example.singlevendorapp.fragments.PizzaFragment
import com.example.singlevendorapp.fragments.RollsFragment
import com.example.singlevendorapp.models.ProductModel
import com.example.singlevendorapp.mycustomviews.Type1ListenerCallback
import com.example.singlevendorapp.toast
import com.google.android.material.chip.Chip
import kotlin.collections.ArrayList

class HomeActivity : MyBaseClass() {

    private val productViewModel: ProductViewModel by viewModels {
        ProductDependencyInjectorUtility.getProductViewModelFactory()
    }

    private var currentPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        addCommonViews(home_rootLayout, this)
        populateData()
        handleFragmentsChips()

    }

    private fun handleFragmentsChips() {
        supportFragmentManager.beginTransaction()
            .add(R.id.home_fragment_container, PizzaFragment.newInstance()).commitNow()
        pizzaChip.elevation = 20f

        home_chipGroup.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.pizzaChip -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_fragment_container, PizzaFragment.newInstance())
                        .commitNow()
                    animateChip(0)

                }
                R.id.burgerChip -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_fragment_container, BurgerFragment.newInstance())
                        .commitNow()
                    animateChip(1)
                }
                R.id.rollsChip -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_fragment_container, RollsFragment.newInstance())
                        .commitNow()
                    animateChip(2)
                }
                R.id.drinksChip -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.home_fragment_container, DrinksFragment.newInstance())
                        .commitNow()

                    animateChip(3)
                }
            }
        }
    }

    private fun populateData() {
        productViewModel.getProducts().observe(this, Observer {
            when (it) {
                is Status.Success -> {
                    hideLoadingView()
                    Log.e("Status check", "Success!!")
                    val dataSnapshot = it.data
                    val list = ArrayList<ProductModel>()
                    for (product in dataSnapshot!!.children) {
                        val pro = product.getValue(ProductModel::class.java)!!
                        list.add(pro)
                    }
                    val adapter = HomeTopPagerAdapter(this, list)
                    home_top_viewpager.adapter = adapter
                    home_tab_indicator.setupWithViewPager(home_top_viewpager)
                    autoScrollViewPager(list)
                }
                is Status.Loading -> {
                    //it should show errorDialog if internet is not connected
                    //because if internet is not connected it will keep showing the loadingView
//                    showLoadingView()
                }
                is Status.Error -> {
                    //showError
                    hideLoadingView()
                    showAlertBox("Error Occurred!")
                    myAlertBox!!.setOnClickListener(object : Type1ListenerCallback {
                        override fun buttonClickListener() {
                            hideDialog()
                        }
                    })
                    this.toast(it.message.toString())
                    Log.e("data null check error", (it.data == null).toString())
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
            runOnUiThread {
                if (currentPosition == list.size) {
                    currentPosition = 0
                }
                home_top_viewpager.currentItem = currentPosition++
            }
        }
        Timer().schedule(object : TimerTask() {
            override fun run() {
                handler.post(runnable)
            }
        }, 500, 5000)
    }
}