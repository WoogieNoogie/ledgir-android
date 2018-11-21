package com.ledgir.android

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.ledgir.android.dashboard.DashboardActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val RC_SIGN_IN = 123
    }

    val user = FirebaseAuth.getInstance().currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (user != null ) {
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        } else {
            setContentView(R.layout.activity_main)

            landing_login.setOnClickListener {
                startActivityForResult(
                    AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(
                            arrayListOf(
                                AuthUI.IdpConfig.GoogleBuilder().build(),
                                AuthUI.IdpConfig.EmailBuilder().build()
                            )
                        )
                        .build(),
                    RC_SIGN_IN
                )
            }
        }
    }
}
