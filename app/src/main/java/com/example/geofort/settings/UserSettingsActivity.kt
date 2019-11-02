package com.example.geofort.settings

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.geofort.main.MainApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user_settings.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import com.example.geofort.R
import com.google.firebase.auth.FirebaseUser
import org.jetbrains.anko.toast



class UserSettingsActivity: AppCompatActivity(), AnkoLogger {
    lateinit var app: MainApp
    lateinit var auth: FirebaseAuth

     lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        buttonOne.setOnClickListener{
            toast("hello")
        }


    }

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        user = currentUser!!
        if(user != null) {

            info("james before username")
            val username = user.displayName

            info("james username is $username")

            // val email =  auth.currentUser!!.email
            val button = findViewById<Button>(R.id.buttonOne)
            info("james"+auth.currentUser!!.email.toString())

            // userEmail.setText(email)
        }
    }
}