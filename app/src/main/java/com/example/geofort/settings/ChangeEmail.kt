package com.example.geofort.settings

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.geofort.R
import com.example.geofort.login.LoginActivity
import com.example.geofort.main.MainApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_change_email.*
import kotlinx.android.synthetic.main.activity_user_settings.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.toast

class ChangeEmail : AppCompatActivity(), AnkoLogger {
    lateinit var app: MainApp
    lateinit var auth: FirebaseAuth

    lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_email)
    }

    override fun onStart() {
        super.onStart()
        auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        user = currentUser!!
        if (user != null) {
            settings_change_email_email.setText(user.email)
        } else {
            toast("no current user available\nPlease sign in")
            val intentLogin = Intent(this@ChangeEmail, LoginActivity::class.java)
            startActivity(intentLogin)
        }


        btn_save_email.setOnClickListener {
            auth.signInWithEmailAndPassword(
                settings_change_email_email.text.toString(),
                settings_change_email_password.text.toString()
            )
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        if (settings_new_email.text.toString() == settings_repeat_email.text.toString()) {
                            user.updateEmail(settings_new_email.text.toString())
                            toast("email changed to " + settings_new_email.text.toString())
                            Thread.sleep(1000)
                            toast("Please login with this new email for this to take effect")
                            Thread.sleep(1000)
                            val intentLogin =
                                Intent(this@ChangeEmail, LoginActivity::class.java)
                            startActivity(intentLogin)
                        } else {
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
    }
}