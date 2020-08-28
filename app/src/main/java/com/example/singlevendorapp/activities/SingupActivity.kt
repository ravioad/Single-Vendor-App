package com.example.singlevendorapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import com.example.singlevendorapp.MainActivity
import com.example.singlevendorapp.MyBaseClass
import com.example.singlevendorapp.R
import com.example.singlevendorapp.models.User
import com.example.singlevendorapp.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_singup.*

class SingupActivity : MyBaseClass(), View.OnClickListener {
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_singup)
        addCommonViews(signUp_rootLayout, this)
        signup_button.setOnClickListener(this)
        database = Firebase.database.reference
        auth = FirebaseAuth.getInstance()
    }

    private fun signUp() {
        if (!validateForm()) {
            return
        }
        showProgressBar(true)
        val name = signup_name_input.text.toString()
        val email = signup_email_input.text.toString()
        val password = signup_password_input.text.toString()
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            hideProgressBar()
            if (task.isSuccessful) {
                addUser(name, email, task.result?.user?.uid.toString())
                updateUI(task.result?.user)
            } else {
                Log.e("AddUser:Failure", task.exception.toString())
                this.toast("Registration failed")
            }
        }
    }

    private fun addUser(name: String, email: String, uid: String) {
        val user = User(name, email, uid)
        database.child("users").child(uid).setValue(user).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                this.toast("Registration Successful")
            } else {
                Log.e("AddUser:Failure", task.exception.toString())
                this.toast("Registration failed")
            }
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        currentUser.let {
            val intent = Intent(this, HomeActivity::class.java).apply {
                putExtra("TheUser", currentUser)
            }
            startActivity(intent)
            finish()
        }
    }

    private fun validateForm(): Boolean {
        var result = true
        if (TextUtils.isEmpty(signup_name_input.text.toString())) {
            signup_name_input.error = "Please enter name"
            result = false
        } else {
            signup_name_input.error = null
        }
        if (TextUtils.isEmpty(signup_email_input.text.toString())) {
            signup_email_input.error = "Please enter email"
            result = false
        } else {
            signup_email_input.error = null
        }
        if (TextUtils.isEmpty(signup_password_input.text.toString())) {
            signup_password_input.error = "Please enter password"
            result = false
        } else {
            signup_password_input.error = null
        }
        return result
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.signup_button -> {
                signUp()
            }
        }
    }

}