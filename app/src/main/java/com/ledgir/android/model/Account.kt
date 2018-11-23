package com.ledgir.android.model

data class Account(
    var budgetCredit: Int = 0,
    var expensesCredit: Int = 0,
    var budgetEntertainment: Int = 0,
    var expensesEntertainment: Int = 0,
    var budgetGrocery: Int = 0,
    var expensesGrocery: Int = 0,
    var budgetInsurance: Int = 0,
    var expensesInsurance: Int = 0,
    var budgetOther: Int = 0,
    var expensesOther: Int = 0,
    var budgetTransportation: Int = 0,
    var expensesTransportation: Int = 0,
    var budgetUtilities: Int = 0,
    var expensesUtilities: Int = 0,
    var income: Int = 0,
    var users: Map<String, Boolean>? = null
)