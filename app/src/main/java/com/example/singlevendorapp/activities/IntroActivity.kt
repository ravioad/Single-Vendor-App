package com.example.singlevendorapp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.singlevendorapp.MainActivity
import com.example.singlevendorapp.R
import com.example.singlevendorapp.adapters.IntroViewPagerAdapter
import com.example.singlevendorapp.models.IntroPageItem
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_intro.*

class IntroActivity : AppCompatActivity() {
    var currentPosition = 0
    lateinit var buttonAnimation: Animation
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        if(checkSeenState()){
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        buttonAnimation =
            AnimationUtils.loadAnimation(applicationContext, R.anim.get_started_button_animation)
        val desc =
            "Lorem ipsum dolor sit amet, eu affert ornatus eam, no sint augue necessitatibus quo. No sea vero malis disputando, meis offendit eos ea."

        val list: List<IntroPageItem> = listOf(
            IntroPageItem("Fresh Food", desc, R.drawable.img1),
            IntroPageItem("Fast Delivery", desc, R.drawable.img2),
            IntroPageItem("Easy Payment", desc, R.drawable.img3)
        )
        val adapter = IntroViewPagerAdapter(this, list)
        intro_viewpager.adapter = adapter
        tab_indicator.setupWithViewPager(intro_viewpager)

        intro_next_button.setOnClickListener {
            currentPosition = intro_viewpager.currentItem
            if (currentPosition < list.size) {
                currentPosition += 1
                intro_viewpager.currentItem = currentPosition
            }
            if (currentPosition == list.size - 1) {
                loadLastScreen()
            }
        }

        tab_indicator.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == list.size - 1) {
                    loadLastScreen()
                } else {
                    unloadLastScreen()
                }
            }
        })

        intro_getstarted_button.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            saveSeenState()
            finish()
        }
    }

    private fun loadLastScreen() {
        intro_next_button.visibility = View.INVISIBLE
        tab_indicator.visibility = View.INVISIBLE
        intro_getstarted_button.visibility = View.VISIBLE
        intro_getstarted_button.animation = buttonAnimation
    }

    fun unloadLastScreen() {
        intro_next_button.visibility = View.VISIBLE
        tab_indicator.visibility = View.VISIBLE
        intro_getstarted_button.visibility = View.INVISIBLE
//        intro_getstarted_button.animation.cancel()
    }

    fun saveSeenState() {
        val preferences: SharedPreferences =
            applicationContext.getSharedPreferences(",myPreferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putBoolean("seen", true)
        editor.apply()
    }

    fun checkSeenState(): Boolean {
        val preferences: SharedPreferences =
            applicationContext.getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        return preferences.getBoolean("seen", false)
    }
}