package com.example.geofort.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.geofort.main.MainApp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_user_settings.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import com.example.geofort.R
import com.example.geofort.activities.GeofortListActivity
import com.example.geofort.login.LoginActivity
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_change_email.*
import kotlinx.android.synthetic.main.activity_user_settings.settings_email
import kotlinx.android.synthetic.main.activity_user_settings.settings_password
import org.jetbrains.anko.toast
import java.lang.Thread.sleep


class UserSettingsActivity: AppCompatActivity(), AnkoLogger {
    lateinit var app: MainApp
    lateinit var auth: FirebaseAuth

     lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_settings)




    }

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        user = currentUser!!
        if(user != null) {
             settings_email.setText(user.email)
        }else{
            toast("no current user available\nPlease sign in")
            val intentLogin = Intent(this@UserSettingsActivity, LoginActivity::class.java)
            startActivity(intentLogin)
        }
        btn_change_pass.setOnClickListener{
            auth.signInWithEmailAndPassword(settings_email.text.toString(), settings_password.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        if(settings_new_password.text.toString() == settings_repeat_password.text.toString()){
                            user.updatePassword(settings_new_password.text.toString())
                            toast("password changed to "+settings_new_password.text.toString())
                            sleep(1000)
                            val listIntent = Intent(this@UserSettingsActivity, GeofortListActivity::class.java)
                            startActivity(listIntent)
                        }else{
                            toast("Passwords do not match!!")
                        }
                    } else {
                        Toast.makeText(
                            baseContext, "Authentication failed.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    if (!task.isSuccessful) {
                        toast("unsuccessful")
                    }

                }
        }

        btn_change_email.setOnClickListener{
            val changeEmailIntent = Intent(this@UserSettingsActivity, ChangeEmail::class.java)
            startActivity(changeEmailIntent)
        }



    }


}