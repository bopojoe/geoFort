package com.example.geofort.login

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.geofort.R
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast
import com.google.firebase.auth.UserProfileChangeRequest
import org.jetbrains.anko.info


class RegisterActivity: AppCompatActivity(), AnkoLogger {
    private lateinit var auth: FirebaseAuth



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        auth = FirebaseAuth.getInstance()

        // get reference to all views
        var et_user_name = findViewById(R.id.et_user_name) as EditText
        var et_email = findViewById(R.id.et_email) as EditText
        var et_password = findViewById(R.id.et_password) as EditText
        var btn_register = findViewById(R.id.btn_register) as Button


        // set on-click listener
        btn_register.setOnClickListener {
            val register_email = et_email.text.toString()
            val password = et_password.text.toString()
            val userName = et_user_name.text.toString()
            createAccount(register_email, password, userName)



        }

    }

    private fun createAccount(email: String, password: String, userName: String) {
        info("userName is $userName")

        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->


                if (task.isSuccessful) {
                    val user = auth.currentUser!!
                    val profileUpdates = UserProfileChangeRequest.Builder()
                        .setDisplayName(userName).build()
                    user.updateProfile(profileUpdates)
                    auth.signOut()
                    toast("success $user")

                    val intentRegister = Intent(this@RegisterActivity, LoginActivity::class.java)
                    startActivity(intentRegister)

                } else {

                    toast("Account Creation Failure")
                }

                // [START_EXCLUDE]
                // hideProgressDialog()
                // [END_EXCLUDE]
            }
        // [END create_user_with_email]
    }
}