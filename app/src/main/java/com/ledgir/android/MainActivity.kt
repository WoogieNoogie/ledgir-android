package com.ledgir.android

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ledgir.android.dashboard.DashboardActivity
import com.ledgir.android.model.Account
import com.ledgir.android.model.User
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val RC_SIGN_IN = 123
    }

    lateinit var auth: FirebaseAuth

    lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = FirebaseAuth.getInstance()
        database = FirebaseFirestore.getInstance()

        if (auth.currentUser != null ) {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                val user = auth.currentUser

                if (user != null) {
                    database.collection("users")
                        .whereEqualTo("externalId", user.uid)
                        .get()
                        .addOnCompleteListener {
                            if (it.isSuccessful && !it.result!!.isEmpty) {
                                startActivity(Intent(this, DashboardActivity::class.java))
                                finish()
                            } else {
                                val accountId = UUID.randomUUID().toString()
                                val userId = UUID.randomUUID().toString()

                                val firstName = if (user.displayName != null) user.displayName!!.split(" ")[0]
                                                else null
                                val lastName = if (user.displayName != null) user.displayName!!.split(" ")[1]
                                                else null

                                val newUser = User(
                                    accountId, null, user.email, user.uid,
                                    user.providerId, firstName, lastName, null
                                )
                                database.collection("users")
                                    .document(userId)
                                    .set(newUser)
                                    .addOnCompleteListener { userResult ->
                                        if (userResult.isSuccessful) {
                                            val account = Account(
                                                users = mapOf(Pair(user.uid, true))
                                            )
                                            database.collection("accounts")
                                                .document(accountId)
                                                .set(account)
                                                .addOnCompleteListener { accountResult ->
                                                    if (accountResult.isSuccessful) {
                                                        startActivity(Intent(this, DashboardActivity::class.java))
                                                        finish()
                                                    }
                                                }
                                        }
                                    }
                            }
                        }
                }
            } else {
                Toast.makeText(this, response!!.error!!.errorCode, Toast.LENGTH_LONG).show()
            }
        }

    }
}
