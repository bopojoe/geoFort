package com.example.geofort.login

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.geofort.R
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity(){

    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_login)


            auth = FirebaseAuth.getInstance()

            // get reference to all views
            var et_user_name = findViewById(R.id.et_user_name) as EditText
            var et_password = findViewById(R.id.et_password) as EditText
            var btn_reset = findViewById(R.id.btn_reset) as Button
            var btn_submit = findViewById(R.id.btn_submit) as Button


            btn_reset.setOnClickListener {
                // clearing user_name and password edit text views on reset button click
                et_user_name.setText("")
                et_password.setText("")
            }

            // set on-click listener
            btn_submit.setOnClickListener {
                val user_name = et_user_name.text.toString()
                val password = et_password.text.toString()
                signIn(user_name, password)

                //Toast.makeText(this@MainActivity, user_name, Toast.LENGTH_LONG).show()

                // your code to validate the user_name and password combination
                // and verify the same

            }

            btn_sign_up.setOnClickListener{
                val user_name = et_user_name.text.toString()
                val password = et_password.text.toString()
                createAccount(user_name, password)

        }
        }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        //updateUI(currentUser)
    }


    private fun createAccount(email: String, password: String) {


        // [START create_user_with_email]
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    // Sign in success, update UI with the signed-in user's information
                //    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    toast("success $user")
                  //  updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                  //  Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                    toast("phail")
                }

                // [START_EXCLUDE]
               // hideProgressDialog()
                // [END_EXCLUDE]
            }
        // [END create_user_with_email]
    }


    private fun signIn(email: String, password: String) {
      //  Log.d(TAG, "signIn:$email")
//        if (!validateForm()) {
//            return
//        }

    //    showProgressDialog()

        // [START sign_in_with_email]
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                 //   Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    toast("success $user")
                //    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                 //   Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                //    updateUI(null)
                }

                // [START_EXCLUDE]
                if (!task.isSuccessful) {
                    toast("unsuccessful")
                 //   status.setText(R.string.auth_failed)
                }
               // hideProgressDialog()
                // [END_EXCLUDE]
            }
        // [END sign_in_with_email]
    }


    }
