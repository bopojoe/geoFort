package com.example.geofort.login



import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.geofort.R
import com.example.geofort.activities.GeofortListActivity
import com.example.geofort.main.MainApp
import com.example.geofort.models.FirebaseStore
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast

class LoginActivity : AppCompatActivity(), AnkoLogger {

    private lateinit var auth: FirebaseAuth
    var fireStore: FirebaseStore? = null
    lateinit var app: MainApp





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        auth = FirebaseAuth.getInstance()

        // get reference to all views
        var et_login_email = findViewById(R.id.login_email) as EditText
        var et_password = findViewById(R.id.et_password) as EditText
        var btn_reset = findViewById(R.id.btn_reset) as Button
        var btn_login = findViewById(R.id.btn_login) as Button


        btn_reset.setOnClickListener {
            // clearing user_name and password edit text views on reset button click
            et_login_email.setText("")
            et_password.setText("")
        }

        // set on-click listener
        btn_login.setOnClickListener {
            val email = login_email.text.toString()
            val password = et_password.text.toString()
            signIn(email, password)
            
        }

        btn_sign_up.setOnClickListener {
            val intentRegister = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intentRegister)

        }
    }


    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        //updateUI(currentUser)
        if (currentUser != null) {
            val user = currentUser.displayName
            toast("Welcome back $user,\nPlease enter your Password")
            login_email.setText(currentUser.email)

        } else {
            toast("Welcome\n Please Login or Register")
        }
    }


    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {

                    val username = auth.currentUser!!.displayName

                    app = application as MainApp
                    toast("Welcome $username")
                    if (username != null) {
                        app.currentuser = username
                        fireStore = app.geoforts as FirebaseStore
                        info("james before firestore check")
                            if (fireStore != null) {
                                info("james firestore check")
                                fireStore!!.fetchGeoforts {
                                    info("james fetch firestore check")
                                    val intent =
                                        Intent(this@LoginActivity, GeofortListActivity::class.java)
                                    startActivity(intent)

                                }
                            } else {
                                val intent =
                                    Intent(this@LoginActivity, GeofortListActivity::class.java)
                                startActivity(intent)
                            }
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


    override fun onBackPressed() {
        toast("Please log back in to view previous page")

    }



}
