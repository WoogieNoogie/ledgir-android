package com.ledgir.android.dashboard

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.ledgir.android.BaseViewModel
import com.ledgir.android.model.LedgirAccount
import com.ledgir.android.model.LedgirUser
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class DashboardViewModel(application: Application) : BaseViewModel(application) {
    val userData = MutableLiveData<LedgirUser>()

    val accountData = MutableLiveData<LedgirAccount>()

    init {
        scope.launch {
            val viewState =
                DashboardViewState(getFirebaseUserAsync().await(), getFirestoreAsync().await())
            viewState.database.collection("users")
                .whereEqualTo("externalId", viewState.firebaseUser.uid)
                .get()
                .addOnCompleteListener { documentTask ->
                    if (documentTask.isSuccessful) {
                        val user = documentTask.result!!.documents.first().toObject(LedgirUser::class.java)
                        userData.postValue(user)

                        viewState.database.collection("accounts")
                            .document(user!!.accountId!!)
                            .get()
                            .addOnCompleteListener { documentSnapshot ->
                                if (documentSnapshot.isSuccessful) {
                                    accountData.postValue(documentSnapshot.result!!.toObject(LedgirAccount::class.java))
                                }
                            }
                    }
                }
        }
    }

    private suspend fun getFirebaseUserAsync() = coroutineScope {
        async { FirebaseAuth.getInstance().currentUser!! }
    }

    private suspend fun getFirestoreAsync() = coroutineScope {
        async { FirebaseFirestore.getInstance() }
    }

    class DashboardViewState(
        val firebaseUser: FirebaseUser,
        val database: FirebaseFirestore
    )
}
