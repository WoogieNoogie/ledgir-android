package com.ledgir.android.model

data class Account(
    var budgetCredit: Int? = null,
    var expensesCredit: Int? = null,
    var budgetEntertainment: Int? = null,
    var expensesEntertainment: Int? = null,
    var budgetGrocery: Int? = null,
    var expensesGrocery: Int? = null,
    var budgetInsurance: Int? = null,
    var expensesInsurance: Int? = null,
    var budgetOther: Int? = null,
    var expensesOther: Int? = null,
    var budgetTransportation: Int? = null,
    var expensesTransportation: Int? = null,
    var budgetUtilities: Int? = null,
    var expensesUtilities: Int? = null,
    var income: Int? = null,
    var users: Map<String, Boolean>? = null
)