package com.example.singlevendorapp.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.singlevendorapp.R
import com.example.singlevendorapp.toast
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MainActivity : AppCompatActivity() {

  private lateinit var appBarConfiguration: AppBarConfiguration

    private var navHeaderImage: ImageView? = null
    private var navHeaderTitle: TextView? = null
    private var navHeaderEmail: TextView? = null

    private val tomImage =
        "https://thumbor.forbes.com/thumbor/fit-in/416x416/filters%3Aformat%28jpg%29/https%3A%2F%2Fspecials-images.forbesimg.com%2Fimageserve%2F593b2e4b31358e03e55a0e8c%2F0x0.jpg%3Fbackground%3D000000%26cropX1%3D634%26cropX2%3D2468%26cropY1%3D39%26cropY2%3D1874"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: MaterialToolbar? = findViewById(R.id.main_topAppBar)
        setSupportActionBar(toolbar)
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val header: View = navView.getHeaderView(0)
        navHeaderImage = header.findViewById(R.id.main_drawer_image)
        navHeaderTitle = header.findViewById(R.id.main_drawer_title)
        navHeaderEmail = header.findViewById(R.id.main_drawer_email)


        val navController = findNavController(R.id.nav_host_fragment)
        appBarConfiguration =
            AppBarConfiguration(
                setOf(
                    R.id.nav_home,
                    R.id.nav_profile,
                    R.id.nav_pendingOrders,
                    R.id.nav_settings
                ), drawerLayout
            )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        populateDrawerHeader()
    }

    private fun populateDrawerHeader() {

        val reference = FirebaseDatabase.getInstance().reference.child("users")
            .child(FirebaseAuth.getInstance().currentUser!!.uid)
        reference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val children = snapshot.children
                val userDetails = ArrayList<String>()
                children.forEach {
                    userDetails.add(it.value.toString())
                }
                navHeaderTitle?.text = userDetails[2]
                navHeaderEmail?.text = userDetails[0]
            }

            override fun onCancelled(error: DatabaseError) {
                this@MainActivity.toast(error.message)
                Log.e("databaseErrorDebug: ", error.message.toString())
            }

        })
        Glide.with(this).load(tomImage).apply(RequestOptions().circleCrop())
            .into(navHeaderImage!!)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorites -> {
                startActivity(Intent(this, FavoritesActivity::class.java))
            }
            R.id.cart -> {
                startActivity(Intent(this, CartActivity::class.java))
            }
            R.id.logout -> {
                FirebaseAuth.getInstance().signOut()
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            }

            //TODO: try removing extra optionMenu items
        }
        return super.onOptionsItemSelected(item)
    }
}