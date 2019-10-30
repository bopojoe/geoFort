package com.example.geofort.activities

import android.os.Bundle
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.example.geofort.R
import androidx.annotation.NonNull
import android.content.Intent
import kotlinx.android.synthetic.main.activity_login.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import org.jetbrains.anko.toast


class LoginActivity : AppCompatActivity() , AnkoLogger {

    //oAuth2 thingy
    //95104700204-7q8kt4rtfv55g6u2jvpmtuloi7rmupqu.apps.googleusercontent.com

    private val RC_SIGN_IN = 9001

    private lateinit var mGoogleSignInClient: GoogleSignInClient


    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {

        super.onCreate(savedInstanceState, persistentState)


        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

        var account: GoogleSignInAccount? = GoogleSignIn.getLastSignedInAccount(this)

        sign_in_button.setOnClickListener() {
            signIn()
        }
    }



  override fun onStart() {
        super.onStart();

        // [START on_start_sign_in]
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        val account = GoogleSignIn.getLastSignedInAccount(this)!!
        updateUI(account)
        // [END on_start_sign_in]
    }

    // [START onActivityResult]
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);

        when (requestCode) {
            RC_SIGN_IN -> {
                handleSignInResult(GoogleSignIn.getSignedInAccountFromIntent(data))
            }


        }
    }
    // [END onActivityResult]

    // [START handleSignInResult]
   fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            var account: GoogleSignInAccount = completedTask.getResult(ApiException::class.java)!!

            // Signed in successfully, show authenticated UI.
            updateUI(account)
        } catch (e: ApiException ) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            val err = e.statusCode
            info("signInResult:failed code= $err");
            //updateUI(null);
        }
    }
    // [END handleSignInResult]

    // [START signIn]
    fun signIn() {
        val signInIntent: Intent = mGoogleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    // [END signIn]

    // [START signOut]
//    fun void signOut() {
//        mGoogleSignInClient.signOut()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // [START_EXCLUDE]
//                        updateUI(null);
//                        // [END_EXCLUDE]
//                    }
//                });
//    }
//    // [END signOut]

    // [START revokeAccess]
//   fun revokeAccess() {
//        mGoogleSignInClient.revokeAccess()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // [START_EXCLUDE]
//                        updateUI(null);
//                        // [END_EXCLUDE]
//                    }
//                });
//    }
//    // [END revokeAccess]

   fun updateUI(account: GoogleSignInAccount) {
       var name = account.getDisplayName()

       toast("hello $name")
//        if (account != null) {
//            mStatusTextView.setText(getString(R.string.signed_in_fmt, account.getDisplayName()));
//
//            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.VISIBLE);
//        } else {
//            mStatusTextView.setText(R.string.signed_out);
//
//            findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
//            findViewById(R.id.sign_out_and_disconnect).setVisibility(View.GONE);
//        }
    }

//    override fun  onClick(View v) {
//        switch (v.getId()) {
//            case R.id.sign_in_button:
//                signIn();
//                break;
//            case R.id.sign_out_button:
//                signOut();
//                break;
//            case R.id.disconnect_button:
//                revokeAccess();
//                break;
//        }
//    }
}