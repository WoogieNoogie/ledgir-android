package com.ledgir.android.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.ledgir.android.BaseViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : BaseViewModel(application) {
    val firebaseValues = MutableLiveData<FirebaseValues>()

    init {
        scope.launch {
            setFirebaseValues(application)
        }
    }

    suspend fun setFirebaseValues(context: Context) = coroutineScope {
        val auth = async {
            FirebaseAuth.getInstance()
        }
        val database = async {
            FirebaseFirestore.getInstance()
        }
        val identifier = async {
            AdvertisingIdClient.getAdvertisingIdInfo(context)
        }
        val instanceId = async {
            FirebaseInstanceId.getInstance()
        }
        firebaseValues.postValue(
            FirebaseValues(
                auth.await(), database.await(), identifier.await(), instanceId.await()
            )
        )
    }
}

class FirebaseValues(
    val auth: FirebaseAuth,
    val database: FirebaseFirestore,
    val identifier: AdvertisingIdClient.Info,
    val instanceId: FirebaseInstanceId
)