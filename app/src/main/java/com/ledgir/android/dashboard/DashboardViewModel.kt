package com.ledgir.android.dashboard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.ledgir.android.model.LedgirAccount
import com.ledgir.android.model.LedgirUser

class DashboardViewModel : ViewModel() {
    val user = FirebaseAuth.getInstance().currentUser

    val database = FirebaseFirestore.getInstance()

    val userData = MutableLiveData<LedgirUser>()

    val accountData = MutableLiveData<LedgirAccount>()

    init {
        Log.d("USER", user!!.uid)
        userData.observeForever { user ->
            database.collection("accounts")
                .document(user.accountId!!)
                .get()
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        accountData.postValue(it.result!!.toObject(LedgirAccount::class.java))
                    }
                }
        }
        database.collection("users")
            .whereEqualTo("externalId", user.uid)
            .get()
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    userData.postValue(it.result!!.documents.first().toObject(LedgirUser::class.java))
                }
            }
    }
}