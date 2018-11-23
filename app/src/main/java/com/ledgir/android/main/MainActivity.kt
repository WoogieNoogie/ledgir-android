package com.ledgir.android.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.ledgir.android.R
import com.ledgir.android.dashboard.DashboardActivity
import com.ledgir.android.model.LedgirAccount
import com.ledgir.android.model.LedgirUser
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {
    companion object {
        const val RC_SIGN_IN = 123
    }

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        viewModel.firebaseValues.observe(this, Observer {
            if (it.auth.currentUser != null ) {
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
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                viewModel.firebaseValues.observe(this, Observer { firebase ->
                    val user = firebase.auth.currentUser

                    if (user != null) {
                        firebase.database.collection("users")
                            .whereEqualTo("externalId", user.uid)
                            .get()
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful && !task.result!!.isEmpty) {
                                    startActivity(Intent(this, DashboardActivity::class.java))
                                    finish()
                                } else {
                                    val accountId = UUID.randomUUID().toString()
                                    val userId = UUID.randomUUID().toString()

                                    val firstName = if (user.displayName != null) user.displayName!!.split(" ")[0]
                                    else null
                                    val lastName = if (user.displayName != null) user.displayName!!.split(" ")[1]
                                    else null

                                    val newUser = LedgirUser(
                                        accountId, listOf(firebase.identifier.id), user.email, user.uid,
                                        user.providerId, firstName, lastName, listOf(firebase.instanceId.id)
                                    )
                                    firebase.database.collection("users")
                                        .document(userId)
                                        .set(newUser)
                                        .addOnCompleteListener { userResult ->
                                            if (userResult.isSuccessful) {
                                                val account = LedgirAccount(
                                                    users = mapOf(Pair(user.uid, true))
                                                )
                                                firebase.database.collection("accounts")
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
                })
            } else {
                Toast.makeText(this, response!!.error!!.errorCode, Toast.LENGTH_LONG).show()
            }
        }
    }
}
