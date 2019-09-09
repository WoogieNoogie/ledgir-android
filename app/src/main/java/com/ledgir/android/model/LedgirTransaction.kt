package com.ledgir.android.model

import java.util.*

data class LedgirTransaction(
    var accountId: String = "",
    var userId: String = "",
    var amount: Int = 0,
    var date: Date = Date(),
    var label: String? = null,
    var budgetCategory: String? = null,
    var notes: String? = null
)
