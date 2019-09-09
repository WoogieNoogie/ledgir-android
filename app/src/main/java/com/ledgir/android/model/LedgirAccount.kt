package com.ledgir.android.model

data class LedgirAccount(
    var availableBalance: Int = 0,
    var budgetEnabled: Boolean = false,
    var budgetCredit: Int = 0,
    var budgetEntertainment: Int = 0,
    var budgetGrocery: Int = 0,
    var budgetInsurance: Int = 0,
    var budgetOther: Int = 0,
    var budgetTransportation: Int = 0,
    var budgetUtilities: Int = 0,
    var budgetIncome: Int = 0,
    var users: Map<String, Boolean>? = null
)
