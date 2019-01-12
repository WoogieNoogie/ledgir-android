package com.ledgir.android

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

open class BaseViewModel(application: Application) : AndroidViewModel(application) {
    protected val job = Job()

    protected val scope = CoroutineScope(job + Dispatchers.IO)

    override fun onCleared() {
        super.onCleared()
        job.cancel()
    }
}