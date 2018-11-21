package com.ledgir.android.dashboard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ledgir.android.model.Account
import com.ledgir.android.model.User

class DashboardViewModel : ViewModel() {
    val user = FirebaseAuth.getInstance().currentUser

    val database = FirebaseFirestore.getInstance()

    val userData = MutableLiveData<User>()

    val accountData = MutableLiveData<Account>()

    init {
        Log.d("USER", user!!.uid)
        userData.observeForever { user ->
            database.collection("accounts")
                .document(user.accountId!!)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        accountData.postValue(it.result!!.toObject(Account::class.java))
                    }
                }
        }
        database.collection("users")
            .whereEqualTo("externalId", user.uid)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    userData.postValue(it.result!!.documents.first().toObject(User::class.java))
                }
            }
    }
}