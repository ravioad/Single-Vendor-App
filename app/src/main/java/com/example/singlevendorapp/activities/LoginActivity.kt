package com.example.singlevendorapp.activities

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import com.example.singlevendorapp.MyBaseClass
import com.example.singlevendorapp.R
import com.example.singlevendorapp.toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : MyBaseClass(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        addCommonViews(login_rootLayout, this)
        login_button.setOnClickListener(this)
        signup_text.setOnClickListener(this)
        auth = FirebaseAuth.getInstance()
        loadLoginDetailsIfAvailable()
        remember_me.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                if (validateForm()) {
                    saveLoginDetails()
                } else {
                    remember_me.isChecked = false
                    this.toast("Enter Email/Password")
                }
            } else {
                removeLoginDetails()
            }
        }

    }

    private fun loadLoginDetailsIfAvailable() {
        val preferences: SharedPreferences =
            applicationContext.getSharedPreferences(",myPreferences", Context.MODE_PRIVATE)
        if (!(preferences.getString("email", "").equals(""))) {
            remember_me.isChecked = true
            login_email_input.setText(preferences.getString("email", ""))
            login_password_input.setText(preferences.getString("password", ""))
        } else {
            remember_me.isChecked = false
            return
        }
    }

    private fun saveLoginDetails() {
        val preferences: SharedPreferences =
            applicationContext.getSharedPreferences(",myPreferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.putString("email", login_email_input.text.toString())
        editor.putString("password", login_password_input.text.toString())
        editor.apply()
    }

    private fun removeLoginDetails() {
        val preferences: SharedPreferences =
            applicationContext.getSharedPreferences(",myPreferences", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = preferences.edit()
        editor.remove("email")
        editor.remove("password")
        editor.apply()
    }

    override fun onStart() {
        super.onStart()
        auth.currentUser?.let {
            updateUI(it)
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        currentUser?.let {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java).apply {
                putExtra("TheUser", currentUser)
            }
            startActivity(intent)
            finish()
        }
    }

    private fun signIn(email: String, password: String) {
        if (!validateForm()) {
            return
        }
        showProgressBar(true)
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                updateUI(auth.currentUser)
            } else {
                Log.e("signInWithEmail: Failed", task.exception?.message.toString())
                this.toast("Authentication Failed")
                updateUI(null)
            }
            hideProgressBar()
        }

    }

    private fun validateForm(): Boolean {
        var result = true

        if (login_email_input.text.toString() == "") {
            login_email_input.error = "Email Not Found!"
            result = false
        } else {
            login_email_input.error = null
        }
        if (login_password_input.text.toString() == "") {
            login_password_input.error = "Password Not Found!"
            result = false
        } else {
            login_password_input.error = null
        }
        return result

    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.login_button -> {
                val email = login_email_input.text.toString()
                val password = login_password_input.text.toString()
                signIn(email, password)
            }
            R.id.signup_text -> {
                startActivity(Intent(this@LoginActivity, SingupActivity::class.java))
            }
        }
    }

}